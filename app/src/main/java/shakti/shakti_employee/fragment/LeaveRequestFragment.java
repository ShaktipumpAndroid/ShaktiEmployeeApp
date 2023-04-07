package shakti.shakti_employee.fragment;

import static shakti.shakti_employee.R.id;
import static shakti.shakti_employee.R.layout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.other.SelectDateFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeaveRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeaveRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaveRequestFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static FragmentManager mFragmentManager;
    String leavetype;
    String obj_leave_balance;

    EditText leave_from, leave_to, leave_reason;
    Spinner spinner;
    int index;
    ImageButton bt_dp_lev_frm, bt_dp_lev_to;
    String st_leave_type, st_leave_duration;
    AutoCompleteTextView per_chrg1, per_chrg2, per_chrg3, per_chrg4;
    MultiAutoCompleteTextView text1;

    DatabaseHelper dataHelper = null;

    ArrayList<String> languages = new ArrayList<String>();
    ArrayList<String> leavebalance = new ArrayList<String>();
    String leave_return_msg = null;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(getActivity(), mString, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    };
    private FragmentPagerAdapter fragmentPager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DashboardActivity dashboardActivity;
    private OnFragmentInteractionListener mListener;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private LoggedInUser userModel;
    private Context mContext;
    private Toolbar toolbar;

    /*  final String string_url = "http://shaktidev.shaktipumps.com:8000/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zhr_emp_app_1/leave_create.htm";*/
    private ProgressDialog progressDialog;


    public LeaveRequestFragment() {
        // Required empty public constructor
    }

    public static LeaveRequestFragment getInstance(FragmentManager fm) {
        mFragmentManager = fm;
        return new LeaveRequestFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LeaveRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaveRequestFragment newInstance() {
        LeaveRequestFragment fragment = new LeaveRequestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setDashBoard(DashboardActivity dashBoard) {
        this.dashboardActivity = dashBoard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getActivity().setTitle("Leave Request");*/
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        mContext = getActivity();
        userModel = new LoggedInUser(mContext);

        dataHelper = new DatabaseHelper(getActivity());
//        insertLeaveBalance();
        leavebalance = dataHelper.getLeaveBalance();


//        try {
//            Thread.sleep(3000);
//                   }
//        catch (InterruptedException ex) { android.util.Log.d("Shakti Employee", ex.toString()); }


        Log.d("leavebalance", " " + leavebalance);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment

        View v = inflater.inflate(layout.fragment_leave_request, container, false);

        /*       Toast.makeText(getActivity(), "Leave Request form", Toast.LENGTH_SHORT).show();*/
        leave_from = v.findViewById(id.leave_from);
        leave_to = v.findViewById(id.leave_to);
        leave_reason = v.findViewById(id.leave_reason);

        per_chrg1 = v.findViewById(id.per_chrg1);
        per_chrg2 = v.findViewById(id.per_chrg2);
        per_chrg3 = v.findViewById(id.per_chrg3);
        per_chrg4 = v.findViewById(id.per_chrg4);
        bt_dp_lev_frm = v.findViewById(id.bt_dp_lev_frm);
        bt_dp_lev_to = v.findViewById(id.bt_dp_lev_to);


        //////////////////////
        bt_dp_lev_frm.setOnClickListener(new View.OnClickListener() {
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

                            TextView tvDt = getView().findViewById(id.leave_from);
                            tvDt.setText(sdf.format(date1));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Leave From");
                datePickerDialog.show();
            }
        });


        // Date help for leave to
        bt_dp_lev_to.setOnClickListener(new View.OnClickListener() {
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

                            TextView tvDt = getView().findViewById(id.leave_to);
                            tvDt.setText(sdf.format(date1));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Leave To");
                datePickerDialog.show();
            }
        });


        // Save Date (Send Request to SAP)
        TextView save = v.findViewById(id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*Toast.makeText(dashboardActivity.this, "You request for reset password", Toast.LENGTH_LONG).show();*/
                if (CustomUtility.isInternetOn(mContext)) {
                    submitForm();   // Send Request to SAP
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });


        // Spinner for Leave type
        /*String [] values = { "Earned", "Casual", "Medical", "Paternity", "Maternity", "Without Pay",};*/
        Spinner spinner = v.findViewById(id.leave_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), layout.spinner_item, leavebalance);
        adapter.setDropDownViewResource(layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner = v.findViewById(id.leave_type);


        // Spinner selection item for leave type
        final Spinner finalSpinner = spinner;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                st_leave_type = finalSpinner.getSelectedItem().toString();
                // Toast.makeText(DsrEntryActivity.this, String.valueOf(index), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        // Spinner selection for Leave Duration
        String[] leave_duration = {"Full Day or More", "Half Day",};
        Spinner spinner1 = v.findViewById(id.leave_duration);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(),
                layout.spinner_item, leave_duration);
        adapter.setDropDownViewResource(layout.spinner_item);
        spinner1.setAdapter(adapter1);


        // Spinner select item from duration
        final Spinner finalSpinner1 = spinner1;
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                st_leave_duration = finalSpinner1.getSelectedItem().toString();
                // Toast.makeText(DsrEntryActivity.this, String.valueOf(index), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        /////////// code for Auto Complete text //////////

        /*Toast.makeText(getActivity(),"Leave Request get data activ emp", Toast.LENGTH_SHORT).show();*/
        languages = dataHelper.getDiscription();
        Log.d("languages", " " + languages);
        per_chrg1 = v.findViewById(id.per_chrg1);
        ArrayAdapter adapter11 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
        per_chrg1.setAdapter(adapter11);
        per_chrg1.setThreshold(1);


        // For per chrg 2 Help

        per_chrg2 = v.findViewById(id.per_chrg2);
        ArrayAdapter adapter12 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
        per_chrg2.setAdapter(adapter12);
        per_chrg2.setThreshold(1);


        // For per chrg 3 Help

        per_chrg3 = v.findViewById(id.per_chrg3);
        ArrayAdapter adapter13 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
        per_chrg3.setAdapter(adapter13);
        per_chrg3.setThreshold(1);


        // For per chrg 4 Help

        per_chrg4 = v.findViewById(id.per_chrg4);
        ArrayAdapter adapter14 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
        per_chrg4.setAdapter(adapter14);
        per_chrg4.setThreshold(1);
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
/*        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
//        fragmentPager.getItem(0).setText("something"); //UPDATE
        leave_from.setText(SelectDateFragment.date);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        boolean addToBack = false;
        int id = v.getId();
        switch (id) {
            case R.id.save:
                Toast.makeText(getActivity(), "Leave Request to SAP", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void submitForm() {
        /*Toast.makeText(getActivity(), "Leave Request submit", Toast.LENGTH_SHORT).show();*/


        String st_pernr = userModel.uid;
        String st_leave_from = leave_from.getText().toString();
        String st_leave_to = leave_to.getText().toString();
        String st_leave_reason = leave_reason.getText().toString();
        String st_per_chrg1 = per_chrg1.getText().toString();
        String st_per_chrg2 = per_chrg2.getText().toString();
        String st_per_chrg3 = per_chrg3.getText().toString();
        String st_per_chrg4 = per_chrg4.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        param.add(new BasicNameValuePair("app_pernr", st_pernr));
        param.add(new BasicNameValuePair("app_leave_type", st_leave_type));
        param.add(new BasicNameValuePair("app_leave_duration", st_leave_duration));
        param.add(new BasicNameValuePair("app_leave_from", st_leave_from));
        param.add(new BasicNameValuePair("app_leave_to", st_leave_to));
        param.add(new BasicNameValuePair("app_leave_reason", st_leave_reason));
        param.add(new BasicNameValuePair("app_per_chrg1", st_per_chrg1));
        param.add(new BasicNameValuePair("app_per_chrg2", st_per_chrg2));
        param.add(new BasicNameValuePair("app_per_chrg3", st_per_chrg3));
        param.add(new BasicNameValuePair("app_per_chrg4", st_per_chrg4));


        progressDialog = ProgressDialog.show(getActivity(), "", "Connecting to server..please wait !");


        new Thread() {
            public void run() {

                try {

                    String obj = CustomHttpClient.executeHttpPost1(SapUrl.leave_create, param);


                    if (obj != null) {
                        progressDialog.dismiss();
                        Log.d("leave", param.toString());
                        JSONArray ja = new JSONArray(obj);
                        for (int i = 0; i < 1; i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            leave_return_msg = jo.getString("name");
                            Log.d("leave_return_msg", leave_return_msg);

                            /*Toast.makeText(getActivity(), leave_return_msg, Toast.LENGTH_LONG).show();*/

                            Message msg = new Message();
                            msg.obj = leave_return_msg;
                            mHandler.sendMessage(msg);
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), leave_return_msg, Toast.LENGTH_LONG).show();
                        /*Toast.makeText(getActivity().getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();*/
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.d("msg", "" + e);
                }
            }

        }.start();
    }

    public void insertLeaveBalance() {

        progressDialog = ProgressDialog.show(getActivity(), "", "Please wait.. Calculating available leave quotas!");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String obj = calculate_leave_balance();
                Log.d("active_employee_log", "" + obj);
                if (obj != null) {
                    progressDialog.dismiss();
                }
            }

        }).start();

    }

    private String calculate_leave_balance() {


        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        dataHelper = new DatabaseHelper(getActivity());
        dataHelper.deleteleaveBalance();

        mContext = getActivity();
        userModel = new LoggedInUser(mContext);
        /*Log.d("use_per",""+userModel.uid);*/

        try {

            param.add(new BasicNameValuePair("pernr", userModel.uid));

            String obj_leave_balance = CustomHttpClient.executeHttpPost1(SapUrl.leave_balance, param);

            Log.d("obj_leave_balance", "" + obj_leave_balance);

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
            Log.d("msg", "" + e);
        }

        return obj_leave_balance;

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
