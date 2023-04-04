package shakti.shakti_employee.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.GPSTracker;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.utility.CameraActivitySurvey;
import shakti.shakti_employee.utility.CameraUtils;
import shakti.shakti_employee.utility.CustomHttpClient;
import shakti.shakti_employee.utility.ImageManager;
import shakti.shakti_employee.utility.Utility;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class CheckInvkActivity extends AppCompatActivity {

    DatabaseHelper db;
    DatabaseHelper dataHelper;

    double inst_latitude_double,
            inst_longitude_double;
    private EditText edtVendorNameID, edtContactNoID, edtLatitudeID, edtLongitudeID, edtremarkConversionID, edtLatLongID, edtSizeOfBorewellID,edtRelevantInformationID,
            edtWaterLeveatSiteID, edtPumpingwaterlevelrequiredID,edtRatingOfPumpSetID, edtSurvePersonContactID,edtSurvePersonNameID,edtFarmerContactID, edtFarmerNameID;

    private String strApplicantNameID, strContactNoID, strApplicationNoID, strSiteAddressID, strVillageNameID, strLatLongID, strSizeOfBorewellID,strRelevantInformationID,
            strWaterLeveatSiteID, strPumpingwaterlevelrequiredID,strRatingOfPumpSetID, strSurvePersonContactID,strSurvePersonNameID,strFarmerContactID, strFarmerNameID;

    private Spinner spinner_RatingOfPumpSetID,spinner_SizeOfBorewellID,spinner_SourceOfwaterID,spinner_InternetConnectivityAvailableID,spinner_PumpingwaterlevelrequiredID,spinner_ACandDCID,spinner_TypeOfIrrigationSystemInstalled,spinner_TypeOfPumpID,spinner_conntype5;

    private RadioGroup rgGroupDarkZoneID1,rgGroupFormerHavingElectricConnectionID1, rgGroupSouthFacingShadowFreeID1, rgAgreeToInstallUniversalsolarPumpID1;
    private RadioButton radio_DarkZone_yesID,radio_FormerHavingElectricConnection_yesID,radio_SouthFacingShadowFree_yesID,radio_AgreeToInstallUniversalsolarPump_yesID;
    private RadioButton radio_DarkZone_NoID,radio_FormerHavingElectricConnection_NoID,radio_SouthFacingShadowFree_NoID,radio_AgreeToInstallUniversalsolarPump_NoID;

    String imageStoragePath, enq_docno, photo1_text, photo2_text, photo3_text;
    File file;
    // String type="INST/";
    String type="VENDOR/";
    String mImageFolderName = "/SKAPP/VENDOR/";
    // String mImageFolderName = "/SKAPP/DMGMISS/";
    // String mImageFolderName = "/SHAKTI/DMGMISS/";

    public static final int BITMAP_SAMPLE_SIZE = 4;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 101;


    int PERMISSION_ALL = 1;
    public static final String GALLERY_DIRECTORY_NAME = "VENDORVISITPHOTO";

    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    boolean photo1_flag = false,photo2_flag = false, photo3_flag = false;

    private String mHomePath, PathHolder, Filename,cust_name;

    TextView photo1,photo2, photo3;
    TextView txtSubmitInspectionID;


    private Context context;



    private RelativeLayout rlvPhotoID1,rlvPhotoID2,rlvPhotoID3,rlvPhotoID4,rlvPhotoID5;
    private RelativeLayout rlvBackViewID;





    private String mPhotoValue1;



    private int index_conntype1,index_conntype2,index_conntype3,index_conntype4,index_conntype5,index_conntype6,index_conntype7, index_conntype8;
    String conntype_text1 = "", conntype_text2 = "",conntype_text3 = "",conntype_text4 = "",conntype_text5 = "", conntype_text6 = "",conntype_text7 = "", conntype_text8 = "";




    String billnoN ;
    String billdate,name,state, city,mobileno,address,kunnr, pernr;

    TextView txtLatID, txtLongID;
    String strLatID, strLongID;

    String mUserID = "";
    String mproject_noID = "";
    String mproject_login_noID = "";

    private RelativeLayout rlvSaveButtonID;

    private LoggedInUser userModel;

    String mApplicationNo, mProjectNo, mMobileNo,mLifnrNo,mCitycTxt, mApplicantName, mregio_txt, mRegistraionNo;
    Intent mIntent ;

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_invk);

        context = this;

        Utility.GALLERY_DIRECTORY_NAME_COMMON = "ShaktiEmployeeApp";

        db = new DatabaseHelper(context);
        dataHelper = new DatabaseHelper(context);
        mIntent = getIntent();


       // CustomUtility.setSharedPreference(context, "userID", pernr);

       // Utility.CUSTOMERID_ID = CustomUtility.getSharedPreferences(context, "userID");

        mApplicationNo = mIntent.getStringExtra("Beneficiary");
        mProjectNo = mIntent.getStringExtra("ProjectNo");
        mMobileNo = mIntent.getStringExtra("Mobile");
        mLifnrNo = mIntent.getStringExtra("Lifnr");
        mCitycTxt = mIntent.getStringExtra("CitycTxt");
        mApplicantName = mIntent.getStringExtra("ApplicantName");
        mregio_txt = mIntent.getStringExtra("regio_txt");
        mRegistraionNo = mIntent.getStringExtra("RegistraionNo");

        //intent.putExtra("RegistraionNo", mSurweyListResponse.get(position).getRegisno());

        userModel = new LoggedInUser(context);


        getGpsLocation();
        initView();
        initViewSurvay();
    }

    private void initViewSurvay() {

        txtLatID = findViewById(R.id.txtLatID);
        txtLongID = findViewById(R.id.txtLongID);

        txtLatID.setText(""+inst_latitude_double);
        txtLongID.setText(""+inst_longitude_double);

        strLatID =  txtLatID.getText().toString().trim();
        strLongID =  txtLongID.getText().toString().trim();

        edtVendorNameID = findViewById(R.id.edtVendorNameID);

        edtContactNoID = findViewById(R.id.edtContactNoID);
        edtLatitudeID = findViewById(R.id.edtLatitudeID);
        edtLongitudeID = findViewById(R.id.edtLongitudeID);
        edtremarkConversionID = findViewById(R.id.edtremarkConversionID);


        //  edtSizeOfBorewellID = findViewById(R.id.edtSizeOfBorewellID);

        //  edtPumpingwaterlevelrequiredID = findViewById(R.id.edtPumpingwaterlevelrequiredID);



        edtVendorNameID.setText(mApplicantName);

        edtContactNoID.setText(mMobileNo);
        edtLatitudeID.setText(mApplicationNo);

        edtLongitudeID.setText(mCitycTxt);

        edtremarkConversionID.setText(mregio_txt);
        //edtLatLongID.setText(inst_latitude_double+" , "+inst_longitude_double);
//        edtLatLongID.setText(strLatID+" , "+strLongID);




    }


    private void initView() {


       // pernr  = CustomUtility.getSharedPreferences(context, "userid");
       // Utility.CUSTOMERID_ID  = CustomUtility.getSharedPreferences(context, "userid");
        Utility.CUSTOMERID_ID  = userModel.uid;





        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();



        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);

        File dir = new File(root.getAbsolutePath() + mImageFolderName); //it is my root directory

        // File billno = new File(root.getAbsolutePath() + mImageFolderName + WebURL.CUSTOMERID_ID); // it is my sub folder directory .. it can vary..
        File billno = new File(root.getAbsolutePath() + mImageFolderName + CustomUtility.getSharedPreferences(context, "userid")); // it is my sub folder directory .. it can vary..

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!billno.exists()) {
                billno.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }


        rlvSaveButtonID= findViewById(R.id.rlvSaveButtonID);

        rlvBackViewID= findViewById(R.id.rlvBackViewID);





        rlvPhotoID1= findViewById(R.id.rlvPhotoID1);




        photo1 = (TextView) findViewById(R.id.photo1);






        // intiValidationCheck();
    }

    private boolean intiValidationCheck() {

        //String mDropdownValue1,mDropdownValue2,mDropdownValue3,mDropdownValue4,mDropdownValue5;

        strApplicantNameID = edtVendorNameID.getText().toString().trim();


        strContactNoID = edtContactNoID.getText().toString().trim();
        strApplicationNoID = edtLatitudeID.getText().toString().trim();

        strSiteAddressID = edtLongitudeID.getText().toString().trim();
        strVillageNameID = edtremarkConversionID.getText().toString().trim();


        //  strSizeOfBorewellID = edtSizeOfBorewellID.getText().toString().trim();
        strWaterLeveatSiteID = edtWaterLeveatSiteID.getText().toString().trim();
        //  strPumpingwaterlevelrequiredID = edtPumpingwaterlevelrequiredID.getText().toString().trim();

        //  strRatingOfPumpSetID = edtRatingOfPumpSetID.getText().toString().trim();

        strRelevantInformationID = edtRelevantInformationID.getText().toString().trim();
        strSurvePersonContactID = edtSurvePersonContactID.getText().toString().trim();
        strSurvePersonNameID = edtSurvePersonNameID.getText().toString().trim();
        strFarmerContactID = edtFarmerContactID.getText().toString().trim();
        strFarmerNameID = edtFarmerNameID.getText().toString().trim();

        mPhotoValue1 = photo1_text;

        //mRodioValue1 =


           return true;

        // return true;
    }







    // rgGroupFormerHavingElectricConnectionID1, rgGroupSouthFacingShadowFreeID1, rgAgreeToInstallUniversalsolarPumpID1



   /* public void showConfirmationGallery(final String keyimage, final String name) {

        final CustomUtility customUtility = new CustomUtility();

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = customUtility.checkPermission(context);
                if (items[item].equals("Take Photo")) {

                    if (result) {
                        openCamera(name);
                        setFlag(keyimage);
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    if (result) {
                        openGallery(name);
                        setFlag(keyimage);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }*/

    public void showConfirmationGallery(final String keyimage, final String name) {

     //   final CustomUtility customUtility = new CustomUtility();

        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = shakti.shakti_employee.utility.CustomUtility.checkPermission(context);
                if (items[item].equals("Take Photo")) {

                    if (result) {
                        openCamera(name);
                        setFlag(keyimage);
                    }

                } else if (items[item].equals("Choose from Gallery")) {
                    if (result) {
                        openGallery(name);
                        setFlag(keyimage);


                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void openCamera(String name) {

        if (CameraUtils.checkPermissions(context)) {

           /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            String from = "INST/";

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE, enq_docno, name, from);

            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
                Log.e("PATH", "&&&" + imageStoragePath);
            }

            fileUri1 = CameraUtils.getOutputMediaFileUri(mContext, file);

            Log.e("fileUri", "&&&" + fileUri1);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
*/

            file = new File(ImageManager.getMediaFilePath(type,name, Utility.CUSTOMERID_ID));

            imageStoragePath = file.getAbsolutePath();
            Log.e("PATH", "&&&" + imageStoragePath);

            Intent i = new Intent(context, CameraActivitySurvey.class);
            i.putExtra("lat", String.valueOf(inst_latitude_double));
            i.putExtra("lng", String.valueOf(inst_longitude_double));
            i.putExtra("cust_name", cust_name);
            i.putExtra("inst_id", Utility.CUSTOMERID_ID);
            i.putExtra("type", type);
            i.putExtra("name", name);

            startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }


    }



    public void openGallery(String name) {

        if (ActivityCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT <= 19) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);

                ((Activity) context).startActivityForResult(Intent.createChooser(i, "Select Photo"), GALLERY_IMAGE_REQUEST_CODE);
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Photo"), GALLERY_IMAGE_REQUEST_CODE);

            }

        } else {
            if (!hasPermissions(context, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_ALL);
            }
        }
    }

    public void setFlag(String key) {

        Log.e("FLAG", "&&&" + key);
        photo1_flag = false;
        photo2_flag = false;
        photo3_flag = false;



        switch (key) {

            case DatabaseHelper.KEY_PHOTO1:
                photo1_flag = true;
                break;



        }

    }

    public void setIcon(String key) {
        switch (key) {
            case DatabaseHelper.KEY_PHOTO1:
                if (photo1_text == null || photo1_text.isEmpty()) {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.red_icn, 0);
                } else {
                    photo1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mendotry, 0, R.drawable.right_mark_icn_green, 0);
                }
                break;

        }
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            try {
                Log.e("Count", "&&&&&" + imageStoragePath);

                Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                int count = bitmap.getByteCount();

                Log.e("Count", "&&&&&" + count);

                Log.e("IMAGEURI", "&&&&" + imageStoragePath);

                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);

                byte[] byteArray = byteArrayBitmapStream.toByteArray();

                long size = byteArray.length;

                Log.e("SIZE1234", "&&&&" + size);

                // Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));

                // if (photo1_flag == true)
                if (photo1_flag)
                {
                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + mImageFolderName + Utility.CUSTOMERID_ID, "/IMG_PHOTO_1.jpg");
                    if (file.exists()) {
                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        mPhotoValue1 = photo1_text;
                        setIcon(DatabaseHelper.KEY_PHOTO1);
                        CustomUtility.setSharedPreference(context, Utility.CUSTOMERID_ID + "PHOTO_1", photo1_text);
                        Log.e("SIZE1", "&&&&" + CustomUtility.getSharedPreferences(context, Utility.CUSTOMERID_ID + "PHOTO_1"));

                    }
                }





            } catch (NullPointerException e) {
                e.printStackTrace();
            }

             /*   File file = newworkorder File(imageStoragePath);
                if (file.exists()) {
                    file.delete();
                }*/
        }
        else {
            if (requestCode == GALLERY_IMAGE_REQUEST_CODE) {

                if (resultCode == RESULT_OK) {

                    if (data != null) {

                        Uri selectedImageUri = data.getData();
                        String selectedImagePath = getImagePath(selectedImageUri);
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                        options.inSampleSize = 6;
                        options.inJustDecodeBounds = false;

                        try {
                            Log.e("IMAGEURI", "&&&&" + selectedImageUri);
                            if (selectedImageUri != null) {

                                Bitmap bitmap  = BitmapFactory.decodeFile(selectedImagePath, options);

                                //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                                int count = bitmap.getByteCount();

                                Log.e("Count", "&&&&&" + count);
                                ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);

                                byte[] byteArray = byteArrayBitmapStream.toByteArray();

                                long size = byteArray.length;


                                Log.e("SIZE1234", "&&&&" + size);

                                Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));

                                if (photo1_flag) {
                                    String destFile = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME+ mImageFolderName + Utility.CUSTOMERID_ID + "/IMG_PHOTO_1.jpg";
                                    copyFile(selectedImagePath, destFile);
                                    File file = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/"+GALLERY_DIRECTORY_NAME + mImageFolderName + Utility.CUSTOMERID_ID, "/IMG_PHOTO_1.jpg");
                                    if (file.exists()) {
                                        photo1_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                        CustomUtility.setSharedPreference(context, Utility.CUSTOMERID_ID + "PHOTO_1", photo1_text);
                                        Log.e("SIZE1", "&&&&" + photo1_text);
                                        setIcon(DatabaseHelper.KEY_PHOTO1);
                                    }

                                }



                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                Uri uri = data.getData();
                String uriString = null;
                if (uri != null) {
                    uriString = uri.toString();
                }
                File myFile = new File(uriString);
                //PathHolder = myFile.getPath();
                Filename = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {

                            Filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            PathHolder = getExternalStorageDirectory() + "/Download/" + Filename;
                            Log.e("&&&&", "DDDDD" + Filename);

                            if (PathHolder != null && !PathHolder.equals("")) {

                            } else {

                            }
                        }
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                } else if (uriString.startsWith("file://")) {
                    Filename = myFile.getName();
                    PathHolder = getExternalStorageDirectory() + "/Download/" + Filename;
                    Log.e("&&&&", "DDDDD" + Filename);
                    if (PathHolder != null && !PathHolder.equals("")) {

                    } else {

                    }
                }
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 301) {

                String year = data.getStringExtra("year");
                String month = data.getStringExtra("month");
                String date = data.getStringExtra("date");
                String finaldate = year + "-" + month + "-" + date;
                finaldate = CustomUtility.formateDate(finaldate);
            }
        }
    }

    private void copyFile(String sourceFilePath, String destinationFilePath) {

        Log.e("Source", "&&&&" + sourceFilePath);
        Log.e("Destination", "&&&&" + destinationFilePath);

        File sourceLocation = new File(sourceFilePath);
        File targetLocation = new File(destinationFilePath);
        Log.e("&&&&&", "sourceLocation: " + sourceLocation);
        Log.e("&&&&&", "targetLocation: " + targetLocation);
        try {
            int actionChoice = 2;
            if (actionChoice == 1) {
                if (sourceLocation.renameTo(targetLocation)) {
                    Log.e("&&&&&", "Move file successful.");
                } else {
                    Log.e("&&&&&", "Move file failed.");
                }
            } else {
                if (sourceLocation.exists()) {
                    InputStream in = new FileInputStream(sourceLocation);
                    OutputStream out = new FileOutputStream(targetLocation);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    Log.e("&&&&&", "Copy file successful.");
                } else {
                    Log.e("&&&&&", "Copy file failed. Source file missing.");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showConfirmationAlert(final String keyimage, final String data, final String name) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        // Setting Dialog Title
        alertDialog.setTitle("Confirmation");
        // Setting Dialog Message
        alertDialog.setMessage("Image already saved, Do you want to change it or display?");
        // On pressing Settings button
        alertDialog.setPositiveButton("Display", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Log.e("KEY", "&&&&" + keyimage);
                Log.e("DATA", "&&&&" + data);

                displayImage(keyimage, data);


            }
        });

        alertDialog.setNegativeButton("Change", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                showConfirmationGallery(keyimage, name);


            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public String getImagePath(Uri uri) {

        String s = null;

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        } else {
            String[] projection = {String.valueOf(MediaStore.Images.Media.DATA)};

            Cursor cursor1 = ((Activity) context).getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
            Cursor cursor2 = ((Activity) context).getContentResolver().query(uri, projection, null, null, null);

            Log.e("CUR1", "&&&&" + cursor1);
            Log.e("CUR2", "&&&&" + cursor2);

            if (cursor1 == null && cursor2 == null) {
                return null;
            } else {

                int column_index = 0;
                if (cursor1 != null) {
                    column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor1.moveToFirst();

                    if (cursor1.moveToFirst()) {
                        s = cursor1.getString(column_index);
                    }
                    cursor1.close();
                }
                int column_index1 = 0;
                if (cursor2 != null) {
                    column_index1 = cursor2.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor2.moveToFirst();

                    if (cursor2.moveToFirst()) {
                        s = cursor2.getString(column_index1);
                    }
                    cursor2.close();
                }

                return s;
            }
        }
    }

    private void displayImage(String key, String data) {
       /* Intent i_display_image = new Intent(context, ShowDocument.class);
        Bundle extras = new Bundle();
        //saveData();
        extras.putString("docno", enq_docno);
        extras.putString("key", key);
        extras.putString("data", "INST");

        CustomUtility.setSharedPreference(context, "data", data);

        i_display_image.putExtras(extras);
        startActivity(i_display_image);*/
    }



    private class SyncDAMAGEMISSData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
            progressDialog = ProgressDialog.show(context, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;

            //  DatabaseHelper db = new DatabaseHelper(context);

            //  InstallationBean param_invc = new InstallationBean();
            // param_invc = db.getInstallationData(pernr, billno);


            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {


                jsonObj.put("APPLICANT_NAME", strApplicantNameID);//userID
                jsonObj.put("CONTACT_NO", strContactNoID);//userID
                jsonObj.put("APPLICANT_NO", strApplicationNoID);//userID
                jsonObj.put("SITE_ADRC", strSiteAddressID);//userID
                jsonObj.put("VILLAGE", strVillageNameID);//userID
                jsonObj.put("Lat", strLatID);//userID
                jsonObj.put("lng", strLongID);//userID

                //  jsonObj.put("BOREWELL_SIZE", strSizeOfBorewellID);//userID

                jsonObj.put("WATER_LVL", strWaterLeveatSiteID);//userID
                // jsonObj.put("PUMP_WATER_LVL", strPumpingwaterlevelrequiredID);//userID

                // jsonObj.put("PUMP_SET_RATING", strRatingOfPumpSetID);//userID

                jsonObj.put("REMARK_ANY_OTH", strRelevantInformationID);//userID
                jsonObj.put("SURVEYOR_SIGN_NAME", strSurvePersonNameID);//userID
                jsonObj.put("SURVEYOR_CONTACT_NO", strSurvePersonContactID);//userID
                jsonObj.put("FARMER_SIGNATURE", strFarmerNameID);//userID
                jsonObj.put("FARMER_CONTACT_NO", strFarmerContactID);//userID


                //   jsonObj.put("category5", mRodioValue5);

                jsonObj.put("PHOTO1", photo1_text);



                // jsonObj.put("PHOTO5", CustomUtility.getSharedPreferences(context, billno + "PHOTO_5"));

                ja_invc_data.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            //  param1_invc.add(new BasicNameValuePair("installation", String.valueOf(ja_invc_data)));
            param1_invc.add(new BasicNameValuePair("survey", String.valueOf(ja_invc_data)));
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println(param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                obj2 = CustomHttpClient.executeHttpPost1(SapUrl.CHECK_IN_VENDORE, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (!obj2.equalsIgnoreCase("")) {

                    JSONObject object = new JSONObject(obj2);
                    String obj1 = object.getString("data_save");


                    JSONArray ja = new JSONArray(obj1);


                    Log.e("OUTPUT2", "&&&&" + ja.toString());

                    for (int i = 0; i < ja.length(); i++) {

                        JSONObject jo = ja.getJSONObject(i);

                        docno_sap = jo.getString("mdocno");
                        invc_done = jo.getString("return");


                        if (invc_done.equalsIgnoreCase("Y")) {

                            Message msg = new Message();
                            msg.obj = "Data Submitted Successfully...";
                            mHandler2.sendMessage(msg);

                            Log.e("DOCNO", "&&&&" + billnoN);
                            //  db.deleteDAMAGEMISSData(billnoN);
                            // db.deleteInstallationListData1(billnoN);

                            deleteDirectory(new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + GALLERY_DIRECTORY_NAME + mImageFolderName + Utility.CUSTOMERID_ID));


                            CustomUtility.setSharedPreference(context, billnoN + "PHOTO_1", "");


                            progressDialog.dismiss();
                            finish();

                        } else if (invc_done.equalsIgnoreCase("N")) {

                            Message msg = new Message();
                            msg.obj = "Data Not Submitted, Please try After Sometime.";
                            mHandler2.sendMessage(msg);
                            progressDialog.dismiss();
                            finish();
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
            onResume();
            progressDialog.dismiss();  // dismiss dialog


        }
    }

    private boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return false;
            }
            for(File file : files) {
                if(file.isDirectory()) {
                    deleteDirectory(file);
                }
                else {
                    file.delete();
                }
            }
        }
        return path.exists() && path.delete();
    }


    android.os.Handler mHandler2 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();


        }
    };

    public void getGpsLocation() {
        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {
            inst_latitude_double = gps.getLatitude();
            inst_longitude_double = gps.getLongitude();
          /*  if (inst_latitude_double == 0.0) {
                CustomUtility.ShowToast("Lat Long not captured, Please try again.", mContext);
            } else {
                //CustomUtility.ShowToast("Latitude:-" + inst_latitude_double + "     " + "Longitude:-" + inst_longitude_double, mContext);
            }*/
        } else {
            gps.showSettingsAlert();
        }
    }

}