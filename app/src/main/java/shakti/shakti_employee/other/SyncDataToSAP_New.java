package shakti.shakti_employee.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import shakti.shakti_employee.BuildConfig;
import shakti.shakti_employee.activity.CartSharedPreferences;
import shakti.shakti_employee.bean.AttendanceBean;
import shakti.shakti_employee.bean.EmployeeGPSActivityBean;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;

//import shakti.shaktiemployee.BuildConfig;
//import backgroundservice.TimeService;
//import webservice.CustomHttpClient;

/**
 * Created by shakti on 11/21/2016.
 */
public class SyncDataToSAP_New {


    String sync_data_name = null;
    String sync_data_value = null;


    Context context = null;
    JSONArray ja_empGPS = null;
    JSONArray ja_task = null;
    JSONArray ja_task_completed = null;
    JSONArray ja_attendance_data = null;


    JSONArray ja_image = null;

    SharedPreferences pref;


//    public void SendDataToSap(Context context) {
//        this.context = context;
//
//        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
//        DatabaseHelper db = new DatabaseHelper(this.context);
//
//
//
//        SharedPreferences.Editor editor ;
//        SharedPreferences pref ;
//        pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
//        editor = pref.edit();
//
//
//
//        /********************************* create json ******************************************/
///********************************* employee gps tracking *************************************/
//        ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
//
//        employeeGPSActivityBeen = db.getEmployeeGpsActivity(this.context);
//
//
//        if (employeeGPSActivityBeen.size() > 0) {
//        //    Log.d("gpsdata", "" + employeeGPSActivityBeen.size());
//            ja_empGPS = new JSONArray();
//
//            for (int i = 0; i < employeeGPSActivityBeen.size(); i++) {
//
//                JSONObject jsonObj = new JSONObject();
//
//                try {
//
//                    jsonObj.put("pernr", employeeGPSActivityBeen.get(i).getPernr());
//                    jsonObj.put("budat", employeeGPSActivityBeen.get(i).getBudat());
//                    jsonObj.put("time", employeeGPSActivityBeen.get(i).getTime());
//                    jsonObj.put("event", employeeGPSActivityBeen.get(i).getEvent());
//                    jsonObj.put("latitude", employeeGPSActivityBeen.get(i).getLatitude());
//                    jsonObj.put("longitude", employeeGPSActivityBeen.get(i).getLongitude());
//                    jsonObj.put("phone_number", employeeGPSActivityBeen.get(i).getPhone_number());
//
//                    ja_empGPS.put(jsonObj);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//
///*********************************************************************************************/
///*                      start   send data to server
///********************************************************************************************/
//
//        param1.add(new BasicNameValuePair("EMPLOYEE_GPS_ACTIVITY", String.valueOf(ja_empGPS)));
//
//
//       // Log.d("logout",pref.getString("key_logout", "logout_error")+"--"+pref.getString("key_username", "user")) ;
//
//        if (pref.getString("key_logout", "logout_error").equalsIgnoreCase("logout")) {
//            param1.add(new BasicNameValuePair(pref.getString("key_logout", "logout_error"), pref.getString("key_username", "user")));
//        }
//
//
//        try {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
//            StrictMode.setThreadPolicy(policy);
//
//
//            String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_OFFLINE_DATA_TO_SAP, param1);
//
//         //   Log.d("output_obj2", obj2);
//
//            if (obj2 != "") {
//
//                JSONObject jo_success = new JSONObject(obj2);
//                JSONArray ja_success = jo_success.getJSONArray("data_success");
//
//                for (int i = 0; i < ja_success.length(); i++) {
//
//
//                    JSONObject jo = ja_success.getJSONObject(i);
//
//                    sync_data_name = jo.getString("sync_data");
//                    sync_data_value = jo.getString("value");
//
//
//                    Log.d("success", "" + sync_data_name + "---" + sync_data_value);
//
//
//                    if (sync_data_name.equalsIgnoreCase("EMP_GPS") &&
//                            sync_data_value.equalsIgnoreCase("Y")) {
//                        db.deleteEmployeeGPSActivity();
//                    }
//
//
//                    if( sync_data_name.equalsIgnoreCase("PLANS") &&
//                            sync_data_value.equalsIgnoreCase("99999999") )
//                    {
//                        //Intent intent = new Intent(context, LoginActivity.class) ;
//
////                        SharedPreferences.Editor editor ;
////                        SharedPreferences pref ;
////                        pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
////                        editor = pref.edit();
//
//                        // stop back groud services
////                        context.stopService(new Intent(context.getApplicationContext(), TimeService.class));
////
////
////                        new DatabaseHelper(context).deleteLogin();
////                        LoginBean.setLogin("","");
////
////                        editor.remove("key_sync_date");
////                        editor.remove("key_login");
////                        editor.remove("key_username");
////                        editor.remove("key_ename");
////                        editor.remove("key_logout");
////
////                        editor.commit(); // commit changes
////
////
////                        System.exit(0);
//                    }
//
//
//
//                    if( sync_data_name.equalsIgnoreCase("LOGOUT") &&
//                            sync_data_value.equalsIgnoreCase("Y") )
//                    {
//
////                        context.stopService(new Intent(context.getApplicationContext(), TimeService.class));
////
////                        new DatabaseHelper(context).deleteLogin();
////                        LoginBean.setLogin("","");
////                        editor.remove("key_sync_date");
////                        editor.remove("key_login");
////                        editor.remove("key_username");
////                        editor.remove("key_logout");
////                        editor.remove("key_ename");
////
////                        editor.commit(); // commit changes
////
////                        System.exit(0);
//                    }
//
//
//
//
//
//                }
//
//
//            }
//
//////                for (int i = 0; i < param.size(); i++) {
//////                    db.deleteCounterSyncOrder(new DatabaseBean(param.get(i).getTelnr(), param.get(i).getDocno()));
//////
//////            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
///*********************************************************************************************/
///*                      end  send data to server
///*********************************************************************************************/
//
//
//    }

    public void SendAllDataToSAP(Context context) {

        this.context = context;
//        DatabaseHelper db = new DatabaseHelper(this.context);
        /********************************* employee gps tracking *************************************/
        ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
        DatabaseHelper db = new DatabaseHelper(this.context);
        employeeGPSActivityBeen = db.getEmployeeGpsActivity(this.context);
//
//
//        /********************************* Task Created Data *************************************/
//        ArrayList<TaskCreated> task_created = new ArrayList<TaskCreated>();
////        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
////        DatabaseHelper db = new DatabaseHelper(this.context);
//        task_created = db.getTaskCreated();
//
//
//        /********************************* Task Completed Data *************************************/
//        ArrayList<TaskPending> task_completed = new ArrayList<TaskPending>();
////        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
////        DatabaseHelper db = new DatabaseHelper(this.context);
//        task_completed = db.getCompletedTask();

        /********************************* Employee Attandance & GPS *************************************/
        ArrayList<AttendanceBean> attandance_data = new ArrayList<AttendanceBean>();
//        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
//        DatabaseHelper db = new DatabaseHelper(this.context);
        pref = CartSharedPreferences.createObject(context);
        if (!pref.getString("in_time_sync", "A").equalsIgnoreCase("Y") ||
                !pref.getString("out_time_sync", "A").equalsIgnoreCase("Y")) {
            attandance_data = db.getAllAttendance();
        }
//        }


//        new SyncDataToSAP().SyncMarkAttendanceToSap(mContex);
//        new SyncDataToSAP().SyncGpsTrakingToSap(mContex);

//        if (employeeGPSActivityBeen.size() > 0 || task_created.size() > 0 || task_completed.size() > 0 ||
        if (employeeGPSActivityBeen.size() > 0 || attandance_data.size() > 0) {

            // GPS Data
            Log.d("gpsdata", "" + employeeGPSActivityBeen.size());
            ja_empGPS = new JSONArray();

            for (int i = 0; i < employeeGPSActivityBeen.size(); i++) {

                JSONObject jsonObj = new JSONObject();

                try {

                    jsonObj.put("pernr", employeeGPSActivityBeen.get(i).getPernr());
                    jsonObj.put("budat", employeeGPSActivityBeen.get(i).getBudat());
                    jsonObj.put("time", employeeGPSActivityBeen.get(i).getTime());
                    jsonObj.put("event", employeeGPSActivityBeen.get(i).getEvent());
                    jsonObj.put("latitude", employeeGPSActivityBeen.get(i).getLatitude());
                    jsonObj.put("longitude", employeeGPSActivityBeen.get(i).getLongitude());
                    jsonObj.put("phone_number", employeeGPSActivityBeen.get(i).getPhone_number());

                    ja_empGPS.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


//            // Task Created Data
//            Log.d("taskdata", ""+task_created.size());
//            ja_task = new JSONArray();
//
//            for (int i = 0; i < task_created.size(); i++) {
//
//                JSONObject jsonObj = new JSONObject();
//
//                try {
//
//                    jsonObj.put("pernr", task_created.get(i).getPernr());
//                    jsonObj.put("budat", task_created.get(i).getCurrentDate());
//                    jsonObj.put("time", task_created.get(i).getCurrentTime());
//                    jsonObj.put("description", task_created.get(i).getDescription());
//                    jsonObj.put("assign_to", task_created.get(i).getTask_assign_to());
//                    jsonObj.put("date_from", task_created.get(i).getFromDateEtxt());
//                    jsonObj.put("date_to", task_created.get(i).getToDateEtxt());
//                    jsonObj.put("mrc_type", task_created.get(i).getMrc_type());
//                    jsonObj.put("department", task_created.get(i).getDepartment());
//
//                    ja_task.put(jsonObj);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//
//            // Task Completed Data
//            Log.d("taskdatacomplete", ""+task_completed.size());
//            ja_task_completed = new JSONArray();
//
//            for (int i = 0; i < task_completed.size(); i++) {
//
//                JSONObject jsonObj = new JSONObject();
//
//                try {
//
////                    jsonObj.put("pernr", task_completed.get(i).getPernr());
////                    jsonObj.put("budat", task_created.get(i).getCurrentDate());
////                    jsonObj.put("time", task_created.get(i).getCurrentTime());
////                    jsonObj.put("description", task_created.get(i).getDescription());
////                    jsonObj.put("assign_to", task_created.get(i).getTask_assign_to());
////                    jsonObj.put("date_from", task_created.get(i).getFromDateEtxt());
////                    jsonObj.put("date_to", task_created.get(i).getToDateEtxt());
////                    jsonObj.put("mrc_type", task_created.get(i).getMrc_type());
//                    jsonObj.put("dno", task_completed.get(i).getDno());
//                    jsonObj.put("srno", task_completed.get(i).getSrno());
//                    jsonObj.put("checker", task_completed.get(i).getChecker());
//                    jsonObj.put("remark", task_completed.get(i).getRemark());
//
//
//
//                    ja_task_completed.put(jsonObj);
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }

            // Attendance Data
            Log.d("attandancedata", "" + attandance_data.size());
            ja_attendance_data = new JSONArray();

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);


            for (int i = 0; i < attandance_data.size(); i++) {

                JSONObject jsonObj = new JSONObject();

                try {


//                Log.d("output_test",attandance_data.get(i).PERNR);
                    jsonObj.put("PERNR", attandance_data.get(i).PERNR);
//                Log.d("output_test1",attandance_data.get(i).BEGDA);
                    jsonObj.put("BEGDA", attandance_data.get(i).BEGDA);
                    //   jsonObj.put("SERVER_DATE_IN", attendanceBean.SERVER_DATE_IN);
                    // jsonObj.put("SERVER_TIME_IN", attendanceBean.SERVER_TIME_IN);
                    //  jsonObj.put("SERVER_DATE_OUT", attendanceBean.SERVER_DATE_OUT);
                    //  jsonObj.put("SERVER_TIME_OUT", attendanceBean.SERVER_TIME_OUT);
                    jsonObj.put("IN_TIME", attandance_data.get(i).IN_TIME);
//                Log.d("output_test2",attandance_data.get(i).IN_TIME);
                    jsonObj.put("OUT_TIME", attandance_data.get(i).OUT_TIME);
//                Log.d("output_test3",attandance_data.get(i).OUT_TIME);
                    // jsonObj.put("WORKING_HOURS", attendanceBean.WORKING_HOURS);
                    jsonObj.put("IN_LAT_LONG", attandance_data.get(i).IN_LAT_LONG);
//                Log.d("output_test4",attandance_data.get(i).IN_LAT_LONG);
                    jsonObj.put("OUT_LAT_LONG", attandance_data.get(i).OUT_LAT_LONG);
//                Log.d("output_test5",attandance_data.get(i).OUT_LAT_LONG);
                    jsonObj.put("IN_ADDRESS", attandance_data.get(i).IN_ADDRESS);
//                Log.d("output_test6",attandance_data.get(i).IN_ADDRESS);
                    jsonObj.put("OUT_ADDRESS", attandance_data.get(i).OUT_ADDRESS);
//                Log.d("output_test7",attandance_data.get(i).OUT_ADDRESS);
                    jsonObj.put("IN_FILE_NAME", attandance_data.get(i).IN_FILE_NAME);
//                Log.d("output_test8",attandance_data.get(i).IN_FILE_NAME);
                    jsonObj.put("OUT_FILE_NAME", attandance_data.get(i).OUT_FILE_NAME);
//                Log.d("output_test9",attandance_data.get(i).OUT_FILE_NAME);
                    jsonObj.put("DEVICE_NAME", CustomUtility.getDeviceName());
//                Log.d("output_test10",CustomUtility.getDeviceName());
                    jsonObj.put("IMEI", "");
//                jsonObj.put("IMEI"       , telephonyManager.getDeviceId());
//                jsonObj.put("IMEI"       , CustomUtility.getDeviceId(this.context));
//                Log.d("output_test11",CustomUtility.getDeviceId(this.context));
                    jsonObj.put("APP_VERSION", BuildConfig.VERSION_NAME);
//                Log.d("output_test12",BuildConfig.VERSION_NAME);
                    jsonObj.put("API", Build.VERSION.SDK_INT);
//                Log.d("output_test13", String.valueOf(Build.VERSION.SDK_INT));
                    jsonObj.put("API_VERSION", Build.VERSION.RELEASE);
//                Log.d("output_test14", Build.VERSION.RELEASE);
                    jsonObj.put("IN_IMAGE", attandance_data.get(i).IN_IMAGE);
//                Log.d("output_test15", attandance_data.get(i).IN_IMAGE);
//                if (!attandance_data.get(i).OUT_IMAGE.equalsIgnoreCase("")) {
                    jsonObj.put("OUT_IMAGE", attandance_data.get(i).OUT_IMAGE);
//                    Log.d("output_test16", attandance_data.get(i).OUT_IMAGE);
//                }
//                if (!attendanceBean.SERVER_DATE_IN.equals("")) {
//                    // save(attendanceBean.IN_FILE_NAME);
//                }
//
//                if (!attendanceBean.SERVER_DATE_OUT.equals("")) {
//                    // save(attendanceBean.OUT_FILE_NAME);
//                }

//                Log.d("output_test1111", "test");
                    ja_attendance_data.put(jsonObj);
//                Log.d("output_test1", String.valueOf(ja_attendance_data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            param1.add(new BasicNameValuePair("EMPLOYEE_GPS_ACTIVITY", String.valueOf(ja_empGPS)));
//            param1.add(new BasicNameValuePair("TASK_CREATED", String.valueOf(ja_task)));
//            param1.add(new BasicNameValuePair("TASK_COMPLETED", String.valueOf(ja_task_completed)));
            param1.add(new BasicNameValuePair("ATTENDANCE_DATA", String.valueOf(ja_attendance_data)));

//            Log.d("output_obj1", "hello test");
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

//                Log.d("output_obj11", "hello test 2");
                String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_OFFLINE_DATA_TO_SAP, param1);

//                  Log.d("output_obj2", obj2);

                if (obj2 != "") {

                    JSONObject jo_success = new JSONObject(obj2);
                    JSONArray ja_success = jo_success.getJSONArray("data_success");

                    for (int i = 0; i < ja_success.length(); i++) {


                        JSONObject jo = ja_success.getJSONObject(i);

                        sync_data_name = jo.getString("sync_data");
                        sync_data_value = jo.getString("value");


                        Log.d("success", "" + sync_data_name + "---" + sync_data_value);


                        if (sync_data_name.equalsIgnoreCase("EMP_GPS") &&
                                sync_data_value.equalsIgnoreCase("Y")) {
                            db.deleteEmployeeGPSActivity();
                        }

//
//                        if (sync_data_name.equalsIgnoreCase("EMP_TASK") &&
//                                sync_data_value.equalsIgnoreCase("Y")) {
//                            db.updateTaskCreated();
//                            //db.deleteEmployeeGPSActivity();
//                        }
//
//                        if (sync_data_name.equalsIgnoreCase("EMP_TASK_COMPLETE") &&
//                                sync_data_value.equalsIgnoreCase("Y")) {
//                            db.updateTaskCompleted();
//                            //db.deleteEmployeeGPSActivity();
//                        }

//                        if (sync_data_name.equalsIgnoreCase("MARK_ATTENDANCE") &&
//                                sync_data_value.equalsIgnoreCase("Y")) {
//
////                            db.updateUnsyncData(DatabaseHelper.TABLE_MARK_ATTENDANCE,sync_key_id);
//                            //db.deleteEmployeeGPSActivity();
//                        }

                        if (sync_data_name.equalsIgnoreCase("IN_TIME_SYNC") &&
                                sync_data_value.equalsIgnoreCase("Y")) {

                            pref = CartSharedPreferences.createObject(context);

                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("in_time_sync", "Y");
//                            editor.commit();
                            editor.apply();
                        }

                        if (sync_data_name.equalsIgnoreCase("OUT_TIME_SYNC") &&
                                sync_data_value.equalsIgnoreCase("Y")) {

                            pref = CartSharedPreferences.createObject(context);

                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("out_time_sync", "Y");
//                            editor.commit();
                            editor.apply();
                        }


                        if (sync_data_name.equalsIgnoreCase("PLANS") &&
                                sync_data_value.equalsIgnoreCase("99999999")) {

                        }


                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void SendEmployeeGPS(Context context) {
//        this.context = context;
//
//        /********************************* employee gps tracking *************************************/
//        ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
//        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
//        DatabaseHelper db = new DatabaseHelper(this.context);
//        employeeGPSActivityBeen = db.getEmployeeGpsActivity(this.context);
//
//
//        /********************************* Task Created Data *************************************/
//        ArrayList<TaskCreated> task_created = new ArrayList<TaskCreated>();
////        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
////        DatabaseHelper db = new DatabaseHelper(this.context);
//        task_created = db.getTaskCreated();
//
////        Log.d("task_size",""+task_created.size());
//
//
//        if (employeeGPSActivityBeen.size() > 0 || task_created.size() > 0) {
//
//           // GPS Data
//           Log.d("gpsdata", ""+employeeGPSActivityBeen.size());
//            ja_empGPS = new JSONArray();
//
//            for (int i = 0; i < employeeGPSActivityBeen.size(); i++) {
//
//                JSONObject jsonObj = new JSONObject();
//
//                try {
//
//                    jsonObj.put("pernr", employeeGPSActivityBeen.get(i).getPernr());
//                    jsonObj.put("budat", employeeGPSActivityBeen.get(i).getBudat());
//                    jsonObj.put("time", employeeGPSActivityBeen.get(i).getTime());
//                    jsonObj.put("event", employeeGPSActivityBeen.get(i).getEvent());
//                    jsonObj.put("latitude", employeeGPSActivityBeen.get(i).getLatitude());
//                    jsonObj.put("longitude", employeeGPSActivityBeen.get(i).getLongitude());
//                    jsonObj.put("phone_number", employeeGPSActivityBeen.get(i).getPhone_number());
//
//                    ja_empGPS.put(jsonObj);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//            // Task Created Data
//            Log.d("taskdata", ""+task_created.size());
//            ja_task = new JSONArray();
//
//            for (int i = 0; i < task_created.size(); i++) {
//
//                JSONObject jsonObj = new JSONObject();
//
//                try {
//
//                    jsonObj.put("pernr", task_created.get(i).getPernr());
//                    jsonObj.put("budat", task_created.get(i).getCurrentDate());
//                    jsonObj.put("time", task_created.get(i).getCurrentTime());
//                    jsonObj.put("description", task_created.get(i).getDescription());
//                    jsonObj.put("assign_to", task_created.get(i).getTask_assign_to());
//                    jsonObj.put("date_from", task_created.get(i).getFromDateEtxt());
//                    jsonObj.put("date_to", task_created.get(i).getToDateEtxt());
//                    jsonObj.put("mrc_type", task_created.get(i).getMrc_type());
//                    jsonObj.put("department", task_created.get(i).getDepartment());
//
//                    ja_task.put(jsonObj);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//
//
//            param1.add(new BasicNameValuePair("EMPLOYEE_GPS_ACTIVITY", String.valueOf(ja_empGPS)));
//            param1.add(new BasicNameValuePair("TASK_CREATED", String.valueOf(ja_task)));
//
//
//            try {
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
//                StrictMode.setThreadPolicy(policy);
//
//
//                String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_OFFLINE_DATA_TO_SAP, param1);
//
//              //  Log.d("output_obj2", obj2);
//
//                if (obj2 != "") {
//
//                    JSONObject jo_success = new JSONObject(obj2);
//                    JSONArray ja_success = jo_success.getJSONArray("data_success");
//
//                    for (int i = 0; i < ja_success.length(); i++) {
//
//
//                        JSONObject jo = ja_success.getJSONObject(i);
//
//                        sync_data_name = jo.getString("sync_data");
//                        sync_data_value = jo.getString("value");
//
//
//                        Log.d("success", "" + sync_data_name + "---" + sync_data_value);
//
//
//
//                        if (sync_data_name.equalsIgnoreCase("EMP_GPS") &&
//                                sync_data_value.equalsIgnoreCase("Y")) {
//                            db.deleteEmployeeGPSActivity();
//                        }
//
//
//                        if (sync_data_name.equalsIgnoreCase("EMP_TASK") &&
//                                sync_data_value.equalsIgnoreCase("Y")) {
//                            db.updateTaskCreated();
//                            //db.deleteEmployeeGPSActivity();
//                        }
//
//                        if( sync_data_name.equalsIgnoreCase("PLANS") &&
//                                sync_data_value.equalsIgnoreCase("99999999") )
//                        {
//
//                        }
//
//
//                    }
//
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }


    }


    public void SendTaskCreated(Context context) {
//        this.context = context;
//
//        /********************************* Task Created Data *************************************/
//        ArrayList<TaskCreated> task_created = new ArrayList<TaskCreated>();
//        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
//        DatabaseHelper db = new DatabaseHelper(this.context);
//        task_created = db.getTaskCreated();
//
//        Log.d("task_size",""+task_created.size());
//
//        if (task_created.size() > 0) {
//            Log.d("taskdata", ""+task_created.size());
//            ja_task = new JSONArray();
//
//            for (int i = 0; i < task_created.size(); i++) {
//
//                JSONObject jsonObj = new JSONObject();
//
//                try {
//
//                    jsonObj.put("pernr", task_created.get(i).getPernr());
//                    jsonObj.put("budat", task_created.get(i).getCurrentDate());
//                    jsonObj.put("time", task_created.get(i).getCurrentTime());
//                    jsonObj.put("description", task_created.get(i).getDescription());
//                    jsonObj.put("assign_to", task_created.get(i).getTask_assign_to());
//                    jsonObj.put("date_from", task_created.get(i).getFromDateEtxt());
//                    jsonObj.put("date_to", task_created.get(i).getToDateEtxt());
//                    jsonObj.put("mrc_type", task_created.get(i).getMrc_type());
//                    jsonObj.put("department", task_created.get(i).getDepartment());
//
//                    ja_task.put(jsonObj);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//
//
//            param1.add(new BasicNameValuePair("TASK_CREATED", String.valueOf(ja_task)));
//
//
//            try {
//                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
//                StrictMode.setThreadPolicy(policy);
//
//
//                String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_OFFLINE_DATA_TO_SAP, param1);
//
//                //  Log.d("output_obj2", obj2);
//
//                if (obj2 != "") {
//
//                    JSONObject jo_success = new JSONObject(obj2);
//                    JSONArray ja_success = jo_success.getJSONArray("data_success");
//
//                    for (int i = 0; i < ja_success.length(); i++) {
//
//
//                        JSONObject jo = ja_success.getJSONObject(i);
//
//                        sync_data_name = jo.getString("sync_data");
//                        sync_data_value = jo.getString("value");
//
//
//                        Log.d("success", "" + sync_data_name + "---" + sync_data_value);
//
//
//
//                        if (sync_data_name.equalsIgnoreCase("EMP_TASK") &&
//                                sync_data_value.equalsIgnoreCase("Y")) {
//                                db.updateTaskCreated();
//                                //db.deleteEmployeeGPSActivity();
//                        }
//
//
//
//                    }
//
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }


    }


}


