package shakti.shakti_employee.services;


import static com.google.android.gms.location.LocationServices.FusedLocationApi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationRequest;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.activity.SplashActivity;
import shakti.shakti_employee.bean.LocalConvenienceBean;
import shakti.shakti_employee.bean.WayPoints;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.utility.Constant;

public class LocationUpdateService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public int TIME_INTERVAL = 120 * 1000;
    public int METER_DISTANCE = 30;

    public static Location currentLocation = null;

    private FusedLocationProviderClient mFusedLocationClient;

    DatabaseHelper databaseHelper;
    LocalConvenienceBean localConvenienceBean;

    Context mContext;
    WayPoints wayPoints;

    private LocationRequest locationRequest;
    public static boolean isServiceRunning;

    //Location Callback
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            currentLocation = locationResult.getLastLocation();

            if (CustomUtility.getSharedPreferences(mContext, Constant.FromLatitude) != null
                    && !CustomUtility.getSharedPreferences(mContext, Constant.FromLatitude).isEmpty()) {

                if (!CustomUtility.getSharedPreferences(mContext, Constant.FromLatitude).equals(String.valueOf(currentLocation.getLatitude()))) {
                    Location loc1 = new Location("PointA");
                    loc1.setLatitude(Double.parseDouble(CustomUtility.getSharedPreferences(mContext, Constant.FromLatitude)));
                    loc1.setLongitude(Double.parseDouble(CustomUtility.getSharedPreferences(mContext, Constant.FromLongitude)));

                    float distanceInMeters = loc1.distanceTo(currentLocation);
                    if (distanceInMeters >= METER_DISTANCE) {
                            wayPoints = databaseHelper.getWayPointsData(localConvenienceBean.getBegda(), localConvenienceBean.getFrom_time());

                            if (wayPoints.getWayPoints() != null && !wayPoints.getWayPoints().isEmpty()) {
                                String wayPoint = wayPoints.getWayPoints() + "|" + "via:" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                                WayPoints wayPoints1 = new WayPoints(wayPoints.getPernr(), wayPoints.getBegda(), "", wayPoints.getFrom_time(), "", wayPoint);

                                databaseHelper.updateWayPointData(wayPoints1);
                                CustomUtility.setSharedPreference(mContext, Constant.FromLatitude, String.valueOf(currentLocation.getLatitude()));
                                CustomUtility.setSharedPreference(mContext, Constant.FromLongitude, String.valueOf(currentLocation.getLongitude()));
                                CustomUtility.setSharedPreference(mContext, Constant.DistanceInMeter, String.valueOf(Math.round(distanceInMeters)));

                            }

                    }

                }
            }


        }
    };


    //endregion

    //onCreate
    @Override
    public void onCreate() {
        super.onCreate();
        isServiceRunning = true;
        initData();
        prepareForegroundNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        prepareForegroundNotification();
        startLocationUpdates();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(this.locationRequest, this.locationCallback,
                Looper.myLooper());
            databaseHelper = new DatabaseHelper(this);
            localConvenienceBean = databaseHelper.getLocalConvinienceData();
        }

        private void prepareForegroundNotification() {


            createNotificationChannel();
            final int flag = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;

            Intent notificationIntent = new Intent(this, SplashActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, flag);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(this.getResources().getString(R.string.app_name))
                    .setContentText("Location fetch running")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void initData() {
            mContext = this;
            locationRequest = LocationRequest.create();
            locationRequest.setInterval(TIME_INTERVAL);
            locationRequest.setFastestInterval(TIME_INTERVAL);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            mFusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(getApplicationContext());

        }


}


