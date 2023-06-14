package shakti.shakti_employee.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.VendorListModel;
import shakti.shakti_employee.R;
import shakti.shakti_employee.adapter.GatePassListAdapter;
import shakti.shakti_employee.adapter.ImageSelectionAdapter;
import shakti.shakti_employee.adapter.VendorListAdapter;
import shakti.shakti_employee.bean.ImageModel;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.GatePassModel;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SAPWebService;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.utility.Constant;


public class DailyReportActivity extends BaseActivity implements View.OnClickListener, ImageSelectionAdapter.ImageSelectionListener, VendorListAdapter.ImageSelectionListener, GatePassListAdapter.GatePassSelectionListener {

    public static int REQUEST_CODE_PERMISSION = 1;
    Context context;

    public String TAG = "DailyReportActivity";

    List<ImageModel> imageArrayList = new ArrayList<>();
    List<ImageModel> imageList = new ArrayList<>();

    List<VendorListModel.Response> vendorList = new ArrayList<>();

    List<GatePassModel.Response> gatePassList = new ArrayList<>();
    List<String> itemNameList = new ArrayList<>();

    Toolbar mToolbar;
    RadioButton prospectiveVendorRadio, vendorRadio;
    EditText vendorCodeExt,vendorNameExt, vendorAddressExt, vendorNumberExt, responsiblePersonExt, responsiblePerson2Ext, responsiblePerson3Ext,
            agendaExt, discussionPointExt;
    TextView currentDateTxt, targetDateTxt, submitBtn;
    Spinner visitAtSpinner, statusSpinner;
    RecyclerView recyclerview, vendorCodeList, vendorNameList, openGatePassList;
    SAPWebService con = null;
    LinearLayout GatePassLinear;
    private LoggedInUser userModel;
    ImageSelectionAdapter customAdapter;

    VendorListAdapter vendorListAdapter;

    GatePassListAdapter gatePassListAdapter;
    int mYear, mMonth, mDay, selectedIndex, vendorPosition,imgCount=0;
    SimpleDateFormat simpleDateFormat;
    String dateFormat = "dd-MM-yyyy", dateFormat2 = "yyyyMMdd", selectedTargetDate = "",
            selectedVisitAt = "", selectedStatus = "", photoTxt = "",selectedGatePass="";
    Uri fileUri;
    ProgressDialog progressDialog;

    JSONArray jsonArray = null;

    DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

        Init();
        listner();
        setAdapter();
    }


    @SuppressLint("SimpleDateFormat")
    private void Init() {

        context =this;
        con = new SAPWebService();
        progressDialog = new ProgressDialog(this);
        databaseHelper = new DatabaseHelper(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prospectiveVendorRadio = findViewById(R.id.prospectiveVendorRadio);
        vendorRadio = findViewById(R.id.vendorRadio);
        vendorNameExt = findViewById(R.id.vendorNameExt);
        vendorCodeExt = findViewById(R.id.vendorCodeExt);
        vendorAddressExt = findViewById(R.id.vendorAddressExt);
        vendorNumberExt = findViewById(R.id.vendorNumberExt);
        responsiblePersonExt = findViewById(R.id.responsiblePersonExt);
        responsiblePerson2Ext = findViewById(R.id.responsiblePerson2Ext);
        responsiblePerson3Ext = findViewById(R.id.responsiblePerson3Ext);
        agendaExt = findViewById(R.id.agendaExt);
        discussionPointExt = findViewById(R.id.discussionPointExt);
        currentDateTxt = findViewById(R.id.currentDateTxt);
        targetDateTxt = findViewById(R.id.targetDateTxt);
        visitAtSpinner = findViewById(R.id.visitAtSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        recyclerview = findViewById(R.id.recyclerview);
        vendorCodeList = findViewById(R.id.vendorCodeList);
        vendorNameList =findViewById(R.id.vendorNameList);
        openGatePassList = findViewById(R.id.openGatePassList);
        GatePassLinear = findViewById(R.id.GatePassLinear);
        submitBtn = findViewById(R.id.submitBtn);
        simpleDateFormat = new SimpleDateFormat(dateFormat);
        currentDateTxt.setText(simpleDateFormat.format(new Date()));
        userModel = new LoggedInUser(context);
    }

    private void listner() {
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        targetDateTxt.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        visitAtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    selectedVisitAt = parent.getItemAtPosition(position).toString().trim();

                    if(selectedVisitAt.equals("Shakti H.O")){
                        GatePassLinear.setVisibility(View.VISIBLE);
                        showGatePassList();
                    }else {
                        GatePassLinear.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    Toast.makeText(getApplicationContext(), "The statusSpinner is " +
                            parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
                    selectedStatus = parent.getItemAtPosition(position).toString().trim();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vendorCodeExt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    setVendorList(s.toString());
                } else {
                    vendorCodeList.setVisibility(View.GONE);
                }
                Log.e("Username",""+ userModel.uid);
            }


        });

        vendorNameExt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 1) {
                    setVendorNameList(s.toString());
                } else {
                    vendorNameList.setVisibility(View.GONE);
                }
            }
        });

        /*prospectiveVendorRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GatePassLinear.setVisibility(View.VISIBLE);
                    showGatePassList();
                }else {
                    GatePassLinear.setVisibility(View.GONE);
                }
            }
        });*/
    }

    private void setVendorNameList(String searchValue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                vendorList = databaseHelper.getVendorName(searchValue);
                if (vendorList.size() > 0) {
                    vendorNameList.setVisibility(View.VISIBLE);
                    vendorListAdapter = new VendorListAdapter(DailyReportActivity.this, vendorList);
                    vendorNameList.setHasFixedSize(true);
                    vendorNameList.setAdapter(vendorListAdapter);
                    vendorListAdapter.VendorSelection(DailyReportActivity.this);
                } else {
                    vendorNameList.setVisibility(View.GONE);
                }
            }
        });

    }

    public void setVendorList(String code) {

         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 vendorList = databaseHelper.getVendorcode(code);
              //   vendorList = con.getVendorCodeList( context,userModel.uid);

                 if (vendorList.size() > 0) {
                     vendorCodeList.setVisibility(View.VISIBLE);
                     vendorListAdapter = new VendorListAdapter(DailyReportActivity.this, vendorList);
                     vendorCodeList.setHasFixedSize(true);
                     vendorCodeList.setAdapter(vendorListAdapter);
                     vendorListAdapter.VendorSelection(DailyReportActivity.this);
                 } else {
                     vendorCodeList.setVisibility(View.GONE);
                 }
             }
         });

    }

    public void showGatePassList() {

        gatePassList = databaseHelper.gatePassList();
        if (gatePassList.size() > 0) {
            openGatePassList.setVisibility(View.VISIBLE);
            gatePassListAdapter = new GatePassListAdapter(DailyReportActivity.this, gatePassList);
            openGatePassList.setHasFixedSize(true);
            openGatePassList.setAdapter(gatePassListAdapter);
            gatePassListAdapter.GatePassSelection(this);
        } else {
            openGatePassList.setVisibility(View.GONE);
        }
    }

    public void setAdapter() {
        imageArrayList = new ArrayList<>();
        imageList = new ArrayList<>();

        itemNameList.add("Photo1");
        itemNameList.add("Photo2");
        itemNameList.add("Photo3");
        itemNameList.add("Photo4");
        itemNameList.add("Photo5");
        for (int i = 0; i < itemNameList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setName(itemNameList.get(i));
            imageModel.setImagePath("");
            imageModel.setImageSelected(false);
            imageArrayList.add(imageModel);
        }

        CustomUtility.deleteArrayList(getApplicationContext(),Constant.DailyRoutineImage);
        imageList = CustomUtility.getArrayList(DailyReportActivity.this, Constant.DailyRoutineImage);

        if (imageArrayList.size() > 0 && imageList != null && imageList.size() > 0) {

            for (int j = 0; j < imageList.size(); j++) {
                if (imageList.get(j).isImageSelected()) {
                    ImageModel imageModel = new ImageModel();
                    imageModel.setName(imageList.get(j).getName());
                    imageModel.setImagePath(imageList.get(j).getImagePath());
                    imageModel.setImageSelected(true);
                    imageArrayList.set(j, imageModel);
                    imgCount = j+1;
                }
            }
        }


        customAdapter = new ImageSelectionAdapter(DailyReportActivity.this, imageArrayList);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(customAdapter);
        customAdapter.ImageSelection(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        CheakPermissions();
    }

    private void CheakPermissions() {
        if (!checkPermission()) {
            requestPermission();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.targetDateTxt:
                selectDate();
                break;

            case R.id.submitBtn:
                if (CustomUtility.isInternetOn(getApplicationContext())) {
                      submit();
                } else {
                    CustomUtility.ShowToast(getResources().getString(R.string.ConnectToInternet), getApplicationContext());
                }
                break;
        }
    }


    private void selectDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        try {
                            selectedTargetDate = new SimpleDateFormat(dateFormat2).format(simpleDateFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                            targetDateTxt.setText(simpleDateFormat.format(simpleDateFormat.parse(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year)));
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, mYear, mMonth, mDay);
         datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);//disable previous dates
        datePickerDialog.show();
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
                        selectImage("0");


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
                        selectImage("0");
                    } else {
                        Toast.makeText(this, R.string.all_permission, Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }


    @Override
    public void ImageSelectionListener(ImageModel imageModelList, int position) {
        selectedIndex = position;
        if (imageModelList.isImageSelected()) {
            selectImage("1");
        } else {
            selectImage("0");
        }
    }

    public void selectImage(String value) {

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
                    i_display_image.putExtra("image_path", imageArrayList.get(selectedIndex).getImagePath());
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
                    selectImage("0");
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
        camraLauncher.launch(new Intent(DailyReportActivity.this, CameraActivity.class));
    }

    ActivityResultLauncher<Intent> camraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getExtras() != null) {

                            Bundle bundle = result.getData().getExtras();
                            Log.e("bundle====>", bundle.get("data").toString());

                            photoTxt = bundle.get("data").toString();
                            UpdateArrayList(photoTxt);


                        }

                    }
                }
            });


    private void UpdateArrayList(String path) {

        ImageModel imageModel = new ImageModel();
        imageModel.setName(imageArrayList.get(selectedIndex).getName());
        imageModel.setImagePath(path);
        imageModel.setImageSelected(true);
        imageArrayList.set(selectedIndex, imageModel);
        CustomUtility.saveArrayList(DailyReportActivity.this, imageArrayList, Constant.DailyRoutineImage);

        if(imgCount==0){
            imgCount = 1;
        }else if(imgCount==1){
            imgCount = 2;
        }
        Log.e("imgCount====>", String.valueOf(imgCount));
        customAdapter.notifyDataSetChanged();
    }

    private void submit() {

        /*vendorNameExt ,vendorCodeExt,vendorAddressExt,vendorNumberExt ,responsiblePersonExt ,responsiblePerson2Ext ,responsiblePerson3Ext,
            agendaExt,discussionPointExt*/
        if (vendorNameExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_vendor_name), getApplicationContext());
        } else if (vendorCodeExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_vendor_code), getApplicationContext());
        } else if (vendorAddressExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_vendor_address), getApplicationContext());
        } else if (vendorNumberExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_vendor_contact_number), getApplicationContext());
        } else if (!CustomUtility.isValidMobile(vendorNumberExt.getText().toString())) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_valid_contact_number), getApplicationContext());
        } else if (selectedVisitAt.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.select_visitAt), getApplicationContext());
        } else if (responsiblePersonExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_sap_code), getApplicationContext());
        } else if (agendaExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_agenda), getApplicationContext());
        } else if (discussionPointExt.getText().toString().isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.enter_discussion_points), getApplicationContext());
        } else if (selectedTargetDate.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.select_target_date), getApplicationContext());
        } else if (selectedStatus.isEmpty()) {
            CustomUtility.ShowToast(getResources().getString(R.string.select_StatusFirst), getApplicationContext());
        }  else if (imgCount < 2) {
            CustomUtility.ShowToast(getResources().getString(R.string.select_MinimumImages), getApplicationContext());
        }  else {

            if(selectedVisitAt.equals("Shakti H.O")){
                  if (selectedGatePass.isEmpty()) {
                    CustomUtility.ShowToast(getResources().getString(R.string.selectOpenGatePass), getApplicationContext());
                }else {
                      SubmitDailyReport();
                  }
            }else {
                SubmitDailyReport();
            }

        }
    }

    private void SubmitDailyReport() {
        jsonArray = new JSONArray();
        JSONObject jsonObj = new JSONObject();

        try {
            if (prospectiveVendorRadio.isChecked()) {
                jsonObj.put("pros_vendor", vendorCodeExt.getText().toString().trim());
            } else {
                jsonObj.put("pros_vendor", "");
            }
            if (vendorRadio.isChecked()) {
                jsonObj.put("vendor", vendorCodeExt.getText().toString().trim());
            } else {
                jsonObj.put("vendor", "");
            }
            jsonObj.put("name", vendorNameExt.getText().toString().trim());
            jsonObj.put("addres", vendorAddressExt.getText().toString().trim());
            jsonObj.put("TELF2", vendorNumberExt.getText().toString().trim());
            jsonObj.put("VISIT_AT", selectedVisitAt.trim());

            jsonObj.put("pernr1", responsiblePersonExt.getText().toString().trim());
            jsonObj.put("pernr2", responsiblePerson2Ext.getText().toString().trim());
            jsonObj.put("pernr3", responsiblePerson3Ext.getText().toString().trim());
            jsonObj.put("ACTIVITY", agendaExt.getText().toString().trim());
            jsonObj.put("DISC", discussionPointExt.getText().toString().trim());
            jsonObj.put("TGTDATE", selectedTargetDate);
            jsonObj.put("STATUS", selectedStatus.trim());
            jsonObj.put("name", vendorList.get(vendorPosition).getName1());
            jsonObj.put("STREET", vendorList.get(vendorPosition).getStras());
            jsonObj.put("REGION", vendorList.get(vendorPosition).getOrt01());
            jsonObj.put("CITY1", vendorList.get(vendorPosition).getOrt02());
            jsonObj.put("CONTACT_P", vendorCodeExt.getText().toString().trim());
            jsonObj.put("GATEPASS_NO", selectedGatePass);
            if (imageArrayList.size() > 0) {
                if (imageArrayList.get(0).isImageSelected()) {
                    jsonObj.put("photo1", CustomUtility.getBase64FromBitmap(imageArrayList.get(0).getImagePath()));
                }
                if (1 <= imageArrayList.size() && imageArrayList.get(1).isImageSelected()) {
                    jsonObj.put("photo2", CustomUtility.getBase64FromBitmap(imageArrayList.get(1).getImagePath()));
                }
            }
            jsonArray.put(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("URL=====>", SapUrl.DailyReportAPI + "?final=" + jsonArray.toString());
        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
        param1.add(new BasicNameValuePair("final", String.valueOf(jsonArray)));
        showProgressDialogue();
        try {
            Log.e("SendDataToSap====>", String.valueOf(param1));
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);
            String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.DailyReportAPI, param1);

            if (!obj2.isEmpty()) {

                try {
                    JSONObject jsonObject = new JSONObject(obj2);
                    Log.e("Response=====>", jsonObject.toString());
                    JSONArray jsonArray1 = new JSONArray();
                    jsonArray1 = jsonObject.getJSONArray("data_return");
                    if (jsonArray1.getJSONObject(0).get("return").equals("SUCCESS")) {
                        stopProgressDialogue();
                        CustomUtility.deleteArrayList(getApplicationContext(), Constant.DailyRoutineImage);
                        CustomUtility.ShowToast(getResources().getString(R.string.reportSubmittedSuccessfully), getApplicationContext());
                        onBackPressed();
                    } else {
                        stopProgressDialogue();
                        CustomUtility.ShowToast(getResources().getString(R.string.somethingWentWrong), getApplicationContext());
                    }

                } catch (JSONException e) {
                    stopProgressDialogue();
                    throw new RuntimeException(e);
                }


            }
        } catch (Exception e) {
            stopProgressDialogue();
            e.printStackTrace();
        }
    }

    public void showProgressDialogue() {
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void stopProgressDialogue() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void VendorSelectionListener(VendorListModel.Response vendorList, int position) {
        vendorPosition = position;
        vendorCodeExt.setText(vendorList.getLifnr());
        vendorNameExt.setText(vendorList.getName1());
        vendorAddressExt.setText(vendorList.getAdd());
        vendorNumberExt.setText(vendorList.getTelf1());
        vendorCodeList.setVisibility(View.GONE);
    }


    @Override
    public void GatePassSelecListener(GatePassModel.Response gatePassList) {
        Log.e("SelectedGatePass:- ", gatePassList.getDocno());
        selectedGatePass = gatePassList.getDocno();
    }
}


