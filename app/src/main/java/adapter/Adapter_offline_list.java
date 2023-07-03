package adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Message;
import android.os.StrictMode;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import models.DistanceResponse;
import models.Element;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.OfflineDataConveyance;
import shakti.shakti_employee.bean.LocalConvenienceBean;
import shakti.shakti_employee.bean.LoginBean;
import shakti.shakti_employee.bean.WayPoints;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.GPSTracker;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.utility.DistanceApiClient;
import shakti.shakti_employee.utility.RestUtil;
import shakti.shakti_employee.utility.Utility;


import static android.content.Context.LOCATION_SERVICE;


public class Adapter_offline_list extends RecyclerView.Adapter<Adapter_offline_list.HomeCategoryViewHolder> {
    View.OnClickListener onclick_listener;
    DatabaseHelper db;
    private ProgressDialog progressDialog;
    String fullAddress = null;
    String fullAddress1 = null;
    String distance1 = null;
    String current_end_time, current_end_date;

    private Context context;
    private LocationManager locationManager;

    private ArrayList<LocalConvenienceBean> responseList;
    private CustomUtility customutility = null;
    String lt1 = "";
    String lt2 = "";
    String lg1 = "";
    String lg2 = "";
    String strtpht = "";
    String endpht = "";
    LocalConvenienceBean localConvenienceBean;
    LoginBean lb;
    public static TextView photo2;
    public static String end_photo_text,totalWayPoint="";
    public static boolean end_photo_flag = false;


    public Adapter_offline_list(Context context, ArrayList<LocalConvenienceBean> responseList) {
        this.context = context;
        this.responseList = responseList;
        customutility = new CustomUtility();
        lb = new LoginBean();
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        progressDialog = new ProgressDialog(context);
        db = new DatabaseHelper(context);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_offline_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        try {

            if (!TextUtils.isEmpty(responseList.get(position).getBegda())) {

                holder.start_date.setText(responseList.get(position).getBegda());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getEndda())) {

                holder.end_date.setText(responseList.get(position).getEndda());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getFrom_time())) {

                holder.start_time.setText(responseList.get(position).getFrom_time());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getTo_time())) {

                holder.end_time.setText(responseList.get(position).getTo_time());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getFrom_lat())) {

                holder.start_lat.setText(responseList.get(position).getFrom_lat()+","+responseList.get(position).getFrom_lng());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getTo_lat())) {

                holder.end_lat.setText(responseList.get(position).getTo_lat()+","+responseList.get(position).getTo_lng());

            }



            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (CustomUtility.isOnline(context)) {

                            startLocationUpdates1(responseList.get(position));

                        } else {
                            if (progressDialog != null)
                                if ((progressDialog != null) && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }
                            ;
                            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        buildAlertMessageNoGps1(responseList.get(position));
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView start_date, end_date, start_time, end_time, start_lat, end_lat, start_lng, end_lng;

        CardView cardView;

        public HomeCategoryViewHolder(View itemView) {
            super(itemView);

            start_date = itemView.findViewById(R.id.start_date);
            end_date = itemView.findViewById(R.id.end_date);
            start_time = itemView.findViewById(R.id.start_time);
            end_time = itemView.findViewById(R.id.end_time);
            start_lat = itemView.findViewById(R.id.start_lat);
            end_lat = itemView.findViewById(R.id.end_lat);
            start_lng = itemView.findViewById(R.id.start_lng);
            end_lng = itemView.findViewById(R.id.end_lng);


            cardView = itemView.findViewById(R.id.card_view);

        }
    }


    private void startLocationUpdates1(LocalConvenienceBean localConvenienceBean) {


        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }


        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {

            if (CustomUtility.isOnline(context)) {
                progressDialog = ProgressDialog.show(context, "Loading...", "Please wait !");


                getDistanceInfo(localConvenienceBean);
            } else {

                Toast.makeText(context, "Please Connect to Internet...,Your Data is Saved to the Offline Mode.", Toast.LENGTH_SHORT).show();

            }


        } else {
            gps.showSettingsAlert();
        }

    }


    private void buildAlertMessageNoGps1(LocalConvenienceBean localConvenienceBean) {


        if (CustomUtility.isOnline(context)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Please turn on the GPRS and keep it on while traveling on tour/trip.")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                            //getGpsLocation1();
                            startLocationUpdates1(localConvenienceBean);


                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void getDistanceInfo(LocalConvenienceBean localConvenience) {
        // http://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY
        Map<String, String> mapQuery = new HashMap<>();
        WayPoints wayPoints = db.getWayPointsData(localConvenience.getBegda(),localConvenience.getFrom_time());

        String Waypoint = wayPoints.getWayPoints();
        String wp = Waypoint.replaceAll("%3A", ":");
        wp = wp.replaceAll("%2C", ",");
        wp = wp.replaceAll("%7C", "|");
        String[] json = wp.split("\\|");



        if (json.length > 20) {
            double position = (double) json.length /8;
            position = position*2 ;

            int pos = (int) position;
            Log.e("position1=====>", String.valueOf(position));
            Log.e("pos=====>", String.valueOf(Math.round(pos)));

            for (int i = 0; i <= json.length; i++) {
                if(i!=0 && i!=json.length-1) {
                    if (totalWayPoint.isEmpty()) {
                        if (!totalWayPoint.contains(json[pos * i])) {

                            totalWayPoint = json[pos * i];
                            Log.e("positi====>", String.valueOf(i) + "=====>" + json[pos * i]);
                        }

                    } else {
                        if (pos * i < json.length) {
                            if (!totalWayPoint.contains(json[pos * i])) {
                                totalWayPoint = totalWayPoint + "|" + json[pos * i];
                                Log.e("positi====>", String.valueOf(i) + "=====>" + json[pos * i]);
                            }
                        }
                    }
                }
            }


        } else {
            for (int i = 0; i <= json.length; i++) {
                if (totalWayPoint.isEmpty()) {
                    if (!totalWayPoint.contains(json[i])) {
                        totalWayPoint = json[i];
                    }
                } else {
                    if (i < json.length) {
                        if (!totalWayPoint.contains(json[i])) {

                            totalWayPoint = totalWayPoint + "|" + json[i];
                        }
                    }
                }
            }
        }

        Log.e("json", Arrays.toString(json));
        Log.e("totalWayPoint", totalWayPoint);

        mapQuery.put("origin", Double.parseDouble(localConvenience.getFrom_lat()) + "," + Double.parseDouble(localConvenience.getFrom_lng()));
        mapQuery.put("destination", Double.parseDouble(localConvenience.getTo_lat()) + "," + Double.parseDouble(localConvenience.getTo_lng()));
        mapQuery.put("waypoints", totalWayPoint);
        mapQuery.put("units", "metric");
        mapQuery.put("mode", "driving");
        mapQuery.put("key", context.getResources().getString(R.string.google_API_KEY));

        DistanceApiClient client = RestUtil.getInstance().getRetrofit().create(DistanceApiClient.class);

        Call<DistanceResponse> call = client.getDistanceInfo(mapQuery);
        call.enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(@NonNull Call<DistanceResponse> call, @NonNull Response<DistanceResponse> response) {
                if (response.body() != null &&
                        response.body().getRoutes() != null &&
                        response.body().getRoutes().size() > 0 &&
                        response.body().getRoutes().get(0) != null &&
                        response.body().getRoutes().get(0).getLegs() != null &&
                        response.body().getRoutes().get(0).getLegs().size() > 0 &&
                        response.body().getRoutes().get(0).getLegs().get(0) != null &&
                        response.body().getRoutes().get(0).getLegs().get(0).getDistance() != null &&
                        response.body().getRoutes().get(0).getLegs().get(0).getDuration() != null) {

                    try {

                        if (progressDialog != null)
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }



                        if(response.body().getRoutes().get(0).getLegs().get(0).getStartAddress()!=null && !response.body().getRoutes().get(0).getLegs().get(0).getStartAddress().isEmpty()) {
                            fullAddress = response.body().getRoutes().get(0).getLegs().get(0).getStartAddress();
                        }else {
                            fullAddress = Utility.retrieveAddress(localConvenience.getFrom_lat() ,localConvenience.getFrom_lng(),context);
                        }
                        if(response.body().getRoutes().get(0).getLegs().get(0).getEndAddress()!=null && !response.body().getRoutes().get(0).getLegs().get(0).getEndAddress().isEmpty()) {
                            fullAddress1 = response.body().getRoutes().get(0).getLegs().get(0).getEndAddress();
                        }else {
                            fullAddress1 = Utility.retrieveAddress(localConvenience.getTo_lat() ,localConvenience.getTo_lng(),context);
                        }

                        distance1 = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getText();


                        localConvenienceBean = db.getLocalConvinienceData();

                        strtpht = localConvenienceBean.getPhoto1();
                        endpht = localConvenienceBean.getPhoto2();



                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.custom_dialog2);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.getWindow().setAttributes(lp);

                        final TextInputEditText etstrdt = dialog.findViewById(R.id.tiet_str_dt);
                        final TextInputEditText etstrlatlng = dialog.findViewById(R.id.tiet_str_lat_lng);
                        final TextInputEditText etstrlocadd = dialog.findViewById(R.id.tiet_str_loc_add);
                        final TextInputEditText etenddt = dialog.findViewById(R.id.tiet_end_dt);
                        final TextInputEditText etendlatlng = dialog.findViewById(R.id.tiet_end_lat_lng);
                        final TextInputEditText etendlocadd = dialog.findViewById(R.id.tiet_end_loc_add);
                        final TextInputEditText ettotdis = dialog.findViewById(R.id.tiet_tot_dis);
                        final TextInputEditText ettrvlmod = dialog.findViewById(R.id.tiet_trvl_mod);
                        final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                        final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);
                        final TextView ettxt1 = dialog.findViewById(R.id.txt1);
                        final TextView ettxt2 = dialog.findViewById(R.id.txt2);
                        photo2 = dialog.findViewById(R.id.photo2);
                        ettrvlmod.requestFocus();

                        etstrdt.setText(localConvenience.getBegda() + " " + localConvenience.getFrom_time() );
                        etstrlatlng.setText(localConvenience.getFrom_lat() + " " + localConvenience.getFrom_lng() );
                        etenddt.setText(localConvenience.getEndda() + " " + localConvenience.getTo_time() );
                        etendlatlng.setText(localConvenience.getTo_lat() + " " + localConvenience.getTo_lng() );
                        etstrlocadd.setText(fullAddress);
                        etendlocadd.setText(fullAddress1);
                        ettotdis.setText(distance1);

                        ettxt1.setText("Local Conveyance Details");
                        ettxt2.setText("Press Confirm will end your Journey");

                        etcncl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        photo2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (end_photo_text == null || end_photo_text.isEmpty()) {

                                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO2");
                                }
                            }
                        });

                        etconfm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final String travel_mode = ettrvlmod.getText().toString();

                                if (CustomUtility.isOnline(context)) {

                                    if (!TextUtils.isEmpty(travel_mode) && !travel_mode.equals("")) {
                                        progressDialog = ProgressDialog.show(context, "", "Sending Data to server..please wait !");
                                        new Thread(new Runnable() {
                                            public void run() {
                                                ((Activity) context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(LoginBean.getUseid(), localConvenience.getBegda(),
                                                                localConvenience.getEndda(),
                                                                localConvenience.getFrom_time(),
                                                                localConvenience.getTo_time(),
                                                                localConvenience.getFrom_lat(),
                                                                localConvenience.getTo_lat(),
                                                                localConvenience.getFrom_lng(),
                                                                localConvenience.getTo_lng(),
                                                                fullAddress,
                                                                fullAddress1,
                                                                distance1,
                                                                strtpht,
                                                                endpht);

                                                        db.updateLocalconvenienceData(localConvenienceBean);
                                                        WayPoints  wayPoints = db.getWayPointsData(localConvenienceBean.getBegda(), localConvenienceBean.getFrom_time());
                                                        WayPoints wp = new WayPoints(LoginBean.getUseid(), localConvenience.getBegda(),
                                                                localConvenience.getEndda(),
                                                                localConvenience.getFrom_time(),
                                                                localConvenience.getTo_time(),wayPoints.getWayPoints());
                                                        db.updateWayPointData1(wp);
                                                        SyncLocalConveneinceDataToSap(travel_mode, localConvenience);
                                                    }
                                                });
                                            }
                                        }).start();

                                        dialog.dismiss();

                                    } else {
                                        Toast.makeText(context, "Please Enter Travel Mode.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        dialog.show();


                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                        if (progressDialog != null)
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        ;
                    }
                }
            }

            @Override
            public void onFailure(Call<DistanceResponse> call, Throwable t) {

                if (progressDialog != null)
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                ;

            }
        });
    }

    public void showConfirmationGallery(final String keyimage, final String name) {

        final CustomUtility customUtility = new CustomUtility();

        final CharSequence[] items = {"Take Photo", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    OfflineDataConveyance.openCamera();
                    setFlag(keyimage);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    public void setFlag(String key) {
        end_photo_flag = false;

        switch (key) {

            case DatabaseHelper.KEY_PHOTO2:
                end_photo_flag = true;
                break;

        }

    }

    public void SyncLocalConveneinceDataToSap(String mode, LocalConvenienceBean localConvenienceBean) {

        String docno_sap = null;
        String invc_done = null;

        DatabaseHelper db = new DatabaseHelper(this.context);

        LocalConvenienceBean param_invc = new LocalConvenienceBean();

        param_invc = db.getLocalConvinienceData( localConvenienceBean.getEndda(), localConvenienceBean.getTo_time());

        JSONArray ja_invc_data = new JSONArray();

        JSONObject jsonObj = new JSONObject();
        totalWayPoint = totalWayPoint.replaceAll("via:","");
        try {

            jsonObj.put("pernr", param_invc.getPernr());
            jsonObj.put("begda", param_invc.getBegda());
            jsonObj.put("endda", param_invc.getEndda());
            jsonObj.put("lat_long111", totalWayPoint);
            jsonObj.put("start_time", param_invc.getFrom_time());
            jsonObj.put("end_time", param_invc.getTo_time());
            jsonObj.put("start_lat", param_invc.getFrom_lat());
            jsonObj.put("end_lat", param_invc.getTo_lat());
            jsonObj.put("start_long", param_invc.getFrom_lng());
            jsonObj.put("end_long", param_invc.getTo_lng());
            jsonObj.put("distance", param_invc.getDistance());
            jsonObj.put("TRAVEL_MODE", mode);

            if (param_invc.getStart_loc() != null && !param_invc.getStart_loc().isEmpty()) {
                jsonObj.put("start_location", param_invc.getStart_loc());
            } else {
                jsonObj.put("start_location", Utility.retrieveAddress(param_invc.getFrom_lat(), param_invc.getFrom_lng(), context));
            }

            if (param_invc.getEnd_loc() != null && !param_invc.getEnd_loc().isEmpty()) {
                jsonObj.put("end_location", param_invc.getEnd_loc());
            } else {
                jsonObj.put("end_location", Utility.retrieveAddress(param_invc.getTo_lat(), param_invc.getTo_lng(), context));
            }
            if (param_invc.getPhoto1() != null && !param_invc.getPhoto1().isEmpty()) {
                jsonObj.put("PHOTO1", Utility.getBase64FromBitmap(context, param_invc.getPhoto1()));
            } else {
                jsonObj.put("PHOTO1", "");
            }

            if (param_invc.getPhoto2() != null && !param_invc.getPhoto2().isEmpty()) {
                jsonObj.put("PHOTO2", Utility.getBase64FromBitmap(context, param_invc.getPhoto2()));
            } else {
                jsonObj.put("PHOTO2", "");
            }


            ja_invc_data.put(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }

        final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
        param1_invc.add(new BasicNameValuePair("travel_distance", String.valueOf(ja_invc_data)));

      try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);

            String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.LOCAL_CONVENIENVCE, param1_invc);

            if (!obj2.isEmpty()) {

                JSONArray ja = new JSONArray(obj2);

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);

                    invc_done = jo.getString("msgtyp");
                    docno_sap = jo.getString("msg");

                    if (invc_done.equalsIgnoreCase("S")) {

                        ((Activity) context).finish();
                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        ;
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);
                        db.deleteLocalconvenienceDetail1(localConvenienceBean.getEndda(), localConvenienceBean.getTo_time());
                        db.deleteWayPointsDetail1(localConvenienceBean.getEndda(), localConvenienceBean.getTo_time());

                    } else if (invc_done.equalsIgnoreCase("E")) {
                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            ;
        }
    }


    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();
        }
    };





}