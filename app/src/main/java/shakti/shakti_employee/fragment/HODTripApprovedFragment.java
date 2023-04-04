package shakti.shakti_employee.fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import adapter.Adapter_report_list1;
import shakti.shakti_employee.R;
import shakti.shakti_employee.bean.TravelHeadBean;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.model.TripHeadResponse;
import shakti.shakti_employee.model.TripListResponse;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.utility.NotifyInterface;


/**
 * A simple {@link Fragment} subclass.
 */
public class HODTripApprovedFragment extends Fragment implements View.OnClickListener {

    Context context;
    ListView listView;
    View.OnClickListener onclick;
    LinearLayout lin1, lin2;
    Adapter_report_list1 adapterEmployeeList;
    TripHeadResponse[] tripHeadResponses;
    TripListResponse[] tripListResponses;
    DatabaseHelper db = null;
    TravelHeadBean travelHeadBean;
    ArrayList<JSONArray> ja_success = new ArrayList<JSONArray>();
    ArrayList<JSONArray> ja_success1 = new ArrayList<JSONArray>();
    int count, count1;
    CardView list;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManagerSubCategory;
    private LoggedInUser userModel;
    /* private EditText pernr_no;*/
    private TextView save;
    private String mUserID, type, mPernr, obj;
    private EditText start_date, end_date;
    private TextInputLayout start, end;
    private String mStart, mEnd;

    public HODTripApprovedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();

        onclick = this;
        db = new DatabaseHelper(context);

        userModel = new LoggedInUser(context);

        type = "4";
        mUserID = userModel.uid;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hodtrip_pend_appro1, container, false);

        listView = rootView.findViewById(R.id.emp_list);

        lin1 = rootView.findViewById(R.id.lin1);
        lin2 = rootView.findViewById(R.id.lin2);
        list = rootView.findViewById(R.id.list);


        //pernr_no = (EditText) rootView.findViewById(R.id.pernr_no);

        start = (TextInputLayout) rootView.findViewById(R.id.start);
        end = (TextInputLayout) rootView.findViewById(R.id.end);

        start_date = (EditText) rootView.findViewById(R.id.start_date);
        end_date = (EditText) rootView.findViewById(R.id.end_date);
        save = (TextView) rootView.findViewById(R.id.save);
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



   /*     pernr_no.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = pernr_no.getText().toString().toLowerCase();
                try {
                    adapterEmployeeList.filter(text);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(context, "Data not Available", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });*/


        return rootView;
    }


    @Override
    public void onClick(View view) {

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

                if (CustomUtility.isInternetOn(context)) {

                    GetTravelApproveList_Task();

                } else {
                    Toast.makeText(context, "No Internet Connection Found, You Are In Offline Mode.", Toast.LENGTH_SHORT).show();

                }

            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }


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

    private void GetTravelApproveList_Task() {

        final String from = "app";


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
                        obj = CustomHttpClient.executeHttpPost1(SapUrl.TRIP_HOD_EXP_VIEW, param);
                        Log.d("login", param.toString());
                        Log.d("login_obj", obj);

                        if (obj != "") {

                            progressDialog.dismiss();
                            JSONObject jo_success = new JSONObject(obj);

                            Log.e("DATA", "&&&&" + jo_success.toString());
                          /*  final JSONArray ja_success = jo_success.getJSONArray("travel_head");
                            final JSONArray ja_success1 = jo_success.getJSONArray("travel_item");*/

                            count = jo_success.getJSONArray("travel_head").length();
                            count1 = jo_success.getJSONArray("travel_item").length();

                            Log.e("TRAVELHEADSIZE", "%%%%%" + jo_success.getJSONArray("travel_head").length());
                            Log.e("TRAVELITEMSIZE", "%%%%%" + jo_success.getJSONArray("travel_item").length());
                            db.deleteTravelHeadData();

                            for (int i = 0; i < count; i++) {
                                ja_success.add(jo_success.getJSONArray("travel_head"));
                            }

                            for (int i = 0; i < count1; i++) {
                                ja_success1.add(jo_success.getJSONArray("travel_item"));
                            }


                            if (ja_success.size() > 0) {


                                //JSONArray jArrAnswer = new JSONArray(ja_success);
                                for (int i = 0; i < ja_success.size(); i++) {

                                    //JSONObject jo = jArrAnswer.getJSONObject(i);
                                    JSONObject jo = ja_success.get(i).getJSONObject(i);

                                    final String link = jo.getString("link");
                                    final String status = jo.getString("antrg_txt");
                                    final String startdate = jo.getString("datv1_char");
                                    final String enddate = jo.getString("datb1_char");
                                    final String triptotal = jo.getString("trip_total");
                                    final String pernr = jo.getString("pernr");
                                    final String reinr = jo.getString("reinr");
                                    final String city = jo.getString("zort1");
                                    final String country_text = jo.getString("zland_txt");


                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {


                                            list.setVisibility(View.VISIBLE);
                                            lin2.setVisibility(View.GONE);

                                            travelHeadBean = new TravelHeadBean(link, status, startdate, enddate, triptotal, pernr, reinr, city, country_text);

                                            db.insertTravelHead(travelHeadBean);

                                            ArrayList<TravelHeadBean> travelHeadBeanArrayList = new ArrayList<TravelHeadBean>();
                                            travelHeadBeanArrayList = db.getTravelHead(context);


                                            adapterEmployeeList = new Adapter_report_list1(context, count, count1, travelHeadBeanArrayList, ja_success, ja_success1, from, new NotifyInterface() {
                                                @Override
                                                public void onSuccessNotify() {
                                                    adapterEmployeeList.notifyDataSetChanged();
                                                }
                                            });
                                            listView.setAdapter(adapterEmployeeList);
                                            adapterEmployeeList.notifyDataSetChanged();

                                        }

                                    });

                                }

                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {

                                        progressDialog.dismiss();
                                        list.setVisibility(View.GONE);
                                        lin2.setVisibility(View.VISIBLE);

                                    }

                                });
                            }

                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    progressDialog.dismiss();
                                    list.setVisibility(View.GONE);
                                    lin2.setVisibility(View.VISIBLE);

                                }

                            });
                        }
                    } catch (Exception e) {

                        progressDialog.dismiss();
                        e.printStackTrace();

                    }
//
                }
            }).start();


        } catch (Exception e) {


            progressDialog.dismiss();


            Log.d("msg", "" + e);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStart != null && mEnd != null) {
            GetTravelApproveList_Task();
        }
    }
}
