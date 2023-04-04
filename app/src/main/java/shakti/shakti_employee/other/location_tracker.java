package shakti.shakti_employee.other;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import shakti.shakti_employee.bean.EmployeeGPSActivityBean;
import shakti.shakti_employee.database.DatabaseHelper;

/**
 * Created by shakti on 3/4/2017.
 */
public class location_tracker extends Service {
    // constant


    public static final long NOTIFY_INTERVAL = 1000 * 60 * 1;    // 1000 = 1 second   15 min
    ArrayList<EmployeeGPSActivityBean> employeeGPSActivityBeen = null;
    DatabaseHelper db = null;
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            // Toast.makeText(this, "already running", Toast.LENGTH_LONG).show();

            mTimer.cancel();
        } else {
            // recreate new
            //  Toast.makeText(this, "create new command", Toast.LENGTH_LONG).show();
            mTimer = new Timer();

            employeeGPSActivityBeen = new ArrayList<EmployeeGPSActivityBean>();
            db = new DatabaseHelper(getApplicationContext());


        }
        // schedule task


        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
//            }
//        }).start();


        // stopSelf();
        // return START_NOT_STICKY;
        //Toast.makeText(this, "start command", Toast.LENGTH_LONG).show();

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

                    getLatLong();

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


                // sync employee gps data to sap every 15 min
//               if (CustomUtility.isInternetOn(c)) {
                    new SyncDataToSAP_New().SendEmployeeGPS(getApplicationContext());
//                }


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
