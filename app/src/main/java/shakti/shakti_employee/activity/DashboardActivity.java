package shakti.shakti_employee.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shakti.shakti_employee.BuildConfig;
import shakti.shakti_employee.R;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.fragment.AttendanceFragment;
import shakti.shakti_employee.fragment.HomeFragment;
import shakti.shakti_employee.fragment.LeaveFragment;
import shakti.shakti_employee.fragment.LeaveRequestFragment;
import shakti.shakti_employee.fragment.LocationUpdatesService;
import shakti.shakti_employee.fragment.OfficialDutyFragment;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.AndroidService;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SAPWebService;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.other.SyncDataService;
import shakti.shakti_employee.other.TimeService;

public class DashboardActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    protected static final String TAG = "LocationOnOff";
    //private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME;
    public static int lastFrPosition = 0;
    DatabaseHelper dataHelper;
    ProgressDialog progressBar;
    SAPWebService con = null;
    SharedPreferences pref;
    DatabaseHelper databaseHelper;
    String newVersion = null;
    PackageInfo pInfo = null;
    TextView leave_notification, od_notification;
    String usrid = "", house_no = "", line1 = "", line2 = "", city = "", district = "", postal_code = "";
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView txtName;
    private TextView app_version;
    private Toolbar toolbar;
    private Handler progressBarHandler = new Handler();
    private int progressBarStatus = 0;
    private long fileSize = 0;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private LoggedInUser userModel;
    private Context mContext;
    private LeaveRequestFragment mBaseFragment;
    private boolean mBound = false;
    private LocationUpdatesService mService = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        mContext = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Check for Permissions For Android GE 6.0

        if (checkAndRequestPermissions()) {

            pref = CartSharedPreferences.createObject(DashboardActivity.this);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("pernr", getIntent().getStringExtra("pernr"));
            editor.commit();


            mHandler = new Handler();

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            /*fab = (FloatingActionButton) findViewById(R.id.fab);*/

            mContext = this;
            userModel = new LoggedInUser(mContext);

            // Navigation view header
            navHeader = navigationView.getHeaderView(0);
            /*txtName = (TextView) navHeader.findViewById(R.id.name);*/
            txtName = (TextView) navHeader.findViewById(R.id.name);
            app_version = (TextView) navHeader.findViewById(R.id.app_version);


            con = new SAPWebService();

            databaseHelper = new DatabaseHelper(DashboardActivity.this);


            //checkAppVersion();

            if (databaseHelper.getemp()) {

                if (userModel.mob_atnd.equalsIgnoreCase("Y")) {
                    // After successful data syncing Enable GPS Tracking .
                    tracking_enabled();

                }

                if (!isTrackingServiceRunning()) {
                    Log.d("tracking_service", "Tracking Service Started");
                    startService(new Intent(DashboardActivity.this, TimeService.class));
                }


            } else {
                downloadDataFromSap();
            }


            // load toolbar titles from string resources
            activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

            // load nav menu header data
            loadNavHeader();

            // initializing navigation menu
            setUpNavigationView();

            if (savedInstanceState == null) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
            }



        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        /*txtName.setText("Rohit Patidar");*/
        txtName.setText("Welcome," + userModel.ename);

        app_version.setText("Version," + BuildConfig.VERSION_NAME);
        /*txtWebsite.setText("www.shaktipumps.com");*/


    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            /*toggleFab();*/
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        /*// show or hide the fab button
        toggleFab();*/


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // photos
                AttendanceFragment attendanceFragment = new AttendanceFragment();
                return attendanceFragment;
            case 2:
                // movies fragment
                LeaveFragment leaveFragment = new LeaveFragment();
                return leaveFragment;
/*                LeaveFragment leaveFragment = new LeaveFragment();
                return leaveFragment;*/
            case 3:
                // notifications fragment
                OfficialDutyFragment officialDutyFragment = new OfficialDutyFragment();
                return officialDutyFragment;

            case 4:
                // settings fragment
                Intent intent = new Intent(DashboardActivity.this, PayslipActivity.class);
                startActivity(intent);

//                PayslipFragment payslipFragment = new PayslipFragment();
//                return payslipFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_attendance:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        break;
                    case R.id.nav_leave:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_od:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_payslip:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;

                    case R.id.nav_personal_info_edit:

                        if (CustomUtility.isInternetOn(mContext)) {

                            drawer.closeDrawers();

                            new Thread(new Runnable() {
                                public void run() {

                                    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
                                    param.add(new BasicNameValuePair("pernr", userModel.uid));

                                    try {

                                        String obj = CustomHttpClient.executeHttpPost1(SapUrl.employee_info_edit, param);

                                        Log.d("emp_obj", obj);

                                        if (obj != null) {

//                                            databaseHelper.deleteempinfo();

                                            JSONObject jsonObj = new JSONObject(obj);
                                            JSONArray ja = jsonObj.getJSONArray("emp_edit");
//                                            databaseHelper.deleteempinfo();

                                            for (int i = 0; i < ja.length(); i++) {

                                                JSONObject jo = ja.getJSONObject(i);


                                                if (i == 0) {

                                                    usrid = jo.getString("usrid");
                                                    Log.d("edit_email_check1", usrid);
                                                }

                                                if (i == 1) {

                                                    house_no = jo.getString("hsnmr");
                                                    line1 = jo.getString("stras");
                                                    line2 = jo.getString("locat");
                                                    city = jo.getString("ort01");
                                                    district = jo.getString("ort02");
                                                    postal_code = jo.getString("pstlz");

                                                    Log.d("edit_email_check2", house_no + "---" + line1 + "---" + line2 + "---" + city + "----" +
                                                            district + "---" + postal_code);

                                                }
                                            }

                                        }



                                    } catch (Exception e) {
                                    }


                                    /*progressBar.cancel();
                                    progressBar.dismiss();*/

                                    Intent intent = new Intent(DashboardActivity.this, PersonalInfoEditActivity.class);
                                    intent.putExtra("USRID", usrid);
                                    intent.putExtra("HOUSE_NO", house_no);
                                    intent.putExtra("LINE1", line1);
                                    intent.putExtra("LINE2", line2);
                                    intent.putExtra("CITY", city);
                                    intent.putExtra("DISTRICT", district);
                                    intent.putExtra("POSTAL_CODE", postal_code);

                                    startActivity(intent);


                                }
                            }).start();

                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                        break;

                    case R.id.nav_personal_info:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(DashboardActivity.this, PersonalInfoActivity.class));
                        drawer.closeDrawers();
                        return true;
//
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            if (CustomUtility.isInternetOn(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();

                // Delete Data from DB

                dataHelper = new DatabaseHelper(DashboardActivity.this);
                dataHelper.deleteLoginDetail();
                dataHelper.deletependingleave();
                dataHelper.deleteActiveEmployee();
                dataHelper.deleteleaveBalance();
                dataHelper.deletependingleave();
                dataHelper.deletependingod();
                dataHelper.deleteattendance();
                dataHelper.deleteleave();
                dataHelper.deleteod();
                dataHelper.deleteempinfo();
                dataHelper.deletemarkattendance();
                dataHelper.deleteEmployeeGPSActivity();
                dataHelper.deleteDomTravelData();
                dataHelper.deleteExpTravelData();
                dataHelper.deleteTaskCompleted();
                dataHelper.deleteLocalconvenienceDetail();
                CustomUtility.setSharedPreference(mContext, "localconvenience", "0");
                // Goto Login Activity
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            } else {
                Toast.makeText(DashboardActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }

        // Sync Data Manually

        if (id == R.id.action_sync) {
            Toast.makeText(getApplicationContext(), "Downloading Data!!", Toast.LENGTH_LONG).show();
            con = new SAPWebService();

            //SyncDataFromSap();
            downloadDataFromSap();

            return true;
        }


        // Sync Offline Data Manually
        if (userModel.mob_atnd.equalsIgnoreCase("Y")) {

            if (id == R.id.action_sync_offline) {
                Toast.makeText(getApplicationContext(), "Synchronizing Offline Data!!", Toast.LENGTH_LONG).show();
                con = new SAPWebService();
                SyncAttendanceInBackground();
                Toast.makeText(getApplicationContext(), "Offline Data Sync Successfully", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Data already send to Server ", Toast.LENGTH_LONG).show();

        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void switchFragment(int position) {
        boolean addToBack = false;
        lastFrPosition = position;
        Log.d("pos", "" + position);
        mBaseFragment = null;
        switch (position) {
            case 1:
                break;

            case 2:
                break;

        }
    }


    public void downloadDataFromSap() {
        // creating progress bar dialog
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        // progressBar.setCancelable(true);
        progressBar.setMessage("Downloading Data from SAP...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressBar.show();
        //reset progress bar and file size status
        progressBarStatus = 0;
        fileSize = 0;

        new Thread(() -> {


            while (progressBarStatus < 100)
            {
                try {
                    //Get All Data
                    progressBarStatus = con.getActiveEmployee(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getLeaveBalance(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getAttendanceEmp(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getInfoEmp(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                   /* progressBarStatus = con.getPendingLeaveForApp(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));*/

                    progressBarStatus = con.getPendingLeave(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getPendingOD(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getLeaveEmployee(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getODEmployee(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getPendingTask(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getCountry(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getRegion(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getDistrict(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getTehsil(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getTaxcode(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getExpenses(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getCurrency(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    // progressBarStatus = con.getAllData(DashboardActivity.this, userModel.uid);




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                // close the progress bar dialog
                progressBar.dismiss();

                if (!isTrackingServiceRunning()) {
                    Log.d("tracking_service", "Tracking Service Started");
                    startService(new Intent(DashboardActivity.this, TimeService.class));
                }


        }).start();

    }



    public void SyncAttendanceInBackground() {
        Intent i = new Intent(this, SyncDataService.class);
        this.startService(i);
    }


    public void tracking_enabled() {


//      // Make sure that GPS is enabled on the device
//      LocationManager mlocManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//      boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//      if(!enabled) {
//          Log.d("gps", "GPS OFF");
//          enableLoc();
//      }

        if (!isNotificationServiceRunning()) {
            // call service for gps notification
            startService(new Intent(DashboardActivity.this, AndroidService.class));
        }
    }

    private boolean isDownloadServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("other.TimeServiceDownloadData".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private boolean isNotificationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("other.AndroidService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isTrackingServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("other.TimeService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private boolean checkAndRequestPermissions() {
        int permissionNetworkState = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE);
        int permissionCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionInternet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int permissionStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionNetworkState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionInternet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted


            } else {
                // Permission Denied

                DashboardActivity.this.finish();
                System.exit(0);

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressBar!=null) {
            progressBar.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressBar!=null) {
            progressBar.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (progressBar!=null) {
            progressBar.cancel();
        }
    }
}