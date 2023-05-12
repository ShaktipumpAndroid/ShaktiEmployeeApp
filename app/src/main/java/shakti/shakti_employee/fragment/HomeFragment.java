package shakti.shakti_employee.fragment;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static android.os.Build.VERSION.SDK_INT;
import static androidx.core.content.PermissionChecker.checkSelfPermission;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import models.DistanceResponse;
import models.Element;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.AttendanceActivity;
import shakti.shakti_employee.activity.CheckInvkActivity;
import shakti.shakti_employee.activity.CompleteTaskActivity;
import shakti.shakti_employee.activity.CreateTaskActivity;
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
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.GPSTracker;
import shakti.shakti_employee.other.SAPWebService;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.utility.CameraUtils;
import shakti.shakti_employee.utility.DistanceApiClient;
import shakti.shakti_employee.utility.RestUtil;
import shakti.shakti_employee.utility.Utility;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int REQUEST_CODE_PERMISSION = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private LocationRequest locationRequest;

    TextView act_leave_req, act_leave_app, act_od_req, act_od_app, act_gp_req, txtCheckINID, txtCheckOutID, act_gp_app,
            gp_notification, tv_dom_travel, tv_dom_rep, tv_exp_rep, tv_exp_travel, markAttendanceBar, leave_notification, od_notification, pending_task_notification,
            tv_travel_txt, tv_create_task, tv_complete_task, tv_web_view, tv_create_attendance, photo1, photo2, start_travel, end_travel, convey_offline_data;
    Context context;
    private static HomeFragment instance;
    // For Leave Balance
    DatabaseHelper dataHelper;
    String fullAddress = null, fullAddress1 = null, distance1 = null, latlng = null, from_lat, from_lng, to_lat, to_lng, start_photo_text, end_photo_text,
            value, latLong, mParam1, mParam2, mTravel, mHod, current_start_date, current_end_date, current_start_time, current_end_time,
            strtlatlng = null, date = null, time = null, startphoto, allLatLong;

    Bitmap bitmap;
    GPSTracker gps;
    CustomUtility customutility = null;
    AttendanceBean attendanceBean;
    View view, view1;
    LinearLayout travel;

    ProgressDialog progressBar;
    private Handler progressBarHandler = new Handler();
    SAPWebService con = null;
    // TODO: Rename and change types of parameters
    private OnFragmentInteractionListener mListener;
    private ProgressDialog progressDialog;
    private LoggedInUser userModel;
    //private Context mContext;
    private Uri fileUri; // file url to store image
    private int progressBarStatus = 0;
    LocalConvenienceBean localConvenienceBean;
    LocationManager locationManager;

    FusedLocationProviderClient fusedLocationClient;
    private MyReceiver myReceiver;
    boolean start_photo_flag = false, end_photo_flag = false;
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

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                SapUrl.IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
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

        if (progressBar != null) {
            progressBar.cancel();
        }
        if (progressDialog != null) {
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
            return checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
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
        start_photo_text = "";
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(5000);
        locationRequest.setInterval(10 * 1000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
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
                try {
                    current_start_date = new CustomUtility().getCurrentDate();
                    current_start_time = new CustomUtility().getCurrentTime();
                    if (location != null) {
                        from_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLatitude())));
                        from_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLongitude())));
                        lat[0] = location.getLatitude();
                        lng[0] = location.getLongitude();
                    } else {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        from_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location1.getLatitude())));
                        from_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location1.getLongitude())));
                        lat[0] = location1.getLatitude();
                        lng[0] = location1.getLongitude();
                    }
                    progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.loading), getResources().getString(R.string.please_wait_));
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something here
                            if (!TextUtils.isEmpty(from_lat) && !TextUtils.isEmpty(from_lng)) {
                                if (progressDialog != null)
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }

                                final Dialog dialog = new Dialog(getActivity());
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
                                photo1 = dialog.findViewById(R.id.photo1);
                                final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                                final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);

                                if (CustomUtility.isOnline(getActivity())) {
                                    Geocoder geo = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
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
                                            etadd.setText(getResources().getString(R.string.no_location_found));
                                        } else {
                                            etadd.setText(addresses.get(0).getAddressLine(0));
                                        }
                                    }
                                }
                                etlat.setText(from_lat);
                                etlng.setText(from_lng);

                                ettxt1.setText(getResources().getString(R.string.Current_Location));
                                ettxt2.setText(getResources().getString(R.string.confirm_));

                                // Toast.makeText(getActivity(), "from_lat="+from_lat+"\nfrom_lng="+from_lng, Toast.LENGTH_SHORT).show();

                                photo1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        value = "1";
                                        if (start_photo_text == null || start_photo_text.isEmpty()) {
                                            if (checkPermission()) {
                                                showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO1");
                                            } else {
                                                requestPermission();
                                            }
                                        }
                                    }
                                });

                                etcncl.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                etconfm.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (mService != null) { // add null checker
                                            mService.requestLocationUpdates();
                                        }
                                        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(String.valueOf(userModel.uid),
                                                current_start_date,
                                                "",
                                                current_start_time,
                                                "",
                                                from_lat,
                                                "",
                                                from_lng,
                                                "",
                                                "",
                                                "",
                                                "",
                                                start_photo_text,
                                                ""
                                        );
                                        dataHelper.insertLocalconvenienceData(localConvenienceBean);
                                        CustomUtility.setSharedPreference(getActivity(), "localconvenience", "1");
                                        changeButtonVisibility(false, 0.5f, start_travel);
                                        changeButtonVisibility(true, 1f, end_travel);
                                        Toast.makeText(getActivity(), getResources().getString(R.string.YourJourney), Toast.LENGTH_LONG).show();
                                        dialog.dismiss();

                                    }
                                });
                                dialog.show();
                            } else {
                                if (progressDialog != null)
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }
                                Toast.makeText(getActivity(), getResources().getString(R.string.Pleasewaitcurrentlocation), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 2000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void startLocationUpdates1() {
        end_photo_text = "";
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setMaxWaitTime(5000);
        locationRequest.setInterval(10 * 1000);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                from_lat = " ";
                from_lng = " ";
                to_lat = " ";
                to_lng = " ";
                fullAddress = "";
                fullAddress1 = "";
                startphoto = "";
                try {
                    localConvenienceBean = dataHelper.getLocalConvinienceData();
                    current_start_date = localConvenienceBean.getBegda();
                    current_start_time = localConvenienceBean.getFrom_time();

                    current_end_date = customutility.getCurrentDate();
                    current_end_time = customutility.getCurrentTime();

                    from_lat = localConvenienceBean.getFrom_lat();
                    from_lng = localConvenienceBean.getFrom_lng();
                    startphoto = localConvenienceBean.getPhoto1();
                    if (location != null) {
                        to_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLatitude())));
                        to_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location.getLongitude())));
                    } else {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        Location location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        to_lat = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location1.getLatitude())));
                        to_lng = String.valueOf(Double.parseDouble(new DecimalFormat("##.#####").format(location1.getLongitude())));


                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                fullAddress = localConvenienceBean.getStart_loc();
                if (CustomUtility.isOnline(getActivity())) {
                    progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.loading), getResources().getString(R.string.please_wait_));
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(from_lat) && !TextUtils.isEmpty(from_lng) && !TextUtils.isEmpty(to_lat) && !TextUtils.isEmpty(to_lng)) {
                                if (progressDialog != null)
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }
                                allLatLong = from_lat + "," + from_lng + "," + to_lat + "," + to_lng;
                                getDistanceInfo(from_lat, from_lng, to_lat, to_lng, allLatLong);
                            } else {
                                if (progressDialog != null)
                                    if (progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                        progressDialog = null;
                                    }
                                Toast.makeText(getActivity(), getResources().getString(R.string.Pleasewaitcurrentlocation), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 2000);
                    if (mService != null) { // add null checker
                        mService.removeLocationUpdates();
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.saved_travel_data, Toast.LENGTH_SHORT).show();
                    LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(String.valueOf(userModel.uid), current_start_date,
                            current_end_date,
                            current_start_time,
                            current_end_time,
                            from_lat,
                            to_lat,
                            from_lng,
                            to_lng,
                            fullAddress,
                            fullAddress1,
                            distance1,
                            startphoto,
                            end_photo_text
                    );
                    dataHelper.updateLocalconvenienceData(localConvenienceBean);
                    CustomUtility.setSharedPreference(getActivity(), "localconvenience", "0");
                    changeButtonVisibility(false, 0.5f, end_travel);
                    changeButtonVisibility(true, 1f, start_travel);
                }
            }
        });
    }

    private void getDistanceInfo(String lat1, String lon1, String lat2, String lon2, String allLatLong) {
        // http://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY


        Map<String, String> mapQuery = new HashMap<>();

        mapQuery.put("origins", lat1 + "," + lon1);
        mapQuery.put("destinations", lat2 + "," + lon2);
        mapQuery.put("units", "metric");
        mapQuery.put("mode", "driving");
        mapQuery.put("key", getResources().getString(R.string.google_API_KEY));

        DistanceApiClient client = RestUtil.getInstance().getRetrofit().create(DistanceApiClient.class);

        Call<DistanceResponse> call = client.getDistanceInfo(mapQuery);
        Log.e("URL===>", String.valueOf(call.request().url()));
        Log.e("URL===>", String.valueOf(call.request().url()));
        call.enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DistanceResponse> call, @NonNull Response<DistanceResponse> response) {



                    if (response.body() != null &&
                            response.body().getRows() != null &&
                            response.body().getRows().size() > 0 &&
                            response.body().getRows().get(0) != null &&
                            response.body().getRows().get(0).getElements() != null &&
                            response.body().getRows().get(0).getElements().size() > 0 &&
                            response.body().getRows().get(0).getElements().get(0) != null &&
                            response.body().getRows().get(0).getElements().get(0).getDistance() != null &&
                            response.body().getRows().get(0).getElements().get(0).getDuration() != null) {
                        Log.e("Response======>", String.valueOf(response.body()));

                        if (progressDialog != null)
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }

                            Element element = response.body().getRows().get(0).getElements().get(0);
                        if(response.body().getOriginAddresses()!=null && response.body().getOriginAddresses().size()>0) {
                            fullAddress = response.body().getOriginAddresses().get(0);
                        }else {
                            fullAddress = Utility.retrieveAddress(lat1,lon1,getActivity());
                        }
                        if(response.body().getDestinationAddresses()!=null && response.body().getDestinationAddresses().size()>0) {
                            fullAddress1 = response.body().getDestinationAddresses().get(0);
                        }else {
                            fullAddress1 = Utility.retrieveAddress(lat2,lon2,getActivity());
                        }

                            distance1 = element.getDistance().getText();


                            Log.e("distance1=====>", distance1);

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
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
                    final TextInputLayout til_trvl_mod = dialog.findViewById(R.id.til_trvl_mod);
                    final TextInputEditText ettrvlmod = dialog.findViewById(R.id.tiet_trvl_mod);


                    final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                    final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);
                    final TextView ettxt1 = dialog.findViewById(R.id.txt1);
                    final TextView ettxt2 = dialog.findViewById(R.id.txt2);
                    photo2 = dialog.findViewById(R.id.photo2);


                    etstrdt.setText(CustomUtility.formateDate1(current_start_date) + " " + CustomUtility.formateTime1(current_start_time));
                    etstrlatlng.setText(from_lat + "," + from_lng);
                    etenddt.setText(CustomUtility.formateDate1(current_end_date) + " " + CustomUtility.formateTime1(current_end_time));
                    etendlatlng.setText(to_lat + "," + to_lng);
                    etstrlocadd.setText(fullAddress);
                    etendlocadd.setText(fullAddress1);
                    ettotdis.setText(distance1);

                    ettxt1.setText(getResources().getString(R.string.localconveniencedetails));
                    ettxt2.setText(getResources().getString(R.string.endyourJourney));

                    photo2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (end_photo_text == null || end_photo_text.isEmpty()) {
                                if (checkPermission()) {
                                    if (checkPermission()) {
                                        openCamera();
                                    } else {
                                        requestPermission();
                                    }
                                } else {
                                    requestPermission();
                                }

                            }
                        }
                    });

                    etcncl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    etconfm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (CustomUtility.isOnline(getActivity())) {
                                if (!ettrvlmod.getText().toString().isEmpty()) {

                                    progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.sending_please_wait));

                                    new Thread(new Runnable() {
                                        public void run() {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(String.valueOf(userModel.uid), current_start_date,
                                                            current_end_date,
                                                            current_start_time,
                                                            current_end_time,
                                                            from_lat,
                                                            to_lat,
                                                            from_lng,
                                                            to_lng,
                                                            fullAddress,
                                                            fullAddress1,
                                                            distance1,
                                                            startphoto,
                                                            end_photo_text
                                                    );

                                                    dataHelper.updateLocalconvenienceData(localConvenienceBean);
                                                    SyncLocalConveneinceDataToSap(ettrvlmod.getText().toString(), current_end_date, current_end_time, distance1, allLatLong);
                                                }
                                            });
                                        }

                                        ;
                                    }).start();

                                    dialog.dismiss();

                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.Please_Enter_Travel_Mode), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.ConnectToInternet), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    dialog.show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<DistanceResponse> call,@NonNull Throwable t) {

                Log.e("Failed", "&&&", t);

                if (progressDialog != null)
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                ;

            }
        });
    }


    public void showConfirmationGallery(final String keyimage, final String name) {

        final CharSequence[] items = {getResources().getString(R.string.take), getResources().getString(R.string.cancel)};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(), R.style.MyDialogTheme);
        builder.setTitle(getResources().getString(R.string.AddPhoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getResources().getString(R.string.take))) {
                    openCamera();
                    setFlag(keyimage);


                } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void setFlag(String key) {
        start_photo_flag = false;
        end_photo_flag = false;

        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                start_photo_flag = true;
                break;
            case DatabaseHelper.KEY_PHOTO2:
                end_photo_flag = true;
                break;

        }

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
                            startLocationUpdates();

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
        ContentValues values = new ContentValues();
        fileUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                try {
                    Bitmap bitmap =
                            MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), fileUri);

                    Bitmap UserBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                            fileUri);

                    String path = CameraUtils.getPath(context, fileUri); // From Gallery

                    if (path == null) {
                        path = data.getData().getPath(); // From File Manager
                    }
                    String filedata = path;
                    Log.e("Activity", "PathHolder22= " + path);

                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    String file;
                    if (filename.indexOf(".") > 0) {
                        file = filename.substring(0, filename.lastIndexOf("."));
                    } else {
                        file = "";
                    }
                    if (android.text.TextUtils.isEmpty(file)) {
                        CustomUtility.ShowToast("File not valid", getActivity());
                    } else {

                        if (start_photo_flag) {
                            start_photo_text = path;
                            setIcon(DatabaseHelper.KEY_PHOTO1);
                        }

                        if (end_photo_flag) {
                            end_photo_text = path;
                            setIcon(DatabaseHelper.KEY_PHOTO2);
                        }
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    public void setIcon(String key) {

        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                if (start_photo_text == null || start_photo_text.isEmpty()) {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


            case DatabaseHelper.KEY_PHOTO2:
                if (end_photo_text == null || end_photo_text.isEmpty()) {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
                } else {
                    photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;


        }

    }

    @Override
    public void onClick(View v) {
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

                if (CustomUtility.isInternetOn(context)) {

                    Intent intent_gp_app = new Intent(context, GatepassApproveActivity.class);
                    startActivity(intent_gp_app);


                } else {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.tv_create_task:

                if (CustomUtility.isInternetOn(context)) {
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

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                    startLocationUpdates();

                } else {
                    buildAlertMessageNoGps();
                }


                break;

            case R.id.end_travel:


                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                       startLocationUpdates1();



                } else {
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

                break;
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
            Toast.makeText(getActivity(), "You need to install Google Play Services to use the App properly", Toast.LENGTH_SHORT).show();
        }
    }

    public void testing() {
        //  Toast.makeText(getActivity(), "Test", Toast.LENGTH_SHORT).show();
        Log.e("MethodCalled======>", "true");
    }


    public void setNotification() {

        Log.e("Notification=====>", "" + "true");

        leave_notification.setText(Integer.toString(dataHelper.getPendinLeaveCount()));
        od_notification.setText(Integer.toString(dataHelper.getPendingOdCount()));
        pending_task_notification.setText(Integer.toString(dataHelper.getPendinTaskCount()));
        gp_notification.setText(Integer.toString(dataHelper.getPendinGatePassCount()));

    }


    @Override
    public void onLocationChanged(Location location) {
        //startLocationUpdates();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }


    public void SyncLocalConveneinceDataToSap(String mode, String endat, String endtm, String mFlotDistanceKM, String allLatLong) {

        String docno_sap = null;
        String invc_done = null;

        DatabaseHelper db = new DatabaseHelper(this.getActivity());

        LocalConvenienceBean param_invc = new LocalConvenienceBean();

        param_invc = db.getLocalConvinienceData(endat, endtm);

        JSONArray ja_invc_data = new JSONArray();

        JSONObject jsonObj = new JSONObject();

        try {

            jsonObj.put("pernr", param_invc.getPernr());
            jsonObj.put("begda", param_invc.getBegda());
            jsonObj.put("endda", param_invc.getEndda());

            jsonObj.put("start_time", param_invc.getFrom_time());
            jsonObj.put("end_time", param_invc.getTo_time());

            jsonObj.put("start_lat", param_invc.getFrom_lat());
            jsonObj.put("end_lat", param_invc.getTo_lat());
            jsonObj.put("start_long", param_invc.getFrom_lng());
            jsonObj.put("end_long", param_invc.getTo_lng());
            if(param_invc.getStart_loc()!=null && !param_invc.getStart_loc().isEmpty()){
                jsonObj.put("start_location", param_invc.getStart_loc());
            }else {
                jsonObj.put("start_location", Utility.retrieveAddress(param_invc.getFrom_lat(),param_invc.getFrom_lng(),getActivity()));
            }

            if(param_invc.getEnd_loc()!=null && !param_invc.getEnd_loc().isEmpty()){
                jsonObj.put("end_location", param_invc.getEnd_loc());
            }else {
                jsonObj.put("end_location", Utility.retrieveAddress(param_invc.getTo_lat(),param_invc.getTo_lng(),getActivity()));
            }
            jsonObj.put("distance", mFlotDistanceKM);
            jsonObj.put("TRAVEL_MODE", mode);
            jsonObj.put("LAT_LONG", allLatLong);
            if(param_invc.getPhoto1()!=null && !param_invc.getPhoto1().isEmpty()){
                jsonObj.put("PHOTO1", Utility.getBase64FromBitmap(context,param_invc.getPhoto1()));
            }else {
                jsonObj.put("PHOTO1", "");
            }

            if(param_invc.getPhoto2()!=null && !param_invc.getPhoto2().isEmpty()){
                jsonObj.put("PHOTO2", Utility.getBase64FromBitmap(context,param_invc.getPhoto2()));
            }else {
                jsonObj.put("PHOTO2", "");
            }
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

            if (!obj2.isEmpty()) {

                JSONArray ja = new JSONArray(obj2);

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);


                    invc_done = jo.getString("msgtyp");
                    docno_sap = jo.getString("msg");
                    if (invc_done.equalsIgnoreCase("S")) {

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        ;
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);
                        db.deleteLocalconvenienceDetail1(endat, endtm);
                        CustomUtility.setSharedPreference(getActivity(), "localconvenience", "0");
                        changeButtonVisibility(false, 0.5f, end_travel);
                        changeButtonVisibility(true, 1f, start_travel);

                    } else if (invc_done.equalsIgnoreCase("E")) {

                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        ;
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

        }
    }


    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_MEDIA_IMAGES},
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);

        }
    }


    private boolean checkPermission() {
        int cameraPermission =
                ContextCompat.checkSelfPermission(getActivity(), CAMERA);
        int writeExternalStorage =
                ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
        int ReadExternalStorage =
                ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE);

        int AccessCoarseLocation =
                ContextCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION);
        int AccessFineLocation =
                ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION);
        int ReadMediaImage =
                ContextCompat.checkSelfPermission(getActivity(), READ_MEDIA_IMAGES);


        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return cameraPermission == PackageManager.PERMISSION_GRANTED
                    && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED && AccessFineLocation == PackageManager.PERMISSION_GRANTED && ReadMediaImage == PackageManager.PERMISSION_GRANTED;
        } else {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED
                    && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED && AccessFineLocation == PackageManager.PERMISSION_GRANTED;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {
                if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessFineLocation = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadMediaImage = grantResults[3] == PackageManager.PERMISSION_GRANTED;


                    if (ACCESSCAMERA && AccessCoarseLocation && AccessFineLocation && ReadMediaImage) {
                        if (value.equals("1")) {
                            showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO1");
                        } else {
                            showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO2");
                        }

                    } else {
                        Toast.makeText(getActivity(), R.string.all_permission, Toast.LENGTH_LONG).show();
                    }
                } else {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalStorage =
                            grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage =
                            grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessFineLocation = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (ACCESSCAMERA && writeExternalStorage && ReadExternalStorage && AccessCoarseLocation && AccessFineLocation) {
                        if (value.equals("1")) {
                            showConfirmationGallery(DatabaseHelper.KEY_PHOTO1, "PHOTO1");
                        } else {
                            showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO2");
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.all_permission, Toast.LENGTH_LONG).show();
                    }

                }
            }
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


}