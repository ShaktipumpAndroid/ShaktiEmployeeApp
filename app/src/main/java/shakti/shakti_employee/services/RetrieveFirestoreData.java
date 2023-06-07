package shakti.shakti_employee.services;

import static android.app.Service.START_STICKY;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import shakti.shakti_employee.model.AppConfig;
import shakti.shakti_employee.receiver.FirestoreBroadcastReceiver;
import shakti.shakti_employee.utility.Constant;
import shakti.shakti_employee.utility.CustomUtility;

public class RetrieveFirestoreData extends Service {
    private final String TAG = "RetrieveFirestoreData";
    public static boolean isServiceRunning;
    public static DocumentReference appConfigRef;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate called");
        getFirestoreDatabaseReferance();
        isServiceRunning = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        return START_STICKY;
    }

    private void getFirestoreDatabaseReferance() {

        appConfigRef =  FirebaseFirestore.getInstance().collection("Setting").document("AppConfig");
        appConfigRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    AppConfig appConfig = snapshot.toObject(AppConfig.class);

                    if (appConfig != null) {
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            if (pInfo != null && appConfig.getMinEmployeeAppVersion() != null
                                    && !appConfig.getMinEmployeeAppVersion().toString().isEmpty()) {

                                if (pInfo.versionCode < Integer.parseInt(appConfig.getMinEmployeeAppVersion())) {
                                    CustomUtility.setSharedPreference(getApplicationContext(), Constant.APPURL, appConfig.getEmployeeAppUrl());
                                    Intent broadcastIntent = new Intent(RetrieveFirestoreData.this, FirestoreBroadcastReceiver.class);
                                    broadcastIntent.putExtra(Constant.SwVersionConfig, Constant.SwVersionConfig);
                                    sendBroadcast(broadcastIntent);

                                }
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });



    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy called");
        isServiceRunning = false;

    }

}
