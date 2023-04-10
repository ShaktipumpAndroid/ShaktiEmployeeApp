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
    private final int progressBarStatus = 0;
    private final Handler progressBarHandler = new Handler();


    public LeaveApproveFragment() {
        // Required empty public constructor
    }

    public static LeaveApproveFragment newInstance() {
        LeaveApproveFragment fragment = new LeaveApproveFragment();
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

        stateList = new ArrayList<>();
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

        final ListView listView = v.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        pressed_direct_indirect = "D";


        bt_direct = v.findViewById(R.id.bt_direct);
        bt_inline = v.findViewById(R.id.bt_inline);


        bt_direct.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                bt_direct.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded_disabled));
                bt_inline.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded));
            }

            pressed_direct_indirect = "D";
            fetch_direct();
        });


        bt_inline.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                bt_direct.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded));
                bt_inline.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded_disabled));
            }
            pressed_direct_indirect = "I";
            fetch_indirect();
        });


        if (pressed_direct_indirect == "D") {
            ArrayList<States> stateList;
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
            ArrayList<States> stateList;
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

        return v;
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
                        Log.d("leave_app_return_msg", leave_app_return_msg);
                        if (leave_app_return_msg.equalsIgnoreCase("k")) {
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
        ListView listView = getView().findViewById(R.id.listView1);
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
        ListView listView = getView().findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // When clicked, show a toast with the TextView text
            States state = (States) parent.getItemAtPosition(position);


            pressed_direct_indirect = "D";
            show_dailog(pressed_direct_indirect, state.getLeaveNo());

            dataAdapter.remove(dataAdapter.getItem(position));
            dataAdapter.notifyDataSetChanged();
            /*Toast.makeText(getContext(),"Clicked on Row: " + state.getName(),Toast.LENGTH_LONG).show();*/

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

        private final ArrayList<States> stateList;

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
                holder.leave_no = convertView.findViewById(R.id.code);
                holder.name = convertView.findViewById(R.id.name);
                holder.horo = convertView.findViewById(R.id.leave_duration);
                holder.leave_type = convertView.findViewById(R.id.leave_type);
                holder.leave_from = convertView.findViewById(R.id.leave_from);
                holder.leave_to = convertView.findViewById(R.id.leave_to);
                holder.leave_reason = convertView.findViewById(R.id.leave_reason);
                holder.checkBox1 = convertView.findViewById(R.id.checkBox1);

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
