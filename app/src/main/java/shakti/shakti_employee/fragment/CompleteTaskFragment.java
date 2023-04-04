package shakti.shakti_employee.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.other.TaskPending;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompleteTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompleteTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompleteTaskFragment extends Fragment {

    DatabaseHelper dataHelper = null;
    ListView list_view;
    ConnectivityManager connectivityManager;
    boolean connected = false;
    ArrayList<String> languages = new ArrayList<String>();
    JSONArray ja_task_completed = null;
    String sync_data_name = null;
    String sync_data_value = null;
    private OnFragmentInteractionListener mListener;
    private DashboardActivity dashboardActivity;
    private MyCustomAdapter adapter = null;
    private List<TaskPending> taskList = new ArrayList<TaskPending>();
    private LoggedInUser userModel;
    private Context mContext;

    public CompleteTaskFragment() {
        // Required empty public constructor
    }

    public static CompleteTaskFragment newInstance() {
        CompleteTaskFragment fragment = new CompleteTaskFragment();
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


//        getActivity().setTitle("Pending Tasks "+"("+ ""+dataHelper.getPendinTaskCount() +")");

        View v = inflater.inflate(R.layout.fragment_complete_task, container, false);

        list_view = (ListView) v.findViewById(R.id.list_view_pending_task);

        displayListView();

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

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void displayListView() {

        ArrayList<TaskPending> al = dataHelper.getPendingTask();
        Iterator<TaskPending> it = al.iterator();

        al = new ArrayList<>();

        al.clear();
        taskList.clear();

        while (it.hasNext()) {

            TaskPending databaseBean = it.next();
            TaskPending task = new TaskPending();
            task.setPernr(databaseBean.getPernr());
            task.setCurrentDate(databaseBean.getCurrentDate());
            task.setDepartment(databaseBean.getDepartment());
            task.setSrno(databaseBean.getSrno());
            task.setDno(databaseBean.getDno());
            task.setDescription(databaseBean.getDescription());
            task.setFromDateEtxt(databaseBean.getFromDateEtxt());
            task.setToDateEtxt(databaseBean.getToDateEtxt());
            task.setMrc_type(databaseBean.getMrc_type());

            taskList.add(task);
            Log.e("LENGTH", "tasklist" + taskList.size());

            // list_view = (ListView)v.findViewById(R.id.list_view_pending_task);
            if (taskList.size() > 0) {
                adapter = new MyCustomAdapter(getActivity().getApplicationContext(), R.layout.fragment_complete_task_row, (ArrayList<TaskPending>) taskList);
                list_view.setItemsCanFocus(true);
                list_view.setAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "You have no Pending Task", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                getActivity().onBackPressed();
            }

        }

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

    private class MyCustomAdapter extends ArrayAdapter<TaskPending> {

        private ArrayList<TaskPending> tasklist;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<TaskPending> productList) {
            super(context, textViewResourceId, productList);
            this.tasklist = new ArrayList<TaskPending>();
            this.tasklist.addAll(productList);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final TaskPending task = tasklist.get(position);


            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.fragment_complete_task_row, null);

                TextView tv_dno = (TextView) convertView.findViewById(R.id.tv_dno);
                TextView tv_srno = (TextView) convertView.findViewById(R.id.tv_srno);
                TextView tv_mrct = (TextView) convertView.findViewById(R.id.tv_mrct);
                TextView tv_dep_name = (TextView) convertView.findViewById(R.id.tv_dep_name);
                TextView tv_assigner = (TextView) convertView.findViewById(R.id.tv_assigner);
                TextView tv_description = (TextView) convertView.findViewById(R.id.tv_description);
                TextView tv_date_from = (TextView) convertView.findViewById(R.id.tv_date_from);
                TextView tv_date_to = (TextView) convertView.findViewById(R.id.tv_date_to);
                Button btn_complete = (Button) convertView.findViewById(R.id.btn_complete);


            }

            TextView tv_dno = (TextView) convertView.findViewById(R.id.tv_dno);
            TextView tv_srno = (TextView) convertView.findViewById(R.id.tv_srno);
            TextView tv_mrct = (TextView) convertView.findViewById(R.id.tv_mrct);
            TextView tv_dep_name = (TextView) convertView.findViewById(R.id.tv_dep_name);
            TextView tv_assigner = (TextView) convertView.findViewById(R.id.tv_assigner);
            TextView tv_description = (TextView) convertView.findViewById(R.id.tv_description);
            TextView tv_date_from = (TextView) convertView.findViewById(R.id.tv_date_from);
            TextView tv_date_to = (TextView) convertView.findViewById(R.id.tv_date_to);
            final Button btn_complete = (Button) convertView.findViewById(R.id.btn_complete);


            tv_dno.setText(task.getDno());
            tv_srno.setText(task.getSrno());
            tv_mrct.setText(task.getMrc_type());
            tv_dep_name.setText(task.getDepartment());
            tv_assigner.setText(task.getPernr());
            tv_description.setText(task.getDescription());
            tv_date_from.setText(task.getFromDateEtxt());
            tv_date_to.setText(task.getToDateEtxt());


            btn_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    // Set up the input
                    final AutoCompleteTextView ac_checker_id = new AutoCompleteTextView(getActivity());
                    final EditText et_remark = new EditText(getActivity());

                    languages = dataHelper.getDiscription();
                    Log.d("languages", " " + languages);
                    final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, languages);
                    ac_checker_id.setAdapter(adapter);
                    ac_checker_id.setFocusableInTouchMode(true);
                    ac_checker_id.setHint("Enter Person name & Select From List");
                    ac_checker_id.setThreshold(1);

                    et_remark.setHint("Remark(If Any)");

                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


                    new AlertDialog.Builder(getActivity())
                            .setTitle("Complete Task")
                            .setMessage("")
                            .setView(ac_checker_id)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    if (ac_checker_id.getText().toString().trim().isEmpty()) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Please Enter Person Name & Select From List", Toast.LENGTH_SHORT).show();

                                    } else {

                                        //Toast.makeText(getActivity().getApplicationContext(),""+tasklist.get(position).getDno()+"---"+tasklist.get(position).getSrno(),Toast.LENGTH_SHORT).show();
                                        if (CustomUtility.isInternetOn(mContext)) {

                                            dataHelper.updatePendingTaskToComplete(ac_checker_id.getText().toString().trim(), tasklist.get(position).getDno(), tasklist.get(position).getSrno(), et_remark.getText().toString().trim());
//
                                            String docno1 = taskList.get(position).getDno();
                                            ArrayList<TaskPending> task_completed = new ArrayList<TaskPending>();
                                            task_completed = dataHelper.getCompletedTask();
                                            Log.d("taskdatacomplete", "" + task_completed.size());
                                            ja_task_completed = new JSONArray();

                                            for (int i = 0; i < task_completed.size(); i++) {

                                                JSONObject jsonObj = new JSONObject();

                                                try {

                                                    jsonObj.put("dno", task_completed.get(i).getDno());
                                                    jsonObj.put("srno", task_completed.get(i).getSrno());
                                                    jsonObj.put("checker", task_completed.get(i).getChecker());
                                                    jsonObj.put("remark", task_completed.get(i).getRemark());


                                                    ja_task_completed.put(jsonObj);


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
                                            param1.add(new BasicNameValuePair("TASK_COMPLETED", String.valueOf(ja_task_completed)));

                                            try {
                                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                                                StrictMode.setThreadPolicy(policy);

//                Log.d("output_obj11", "hello test 2");
                                                String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_OFFLINE_DATA_TO_SAP, param1);


                                                if (obj2 != "") {

                                                    JSONObject jo_success = new JSONObject(obj2);
                                                    JSONArray ja_success = jo_success.getJSONArray("data_success");

                                                    for (int i = 0; i < ja_success.length(); i++) {


                                                        JSONObject jo = ja_success.getJSONObject(i);

                                                        sync_data_name = jo.getString("sync_data");
                                                        sync_data_value = jo.getString("value");


                                                        Log.d("success", "" + sync_data_name + "---" + sync_data_value);


                                                        if (sync_data_name.equalsIgnoreCase("EMP_TASK_COMPLETE") &&
                                                                sync_data_value.equalsIgnoreCase("Y")) {
//                                                            dataHelper.updateTaskCompleted();
                                                            dataHelper.deleteTaskCompletedEntry(docno1);
                                                            //db.deleteEmployeeGPSActivity();
                                                        }

                                                    }

                                                }


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            getActivity().finish();
                                            getActivity().onBackPressed();
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            })
                            .show();


                }
            });


            return convertView;


        }

    }


}
