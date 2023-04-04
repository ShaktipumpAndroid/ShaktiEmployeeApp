package shakti.shakti_employee.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.fragment.AttendanceFragment;
import shakti.shakti_employee.fragment.HODTripApprovalFragment;
import shakti.shakti_employee.fragment.HomeFragment;
import shakti.shakti_employee.fragment.LeaveFragment;
import shakti.shakti_employee.fragment.OfficialDutyFragment;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;

public class HODApprovalActivity extends AppCompatActivity {

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static int navItemIndex;
    public static String CURRENT_TAG = TAG_HOME;
    DatabaseHelper dataHelper = null;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView txtName;
    private Toolbar toolbar;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private LoggedInUser userModel;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        mContext = this;
        userModel = new LoggedInUser(mContext);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        /*txtName = (TextView) navHeader.findViewById(R.id.name);*/
        txtName = (TextView) navHeader.findViewById(R.id.name);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

      /*  if (!isDownloadServiceRunning()) {
            //Toast.makeText(DashboardActivity.this, "Download Data", Toast.LENGTH_SHORT).show();
            startService(new Intent(HODApprovalActivity.this, TimeServiceDownloadData.class));

        }*/

        /*  getSupportActionBar().setTitle("Approval Report");*/

        Fragment fragment = new HODTripApprovalFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();

    }

    private void loadNavHeader() {
        txtName.setText("Welcome," + userModel.ename);
    }


    private void loadHomeFragment() {
        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        drawer.closeDrawers();
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
            case 3:
                // notifications fragment
                OfficialDutyFragment officialDutyFragment = new OfficialDutyFragment();
                return officialDutyFragment;

            case 4:
                // settings fragment
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
//                        Toast.makeText(OdApproveActivity.this,"Leave Request Activity", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HODApprovalActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_attendance:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        loadHomeFragment();
                        break;
                    case R.id.nav_leave:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        loadHomeFragment();
                        break;
                    case R.id.nav_od:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        loadHomeFragment();
                        break;
                    case R.id.nav_payslip:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        loadHomeFragment();
                        break;
                    default:
//                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

//                loadHomeFragment();

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


        finish();


//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawers();
//            return;
//        }
//
//        // This code loads home fragment when back key is pressed
//        // when user is in other fragment than home
//        if (shouldLoadHomeFragOnBackPress) {
//            // checking if user is on other navigation menu
//            // rather than home
//            if (navItemIndex != 0) {
//                navItemIndex = 0;
//                CURRENT_TAG = TAG_HOME;
//                loadHomeFragment();
//                return;
//            }
//        }
//
//        super.onBackPressed();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//
//        // show menu only when home fragment is selected
//        if (navItemIndex == 0) {
//            getMenuInflater().inflate(R.menu.main, menu);
//        }
//
//        // when fragment is notifications, load the menu created for notifications
//        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
//        }
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            if (CustomUtility.isInternetOn(mContext)) {
                Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();

                // Delete Data from DB

                dataHelper = new DatabaseHelper(HODApprovalActivity.this);
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

                // Goto Login Activity
                Intent intent = new Intent(HODApprovalActivity.this, LoginActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            } else {
                Toast.makeText(HODApprovalActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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


}
