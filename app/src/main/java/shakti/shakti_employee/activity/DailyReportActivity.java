package shakti.shakti_employee.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import shakti.shakti_employee.R;


public class DailyReportActivity extends AppCompatActivity  {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

            Init();
            listner();
    }



    private void Init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("Daily Report");

    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

    }
}

