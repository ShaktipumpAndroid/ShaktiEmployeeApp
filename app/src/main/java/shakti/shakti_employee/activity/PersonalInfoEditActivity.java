package shakti.shakti_employee.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import shakti.shakti_employee.R;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SapUrl;

public class PersonalInfoEditActivity extends AppCompatActivity {

    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
    DatabaseHelper dataHelper = null;


    TextView pi_email, pi_house_no, pi_line1, pi_line2, pi_city, pi_district, pi_postal_code;
    EditText pi_email_val_edit, pi_house_no_val_edit, pi_line1_val_edit, pi_line2_val_edit,
            pi_city_val_edit, pi_district_val_edit, pi_postal_code_val_edit;
    Button btn_save;

    Spinner spinner_type = null;
    String usrid, house_no, line1, line2, city, district, postal_code;
    ProgressDialog progressBar;
    JSONArray ja_email = null;
    String message_status, message_text;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(PersonalInfoEditActivity.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar toolbar;
    private Handler progressBarHandler = new Handler();
    private int progressBarStatus = 0;
    private LoggedInUser userModel;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_edit);

        mContext = this;
        userModel = new LoggedInUser(mContext);

        usrid = getIntent().getStringExtra("USRID");
        house_no = getIntent().getStringExtra("HOUSE_NO");
        line1 = getIntent().getStringExtra("LINE1");
        line2 = getIntent().getStringExtra("LINE2");
        city = getIntent().getStringExtra("CITY");
        district = getIntent().getStringExtra("DISTRICT");
        postal_code = getIntent().getStringExtra("POSTAL_CODE");

        Log.d("get_email", usrid);


        spinner_type = (Spinner) findViewById(R.id.spinner_type);
        btn_save = (Button) findViewById(R.id.btn_save);

//        pi_mob = (TextView) findViewById(R.id.pi_mob);
//        pi_mob_val_edit = (EditText) findViewById(R.id.pi_mob_val_edit);
//        pi_mob_val_edit.setFocusable(true); //to enable it

        pi_email = (TextView) findViewById(R.id.pi_email);
        pi_email_val_edit = (EditText) findViewById(R.id.pi_email_val_edit);
        pi_email_val_edit.setFocusable(true); //to enable it

//        pi_email = (TextView) findViewById(R.id.pi_email);
//        pi_email_val_edit = (EditText) findViewById(R.id.pi_email_val_edit);

        pi_house_no = (TextView) findViewById(R.id.pi_house_no);
        pi_house_no_val_edit = (EditText) findViewById(R.id.pi_house_no_val_edit);

        pi_line1 = (TextView) findViewById(R.id.pi_line1);
        pi_line1_val_edit = (EditText) findViewById(R.id.pi_line1_val_edit);

        pi_line2 = (TextView) findViewById(R.id.pi_line2);
        pi_line2_val_edit = (EditText) findViewById(R.id.pi_line2_val_edit);

        pi_city = (TextView) findViewById(R.id.pi_city_text);
        pi_city_val_edit = (EditText) findViewById(R.id.pi_city_val_edit);

        pi_district = (TextView) findViewById(R.id.pi_district_text);
        pi_district_val_edit = (EditText) findViewById(R.id.pi_district_val_edit);

        pi_postal_code = (TextView) findViewById(R.id.pi_postal_code);
        pi_postal_code_val_edit = (EditText) findViewById(R.id.pi_postal_code_val_edit);


        List<String> list = new ArrayList<String>();
        list.clear();
//        list.add("Personal Mobile No");
        list.add("Peronal Email ID");
        list.add("Temporary Address");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(dataAdapter);


        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


//                Log.d("edit_email2",usrid);

//                if (spinner_type.getSelectedItem().toString().equalsIgnoreCase("Personal Mobile No")){
//
//
//                    pi_mob.setVisibility(View.VISIBLE); //to enable it
//                    pi_mob_val_edit.setVisibility(View.VISIBLE); //to enable it
////                    pi_mob_val_edit.setFocusable(true); //to enable it
////                    pi_email_val_edit.setFocusable(false); //to disable it
//                    pi_email_val_edit.setVisibility(View.GONE);
//                    pi_email.setVisibility(View.GONE);
//
//                    pi_house_no_val_edit.setVisibility(View.GONE);
//                    pi_house_no.setVisibility(View.GONE);
//
//                    pi_line1_val_edit.setVisibility(View.GONE);
//                    pi_line1.setVisibility(View.GONE);
//
//                    pi_line2_val_edit.setVisibility(View.GONE);
//                    pi_line2.setVisibility(View.GONE);
//
//                    pi_city_val_edit.setVisibility(View.GONE);
//                    pi_city.setVisibility(View.GONE);
//
//                    pi_district_val_edit.setVisibility(View.GONE);
//                    pi_district.setVisibility(View.GONE);
//
//                    pi_postal_code_val_edit.setVisibility(View.GONE);
//                    pi_postal_code.setVisibility(View.GONE);
//
//
//                }

                if (spinner_type.getSelectedItem().toString().equalsIgnoreCase("Peronal Email ID")) {


////                    pi_mob_val_edit.setFocusable(false); //to disable it
//                    pi_mob_val_edit.setVisibility(View.GONE); // invisible
//                    pi_mob.setVisibility(View.GONE); // invisible


                    pi_email_val_edit.setVisibility(View.VISIBLE);
                    pi_email_val_edit.setFocusable(true); //to enable it
                    pi_email_val_edit.setText(usrid);
                    pi_email.setVisibility(View.VISIBLE);

                    pi_house_no_val_edit.setVisibility(View.GONE);
                    pi_house_no.setVisibility(View.GONE);

                    pi_line1_val_edit.setVisibility(View.GONE);
                    pi_line1.setVisibility(View.GONE);

                    pi_line2_val_edit.setVisibility(View.GONE);
                    pi_line2.setVisibility(View.GONE);

                    pi_city_val_edit.setVisibility(View.GONE);
                    pi_city.setVisibility(View.GONE);

                    pi_district_val_edit.setVisibility(View.GONE);
                    pi_district.setVisibility(View.GONE);

                    pi_postal_code_val_edit.setVisibility(View.GONE);
                    pi_postal_code.setVisibility(View.GONE);


                }


                if (spinner_type.getSelectedItem().toString().equalsIgnoreCase("Temporary Address")) {


//                    pi_mob_val_edit.setFocusable(false); //to disable it
//                    pi_mob_val_edit.setVisibility(View.GONE); // invisible
//                    pi_mob.setVisibility(View.GONE); // invisible

                    pi_email_val_edit.setVisibility(View.GONE);
                    pi_email.setVisibility(View.GONE);


                    pi_house_no_val_edit.setVisibility(View.VISIBLE);
                    pi_house_no_val_edit.setFocusable(true); //to enable it
                    pi_house_no_val_edit.setText(house_no);
                    pi_house_no.setVisibility(View.VISIBLE);


                    pi_line1_val_edit.setVisibility(View.VISIBLE);
                    pi_line1_val_edit.setFocusable(true); //to enable it
                    pi_line1_val_edit.setText(line1);
                    pi_line1.setVisibility(View.VISIBLE);


                    pi_line2_val_edit.setVisibility(View.VISIBLE);
                    pi_line2_val_edit.setFocusable(true); //to enable it
                    pi_line2_val_edit.setText(line2);
                    pi_line2.setVisibility(View.VISIBLE);


                    pi_city_val_edit.setVisibility(View.VISIBLE);
                    pi_city_val_edit.setFocusable(true); //to enable it
                    pi_city_val_edit.setText(city);
                    pi_city.setVisibility(View.VISIBLE);


                    pi_district_val_edit.setVisibility(View.VISIBLE);
                    pi_district_val_edit.setFocusable(true); //to enable it
                    pi_district_val_edit.setText(district);
                    pi_district.setVisibility(View.VISIBLE);


                    pi_postal_code_val_edit.setVisibility(View.VISIBLE);
                    pi_postal_code_val_edit.setFocusable(true); //to enable it
                    pi_postal_code_val_edit.setText(postal_code);
                    pi_postal_code.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spinner_type.getSelectedItem().toString().equalsIgnoreCase("Peronal Email ID")) {

                    if (!pi_email_val_edit.getText().toString().trim().isEmpty()) {
                        updateEmailID();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter Email Address To Update", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

//        pi_mob_val_edit = (EditText) findViewById(R.id.pi_mob_val_edit);
//        pi_mob_val_edit.setFocusable(false); //to disable it
//
//        pi_email_val_edit = (EditText) findViewById(R.id.pi_email_val_edit);
//        pi_email_val_edit.setFocusable(false); //to disable it
//
//        pi_add_val_edit = (EditText) findViewById(R.id.pi_add_val_edit);
//        pi_add_val_edit.setFocusable(false); //to disable it
//
//
//        Button btn_edit = (Button)findViewById(R.id.btn_edit) ;
//
//
//        btn_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                pi_mob_val_edit.setEnabled(true);
//                pi_mob_val_edit.setFocusableInTouchMode(true); //to enable it
//                pi_mob_val_edit.setCursorVisible(true);
//
//                pi_email_val_edit.setEnabled(true);
//                pi_email_val_edit.setFocusableInTouchMode(true); //to enable it
//                pi_email_val_edit.setCursorVisible(true);
//
//                pi_add_val_edit.setEnabled(true);
//                pi_add_val_edit.setFocusableInTouchMode(true); //to enable it
//                pi_add_val_edit.setCursorVisible(true);
//
//
//            }
//        });


//        get_personal_info();
    }

    private void get_personal_info() {

        dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_EMPLOYEE_INFO;

        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.d("pers_info", "" + cursor.getCount());

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

//                // Read columns data
//                String dept = cursor.getString(2);
//                pi_dept_val.setText(dept);
//
//                String desig = cursor.getString(3);
//                pi_desig_val.setText(desig);
//
//                String mob = cursor.getString(4);
//                pi_mob_val.setText(mob);
//
//                String email = cursor.getString(5);
//                pi_email_val.setText(email);
//
//                String hod = cursor.getString(6);
//                pi_hod_val.setText(hod);
//
//                String add = cursor.getString(7);
//                pi_add_val.setText(add);
//
//                String dob = cursor.getString(8);
//                pi_dob_val.setText(dob);
//
//                String acno = cursor.getString(9);
//                pi_acno_val.setText(acno);
//
//                String bank = cursor.getString(10);
//                pi_bank_val.setText(bank);


            }

        }
    }


    public void updateEmailID() {


        if (CustomUtility.isInternetOn(mContext)) {

            progressBar = new ProgressDialog(PersonalInfoEditActivity.this);
            progressBar.setCancelable(true);
            // progressBar.setCancelable(true);
            progressBar.setMessage("Updating Data...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            //reset progress bar and filesize status
            progressBarStatus = 0;

            new Thread(new Runnable() {
                public void run() {

                    ja_email = new JSONArray();
                    JSONObject jsonObj_email = new JSONObject();

                    try {

                        jsonObj_email.put("pernr_edit", userModel.uid);
                        jsonObj_email.put("email_id", pi_email_val_edit.getText().toString());

                        ja_email.put(jsonObj_email);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    param.add(new BasicNameValuePair("PERNR_EDIT_EMAIL", String.valueOf(ja_email)));

                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);

                        String obj = CustomHttpClient.executeHttpPost1(SapUrl.employee_info_edit, param);


                        if (obj != "") {

                            JSONObject jo_success = new JSONObject(obj);
                            JSONArray ja_success = jo_success.getJSONArray("email_edit_message");

                            for (int i = 0; i < ja_success.length(); i++) {

                                JSONObject jo = ja_success.getJSONObject(i);

                                message_status = jo.getString("status");
                                message_text = jo.getString("text");

                            }

                            progressBar.cancel();
                            progressBar.dismiss();

                            if (message_status.trim().equalsIgnoreCase("S")) {

                                Message msg = new Message();
                                msg.obj = "Record Successfully Updated";
                                mHandler.sendMessage(msg);

                                startActivity(new Intent(PersonalInfoEditActivity.this, DashboardActivity.class));
                            } else if (message_status.trim().equalsIgnoreCase("E")) {

                                Message msg = new Message();
                                msg.obj = message_text;
                                mHandler.sendMessage(msg);
                            }

                        }

                        progressBar.cancel();
                        progressBar.dismiss();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }).start();

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


}