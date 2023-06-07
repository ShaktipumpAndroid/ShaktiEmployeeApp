package shakti.shakti_employee.activity;


import static shakti.shakti_employee.activity.CheckInvkActivity.MEDIA_TYPE_IMAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapter.Adapter_offline_list;

import shakti.shakti_employee.BuildConfig;
import shakti.shakti_employee.R;
import shakti.shakti_employee.bean.LocalConvenienceBean;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.utility.CameraUtils;


public class OfflineDataConveyance extends BaseActivity {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int BITMAP_SAMPLE_SIZE = 8;

   public static Context context;
    DatabaseHelper db;
    String version,device_name;
    public  static  String  imageStoragePath;
    private Toolbar mToolbar;
    LinearLayout lin1, lin2;
    RecyclerView recyclerView;
    Adapter_offline_list adapterEmployeeList;
    private LinearLayoutManager layoutManagerSubCategory;

    ArrayList<LocalConvenienceBean> localConvenienceBeanArrayList;

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

    public static void openCamera() {



        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);

        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
            Log.e("PATH", "&&&" + imageStoragePath);
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(context, file);

        Log.e("PATH", "&&&" + fileUri);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        ((Activity) context).startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        Bitmap bitmap = null;
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                try {

                    bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                    int count = bitmap.getByteCount();

                    Log.e("Count", "&&&&&" + count);

                    ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);

                    byte[] byteArray = byteArrayBitmapStream.toByteArray();

                    long size = byteArray.length;

                    Log.e("SIZE1234", "&&&&" + size);

                    Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));

                    if (Adapter_offline_list.end_photo_flag) {
                        Adapter_offline_list.end_photo_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        setIcon(DatabaseHelper.KEY_PHOTO2);
                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                File file = new File(imageStoragePath);
                if (file.exists()) {
                    file.delete();
                }


            }
        }
    }


    public void setIcon(String key) {

        if (DatabaseHelper.KEY_PHOTO2.equals(key)) {
            if (Adapter_offline_list.end_photo_text == null || Adapter_offline_list.end_photo_text.isEmpty()) {
                Adapter_offline_list.photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_icn, 0);
            } else {
                Adapter_offline_list.photo2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_mark_icn_green, 0);
            }
        }
    }

}
