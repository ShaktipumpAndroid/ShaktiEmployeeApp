package shakti.shakti_employee.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.TimeZone;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.SapUrl;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ODRequestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ODRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ODRequestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String od_request_msg = null;
    ImageButton bt_od_frm, bt_od_to;
    AutoCompleteTextView od_workplace;
    EditText od_from, od_to, od_visitplace, od_purpose1, od_purpose2, od_purpose3, od_remark;
    AutoCompleteTextView od_charge;
    int index;
    String od_status;
    ArrayList<String> languages = new ArrayList<String>();
    DatabaseHelper dataHelper = null;
    String[] workplace = {
            "State Bank of India-01",
            "State Bank of Indore-02",
            "Other Bank-03",
            "Income Tax Office-04",
            "Central Excise Office-05",
            "Sales Tax Office-06",
            "High Court-07",
            "District Court-08",
            "Labour Court-09",
            "ICD-10",
            "Indore Godown-11",
            "Nagar Nigam-12",
            "Pollution Control Board-13",
            "MPAKVN Indore-14",
            "SPIL Industries-15",
            "SEZ Plant-16",
            "SEZ Office-17",
            "MPFC-18",
            "Exhibition-19",
            "Seminar-20",
            "Others-21",
            "Sales Office-22",
            "Business Tour-23",
            "Traning-24",
            "Vendor visit-25",
            "Vendor Mtl. inspection & Dispatch-26"
    };
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(getActivity(), mString, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    };
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    // User defined types
    private LoggedInUser userModel;
    private Context mContext;
    private ProgressDialog progressDialog;
    private DashboardActivity dashboardActivity;
    // Listener
    private DatePickerDialog.OnDateSetListener datePicker_OdFrom = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            TextView tvDt = (TextView) getView().findViewById(R.id.od_from);
            tvDt.setText(day1 + "/" + month1 + "/" + year1);

        }
    };
    // Listener
    private DatePickerDialog.OnDateSetListener datePicker_OdTo = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            TextView tvDt = (TextView) getView().findViewById(R.id.od_to);
            tvDt.setText(day1 + "/" + month1 + "/" + year1);

        }
    };

    public ODRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ODRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ODRequestFragment newInstance() {
        ODRequestFragment fragment = new ODRequestFragment();
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
        mContext = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_odrequest, container, false);

        dataHelper = new DatabaseHelper(getActivity());

        od_from = (EditText) v.findViewById(R.id.od_from);
        od_to = (EditText) v.findViewById(R.id.od_to);
        od_visitplace = (EditText) v.findViewById(R.id.od_visitplace);
        od_purpose1 = (EditText) v.findViewById(R.id.od_purpose1);
        od_purpose2 = (EditText) v.findViewById(R.id.od_purpose2);
        od_purpose3 = (EditText) v.findViewById(R.id.od_purpose3);
        od_remark = (EditText) v.findViewById(R.id.od_remark);
        od_charge = (AutoCompleteTextView) v.findViewById(R.id.od_charge);

        od_workplace = (AutoCompleteTextView) v.findViewById(R.id.od_workplace);

        bt_od_frm = (ImageButton) v.findViewById(R.id.bt_od_frm);
        bt_od_to = (ImageButton) v.findViewById(R.id.bt_od_to);


        languages = dataHelper.getDiscription();
        Log.d("languages", " " + languages);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
        od_charge.setThreshold(1);
        od_charge.setAdapter(adapter);



        // Date help for od from

//        bt_od_frm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /*leave_from.setEnabled(true);*/
//                /*leave_from.setText("");*/
//                DialogFragment newFragment = new SelectDateFragment();
//                newFragment.show(getFragmentManager(), "DatePicker");
//                od_from.setText(SelectDateFragment.date);
//                /*leave_from.setEnabled(false);*/
//            }
//
//        });
//
//
//        // Date help for od to
//
//        bt_od_to.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /*leave_to.setEnabled(true);*/
//                /*leave_to.setText("");*/
//                DialogFragment newFragment = new SelectDateFragment();
//                newFragment.show(getFragmentManager(), "DatePicker");
//                od_to.setText(SelectDateFragment.date);
//                /*leave_to.setEnabled(false);*/
//            }
//
//        });


        //////////////////////
        bt_od_frm.setOnClickListener(new View.OnClickListener() {
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

                            TextView tvDt = (TextView) getView().findViewById(R.id.od_from);
                            tvDt.setText(sdf.format(date1));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("OD From");
                datePickerDialog.show();
            }
        });

        bt_od_to.setOnClickListener(new View.OnClickListener() {
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

                            TextView tvDt = (TextView) getView().findViewById(R.id.od_to);
                            tvDt.setText(sdf.format(date1));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("OD To");
                datePickerDialog.show();
            }
        });

        // Spinner selection for OD Status

        final String[] st_od_status = {"On Duty", "On Tour",};
        Spinner spinner1 = (Spinner) v.findViewById(R.id.od_status);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(),
                R.layout.spinner_item, st_od_status);
        adapter1.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(adapter1);

        // Spinner selection item for leave type
        final Spinner finalSpinner = spinner1;
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                od_status = finalSpinner.getSelectedItem().toString();
                // Toast.makeText(DsrEntryActivity.this, String.valueOf(index), Toast.LENGTH_LONG).show();
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


                if (!od_charge.getText().toString().trim().isEmpty()) {
                    /*Toast.makeText(dashboardActivity.this, "You request for reset password", Toast.LENGTH_LONG).show();*/
                    submitForm();   // Send Request to SAP

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter Charge Given And Select From List", Toast.LENGTH_SHORT).show();
                }

            }
        });


        /////////// code for Auto Complete text //////////

        /*Toast.makeText(getActivity(),"Leave Request get data activ emp", Toast.LENGTH_SHORT).show();*/
        od_workplace = (AutoCompleteTextView) v.findViewById(R.id.od_workplace);
        ArrayAdapter adapter11 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, workplace);
        od_workplace.setAdapter(adapter11);
        od_workplace.setThreshold(1);


        return v;

    }

    private void submitForm() {
        Toast.makeText(getActivity(), "OD Request submit", Toast.LENGTH_SHORT).show();

        mContext = getActivity();
        userModel = new LoggedInUser(mContext);

        String st_pernr = userModel.uid;
        String st_od_from = od_from.getText().toString();
        String st_od_to = od_to.getText().toString();
        String st_od_visitplace = od_visitplace.getText().toString();
        String st_od_workplace = od_workplace.getText().toString();
        String st_od_purpose1 = od_purpose1.getText().toString();
        String st_od_purpose2 = od_purpose2.getText().toString();
        String st_od_purpose3 = od_purpose3.getText().toString();
        String st_od_remark = od_remark.getText().toString();
        String st_od_charge = od_charge.getText().toString();

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        param.add(new BasicNameValuePair("app_pernr", st_pernr));
        param.add(new BasicNameValuePair("atnds_status", od_status));
        param.add(new BasicNameValuePair("app_od_from", st_od_from));
        param.add(new BasicNameValuePair("app_od_to", st_od_to));
        param.add(new BasicNameValuePair("app_od_visitplace", st_od_visitplace));
        param.add(new BasicNameValuePair("app_od_workplace", st_od_workplace));
        param.add(new BasicNameValuePair("app_od_purpose1", st_od_purpose1));
        param.add(new BasicNameValuePair("app_od_purpose2", st_od_purpose2));
        param.add(new BasicNameValuePair("app_od_purpose3", st_od_purpose3));
        param.add(new BasicNameValuePair("app_od_remark", st_od_remark));
        param.add(new BasicNameValuePair("app_od_charge", st_od_charge));


        progressDialog = ProgressDialog.show(getActivity(), "", "Connecting to server..please wait !");


        new Thread() {
            public void run() {

                try {

                    String obj = CustomHttpClient.executeHttpPost1(SapUrl.od_request, param);


                    if (obj != null) {
                        progressDialog.dismiss();
                        Log.d("od_request", param.toString());
                        JSONArray ja = new JSONArray(obj);
                        for (int i = 0; i < 1; i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            od_request_msg = jo.getString("name");
                            Log.d("leave_return_msg", od_request_msg.toString());
                            /*Toast.makeText(getActivity(), leave_return_msg, Toast.LENGTH_LONG).show();*/
                            Message msg = new Message();
                            msg.obj = od_request_msg;
                            mHandler.sendMessage(msg);

                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), od_request_msg, Toast.LENGTH_LONG).show();
                        /*Toast.makeText(getActivity().getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();*/
                    }

                } catch (Exception e) {
                    progressDialog.dismiss();
                    Log.d("msg", "" + e);
                }
            }

        }.start();
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
