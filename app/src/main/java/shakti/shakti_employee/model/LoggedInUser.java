package shakti.shakti_employee.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by shakti on 11/15/2016.
 */
public class LoggedInUser {
    private static final String PREF_NAME = "UserPref";
    public String uid;
    public String ename;
    public String password;
    public String mob_atnd;
    public String travel;
    public String hod;

    public LoggedInUser() {

    }

    public LoggedInUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.uid = sharedPreferences.getString("uid", "");
        this.ename = sharedPreferences.getString("ename", "");
        this.password = sharedPreferences.getString("password", "");
        this.mob_atnd = sharedPreferences.getString("mob_atnd", "");
        this.travel = sharedPreferences.getString("travel", "");
        this.hod = sharedPreferences.getString("hod", "");

    }


    public void persist(Context context, String uid, String password, String ename, String mob_atnd, String travel, String hod) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString("uid", uid);
        prefsEditor.putString("password", password);
        prefsEditor.putString("ename", ename);
        prefsEditor.putString("mob_atnd", mob_atnd);
        prefsEditor.putString("travel", travel);
        prefsEditor.putString("hod", hod);

        prefsEditor.commit();
    }
}
