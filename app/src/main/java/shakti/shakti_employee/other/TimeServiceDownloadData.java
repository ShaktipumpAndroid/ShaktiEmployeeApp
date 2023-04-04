package shakti.shakti_employee.other;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;

import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import shakti.shakti_employee.activity.CartSharedPreferences;
import shakti.shakti_employee.bean.EmployeeGPSActivityBean;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;

/**
 * Created by shakti on 12/3/2016.
 * capture lat long on every 15 min
 */


public class TimeServiceDownloadData extends Service {
    // constant

    //   public static final long NOTIFY_INTERVAL =   60 * 1 * 1000;    // 1000 = 1 second   15 min

    // public static final long NOTIFY_INTERVAL =   60 * 1 * 1000;    // 1000 = 1 second   1 min

    public static final long NOTIFY_INTERVAL = 1000 * 60 * 15;  // 15 Minute
    ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = null;
    DatabaseHelper db = null;
    SharedPreferences pref;
    Context context;
    DatabaseHelper dataHelper;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private Context mContext;
    private LoggedInUser userModel;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
        {
            startMyOwnForeground();
        }

        else
        {
            startForeground(1, new android.app.Notification());
        }
        // cancel if already existed
        if (mTimer != null) {
//            Toast.makeText(this, "already running", Toast.LENGTH_LONG).show();

            mTimer.cancel();
        } else {
            // recreate new
//            Toast.makeText(this, "create new command", Toast.LENGTH_LONG).show();
            mTimer = new Timer();

            employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
            db = new DatabaseHelper(getApplicationContext());


        }
        // schedule task


        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);

        mContext = this;
        userModel = new LoggedInUser(mContext);


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "shakti.shakti_employee";
        String channelName = "Shakti Employee";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        android.app.Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(android.app.Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("service_onstart", "onstartcommand");

        context = getApplicationContext();

        pref = CartSharedPreferences.createObject(context);


        final String pernr = pref.getString("pernr", "A");


        new Thread(new Runnable() {
            @Override
            public void run() {

                dataHelper = new DatabaseHelper(context);

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                Log.d("Download Services", "&&&&&");
                final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

// Emp Attendance

                param.add(new BasicNameValuePair("PERNR", userModel.uid));

                try {

                    String obj = CustomHttpClient.executeHttpPost1(SapUrl.attendance_report, param);

                    Log.d("att_emp_obj", obj);

                    if (obj != null) {

                        JSONObject jsonObj = new JSONObject(obj);
                        JSONArray ja = jsonObj.getJSONArray("attendance");


                        dataHelper.deleteattendance();

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jo = ja.getJSONObject(i);
                            // login = jo.getString("LOGIN");


                            dataHelper.insertAttendance(userModel.uid,
                                    jo.getString("begdat"),
                                    jo.getString("indz"),
                                    jo.getString("iodz"),
                                    jo.getString("totdz"),
                                    jo.getString("atn_status"),
                                    jo.getString("leave_typ"));
                        }


                    }

                    Thread.sleep(5000);

                } catch (Exception e) {

                }


//  Emp leave Balance

                String leavetype;

                dataHelper.deleteleaveBalance();

                try {


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
                    Thread.sleep(5000);
                } catch (Exception e) {

                }


//         Pending Leave for approval

                String obj_pending_leave = null;

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


                param.add(new BasicNameValuePair("app_pernr", userModel.uid));

                try {

                    String obj = CustomHttpClient.executeHttpPost1(SapUrl.pending_leave, param);

                    Log.d("att_emp_obj", obj);

                    if (obj != null) {

                        dataHelper.deletePendingLeave();

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

                    }
                    Thread.sleep(5000);
                } catch (Exception e) {

                }


// Pending OD for Approval


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
                try {


                    param.add(new BasicNameValuePair("app_pernr", userModel.uid));

                    obj_pending_od = CustomHttpClient.executeHttpPost1(SapUrl.pending_od, param);

                    if (obj_pending_leave != null) {
                        dataHelper.deletePendingOd();
                    }

                    Log.d("pending_od", "" + obj_pending_od);

                    JSONArray ja_mat = new JSONArray(obj_pending_od);

                    /*Log.d("json55", "" + ja_mat);*/


                    for (int i = 0; i < ja_mat.length(); i++) {

                        JSONObject jo_matnr = ja_mat.getJSONObject(i);

                        key_od_no = jo_matnr.getString("odno");
                        od_horo = jo_matnr.getString("horo");
                        od_ename = jo_matnr.getString("ename");
                        od_frm = jo_matnr.getString("odstdateC");
                        od_to = jo_matnr.getString("odedateC");
                        od_work_status = jo_matnr.getString("atnStatus");
                        visit_place = jo_matnr.getString("vplace");
                        purpose1 = jo_matnr.getString("purpose1");
                        purpose2 = jo_matnr.getString("purpose2");
                        purpose3 = jo_matnr.getString("purpose3");
                        remark = jo_matnr.getString("remark");
                        od_direct_indirect = jo_matnr.getString("directIndirect");

                        dataHelper.createPendingOD(key_od_no, od_horo, od_ename, od_frm, od_to, od_work_status,
                                visit_place, purpose1, purpose2, purpose3, remark, od_direct_indirect);

                    }
                    Thread.sleep(5000);
                } catch (Exception e) {

                }


// Emp Leave


                param.add(new BasicNameValuePair("PERNR", userModel.uid));

                try {

                    String obj = CustomHttpClient.executeHttpPost1(SapUrl.leave_report, param);

                    Log.d("lev_emp_obj", obj);

                    if (obj != null) {

                        JSONObject jsonObj = new JSONObject(obj);
                        JSONArray ja = jsonObj.getJSONArray("leave");


                        dataHelper.deleteleave();

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jo = ja.getJSONObject(i);
                            // login = jo.getString("LOGIN");


                            dataHelper.insertLeave(userModel.uid,
                                    jo.getString("leav_no"),
                                    jo.getString("horo"),
                                    jo.getString("lev_frm"),
                                    jo.getString("lev_to"),
                                    jo.getString("lev_typ"),
                                    jo.getString("apphod"),
                                    jo.getString("dele"),
                                    jo.getString("reason"));
                        }

                    }
                    Thread.sleep(5000);

                } catch (Exception e) {

                }

                //  Emp OD

                param.add(new BasicNameValuePair("pernr", userModel.uid));

                try {

                    String obj = CustomHttpClient.executeHttpPost1(SapUrl.od_report, param);

                    Log.d("od_emp_obj", obj);

                    if (obj != null) {

                        JSONObject jsonObj = new JSONObject(obj);
                        JSONArray ja = jsonObj.getJSONArray("od");
                        dataHelper.deleteod();

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jo = ja.getJSONObject(i);
                            // login = jo.getString("LOGIN");

                            dataHelper.insertOD(userModel.uid,
                                    jo.getString("odno"),
                                    jo.getString("odaprdt_c"),
                                    jo.getString("horo"),
                                    jo.getString("odstdate_c"),
                                    jo.getString("odedate_c"),
                                    jo.getString("atn_status"),
                                    jo.getString("vplace"),
                                    jo.getString("purpose1"));
                        }

                    }
                    Thread.sleep(5000);


                } catch (Exception e) {

                }

// Emp Info


                param.add(new BasicNameValuePair("pernr", userModel.uid));

                try {

                    String obj = CustomHttpClient.executeHttpPost1(SapUrl.employee_info, param);

                    Log.d("emp_obj", obj);

                    if (obj != null) {

                        JSONObject jsonObj = new JSONObject(obj);
                        JSONArray ja = jsonObj.getJSONArray("emp");
                        dataHelper.deleteempinfo();

                        for (int i = 0; i < ja.length(); i++) {

                            JSONObject jo = ja.getJSONObject(i);
                            // login = jo.getString("LOGIN");

                            dataHelper.insertEmployeeInfo(userModel.uid,
                                    jo.getString("btrtl_txt"),
                                    jo.getString("persk_txt"),
                                    jo.getString("telnr"),
                                    jo.getString("email_shkt"),
                                    jo.getString("hod_ename"),
                                    jo.getString("address"),
                                    jo.getString("birth1"),
                                    jo.getString("bankn"),
                                    jo.getString("bank_txt"));
                        }

                    }

                    Thread.sleep(5000);

                } catch (Exception e) {

                }


//
//                mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);

//


//               new SAPWebService().getLeaveBal(context,pernr);
//               new SAPWebService().getAttendanceEmp(context,pernr);
//               new SAPWebService().getPendingLeaveForApp(context,pernr);
//               new SAPWebService().getPendingOdForApp(context,pernr);
//               new SAPWebService().getLeaveEmp(context,pernr);
//               new SAPWebService().getODEmp(context,pernr);
//               new SAPWebService().getInfoEmp(context,pernr);
                // stopSelf();
                // return START_NOT_STICKY;
                //Toast.makeText(this, "start command", Toast.LENGTH_LONG).show();

            }
        }).start();

        return Service.START_STICKY;

    }


    @Override
    public void onDestroy() {
        //  isRunning = false;
        mTimer.cancel();
        // Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }


    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast

//                    getLatLong();    Commented as no need in data download

                    // run in background
                    new Worker().execute();


//
//                    Toast.makeText(getApplicationContext(), getLatLong(),
//                            Toast.LENGTH_SHORT).show();
                }

            });
        }

        private String getLatLong() {
            GPSTracker gps;
            gps = new GPSTracker(getApplicationContext());
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();


            new Capture_employee_gps_location(getApplicationContext(), "0", "");


            String latlong;
            latlong = latitude + "," + longitude;
            Log.d("background", "" + latlong);
            return latlong.trim();
        }

    }


    private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {
                SAPWebService_AutoDataDownload con = null;

                userModel = new LoggedInUser(getApplicationContext());

//                Toast.makeText(getApplicationContext(),"Download attendance data",Toast.LENGTH_SHORT).show();

                Log.d("att_emp_tt", userModel.uid);

                con.getAttendanceEmp(getApplicationContext(), userModel.uid);
//                con.getLeaveEmp(getApplicationContext(),userModel.uid);

                // sync employee gps data to sap every 15 min
//                             if (CustomUtility.isOnline(getApplicationContext())) {
//                                 new SyncDataToSAP_New().SendEmployeeGPS(getApplicationContext());
//                             }


            } catch (Exception e) {
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Log.i("SomeTag", System.currentTimeMillis() / 1000L
//                    + " post execute \n" + result);
        }


    }

}
