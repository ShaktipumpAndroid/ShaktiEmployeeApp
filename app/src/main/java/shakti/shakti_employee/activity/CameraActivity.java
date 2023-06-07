package shakti.shakti_employee.activity;

import static shakti.shakti_employee.utility.Config.TIME_STAMP_FORMAT_DATE;
import static shakti.shakti_employee.utility.Config.TIME_STAMP_FORMAT_TIME;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import shakti.shakti_employee.R;
import shakti.shakti_employee.other.CustomUtility;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PictureCallback {

    private static final String GALLERY_DIRECTORY_NAME_COMMON = "EmployeeCamera";
    public static int RESULT_CODE = 1, REQUEST_CODE = 1;
    LinearLayout layoutpreview;
    TextView display, takePhotoBtn;
    FusedLocationProviderClient location;

    File save;
    String latitudetxt, longitudetxt, addresstxt, state, country, postalcode;
    Bitmap bitmap;

    SimpleDateFormat getDate, getTime;
    private SurfaceView surfaceView;

    private SurfaceHolder surfaceHolder;

    private android.hardware.Camera camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        Init();
        setupSurfaceHolder();
        getlastLocation();

        listner();

    }


    private void Init() {
        surfaceView = findViewById(R.id.surfaceView);
        layoutpreview = findViewById(R.id.layoutPreview);
        display = findViewById(R.id.display);

        takePhotoBtn = findViewById(R.id.takePhotoBtn);

    }

    private void setupSurfaceHolder() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

    }

    private void listner() {
        takePhotoBtn.setOnClickListener(view -> captureImage());
    }

    public void captureImage() {
        if (camera != null) {
            camera.takePicture(null, null, this);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.e("surfaceHolder===>", String.valueOf(surfaceHolder.getSurface()));
        startCamera();
    }

    private void startCamera() {
        camera = android.hardware.Camera.open(0);
        camera.setDisplayOrientation(90);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        resetCamera();
    }

    public void resetCamera() {
        if (surfaceHolder.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }

        if (camera != null) {
            // Stop if preview surface is already running.
            camera.stopPreview();
            try {
                // Set preview display
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the camera preview...
            camera.startPreview();
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onBackPressed() {

        if (save != null) {
            Intent intent = new Intent();
            intent.putExtra("data", save);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, getResources().getString(R.string.Click_Again), Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    private void getlastLocation() {
        location = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            location.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null && !String.valueOf(location.getLatitude()).isEmpty() && !String.valueOf(location.getLongitude()).isEmpty()) {
                            Geocoder geocoder = new Geocoder(CameraActivity.this, Locale.getDefault());
                            try {
                                if (CustomUtility.isInternetOn(getApplicationContext())) {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    if (!addresses.isEmpty()) {
                                        latitudetxt = String.valueOf(addresses.get(0).getLatitude());
                                        longitudetxt = String.valueOf(addresses.get(0).getLongitude());
                                        addresstxt = addresses.get(0).getAddressLine(0).substring(0, 35);
                                        state = addresses.get(0).getAdminArea();
                                        postalcode = addresses.get(0).getPostalCode();
                                        country = addresses.get(0).getCountryName();
                                        getDate = new SimpleDateFormat(TIME_STAMP_FORMAT_DATE, Locale.getDefault());
                                        getTime = new SimpleDateFormat(TIME_STAMP_FORMAT_TIME, Locale.getDefault());

                                        display.setText(" Latitude : " + latitudetxt + "\n" + " Longitude : " + longitudetxt + "\n" + " Address : " + addresstxt + ","
                                                + state + " " + postalcode + "," + country + "\n" + "Date: " + getDate.format(new Date()) + "\n" + "Time: " + getTime.format(new Date()));


                                    }
                                } else {
                                    latitudetxt = String.valueOf(location.getLatitude());
                                    longitudetxt = String.valueOf(location.getLongitude());
                                    getDate = new SimpleDateFormat(TIME_STAMP_FORMAT_DATE, Locale.getDefault());
                                    getTime = new SimpleDateFormat(TIME_STAMP_FORMAT_TIME, Locale.getDefault());

                                    display.setText(" Latitude : " + latitudetxt + "\n" + " Longitude : " + longitudetxt + "\n" + " Address : " + addresstxt + ","
                                            + state + " " + postalcode + "," + country + "\n" + "Date: " + getDate.format(new Date()) + "\n" + "Time: " + getTime.format(new Date()));


                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
        } else {
            askpermission();
        }
    }

    private void askpermission() {
        ActivityCompat.requestPermissions(CameraActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, RESULT_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You can't use camera without permission", Toast.LENGTH_SHORT).show();
            }
            setupSurfaceHolder();
        }

    }


    @Override
    public void onPictureTaken(byte[] bytes, android.hardware.Camera camera) {

        bitmap = saveImageWithTimeStamp(bytes);
        save = saveFile(bitmap, getResources().getString(R.string.app_name));
        onBackPressed();
    }

    public Bitmap saveImageWithTimeStamp(byte[] data) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);

        bmp = rotateBitmap(bmp, 90);
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_STAMP_FORMAT_DATE, Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat(TIME_STAMP_FORMAT_TIME, Locale.getDefault());
        String date = sdf.format(new Date());
        String time = sdf1.format(new Date());

        Canvas canvas = new Canvas(bmp);
        int color = ContextCompat.getColor(CameraActivity.this, R.color.colorPrimaryDark);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(110);
        paint.setColor(color);
        paint.setFakeBoldText(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        //  canvas.drawBitmap(bmp, 0f, 0f, null);


        float height = paint.measureText("yY");
        float width = paint.measureText("Date: " + date + "\n" + "Time: " + time);
        float startXPosition = (bmp.getWidth() - width);
        float startYPosition = (bmp.getHeight() - height);

        String text1 = "Latitude : " + latitudetxt;
        String text2 = "Longitude : " + longitudetxt;
        String text3 = "Address : " + addresstxt + ",";
        String text = state + " " + postalcode + "," + country;
        String text4 = "Date: " + date;
        String text5 = "Time: " + time;


        //canvas.drawText("Customer: " + customer_name, startXPosition - 1250, startYPosition - 900, paint);
        canvas.drawText(text4 + " " + text5, startXPosition - 1250, startYPosition - 750, paint);
        canvas.drawText(text, startXPosition - 1250, startYPosition - 600, paint);
        canvas.drawText(text3, startXPosition - 1250, startYPosition - 450, paint);
        canvas.drawText(text1, startXPosition - 1250, startYPosition - 300, paint);
        canvas.drawText(text2, startXPosition - 1250, startYPosition - 150, paint);


        return bmp;
    }

    public static File saveFile(Bitmap bitmap, String type) {
        File file = new File(getMediaFilePath(type));
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }


    private static Bitmap rotateBitmap(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        matrix.preScale(1.0f, 1.0f);

        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public static String getMediaFilePath(String type) {

        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), GALLERY_DIRECTORY_NAME_COMMON);

        File dir = new File(root.getAbsolutePath() + "/ShaktiEmpAPP/" + type); //it is my root directory

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a media file name
        return dir.getPath() + File.separator + "IMG_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
    }
}
