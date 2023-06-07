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


    private static CrashDetails instance;

    public static CrashDetails getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}


