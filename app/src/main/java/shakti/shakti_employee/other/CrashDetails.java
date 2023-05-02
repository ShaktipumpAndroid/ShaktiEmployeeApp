package shakti.shakti_employee.other;

/**
 * Created by shakti on 12/19/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import shakti.shakti_employee.R;


public class CrashDetails extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                handleUncaughtException(thread, ex);

            }
        });

    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        String stackTrace = Log.getStackTraceString(e);
        String message = e.getMessage();

        Intent intent = new Intent (Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"vaibhav.patel@shaktipumps.com"});
        intent.putExtra (Intent.EXTRA_SUBJECT, R.string.crash_toast_text);
        intent.putExtra (Intent.EXTRA_TEXT, stackTrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);


    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}


