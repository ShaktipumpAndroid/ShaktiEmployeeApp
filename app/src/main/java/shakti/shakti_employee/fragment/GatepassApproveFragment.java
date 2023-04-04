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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.Gatepass;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.other.States;

/**
 * A simple {@link Fragment} subclass.
 */


public class GatepassApproveFragment extends Fragment {

    DatabaseHelper dataHelper = null;
    TextView bt_direct, bt_inline, approv;
    ListView listView;
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
    ArrayList<Gatepass> stateList;
    Timer timer;
    MyCustomAdapter dataAdapter = null;
    String obj_pending_gp;
    Handler handler;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(getActivity(), mString, Toast.LENGTH_LONG).show();
        }
    };
    private DashboardActivity dashboardActivity;
    private ProgressDialog progressDialog;
    private LoggedInUser userModel;
    Handler mHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pressed_direct_indirect = "D";
            fetch_direct();
        }
    };
    private Context mContext;
    Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_SHORT).show();

        }
    };
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    public GatepassApproveFragment() {
        // Required empty public constructor
    }

    public static GatepassApproveFragment newInstance() {
        GatepassApproveFragment fragment = new GatepassApproveFragment();
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
        dataHelper = new DatabaseHelper(getActivity());


        if (CustomUtility.isInternetOn(mContext)) {
            dataHelper.deletePendingGatepass();
            progressBar = new ProgressDialog(getActivity());
            progressBar.setCancelable(true);
            progressBar.setMessage("Downloading Data...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            //reset progress bar and filesize status
            progressBarStatus = 0;


            new Thread(new Runnable() {
                public void run() {

                    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("app_pernr", userModel.uid));

                    try {

                        obj_pending_gp = CustomHttpClient.executeHttpPost1(SapUrl.pending_Gatepass, param);

                        Log.d("pending_gp", obj_pending_gp);

                        if (obj_pending_gp != null) {

                            JSONArray ja_mat = new JSONArray(obj_pending_gp);
                            Log.d("length", "&&&&" + ja_mat.length());
                            if (ja_mat.length() > 0) {
                                for (int i = 0; i < ja_mat.length(); i++) {

                                    JSONObject jo_gp = ja_mat.getJSONObject(i);

                                    Log.d("gatepass", "&&&" + jo_gp.length());

                                    dataHelper.createPendingGatepass(jo_gp.getString("gpno"), jo_gp.getString("pernr"),
                                            jo_gp.getString("ename"), jo_gp.getString("gpdat1"),
                                            jo_gp.getString("gptime1"), jo_gp.getString("gptypeTxt"),
                                            jo_gp.getString("reqtypeTxt"), jo_gp.getString("directindirect"));

                                }
                            }
                        }

                        progressBarStatus = 100;
                        progressBar.cancel();
                        progressBar.dismiss();

                        // Updating the progress bar
                        progressBarHandler.post(new Runnable() {
                            public void run() {

                                progressBar.setProgress(progressBarStatus);
                                Message msg = new Message();
                                msg.obj = "";
                                mHandler1.sendMessage(msg);


                            }
                        });

                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();

                        progressBar.cancel();
                        progressBar.dismiss();
                        Message msg = new Message();
                        msg.obj = "No Gatepass Data Available";
                        mHandler2.sendMessage(msg);
                    }


                }
            }).start();


        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        // stateList = new ArrayList<Gatepass>();
        //stateList=dataHelper.getPendingGatepassDirect();

//        if (stateList.size()>0) {
//            dataAdapter = new MyCustomAdapter(getActivity(), R.layout.state_info, stateList);
//        }else {
//            Toast.makeText(getActivity().getApplicationContext(),"No Records Found",Toast.LENGTH_SHORT).show();
//        }


    }
//
//    public void insert_pending_leave() {
//
//        progressDialog = ProgressDialog.show(getActivity(), "", "Please wait.. searching pending leave(s)!  ");
//
//        Toast.makeText(getActivity(), "Pending leave list", Toast.LENGTH_SHORT).show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                String obj = calculate_pending_leave();
//                Log.d("res_obj", "" + obj);
//                if (obj != null) {
//                   progressDialog.dismiss();
//                }
//            }
//
//        }).start();
//
//    }


//
//    private String calculate_pending_leave() {
//
//        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
//
//        DatabaseHelper dataHelper = new DatabaseHelper(getActivity());
//
//        // Delete Pending leave from DB
//        dataHelper.deletependingleave();
//
//        try {
//
//
//            param.add(new BasicNameValuePair("app_pernr",userModel.uid));
//
//            obj_pending_gp = CustomHttpClient.executeHttpPost1(SapUrl.pending_Gatepass, param);
//
//            Log.d("pending_leave",""+obj_pending_gp);
//
//            JSONArray ja_mat = new JSONArray(obj_pending_gp);
//
//            /*Log.d("json55", "" + ja_mat);*/
//
//
//            for (int i = 0; i < ja_mat.length(); i++) {
//
//                JSONObject jo_matnr = ja_mat.getJSONObject(i);
//
//
//                KEY_LEV_NO = jo_matnr.getString("leavNo");
//                HORO = jo_matnr.getString("horo");
//                ENAME = jo_matnr.getString("name");
//                LEV_TYP = jo_matnr.getString("dedQuta1");
//                LEV_FRM = jo_matnr.getString("levFr");
//                LEV_TO = jo_matnr.getString("levT");
//                REASON = jo_matnr.getString("reason");
//                CHRG_NAME1= jo_matnr.getString("nameperl");
//                CHRG_NAME2= jo_matnr.getString("nameperl2");
//                CHRG_NAME3= jo_matnr.getString("nameperl3");
//                CHRG_NAME4= jo_matnr.getString("nameperl4");
//                DIRECT_INDIRECT= jo_matnr.getString("directIndirect");
//
//                dataHelper.createPendingLeave(  KEY_LEV_NO ,HORO, ENAME, LEV_TYP,LEV_FRM, LEV_TO,
//                        REASON, CHRG_NAME1, CHRG_NAME2 ,CHRG_NAME3,CHRG_NAME4,DIRECT_INDIRECT);
//
//            }
//            return obj_pending_gp ;
//        }
//
//        catch (Exception e)
//        {
//           /* progressBarStatus = 40 ;*/
//            Log.d("msg", "" + e);
//        }
//
//        return obj_pending_gp ;
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gatepass_approve, container, false);

        approv = (TextView) v.findViewById(R.id.approv);
        listView = (ListView) v.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


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
            ArrayList<Gatepass> stateList = new ArrayList<Gatepass>();
            stateList = dataHelper.getPendingGatepassDirect();


            //create an ArrayAdaptar from the String Array
            dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.state_info, stateList);

            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    States state = (States) parent.getItemAtPosition(position);

                    show_dailog(state.getLeaveNo());


                }
            });


        }
        if (pressed_direct_indirect == "I") {
            ArrayList<Gatepass> stateList = new ArrayList<Gatepass>();
            stateList = dataHelper.getPendingGatepassInDirect();

            //create an ArrayAdaptar from the String Array
            dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.state_info, stateList);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    States state = (States) parent.getItemAtPosition(position);
                    show_dailog(state.getLeaveNo());

                }
            });

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bt_direct.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded_disabled));
            bt_inline.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded));
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

    public void show_dailog(final String leave_no) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Leave Approve alert !");
        alertDialog.setMessage("Do you want Approve Leave ?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                // Approve Selected Leaves
                send_leave_approval_sap(leave_no);

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }

    public void fetch_indirect() {
        ArrayList<Gatepass> stateList = new ArrayList<Gatepass>();
        stateList.clear();
        stateList = dataHelper.getPendingGatepassInDirect();

        Log.d("LIST1", "&&&&" + stateList.size());

        if (stateList.size() > 0) {
            //create an ArrayAdaptar from the String Array
            dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.state_info, stateList);
            ListView listView = (ListView) getView().findViewById(R.id.listView1);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

           /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    States state = (States) parent.getItemAtPosition(position);

                    show_dailog(state.getLeaveNo());

                    *//*Toast.makeText(getContext(),"Clicked on Row: " + state.getName(),Toast.LENGTH_LONG).show();*//*

                }
            });*/
        } else {
            listView.setAdapter(null);
            Message msg = new Message();
            msg.obj = "No Gatepass Data Available";
            mHandler2.sendMessage(msg);

        }


    }

    public void fetch_direct() {
        ArrayList<Gatepass> stateList = new ArrayList<Gatepass>();
        stateList.clear();
        stateList = dataHelper.getPendingGatepassDirect();

        Log.d("LIST2", "&&&&" + stateList.size());

        if (stateList.size() > 0) {

            //create an ArrayAdaptar from the String Array
            dataAdapter = new MyCustomAdapter(getActivity(), R.layout.state_info, stateList);

            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

           /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    States state = (States) parent.getItemAtPosition(position);

                    show_dailog(state.getLeaveNo());

                    dataAdapter.remove(dataAdapter.getItem(position));
                    dataAdapter.notifyDataSetChanged();
                    *//*Toast.makeText(getContext(),"Clicked on Row: " + state.getName(),Toast.LENGTH_LONG).show();*//*

                }
            });*/
        } else {
            listView.setAdapter(null);
            Message msg = new Message();
            msg.obj = "No Gatepass Data Available";
            mHandler2.sendMessage(msg);

        }

    }

    @Override
    public void onResume() {

        ArrayList<Gatepass> stateList = new ArrayList<Gatepass>();
        stateList = dataHelper.getPendingGatepassDirect();

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.state_info, stateList);

        dataAdapter.notifyDataSetChanged();

        super.onResume();
    }


    public void SendApprovallToSAP(final String gp_no, final String pernr) {


        if (CustomUtility.isInternetOn(mContext)) {
            dataHelper.deletePendingGatepass();
            progressBar = new ProgressDialog(getActivity());
            progressBar.setCancelable(true);
            progressBar.setMessage("Sending Data...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            progressBarStatus = 0;

            new Thread(new Runnable() {
                public void run() {

                    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
                    param.add(new BasicNameValuePair("pernr", pernr));
                    param.add(new BasicNameValuePair("gp_no", gp_no));
                    param.add(new BasicNameValuePair("app_pernr", userModel.uid));

                    try {

                        String obj_gp_app = CustomHttpClient.executeHttpPost1(SapUrl.approval_Gatepass, param);

                        Log.d("approved_gp", obj_gp_app);

                        if (obj_gp_app != null) {
                            Message msg2 = new Message();
                            JSONArray ja_gp_app = new JSONArray(obj_gp_app);

                            for (int i = 0; i < ja_gp_app.length(); i++) {
                                JSONObject jo_gp_app = ja_gp_app.getJSONObject(i);

                                String msg = jo_gp_app.getString("msgtyp");
                                String msg1 = jo_gp_app.getString("text");
                                if (msg.equalsIgnoreCase("S")) {

                                    msg2.obj = msg1;
                                    mHandler.sendMessage(msg2);
                                    getActivity().finish();
                                } else {
                                    msg2.obj = msg1;
                                    mHandler.sendMessage(msg2);
                                }

                            }

                        }

                        progressBarStatus = 100;

                        // Updating the progress bar
                        progressBarHandler.post(new Runnable() {
                            public void run() {

                                progressBar.setProgress(progressBarStatus);
                            }
                        });


                        progressBar.cancel();
                        progressBar.dismiss();


                        Thread.sleep(5000);
                    } catch (Exception e) {

                        e.printStackTrace();

                    }


                }
            }).start();

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private class MyCustomAdapter extends ArrayAdapter<Gatepass> {

        private ArrayList<Gatepass> stateList;

        private MyCustomAdapter(Context context, int textViewResourceId,

                                ArrayList<Gatepass> stateList) {
            super(context, textViewResourceId, stateList);
            this.stateList = new ArrayList<Gatepass>();
            this.stateList.addAll(stateList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.gatepass_info, null);


                holder = new ViewHolder();
                holder.leave_no = (TextView) convertView.findViewById(R.id.gatePassNo);
                holder.pernr = (TextView) convertView.findViewById(R.id.pernr);
                holder.ename = (TextView) convertView.findViewById(R.id.name);
                holder.date = (TextView) convertView.findViewById(R.id.gpDate);
                holder.time = (TextView) convertView.findViewById(R.id.gpTime);
                holder.req_type = (TextView) convertView.findViewById(R.id.gpReqTypq);
                holder.gp_type = (TextView) convertView.findViewById(R.id.gpType);
                holder.approve = (TextView) convertView.findViewById(R.id.approv);

                convertView.setTag(holder);


            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Gatepass gp = stateList.get(position);

            /*holder.code.setText(" (" + state.getCode() + ")");*/
            holder.leave_no.setText(gp.getGp_no());
            holder.pernr.setText(gp.getPernr());
            holder.ename.setText(gp.getEname());
            holder.date.setText(gp.getDate());
            holder.time.setText(gp.getTime());
            holder.req_type.setText(gp.getReq_type());
            holder.gp_type.setText(gp.getGp_type());
            // holder.direct_indirect.setText(gp.getDirect_indirect());


            holder.approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Log.e("Number","%%%%"+gp.getGp_no()+" "+gp.getPernr());

                    SendApprovallToSAP(gp.getGp_no(), gp.getPernr());

                }
            });


            return convertView;
        }

        private void getSystemService(String layoutInflaterService) {
            throw new RuntimeException("Stub!");
        }

        private class ViewHolder {

            TextView leave_no;
            TextView pernr;
            TextView ename;
            TextView date;
            TextView time;
            TextView req_type;
            TextView gp_type;
            TextView approve;
        }

    }


}
