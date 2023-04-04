package shakti.shakti_employee.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import shakti.shakti_employee.R;


public class BaseActivity extends Application {

    private static BaseActivity instance;

    public static BaseActivity getInstance() {
        return instance;
    }

    public static void setInstance(BaseActivity instance) {
        BaseActivity.instance = instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        setInstance(this);
        // The following line triggers the initialization of ACRA
       // ACRA.init(this);

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
        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"vikas.gothi@shaktipumps.com"});
        intent.putExtra (Intent.EXTRA_SUBJECT, R.string.crash_toast_text);
        intent.putExtra (Intent.EXTRA_TEXT, stackTrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);


    }


}
