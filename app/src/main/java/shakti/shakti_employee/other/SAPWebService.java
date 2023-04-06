package shakti.shakti_employee.other;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import shakti.shakti_employee.bean.BeanActiveEmployee;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;

/**
 * Created by shakti on 1/23/2017.
 */
public class SAPWebService {

    String obj_active_employee;
    String pernr, ename, btext;


    // For Pending Leave List

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


    // For pending OD List

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

    String obj_pending_od;
    DatabaseHelper dataHelper;


    // For leave Balance
    String leavetype;


    /****************************
     * create Leave Balance
     ***********************************************/
    // Create DatabaseHelper instance
    public int getLeaveBal(Context context, String att_emp) {

        int progressBarStatus = 0;

        dataHelper = new DatabaseHelper(context);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);


        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("pernr", att_emp));
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
            progressBarStatus = 70;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return progressBarStatus;
    }

    /****************************
     * Create Pending Leave for Approval table
     ***********************************************/

    public int getPendingLeaveForApp(Context context, String att_emp) {

        dataHelper = new DatabaseHelper(context);

        dataHelper.deletependingleave();

        int progressBarStatus = 0;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);


        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        param.add(new BasicNameValuePair("app_pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.pending_leave, param);

            Log.d("att_emp_obj", obj);

            if (obj != null) {

                param.add(new BasicNameValuePair("app_pernr", att_emp));

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
            progressBarStatus = 50;
        } catch (Exception e) {
         e.printStackTrace();
        }

        return progressBarStatus;
    }


    /****************************
     * Create Pending Od for Approval table
     ***********************************************/

    public int getPendingOdForApp(Context context, String att_emp) {

        dataHelper = new DatabaseHelper(context);

        dataHelper.deletependingod();

        int progressBarStatus = 0;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);


        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

//        param.add(new BasicNameValuePair("app_pernr", att_emp));

        try {


            param.add(new BasicNameValuePair("app_pernr", att_emp));

            obj_pending_od = CustomHttpClient.executeHttpPost1(SapUrl.pending_od, param);

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

        } catch (Exception e) {
            progressBarStatus = 60;
        }

        return progressBarStatus;
    }


    /****************************
     * create Leave table
     ***********************************************/

    public int getLeaveEmp(Context context, String att_emp) {

        ArrayList<String> al;

        dataHelper = new DatabaseHelper(context);

        dataHelper.deleteleave();

        int progressBarStatus = 0;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        Log.d("lev_emp", att_emp);

        param.add(new BasicNameValuePair("PERNR", att_emp));

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


                    dataHelper.insertLeave(att_emp,
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

            progressBarStatus = 70;
        } catch (Exception e) {
            progressBarStatus = 70;
        }

        return progressBarStatus;
    }


    /****************************
     * create OD table
     ***********************************************/

    public int getODEmp(Context context, String att_emp) {


        ArrayList<String> al;

        dataHelper = new DatabaseHelper(context);

        dataHelper.deleteod();

        int progressBarStatus = 0;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        al = new ArrayList<>();
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        Log.d("od_emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

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

                    dataHelper.insertOD(att_emp,
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


            progressBarStatus = 80;
        } catch (Exception e) {
            progressBarStatus = 80;
        }

        return progressBarStatus;
    }


    /****************************
     * create OD table
     ***********************************************/

    public int getInfoEmp(Context context, String att_emp) {

        ArrayList<String> al;

        dataHelper = new DatabaseHelper(context);

        dataHelper.deleteempinfo();

        int progressBarStatus= 0;

        al = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

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

                    dataHelper.insertEmployeeInfo(att_emp,
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


            progressBarStatus = 40;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }


    /****************************
     * create attendance table
     ***********************************************/
    // Create DatabaseHelper instance


    public int getActiveEmployee(Context context, String att_emp) {

        dataHelper = new DatabaseHelper(context);

        dataHelper.deleteSyncedCreatedTaskWhere();
        dataHelper.deleteSyncedCompletedTaskWhere();
        dataHelper.deleteActiveEmployee();

        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);

            JSONArray ja_active;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);

                if(jsonObj.has("activeemployee") && !String.valueOf(jsonObj.getJSONArray("activeemployee")).isEmpty()){
                    dataHelper.deleteActiveEmployee();

                    ja_active = jsonObj.getJSONArray("activeemployee");

                    Log.d("json_active", "" + ja_active);


                    for (int i = 0; i < ja_active.length(); i++) {

                        JSONObject jo_active = ja_active.getJSONObject(i);


                        pernr = jo_active.getString("pernr");
                        ename = jo_active.getString("ename");
                        btext = jo_active.getString("btext");
                        BeanActiveEmployee beanActiveEmployee = new BeanActiveEmployee(pernr, ename, btext);
                        dataHelper.insertActiveEmployee(beanActiveEmployee);

                    }
                }

            }

            progressBarStatus = 10;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getLeaveBalance(Context context, String att_emp) {

        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteleaveBalance();
        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);
            JSONArray ja_leave_balance;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if(jsonObj.has("leavebalance") && !String.valueOf(jsonObj.getJSONArray("leavebalance")).isEmpty()) {
                    dataHelper.deleteleaveBalance();

                    ja_leave_balance = jsonObj.getJSONArray("leavebalance");

                    Log.d("json_leave_balance", "" + ja_leave_balance);

                    for (int i = 0; i < ja_leave_balance.length(); i++) {

                        JSONObject jo_leave_balance = ja_leave_balance.getJSONObject(i);

                        leavetype = jo_leave_balance.getString("leaveType");
                        dataHelper.createLeaveBalance(leavetype);


                    }

                }

            }


            progressBarStatus = 20;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }


    public int getAttendanceEmp(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteattendance();
        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);
            JSONArray ja_attendance_emp = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if(jsonObj.has("attendanceemp") && !String.valueOf(jsonObj.getJSONArray("attendanceemp")).isEmpty()) {

                    dataHelper.deleteattendance();

                    ja_attendance_emp = jsonObj.getJSONArray("attendanceemp");
                    Log.d("json_attendance_emp", "" + ja_attendance_emp);


                    for (int i = 0; i < ja_attendance_emp.length(); i++) {

                        JSONObject jo_attendance_emp = ja_attendance_emp.getJSONObject(i);

                        dataHelper.insertAttendance(att_emp,
                                jo_attendance_emp.getString("begdat"),
                                jo_attendance_emp.getString("indz"),
                                jo_attendance_emp.getString("iodz"),
                                jo_attendance_emp.getString("totdz"),
                                jo_attendance_emp.getString("atn_status"),
                                jo_attendance_emp.getString("leave_typ"));
                    }
                }


            }
            progressBarStatus = 30;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getPendingLeave(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deletependingleave();
        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);
            JSONArray ja_pending_leave = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if(jsonObj.has("pendingleave") && !String.valueOf(jsonObj.getJSONArray("pendingleave")).isEmpty()) {

                    dataHelper.deletependingleave();

                    ja_pending_leave = jsonObj.getJSONArray("pendingleave");
                    Log.d("json_pending_leave", "" + ja_pending_leave);


                    for (int i = 0; i < ja_pending_leave.length(); i++) {

                        JSONObject jo_pending_leave = ja_pending_leave.getJSONObject(i);


                        KEY_LEV_NO = jo_pending_leave.getString("leavNo");
                        HORO = jo_pending_leave.getString("horo");
                        ENAME = jo_pending_leave.getString("name");
                        LEV_TYP = jo_pending_leave.getString("dedQuta1");
                        LEV_FRM = jo_pending_leave.getString("levFr");
                        LEV_TO = jo_pending_leave.getString("levT");
                        REASON = jo_pending_leave.getString("reason");
                        CHRG_NAME1 = jo_pending_leave.getString("nameperl");
                        CHRG_NAME2 = jo_pending_leave.getString("nameperl2");
                        CHRG_NAME3 = jo_pending_leave.getString("nameperl3");
                        CHRG_NAME4 = jo_pending_leave.getString("nameperl4");
                        DIRECT_INDIRECT = jo_pending_leave.getString("directIndirect");

                        dataHelper.createPendingLeave(KEY_LEV_NO, HORO, ENAME, LEV_TYP, LEV_FRM, LEV_TO,
                                REASON, CHRG_NAME1, CHRG_NAME2, CHRG_NAME3, CHRG_NAME4, DIRECT_INDIRECT);

                    }


                }

            }
            progressBarStatus = 66;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getPendingOD(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deletependingod();
        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();


        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);
            JSONArray ja_pending_od = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if(jsonObj.has("pendingod") && !String.valueOf(jsonObj.getJSONArray("pendingod")).isEmpty()) {

                    dataHelper.deletependingod();

                    ja_pending_od = jsonObj.getJSONArray("pendingod");
                    Log.d("json_pending_od", "" + ja_pending_od);

                    for (int i = 0; i < ja_pending_od.length(); i++) {

                        JSONObject jo_pending_od = ja_pending_od.getJSONObject(i);

                        key_od_no = jo_pending_od.getString("odno");
                        od_horo = jo_pending_od.getString("horo");
                        od_ename = jo_pending_od.getString("ename");
                        od_frm = jo_pending_od.getString("odstdateC");
                        od_to = jo_pending_od.getString("odedateC");
                        od_work_status = jo_pending_od.getString("atnStatus");
                        visit_place = jo_pending_od.getString("vplace");
                        purpose1 = jo_pending_od.getString("purpose1");
                        purpose2 = jo_pending_od.getString("purpose2");
                        purpose3 = jo_pending_od.getString("purpose3");
                        remark = jo_pending_od.getString("remark");
                        od_direct_indirect = jo_pending_od.getString("directIndirect");

                        dataHelper.createPendingOD(key_od_no, od_horo, od_ename, od_frm, od_to, od_work_status,
                                visit_place, purpose1, purpose2, purpose3, remark, od_direct_indirect);

                    }
                }


            }
            progressBarStatus = 67;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getLeaveEmployee(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteleave();
        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);
            JSONArray ja_leave_emp = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);

                if(jsonObj.has("leaveemp") && !String.valueOf(jsonObj.getJSONArray("leaveemp")).isEmpty()) {

                    dataHelper.deleteleave();

                    ja_leave_emp = jsonObj.getJSONArray("leaveemp");
                    Log.d("json_leave_emp", "" + ja_leave_emp);

                    for (int i = 0; i < ja_leave_emp.length(); i++) {

                        JSONObject jo_leave_emp = ja_leave_emp.getJSONObject(i);


                        dataHelper.insertLeave(att_emp,
                                jo_leave_emp.getString("leav_no"),
                                jo_leave_emp.getString("horo"),
                                jo_leave_emp.getString("lev_frm"),
                                jo_leave_emp.getString("lev_to"),
                                jo_leave_emp.getString("lev_typ"),
                                jo_leave_emp.getString("apphod"),
                                jo_leave_emp.getString("dele"),
                                jo_leave_emp.getString("reason"));

                    }

                }

            }
            progressBarStatus = 77;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getODEmployee(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteod();
        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);
            JSONArray ja_od_emp = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);

                if( jsonObj.has("odemp")&& !String.valueOf(jsonObj.getJSONArray("odemp")).isEmpty()) {
                      dataHelper.deleteod();

                      ja_od_emp = jsonObj.getJSONArray("odemp");
                      Log.d("json_od_emp", "" + ja_od_emp);


                      for (int i = 0; i < ja_od_emp.length(); i++) {

                          JSONObject jo_od_emp = ja_od_emp.getJSONObject(i);
                          // login = jo.getString("LOGIN");

                          dataHelper.insertOD(att_emp,
                                  jo_od_emp.getString("odno"),
                                  jo_od_emp.getString("odaprdt_c"),
                                  jo_od_emp.getString("horo"),
                                  jo_od_emp.getString("odstdate_c"),
                                  jo_od_emp.getString("odedate_c"),
                                  jo_od_emp.getString("atn_status"),
                                  jo_od_emp.getString("vplace"),
                                  jo_od_emp.getString("purpose1"));
                      }
                  }
            }
            progressBarStatus = 85;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getPendingTask(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deletePendingTask();

        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);

            JSONArray ja_task_pending = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if( jsonObj.has("pendingtask")&& !String.valueOf(jsonObj.getJSONArray("pendingtask")).isEmpty()) {

                    dataHelper.deletePendingTask();

                    ja_task_pending = jsonObj.getJSONArray("pendingtask");
                    Log.d("json_task_pending", "" + ja_task_pending);


                    for (int i = 0; i < ja_task_pending.length(); i++) {

                        JSONObject jo_task_pending = ja_task_pending.getJSONObject(i);

                        dataHelper.insertTaskPending(jo_task_pending.getString("asgnr1"),
                                jo_task_pending.getString("mrc_date1"),
                                "",
                                jo_task_pending.getString("agenda"),
                                att_emp,
                                jo_task_pending.getString("com_date_from1"),
                                jo_task_pending.getString("com_date_to1"),
                                jo_task_pending.getString("mrct1"),
                                jo_task_pending.getString("dep_name"),
                                jo_task_pending.getString("dno"),
                                jo_task_pending.getString("srno"),
                                jo_task_pending.getString("chker1"));
                    }


                }

            }
            progressBarStatus = 89;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getCountry(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteCountryData();

        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);


            JSONArray ja_country = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if( jsonObj.has("country")&& !String.valueOf(jsonObj.getJSONArray("country")).isEmpty()) {

                    dataHelper.deleteCountryData();

                    ja_country = jsonObj.getJSONArray("country");
                    Log.d("json_country", "" + ja_country);


                    for (int i = 0; i < ja_country.length(); i++) {

                        JSONObject jo_country = ja_country.getJSONObject(i);

                        dataHelper.insertCountry(jo_country.getString("land1"), jo_country.getString("landx"));
                    }
                }


            }
            progressBarStatus = 89;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getRegion(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteRegionData();

        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);

            JSONArray ja_region = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if( jsonObj.has("region")&& !String.valueOf(jsonObj.getJSONArray("region")).isEmpty()) {

                    dataHelper.deleteRegionData();

                    ja_region = jsonObj.getJSONArray("region");
                    Log.d("json_region", "" + ja_region);


                    for (int i = 0; i < ja_region.length(); i++) {

                        JSONObject jo_region = ja_region.getJSONObject(i);

                        dataHelper.insertRegion(jo_region.getString("land1"), jo_region.getString("bland"), jo_region.getString("regio"), jo_region.getString("bezei"));
                    }
                }
            }
            progressBarStatus = 92;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getDistrict(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteDistrictData();

        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);


            JSONArray ja_district = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if (jsonObj.has("district") && !String.valueOf(jsonObj.getJSONArray("district")).isEmpty()) {

                    dataHelper.deleteDistrictData();

                    ja_district = jsonObj.getJSONArray("district");
                    Log.d("json_district", "" + ja_district);


                    for (int i = 0; i < ja_district.length(); i++) {

                        JSONObject jo_district = ja_district.getJSONObject(i);


                        dataHelper.insertDistrict(jo_district.getString("land1"), jo_district.getString("regio"), jo_district.getString("cityc"), jo_district.getString("bezei"));
                    }

                }
            }
            progressBarStatus = 96;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getTehsil(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteTehsilData();

        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);

            JSONArray ja_tehsil = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if (jsonObj.has("tehsil") && !String.valueOf(jsonObj.getJSONArray("tehsil")).isEmpty()) {


                    dataHelper.deleteTehsilData();

                    ja_tehsil = jsonObj.getJSONArray("tehsil");
                    Log.d("json_tehsil", "" + ja_tehsil);


                    for (int i = 0; i < ja_tehsil.length(); i++) {

                        JSONObject jo_tehsil = ja_tehsil.getJSONObject(i);


                        dataHelper.insertTehsil(jo_tehsil.getString("land1"), jo_tehsil.getString("regio"), jo_tehsil.getString("district"), jo_tehsil.getString("tehsil"), jo_tehsil.getString("tehsil_text"));
                    }
                }
            }
            progressBarStatus = 97;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getTaxcode(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteTaxcodeData();

        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);

            JSONArray ja_taxcode = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if (jsonObj.has("taxcode") && !String.valueOf(jsonObj.getJSONArray("taxcode")).isEmpty()) {

                    dataHelper.deleteTaxcodeData();

                    ja_taxcode = jsonObj.getJSONArray("taxcode");
                    Log.d("json_taxcode", "" + ja_taxcode);


                    for (int i = 0; i < ja_taxcode.length(); i++) {

                        JSONObject jo_taxcode = ja_taxcode.getJSONObject(i);


                        dataHelper.insertTaxcode(jo_taxcode.getString("mandt"), jo_taxcode.getString("tax_code"), jo_taxcode.getString("text"));
                    }
                }
            }
            progressBarStatus = 98;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getExpenses(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteExpensesData();

        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);


            JSONArray ja_expenses = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if (jsonObj.has("exptype") && !String.valueOf(jsonObj.getJSONArray("exptype")).isEmpty()) {

                    dataHelper.deleteExpensesData();

                    ja_expenses = jsonObj.getJSONArray("exptype");
                    Log.d("json_expenses", "" + ja_expenses);


                    for (int i = 0; i < ja_expenses.length(); i++) {

                        JSONObject jo_expenses = ja_expenses.getJSONObject(i);


                        dataHelper.insertExpenses(jo_expenses.getString("spkzl"), jo_expenses.getString("sptxt"));
                    }
                }
            }
            progressBarStatus = 99;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }

    public int getCurrency(Context context, String att_emp) {


        dataHelper = new DatabaseHelper(context);
        dataHelper.deleteCurrencyData();

        int progressBarStatus= 0;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);

        final ArrayList<NameValuePair> param = new ArrayList<>();
        param.clear();
        Log.d("emp", att_emp);

        param.add(new BasicNameValuePair("pernr", att_emp));

        try {

            String obj = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP,param);

            JSONArray ja_currency = null;

            if (obj != null) {

                JSONObject jsonObj = new JSONObject(obj);
                if (jsonObj.has("curr") && !String.valueOf(jsonObj.getJSONArray("curr")).isEmpty()) {

                    dataHelper.deleteCurrencyData();

                    ja_currency = jsonObj.getJSONArray("curr");
                    Log.d("json_curr", "" + ja_currency);


                    for (int i = 0; i < ja_currency.length(); i++) {

                        JSONObject jo_currency = ja_currency.getJSONObject(i);


                        dataHelper.insertCurrency(jo_currency.getString("waers"), jo_currency.getString("ltext"));
                    }
                }
            }
            progressBarStatus = 100;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return progressBarStatus;
    }
    public int getAllData(Context context, String att_emp) {

        int progressBarStatus = 0;

        dataHelper = new DatabaseHelper(context);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);


        //final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();


        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("pernr", att_emp));


        try {

            /*String obj = CustomHttpClient.executeHttpPost1(SapUrl.active_employee, param);*/
            obj_active_employee = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_ANDROID_TO_SAP, param);

            Log.d("output_sync", obj_active_employee);

            JSONObject jsonObj = new JSONObject(obj_active_employee);

            JSONArray ja_active = null;
            JSONArray ja_leave_balance = null;
            JSONArray ja_attendance_emp = null;
            JSONArray ja_pending_leave = null;
            JSONArray ja_pending_od = null;
            JSONArray ja_leave_emp = null;
            JSONArray ja_od_emp = null;
            JSONArray ja_info_emp = null;
            JSONArray ja_task_pending = null;
            JSONArray ja_country = null;
            JSONArray ja_region = null;
            JSONArray ja_district = null;
            JSONArray ja_tehsil = null;
            JSONArray ja_taxcode = null;
            JSONArray ja_expenses = null;
            JSONArray ja_currency = null;


            //  Active Employee
            //Log.d("json_before", "Hello");
            if (jsonObj.has("activeemployee")) {


                dataHelper.deleteSyncedCreatedTaskWhere();
                dataHelper.deleteSyncedCompletedTaskWhere();

                dataHelper.deleteActiveEmployee();

                ja_active = jsonObj.getJSONArray("activeemployee");

                Log.d("json_active", "" + ja_active);


                for (int i = 0; i < ja_active.length(); i++) {

                    JSONObject jo_active = ja_active.getJSONObject(i);


                    pernr = jo_active.getString("pernr");
                    ename = jo_active.getString("ename");
                    btext = jo_active.getString("btext");
                    BeanActiveEmployee beanActiveEmployee = new BeanActiveEmployee(pernr, ename, btext);
                    dataHelper.insertActiveEmployee(beanActiveEmployee);

                }

            }


            // Leave balance


            if (jsonObj.has("leavebalance")) {

                dataHelper.deleteleaveBalance();

                ja_leave_balance = jsonObj.getJSONArray("leavebalance");

                Log.d("json_leave_balance", "" + ja_leave_balance);

                for (int i = 0; i < ja_leave_balance.length(); i++) {

                    JSONObject jo_leave_balance = ja_leave_balance.getJSONObject(i);

                    leavetype = jo_leave_balance.getString("leaveType");
                    dataHelper.createLeaveBalance(leavetype);

                }


            }


            // Attendance Emp

            if (jsonObj.has("attendanceemp")) {

                dataHelper.deleteattendance();

                ja_attendance_emp = jsonObj.getJSONArray("attendanceemp");
                Log.d("json_attendance_emp", "" + ja_attendance_emp);


                for (int i = 0; i < ja_attendance_emp.length(); i++) {

                    JSONObject jo_attendance_emp = ja_attendance_emp.getJSONObject(i);

                    dataHelper.insertAttendance(att_emp,
                            jo_attendance_emp.getString("begdat"),
                            jo_attendance_emp.getString("indz"),
                            jo_attendance_emp.getString("iodz"),
                            jo_attendance_emp.getString("totdz"),
                            jo_attendance_emp.getString("atn_status"),
                            jo_attendance_emp.getString("leave_typ"));
                }

            }


            // Pending Leave

            if (jsonObj.has("pendingleave")) {

                dataHelper.deletependingleave();

                ja_pending_leave = jsonObj.getJSONArray("pendingleave");
                Log.d("json_pending_leave", "" + ja_pending_leave);


                for (int i = 0; i < ja_pending_leave.length(); i++) {

                    JSONObject jo_pending_leave = ja_pending_leave.getJSONObject(i);


                    KEY_LEV_NO = jo_pending_leave.getString("leavNo");
                    HORO = jo_pending_leave.getString("horo");
                    ENAME = jo_pending_leave.getString("name");
                    LEV_TYP = jo_pending_leave.getString("dedQuta1");
                    LEV_FRM = jo_pending_leave.getString("levFr");
                    LEV_TO = jo_pending_leave.getString("levT");
                    REASON = jo_pending_leave.getString("reason");
                    CHRG_NAME1 = jo_pending_leave.getString("nameperl");
                    CHRG_NAME2 = jo_pending_leave.getString("nameperl2");
                    CHRG_NAME3 = jo_pending_leave.getString("nameperl3");
                    CHRG_NAME4 = jo_pending_leave.getString("nameperl4");
                    DIRECT_INDIRECT = jo_pending_leave.getString("directIndirect");

                    dataHelper.createPendingLeave(KEY_LEV_NO, HORO, ENAME, LEV_TYP, LEV_FRM, LEV_TO,
                            REASON, CHRG_NAME1, CHRG_NAME2, CHRG_NAME3, CHRG_NAME4, DIRECT_INDIRECT);

                }

            }


            // Pending OD


            if (jsonObj.has("pendingod")) {

                dataHelper.deletependingod();

                ja_pending_od = jsonObj.getJSONArray("pendingod");
                Log.d("json_pending_od", "" + ja_pending_od);

                for (int i = 0; i < ja_pending_od.length(); i++) {

                    JSONObject jo_pending_od = ja_pending_od.getJSONObject(i);

                    key_od_no = jo_pending_od.getString("odno");
                    od_horo = jo_pending_od.getString("horo");
                    od_ename = jo_pending_od.getString("ename");
                    od_frm = jo_pending_od.getString("odstdateC");
                    od_to = jo_pending_od.getString("odedateC");
                    od_work_status = jo_pending_od.getString("atnStatus");
                    visit_place = jo_pending_od.getString("vplace");
                    purpose1 = jo_pending_od.getString("purpose1");
                    purpose2 = jo_pending_od.getString("purpose2");
                    purpose3 = jo_pending_od.getString("purpose3");
                    remark = jo_pending_od.getString("remark");
                    od_direct_indirect = jo_pending_od.getString("directIndirect");

                    dataHelper.createPendingOD(key_od_no, od_horo, od_ename, od_frm, od_to, od_work_status,
                            visit_place, purpose1, purpose2, purpose3, remark, od_direct_indirect);

                }

            }


            // Leave Emp


            if (jsonObj.has("leaveemp")) {

                dataHelper.deleteleave();

                ja_leave_emp = jsonObj.getJSONArray("leaveemp");
                Log.d("json_leave_emp", "" + ja_leave_emp);

                for (int i = 0; i < ja_leave_emp.length(); i++) {

                    JSONObject jo_leave_emp = ja_leave_emp.getJSONObject(i);


                    dataHelper.insertLeave(att_emp,
                            jo_leave_emp.getString("leav_no"),
                            jo_leave_emp.getString("horo"),
                            jo_leave_emp.getString("lev_frm"),
                            jo_leave_emp.getString("lev_to"),
                            jo_leave_emp.getString("lev_typ"),
                            jo_leave_emp.getString("apphod"),
                            jo_leave_emp.getString("dele"),
                            jo_leave_emp.getString("reason"));
                }

            }


            // OD Emp


            if (jsonObj.has("odemp")) {

                dataHelper.deleteod();

                ja_od_emp = jsonObj.getJSONArray("odemp");
                Log.d("json_od_emp", "" + ja_od_emp);


                for (int i = 0; i < ja_od_emp.length(); i++) {

                    JSONObject jo_od_emp = ja_od_emp.getJSONObject(i);
                    // login = jo.getString("LOGIN");

                    dataHelper.insertOD(att_emp,
                            jo_od_emp.getString("odno"),
                            jo_od_emp.getString("odaprdt_c"),
                            jo_od_emp.getString("horo"),
                            jo_od_emp.getString("odstdate_c"),
                            jo_od_emp.getString("odedate_c"),
                            jo_od_emp.getString("atn_status"),
                            jo_od_emp.getString("vplace"),
                            jo_od_emp.getString("purpose1"));
                }

            }


            // Info Emp

            if (jsonObj.has("infoemp")) {

                dataHelper.deleteempinfo();

                ja_info_emp = jsonObj.getJSONArray("infoemp");
                Log.d("json_info_emp", "" + ja_info_emp);


                for (int i = 0; i < ja_info_emp.length(); i++) {

                    JSONObject jo_info_emp = ja_info_emp.getJSONObject(i);
                    // login = jo.getString("LOGIN");

                    dataHelper.insertEmployeeInfo(att_emp,
                            jo_info_emp.getString("btrtl_txt"),
                            jo_info_emp.getString("persk_txt"),
                            jo_info_emp.getString("telnr"),
                            jo_info_emp.getString("email_shkt"),
                            jo_info_emp.getString("hod_ename"),
                            jo_info_emp.getString("address"),
                            jo_info_emp.getString("birth1"),
                            jo_info_emp.getString("bankn"),
                            jo_info_emp.getString("bank_txt"));
                }
            }


            // Pending Task For Completion

            if (jsonObj.has("pendingtask")) {


                dataHelper.deletePendingTask();

                ja_task_pending = jsonObj.getJSONArray("pendingtask");
                Log.d("json_task_pending", "" + ja_task_pending);


                for (int i = 0; i < ja_task_pending.length(); i++) {

                    JSONObject jo_task_pending = ja_task_pending.getJSONObject(i);
                    // login = jo.getString("LOGIN");

                    dataHelper.insertTaskPending(jo_task_pending.getString("asgnr1"),
                            jo_task_pending.getString("mrc_date1"),
                            "",
                            jo_task_pending.getString("agenda"),
                            att_emp,
                            jo_task_pending.getString("com_date_from1"),
                            jo_task_pending.getString("com_date_to1"),
                            jo_task_pending.getString("mrct1"),
                            jo_task_pending.getString("dep_name"),
                            jo_task_pending.getString("dno"),
                            jo_task_pending.getString("srno"),
                            jo_task_pending.getString("chker1"));
                }
            }


            if (jsonObj.has("country")) {


                dataHelper.deleteCountryData();

                ja_country = jsonObj.getJSONArray("country");
                Log.d("json_country", "" + ja_country);


                for (int i = 0; i < ja_country.length(); i++) {

                    JSONObject jo_country = ja_country.getJSONObject(i);


                    dataHelper.insertCountry(jo_country.getString("land1"), jo_country.getString("landx"));
                }
            }


            if (jsonObj.has("region")) {


                dataHelper.deleteRegionData();

                ja_region = jsonObj.getJSONArray("region");
                Log.d("json_region", "" + ja_region);


                for (int i = 0; i < ja_region.length(); i++) {

                    JSONObject jo_region = ja_region.getJSONObject(i);


                    dataHelper.insertRegion(jo_region.getString("land1"), jo_region.getString("bland"), jo_region.getString("regio"), jo_region.getString("bezei"));
                }
            }

            if (jsonObj.has("district")) {


                dataHelper.deleteDistrictData();

                ja_district = jsonObj.getJSONArray("district");
                Log.d("json_district", "" + ja_district);


                for (int i = 0; i < ja_district.length(); i++) {

                    JSONObject jo_district = ja_district.getJSONObject(i);


                    dataHelper.insertDistrict(jo_district.getString("land1"), jo_district.getString("regio"), jo_district.getString("cityc"), jo_district.getString("bezei"));
                }
            }


            if (jsonObj.has("tehsil")) {


                dataHelper.deleteTehsilData();

                ja_tehsil = jsonObj.getJSONArray("tehsil");
                Log.d("json_tehsil", "" + ja_tehsil);


                for (int i = 0; i < ja_tehsil.length(); i++) {

                    JSONObject jo_tehsil = ja_tehsil.getJSONObject(i);


                    dataHelper.insertTehsil(jo_tehsil.getString("land1"), jo_tehsil.getString("regio"), jo_tehsil.getString("district"), jo_tehsil.getString("tehsil"), jo_tehsil.getString("tehsil_text"));
                }
            }

            if (jsonObj.has("taxcode")) {


                dataHelper.deleteTaxcodeData();

                ja_taxcode = jsonObj.getJSONArray("taxcode");
                Log.d("json_taxcode", "" + ja_taxcode);


                for (int i = 0; i < ja_taxcode.length(); i++) {

                    JSONObject jo_taxcode = ja_taxcode.getJSONObject(i);


                    dataHelper.insertTaxcode(jo_taxcode.getString("mandt"), jo_taxcode.getString("tax_code"), jo_taxcode.getString("text"));
                }
            }

            if (jsonObj.has("exptype")) {


                dataHelper.deleteExpensesData();

                ja_expenses = jsonObj.getJSONArray("exptype");
                Log.d("json_expenses", "" + ja_expenses);


                for (int i = 0; i < ja_expenses.length(); i++) {

                    JSONObject jo_expenses = ja_expenses.getJSONObject(i);


                    dataHelper.insertExpenses(jo_expenses.getString("spkzl"), jo_expenses.getString("sptxt"));
                }
            }

            if (jsonObj.has("curr")) {


                dataHelper.deleteCurrencyData();

                ja_currency = jsonObj.getJSONArray("curr");
                Log.d("json_curr", "" + ja_currency);


                for (int i = 0; i < ja_currency.length(); i++) {

                    JSONObject jo_currency = ja_currency.getJSONObject(i);


                    dataHelper.insertCurrency(jo_currency.getString("waers"), jo_currency.getString("ltext"));
                }
            }

            progressBarStatus = 10;


        } catch (Exception e) {
            progressBarStatus = 10;
        }

        return progressBarStatus;


    }
}




