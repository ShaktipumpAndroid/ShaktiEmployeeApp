package shakti.shakti_employee.activity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.services.RetrieveFirestoreData;
import shakti.shakti_employee.utility.Constant;
import shakti.shakti_employee.utility.CustomUtility;


public abstract class  BaseActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(SwVersionConfigBroadcastReceiver, new IntentFilter(Constant.SwVersionConfig));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFirestoreData();
    }

    private void getFirestoreData() {
        databaseHelper = new DatabaseHelper(this);
        if (databaseHelper.getLogin() )  {

            if (!RetrieveFirestoreData.isServiceRunning) {
                Intent intent = new Intent(getApplicationContext(), RetrieveFirestoreData.class);
                startService(intent);
            }
        }

    }

    BroadcastReceiver SwVersionConfigBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
                alertDialog = null;
            }
            Log.e("broadcastReceive====>","true");
            Intent intent1 = new Intent(getApplicationContext(),SwVersionCheckActivity.class);
            startActivity(intent1);
            finish();

        }
    };
}
