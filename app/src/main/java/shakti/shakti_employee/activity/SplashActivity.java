package shakti.shakti_employee.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import shakti.shakti_employee.BuildConfig;
import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.utility.Utility;


public class SplashActivity extends AppCompatActivity {
    ImageView imageView;
    DatabaseHelper databaseHelper;
    Intent i;
    String versionName = "0.0";
    String newVersion = "0.0";
    private Context mContext;
    private final int REQUEST_CODE_PERMISSION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;


        imageView = findViewById(R.id.imageSplash);

        versionName = BuildConfig.VERSION_NAME;

        databaseHelper = new DatabaseHelper(SplashActivity.this);

        if (!checkPermission()) {
            requestPermission();
        } else {
            checkLoginStatus();
        }


    }

    private void checkLoginStatus() {
        new Handler().postDelayed(() -> {

            Log.d("newVersion", newVersion + "--" + versionName);
            if (Utility.isDateTimeAutoUpdate(mContext)) {

                if (databaseHelper.getLogin()) {
                    i = new Intent(SplashActivity.this, DashboardActivity.class);
                } else {

                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(i);
                SplashActivity.this.finish();

            } else {
                Utility.ShowToast("Date Time not auto update please check it.", mContext);
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                mContext.startActivity(intent);
                finish();
            }
        }, 1000);
    }


    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);

        }
    }

    private boolean checkPermission() {
        int cameraPermission =
                ContextCompat.checkSelfPermission(SplashActivity.this, CAMERA);
        int writeExternalStorage =
                ContextCompat.checkSelfPermission(SplashActivity.this, WRITE_EXTERNAL_STORAGE);
        int ReadExternalStorage =
                ContextCompat.checkSelfPermission(SplashActivity.this, READ_EXTERNAL_STORAGE);
        int ReadMediaImages =
                ContextCompat.checkSelfPermission(SplashActivity.this, READ_MEDIA_IMAGES);

        int AccessCoarseLocation =
                ContextCompat.checkSelfPermission(SplashActivity.this, ACCESS_COARSE_LOCATION);
        int AccessFineLocation =
                ContextCompat.checkSelfPermission(SplashActivity.this, ACCESS_FINE_LOCATION);


        if (SDK_INT >= Build.VERSION_CODES.R) {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && ReadMediaImages == PackageManager.PERMISSION_GRANTED
                    && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED && AccessFineLocation == PackageManager.PERMISSION_GRANTED;
        } else {
            return cameraPermission == PackageManager.PERMISSION_GRANTED && writeExternalStorage == PackageManager.PERMISSION_GRANTED
                    && ReadExternalStorage == PackageManager.PERMISSION_GRANTED
                    && AccessCoarseLocation == PackageManager.PERMISSION_GRANTED && AccessFineLocation == PackageManager.PERMISSION_GRANTED;

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0) {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    //boolean ReadMediaImages = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessFineLocation = grantResults[3] == PackageManager.PERMISSION_GRANTED;


                    if (ACCESSCAMERA && AccessCoarseLocation && AccessFineLocation) {
                        checkLoginStatus();
                    } else {
                        Toast.makeText(SplashActivity.this, R.string.all_permission, Toast.LENGTH_LONG).show();
                    }
                } else {
                    boolean ACCESSCAMERA = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStorage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarseLocation = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessFineLocation = grantResults[4] == PackageManager.PERMISSION_GRANTED;

                    if (ACCESSCAMERA && writeExternalStorage && ReadExternalStorage && AccessCoarseLocation && AccessFineLocation) {
                        checkLoginStatus();
                    } else {
                        Toast.makeText(SplashActivity.this, R.string.all_permission, Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }

}
