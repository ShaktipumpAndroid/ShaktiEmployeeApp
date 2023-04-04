package shakti.shakti_employee.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import shakti.shakti_employee.bean.LoginBean;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;

/**
 * Created by shakti on 12/16/2016.
 */
public class Capture_employee_gps_location {

    DatabaseHelper dataHelper;
    private LoggedInUser userModel;
    private Context mContext;


    public Capture_employee_gps_location(Context context, String event, String phone_number) {

        GPSTracker gps = new GPSTracker(context);
        String latitude = String.valueOf(gps.getLatitude());
        String longitude = String.valueOf(gps.getLongitude());

        LoginBean lb = new LoginBean();
        String userid = lb.getUseid();

        dataHelper = new DatabaseHelper(context);


        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);


        userid = pref.getString("key_username", "username");


        mContext = context;
        userModel = new LoggedInUser(mContext);

        Log.d("gps_person_insert", "" + userModel.uid + latitude + longitude);

        dataHelper.insertEmployeeGPSActivity(
                userModel.uid,
                new CustomUtility().getCurrentDate1(),
                new CustomUtility().getCurrentTime1(),
                event,
                latitude,
                longitude,
                context,
                phone_number);

    }
}
