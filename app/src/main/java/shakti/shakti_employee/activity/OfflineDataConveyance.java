package shakti.shakti_employee.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import adapter.Adapter_offline_list;

import shakti.shakti_employee.BuildConfig;
import shakti.shakti_employee.R;
import shakti.shakti_employee.bean.LocalConvenienceBean;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.other.CustomUtility;


public class OfflineDataConveyance extends AppCompatActivity {
    Context context;

    DatabaseHelper db;

    String doc_no,version,device_name;
    private Toolbar mToolbar;
    LinearLayout lin1, lin2;
    RecyclerView recyclerView;

    Adapter_offline_list adapterEmployeeList;
    List<String> enq_docno = new ArrayList<>();
    String photo1_text,photo2_text,photo3_text,photo4_text,photo5_text,photo6_text,photo7_text,photo8_text,photo9_text,photo10_text,photo11_text,photo12_text;
    private LinearLayoutManager layoutManagerSubCategory;

    ArrayList<LocalConvenienceBean> localConvenienceBeanArrayList;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(OfflineDataConveyance.this, mString, Toast.LENGTH_LONG).show();
        }
    };

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_con);
        context = this;

        db = new DatabaseHelper(context);


        version = BuildConfig.VERSION_NAME;
        device_name = CustomUtility.getDeviceName();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.emp_list);

        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);

        localConvenienceBeanArrayList = new ArrayList<LocalConvenienceBean>();

        localConvenienceBeanArrayList = db.getLocalConveyance();


        Log.e("SIZE", "&&&&" + localConvenienceBeanArrayList.size());
        if (localConvenienceBeanArrayList != null && localConvenienceBeanArrayList.size() > 0) {


            lin1.setVisibility(View.VISIBLE);
            lin2.setVisibility(View.GONE);

            recyclerView.setAdapter(null);

            Log.e("SIZE", "&&&&" + localConvenienceBeanArrayList.size());
            adapterEmployeeList = new Adapter_offline_list(context, localConvenienceBeanArrayList);
            layoutManagerSubCategory = new LinearLayoutManager(context);
            layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManagerSubCategory);
            recyclerView.setAdapter(adapterEmployeeList);
            adapterEmployeeList.notifyDataSetChanged();


        } else {

            lin1.setVisibility(View.GONE);
            lin2.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
