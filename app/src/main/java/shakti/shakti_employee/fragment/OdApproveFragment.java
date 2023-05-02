package shakti.shakti_employee.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.activity.OdApproveActivity;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.OD;
import shakti.shakti_employee.other.SapUrl;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OdApproveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OdApproveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OdApproveFragment extends Fragment {

    DatabaseHelper dataHelper = null;
    String obj_pending_od;
    String key_od_no;
    String od_horo;
    String od_ename;
    String od_work_status;
    String od_frm;
    String od_to;
    String visit_place;
    String purpose1;
    String purpose2;
    String purpose3;
    String remark;
    String od_direct_indirect;
    String od_app_return_msg = null;
    TextView bt_od_direct, bt_od_inline;
    String pressed_direct_indirect;
    ProgressDialog progressBar;
    Context context;
    MyCustomAdapter dataAdapter = null;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(getActivity(), mString, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    };
    private OnFragmentInteractionListener mListener;
    private DashboardActivity dashboardActivity;
    private ProgressDialog progressDialog;
    private LoggedInUser userModel;
    private Context mContext;
    private final int progressBarStatus = 0;
    private final Handler progressBarHandler = new Handler();

    public OdApproveFragment() {
        // Required empty public constructor
    }

    public static OdApproveFragment newInstance() {
        OdApproveFragment fragment = new OdApproveFragment();
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



        ArrayList<OD> pendingOdList = new ArrayList<OD>();
        pendingOdList = dataHelper.getPendingOdDirect();

        if (pendingOdList.size() > 0) {
            dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.od_list, pendingOdList);
        } else {

            Toast.makeText(getActivity().getApplicationContext(), "No Records Found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("OD Approve");

        View v = inflater.inflate(R.layout.fragment_od_approve, container, false);

        final ListView listView = v.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        //        insert_pending_od();

        pressed_direct_indirect = "D";

//        ArrayList<OD> pendingOdList = new ArrayList<OD>();
//        pendingOdList = dataHelper.getPendingOD();


        bt_od_direct = v.findViewById(R.id.bt_od_direct);
        bt_od_inline = v.findViewById(R.id.bt_od_inline);

        bt_od_direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bt_od_direct.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded_disabled));
                    bt_od_inline.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded));
                }

                pressed_direct_indirect = "D";
                fetch_direct();
            }
        });


        bt_od_inline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bt_od_direct.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded));
                    bt_od_inline.setBackground(mContext.getResources().getDrawable(R.drawable.all_corners_rounded_disabled));
                }
                pressed_direct_indirect = "I";
                fetch_indirect();
            }
        });


        if (pressed_direct_indirect == "D") {

            ArrayList<OD> pendingOdList = new ArrayList<OD>();
            pendingOdList = dataHelper.getPendingOdDirect();

            dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.od_list, pendingOdList);
//            ListView listView = (ListView) getView().findViewById(R.id.listView1);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    OD od = (OD) parent.getItemAtPosition(position);
                    show_dailog(od.getOd_no());
                    /*Toast.makeText(getContext(),"Clicked on Row: " + od.getOd_no(),Toast.LENGTH_LONG).show();*/
                }
            });
        }

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

    // Adapter for Pending List

    public void send_od_approval_sap(final String od_request_no) {

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        param.add(new BasicNameValuePair("od_no", od_request_no));
        param.add(new BasicNameValuePair("approver", userModel.uid));
        param.add(new BasicNameValuePair("password", userModel.password));


        progressDialog = ProgressDialog.show(getActivity(), "", "Please wait.. Approving OD!  ");

        new Thread(new Runnable() {
            @Override
            public void run() {
                String obj = null;

                try {
                    obj = CustomHttpClient.executeHttpPost1(SapUrl.approve_od, param);

                    Log.d("od_approve", param.toString());

                    JSONArray ja = new JSONArray(obj);
                    for (int i = 0; i < 1; i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        od_app_return_msg = jo.getString("msg");
                        Log.d("od_app_return_msg", od_app_return_msg);
                        /*Toast.makeText(getActivity(), leave_return_msg, Toast.LENGTH_LONG).show();*/
                        Message msg = new Message();
                        msg.obj = od_app_return_msg;
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

                    Log.d("OdReqno", od_request_no);
                    progressDialog.dismiss();
                    if (od_app_return_msg.equalsIgnoreCase("OD Approved Successfully")) {
                        db.deletePendingOdWhere(od_request_no);
                        Intent intent = new Intent(getActivity(), OdApproveActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }

        }).start();
    }

    public void show_dailog(final String od_no) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("OD Approve alert !");
        alertDialog.setMessage("Do you want Approve OD ?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

//                new TimeServiceDownloadData().stopSelf();
//                new TimeServiceDownloadData().onDestroy();

                // Approve Selected Leaves
                send_od_approval_sap(od_no);
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

    public void fetch_direct() {

        ArrayList<OD> pendingOdList = new ArrayList<OD>();
        pendingOdList = dataHelper.getPendingOdDirect();

        dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.od_list, pendingOdList);
        ListView listView = getView().findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                OD od = (OD) parent.getItemAtPosition(position);
                show_dailog(od.getOd_no());
                /*Toast.makeText(getContext(),"Clicked on Row: " + od.getOd_no(),Toast.LENGTH_LONG).show();*/
            }
        });
    }

    public void fetch_indirect() {
        ArrayList<OD> pendingOdList = new ArrayList<OD>();
        pendingOdList = dataHelper.getPendingOdIndirect();

        dataAdapter = new MyCustomAdapter(this.getActivity(), R.layout.od_list, pendingOdList);
        ListView listView = getView().findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                OD od = (OD) parent.getItemAtPosition(position);
                show_dailog(od.getOd_no());
                /*Toast.makeText(getContext(),"Clicked on Row: " + od.getOd_no(),Toast.LENGTH_LONG).show();*/
            }
        });
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

    private class MyCustomAdapter extends ArrayAdapter<OD> {

        private final ArrayList<OD> odList;

        public MyCustomAdapter(Context context, int textViewResourceId,

                               ArrayList<OD> odList) {
            super(context, textViewResourceId, odList);
            this.odList = new ArrayList<OD>();
            this.odList.addAll(odList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.od_list, null);

                holder = new ViewHolder();
                holder.od_no = convertView.findViewById(R.id.od_no);
                holder.od_ename = convertView.findViewById(R.id.od_ename);
                holder.od_horo = convertView.findViewById(R.id.od_horo);
                holder.od_work_status = convertView.findViewById(R.id.od_work_status);
                holder.od_frm = convertView.findViewById(R.id.od_from);
                holder.od_to = convertView.findViewById(R.id.od_to);
                holder.visit_place = convertView.findViewById(R.id.od_place);
                holder.purpose1 = convertView.findViewById(R.id.od_purpose);
                /*holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);*/

                convertView.setTag(holder);

                holder.od_no.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        OD od = (OD) cb.getTag();

                        Toast.makeText(getContext(), "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), Toast.LENGTH_LONG).show();

                        od.setSelected(cb.isChecked());
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            OD od = odList.get(position);

            /*holder.code.setText(" (" + state.getCode() + ")");*/
            holder.od_no.setText(od.getOd_no());
            holder.od_ename.setText(od.getEname());
            holder.od_horo.setText(od.getHoro());
            holder.od_work_status.setText(od.getWork_status());
            holder.od_frm.setText(od.getOd_from());
            holder.od_to.setText(od.getOd_to());
            holder.visit_place.setText(od.getVisit_place());
            holder.purpose1.setText(od.isPurpose1());

            holder.od_no.setTag(od);

            return convertView;
        }

        private void getSystemService(String layoutInflaterService) {
            throw new RuntimeException("Stub!");
        }

        private class ViewHolder {

            TextView od_no;
            TextView od_ename;
            TextView od_horo;
            TextView od_work_status;
            TextView od_frm;
            TextView od_to;
            TextView visit_place;
            TextView purpose1;
        }

    }
}
