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


    @Override
    public void onCreate() {
        mContex = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Worker().execute();
        stopSelf();
        return START_NOT_STICKY;
    }

    /******************** sync data in background process ********************************/
    private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            String data = null;

            try {


                if (CustomUtility.isInternetOn(getApplicationContext())) {
                    new SyncDataToSAP_New().SendAllDataToSAP(getApplicationContext());

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
        }


    }
}
