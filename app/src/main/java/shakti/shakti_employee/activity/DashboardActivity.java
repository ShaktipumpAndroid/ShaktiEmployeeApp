package shakti.shakti_employee.activity;

import static shakti.shakti_employee.other.AndroidService.isNotificationServiceRunning;
import static shakti.shakti_employee.other.TimeService.isTimeServiceRunning;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import shakti.shakti_employee.BuildConfig;
import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.fragment.AttendanceFragment;
import shakti.shakti_employee.fragment.HomeFragment;
import shakti.shakti_employee.fragment.LeaveFragment;
import shakti.shakti_employee.fragment.LeaveRequestFragment;
import shakti.shakti_employee.fragment.OfficialDutyFragment;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.AndroidService;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SAPWebService;
import shakti.shakti_employee.other.SyncDataService;
import shakti.shakti_employee.other.SyncDataToSAP_New;
import shakti.shakti_employee.other.TimeService;
import shakti.shakti_employee.services.LocationUpdateService;
import shakti.shakti_employee.utility.Constant;

public class DashboardActivity extends BaseActivity  {

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
    private final Handler progressBarHandler = new Handler();
    private int progressBarStatus = 0;
    private long fileSize = 0;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    // flag to load home fragment when user presses back key
    private final boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private LoggedInUser userModel;
    private Context mContext;
    private LeaveRequestFragment mBaseFragment;
    private final boolean mBound = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        mContext = this;
        toolbar = findViewById(R.id.toolbar);
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

        //     if (checkAndRequestPermissions()) {

        pref = CartSharedPreferences.createObject(DashboardActivity.this);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("pernr", getIntent().getStringExtra("pernr"));
        editor.commit();


        mHandler = new Handler();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        /*fab =  findViewById(R.id.fab);*/

        mContext = this;
        userModel = new LoggedInUser(mContext);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        /*txtName = ) navHeader.findViewById(R.id.name);*/
        txtName = navHeader.findViewById(R.id.name);
        app_version = navHeader.findViewById(R.id.app_version);


        con = new SAPWebService();

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

        databaseHelper = new DatabaseHelper(DashboardActivity.this);

        if (databaseHelper.getemp()) {

            if (userModel.mob_atnd.equalsIgnoreCase("Y")) {
                // After successful data syncing Enable GPS Tracking .
                tracking_enabled();

            }

            if (!isTimeServiceRunning) {
                Log.d("tracking_service", "Tracking Service Started");
                startService(new Intent(DashboardActivity.this, TimeService.class));
            }
        } else {
            if(CustomUtility.isOnline(getApplicationContext())) {
                downloadDataFromSap();
            }else {
                CustomUtility.ShowToast(getResources().getString(R.string.ConnectToInternet),getApplicationContext());
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
    }


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

        Runnable mPendingRunnable = () -> {
            // update the main content by replacing fragments
            Fragment fragment = getHomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
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
                menuItem.setChecked(!menuItem.isChecked());
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
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            if (CustomUtility.isInternetOn(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
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
                dataHelper.deleteWayPointsDetail();
                dataHelper.deleteVendorcodeData();
                dataHelper.deleteOpenGatePassData();
                stopService(new Intent(getApplicationContext(), TimeService.class));
                stopService(new Intent(getApplicationContext(), SyncDataService.class));
                stopService(new Intent(getApplicationContext(), AndroidService.class));
                stopService(new Intent(getApplicationContext(), LocationUpdateService.class));

                CustomUtility.setSharedPreference(mContext, Constant.LocalConveyance, "0");
                CustomUtility.clearSharedPreference(mContext);
                // Goto Login Activity
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            } else {
                Toast.makeText(DashboardActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }

        if (id == R.id.action_sync) {
            Toast.makeText(getApplicationContext(), "Downloading Data!!", Toast.LENGTH_LONG).show();
            con = new SAPWebService();

            //SyncDataFromSap();
            if(CustomUtility.isOnline(getApplicationContext())) {
                downloadDataFromSap();
            }else {
                CustomUtility.ShowToast(getResources().getString(R.string.ConnectToInternet),getApplicationContext());
            }

            return true;
        }

        if (id == R.id.action_sync_offline) {
            if(CustomUtility.isOnline(getApplicationContext())) {
            new SyncDataToSAP_New().SendAllDataToSAP(getApplicationContext());
            }else {
                CustomUtility.ShowToast(getResources().getString(R.string.ConnectToInternet),getApplicationContext());
            }
                return true;
            }

        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


    public void downloadDataFromSap() {
        // creating progress bar dialog
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        // progressBar.setCancelable(true);
        progressBar.setMessage("Downloading Data from Server...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(5);
        progressBar.setMax(100);
        progressBar.show();
        //reset progress bar and file size status
        progressBarStatus = 5;
        fileSize = 0;

        new Thread(() -> {


            while (progressBarStatus < 100)
            {
                progressBarHandler.post(() -> progressBar.setProgress(2));

                try {
                    //Get All Data
                    progressBarStatus = con.getActiveEmployee(DashboardActivity.this,userModel.uid);
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));

                    progressBarStatus = con.getGatePass(DashboardActivity.this,userModel.uid);
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

                    progressBarStatus = con.getVendorCode(DashboardActivity.this,userModel.uid);
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

                if (!isTimeServiceRunning) {
                    Log.d("tracking_service", "Tracking Service Started");
                    startService(new Intent(DashboardActivity.this, TimeService.class));
                }


        }).start();

    }




    public void tracking_enabled() {

        if (!isNotificationServiceRunning) {
            // call service for gps notification
            startService(new Intent(DashboardActivity.this, AndroidService.class));
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