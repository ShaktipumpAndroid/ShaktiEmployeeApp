package shakti.shakti_employee.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import shakti.shakti_employee.R;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.other.SapUrl;

public class ForgotPwdActivity extends AppCompatActivity {


    EditText textview_login_id, textview_mobno, textview_dob;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(ForgotPwdActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private ProgressDialog progressDialog;
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);

        ImageView datepick = (ImageView) findViewById(R.id.bt_datepick);
        ImageView resetpassword = (ImageView) findViewById(R.id.resetpassword);
        resetpassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Toast.makeText(ForgotPwdActivity.this, "You request for reset password", Toast.LENGTH_LONG).show();
                forgotpwd();

            }
        });

        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(ForgotPwdActivity.this, "You click datepicker", Toast.LENGTH_LONG).show();*/
                dateView = (TextView) findViewById(R.id.dob);
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                showDate(year, month + 1, day);
                setDate(view);

            }

            @SuppressWarnings("deprecation")
            public void setDate(View view) {
                showDialog(999);
                /*Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();*/
            }
        });

    }

    private void forgotpwd() {
        textview_login_id = (EditText) findViewById(R.id.login_id);
        textview_mobno = (EditText) findViewById(R.id.mobno);
        textview_dob = (EditText) findViewById(R.id.dob);

        String st_login = textview_login_id.getText().toString();
        String st_mobno = textview_mobno.getText().toString();
        String st_dob = textview_dob.getText().toString();

        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();

        param1.add(new BasicNameValuePair("pernr", st_login));
        param1.add(new BasicNameValuePair("mobno", st_mobno));
        param1.add(new BasicNameValuePair("dob", st_dob));

        progressDialog = ProgressDialog.show(this, "", "Connecting to server..please wait !");

        new Thread() {
            public void run() {
                try {

//            Toast.makeText(ForgotPwdActivity.this, "Your All Employee related Password will be reset", Toast.LENGTH_LONG).show();

                    String obj = CustomHttpClient.executeHttpPost1(SapUrl.forgotpass_url, param1);

                    if (obj != null) {

                        progressDialog.dismiss();
                        Log.d("forgotpass", param1.toString());

                        JSONArray ja = new JSONArray(obj);

                        String pernr = null;
                        String mobno = null;
                        String dob = null;
                        String msg = null;

                        for (int i = 0; i < 1; i++) {

                            JSONObject jo = ja.getJSONObject(i);

/*                pernr = jo.getString("persno");
                mobno = jo.getString("mobno");
                dob = jo.getString("dob");*/
                            msg = jo.getString("msg");
                            Message mseg = new Message();
                            mseg.obj = msg;
                            mHandler.sendMessage(mseg);

                        }

                        Log.d("msg", ja.toString());

                        Toast.makeText(ForgotPwdActivity.this, msg, Toast.LENGTH_SHORT).show();

/*            if(!msg.equals( '' )){
               Toast.makeText(ForgotPwdActivity.this, "Enter correct re", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(ForgotPwdActivity.this, Login_Activity.class);
               startActivity(intent);

            }
            else

            {
                Toast.makeText( getApplicationContext() , "Wrong User Id /Password, Try Again", Toast.LENGTH_LONG).show();
            }*/


                    }


                } catch (Exception e) {
                    Log.d("msg", "" + e);
                }
            }

        }.start();
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}
