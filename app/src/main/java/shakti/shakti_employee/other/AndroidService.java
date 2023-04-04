package shakti.shakti_employee.other;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import shakti.shakti_employee.R;
import shakti.shakti_employee.bean.EmployeeGPSActivityBean;
import shakti.shakti_employee.database.DatabaseHelper;

/**
 * Created by shakti on 12/3/2016.
 * capture lat long on every 15 min
 */


public class AndroidService extends Service {
    // constant


    public static final long NOTIFY_INTERVAL = 1000 * 60 * 10;    // 1000 = 1 second   1 min

    // public static final long NOTIFY_INTERVAL =   60 * 1 * 1000;    // 1000 = 1 second   1 min

    // public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
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

        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        //  isRunning = false;
        mTimer.cancel();
        // Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_notification)
                        .setContentTitle("Shakti Employee")
                        .setContentText("Please Turn on GPS !! ");
//

        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        builder.setVibrate(new long[]{1000, 2000});


//        builder.setSound( RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        Intent notificationIntent = new Intent(this, Notification.class);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

//        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {

                    getLatLong();

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

            if (latitude == 0.0) {
                addNotification();
            }

            return latlong;
        }

    }

}
