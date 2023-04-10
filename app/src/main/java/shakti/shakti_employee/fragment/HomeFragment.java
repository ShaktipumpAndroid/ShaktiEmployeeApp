package shakti.shakti_employee.fragment;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.AttendanceActivity;
import shakti.shakti_employee.activity.CheckInvkActivity;
import shakti.shakti_employee.activity.CompleteTaskActivity;
import shakti.shakti_employee.activity.CreateTaskActivity;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.activity.DomesticTravelExpensesActivity;
import shakti.shakti_employee.activity.ExportTravelExpensesActivity;
import shakti.shakti_employee.activity.GatepassApproveActivity;
import shakti.shakti_employee.activity.GpRequestActivity;
import shakti.shakti_employee.activity.HODApprovalActivity;
import shakti.shakti_employee.activity.LeaveApproveActivity;
import shakti.shakti_employee.activity.LeaveRequestActivity;
import shakti.shakti_employee.activity.OdApproveActivity;
import shakti.shakti_employee.activity.OdRequestActivity;
import shakti.shakti_employee.activity.OfflineDataConveyance;
import shakti.shakti_employee.activity.TravelExpenseReportActivity;
import shakti.shakti_employee.activity.webViewActivity;
import shakti.shakti_employee.bean.AttendanceBean;
import shakti.shakti_employee.bean.LocalConvenienceBean;
import shakti.shakti_employee.bean.LocalConvenienceBean1;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.GPSTracker;
import shakti.shakti_employee.other.SAPWebService;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.other.SyncDataService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    DashboardActivity dashboardActivity = new DashboardActivity();

    TextView act_leave_req;
    TextView act_leave_app;
    TextView act_od_req;
    TextView act_od_app;
    TextView act_gp_req;
    TextView txtCheckINID;
    TextView txtCheckOutID;
    TextView act_gp_app;
    TextView gp_notification;
    TextView tv_dom_travel;
    TextView tv_dom_rep;
    TextView tv_exp_rep;
    TextView tv_exp_travel, markAttendanceBar;
    TextView leave_notification, od_notification, pending_task_notification;
    Context context;
    private static HomeFragment instance;
    // For Leave Balance
    DatabaseHelper dataHelper;
    String leavetype;
    String obj_leave_balance;
    // For Leave pending for approval
    String fullAddress = null;
    String fullAddress1 = null;
    /*String distance = null;*/
    String distance = null;
    String distance1 = null;
    String latlng = null;
    String obj_pending_leave;
    String travel_create;
    GPSTracker gps;
    String KEY_LEV_NO;
    String LEV_TYP;
    String ENAME;
    String HORO;
    String LEV_FRM;
    String LEV_TO;
    String REASON;
    String CHRG_NAME1;
    String CHRG_NAME2;
    String CHRG_NAME3;
    String CHRG_NAME4;
    String DIRECT_INDIRECT;
    String key_od_no;
    String od_horo;
    String od_ename;
    String od_work_status;
    String od_frm;
    String od_to;
    String visit_place;
    String purpose1;
    String purpose2;
    String purpose3;
    String remark;
    String od_direct_indirect;
    String from_lat;
    String from_lng;
    String to_lat;
    String to_lng;
    String id;
    CustomUtility customutility = null;
    AttendanceBean attendanceBean;
    String Attendance_Mark = null, latLong;
    SAPWebService con = null;
    View view, view1;
    LinearLayout travel;

    TextView tv_travel_txt;
    TextView tv_create_task, tv_complete_task, tv_web_view, tv_create_attendance;
    //    private ProgressDialog progressDialog;
    ProgressDialog progressBar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mTravel, mHod;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog progressDialog;
    private LoggedInUser userModel;
    //private Context mContext;
    private Uri fileUri; // file url to store image
    private final int progressBarStatus = 0;
    private final Handler progressBarHandler = new Handler();
    private final ArrayList<String> permissionsRejected = new ArrayList<>();

    LocalConvenienceBean localConvenienceBean;
    LocationManager locationManager;


    String current_start_date, current_end_date, current_start_time, current_end_time;


    private Location location;
    private TextView locationTv;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 1000, FASTEST_INTERVAL = 1000; // = 5 seconds
    // lists for permissions
    private ArrayList<String> permissionsToRequest;
    private final ArrayList<String> permissions = new ArrayList<>();
    TextView start_travel, end_travel, convey_offline_data;
    // integer for permissions results request
    private static final int ALL_PERMISSIONS_RESULT = 1011;
    FusedLocationProviderClient fusedLocationClient;
    private MyReceiver myReceiver;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(getActivity(), mString, Toast.LENGTH_LONG).show();
        }
    };
    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;
    // Tracks the bound state of the service.
    private boolean mBound = false;
    String strtlatlng = null;
    String fromlatlng = null;
    String date = null;
    String time = null;

    FusedLocationProviderClient fusedLocationProviderClient;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                SapUrl.IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
//                Log.d(TAG, "Oops! Failed create "
//                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }


        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }


        return mediaFile;
    }

    public static HomeFragment GetInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        context = this.getActivity();


        if (context != null) {
            userModel = new LoggedInUser(context);
        }
        customutility = new CustomUtility();
        progressDialog = new ProgressDialog(context);

        mTravel = userModel.travel;
        mHod = userModel.hod;
        dataHelper = new DatabaseHelper(context);


        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        // we add permissions we need to request location of the users
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.
                        toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (!checkPlayServices()) {
            Toast.makeText(dashboardActivity, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    public void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            context.unbindService(mServiceConnection);
            mBound = false;
        }
       /* PreferenceManager.getDefaultSharedPreferences(context)
                .unregisterOnSharedPreferenceChangeListener(this);*/
        super.onStop();

        if (progressBar!=null) {
            progressBar.cancel();
        }
        if (progressDialog!=null) {
            progressDialog.cancel();
        }
    }

  /*  @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }
    }

    private void setButtonsState(boolean requestingLocationUpdates) {
        if (requestingLocationUpdates) {
            start_travel.setEnabled(false);
            end_travel.setEnabled(true);
        } else {
            start_travel.setEnabled(true);
            end_travel.setEnabled(false);
        }
    }*/

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                //Toast.makeText(MainActivity.this, Utils.getLocationText(location),
                //      Toast.LENGTH_SHORT).show();
            }
        }
    }


    @SuppressLint("WrongConstant")
    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        // Restore the state of the buttons when the activity (re)launches.
        //setButtonsState(Utils.requestingLocationUpdates(context));

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        context.bindService(new Intent(context, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity) context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                getActivity().finish();
            }

            return false;
        }

        return true;
    }



    @SuppressLint("UseRequireInsteadOfGet")
    public void startLocationUpdates() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(5000);
        locationRequest.setInterval(10 * 1000);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                final Double[] lat = new Double[1];
                final Double[] lng = new Double[1];
                from_lat = "";
                from_lng = "";
                to_lat = "";
                to_lng = "";
                fullAddress = "";
                fullAddress1 = "";

                try{

                    current_start_date = customutility.getCurrentDate1();
                    current_start_time = customutility.getCurrentTime1();

                    if(location != null)
                    {
                        from_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.######").format(location.getLatitude())));
                        from_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.######").format(location.getLongitude())));
                        lat[0] = location.getLatitude();
                        lng[0] = location.getLongitude();
                    }
                    else{
                        LocationCallback mLocationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult == null) {
                                    return;
                                }
                                for (Location location : locationResult.getLocations()) {
                                    if (location != null) {
                                        //TODO: UI updates.
                                        from_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.######").format(location.getLatitude())));
                                        from_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.######").format(location.getLongitude())));
                                        lat[0] = location.getLatitude();
                                        lng[0] = location.getLongitude();

                                    }
                                }
                            }
                        };

                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(locationRequest, mLocationCallback, null);
                    }



                    progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait !");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something here

                            if(!TextUtils.isEmpty(from_lat) && !TextUtils.isEmpty(from_lng)) {

                                if (progressDialog != null)
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }

                                final Dialog dialog = new Dialog(context);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setCancelable(false);
                                dialog.setContentView(R.layout.custom_dialog1);
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                dialog.getWindow().setAttributes(lp);


                                final TextInputEditText etlat = dialog.findViewById(R.id.tiet_lat);
                                final TextInputEditText etlng = dialog.findViewById(R.id.tiet_lng);
                                final TextInputEditText etadd = dialog.findViewById(R.id.tiet_add);
                                final TextView ettxt1 = dialog.findViewById(R.id.txt1);
                                final TextView ettxt2 = dialog.findViewById(R.id.txt2);
                                final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                                final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);


                                Geocoder geo = new Geocoder(context.getApplicationContext(), Locale.getDefault());
                                List<Address> addresses = null;
                                if (location != null) {
                                    try {
                                        addresses = geo.getFromLocation(lat[0], lng[0], 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (addresses != null) {
                                    if (addresses.isEmpty()) {
                                        etadd.setText("Please try Again, Waiting for Location");
                                    } else {
                                        etadd.setText(addresses.get(0).getAddressLine(0));
                                    }
                                }


                                etlat.setText(from_lat);
                                etlng.setText(from_lng);

                                ettxt1.setText("Current Location");
                                ettxt2.setText(Html.fromHtml(getString(R.string.confirm)));

                                etcncl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                etconfm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        //dataHelper.deleteLocalconvenienceDetail();

                                        if (CustomUtility.isInternetOn(context)) {

                                            LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(userModel.uid,
                                                    new CustomUtility().getCurrentDate(),
                                                    "",
                                                    new CustomUtility().getCurrentTime(),
                                                    "",
                                                    from_lng+","+from_lat+";",
                                                    "",
                                                    from_lat+","+from_lng,
                                                    "",
                                                    "",
                                                    "",
                                                    "");

                                            dataHelper.insertLocalconvenienceData(localConvenienceBean);

                                            CustomUtility.setSharedPreference(context, "localconvenience", "1");
                                            changeButtonVisibility(false, 0.5f, start_travel);
                                            changeButtonVisibility(true, 1f, end_travel);

                                            Toast.makeText(getActivity(), "Your Journey will be Started...", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                                            LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(userModel.uid,
                                                    new CustomUtility().getCurrentDate(),
                                                    "",
                                                    new CustomUtility().getCurrentDate(),
                                                    "",
                                                    from_lng+","+from_lat+";",
                                                    "",
                                                    from_lat+","+from_lng,
                                                    "",
                                                    "",
                                                    "",
                                                    "");

                                            dataHelper.insertLocalconvenienceData(localConvenienceBean);

                                            CustomUtility.setSharedPreference(context, "localconvenience", "1");
                                            changeButtonVisibility(false, 0.5f, start_travel);
                                            changeButtonVisibility(true, 1f, end_travel);

                                            Toast.makeText(getActivity(), "Your Journey will be Started...", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        }
                                        mService.requestLocationUpdates();


                                    }
                                });

                                dialog.show();
                            }
                            else{

                                if (progressDialog != null)
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }

                                Toast.makeText(context, "Please wait for your current location.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 2000);

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    if (progressDialog != null)
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                }

            }
        });

    }

    @SuppressLint("UseRequireInsteadOfGet")
    public void startLocationUpdates1() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(5000);
        locationRequest.setInterval(10 * 1000);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                from_lat = " ";
                from_lng = " ";
                to_lat = " ";
                to_lng = " ";
                fullAddress = "";
                fullAddress1 = "";

                try
                {

                    localConvenienceBean = dataHelper.getLocalConvinienceData();
                    current_start_date = localConvenienceBean.getBegda();
                    current_start_time = localConvenienceBean.getFrom_time();

                    current_end_date = customutility.getCurrentDate1();
                    current_end_time = customutility.getCurrentTime1();

                  /*  from_lat =  localConvenienceBean.getFrom_lat();
                    from_lng = localConvenienceBean.getFrom_lng();*/

                   /* to_lat = String.valueOf(location.getLatitude());
                    to_lng = String.valueOf(location.getLongitude());*/



                    if(location != null)
                    {
                        to_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.######").format(location.getLatitude())));
                        to_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.######").format(location.getLongitude())));
                    }
                    else{
                        LocationCallback mLocationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult == null) {
                                    return;
                                }
                                for (Location location : locationResult.getLocations()) {
                                    if (location != null) {
                                        //TODO: UI updates.
                                        to_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.######").format(location.getLatitude())));
                                        to_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.######").format(location.getLongitude())));

                                    }
                                }
                            }
                        };

                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(locationRequest, mLocationCallback, null);
                    }


                }
                catch(NumberFormatException e)
                {
                    e.printStackTrace();
                }





                if(CustomUtility.isInternetOn(context)) {

                    ArrayList<LocalConvenienceBean1> localConvenienceBean1 = new ArrayList<LocalConvenienceBean1>();
                    localConvenienceBean1 = dataHelper.getLocalConvience(context, userModel.uid);


                    for (int i = 0; i < localConvenienceBean1.size(); i++) {
                        strtlatlng = localConvenienceBean1.get(i).getFrom_lng();

                        date = localConvenienceBean1.get(i).getBegda();
                        time = localConvenienceBean1.get(i).getFrom_time();
                        latlng = localConvenienceBean1.get(i).getFrom_lat();
                        distance = localConvenienceBean1.get(i).getDistance();


                    }
                    String[] separated= strtlatlng.split(",");
                    Geocoder geo = new Geocoder(context.getApplicationContext(), Locale.getDefault());
                    List<Address> addresses1 = null;
                    List<Address> addresses2 = null;
                    if (separated[0] != null && separated[1] != null) {
                        try {
                            addresses1 = geo.getFromLocation(Double.parseDouble(separated[0]), Double.parseDouble(separated[1]), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (addresses1 != null) {
                        if (addresses1.isEmpty()) {
                            fullAddress = "Please try Again, Waiting for Location";
                        } else {
                            fullAddress = addresses1.get(0).getAddressLine(0);
                        }
                    }


                    if (to_lat != null && to_lng != null) {
                        try {
                            addresses2 = geo.getFromLocation(Double.parseDouble(to_lat), Double.parseDouble(to_lng), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (addresses2 != null) {
                        if (addresses2.isEmpty()) {
                            fullAddress1 = "Please try Again, Waiting for Location";
                        } else {
                            fullAddress1 = addresses2.get(0).getAddressLine(0);
                        }
                    }

              /*  if(!TextUtils.isEmpty(distance)) {
                     localConvenienceBean = new LocalConvenienceBean(userModel.uid, date,
                            new CustomUtility().getCurrentDate(),
                            time,
                            new CustomUtility().getCurrentTime(),
                            latlng + to_lng + "," + to_lat + "?",
                            "",
                            strtlatlng,
                            to_lat + "," + to_lng,
                            "",
                            "",
                            distance);
                }
                else{
                     localConvenienceBean = new LocalConvenienceBean(userModel.uid, date,
                            new CustomUtility().getCurrentDate(),
                            time,
                            new CustomUtility().getCurrentTime(),
                            latlng + to_lng + "," + to_lat + "?",
                            "",
                            strtlatlng,
                            to_lat + "," + to_lng,
                            "",
                            "",
                            "");
                }

                    dataHelper.updateLocalconvenienceData(localConvenienceBean, userModel.uid, new CustomUtility().getCurrentDate());
*/
                    progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait !");

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something here
                            //getDistanceInfo(from_lat,from_lng, to_lat,to_lng);
                           /* if(TextUtils.isEmpty(distance))
                            {*/
                            ArrayList<LocalConvenienceBean1> localConvenienceBean1 = new ArrayList<LocalConvenienceBean1>();
                            localConvenienceBean1 = dataHelper.getLocalConvience(context, userModel.uid);


                            for (int i = 0; i < localConvenienceBean1.size(); i++) {
                                strtlatlng = localConvenienceBean1.get(i).getFrom_lng();

                                date = localConvenienceBean1.get(i).getBegda();
                                time = localConvenienceBean1.get(i).getFrom_time();
                                latlng = localConvenienceBean1.get(i).getFrom_lat();

                            }

                            localConvenienceBean = new LocalConvenienceBean(userModel.uid, date,
                                    new CustomUtility().getCurrentDate(),
                                    time,
                                    new CustomUtility().getCurrentTime(),
                                    latlng + to_lng + "," + to_lat+";" ,
                                    "",
                                    strtlatlng,
                                    to_lat + "," + to_lng,
                                    "",
                                    "",
                                    "");
                            dataHelper.updateLocalconvenienceData(localConvenienceBean, userModel.uid, date,time);
                            new JSONAsyncTask().execute();

                        }
                    }, 2000);



                }
                else{
                    Toast.makeText(context, "Please Connect to Internet...,Your Data is Saved to the Offline Mode.", Toast.LENGTH_SHORT).show();
                    ArrayList<LocalConvenienceBean1> localConvenienceBean1 = new ArrayList<LocalConvenienceBean1>();
                    localConvenienceBean1 = dataHelper.getLocalConvience(context, userModel.uid);


                    for (int i = 0; i < localConvenienceBean1.size(); i++) {
                        strtlatlng = localConvenienceBean1.get(i).getFrom_lng();
                        date = localConvenienceBean1.get(i).getBegda();
                        time = localConvenienceBean1.get(i).getFrom_time();
                        latlng = localConvenienceBean1.get(i).getFrom_lat();
                        distance = localConvenienceBean1.get(i).getDistance();
                    }

                         localConvenienceBean = new LocalConvenienceBean(userModel.uid, date,
                                new CustomUtility().getCurrentDate(),
                                time,
                                new CustomUtility().getCurrentTime(),
                                latlng + to_lng + "," + to_lat+";",
                                "",
                                strtlatlng,
                                to_lat + "," + to_lng,
                                "",
                                "",
                                "");

                    dataHelper.updateLocalconvenienceData(localConvenienceBean, userModel.uid, date,time);

                    mService.removeLocationUpdates();
                    CustomUtility.setSharedPreference(context,"localconvenience","0");
                    changeButtonVisibility(false, 0.5f, end_travel);
                    changeButtonVisibility(true, 1f, start_travel);

                }

            }
        });

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        instance = HomeFragment.this;

        view1 = v.findViewById(R.id.view1);
        tv_travel_txt = v.findViewById(R.id.tv_travel_txt);
        travel = v.findViewById(R.id.travel);

        fusedLocationProviderClient = getFusedLocationProviderClient(context);

        if (mTravel.equalsIgnoreCase("Y")) {
            view1.setVisibility(View.GONE);
            tv_travel_txt.setVisibility(View.GONE);
            travel.setVisibility(View.GONE);

        } else {
            view1.setVisibility(View.GONE);
            tv_travel_txt.setVisibility(View.GONE);
            travel.setVisibility(View.GONE);
        }


        act_leave_req = v.findViewById(R.id.act_leave_req);
        act_leave_req.setOnClickListener(this);

        act_leave_app = v.findViewById(R.id.act_leave_app);
        act_leave_app.setOnClickListener(this);

        act_od_req = v.findViewById(R.id.act_od_req);
        act_od_req.setOnClickListener(this);

        act_od_app = v.findViewById(R.id.act_od_app);
        act_od_app.setOnClickListener(this);

        act_gp_req = v.findViewById(R.id.act_gp_req);
        act_gp_req.setOnClickListener(this);

        txtCheckINID = v.findViewById(R.id.txtCheckINID);
        txtCheckINID.setOnClickListener(this);

        txtCheckOutID = v.findViewById(R.id.txtCheckOutID);
        txtCheckOutID.setOnClickListener(this);

        act_gp_app = v.findViewById(R.id.act_gp_app);
        act_gp_app.setOnClickListener(this);

        tv_create_task = v.findViewById(R.id.tv_create_task);
        tv_create_task.setOnClickListener(this);

        tv_complete_task = v.findViewById(R.id.tv_complete_task);
        tv_complete_task.setOnClickListener(this);

        tv_web_view = v.findViewById(R.id.tv_web_view);
        tv_web_view.setOnClickListener(this);

        tv_dom_travel = v.findViewById(R.id.tv_dom_travel);
        tv_exp_travel = v.findViewById(R.id.tv_exp_travel);
        tv_dom_rep = v.findViewById(R.id.tv_dom_rep);
        tv_exp_rep = v.findViewById(R.id.tv_exp_rep);

        start_travel = v.findViewById(R.id.start_travel);
        end_travel = v.findViewById(R.id.end_travel);
        convey_offline_data = v.findViewById(R.id.convey_offline_data);

        tv_dom_travel.setOnClickListener(this);
        tv_exp_travel.setOnClickListener(this);
        tv_exp_rep.setOnClickListener(this);
        tv_dom_rep.setOnClickListener(this);

        start_travel.setOnClickListener(this);
        end_travel.setOnClickListener(this);
        convey_offline_data.setOnClickListener(this);


        if (CustomUtility.getSharedPreferences(context, "localconvenience").equalsIgnoreCase("0")) {
            changeButtonVisibility(false, 0.5f, end_travel);
            changeButtonVisibility(true, 1f, start_travel);
        } else {
            changeButtonVisibility(false, 0.5f, start_travel);
            changeButtonVisibility(true, 1f, end_travel);
        }

        if (userModel.mob_atnd.equalsIgnoreCase("N")) {

            tv_create_attendance = v.findViewById(R.id.tv_create_attendance);
//            in_attendance.setOnClickListener(this);
            tv_create_attendance.setVisibility(View.GONE);

//            out_attendance = (TextView) v.findViewById(R.id.out_attendance);
////            out_attendance.setOnClickListener(this);
//            out_attendance.setVisibility(View.GONE);


            markAttendanceBar = v.findViewById(R.id.markAttendanceBar);
            markAttendanceBar.setVisibility(View.GONE);

            view = v.findViewById(R.id.view);
            view.setVisibility(View.GONE);


        } else {

            tv_create_attendance = v.findViewById(R.id.tv_create_attendance);
            tv_create_attendance.setOnClickListener(this);

        }


        customutility = new CustomUtility();

        leave_notification = v.findViewById(R.id.leave_notification);

        od_notification = v.findViewById(R.id.od_notification);

        pending_task_notification = v.findViewById(R.id.pending_task_notification);

        act_gp_app = v.findViewById(R.id.act_gp_app);

        gp_notification = v.findViewById(R.id.gp_notification);

        leave_notification.setText(Integer.toString(dataHelper.getPendinLeaveCount()));
        od_notification.setText(Integer.toString(dataHelper.getPendingOdCount()));
        pending_task_notification.setText(Integer.toString(dataHelper.getPendinTaskCount()));
        gp_notification.setText(Integer.toString(dataHelper.getPendinGatePassCount()));

        localConvenienceBean = new LocalConvenienceBean();
        localConvenienceBean = dataHelper.getLocalConvinienceData();


        return v;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public boolean isValidate() {
        if (CustomUtility.isDateTimeAutoUpdate(context)) {

        } else {
            CustomUtility.showSettingsAlert(context);
            return false;
        }
        return true;
    }


    private void buildAlertMessageNoGps() {

        if (CustomUtility.isInternetOn(context)) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Please turn on the GPRS and keep it on while traveling on tour/trip.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                           /* android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
                            // Setting Dialog Title
                            alertDialog.setTitle("Confirmation");
                            // Setting Dialog Message
                            alertDialog.setMessage("Press Confirm will start your Journey");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {*/

                            //getGpsLocation();
                            startLocationUpdates();

                            /*    }
                            });
                            // on pressing cancel button
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();*/
                            dialog.dismiss();


                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void buildAlertMessageNoGps1() {

        if (CustomUtility.isInternetOn(context)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Please turn on the GPRS and keep it on while traveling on tour/trip.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {


                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                           /* android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
                            // Setting Dialog Title
                            alertDialog.setTitle("Confirmation");
                            // Setting Dialog Message
                            alertDialog.setMessage("Press Confirm will start your Journey");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
*/
                            //getGpsLocation();
                            //startLocationUpdates1();
/*

                                }
                            });
                            // on pressing cancel button
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();

*/
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void openCamera() {

        gps = new GPSTracker(context);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        latLong = "" + latitude + "," + longitude;


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);


        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // save file url in bundle as it will be null on screen orientation
//        // changes
//        outState.putParcelable("file_uri", fileUri);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        // get the file url
//        fileUri = savedInstanceState.getParcelable("file_uri");
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {


                Date dt = new Date();
                int hours = dt.getHours();
                int minutes = dt.getMinutes();
                int seconds = dt.getSeconds();

                String time = "" + hours + minutes + seconds;

                String fDate = new SimpleDateFormat("yyyyMMdd").format(dt);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;


                final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                byte[] byteArray = stream.toByteArray();

                if (Attendance_Mark.equals("IN")) {
                    attendanceBean.IN_IMAGE = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    saveLocally();


                    File file = new File(fileUri.getPath());
                    if (file.exists()) {
                        file.delete();
                    }

                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                }

                if (Attendance_Mark.equals("OUT")) {
                    attendanceBean.OUT_IMAGE = Base64.encodeToString(byteArray, Base64.DEFAULT);


                    updateLocally();

                    File file = new File(fileUri.getPath());
                    if (file.exists()) {
                        file.delete();
                    }

                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }


//
//                    if  (validatePhoto() )
//                    {

//
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//                    alertDialog.setTitle("Data Save alert !");
//                    alertDialog.setMessage("Do you want to save data ?");
//
//                    // On pressing Settings button
//                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
//
//
//                        }
//                    });
//
//
//                    // on pressing cancel button
//
//                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    // Showing Alert Message
//                    alertDialog.show();

                //   }

// delete file from memory card
//                File file = new File( fileUri.getPath() );
//                if ( file.exists())
//                {
//                    file.delete();
//                }


            }

        }
    }


/*    private void setToolbarTitle() {
        getSupportActionBar().setTitle("Leave Request");
    }*/

    public void insertLeaveBalance() {

        progressDialog = ProgressDialog.show(getActivity(), "", "Please wait.. Calculating available leave quotas!");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String obj = calculate_leave_balance();

                if (obj != null) {
                    progressDialog.dismiss();
                }
            }

        }).start();
    }

    @Override
    public void onClick(View v) {
        boolean addToBack = false;
        int id = v.getId();
        switch (id) {

            case R.id.tv_dom_travel:

                Intent intent = new Intent(context, DomesticTravelExpensesActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_exp_travel:

                Intent inte = new Intent(context, ExportTravelExpensesActivity.class);
                startActivity(inte);
                break;

            case R.id.tv_dom_rep:

                Intent in = new Intent(context, TravelExpenseReportActivity.class);
                startActivity(in);
                break;

            case R.id.tv_exp_rep:

                if (mHod.equalsIgnoreCase("Y")) {
                    Intent i = new Intent(context, HODApprovalActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(context, "You are not Authorised Person.", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.act_leave_req:

                if (CustomUtility.isInternetOn(context)) {

                    Intent intent1 = new Intent(context, LeaveRequestActivity.class);
                    startActivity(intent1);
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.act_leave_app:

                if (dataHelper.getPendinLeaveCount() > 0) {

                    Intent intent2 = new Intent(context, LeaveApproveActivity.class);
                    startActivity(intent2);

                } else {

                    Message msg = new Message();
                    msg.obj = "You have nothing Pending Leave";
                    mHandler.sendMessage(msg);
                }




                break;


            case R.id.act_od_req:

                if (CustomUtility.isInternetOn(context)) {
//                Toast.makeText(getActivity(),"OD Request Activity", Toast.LENGTH_SHORT).show();
                    Intent intent3 = new Intent(context, OdRequestActivity.class);
                    startActivity(intent3);
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.act_od_app:

                if (dataHelper.getPendingOdCount() > 0) {

                    Intent intent4 = new Intent(context, OdApproveActivity.class);
                    startActivity(intent4);

                }
                break;


            case R.id.act_gp_req:

                if (CustomUtility.isInternetOn(context)) {
//                Toast.makeText(getActivity(),"OD Request Activity", Toast.LENGTH_SHORT).show();
                    Intent intent_gp = new Intent(context, GpRequestActivity.class);
                    startActivity(intent_gp);
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;

                case R.id.txtCheckINID:

                if (CustomUtility.isInternetOn(context)) {
//                Toast.makeText(getActivity(),"OD Request Activity", Toast.LENGTH_SHORT).show();
                    Intent intent_gp = new Intent(context, CheckInvkActivity.class);
                    startActivity(intent_gp);
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.txtCheckOutID:

                if (CustomUtility.isInternetOn(context)) {
//                Toast.makeText(getActivity(),"OD Request Activity", Toast.LENGTH_SHORT).show();
                    Intent intent_gp = new Intent(context, GpRequestActivity.class);
                    startActivity(intent_gp);
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.act_gp_app:

                /*  if(db.getPendinGatePassCount() > 0) {*/

                if (CustomUtility.isInternetOn(context)) {

                    Intent intent_gp_app = new Intent(context, GatepassApproveActivity.class);
                    startActivity(intent_gp_app);


                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
              /*  }
                else{
                    Toast.makeText(getActivity(), "You have no Request for GatePass", Toast.LENGTH_SHORT).show();
                }*/
                break;


            case R.id.tv_create_task:

                if (CustomUtility.isInternetOn(context)) {
//                Toast.makeText(getActivity(),"OD Request Activity", Toast.LENGTH_SHORT).show();
                    Intent intent_create_task = new Intent(context, CreateTaskActivity.class);

                    startActivity(intent_create_task);
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.tv_complete_task:


                Intent intent_complete_task = new Intent(context, CompleteTaskActivity.class);

                startActivity(intent_complete_task);

                break;


            case R.id.tv_web_view:

                if (CustomUtility.isInternetOn(context)) {

                    Intent intent_web_view = new Intent(context, webViewActivity.class);

                    startActivity(intent_web_view);
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.start_travel:

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                    startLocationUpdates();

                }else{
                    buildAlertMessageNoGps();
                }


                break;

            case R.id.end_travel:


                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    startLocationUpdates1();

                }else{
                    buildAlertMessageNoGps1();
                }

                break;


            case R.id.convey_offline_data:

                Intent intnt = new Intent(context, OfflineDataConveyance.class);

                startActivity(intnt);


                break;

            case R.id.tv_create_attendance:


                Intent intent_web_view = new Intent(context, AttendanceActivity.class);

                startActivity(intent_web_view);


//                if (isValidate() && CustomUtility.CheckGPS(mContext)     ) {
//                attendanceBean =  db.getMarkAttendanceByDate(customutility.getCurrentDate());
//
//                    Log.d("server_date_in",attendanceBean.SERVER_DATE_IN);
//
//                    if (TextUtils.isEmpty(attendanceBean.SERVER_DATE_IN)) {
//                        Attendance_Mark = "IN";
//
//                        // getCamera();
//
//                        if(checkAndRequestPermissions()) {
//
//                            openCamera();
//                        }
//                        //else {
//                         //   Toast.makeText(getActivity().getApplicationContext(),"Permission Not Granted To Access Camera",Toast.LENGTH_SHORT).show();
//                       // }
//
//
//                    } else {
//                        in_attendance.setEnabled(false);
//                        CustomUtility.ShowToast("In Attendance Already Marked", mContext);
//
//                    }
//                }


                break;

//            case R.id.tv_create_travel:
//
//                if (checkInternetConenction())
//                {
//
//                    dataHelper.deleteTravelParameters();
//
//                    progressBar = new ProgressDialog(mContext);
//                    progressBar.setCancelable(true);
//                    // progressBar.setCancelable(true);
//                    progressBar.setMessage("Downloading Data...");
//                    progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    progressBar.setProgress(0);
//                    progressBar.setMax(100);
//                    progressBar.show();
//                    //reset progress bar and filesize status
//                    progressBarStatus = 0;
//
//                    new Thread(new Runnable() {
//                        public void run() {
//
//                            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
//                            param.add(new BasicNameValuePair("app_pernr", userModel.uid));
//
//                            try {
//
//                                String obj = CustomHttpClient.executeHttpPost1(SapUrl.travel_create, param);
//
//                                Log.d("travel_create", obj);
//
//                                if (obj != null) {
//
//
//                                    param.add(new BasicNameValuePair("app_pernr", userModel.uid));
//
//                                    travel_create = CustomHttpClient.executeHttpPost1(SapUrl.pending_leave, param);
//
//                                    Log.d("pending_leave", "" + travel_create);
//
//                                    JSONArray ja_travel = new JSONArray(travel_create);
//
//
//                                    for (int i = 0; i < ja_travel.length(); i++) {
//
//                                        JSONObject jo_travel = ja_travel.getJSONObject(i);
//
////
////                                        KEY_LEV_NO = jo_matnr.getString("leavNo");
////                                        HORO = jo_matnr.getString("horo");
////                                        ENAME = jo_matnr.getString("name");
////                                        LEV_TYP = jo_matnr.getString("dedQuta1");
////                                        LEV_FRM = jo_matnr.getString("levFr");
////                                        LEV_TO = jo_matnr.getString("levT");
////                                        REASON = jo_matnr.getString("reason");
////                                        CHRG_NAME1 = jo_matnr.getString("nameperl");
////                                        CHRG_NAME2 = jo_matnr.getString("nameperl2");
////                                        CHRG_NAME3 = jo_matnr.getString("nameperl3");
////                                        CHRG_NAME4 = jo_matnr.getString("nameperl4");
////                                        DIRECT_INDIRECT = jo_matnr.getString("directIndirect");
////
////                                        dataHelper.createPendingLeave(KEY_LEV_NO, HORO, ENAME, LEV_TYP, LEV_FRM, LEV_TO,
////                                                REASON, CHRG_NAME1, CHRG_NAME2, CHRG_NAME3, CHRG_NAME4, DIRECT_INDIRECT);
//
//                                    }
//
//
//                                }
//
//                                progressBarStatus = 100;
//
//                                // Updating the progress bar
//                                progressBarHandler.post(new Runnable() {
//                                    public void run() {
//
//                                        progressBar.setProgress(progressBarStatus);
//                                    }
//                                });
//
//
//                                progressBar.cancel();
//                                progressBar.dismiss();
//
//                                Intent intent2 = new Intent(context, LeaveApproveActivity.class);
//                                startActivity(intent2);
//
//
//                                Thread.sleep(5000);
//                            } catch (Exception e) {
//
//                            }
//
//
//                        }
//                    }).start();
//
//
//
//                }
//                else {
//                    Toast.makeText(getActivity(),"No Internet Connection", Toast.LENGTH_SHORT).show();
//                }
//                break;


//            case R.id.out_attendance:
//                if (isValidate() && CustomUtility.CheckGPS(mContext) ) {
//
//                    if (TextUtils.isEmpty(attendanceBean.SERVER_DATE_IN)) {
//                        CustomUtility.ShowToast("Please Mark In Attendance First.", mContext);
//                    } else {
//                        if (TextUtils.isEmpty(attendanceBean.SERVER_DATE_OUT)) {
//                            out_attendance.setEnabled(true);
//                            Attendance_Mark = "OUT";
//
//                            //    getCamera();   old code for camera
//                            if(checkAndRequestPermissions()) {
//                                openCamera();
//                            }
//
//                        } else {
//                            out_attendance.setEnabled(false);
//                            in_attendance.setEnabled(false);
//                            CustomUtility.ShowToast("Attendance Already Marked", mContext);
//                        }
//                    }
//
//                }
//                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        leave_notification.setText(Integer.toString(dataHelper.getPendinLeaveCount()));
        od_notification.setText(Integer.toString(dataHelper.getPendingOdCount()));
        pending_task_notification.setText(Integer.toString(dataHelper.getPendinTaskCount()));
        gp_notification.setText(Integer.toString(dataHelper.getPendinGatePassCount()));

        LocalBroadcastManager.getInstance(context).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));

        if (!checkPlayServices()) {
            Toast.makeText(dashboardActivity, "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }
    }

    public void testing() {
        //  Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
        Log.e("MethodCalled======>", "true");
    }

    private String calculate_leave_balance() {


        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        dataHelper = new DatabaseHelper(getActivity());
        dataHelper.deleteleaveBalance();

        context = getActivity();
        userModel = new LoggedInUser(context);
        /*Log.d("use_per",""+userModel.uid);*/

        try {


            param.add(new BasicNameValuePair("pernr", userModel.uid));

            String obj_leave_balance = CustomHttpClient.executeHttpPost1(SapUrl.leave_balance, param);



            JSONArray ja_mat = new JSONArray(obj_leave_balance);

            /*Log.d("json55", "" + ja_mat);*/


            for (int i = 0; i < ja_mat.length(); i++) {

                JSONObject jo_matnr = ja_mat.getJSONObject(i);


                leavetype = jo_matnr.getString("leaveType");
/*                ename = jo_matnr.getString("ename");
                btext = jo_matnr.getString("btext");*/

                dataHelper.createLeaveBalance(leavetype);

            }
            return obj_leave_balance;
        } catch (Exception e) {
            /* progressBarStatus = 40 ;*/
            e.printStackTrace();

        }

        return obj_leave_balance;

    }

    public void insert_pending_leave() {

        progressDialog = ProgressDialog.show(getActivity(), "", "Please wait.. searching pending leave(s)!  ");

        Toast.makeText(getActivity(), "Pending leave list", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                String obj = calculate_pending_leave();

                if (obj != null) {
                    progressDialog.dismiss();
                }
            }

        }).start();

    }


    //  Leave Pending for Approval

    private String calculate_pending_leave() {

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        dataHelper = new DatabaseHelper(getActivity());

        // Delete Pending leave from DB
        dataHelper.deletependingleave();

        context = getActivity();
        userModel = new LoggedInUser(context);
        /*Log.d("use_per",""+userModel.uid);*/

        try {


            param.add(new BasicNameValuePair("app_pernr", userModel.uid));

            obj_pending_leave = CustomHttpClient.executeHttpPost1(SapUrl.pending_leave, param);



            JSONArray ja_mat = new JSONArray(obj_pending_leave);

            /*Log.d("json55", "" + ja_mat);*/


            for (int i = 0; i < ja_mat.length(); i++) {

                JSONObject jo_matnr = ja_mat.getJSONObject(i);


                KEY_LEV_NO = jo_matnr.getString("leavNo");
                HORO = jo_matnr.getString("horo");
                ENAME = jo_matnr.getString("name");
                LEV_TYP = jo_matnr.getString("dedQuta1");
                LEV_FRM = jo_matnr.getString("levFr");
                LEV_TO = jo_matnr.getString("levT");
                REASON = jo_matnr.getString("reason");
                CHRG_NAME1 = jo_matnr.getString("nameperl");
                CHRG_NAME2 = jo_matnr.getString("nameperl2");
                CHRG_NAME3 = jo_matnr.getString("nameperl3");
                CHRG_NAME4 = jo_matnr.getString("nameperl4");
                DIRECT_INDIRECT = jo_matnr.getString("directIndirect");

                dataHelper.createPendingLeave(KEY_LEV_NO, HORO, ENAME, LEV_TYP, LEV_FRM, LEV_TO,
                        REASON, CHRG_NAME1, CHRG_NAME2, CHRG_NAME3, CHRG_NAME4, DIRECT_INDIRECT);

            }
            return obj_pending_leave;
        } catch (Exception e) {
            /* progressBarStatus = 40 ;*/
            e.printStackTrace();
        }

        return obj_pending_leave;

    }

    public void setNotification() {

        Log.e("Notification=====>", "" + "true");

        leave_notification.setText(Integer.toString(dataHelper.getPendinLeaveCount()));
        od_notification.setText(Integer.toString(dataHelper.getPendingOdCount()));
        pending_task_notification.setText(Integer.toString(dataHelper.getPendinTaskCount()));
        gp_notification.setText(Integer.toString(dataHelper.getPendinGatePassCount()));

    }

/// attendance code

    public void SyncAttendanceInBackground() {


        Intent i = new Intent(getActivity(), SyncDataService.class);
        getActivity().startService(i);


//
//        progressDialog = ProgressDialog.show(MarkAttendanceActivity.this, "", "Connecting to internet..");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                if (CustomUtility.isOnline(MarkAttendanceActivity.this))
//                {
//
//                    Intent i = new Intent(MarkAttendanceActivity.this, SyncDataService.class);
//                    i.putExtra("sync_data", "sync_mark_attendance");
//                    startService(i);
//
//                    progressDialog.dismiss();
//
//                    Message msg = new Message();
//                    msg.obj = "Attendance Sync Successfully";
//                    mHandler.sendMessage(msg);
//
//
//
//
//                } else {
//                    progressDialog.dismiss();
//                    Message msg = new Message();
//                    msg.obj = "No internet Connection . Attendance saved in offline";
//                    mHandler.sendMessage(msg);
//
//                }
//
//            }
//        }).start();


    }

    void saveLocally() {

        //  MainActivity.mainActivity.mydb.insertAttendance(attendanceBean);
        dataHelper = new DatabaseHelper(context);


        attendanceBean.PERNR = userModel.uid;
        attendanceBean.BEGDA = customutility.getCurrentDate1();
        attendanceBean.SERVER_DATE_IN = customutility.getCurrentDate1();
        attendanceBean.SERVER_TIME_IN = customutility.getCurrentTime1();
        String[] latlong = latLong.split(",");
        attendanceBean.IN_ADDRESS = getCompleteAddressString(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
        attendanceBean.IN_TIME = customutility.getCurrentTime1();
        attendanceBean.SERVER_DATE_OUT = "";
        attendanceBean.SERVER_TIME_OUT = "";
        attendanceBean.OUT_ADDRESS = "";
        attendanceBean.OUT_TIME = "";
        attendanceBean.WORKING_HOURS = "";
        attendanceBean.IMAGE_DATA = "";
        attendanceBean.CURRENT_MILLIS = System.currentTimeMillis();
        attendanceBean.IN_LAT_LONG = latLong;
//        attendanceBean.IN_FILE_NAME = mFile.getName();
//        attendanceBean.IN_FILE_VALUE = mFile.getPath();
        attendanceBean.OUT_LAT_LONG = "";
        attendanceBean.OUT_FILE_NAME = "";
        attendanceBean.OUT_FILE_LENGTH = "";
        attendanceBean.OUT_FILE_VALUE = "";


        dataHelper.insertMarkAttendance(attendanceBean);


        // Sync Data
//        new Capture_employee_gps_location(this, "2","");


        Toast.makeText(context, "In Attendance Marked", Toast.LENGTH_LONG).show();


//        SyncAttendanceInBackground();


    }

    void updateLocally() {

        dataHelper = new DatabaseHelper(context);

        attendanceBean.PERNR = userModel.uid;
        attendanceBean.SERVER_DATE_OUT = customutility.getCurrentDate1();
        attendanceBean.SERVER_TIME_OUT = customutility.getCurrentTime1();
        String[] latlong = latLong.split(",");
        attendanceBean.OUT_ADDRESS = getCompleteAddressString(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
        attendanceBean.OUT_TIME = customutility.getCurrentTime1();
        long attendanceDifference = System.currentTimeMillis() - attendanceBean.CURRENT_MILLIS;
        long second = (attendanceDifference / 1000) % 60;
        long minute = (attendanceDifference / (1000 * 60)) % 60;
        long hour = (attendanceDifference / (1000 * 60 * 60)) % 24;
        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        attendanceBean.WORKING_HOURS = time;
        attendanceBean.IMAGE_DATA = "";
        attendanceBean.OUT_LAT_LONG = latLong;
        // attendanceBean.OUT_FILE_NAME = mFile.getName();
        // attendanceBean.OUT_FILE_VALUE = mFile.getPath();


//        dataHelper.updateMarkAttendance(attendanceBean);
        //Out Attendance
//        new Capture_employee_gps_location(this, "3","");

        //   MainActivity.mainActivity.mydb.updateAttendance(attendanceBean);

        Toast.makeText(context, "Out Attendance Marked", Toast.LENGTH_LONG).show();


//        SyncAttendanceInBackground();
    }

    private boolean checkAndRequestPermissions() {

        int permissionCamera = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.CAMERA);
        int permissionStorage = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int locationPermission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted


                } else {
                    // Permission Denied

                    getActivity().finish();
                    System.exit(0);

                }
                break;
            case ALL_PERMISSIONS_RESULT:
                for (String perm : permissionsToRequest) {
                    if (!hasPermission(perm)) {
                        permissionsRejected.add(perm);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            new AlertDialog.Builder(context).
                                    setMessage("These permissions are mandatory to get your location. You need to allow them.").
                                    setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.
                                                        toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    }).setNegativeButton("Cancel", null).create().show();

                            return;
                        }
                    }
                }/* else {
                    if (googleApiClient != null) {
                        googleApiClient.connect();
                    }
                }*/

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        //startLocationUpdates();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Permissions ok, we get last location
        //location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

       /* if (location != null) {
            Log.e("Latitude : ","Longitued" + location.getLatitude()+"  "+location.getLongitude());
        }*/

        //startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void changeButtonVisibility(boolean state, float alphaRate, TextView txtSubmiteOrderID) {
        txtSubmiteOrderID.setEnabled(state);
        txtSubmiteOrderID.setAlpha(alphaRate);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                //   Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                //     Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //    Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }


    public void SyncLocalConveneinceDataToSap(String mode, String add, String add1, String date, String time, String pernr) {


        String docno_sap = null;
        String invc_done = null;


        DatabaseHelper db = new DatabaseHelper(this.context);

        LocalConvenienceBean param_invc = new LocalConvenienceBean();

        date = CustomUtility.formateDate1(date);
        time = CustomUtility.formateTime1(time);

        param_invc = db.getLocalConvinienceData(date, pernr, time);


        JSONArray ja_invc_data = new JSONArray();

        JSONObject jsonObj = new JSONObject();

        String[] sep = param_invc.getFrom_lng().split(",");
        String[] sep1 = param_invc.getTo_lng().split(",");


        try {


            jsonObj.put("pernr", param_invc.getPernr());
            jsonObj.put("begda", param_invc.getBegda());
            jsonObj.put("endda", param_invc.getEndda());

            jsonObj.put("start_time", param_invc.getFrom_time());
            jsonObj.put("end_time", param_invc.getTo_time());

            jsonObj.put("start_lat", sep[0]);
            jsonObj.put("end_lat", sep1[0]);
            jsonObj.put("start_long", sep[1]);
            jsonObj.put("end_long", sep1[1]);
            jsonObj.put("start_location", add);
            jsonObj.put("end_location", add1);
            jsonObj.put("distance", param_invc.getDistance());
            jsonObj.put("TRAVEL_MODE", mode);

            ja_invc_data.put(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }


        final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
        param1_invc.add(new BasicNameValuePair("travel_distance", String.valueOf(ja_invc_data)));


        //System.out.println(param1_invc.toString());

        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);

            String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.LOCAL_CONVENIENVCE, param1_invc);

            if (obj2 != "") {

                JSONArray ja = new JSONArray(obj2);

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);


                    invc_done = jo.getString("msgtyp");
                    docno_sap = jo.getString("msg");
                    if (invc_done.equalsIgnoreCase("S")) {

                        progressDialog.dismiss();
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);
                        db.deleteLocalconvenienceDetail(pernr, date, time);
                        CustomUtility.setSharedPreference(context, "localconvenience", "0");
                        changeButtonVisibility(false, 0.5f, end_travel);
                        changeButtonVisibility(true, 1f, start_travel);

                    } else if (invc_done.equalsIgnoreCase("E")) {
                        progressDialog.dismiss();
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressBar != null) {
            progressBar.cancel();
        }
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    class JSONAsyncTask extends AsyncTask<String, String, JSONObject> {

        JSONArray user = null;
        JSONObject jsono = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... urls) {

            try {

                ArrayList<LocalConvenienceBean1> localConvenienceBean1 = new ArrayList<LocalConvenienceBean1>();

                localConvenienceBean1 = dataHelper.getLocalConvience(context, userModel.uid);


                for (int i = 0; i < localConvenienceBean1.size(); i++) {
                    strtlatlng = localConvenienceBean1.get(i).getFrom_lng();

                    date = localConvenienceBean1.get(i).getBegda();
                    time = localConvenienceBean1.get(i).getFrom_time();
                    latlng = localConvenienceBean1.get(i).getFrom_lat();

                }

                String string = latlng;

                StringBuilder sb= new StringBuilder(string);

                sb.deleteCharAt(sb.length()-1);


                //String URL = "https://apis.mapmyindia.com/advancedmaps/v1/xl9ch768pp7v2esbr1uueymolk9k6iaf/route_eta/driving/" + latlng + "?alternatives=true&rtype=0&geometries=polyline&overview=full&exclude=&steps=true&region=ind";
                String URL = "https://apis.mapmyindia.com/advancedmaps/v1/3v3v2933alxcy2mq2swgrigo3p9k7icl/route_eta/driving/"+ sb + "?alternatives=true&rtype=0&geometries=polyline&overview=full&exclude=&steps=true&region=ind";

                //------------------>>
                HttpGet httppost = new HttpGet(URL);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                // StatusLine stat = response.getStatusLine();
                int status = response.getStatusLine().getStatusCode();



                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    String data = EntityUtils.toString(entity);
                    jsono = new JSONObject(data);


                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return jsono;

        }

        protected void onPostExecute(JSONObject jsono) {

            try {

                if (progressDialog != null)
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }

                try {
                    // Getting JSON Array
                    user = jsono.getJSONArray("routes");
                    JSONObject c = user.getJSONObject(0);

                    // Storing  JSON item in a Variable
                    try {

                        double distance = Double.parseDouble(c.getString("distance"));

                        distance1 = String.valueOf((distance >= 1000 ? (distance) / 1000  : distance ));



                        //distance1 = String.valueOf(Double.parseDouble(new DecimalFormat("#.##").format(distance1)));
                        if(distance >= 1000 )
                        {
                            DecimalFormat df = new DecimalFormat("#.##");
                            distance1 = df.format(Double.parseDouble(distance1));
                            distance1 = distance1 + " Km";
                        }
                        else{
                            distance1 = distance1 + " mtrs";
                        }

                    }
                    catch(NumberFormatException e){
                        e.printStackTrace();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog2);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);

                final TextInputEditText etstrdt = dialog.findViewById(R.id.tiet_str_dt);
                final TextInputEditText etstrlatlng = dialog.findViewById(R.id.tiet_str_lat_lng);
                final TextInputEditText etstrlocadd = dialog.findViewById(R.id.tiet_str_loc_add);
                final TextInputEditText etenddt = dialog.findViewById(R.id.tiet_end_dt);
                final TextInputEditText etendlatlng = dialog.findViewById(R.id.tiet_end_lat_lng);
                final TextInputEditText etendlocadd = dialog.findViewById(R.id.tiet_end_loc_add);
                final TextInputEditText ettotdis = dialog.findViewById(R.id.tiet_tot_dis);
                final TextInputEditText ettrvlmod = dialog.findViewById(R.id.tiet_trvl_mod);
                final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);
                final TextView ettxt1 = dialog.findViewById(R.id.txt1);
                final TextView ettxt2 = dialog.findViewById(R.id.txt2);
                ettrvlmod.requestFocus();

                date = CustomUtility.formateDate(date);
                time = CustomUtility.formateTime(time);

                etstrdt.setText(date + " " + time);
                etstrlatlng.setText(strtlatlng);
                etenddt.setText(current_end_date + " " + current_end_time);
                etendlatlng.setText(to_lat+","+to_lng);

                etstrlocadd.setText(fullAddress);
                etendlocadd.setText(fullAddress1);

                ettotdis.setText(distance1);


                ettxt1.setText("Local Conveyance Details");
                ettxt2.setText("Press Confirm will end your Journey");

                etcncl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                etconfm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String travel_mode = ettrvlmod.getText().toString();


                        if (CustomUtility.isInternetOn(context)) {
                            if (!TextUtils.isEmpty(travel_mode) && !travel_mode.equals("")) {

                                progressDialog = ProgressDialog.show(context, "", "Sending Data to server..please wait !");

                                new Thread(new Runnable() {
                                    public void run() {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(mService != null) {
                                                    mService.removeLocationUpdates();
                                                }
                                                SyncLocalConveneinceDataToSap(travel_mode,fullAddress,fullAddress1,date,time, userModel.uid);
                                            }
                                        });
                                    }
                                }).start();

                                dialog.dismiss();

                            } else {
                                Toast.makeText(context, "Please Enter Travel Mode.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.show();

            } catch (Exception e) {
                Log.d("onResponse", "There is an error");
                e.printStackTrace();
                if (progressDialog != null)
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
            }
        }

        }

}