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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import models.DistanceResponse;
import models.Element;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shakti.shakti_employee.R;
import shakti.shakti_employee.bean.LocalConvenienceBean;
import shakti.shakti_employee.bean.LoginBean;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.GPSTracker;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.utility.DistanceApiClient;
import shakti.shakti_employee.utility.RestUtil;


import static android.content.Context.LOCATION_SERVICE;


public class Adapter_offline_list extends RecyclerView.Adapter<Adapter_offline_list.HomeCategoryViewHolder> {
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
    String lt1= "";
    String lt2= "";
    String lg1= "";
    String lg2= "";

    LoginBean lb;


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

                holder.start_date.setText(CustomUtility.formateDate(responseList.get(position).getBegda()));

            }

            if (!TextUtils.isEmpty(responseList.get(position).getEndda())) {

                holder.end_date.setText(CustomUtility.formateDate(responseList.get(position).getEndda()));

            }
            if (!TextUtils.isEmpty(responseList.get(position).getFrom_time())) {

                holder.start_time.setText(CustomUtility.formateTime(responseList.get(position).getFrom_time()));

            }
            if (!TextUtils.isEmpty(responseList.get(position).getTo_time())) {

                holder.end_time.setText(CustomUtility.formateTime(responseList.get(position).getTo_time()));

            }
            if (!TextUtils.isEmpty(responseList.get(position).getFrom_lat())) {

                holder.start_lat.setText(responseList.get(position).getFrom_lng());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getTo_lat())) {

                holder.end_lat.setText(responseList.get(position).getTo_lng());

            }
          /*  if (!TextUtils.isEmpty(responseList.get(position).getFrom_lng())) {

                holder.start_lng.setText(responseList.get(position).getTo_lng());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getTo_lng())) {

                holder.end_lng.setText(responseList.get(position).getTo_lng());

            }*/


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        //Toast.makeText(context, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
                        if (CustomUtility.isInternetOn(context)) {
                            String start_lat,start_lng,end_lat,end_lng,start_dat,start_tm,end_dat,end_tm;



                            start_lat = responseList.get(position).getFrom_lat();
                            start_lng = responseList.get(position).getFrom_lng();
                            end_lat   = responseList.get(position).getTo_lat();
                            end_lng   = responseList.get(position).getTo_lng();

                            start_dat = responseList.get(position).getBegda();
                            start_tm = responseList.get(position).getFrom_time();
                            end_dat   = responseList.get(position).getEndda();
                            end_tm   = responseList.get(position).getTo_time();


                        } else {
                            if (progressDialog != null)
                                if ((progressDialog != null) && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                };
                            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        String start_lat,start_lng,end_lat,end_lng,start_dat,start_tm,end_dat,end_tm;

                        start_lat = responseList.get(position).getFrom_lat();
                        start_lng = responseList.get(position).getFrom_lng();
                        end_lat   = responseList.get(position).getTo_lat();
                        end_lng   = responseList.get(position).getTo_lng();

                        start_dat = responseList.get(position).getBegda();
                        start_tm = responseList.get(position).getFrom_time();
                        end_dat   = responseList.get(position).getEndda();
                        end_tm   = responseList.get(position).getTo_time();

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

        TextView start_date, end_date,start_time,end_time,start_lat,end_lat,start_lng,end_lng;

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



    public void SyncLocalConveneinceDataToSap(String mode, String endat, String endtm) {

        String docno_sap = null;
        String invc_done = null;

        DatabaseHelper db = new DatabaseHelper(this.context);

        LocalConvenienceBean param_invc = new LocalConvenienceBean();

        //param_invc = db.getLocalConvinienceData(endat,endtm);

        JSONArray ja_invc_data = new JSONArray();

        JSONObject jsonObj = new JSONObject();

        try {


            jsonObj.put("pernr", param_invc.getPernr());
            jsonObj.put("begda", CustomUtility.formateDate1(param_invc.getBegda()));
            jsonObj.put("endda", CustomUtility.formateDate1(param_invc.getEndda()));
            jsonObj.put("start_time",CustomUtility.formateTime1(param_invc.getFrom_time()));
            jsonObj.put("end_time", CustomUtility.formateTime1(param_invc.getTo_time()));
            jsonObj.put("start_lat", param_invc.getFrom_lat());
            jsonObj.put("end_lat", param_invc.getTo_lat());
            jsonObj.put("start_long", param_invc.getFrom_lng());
            jsonObj.put("end_long", param_invc.getTo_lng());
            jsonObj.put("start_location", param_invc.getStart_loc());
            jsonObj.put("end_location", param_invc.getEnd_loc());
            jsonObj.put("distance",  param_invc.getDistance());
            jsonObj.put("TRAVEL_MODE",  mode);

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

            if (obj2 != "") {

                JSONArray ja = new JSONArray(obj2);

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);

                    invc_done = jo.getString("msgtyp");
                    docno_sap = jo.getString("msg");

                    if (invc_done.equalsIgnoreCase("S")) {

                        ((Activity)context).finish();
                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);
                       // db.deleteLocalconvenienceDetail(endat,endtm);



                    } else if (invc_done.equalsIgnoreCase("E")) {
                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
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
            };
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