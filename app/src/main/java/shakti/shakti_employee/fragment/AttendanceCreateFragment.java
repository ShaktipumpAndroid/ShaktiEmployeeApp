package shakti.shakti_employee.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.CartSharedPreferences;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.bean.AttendanceBean;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.GPSTracker;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.other.SyncDataToSAP_New;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttendanceCreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttendanceCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceCreateFragment extends Fragment {

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    TextView tv_intime, tv_outtime, in_attendance, out_attendance, tv_intime_sync, tv_outtime_sync;
    DatabaseHelper dataHelper;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    DatabaseHelper db = null;
    CustomUtility customutility = null;
    AttendanceBean attendanceBean;
    String Attendance_Mark = null, latLong;
    GPSTracker gps;
    ProgressDialog progressBar;
    SharedPreferences pref;
    private OnFragmentInteractionListener mListener;
    private DashboardActivity dashboardActivity;
    private Context mContext;
    private LoggedInUser userModel;
    private Uri fileUri; // file url to store image
    private Handler progressBarHandler = new Handler();
    private int progressBarStatus = 0;

    public AttendanceCreateFragment() {
        // Required empty public constructor
    }

    public static AttendanceCreateFragment newInstance() {
        AttendanceCreateFragment fragment = new AttendanceCreateFragment();
        Bundle args = new Bundle();
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

    public void setDashBoard(DashboardActivity dashBoard) {
        this.dashboardActivity = dashBoard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mContext = this.getActivity();
        userModel = new LoggedInUser(mContext);


        db = new DatabaseHelper(mContext);
        customutility = new CustomUtility();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_create_attendance, container, false);


        tv_intime = (TextView) v.findViewById(R.id.tv_intime);
        tv_intime_sync = (TextView) v.findViewById(R.id.tv_intime_sync_status);
        tv_outtime = (TextView) v.findViewById(R.id.tv_outtime);
        tv_outtime_sync = (TextView) v.findViewById(R.id.tv_outtime_sync_status);
        in_attendance = (TextView) v.findViewById(R.id.in_attendance);
        out_attendance = (TextView) v.findViewById(R.id.out_attendance);


        attendanceBean = db.getMarkAttendanceByDate(customutility.getCurrentDate1(), mContext);

        Log.d("disp_atnd", attendanceBean.IN_TIME + "---" + attendanceBean.OUT_TIME);

        if (TextUtils.isEmpty(attendanceBean.IN_TIME)) {
            tv_intime.setVisibility(View.INVISIBLE);
        } else {
            tv_intime.setVisibility(View.VISIBLE);
            tv_intime.setText(attendanceBean.SERVER_DATE_IN + "  " + attendanceBean.IN_TIME);
        }

        if (TextUtils.isEmpty(attendanceBean.OUT_TIME)) {
            tv_outtime.setVisibility(View.INVISIBLE);
        } else {
            tv_outtime.setVisibility(View.VISIBLE);
            tv_outtime.setText(attendanceBean.SERVER_DATE_OUT + "  " + attendanceBean.OUT_TIME);
        }

        if (tv_intime.getText().toString().trim().isEmpty()) {
            pref = CartSharedPreferences.createObject(mContext);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("in_time_sync", "");
            editor.commit();
            editor.apply();
        }

        if (tv_outtime.getText().toString().trim().isEmpty()) {
            pref = CartSharedPreferences.createObject(mContext);

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("out_time_sync", "");
            editor.commit();
            editor.apply();
        }


        SharedPreferences pref = CartSharedPreferences.createObject(getActivity().getApplicationContext());
        if (pref.getString("in_time_sync", "A").equalsIgnoreCase("Y")) {
            tv_intime_sync.setText("Data Synced");
            tv_intime_sync.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (!pref.getString("in_time_sync", "A").equalsIgnoreCase("Y") && tv_intime.getText().toString().isEmpty()) {
            tv_intime_sync.setText("In Attendance Not Marked");
            tv_intime_sync.setTextColor(getResources().getColor(R.color.red));
        } else if (!pref.getString("in_time_sync", "A").equalsIgnoreCase("Y") && !tv_intime.getText().toString().isEmpty()) {
            tv_intime_sync.setText("Data Not Synced");
            tv_intime_sync.setTextColor(getResources().getColor(R.color.red));
        }


        if (pref.getString("out_time_sync", "A").equalsIgnoreCase("Y")) {
            tv_outtime_sync.setText("Data Synced");
            tv_outtime_sync.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (!pref.getString("out_time_sync", "A").equalsIgnoreCase("Y") && tv_outtime.getText().toString().isEmpty() &&
                !tv_intime.getText().toString().isEmpty()) {
            tv_outtime_sync.setText("Out Attendance Not Marked");
            tv_outtime_sync.setTextColor(getResources().getColor(R.color.red));
        } else if (!pref.getString("out_time_sync", "A").equalsIgnoreCase("Y") && !tv_outtime.getText().toString().isEmpty()) {
            tv_outtime_sync.setText("Data Not Synced");
            tv_outtime_sync.setTextColor(getResources().getColor(R.color.red));
        }


        in_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidate() && CustomUtility.CheckGPS(mContext)) {
//                    attendanceBean = db.getMarkAttendanceByDate(customutility.getCurrentDate());

                    Log.d("server_date_in", attendanceBean.SERVER_DATE_IN);

                    if (TextUtils.isEmpty(attendanceBean.SERVER_DATE_IN)) {
                        Attendance_Mark = "IN";


                        if (checkAndRequestPermissions()) {

                            openCamera();
                        }


                    } else {
                        in_attendance.setEnabled(false);
                        CustomUtility.ShowToast("In Attendance Already Marked", mContext);

                    }
                }

            }
        });


        out_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidate() && CustomUtility.CheckGPS(mContext)) {

                    if (tv_intime.getText().toString().isEmpty()) {
                        CustomUtility.ShowToast("Please Mark In Attendance First.", mContext);
                    } else {
                        if (tv_outtime.getText().toString().isEmpty()) {
                            out_attendance.setEnabled(true);
                            Attendance_Mark = "OUT";

                            if (checkAndRequestPermissions()) {
                                openCamera();
                            }

                        } else {
                            out_attendance.setEnabled(false);
                            in_attendance.setEnabled(false);
                            CustomUtility.ShowToast("Attendance Already Marked", mContext);
                        }
                    }

                }

            }
        });


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    public boolean isValidate() {
        if (CustomUtility.isDateTimeAutoUpdate(mContext)) {

        } else {
            CustomUtility.showSettingsAlert(mContext);
            return false;
        }
        return true;
    }


    public void openCamera() {

        gps = new GPSTracker(mContext);
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

//                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
//                    startActivity(intent);
//                    getActivity().finish();

                }

                if (Attendance_Mark.equals("OUT")) {
                    attendanceBean.OUT_IMAGE = Base64.encodeToString(byteArray, Base64.DEFAULT);


                    updateLocally();

                    File file = new File(fileUri.getPath());
                    if (file.exists()) {
                        file.delete();
                    }

//                    Intent intent = new Intent(getActivity(), DashboardActivity.class);
//                    startActivity(intent);
//                    getActivity().finish();
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        synDataToSAP();

                    }
                }, 1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getActivity(), DashboardActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                    }
                }, 2000);


            }

        }
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

    void saveLocally() {

        //  MainActivity.mainActivity.mydb.insertAttendance(attendanceBean);
        dataHelper = new DatabaseHelper(mContext);


        attendanceBean.PERNR = userModel.uid;
        attendanceBean.BEGDA = customutility.getCurrentDate1();
        attendanceBean.SERVER_DATE_IN = customutility.getCurrentDate1();
        attendanceBean.SERVER_TIME_IN = customutility.getCurrentTime1();
        String latlong[] = latLong.split(",");
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


        Toast.makeText(mContext, "In Attendance Marked", Toast.LENGTH_LONG).show();


    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

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

    void updateLocally() {

        dataHelper = new DatabaseHelper(mContext);

        attendanceBean.PERNR = userModel.uid;
        attendanceBean.SERVER_DATE_OUT = customutility.getCurrentDate1();
        attendanceBean.SERVER_TIME_OUT = customutility.getCurrentTime1();
        String latlong[] = latLong.split(",");
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


        dataHelper.updateMarkAttendance(attendanceBean, getActivity().getApplicationContext());
        //Out Attendance
//        new Capture_employee_gps_location(this, "3","");

        //   MainActivity.mainActivity.mydb.updateAttendance(attendanceBean);

        Toast.makeText(mContext, "Out Attendance Marked", Toast.LENGTH_LONG).show();
//        SyncAttendanceInBackground();
    }

    public void synDataToSAP() {


        if (CustomUtility.isInternetOn(mContext)) {

            // creating progress bar dialog
            progressBar = new ProgressDialog(getActivity());
            progressBar.setCancelable(true);
            // progressBar.setCancelable(true);
            progressBar.setMessage("Syncing Attendance");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            //reset progress bar and filesize status
            progressBarStatus = 0;

            new Thread(new Runnable() {
                public void run() {

//                while (progressBarStatus < 100) {
                    // performing operation

                    try {

                        new SyncDataToSAP_New().SendAllDataToSAP(getActivity().getApplication());

                        progressBar.dismiss();
                        progressBar.cancel();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }).start();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection.Start Internet Service & Check after 15 Minutes", Toast.LENGTH_SHORT).show();
        }


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
