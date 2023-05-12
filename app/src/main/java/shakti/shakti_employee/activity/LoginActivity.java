package shakti.shakti_employee.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shakti.shakti_employee.R;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SapUrl;

public class LoginActivity extends AppCompatActivity {

    private AppUpdateManager appUpdateManager;
    private static final int IMMEDIATE_APP_UPDATE_REQ_CODE = 100;
    private final int REQUEST_CODE_PERMISSION = 123;
    EditText textview_login_id, textview_pwd;
    DatabaseHelper dataHelper;
    ProgressDialog progressBar;
    String obj;  // For Login request to SAP
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(LoginActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private String mFromAddress, mToAddress, mAmount, mDescription, mLocation1, mRegion, mGSTIN, mSerialNo, mStartDate, mEndDate, mLocation, mCountry, mType, mUserID;
    private String mExpenses1, mCurrency1, mTaxcode1;
    private LoggedInUser mloggedInModel;
    private LoggedInUser mModel;
    private Context mContext;
    private Handler progressBarHandler = new Handler();
    private ProgressDialog progressDialog;
    //    SAPWebService con = null;
    private String st_pass1;
    private int progressBarStatus = 0;
    private long fileSize = 0;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        mModel = new LoggedInUser(mContext);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textview_login_id = (EditText) findViewById(R.id.login_id);
        textview_pwd = (EditText) findViewById(R.id.pwd);
        TextView forgotpassword = (TextView) findViewById(R.id.forgotpassword);


        forgotpassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "You clicked forgot password", Toast.LENGTH_SHORT).show();
                gotoforgotpwd();
            }
        });


        TextView btn_login = (TextView) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (CustomUtility.isInternetOn(mContext)) {
                    if(checkPermission()) {
                        submitForm();
                    }else {
                        requestPermission();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

        appUpdateManager = AppUpdateManagerFactory.create(LoginActivity.this);

        checkUpdate();


    }


    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, LoginActivity.IMMEDIATE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMMEDIATE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(LoginActivity.this, R.string.result_code + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(LoginActivity.this, R.string.update_success, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, R.string.update_failed + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }

    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        final String st_login = textview_login_id.getText().toString();
        final String st_pass = textview_pwd.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        param.add(new BasicNameValuePair("pernr", st_login));
        param.add(new BasicNameValuePair("pass", st_pass));

        try {


            progressDialog = ProgressDialog.show(this, "", "Please wait.. Connecting to Server!  ");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        obj = CustomHttpClient.executeHttpPost1(SapUrl.login_url, param);
                        Log.d("login", param.toString());
                        Log.d("login_obj", obj);

                        JSONArray ja = new JSONArray(obj);

                        String pernr = null;
                        String password = null;
                        String ename = null;
                        String mob_atnd = null;
                        String travel = null;
                        String hod = null;

                        for (int i = 0; i < 1; i++) {

                            JSONObject jo = ja.getJSONObject(i);

                            pernr = jo.getString("persno");
                            password = jo.getString("pass");
                            ename = jo.getString("name");
                            mob_atnd = jo.getString("mobAtnd");
                            travel = jo.getString("travel");
                            hod = jo.getString("hod");
                            Log.d("before", "" + st_pass);
                            st_pass1 = st_pass.toUpperCase();
                            Log.d("after", "" + st_pass);

                        }

                        Log.d("ja", ja.toString());
                        Log.d("pernr", pernr);
                        Log.d("password", password);
                        Log.d("st_pass", st_pass);
                        Log.d("mob_atnd", mob_atnd);
                        Log.d("travel", travel);
                        Log.d("hod", hod);


                        if (password.equals(st_pass1)) {

                            mloggedInModel = new LoggedInUser();
                            mloggedInModel.persist(mContext, st_login, st_pass, ename, mob_atnd, travel, hod);


                            dataHelper = new DatabaseHelper(LoginActivity.this);

                            dataHelper.insertLoginData(st_login, ename, st_pass, mob_atnd, travel, hod);

                            CustomUtility.setSharedPreference(mContext, "userID", pernr);


                            progressDialog.dismiss();
                            CustomUtility.setSharedPreference(mContext,"localconvenience","0");
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.putExtra("pernr", st_login);
                            startActivity(intent);

                            LoginActivity.this.finish();
                            finish();

                        } else {
                            progressDialog.dismiss();
                            String mesg = null;
                            mesg = "Wrong User Id /Password, Try Again";
                            Message msg = new Message();
                            msg.obj = mesg;
                            mHandler.sendMessage(msg);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();


        } catch (Exception e) {
            Log.d("msg", "" + e);
        }

    }

    private boolean validateName() {
        if (textview_login_id.getText().toString().trim().isEmpty()) {
            requestFocus(textview_login_id);
            return false;
        } 

        return true;
    }

    private boolean validatePassword() {
        if (textview_pwd.getText().toString().trim().isEmpty()) {
            requestFocus(textview_pwd);
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void gotoforgotpwd() {
        Toast.makeText(LoginActivity.this, "Your All Employee related Password will be reset !!",
                Toast.LENGTH_LONG).show();


        Intent intent = new Intent(LoginActivity.this, ForgotPwdActivity.class);
        startActivity(intent);

    }

    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_MEDIA_IMAGES},
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);

        }
    }


    private boolean checkPermission() {
        int cameraPermission =
                ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CAMERA);
        int writeExternalStorage =
                ContextCompat.checkSelfPermission(LoginActivity.this, WRITE_EXTERNAL_STORAGE);
        int ReadExternalStorage =
                ContextCompat.checkSelfPermission(LoginActivity.this, READ_EXTERNAL_STORAGE);

        int AccessCoarseLocation =
                ContextCompat.checkSelfPermission(LoginActivity.this, ACCESS_COARSE_LOCATION);
        int AccessFineLocation =
                ContextCompat.checkSelfPermission(LoginActivity.this, ACCESS_FINE_LOCATION);
        int ReadMediaImage =
                ContextCompat.checkSelfPermission(LoginActivity.this, READ_MEDIA_IMAGES);



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


                    if (ACCESSCAMERA && AccessCoarseLocation && AccessFineLocation && ReadMediaImage ) {
                        submitForm();

                    } else {
                        Toast.makeText(LoginActivity.this, R.string.all_permission, Toast.LENGTH_LONG).show();
                        requestPermission();
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
                        submitForm();

                    } else {
                        Toast.makeText(LoginActivity.this, R.string.all_permission, Toast.LENGTH_LONG).show();
                        requestPermission();
                    }

                }
            }
        }
    }
}
