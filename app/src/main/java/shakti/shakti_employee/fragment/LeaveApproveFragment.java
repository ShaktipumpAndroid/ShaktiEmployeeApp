package shakti.shakti_employee.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.activity.LeaveApproveActivity;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.other.States;

/**
 * A simple {@link Fragment} subclass.
 */


public class LeaveApproveFragment extends Fragment {

    DatabaseHelper dataHelper = null;
    TextView bt_direct, bt_inline;
    String obj_pending_leave;
    String KEY_LEV_NO;
    String LEV_TYP;
    String ENAME;
    String HORO;
    String LEV_FRM;
    String LEV_TO;
    String REASON;
    String CHRG_NAME1;
    String CHRG_NAME2;
    String CHRG_NAME3;
    String CHRG_NAME4;
    String DIRECT_INDIRECT;
    String pressed_direct_indirect;
    String leave_app_return_msg = null;
    String userid, dsr_comment, dsr_activity_type, help_name, save_data;
    EditText editText_comment;
    ProgressDialog progressBar;
    ArrayList<States> stateList;
    Timer timer;
    Context context;
    MyCustomAdapter dataAdapter = null;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(getActivity(), mString, Toast.LENGTH_LONG).show();
        }
    };
    private DashboardActivity dashboardActivity;
    private ProgressDialog progressDialog;
    private LoggedInUser userModel;
    private Context mContext;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();


    public LeaveApproveFragment() {
        // Required empty public constructor
    }

    public static LeaveApproveFragment newInstance() {
        LeaveApproveFragment fragment = new LeaveApproveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    //

    public void setDashBoard(DashboardActivity dashBoard) {
        this.dashboardActivity = dashBoard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataHelper = new DatabaseHelper(getActivity());

        mContext = getActivity();
        userModel = new LoggedInUser(mContext);
        dataHelper = new DatabaseHelper(getActivity());


//        if (checkInternetConenction()) {
//
//            dataHelper.deletePendingLeave();
//
//            progressBar = new ProgressDialog(getActivity());
//            progressBar.setCancelable(true);
//            // progressBar.setCancelable(true);
//            progressBar.setMessage("Downloading Data...");
//            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressBar.setProgress(0);
//            progressBar.setMax(100);
//            progressBar.show();
//            //reset progress bar and filesize status
//            progressBarStatus = 0;
//
//            new Thread(new Runnable() {
//                public void run() {
//
//                    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
//                    param.add(new BasicNameValuePair("app_pernr", userModel.uid));
//
//                    try {
//
//                        String obj = CustomHttpClient.executeHttpPost1(SapUrl.pending_leave, param);
//
//                        Log.d("att_emp_obj", obj);
//
//                        if (obj != null) {
//
//
//                            param.add(new BasicNameValuePair("app_pernr", userModel.uid));
//
//                            obj_pending_leave = CustomHttpClient.executeHttpPost1(SapUrl.pending_leave, param);
//
//                            Log.d("pending_leave", "" + obj_pending_leave);
//
//                            JSONArray ja_mat = new JSONArray(obj_pending_leave);
//
//
//                            for (int i = 0; i < ja_mat.length(); i++) {
//
//                                JSONObject jo_matnr = ja_mat.getJSONObject(i);
//
//
//                                KEY_LEV_NO = jo_matnr.getString("leavNo");
//                                HORO = jo_matnr.getString("horo");
//                                ENAME = jo_matnr.getString("name");
//                                LEV_TYP = jo_matnr.getString("dedQuta1");
//                                LEV_FRM = jo_matnr.getString("levFr");
//                                LEV_TO = jo_matnr.getString("levT");
//                                REASON = jo_matnr.getString("reason");
//                                CHRG_NAME1 = jo_matnr.getString("nameperl");
//                                CHRG_NAME2 = jo_matnr.getString("nameperl2");
//                                CHRG_NAME3 = jo_matnr.getString("nameperl3");
//                                CHRG_NAME4 = jo_matnr.getString("nameperl4");
//                                DIRECT_INDIRECT = jo_matnr.getString("directIndirect");
//
//                                dataHelper.createPendingLeave(KEY_LEV_NO, HORO, ENAME, LEV_TYP, LEV_FRM, LEV_TO,
//                                        REASON, CHRG_NAME1, CHRG_NAME2, CHRG_NAME3, CHRG_NAME4, DIRECT_INDIRECT);
//
//                            }
//
//
//                        }
//
//                        progressBarStatus = 100;
//
//                        // Updating the progress bar
//                        progressBarHandler.post(new Runnable() {
//                            public void run() {
//
//                                progressBar.setProgress(progressBarStatus);
//                            }
//                        });
//
//
//                        progressBar.cancel();
//                        progressBar.dismiss();
//
//
//                        Thread.sleep(5000);
//                    } catch (Exception e) {
//
//                    }
//
//
//                }
//            }).start();
//
//        } else {
//            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//        }


        stateList = new ArrayList<States>();
        stateList = dataHelper.getPendingLeaveDirect();

        if (stateList.size() > 0) {
            dataAdapter = new MyCustomAdapter(getActivity(), R.layout.state_info, stateList);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No Records Found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_leave_approve, container, false);

        final ListView listView = (ListView) v.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

//        insert_pending_leave();      // Commented

        pressed_direct_indirect = "D";
//        ArrayList<States> stateList = new ArrayList<States>();
//        stateList = dataHelper.getPendingLeaveDirect();

        bt_direct = (TextView) v.findViewById(R.id.bt_direct);
        bt_inline = (TextView) v.findViewById(R.id.bt_inline);


        bt_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bt_direct.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded_disabled));
                    bt_inline.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded));
                }

                pressed_direct_indirect = "D";
                fetch_direct();
            }
        });


        bt_inline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bt_direct.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded));
                    bt_inline.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded_disabled));
                }
                pressed_direct_indirect = "I";
                fetch_indirect();
            }
        });


        if (pressed_direct_indirect == "D") {
            ArrayList<States> stateList = new ArrayList<States>();
            stateList = dataHelper.getPendingLeaveDirect();


            //create an ArrayAdaptar from the String Array
            dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.state_info, stateList);

//            final ListView listView = (ListView) v.findViewById(R.id.listView1);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    States state = (States) parent.getItemAtPosition(position);



                    show_dailog(pressed_direct_indirect,state.getLeaveNo());


                    /*Toast.makeText(getContext(),"Clicked on Row: " + state.getName(),Toast.LENGTH_LONG).show();*/

                }
            });


        }
        if (pressed_direct_indirect == "I") {
            ArrayList<States> stateList = new ArrayList<States>();
            stateList = dataHelper.getPendingLeaveInDirect();

            //create an ArrayAdaptar from the String Array
            dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.state_info, stateList);
//            final ListView listView = (ListView) v.findViewById(R.id.listView1);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    States state = (States) parent.getItemAtPosition(position);

                    show_dailog(pressed_direct_indirect,state.getLeaveNo());

                    /*Toast.makeText(getContext(),"Clicked on Row: " + state.getName(),Toast.LENGTH_LONG).show();*/

                }
            });

        }



        /*leavebalance = dataHelper.getLeaveBalance();*/


        //Array list of countries


/*        States _states = new States("651","1","Rohit Patidar","Earned","01.01.2017","01.01.2017","New Year",false);
        stateList.add(_states);
        _states = new States("651","1","Rohit Patidar","Earned","01.01.2017","01.01.2017","New Year",false);
        stateList.add(_states);
        _states = new States("651","1","Rohit Patidar","Earned","01.01.2017","01.01.2017","New Year",false);
        stateList.add(_states);
        _states = new States("651","1","Rohit Patidar","Earned","01.01.2017","01.01.2017","New Year",false);
        stateList.add(_states);
            */


//        //create an ArrayAdaptar from the String Array
//        dataAdapter = new MyCustomAdapter(this.getActivity(),R.layout.state_info, stateList);
//        ListView listView = (ListView) v.findViewById(R.id.listView1);
//        // Assign adapter to ListView
//        listView.setAdapter(dataAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                // When clicked, show a toast with the TextView text
//                States state = (States) parent.getItemAtPosition(position);
//
//                show_dailog(state.getLeaveNo());
//
//                /*Toast.makeText(getContext(),"Clicked on Row: " + state.getName(),Toast.LENGTH_LONG).show();*/
//
//            }
//        });


        /////////////

        /*
        TextView myButton = (TextView) v.findViewById(R.id.leave_approve);

        myButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<States> stateList = dataAdapter.stateList;

                for(int i=0;i<stateList.size();i++)
                {
                    States state = stateList.get(i);

                    if(state.isSelected())
                    {

                        *//*responseText.append("\n" + state.getName());*//*

                        // Approve Selected Leaves
                           send_leave_approval_sap(state.getLeaveNo());


                    }
                }

                Toast.makeText(getContext(), responseText, Toast.LENGTH_LONG).show();
            }
        });*/


        return v;
    }

    private void displayListView() {

/*        //Array list of countries
        ArrayList<States> stateList = new ArrayList<States>();

        States _states = new States("AP","Andhra Pradesh",false);
        stateList.add(_states);
        _states = new States("DL","Delhi",true);
        stateList.add(_states);
        _states = new States("GA","Goa",false);
        stateList.add(_states);
        _states = new States("JK","Jammu & Kashmir",true);
        stateList.add(_states);
        _states = new States("KA","Karnataka",true);
        stateList.add(_states);
        _states = new States("KL","Kerala",false);
        stateList.add(_states);
        _states = new States("RJ","Rajasthan",false);
        stateList.add(_states);
        _states = new States("WB","West Bengal",false);
        stateList.add(_states);

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(getActivity().R.layout.state_info, stateList);
        ListView listView = (ListView) v.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // When clicked, show a toast with the TextView text
                States state = (States) parent.getItemAtPosition(position);
                Toast.makeText(getContext(),"Clicked on Row: " + state.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });*/
    }

    public void insert_pending_leave() {

        progressDialog = ProgressDialog.show(getActivity(), "", "Please wait.. searching pending leave(s)!  ");

        Toast.makeText(getActivity(), "Pending leave list", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                String obj = calculate_pending_leave();
                Log.d("res_obj", "" + obj);
                if (obj != null) {
                    progressDialog.dismiss();
                }
            }

        }).start();

    }


    private String calculate_pending_leave() {

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        dataHelper = new DatabaseHelper(getActivity());

        // Delete Pending leave from DB
        dataHelper.deletependingleave();


        /*Log.d("use_per",""+userModel.uid);*/

        try {


            param.add(new BasicNameValuePair("app_pernr", userModel.uid));

            obj_pending_leave = CustomHttpClient.executeHttpPost1(SapUrl.pending_leave, param);

            Log.d("pending_leave", "" + obj_pending_leave);

            JSONArray ja_mat = new JSONArray(obj_pending_leave);

            /*Log.d("json55", "" + ja_mat);*/


            for (int i = 0; i < ja_mat.length(); i++) {

                JSONObject jo_matnr = ja_mat.getJSONObject(i);


                KEY_LEV_NO = jo_matnr.getString("leavNo");
                HORO = jo_matnr.getString("horo");
                ENAME = jo_matnr.getString("name");
                LEV_TYP = jo_matnr.getString("dedQuta1");
                LEV_FRM = jo_matnr.getString("levFr");
                LEV_TO = jo_matnr.getString("levT");
                REASON = jo_matnr.getString("reason");
                CHRG_NAME1 = jo_matnr.getString("nameperl");
                CHRG_NAME2 = jo_matnr.getString("nameperl2");
                CHRG_NAME3 = jo_matnr.getString("nameperl3");
                CHRG_NAME4 = jo_matnr.getString("nameperl4");
                DIRECT_INDIRECT = jo_matnr.getString("directIndirect");

                dataHelper.createPendingLeave(KEY_LEV_NO, HORO, ENAME, LEV_TYP, LEV_FRM, LEV_TO,
                        REASON, CHRG_NAME1, CHRG_NAME2, CHRG_NAME3, CHRG_NAME4, DIRECT_INDIRECT);

            }
            return obj_pending_leave;
        } catch (Exception e) {
            /* progressBarStatus = 40 ;*/
            Log.d("msg", "" + e);
        }

        return obj_pending_leave;

    }


    public void send_leave_approval_sap(final String leave_request_no) {

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        final Integer rem_req_no;
        param.add(new BasicNameValuePair("leave_no", leave_request_no));
        param.add(new BasicNameValuePair("approver", userModel.uid));
        param.add(new BasicNameValuePair("password", userModel.password));

//        rem_req_no = leave_request_no;

        rem_req_no = Integer.parseInt(leave_request_no);

        progressDialog = ProgressDialog.show(getActivity(), "", "Please wait.. Approving leave!  ");

        new Thread(new Runnable() {
            @Override
            public void run() {

                String obj = null;

                try {

                    obj = CustomHttpClient.executeHttpPost1(SapUrl.approve_leave, param);

                    Log.d("leave_approve", param.toString());

                    JSONArray ja = new JSONArray(obj);
                    for (int i = 0; i < 1; i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        leave_app_return_msg = jo.getString("msg");
                        Log.d("leave_app_return_msg", leave_app_return_msg.toString());
                        if (leave_app_return_msg.toString().equalsIgnoreCase("k")) {
                            leave_app_return_msg = "Leave Application Approved Successfully";
                        }

                        /*Toast.makeText(getActivity(), leave_return_msg, Toast.LENGTH_LONG).show();*/
                        Message msg = new Message();
                        msg.obj = leave_app_return_msg;
                        mHandler.sendMessage(msg);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.cancel();
                    progressDialog.dismiss();
                }


                if (obj != null) {
                    DatabaseHelper db;
                    db = new DatabaseHelper(getActivity());
                    Log.d("Reqno", leave_request_no);
                    progressDialog.dismiss();
                    if (leave_app_return_msg.equalsIgnoreCase("Leave Application Approved Successfully")) {
                        db.deletePendingLeaveWhere(leave_request_no);
                        Intent intent = new Intent(getActivity(), LeaveApproveActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                }
            }

        }).start();

    }

    public void show_dailog(final String direct_indirect,final String leave_no) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Leave Approve alert !");
        alertDialog.setMessage("Do you want Approve Leave ?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

//                new TimeServiceDownloadData().stopSelf();
//                new TimeServiceDownloadData().onDestroy();

                // Approve Selected Leaves
                send_leave_approval_sap(leave_no);

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (direct_indirect.equalsIgnoreCase("I"))
                {
                    fetch_indirect();
                }
                else{
                    fetch_direct();
                }
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    public void fetch_indirect() {
        ArrayList<States> stateList = new ArrayList<States>();
        stateList = dataHelper.getPendingLeaveInDirect();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.state_info, stateList);
        ListView listView = (ListView) getView().findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                States state = (States) parent.getItemAtPosition(position);

                pressed_direct_indirect = "I";

                show_dailog(pressed_direct_indirect,state.getLeaveNo());

                /*Toast.makeText(getContext(),"Clicked on Row: " + state.getName(),Toast.LENGTH_LONG).show();*/
                dataAdapter.remove(dataAdapter.getItem(position));
                dataAdapter.notifyDataSetChanged();

            }
        });

    }

    public void fetch_direct() {
        ArrayList<States> stateList = new ArrayList<States>();
        stateList = dataHelper.getPendingLeaveDirect();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.state_info, stateList);
        ListView listView = (ListView) getView().findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                States state = (States) parent.getItemAtPosition(position);


                pressed_direct_indirect = "D";
                show_dailog(pressed_direct_indirect,state.getLeaveNo());

                dataAdapter.remove(dataAdapter.getItem(position));
                dataAdapter.notifyDataSetChanged();
                /*Toast.makeText(getContext(),"Clicked on Row: " + state.getName(),Toast.LENGTH_LONG).show();*/

            }
        });

    }

    @Override
    public void onResume() {

        ArrayList<States> stateList = new ArrayList<States>();
        stateList = dataHelper.getPendingLeaveDirect();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.state_info, stateList);

        dataAdapter.notifyDataSetChanged();

        super.onResume();
    }


    private class MyCustomAdapter extends ArrayAdapter<States> {

        private ArrayList<States> stateList;

        public MyCustomAdapter(Context context, int textViewResourceId,

                               ArrayList<States> stateList) {
            super(context, textViewResourceId, stateList);
            this.stateList = new ArrayList<States>();
            this.stateList.addAll(stateList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.state_info, null);


                holder = new ViewHolder();
                holder.leave_no = (TextView) convertView.findViewById(R.id.code);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.horo = (TextView) convertView.findViewById(R.id.leave_duration);
                holder.leave_type = (TextView) convertView.findViewById(R.id.leave_type);
                holder.leave_from = (TextView) convertView.findViewById(R.id.leave_from);
                holder.leave_to = (TextView) convertView.findViewById(R.id.leave_to);
                holder.leave_reason = (TextView) convertView.findViewById(R.id.leave_reason);
                holder.checkBox1 = (CheckBox) convertView.findViewById(R.id.checkBox1);

                convertView.setTag(holder);

                holder.checkBox1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        States _state = (States) cb.getTag();

                        Toast.makeText(getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();

                        _state.setSelected(cb.isChecked());
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            States state = stateList.get(position);

            /*holder.code.setText(" (" + state.getCode() + ")");*/
            holder.leave_no.setText(state.getLeaveNo());
            holder.name.setText(state.getName());
            holder.horo.setText(state.getHoro());
            holder.leave_type.setText(state.getLeave_type());
            holder.leave_from.setText(state.getLeave_from());
            holder.leave_to.setText(state.getLeave_to());
            holder.leave_reason.setText(state.getLeave_reason());

            holder.checkBox1.setTag(state);

            return convertView;
        }

        private void getSystemService(String layoutInflaterService) {
            throw new RuntimeException("Stub!");
        }

        private class ViewHolder {

            TextView leave_no;
            TextView name;
            TextView horo;
            TextView leave_type;
            TextView leave_from;
            TextView leave_to;
            TextView leave_reason;
            CheckBox checkBox1;
        }

    }


}
