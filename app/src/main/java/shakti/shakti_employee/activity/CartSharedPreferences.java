package shakti.shakti_employee.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Rahul on 10/22/2016.
 */
public class CartSharedPreferences {

    public static SharedPreferences sharedPreferences;


    public static SharedPreferences createObject(Context context) {

        Log.d("output_prefrerence", String.valueOf(sharedPreferences));

        if (sharedPreferences == null) {

            sharedPreferences = context.getSharedPreferences("MyPref", 0);
        }

        return sharedPreferences;

    }


    public static SharedPreferences clearData(Context context) {

        sharedPreferences = context.getSharedPreferences("MyPref", 0);
        return sharedPreferences;

    }

}
