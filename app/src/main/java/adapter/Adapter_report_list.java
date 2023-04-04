package adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.TravelTripExpensesActivity;
import shakti.shakti_employee.bean.TravelTripDomDocBean;
import shakti.shakti_employee.bean.TravelTripExpDocBean;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.utility.NotifyInterface;
import shakti.shakti_employee.utility.Utility;


public class Adapter_report_list extends RecyclerView.Adapter<adapter.Adapter_report_list.HomeCategoryViewHolder> {

    final Adapter_report_list adapter_report_list = this;
    String taxcode = "", start = "", end = "", loc = "", loc_txt = "", coun = "", reinr = "", status = "", pernr = "", obj = "";
    DatabaseHelper dataHelper = null;
    TravelTripExpDocBean travelTripExpDocBean;
    TravelTripDomDocBean travelTripDomDocBean;
    private ArrayList<JSONArray> jsonObject;
    private ArrayList<JSONArray> jsonObject1;
    private Context context;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();
            adapter_report_list.notifyDataSetChanged();
        }
    };

    private ProgressDialog progressDialog;
    private LoggedInUser userModel;
    private int count, count123, len;
    private String mFromAddress, mToAddress, mExpenses, mAmount, mCurrency, mTaxcode, mDescription, mLocation1, mRegion, mReinr, mGSTIN, mCurrText, mExpText, mZland1, mLand1, mStartDate, mEndDate, mLocation, mCountry, mType, mUserID, mSerialNo;
    private String count1, from;
    private NotifyInterface notifyInterface;


    public Adapter_report_list(Context context, int count, String count1, ArrayList<JSONArray> jsonObject, ArrayList<JSONArray> jsonObject1, String from, NotifyInterface notifyInterface) {
        this.context = context;
        this.jsonObject = jsonObject;
        this.jsonObject1 = jsonObject1;
        this.count = count;
        this.count1 = count1;
        this.from = from;
        this.notifyInterface = notifyInterface;

        userModel = new LoggedInUser(context);
        dataHelper = new DatabaseHelper(context);

        userModel = new LoggedInUser(context);
        Log.e("uidd", "" + userModel.uid);

        mUserID = userModel.uid;

        Utility.setSharedPreference(context, "count", count1);


    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_report_list, parent, false);

        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {

        count123 = Integer.parseInt(count1);

        if (from.equalsIgnoreCase("req")) {
            holder.butn_txt.setVisibility(View.VISIBLE);
            holder.butn_txt1.setVisibility(View.GONE);
            holder.edit.setText("EDIT");
            holder.cmplt.setText("COMPLETE");
        } else {
            holder.butn_txt.setVisibility(View.GONE);
            holder.butn_txt1.setVisibility(View.VISIBLE);
            holder.edit1.setText("VIEW");
            //holder.cmplt.setText("COMPLETE");
        }

        try {

            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("reinr"))) {

                holder.trip_no.setText(jsonObject.get(position).getJSONObject(position).optString("reinr"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("datv1_char"))) {

                holder.from_date.setText(jsonObject.get(position).getJSONObject(position).optString("datv1_char"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("datb1_char"))) {

                holder.to_date.setText(jsonObject.get(position).getJSONObject(position).optString("datb1_char"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("zort1"))) {

                holder.trip_loc.setText(jsonObject.get(position).getJSONObject(position).optString("zort1"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("trip_total"))) {

                holder.cost.setText(jsonObject.get(position).getJSONObject(position).optString("trip_total"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("currency"))) {

                holder.currency.setText(jsonObject.get(position).getJSONObject(position).optString("currency"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("antrg_txt"))) {

                holder.status.setText(jsonObject.get(position).getJSONObject(position).optString("antrg_txt"));

            }

            ((HomeCategoryViewHolder) holder).edit_txt.setTag(position);
            ((HomeCategoryViewHolder) holder).edit_txt1.setTag(position);
            ((HomeCategoryViewHolder) holder).cmplt_txt.setTag(position);


            ((HomeCategoryViewHolder) holder).edit_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) view.getTag();

                    dataHelper.deleteDomTravelData1();
                    dataHelper.deleteExpTravelData1();

                    try {
                        reinr = jsonObject.get(pos).getJSONObject(pos).optString("reinr");
                        taxcode = jsonObject1.get(pos).getJSONObject(pos).optString("tax_code");

                        start = jsonObject.get(pos).getJSONObject(pos).optString("datv1_char");
                        end = jsonObject.get(pos).getJSONObject(pos).optString("datb1_char");
                        loc = jsonObject.get(pos).getJSONObject(pos).optString("zort1");
                        coun = jsonObject.get(pos).getJSONObject(pos).optString("zland");
                        loc_txt = jsonObject.get(pos).getJSONObject(pos).optString("zland_txt");


                        Log.e("LENGTH!@#", "&&&&&&" + len);
                        saveData(reinr);
                        Intent i = new Intent(context, TravelTripExpensesActivity.class);
                        i.putExtra("tax", taxcode);
                        i.putExtra("start", start);
                        i.putExtra("end", end);
                        i.putExtra("loc", loc);
                        i.putExtra("coun", coun);
                        i.putExtra("reinr", reinr);
                        i.putExtra("from", from);
                        i.putExtra("loc_txt", loc_txt);
                        context.startActivity(i);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            ((HomeCategoryViewHolder) holder).edit_txt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) view.getTag();

                    dataHelper.deleteDomTravelData1();
                    dataHelper.deleteExpTravelData1();

                    try {
                        reinr = jsonObject.get(pos).getJSONObject(pos).optString("reinr");
                        taxcode = jsonObject1.get(pos).getJSONObject(pos).optString("tax_code");

                        start = jsonObject.get(pos).getJSONObject(pos).optString("datv1_char");
                        end = jsonObject.get(pos).getJSONObject(pos).optString("datb1_char");
                        loc = jsonObject.get(pos).getJSONObject(pos).optString("zort1");
                        coun = jsonObject.get(pos).getJSONObject(pos).optString("zland");
                        loc_txt = jsonObject.get(pos).getJSONObject(pos).optString("zland_txt");


                        Log.e("LENGTH!@#", "&&&&&&" + len);
                        saveData(reinr);
                        Intent i = new Intent(context, TravelTripExpensesActivity.class);
                        i.putExtra("tax", taxcode);
                        i.putExtra("start", start);
                        i.putExtra("end", end);
                        i.putExtra("loc", loc);
                        i.putExtra("coun", coun);
                        i.putExtra("reinr", reinr);
                        i.putExtra("from", from);
                        i.putExtra("loc_txt", loc_txt);
                        context.startActivity(i);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


            ((HomeCategoryViewHolder) holder).cmplt_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) view.getTag();
                    try {
                        reinr = jsonObject.get(pos).getJSONObject(pos).optString("reinr");

                        pernr = mUserID;

                        status = "C";

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    GetCompleteList_Task(pernr, reinr, status, notifyInterface, pos);
                    //notifyDataSetChanged();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonObject.size();
    }

    public void saveData(String reinr) {


        dataHelper.deleteDomTravelData1();
        dataHelper.deleteExpTravelData1();
        try {

            for (int i = 0; i < count123; i++) {

                mUserID = userModel.uid;
                mSerialNo = String.valueOf(i + 1);

                mFromAddress = jsonObject1.get(i).getJSONObject(i).optString("from_date1");
                mToAddress = jsonObject1.get(i).getJSONObject(i).optString("to_date1");
                mExpenses = jsonObject1.get(i).getJSONObject(i).optString("exp_type");

                mCurrency = jsonObject1.get(i).getJSONObject(i).optString("rec_curr");
                if (coun.equalsIgnoreCase("IN")) {
                    mTaxcode = jsonObject1.get(i).getJSONObject(i).optString("tax_code");
                } else {
                    mTaxcode = "";
                }


                mLocation1 = jsonObject1.get(i).getJSONObject(i).optString("location");

                mReinr = jsonObject1.get(i).getJSONObject(i).optString("reinr");
                mAmount = jsonObject1.get(i).getJSONObject(i).optString("rec_amount");
                mDescription = jsonObject1.get(i).getJSONObject(i).optString("descript");
                mGSTIN = jsonObject1.get(i).getJSONObject(i).optString("p_doc");
                mCurrText = jsonObject1.get(i).getJSONObject(i).optString("rec_curr_txt");
                mExpText = jsonObject1.get(i).getJSONObject(i).optString("exp_type_txt");
                mZland1 = jsonObject1.get(i).getJSONObject(i).optString("land1_txt");
                mLand1 = jsonObject1.get(i).getJSONObject(i).optString("land1");

                if (coun.equalsIgnoreCase("IN")) {
                    mRegion = jsonObject1.get(i).getJSONObject(i).optString("region");
                } else {
                    mRegion = mLand1 + " " + mZland1;
                }
                if (coun.equalsIgnoreCase("IN")) {

                    mType = "D";
                    travelTripDomDocBean = new TravelTripDomDocBean(mUserID, mSerialNo,
                            mStartDate,
                            mEndDate,
                            mCountry,
                            mLocation,
                            mExpenses,
                            mAmount,
                            mCurrency,
                            mTaxcode,
                            mFromAddress,
                            mToAddress,
                            mRegion,
                            mDescription,
                            mLocation1,
                            mGSTIN,
                            mReinr,
                            mType, mCurrText, mZland1, mExpText);


                    if (dataHelper.isRecordExist(dataHelper.TABLE_TRAVEL_DOM_EXPENSES1, dataHelper.SERIALNO, mSerialNo)) {
                        dataHelper.updateDOMTravelTripEntryDocument1(travelTripDomDocBean, mSerialNo);
                    } else {
                        dataHelper.insertDOMTravelTripEntryDocument1(travelTripDomDocBean, mUserID, reinr);
                    }

                } else {
                    mType = "E";
                    travelTripExpDocBean = new TravelTripExpDocBean(mUserID, mSerialNo,
                            mStartDate,
                            mEndDate,
                            mCountry,
                            mLocation,
                            mExpenses,
                            mAmount,
                            mCurrency,
                            mTaxcode,
                            mFromAddress,
                            mToAddress,
                            mRegion,
                            mDescription,
                            mLocation1,
                            mGSTIN,
                            mReinr,
                            mType, mCurrText, mZland1, mExpText);

                    if (dataHelper.isRecordExist(dataHelper.TABLE_TRAVEL_EXP_EXPENSES1, dataHelper.SERIALNO, mSerialNo)) {
                        dataHelper.updateEXPTravelTripEntryDocument1(travelTripExpDocBean, mSerialNo);
                    } else {
                        dataHelper.insertEXPTravelTripEntryDocument1(travelTripExpDocBean, mUserID, reinr);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void GetCompleteList_Task(final String pernr, final String reinr, final String status, final NotifyInterface notifyInterface, final int pos) {

        try {

            progressDialog = ProgressDialog.show(context, "Loading...", "please wait !");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

                    param.add(new BasicNameValuePair("pernr", pernr));
                    param.add(new BasicNameValuePair("reinr", reinr));
                    param.add(new BasicNameValuePair("status", status));
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);

                        obj = CustomHttpClient.executeHttpPost1(SapUrl.TRIP_COMPLETE_APPROVE, param);
                        Log.d("login", param.toString());
                        Log.d("login_obj", obj);

                        if (obj != "") {

                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Message msg2 = new Message();
                            JSONObject jo_success = new JSONObject(obj);
                            Log.e("DATA", "&&&&" + jo_success.toString());
                            final JSONArray ja_success = jo_success.getJSONArray("status_change");


                            for (int i = 0; i < ja_success.length(); i++) {
                                JSONObject jo_gp_app = ja_success.getJSONObject(i);

                                String msg = jo_gp_app.getString("msgtyp");

                                if (msg.equalsIgnoreCase("S")) {

                                    jsonObject.remove(pos);

                                    msg2.obj = "Trip Completed Sucessfully";
                                    mHandler.sendMessage(msg2);

                                } else {
                                    msg2.obj = "Trip not Completed, Please try Again...";
                                    mHandler.sendMessage(msg2);


                                }

                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                    }
//
                }
            }).start();


        } catch (Exception e) {
            Log.d("msg", "" + e);
            if (progressDialog != null)
                progressDialog.dismiss();
        }

    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView trip_no, from_date, to_date, trip_loc, cost, currency, status, edit, cmplt, edit1;
        LinearLayout butn_txt, butn_txt1;
        CardView cardView, edit_txt, edit_txt1, cmplt_txt;

        public HomeCategoryViewHolder(View itemView) {
            super(itemView);

            trip_no = (TextView) itemView.findViewById(R.id.trip_no);
            from_date = (TextView) itemView.findViewById(R.id.from_date);
            to_date = (TextView) itemView.findViewById(R.id.to_date);
            trip_loc = (TextView) itemView.findViewById(R.id.trip_loc);
            cost = (TextView) itemView.findViewById(R.id.cost);
            currency = (TextView) itemView.findViewById(R.id.currency);
            status = (TextView) itemView.findViewById(R.id.status);
            edit = (TextView) itemView.findViewById(R.id.edit);
            edit1 = (TextView) itemView.findViewById(R.id.edit1);
            cmplt = (TextView) itemView.findViewById(R.id.cmplt);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            edit_txt = (CardView) itemView.findViewById(R.id.edit_txt);
            edit_txt1 = (CardView) itemView.findViewById(R.id.edit_txt1);
            cmplt_txt = (CardView) itemView.findViewById(R.id.cmplt_txt);
            butn_txt = (LinearLayout) itemView.findViewById(R.id.butn_txt);
            butn_txt1 = (LinearLayout) itemView.findViewById(R.id.butn_txt1);


        }
    }


}