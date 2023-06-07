package shakti.shakti_employee.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import shakti.shakti_employee.utility.Constant;

public class FirestoreBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = "MyReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive called");

        if (intent.getStringExtra(Constant.SwVersionConfig)!=null && !intent.getStringExtra(Constant.SwVersionConfig).isEmpty()) {
            Intent i = new Intent(Constant.SwVersionConfig);
            i.putExtra(Constant.SwVersionConfig, Constant.SwVersionConfig);
            context.sendBroadcast(i);
        }
    }
}
