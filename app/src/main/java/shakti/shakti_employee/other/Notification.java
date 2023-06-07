package shakti.shakti_employee.other;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;


import androidx.appcompat.app.AppCompatActivity;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.BaseActivity;

public class Notification extends BaseActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);


//
//     /* check background service is running or not */
//        if ( ! isServiceRunning()) {
//            // call service for capture lat long on every 15 min
//            startService(new Intent(this, TimeService.class));
//        }
//
//
//        if ( ! isNotificationServiceRunning()) {
//            // call service for gps notification
//            startService(new Intent(this, AndroidService.class));
//           }
//


        mContext = this;
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();


        if (latitude == 0.0) {
            showSettingsAlert();
        }

    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage("Your location services seems to be disabled. For better location accuracy , please enable all the options of the location services. go to settings menu. ");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                Notification.this.finish();
                finish();

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.show();
    }


    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("backgroundservice.TimeService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private boolean isNotificationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("backgroundservice.AndroidService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
