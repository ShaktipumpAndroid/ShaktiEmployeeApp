package shakti.shakti_employee.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;
import android.widget.TextView;

import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;

public class PersonalInfoActivity extends AppCompatActivity {

    DatabaseHelper dataHelper = null;
    TextView pi_dept_val,pi_sap_val,pi_name_val, pi_desig_val, pi_dob_val, pi_add_val, pi_mob_val, pi_email_val, pi_hod_val;
    private Toolbar toolbar;
    private LoggedInUser userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        userModel = new LoggedInUser(PersonalInfoActivity.this);

        pi_sap_val = findViewById(R.id.pi_sap_val);
        pi_name_val = findViewById(R.id.pi_name_val);
        pi_dept_val =  findViewById(R.id.pi_dept_val);
        pi_desig_val =  findViewById(R.id.pi_desig_val);
        pi_mob_val =  findViewById(R.id.pi_mob_val);
        pi_email_val =  findViewById(R.id.pi_email_val);
        pi_hod_val =  findViewById(R.id.pi_hod_val);
        pi_add_val =  findViewById(R.id.pi_add_val);
        pi_dob_val =  findViewById(R.id.pi_dob_val);

        pi_sap_val.setText(userModel.uid);
        pi_name_val.setText(userModel.ename);


        get_personal_info();
    }

    private void get_personal_info() {

        dataHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dataHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_EMPLOYEE_INFO;

        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.d("pers_info", "" + cursor.getCount());

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                // Read columns data
                String dept = cursor.getString(2);
                pi_dept_val.setText(dept);

                String desig = cursor.getString(3);
                pi_desig_val.setText(desig);

                String mob = cursor.getString(4);
                pi_mob_val.setText(mob);

                String email = cursor.getString(5);
                pi_email_val.setText(email);

                String hod = cursor.getString(6);
                pi_hod_val.setText(hod);

                String add = cursor.getString(7);
                pi_add_val.setText(add);

                String dob = cursor.getString(8);
                pi_dob_val.setText(dob);




            }
        }
    }
}