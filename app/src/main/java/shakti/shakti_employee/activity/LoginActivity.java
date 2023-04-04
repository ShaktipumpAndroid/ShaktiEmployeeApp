package shakti.shakti_employee.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    EditText textview_login_id, textview_pwd;
    String synced;
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

//       con = new SAPWebService();

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

                textview_login_id = (EditText) findViewById(R.id.login_id);
                textview_pwd = (EditText) findViewById(R.id.pwd);

                if (CustomUtility.isInternetOn(mContext)) {
                    dataHelper = new DatabaseHelper(LoginActivity.this);
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
                    dataHelper.deleteDomTravelData1();
                    dataHelper.deleteExpTravelData1();
                    submitForm();
                } else {
                    Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });

        // Check for Permissions For Android GE 6.0

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        if (checkAndRequestPermissions()) {

        }
        //else {
        //  LoginActivity.this.finish();
        //  System.exit(0);
        //}
//            }
//        }, 2000);


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
//                           Toast.makeText(LoginActivity.this, "you clicked login", Toast.LENGTH_SHORT).show();
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

                            //saveLocally(pernr);
                            //saveLocally1(pernr);

                            progressDialog.dismiss();
                            CustomUtility.setSharedPreference(mContext,"localconvenience","0");
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.putExtra("pernr", st_login);
                            startActivity(intent);

                            LoginActivity.this.finish();
                            finish();

                        } else {
                            progressDialog.dismiss();
//                            Toast.makeText( getApplicationContext() , "Wrong User Id /Password, Try Again", Toast.LENGTH_LONG).show();
                            String mesg = null;
                            mesg = "Wrong User Id /Password, Try Again";
                            Message msg = new Message();
                            msg.obj = mesg;
                            mHandler.sendMessage(msg);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//
                }
            }).start();


        } catch (Exception e) {
            Log.d("msg", "" + e);
        }

    }

    private boolean validateName() {
        if (textview_login_id.getText().toString().trim().isEmpty()) {
            /*            layout_login.setError(getString(R.string.err_msg_login_id));*/
            requestFocus(textview_login_id);
            return false;
        } else {
            /*            layout_login.setErrorEnabled(false);*/
        }

        return true;
    }

    private boolean validatePassword() {
        if (textview_pwd.getText().toString().trim().isEmpty()) {
            /*            layout_password.setError(getString(R.string.err_msg_password));*/
            requestFocus(textview_pwd);
            return false;
        } else {
            /*            layout_password.setErrorEnabled(false);*/
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
        int locationPermission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (locationPermission1 != PackageManager.PERMISSION_GRANTED) {
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
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted


                } else {
                    // Permission Denied

                    LoginActivity.this.finish();
                    System.exit(0);

                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
