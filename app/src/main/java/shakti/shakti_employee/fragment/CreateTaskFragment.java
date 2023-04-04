package shakti.shakti_employee.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
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
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.other.TaskCreated;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTaskFragment extends Fragment {

    DatabaseHelper dataHelper = null;
    EditText et_description;
    AutoCompleteTextView ac_assign_to;
    String task_assign_to;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    ArrayList<String> languages = new ArrayList<String>();
    JSONArray ja_task = null;
    String sync_data_name = null;
    //    private DatePicker dp_date_from,dp_date_to;
    String sync_data_value = null;
    private OnFragmentInteractionListener mListener;
    private DashboardActivity dashboardActivity;
    private ProgressDialog progressDialog;
    private LoggedInUser userModel;
    private Context mContext;
    private TextView tv_create_task;
    private Spinner spinner_mrc_type;
    private Spinner spinner_department;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private EditText fromDateEtxt;
    private EditText toDateEtxt;


    public CreateTaskFragment() {
        // Required empty public constructor
    }

    public static CreateTaskFragment newInstance() {
        CreateTaskFragment fragment = new CreateTaskFragment();
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
        dataHelper = new DatabaseHelper(getActivity());


        mContext = getActivity();
        userModel = new LoggedInUser(mContext);
        //ArrayList<OD> pendingOdList = new ArrayList<OD>();
        //pendingOdList = dataHelper.getPendingOdDirect();

        //dataAdapter = new MyCustomAdapter(this.getActivity(),R.layout.od_list, pendingOdList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Create Task");

        View v = inflater.inflate(R.layout.fragment_create_task, container, false);

        // tv_create_task = (TextView)v.findViewById(R.id.test);


        spinner_mrc_type = (Spinner) v.findViewById(R.id.spinner_mrc_type);
        spinner_department = (Spinner) v.findViewById(R.id.spinner_department);
        et_description = (EditText) v.findViewById(R.id.et_description);
        ac_assign_to = (AutoCompleteTextView) v.findViewById(R.id.ac_assign_to);
        Button btn_save = (Button) v.findViewById(R.id.btn_save);
//        DatePicker dp_date_from = (DatePicker)v.findViewById(R.id.dp_date_from);
//        DatePicker dp_date_to = (DatePicker)v.findViewById(R.id.dp_date_to);

        fromDateEtxt = (EditText) v.findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
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

                            TextView tvDt = (TextView) getView().findViewById(R.id.etxt_fromdate);
                            tvDt.setText(sdf.format(date1));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Task From");
                datePickerDialog.show();
            }
        });


        toDateEtxt = (EditText) v.findViewById(R.id.etxt_todate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);

        toDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date

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

                            TextView tvDt = (TextView) getView().findViewById(R.id.etxt_todate);
                            tvDt.setText(sdf.format(date1));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }


                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Task To");
                datePickerDialog.show();
            }
        });


        languages = dataHelper.getDiscription();
        Log.d("languages", " " + languages);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
        ac_assign_to.setAdapter(adapter);
        ac_assign_to.setThreshold(1);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check ac_assign_to value .....
                // Check current date and time value and use below


                if (CustomUtility.isInternetOn(mContext)) {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    SimpleDateFormat stf = new SimpleDateFormat("HHmmss");
                    String currentDate = sdf.format(new Date());
                    String currentTime = stf.format(new Date());

                    if (!ac_assign_to.getText().toString().trim().isEmpty()) {
                        String CurrentString = ac_assign_to.getText().toString();
                        String[] separated = CurrentString.split("-");
                        //separated[0];
                        task_assign_to = separated[1];
                    }


                    // Toast.makeText(getActivity(),userModel.uid+"---"+currentDate+"---"+currentTime+"---"+et_description.getText().toString().trim()+"---"+task_assign_to.trim()+"---"+fromDateEtxt.getText().toString()+"---"+toDateEtxt.getText().toString(),Toast.LENGTH_SHORT).show();


                    if (et_description.getText().toString().trim().isEmpty() ||
                            ac_assign_to.getText().toString().trim().isEmpty() ||
                            fromDateEtxt.getText().toString().isEmpty() ||
                            toDateEtxt.getText().toString().isEmpty()) {

                        if (et_description.getText().toString().trim().isEmpty()) {
                            Toast.makeText(getActivity(), "Enter Description", Toast.LENGTH_SHORT).show();
                        } else if (ac_assign_to.getText().toString().trim().isEmpty()) {
                            Toast.makeText(getActivity(), "Enter Task Assigned To", Toast.LENGTH_SHORT).show();
                        } else if (fromDateEtxt.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Select Date From", Toast.LENGTH_SHORT).show();
                        } else if (toDateEtxt.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "Select date To", Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        //Toast.makeText(getActivity(),userModel.uid+"---"+currentDate+"---"+currentTime+"---"+et_description.getText().toString().trim()+"---"+task_assign_to.trim()+"---"+fromDateEtxt.getText().toString()+"---"+toDateEtxt.getText().toString(),Toast.LENGTH_SHORT).show();

                        dataHelper.createTask(userModel.uid, currentDate, currentTime, et_description.getText().toString().trim(), task_assign_to.trim(), fromDateEtxt.getText().toString().replace("/", "."), toDateEtxt.getText().toString().replace("/", "."), spinner_mrc_type.getSelectedItem().toString(), spinner_department.getSelectedItem().toString());
                        Toast.makeText(getActivity(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();


                        ArrayList<TaskCreated> task_created = new ArrayList<TaskCreated>();
                        task_created = dataHelper.getTaskCreated();

                        if (task_created.size() > 0) {
                            // Task Created Data
                            Log.d("taskdata", "" + task_created.size());
                            ja_task = new JSONArray();

                            for (int i = 0; i < task_created.size(); i++) {

                                JSONObject jsonObj = new JSONObject();

                                try {

                                    jsonObj.put("pernr", task_created.get(i).getPernr());
                                    jsonObj.put("budat", task_created.get(i).getCurrentDate());
                                    jsonObj.put("time", task_created.get(i).getCurrentTime());
                                    jsonObj.put("description", task_created.get(i).getDescription());
                                    jsonObj.put("assign_to", task_created.get(i).getTask_assign_to());
                                    jsonObj.put("date_from", task_created.get(i).getFromDateEtxt());
                                    jsonObj.put("date_to", task_created.get(i).getToDateEtxt());
                                    jsonObj.put("mrc_type", task_created.get(i).getMrc_type());
                                    jsonObj.put("department", task_created.get(i).getDepartment());

                                    ja_task.put(jsonObj);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
                            param1.add(new BasicNameValuePair("TASK_CREATED", String.valueOf(ja_task)));

                            try {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                                StrictMode.setThreadPolicy(policy);

                                String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_OFFLINE_DATA_TO_SAP, param1);

                                if (obj2 != "") {

                                    JSONObject jo_success = new JSONObject(obj2);
                                    JSONArray ja_success = jo_success.getJSONArray("data_success");

                                    for (int i = 0; i < ja_success.length(); i++) {


                                        JSONObject jo = ja_success.getJSONObject(i);

                                        sync_data_name = jo.getString("sync_data");
                                        sync_data_value = jo.getString("value");


                                        Log.d("success", "" + sync_data_name + "---" + sync_data_value);


                                        if (sync_data_name.equalsIgnoreCase("EMP_TASK") &&
                                                sync_data_value.equalsIgnoreCase("Y")) {
//                                        dataHelper.updateTaskCreated();
                                            dataHelper.deleteTaskCreate();
                                            //db.deleteEmployeeGPSActivity();
                                        }

                                    }

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                        et_description.setText("");
                        ac_assign_to.setText("");
                        fromDateEtxt.setText("");
                        toDateEtxt.setText("");

                        getActivity().finish();

//                    if (isOnline()) {
//                        new SyncDataToSAP_New().SendTaskCreated(getActivity().getApplicationContext());
//                    }
                    }


                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return v;
    }

    public boolean isOnline() {
        try {


            connectivityManager = (ConnectivityManager) getActivity().getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();

            try {
                Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
                int returnVal = p1.waitFor();
                connected = (returnVal == 0);
                return connected;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;


        } catch (Exception e) {
            Log.v("connectivity", e.toString());
        }
        return connected;
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
