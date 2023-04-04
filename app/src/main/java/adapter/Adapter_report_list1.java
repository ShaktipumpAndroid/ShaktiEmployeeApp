package adapter;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.TravelTripExpensesActivity;
import shakti.shakti_employee.bean.TravelHeadBean;
import shakti.shakti_employee.bean.TravelTripDomDocBean;
import shakti.shakti_employee.bean.TravelTripExpDocBean;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.utility.NotifyInterface;
import shakti.shakti_employee.utility.Utility;


public class Adapter_report_list1 extends ArrayAdapter<TravelHeadBean> {

    final Adapter_report_list1 adapter_report_list1 = this;
    String taxcode = "", start = "", end = "", loc = "", loc_txt = "", coun = "", reinr = "", status = "", pernr = "", obj = "";
    DatabaseHelper dataHelper = null;
    TravelTripExpDocBean travelTripExpDocBean;
    TravelTripDomDocBean travelTripDomDocBean;
    DatabaseHelper db;
    ArrayList<TravelHeadBean> arrayList;
    ArrayList<TravelHeadBean> travelHeadBeanList;
    TextView trip_no, from_date, to_date, trip_loc, cost, currency, status1, pernr_no, edit, cmplt, edit1;
    LinearLayout butn_txt, butn_txt1;
    CardView cardView, edit_txt, edit_txt1, cmplt_txt;
    LinearLayout pernr_txt;
    NotifyInterface notifyInterface;
    private Context context;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();
            adapter_report_list1.notifyDataSetChanged();
        }
    };
    private ArrayList<JSONArray> jsonObject;
    private ArrayList<JSONArray> jsonObject1;
    private ProgressDialog progressDialog;
    private LoggedInUser userModel;
    private int count, count123, count1;
    // private List<TravelHeadBean> travelHeadBeanList = null;
    private String mFromAddress, mToAddress, mExpenses, mAmount, mCurrency, mTaxcode, mDescription, mLocation1, mRegion, mReinr, mGSTIN, mCurrText, mExpText, mLand1, mZland1, mStartDate, mEndDate, mLocation, mCountry, mType, mUserID, mSerialNo;
    private String from;

    public Adapter_report_list1(Context context, int count, int count1, ArrayList<TravelHeadBean> arrayList, ArrayList<JSONArray> jsonObject, ArrayList<JSONArray> jsonObject1, String from, NotifyInterface notifyInterface) {


        super(context, R.layout.adpter_report_list, arrayList);
        this.context = context;
        this.jsonObject = jsonObject;
        this.jsonObject1 = jsonObject1;
        this.count = count;
        this.arrayList = arrayList;
        this.count1 = count1;
        this.from = from;
        this.notifyInterface = notifyInterface;

        this.travelHeadBeanList = new ArrayList<TravelHeadBean>();
        this.travelHeadBeanList.addAll(arrayList);

        userModel = new LoggedInUser(context);
        dataHelper = new DatabaseHelper(context);

        userModel = new LoggedInUser(context);
        Log.e("SIZE", "" + arrayList.size());

        mUserID = userModel.uid;

        Utility.setSharedPreference(context, "count", String.valueOf(count1));

    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public TravelHeadBean getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, @NonNull final ViewGroup parent) {

        db = new DatabaseHelper(context);

        // Get the data item for this position
        TravelHeadBean travelHeadBean = getItem(position);

        count123 = count1;

        if (view == null) {

            view = LayoutInflater.from(getContext()).inflate(R.layout.adpter_report_list, parent, false);
        }

        trip_no = (TextView) view.findViewById(R.id.trip_no);
        from_date = (TextView) view.findViewById(R.id.from_date);
        to_date = (TextView) view.findViewById(R.id.to_date);
        trip_loc = (TextView) view.findViewById(R.id.trip_loc);
        cost = (TextView) view.findViewById(R.id.cost);
        currency = (TextView) view.findViewById(R.id.currency);
        status1 = (TextView) view.findViewById(R.id.status);
        pernr_no = (TextView) view.findViewById(R.id.pernr_no);
        pernr_txt = (LinearLayout) view.findViewById(R.id.pernr_txt);
        edit = (TextView) view.findViewById(R.id.edit);
        edit1 = (TextView) view.findViewById(R.id.edit1);
        cmplt = (TextView) view.findViewById(R.id.cmplt);
        cardView = (CardView) view.findViewById(R.id.card_view);
        edit_txt = (CardView) view.findViewById(R.id.edit_txt);
        edit_txt1 = (CardView) view.findViewById(R.id.edit_txt1);
        cmplt_txt = (CardView) view.findViewById(R.id.cmplt_txt);
        butn_txt = (LinearLayout) view.findViewById(R.id.butn_txt);
        butn_txt1 = (LinearLayout) view.findViewById(R.id.butn_txt1);

        pernr_txt.setVisibility(View.VISIBLE);

        if (from.equalsIgnoreCase("pen")) {
            butn_txt.setVisibility(View.VISIBLE);
            butn_txt1.setVisibility(View.GONE);
            edit.setText("VIEW");
            cmplt.setText("APPROVE");
        } else {
            butn_txt.setVisibility(View.GONE);
            butn_txt1.setVisibility(View.VISIBLE);
            edit1.setText("VIEW");

        }

        try {

            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("reinr"))) {

                trip_no.setText(jsonObject.get(position).getJSONObject(position).optString("reinr"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("datv1_char"))) {

                from_date.setText(jsonObject.get(position).getJSONObject(position).optString("datv1_char"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("datb1_char"))) {

                to_date.setText(jsonObject.get(position).getJSONObject(position).optString("datb1_char"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("zort1"))) {

                trip_loc.setText(jsonObject.get(position).getJSONObject(position).optString("zort1"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("trip_total"))) {

                cost.setText(jsonObject.get(position).getJSONObject(position).optString("trip_total"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("currency"))) {

                currency.setText(jsonObject.get(position).getJSONObject(position).optString("currency"));

            }
            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("antrg_txt"))) {

                status1.setText(jsonObject.get(position).getJSONObject(position).optString("antrg_txt"));

            }

            if (!TextUtils.isEmpty(jsonObject.get(position).getJSONObject(position).optString("pernr"))) {

                pernr_no.setText(jsonObject.get(position).getJSONObject(position).optString("pernr"));

            }

            edit_txt.setTag(position);
            edit_txt1.setTag(position);
            cmplt_txt.setTag(position);


            edit_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) view.getTag();

                    dataHelper.deleteDomTravelData1();
                    dataHelper.deleteExpTravelData1();

                    try {
                        reinr = jsonObject.get(position).getJSONObject(pos).optString("reinr");
                        taxcode = jsonObject1.get(position).getJSONObject(pos).optString("tax_code");

                        start = jsonObject.get(position).getJSONObject(pos).optString("datv1_char");
                        end = jsonObject.get(position).getJSONObject(pos).optString("datb1_char");
                        loc = jsonObject.get(position).getJSONObject(pos).optString("zort1");
                        coun = jsonObject.get(position).getJSONObject(pos).optString("zland");
                        loc_txt = jsonObject.get(position).getJSONObject(pos).optString("zland_txt");


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

            edit_txt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) view.getTag();

                    dataHelper.deleteDomTravelData1();
                    dataHelper.deleteExpTravelData1();

                    try {
                        reinr = jsonObject.get(position).getJSONObject(pos).optString("reinr");
                        taxcode = jsonObject1.get(position).getJSONObject(pos).optString("tax_code");

                        start = jsonObject.get(position).getJSONObject(pos).optString("datv1_char");
                        end = jsonObject.get(position).getJSONObject(pos).optString("datb1_char");
                        loc = jsonObject.get(position).getJSONObject(pos).optString("zort1");
                        coun = jsonObject.get(position).getJSONObject(pos).optString("zland");
                        loc_txt = jsonObject.get(position).getJSONObject(pos).optString("zland_txt");


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


            cmplt_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) view.getTag();
                    try {
                        reinr = jsonObject.get(position).getJSONObject(pos).optString("reinr");

                        pernr = jsonObject.get(position).getJSONObject(pos).optString("pernr");

                        status = "A";
                        GetApproveList_Task(pernr, reinr, status, notifyInterface, position);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    notifyDataSetChanged();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase();

        arrayList.clear();
        if (charText.length() == 0) {
            arrayList.addAll(travelHeadBeanList);
        } else {
            for (TravelHeadBean cs : travelHeadBeanList) {
                if (cs.getPernr().toLowerCase().contains(charText) ||
                        cs.getReinr().toLowerCase().contains(charText) ||
                        cs.getZort1().toLowerCase().contains(charText) ||
                        cs.getTrip_total().toLowerCase().contains(charText)) {
                    arrayList.add(cs);
                }
            }
        }
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
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
                mRegion = jsonObject1.get(i).getJSONObject(i).optString("region");
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


    private void GetApproveList_Task(final String pernr, final String reinr, final String status, final NotifyInterface notifyInterface, final int position) {

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
//                           Toast.makeText(LoginActivity.this, "you clicked login", Toast.LENGTH_SHORT).show();
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

                                    jsonObject.remove(position);

                                    msg2.obj = "Trip Approve Sucessfully";
                                    mHandler.sendMessage(msg2);

                                } else {

                                    msg2.obj = "Trip not Approve, Please try Again...";
                                    mHandler.sendMessage(msg2);

                                }

                            }
                            adapter_report_list1.notifyDataSetChanged();
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
}