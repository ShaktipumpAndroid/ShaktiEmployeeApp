package shakti.shakti_employee.fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapter.Adapter_report_list;
import shakti.shakti_employee.R;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.model.TripHeadResponse;
import shakti.shakti_employee.model.TripListResponse;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.utility.NotifyInterface;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripRequestEnteredFragment extends Fragment implements View.OnClickListener {


    Context context;
    RecyclerView recyclerView;
    View.OnClickListener onclick;
    LinearLayout lin1, lin2;
    Adapter_report_list adapterReportList;
    String obj;
    Adapter_report_list adapterEmployeeList;
    JSONArray TripHeadResponseArray, TripListResponseArray;
    TripHeadResponse[] tripHeadResponses;
    TripListResponse[] tripListResponses;
    ArrayList<JSONArray> ja_success = new ArrayList<JSONArray>();
    ArrayList<JSONArray> ja_success1 = new ArrayList<JSONArray>();
    String from;
    NotifyInterface notifyInterface;
    int count, count1;
    private ProgressDialog progressDialog;
    private TextInputLayout start, end;
    private LoggedInUser userModel;
    private EditText start_date, end_date;
    private TextView save;
    private LinearLayoutManager layoutManagerSubCategory;
    private String mUserID, type, mStart, mEnd;
    private Handler mHandler;


    public TripRequestEnteredFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();

        onclick = this;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        userModel = new LoggedInUser(getActivity());

        type = "1";
        mUserID = userModel.uid;


       /* if (CustomUtility.isInternetOn()) {
            // Write Your Code What you want to do
            progressDialog = ProgressDialog.show(context, "Loading...", "please wait !");

            // GetEmployeeList_Task();
        } else {
            Toast.makeText(context, "No internet Connection ", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_req_entered, container, false);
        mHandler = new Handler();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.emp_list);

        lin1 = (LinearLayout) rootView.findViewById(R.id.lin1);
        lin2 = (LinearLayout) rootView.findViewById(R.id.lin2);

        start = (TextInputLayout) rootView.findViewById(R.id.start);
        end = (TextInputLayout) rootView.findViewById(R.id.end);

        save = (TextView) rootView.findViewById(R.id.save);

        start_date = (EditText) rootView.findViewById(R.id.start_date);
        end_date = (EditText) rootView.findViewById(R.id.end_date);
        start_date.setFocusable(false);
        end_date.setFocusable(false);


        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        start_date.setText(i2 + "/" + i1 + "/" + i);
                        mStart = start_date.getText().toString().trim();
                        parseDateToddMMyyyy1(mStart);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Start Date");
                datePickerDialog.show();
            }
        });


        // Date help for leave to
        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        end_date.setText(i2 + "/" + i1 + "/" + i);
                        mEnd = end_date.getText().toString().trim();
                        parseDateToddMMyyyy2(mEnd);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("End Date");
                datePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CustomUtility.isInternetOn(context)) {
                    // Write Your Code What you want to do

                    checkDataValtidation();
                } else {
                    Toast.makeText(context, "No internet Connection ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onClick(View view) {

    }

    public String parseDateToddMMyyyy1(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            mStart = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String parseDateToddMMyyyy2(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            mEnd = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void checkDataValtidation() {

        try {

            if (mStart == null || mStart.equalsIgnoreCase("") || mStart.equalsIgnoreCase(null)) {

                start_date.setFocusable(true);
                start_date.requestFocus();
                Toast.makeText(context, getResources().getString(R.string.Please_select_start), Toast.LENGTH_SHORT).show();
                if (progressDialog != null)
                    progressDialog.dismiss();

            } else if (mEnd == null || mEnd.equalsIgnoreCase("") || mEnd.equalsIgnoreCase(null)) {

                end_date.setFocusable(true);
                end_date.requestFocus();

                Toast.makeText(context, getResources().getString(R.string.Please_select_end), Toast.LENGTH_SHORT).show();
                if (progressDialog != null)
                    progressDialog.dismiss();

            } else {
                ja_success.clear();
                ja_success1.clear();
                GetTravelRecordedList_Task();
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


    private void GetTravelRecordedList_Task() {

        final String from = "req";
        try {

            progressDialog = ProgressDialog.show(context, "Loading...", "please wait !");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

                    param.add(new BasicNameValuePair("pernr", mUserID));
                    param.add(new BasicNameValuePair("begda", mStart));
                    param.add(new BasicNameValuePair("endda", mEnd));
                    param.add(new BasicNameValuePair("type", type));
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);
//                           Toast.makeText(LoginActivity.this, "you clicked login", Toast.LENGTH_SHORT).show();
                        obj = CustomHttpClient.executeHttpPost1(SapUrl.TRAVEL_EXP_VIEW, param);
                        Log.d("login", param.toString());
                        Log.d("login_obj", obj);

                        if (obj != "") {

                            progressDialog.dismiss();
                            JSONObject jo_success = new JSONObject(obj);


                            count = jo_success.getJSONArray("travel_head").length();
                            count1 = jo_success.getJSONArray("travel_item").length();

                            Log.e("TRAVELHEADSIZE", "%%%%%" + jo_success.getJSONArray("travel_head").length());
                            Log.e("TRAVELITEMSIZE", "%%%%%" + jo_success.getJSONArray("travel_item").length());


                            for (int i = 0; i < count; i++) {
                                ja_success.add(jo_success.getJSONArray("travel_head"));
                            }

                            for (int i = 0; i < count1; i++) {
                                ja_success1.add(jo_success.getJSONArray("travel_item"));
                            }

                            if (ja_success.size() > 0) {

                                final int count = ja_success.size();
                                final String count1 = String.valueOf(ja_success1.size());

                                for (int i = 0; i < ja_success.size(); i++) {

                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {

                                            lin1.setVisibility(View.VISIBLE);
                                            lin2.setVisibility(View.GONE);

                                            adapterEmployeeList = new Adapter_report_list(context, count, count1, ja_success, ja_success1, from, new NotifyInterface() {
                                                @Override
                                                public void onSuccessNotify() {
                                                    adapterEmployeeList.notifyDataSetChanged();
                                                }
                                            });

                                            layoutManagerSubCategory = new LinearLayoutManager(context);
                                            layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                                            recyclerView.setLayoutManager(layoutManagerSubCategory);
                                            recyclerView.setAdapter(adapterEmployeeList);
                                            adapterEmployeeList.notifyDataSetChanged();


                                        }

                                    });
                                }
                            } else {

                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {

                                        progressDialog.dismiss();
                                        lin1.setVisibility(View.GONE);
                                        lin2.setVisibility(View.VISIBLE);

                                    }

                                });
                            }

                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    progressDialog.dismiss();
                                    lin1.setVisibility(View.GONE);
                                    lin2.setVisibility(View.VISIBLE);

                                }

                            });
                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                        progressDialog.dismiss();

                    }
//
                }
            }).start();


        } catch (Exception e) {
            Log.d("msg", "" + e);
            progressDialog.dismiss();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ja_success.clear();
        ja_success1.clear();
        if (mStart != null && mEnd != null) {
            GetTravelRecordedList_Task();
        }
    }


}
