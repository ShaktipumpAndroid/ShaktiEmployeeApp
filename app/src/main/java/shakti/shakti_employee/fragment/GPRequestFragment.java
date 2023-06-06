package shakti.shakti_employee.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import shakti.shakti_employee.R;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.SapUrl;

public class GPRequestFragment extends Fragment {

    String gp_request_msg = null;

    int index;

    EditText gp_date, gp_time, gp_exp_date, gp_exp_time, gp_visitplace, gp_purpose;
    AutoCompleteTextView gp_charge;
    ArrayList<String> languages = new ArrayList<String>();
    DatabaseHelper dataHelper = null;

    String req_type_string, gp_type_string;
    @SuppressLint("HandlerLeak")
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(getActivity(), mString, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    };

    private LoggedInUser userModel;
    private Context mContext;
    private ProgressDialog progressDialog;

    LinearLayout expectedComebackDate,expectedComebackTime;

    public GPRequestFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gprequest, container, false);

        dataHelper = new DatabaseHelper(getActivity());


        ImageButton gp_date_img, gp_time_img, gp_exp_date_img, gp_exp_time_img;


        gp_date = (EditText) v.findViewById(R.id.gp_date);
        gp_time = (EditText) v.findViewById(R.id.gp_time);
        gp_exp_date = (EditText) v.findViewById(R.id.gp_exp_date);
        gp_exp_time = (EditText) v.findViewById(R.id.gp_exp_time);
        gp_visitplace = (EditText) v.findViewById(R.id.gp_visitplace);
        gp_purpose = (EditText) v.findViewById(R.id.gp_purpose);
        gp_charge = (AutoCompleteTextView) v.findViewById(R.id.gp_charge);
        gp_date_img = (ImageButton) v.findViewById(R.id.gp_date_img);
        gp_time_img = (ImageButton) v.findViewById(R.id.gp_time_img);
        gp_exp_date_img = (ImageButton) v.findViewById(R.id.gp_exp_date_img);
        gp_exp_time_img = (ImageButton) v.findViewById(R.id.gp_exp_time_img);
        expectedComebackDate = v.findViewById(R.id.expectedComebackDate);
        expectedComebackTime = v.findViewById(R.id.expectedComebackTime);

        languages = dataHelper.getDiscription();
        Log.d("languages", " " + languages);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
        gp_charge.setAdapter(adapter);
        gp_charge.setThreshold(1);


        gp_date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;

                        try {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                            String selectedDate = i2 + "/" + i1 + "/" + i;
                            Date date1 = sdf.parse(selectedDate);

                            TextView tvDt = (TextView) getView().findViewById(R.id.gp_date);
                            tvDt.setText(sdf.format(date1));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Completion Date");
                datePickerDialog.show();
            }
        });


        gp_exp_date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;

                        try {

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                            String selectedDate = i2 + "/" + i1 + "/" + i;
                            Date date1 = sdf.parse(selectedDate);

                            TextView tvDt = (TextView) getView().findViewById(R.id.gp_exp_date);
                            tvDt.setText(sdf.format(date1));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Expected Comeback Date");
                datePickerDialog.show();
            }
        });


        gp_time_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            try {

                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                                String selectedDate = hourOfDay + ":" + minute + ":" + "00";
                                Date date1 = sdf.parse(selectedDate);
                                gp_time.setText(sdf.format(date1));
                            }
                            catch (ParseException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });


        gp_exp_time_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            try {

                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

                                String selectedDate = hourOfDay + ":" + minute + ":" + "00";
                                Date date1 = sdf.parse(selectedDate);
                                gp_exp_time.setText(sdf.format(date1));
                            }
                            catch (ParseException e)
                            {
                                e.printStackTrace();
                            }


                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
            }
        });


        // Spinner selection for Request Type

        final String[] st_req_type = {"Personal", "Official",};
        Spinner spinner1 = (Spinner) v.findViewById(R.id.req_type);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(),
                R.layout.spinner_item, st_req_type);
        adapter1.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(adapter1);

        // Spinner selection for Request Type

        final String[] st_gp_type = {"Returnable", "Non-Returnable",};
        Spinner spinner2 = (Spinner) v.findViewById(R.id.gp_type);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(),
                R.layout.spinner_item, st_gp_type);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        spinner2.setAdapter(adapter2);

        // Spinner selection item for Gatepass Type
        final Spinner finalSpinner1 = spinner1;
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                req_type_string = finalSpinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        // Spinner selection item for Gatepass Type
        final Spinner finalSpinner2 = spinner2;
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                index = arg0.getSelectedItemPosition();
                gp_type_string = finalSpinner2.getSelectedItem().toString();
                if(gp_type_string.equals("Returnable")){
                    expectedComebackDate.setVisibility(View.VISIBLE);
                    expectedComebackTime.setVisibility(View.VISIBLE);
                }else {
                    expectedComebackDate.setVisibility(View.GONE);
                    expectedComebackTime.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        // Save Date (Send Request to SAP)
        TextView save = (TextView) v.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (!gp_date.getText().toString().trim().isEmpty() &&
                        !gp_time.getText().toString().trim().isEmpty() &&
                        !gp_purpose.getText().toString().trim().isEmpty() &&
                        !gp_charge.getText().toString().trim().isEmpty() &&
                        !gp_visitplace.getText().toString().trim().isEmpty()) {

                    String tmp = "";
                    Log.e("DATA", "%%%%" + req_type_string);

                    if (gp_type_string.equalsIgnoreCase("Returnable")) {
                        if (gp_exp_date.getText().toString().trim().isEmpty()) {
                            tmp = "X";
                            Toast.makeText(getActivity().getApplicationContext(), "Select Expected Comeback Date", Toast.LENGTH_SHORT).show();

                        }
                        if (gp_exp_time.getText().toString().trim().isEmpty()) {
                            tmp = "X";
                            Toast.makeText(getActivity().getApplicationContext(), "Select Expected Comeback Time", Toast.LENGTH_SHORT).show();

                        }
                    }

                    if (!tmp.equalsIgnoreCase("X")) {
                        submitForm();   // Send Request to SAP
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Select Expected Comeback Date/Time", Toast.LENGTH_SHORT).show();

                    }


                } else {

                    if (gp_date.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Select Gatepass Date", Toast.LENGTH_SHORT).show();

                    }
                    if (gp_time.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Select Gatepass Time", Toast.LENGTH_SHORT).show();

                    }
                    if (gp_purpose.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Enter Purpose", Toast.LENGTH_SHORT).show();

                    }
                    if (gp_visitplace.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Enter Place to visit", Toast.LENGTH_SHORT).show();

                    }
                    if (gp_charge.getText().toString().trim().isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Enter Charge Given And Select From List", Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });

        return v;

    }


    private void submitForm() {


        mContext = getActivity();
        userModel = new LoggedInUser(mContext);
        SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        String st_pernr = userModel.uid;
        String st_gp_date = gp_date.getText().toString();
        String st_gp_time = gp_time.getText().toString();
        String st_req_type = req_type_string.toString();
        String st_gp_type = gp_type_string.toString();
        String st_gp_exp_date = gp_exp_date.getText().toString();

        try {
            st_gp_exp_date = sdf.format(fromUser.parse(st_gp_exp_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String st_gp_exp_time = gp_exp_time.getText().toString();
        String st_gp_vp = gp_visitplace.getText().toString();
        String st_gp_purpose = gp_purpose.getText().toString();
        String st_gp_charge = gp_charge.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        Log.d("test", st_req_type + "---" + st_gp_type);

        param.add(new BasicNameValuePair("app_pernr", st_pernr));
        param.add(new BasicNameValuePair("gp_date", st_gp_date));
        param.add(new BasicNameValuePair("gp_time", st_gp_time));
        param.add(new BasicNameValuePair("req_type", st_req_type));
        param.add(new BasicNameValuePair("gp_type", st_gp_type));
        param.add(new BasicNameValuePair("gp_exp_date", st_gp_exp_date));
        param.add(new BasicNameValuePair("gp_exp_time", st_gp_exp_time));
        param.add(new BasicNameValuePair("gp_vp", st_gp_vp));
        param.add(new BasicNameValuePair("gp_purpose", st_gp_purpose));
        param.add(new BasicNameValuePair("gp_charge", st_gp_charge));


        progressDialog = ProgressDialog.show(getActivity(), "", "Connecting to server..please wait !");


        new Thread() {
            public void run() {

                try {

                    String obj = CustomHttpClient.executeHttpPost1(SapUrl.gp_request, param);


                    if (obj != null) {
                        progressDialog.dismiss();
                        Log.d("gp_request", param.toString());
                        JSONArray ja = new JSONArray(obj);
                        for (int i = 0; i < 1; i++) {
                            JSONObject jo = ja.getJSONObject(i);

                            gp_request_msg = jo.getString("text");
                            Log.d("gp_return_msg", gp_request_msg);
                            /*Toast.makeText(getActivity(), leave_return_msg, Toast.LENGTH_LONG).show();*/
                            Message msg = new Message();
                            msg.obj = gp_request_msg;
                            mHandler.sendMessage(msg);

                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), gp_request_msg, Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity().getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.d("msg", "" + e);
                }
            }

        }.start();
    }





}
