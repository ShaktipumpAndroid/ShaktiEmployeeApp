package shakti.shakti_employee.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.utility.CameraUtils;
import shakti.shakti_employee.utility.Constant;

public class DailyReportActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_PERMISSION = 1,CAMERA_CAPTURE_IMAGE_REQUEST_CODE=234;
    Toolbar mToolbar;
    RelativeLayout ImageNameRelative;
    TextView dailyReportDate,submitBtn;
    String photoTxt="";
    Uri fileUri;
    ImageView imgIcon;

    Spinner selectPersonSpinner,selectReportType;

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
        ImageNameRelative = findViewById(R.id.ImageNameRelative);
        imgIcon = findViewById(R.id.imgIcon);
        selectPersonSpinner = findViewById(R.id.selectPersonSpinner);
        selectReportType = findViewById(R.id.selectReportType);
        dailyReportDate = findViewById(R.id.dailyReportTxt);
        submitBtn = findViewById(R.id.submitBtn);

        dailyReportDate.setText( new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        setSpinner();
    }

    private void setSpinner() {
        final String[] st_req_type = {"Official Gate Pass","Official Duty","Official  Tour"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, st_req_type);
        adapter1.setDropDownViewResource(R.layout.spinner_item);
        selectPersonSpinner.setAdapter(adapter1);

        // Spinner selection for Request Type

        final String[] st_gp_type = {"Customer", "Vendor",};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, st_gp_type);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        selectReportType.setAdapter(adapter2);

    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        ImageNameRelative.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.ImageNameRelative:
                if(checkPermission()){
                if(photoTxt.isEmpty()) {
                    showConfirmationGallery("0");
                }else {
                    showConfirmationGallery("1");
                }
                }else {
                    requestPermission();
                }
                break;
            case R.id.submitBtn:

                break;
        }
    }

 
    private boolean checkPermission() {
        int cameraPermission =
                ContextCompat.checkSelfPermission(this, CAMERA);
        int writeExternalStorage =
                ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int ReadExternalStorage =
                ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE);

        int AccessCoarseLocation =
                ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION);
        int AccessFineLocation =
                ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        int ReadMediaImage =
                ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES);


        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return cameraPermission == PackageManager.PERMISSION_GRANTED
                    && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED && AccessFineLocation == PackageManager.PERMISSION_GRANTED && ReadMediaImage == PackageManager.PERMISSION_GRANTED;
        } else {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED
                    && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED && AccessFineLocation == PackageManager.PERMISSION_GRANTED;

        }

    }

   
    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_MEDIA_IMAGES},
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {
                if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessFineLocation = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadMediaImage = grantResults[3] == PackageManager.PERMISSION_GRANTED;


                    if (ACCESSCAMERA && AccessCoarseLocation && AccessFineLocation && ReadMediaImage) {
                        showConfirmationGallery("0");


                    } else {
                        Toast.makeText(this, R.string.all_permission, Toast.LENGTH_LONG).show();
                    }
                } else {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalStorage =
                            grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage =
                            grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessFineLocation = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (ACCESSCAMERA && writeExternalStorage && ReadExternalStorage && AccessCoarseLocation && AccessFineLocation) {
                        showConfirmationGallery("0");
                    } else {
                        Toast.makeText(this, R.string.all_permission, Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }
    public void showConfirmationGallery(String value) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.pick_img_layout, null);
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.MyDialogTheme);

        builder.setView(layout);
        builder.setCancelable(true);
       AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.show();

        TextView title = layout.findViewById(R.id.titleTxt);
        TextView camera = layout.findViewById(R.id.camera);
        TextView gallery = layout.findViewById(R.id.gallery);
        TextView cancel = layout.findViewById(R.id.cancel);


        if (value.equals("0")) {
            gallery.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.click_image));
            camera.setText(getResources().getString(R.string.camera));
        } else {
            title.setText(getResources().getString(R.string.want_to_perform));
            gallery.setText(getResources().getString(R.string.display));
            camera.setText(getResources().getString(R.string.change));
        }

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (value.equals("1")) {

                    Intent i_display_image = new Intent(getApplicationContext(), ShowDocument.class);
                    i_display_image.putExtra("image_path", photoTxt);
                    startActivity(i_display_image);
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (value.equals("0")) {
                    openCamera();
                } else {
                    showConfirmationGallery("0");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void openCamera() {
        ContentValues values = new ContentValues();
        fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                try {
                    Bitmap bitmap =
                            MediaStore.Images.Media.getBitmap(getContentResolver(), fileUri);

                    Bitmap UserBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                            fileUri);

                    String path = CameraUtils.getPath(this, fileUri); // From Gallery

                    if (path == null) {
                        path = data.getData().getPath(); // From File Manager
                    }
                    String filedata = path;
                    Log.e("Activity", "PathHolder22= " + path);

                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    String file;
                    if (filename.indexOf(".") > 0) {
                        file = filename.substring(0, filename.lastIndexOf("."));
                    } else {
                        file = "";
                    }
                    if (TextUtils.isEmpty(file)) {
                        CustomUtility.ShowToast("File not valid", this);
                    } else {
                        photoTxt = path;
                        if (photoTxt.isEmpty()) {
                            imgIcon.setImageResource(R.drawable.red_icn);
                        } else {
                            imgIcon.setImageResource(R.drawable.right_mark_icn_green);
                        }
                    }

                } catch (NullPointerException | FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }


}