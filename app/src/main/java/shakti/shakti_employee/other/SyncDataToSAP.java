package shakti.shakti_employee.other;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import shakti.shakti_employee.BuildConfig;
import shakti.shakti_employee.bean.AttendanceBean;
import shakti.shakti_employee.bean.EmployeeGPSActivityBean;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;

/**
 * Created by shakti on 11/21/2016.
 */
public class SyncDataToSAP {

    Context context = null;
    JSONArray ja_empGPS = null;

// *********************************** sync mark attendance data  **************************************************

    public void SyncMarkAttendanceToSap(Context context) {
        this.context = context;
        DatabaseHelper db = new DatabaseHelper(this.context);
        ArrayList<AttendanceBean> param = db.getAllAttendance();

        if (param.size() > 0) {
            JSONArray ja_order = new JSONArray();

            for (int i = 0; i < param.size(); i++) {
                JSONObject jsonObj = new JSONObject();
                AttendanceBean attendanceBean = param.get(i);
                try {

                    jsonObj.put("PERNR", attendanceBean.PERNR);
                    jsonObj.put("BEGDA", attendanceBean.BEGDA);
                    //   jsonObj.put("SERVER_DATE_IN", attendanceBean.SERVER_DATE_IN);
                    // jsonObj.put("SERVER_TIME_IN", attendanceBean.SERVER_TIME_IN);
                    //  jsonObj.put("SERVER_DATE_OUT", attendanceBean.SERVER_DATE_OUT);
                    //  jsonObj.put("SERVER_TIME_OUT", attendanceBean.SERVER_TIME_OUT);
                    jsonObj.put("IN_TIME", attendanceBean.IN_TIME);
                    jsonObj.put("OUT_TIME", attendanceBean.OUT_TIME);
                    // jsonObj.put("WORKING_HOURS", attendanceBean.WORKING_HOURS);
                    jsonObj.put("IN_LAT_LONG", attendanceBean.IN_LAT_LONG);
                    jsonObj.put("OUT_LAT_LONG", attendanceBean.OUT_LAT_LONG);
                    jsonObj.put("IN_ADDRESS", attendanceBean.IN_ADDRESS);
                    jsonObj.put("OUT_ADDRESS", attendanceBean.OUT_ADDRESS);
                    jsonObj.put("IN_FILE_NAME", attendanceBean.IN_FILE_NAME);
                    jsonObj.put("OUT_FILE_NAME", attendanceBean.OUT_FILE_NAME);

                    jsonObj.put("DEVICE_NAME", CustomUtility.getDeviceName());
                    jsonObj.put("IMEI", CustomUtility.getDeviceId(this.context));
                    jsonObj.put("APP_VERSION", BuildConfig.VERSION_NAME);
                    jsonObj.put("API", Build.VERSION.SDK_INT);
                    jsonObj.put("API_VERSION", Build.VERSION.RELEASE);

                    jsonObj.put("IN_IMAGE", attendanceBean.IN_IMAGE);
                    jsonObj.put("OUT_IMAGE", attendanceBean.OUT_IMAGE);

                    if (!attendanceBean.SERVER_DATE_IN.equals("")) {
                        // save(attendanceBean.IN_FILE_NAME);
                    }

                    if (!attendanceBean.SERVER_DATE_OUT.equals("")) {
                        // save(attendanceBean.OUT_FILE_NAME);
                    }


                    ja_order.put(jsonObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            Log.d("output", String.valueOf(ja_order));


            final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();

            param1.add(new BasicNameValuePair("mark_attendance", String.valueOf(ja_order)));


            try {

                // Toast.makeText(this, String.valueOf(ja_order), Toast.LENGTH_SHORT).show();

                String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.SYNC_OFFLINE_DATA_TO_SAP, param1);
                //  Toast.makeText(this, obj2, Toast.LENGTH_LONG).show();


            } catch (Exception e) {

                // dismiss the progress dialog
                /*   progressDialog.dismiss();*/
            }
        }
    }


    /********************************* employee gps tracking *************************************/

    public void SyncGpsTrakingToSap(Context context) {
        this.context = context;
        DatabaseHelper db = new DatabaseHelper(this.context);
        ArrayList<AttendanceBean> param = db.getAllAttendance();

        ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
        employeeGPSActivityBeen = db.getEmployeeGpsActivity(this.context);


        if (employeeGPSActivityBeen.size() > 0) {
            //    Log.d("gpsdata", "" + employeeGPSActivityBeen.size());
            ja_empGPS = new JSONArray();

            for (int i = 0; i < employeeGPSActivityBeen.size(); i++) {

                JSONObject jsonObj = new JSONObject();

                try {

                    jsonObj.put("key_id", employeeGPSActivityBeen.get(i).getKey_id());
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
        }


    }


}
