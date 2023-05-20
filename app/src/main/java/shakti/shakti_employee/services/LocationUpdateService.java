package shakti.shakti_employee.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.bean.LocalConvenienceBean;
import shakti.shakti_employee.bean.WayPoints;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.utility.Constant;

public class LocationUpdateService extends Service {
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;

    public static Location currentLocation = null;

    private FusedLocationProviderClient mFusedLocationClient;

    DatabaseHelper databaseHelper;
    LocalConvenienceBean localConvenienceBean;

    Context mContext;
    WayPoints wayPoints;

    //Location Callback
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            currentLocation = locationResult.getLastLocation();
            Log.e("Locations ", currentLocation.getLatitude() + "," + currentLocation.getLongitude());


            if (CustomUtility.getSharedPreferences(mContext, Constant.FromLatitude) != null
                    && !CustomUtility.getSharedPreferences(mContext, Constant.FromLatitude).isEmpty()
                    && CustomUtility.getSharedPreferences(mContext, Constant.FromLongitude) != null
                    && !CustomUtility.getSharedPreferences(mContext, Constant.FromLongitude).isEmpty()) {
                Log.e("FromLatitude2 ", CustomUtility.getSharedPreferences(mContext, Constant.FromLatitude));
                Log.e("FromLongitude2 ", CustomUtility.getSharedPreferences(mContext, Constant.FromLongitude));

                if (!CustomUtility.getSharedPreferences(mContext, Constant.FromLatitude).equals(String.valueOf(currentLocation.getLatitude()))) {
                    Location loc1 = new Location("PointA");
                    loc1.setLatitude(Double.parseDouble(CustomUtility.getSharedPreferences(mContext, Constant.FromLatitude)));
                    loc1.setLongitude(Double.parseDouble(CustomUtility.getSharedPreferences(mContext, Constant.FromLongitude)));

                    float distanceInMeters = loc1.distanceTo(currentLocation);
                    if (distanceInMeters > 30) {
                        Log.e("distanceInMeters======>", String.valueOf(Math.round(distanceInMeters)));
                        if (CustomUtility.getSharedPreferences(mContext, Constant.DistanceInMeter) != null
                                && !CustomUtility.getSharedPreferences(mContext, Constant.DistanceInMeter).isEmpty()
                        &&!CustomUtility.getSharedPreferences(mContext, Constant.DistanceInMeter).equals(String.valueOf(Math.round(distanceInMeters)))) {
                            wayPoints = databaseHelper.getWayPointsData(localConvenienceBean.getBegda(), localConvenienceBean.getFrom_time());

                            if (wayPoints.getWayPoints() != null && !wayPoints.getWayPoints().isEmpty()) {
                                String wayPoint = wayPoints.getWayPoints() + "|" + "via:" + currentLocation.getLatitude() + "," + currentLocation.getLongitude();
                                WayPoints wayPoints1 = new WayPoints(wayPoints.getPernr(), wayPoints.getBegda(), "", wayPoints.getFrom_time(), "", wayPoint);

                                databaseHelper.updateWayPointData(wayPoints1);
                                Log.e("databaseWayPoints2====>", databaseHelper.getWayPointsData(localConvenienceBean.getBegda(), localConvenienceBean.getFrom_time()).getWayPoints());
                                CustomUtility.setSharedPreference(mContext, Constant.FromLatitude, String.valueOf(currentLocation.getLatitude()));
                                CustomUtility.setSharedPreference(mContext, Constant.FromLongitude, String.valueOf(currentLocation.getLongitude()));
                                CustomUtility.setSharedPreference(mContext, Constant.DistanceInMeter, String.valueOf(Math.round(distanceInMeters)));

                            }
                        }
                    }

                }
            }


        }
    };

    private LocationRequest locationRequest;

    //endregion

    //onCreate
    @Override
    public void onCreate() {
        super.onCreate();
        initData();
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
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(this.locationRequest, this.locationCallback,
                Looper.myLooper());
        databaseHelper = new DatabaseHelper(this);
        localConvenienceBean = databaseHelper.getLocalConvinienceData();
    }

    private void prepareForegroundNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel =
                    new NotificationChannel("Location-Notification", "Location Service Channel",
                            NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
        Intent notificationIntent = new Intent(this, DashboardActivity.class);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 1234, notificationIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 1234, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Notification notification =
                new NotificationCompat.Builder(this, "Location-Notification").setContentTitle(
                        getString(R.string.app_name)).setContentTitle(
                        getString(R.string.location_notification_desc)).setSmallIcon(
                        R.mipmap.ic_launcher).setContentIntent(pendingIntent).build();
        startForeground(111, notification);
    }

    private void initData() {
        mContext = this;
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setSmallestDisplacement(50);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(getApplicationContext());
    }


}
