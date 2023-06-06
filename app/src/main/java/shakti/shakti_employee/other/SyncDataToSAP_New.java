package shakti.shakti_employee.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import models.DistanceResponse;
import models.Element;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shakti.shakti_employee.BuildConfig;
import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.CartSharedPreferences;
import shakti.shakti_employee.bean.AttendanceBean;
import shakti.shakti_employee.bean.EmployeeGPSActivityBean;
import shakti.shakti_employee.bean.LocalConvenienceBean;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.utility.CustomHttpClient;
import shakti.shakti_employee.utility.DistanceApiClient;
import shakti.shakti_employee.utility.RestUtil;
import shakti.shakti_employee.utility.Utility;


/**
 * Created by shakti on 11/21/2016.
 */
public class SyncDataToSAP_New {

    String sync_data_name = null, sync_data_value = null;
    Context context;
    JSONArray ja_empGPS = null,  ja_attendance_data = null;

    SharedPreferences pref;

    DatabaseHelper db;

    public void SendAllDataToSAP(Context context) {

        this.context = context;

        ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
        ArrayList<AttendanceBean> attandance_data = new ArrayList<AttendanceBean>();

        db = new DatabaseHelper(this.context);

        employeeGPSActivityBeen = db.getEmployeeGpsActivity(this.context);

        pref = CartSharedPreferences.createObject(context);
        if (!pref.getString("in_time_sync", "A").equalsIgnoreCase("Y") ||
                !pref.getString("out_time_sync", "A").equalsIgnoreCase("Y")) {
            attandance_data = db.getAllAttendance();
        }


        if (employeeGPSActivityBeen.size() > 0 || attandance_data.size() > 0) {

            // GPS Data
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

            //attendance data
            ja_attendance_data = new JSONArray();
            for (int i = 0; i < attandance_data.size(); i++) {

                JSONObject jsonObj = new JSONObject();

                try {
                    jsonObj.put("PERNR", attandance_data.get(i).PERNR);
                    jsonObj.put("BEGDA", attandance_data.get(i).BEGDA);
                    jsonObj.put("IN_TIME", attandance_data.get(i).IN_TIME);
                    jsonObj.put("OUT_TIME", attandance_data.get(i).OUT_TIME);
                    jsonObj.put("IN_LAT_LONG", attandance_data.get(i).IN_LAT_LONG);
                    jsonObj.put("OUT_LAT_LONG", attandance_data.get(i).OUT_LAT_LONG);
                    jsonObj.put("IN_ADDRESS", attandance_data.get(i).IN_ADDRESS);
                    jsonObj.put("OUT_ADDRESS", attandance_data.get(i).OUT_ADDRESS);
                    jsonObj.put("IN_FILE_NAME", attandance_data.get(i).IN_FILE_NAME);
                    jsonObj.put("OUT_FILE_NAME", attandance_data.get(i).OUT_FILE_NAME);
                    jsonObj.put("DEVICE_NAME", CustomUtility.getDeviceName());
                    jsonObj.put("IMEI", "");
                    jsonObj.put("APP_VERSION", BuildConfig.VERSION_NAME);
                    jsonObj.put("API", Build.VERSION.SDK_INT);
                    jsonObj.put("API_VERSION", Build.VERSION.RELEASE);
                    jsonObj.put("IN_IMAGE", attandance_data.get(i).IN_IMAGE);
                    jsonObj.put("OUT_IMAGE", attandance_data.get(i).OUT_IMAGE);
                    ja_attendance_data.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sendDataToServer();
        }
    }

    private void sendDataToServer() {
        try {

            final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
            param1.add(new BasicNameValuePair("EMPLOYEE_GPS_ACTIVITY", String.valueOf(ja_empGPS)));

            param1.add(new BasicNameValuePair("ATTENDANCE_DATA", String.valueOf(ja_attendance_data)));


           StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);
            String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_OFFLINE_DATA_TO_SAP, param1);

            Log.e("SyncDataOffline====>", obj2.trim());
            if (!obj2.isEmpty()) {

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


                    if (sync_data_name.equalsIgnoreCase("IN_TIME_SYNC") &&
                            sync_data_value.equalsIgnoreCase("Y")) {

                        pref = CartSharedPreferences.createObject(context);

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("in_time_sync", "Y");
                        editor.apply();
                    }

                    if (sync_data_name.equalsIgnoreCase("OUT_TIME_SYNC") &&
                            sync_data_value.equalsIgnoreCase("Y")) {

                        pref = CartSharedPreferences.createObject(context);

                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("out_time_sync", "Y");
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


