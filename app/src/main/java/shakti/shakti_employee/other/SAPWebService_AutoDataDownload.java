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
public class SAPWebService_AutoDataDownload {

    String obj_active_employee;
    String pernr, ename, btext;
    DatabaseHelper dataHelper;


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


    // For leave Balance
    String leavetype;

    String obj_leave_balance;

    /**************************** create attendance table ***********************************************/
    // Create DatabaseHelper instance
    public int getActiveEmp(Context context) {

        int progressBarStatus = 0;

        dataHelper = new DatabaseHelper(context);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);


        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        dataHelper.deleteActiveEmployee();

        try {

            /*String obj = CustomHttpClient.executeHttpPost1(SapUrl.active_employee, param);*/
            obj_active_employee = CustomHttpClient.executeHttpPost1(SapUrl.active_employee, param);

            JSONArray ja_matnr = new JSONArray(obj_active_employee);

            Log.d("json55", "" + ja_matnr);


            for (int i = 0; i < ja_matnr.length(); i++) {

                JSONObject jo_matnr = ja_matnr.getJSONObject(i);


                pernr = jo_matnr.getString("pernr");
                ename = jo_matnr.getString("ename");
                btext = jo_matnr.getString("btext");
                BeanActiveEmployee beanActiveEmployee = new BeanActiveEmployee(pernr, ename, btext);
                dataHelper.insertActiveEmployee(beanActiveEmployee);

            }
            progressBarStatus = 20;
        } catch (Exception e) {
            progressBarStatus = 20;
        }

        return progressBarStatus;
    }


    /**************************** create Leave Balance ***********************************************/
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
            progressBarStatus = 30;
        } catch (Exception e) {
            progressBarStatus = 30;
        }
        return progressBarStatus;
    }


    /**************************** create attendance table ***********************************************/

    public int getAttendanceEmp(Context context, String att_emp) {

        dataHelper = new DatabaseHelper(context);

        dataHelper.deleteattendance();

        int progressBarStatus = 0;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);


        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        param.add(new BasicNameValuePair("PERNR", att_emp));

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


                    dataHelper.insertAttendance(att_emp,
                            jo.getString("begdat"),
                            jo.getString("indz"),
                            jo.getString("iodz"),
                            jo.getString("totdz"),
                            jo.getString("atn_status"),
                            jo.getString("leave_typ"));
                }

            }

        } catch (Exception e) {

        }

        return progressBarStatus;
    }

    /**************************** Create Pending Leave for Approval table ***********************************************/

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
            progressBarStatus = 50;
        }

        return progressBarStatus;
    }


    /**************************** Create Pending Od for Approval table ***********************************************/

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


    /**************************** create Leave table     ***********************************************/

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


    /**************************** create OD table     ***********************************************/

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


    /**************************** create OD table     ***********************************************/

    public int getInfoEmp(Context context, String att_emp) {

        ArrayList<String> al;

        dataHelper = new DatabaseHelper(context);

        dataHelper.deleteempinfo();

        int progressBarStatus = 0;

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
                Log.e("","length"+ja.length());

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


            progressBarStatus = 100;
        } catch (Exception e) {
            progressBarStatus = 100;
        }

        return progressBarStatus;
    }


}




