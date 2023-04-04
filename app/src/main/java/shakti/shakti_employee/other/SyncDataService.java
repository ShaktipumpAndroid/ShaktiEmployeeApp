package shakti.shakti_employee.other;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;

import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;


/**
 * Created by shakti on 12/12/2016.
 */
public class SyncDataService extends Service {

    Context mContex;
    String sync_data = null;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContex, mString, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


//    @Override
//    public void onStart(Intent intent, int startId) {
//        Toast.makeText(this, " MyService Started", Toast.LENGTH_LONG).show();
//    }

    @Override
    public void onCreate() {
        mContex = getApplicationContext();

      /*  if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
        {
            startMyOwnForeground();
        }
        else
        {
            startForeground(1, new android.app.Notification());
        }*/
        // Toast.makeText(this, " MyService Created ", Toast.LENGTH_LONG).show();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //  handleStart(intent, startId);

        /// sync_data  = intent.getStringExtra("sync_data");


// add 19.01.2017
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Worker().execute();

            }
        }).start();


///***************************** comment 19.1.2017 sync data to sap ****************/
//
//        new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    new SyncDataToSAP_New().SendDataToSap(mContex);
//
//                }
//            }).start();
//
///***************************** new code for sync data to sap ****************/


        stopSelf();
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        //  Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
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

    /******************** sync data in background process ********************************/
    private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {


                if (CustomUtility.isInternetOn(mContex)) {
//                    new SyncDataToSAP().SyncMarkAttendanceToSap(mContex);
//                    new SyncDataToSAP().SyncGpsTrakingToSap(mContex);

                    new SyncDataToSAP_New().SendAllDataToSAP(getApplicationContext());

                    Message msg = new Message();
                    msg.obj = "Data Sync Successfully";
                    mHandler.sendMessage(msg);

                } else {

                    Message msg = new Message();
                    msg.obj = "No internet Connection. Data saved in offline";
                    mHandler.sendMessage(msg);
                }


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
