package shakti.shakti_employee.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.Adapter_report_list1;
import shakti.shakti_employee.R;
import shakti.shakti_employee.bean.TravelHeadBean;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.model.TripHeadResponse;
import shakti.shakti_employee.model.TripListResponse;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SapUrl;
import shakti.shakti_employee.utility.NotifyInterface;


/**
 * A simple {@link Fragment} subclass.
 */
public class HODTripPendingFragment extends Fragment implements View.OnClickListener {

    Context context;
    ListView listView;
    View.OnClickListener onclick;
    LinearLayout lin1, lin2;
    Adapter_report_list1 adapterEmployeeList;
    TripHeadResponse[] tripHeadResponses;
    TripListResponse[] tripListResponses;
    DatabaseHelper db = null;
    TravelHeadBean travelHeadBean;
    ArrayList<JSONArray> ja_success = new ArrayList<JSONArray>();
    ArrayList<JSONArray> ja_success1 = new ArrayList<JSONArray>();
    String from;
    int count, count1;
    CardView list;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManagerSubCategory;
    private LoggedInUser userModel;
    private EditText pernr_no;
    private TextView save;
    private String mUserID, type, mPernr, obj;
    private Handler mHandler;


    public HODTripPendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();

        db = new DatabaseHelper(context);

        onclick = this;

        userModel = new LoggedInUser(context);

        type = "3";
        mUserID = userModel.uid;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hodtrip_pend_appro, container, false);

        listView = rootView.findViewById(R.id.emp_list);

        lin1 = rootView.findViewById(R.id.lin1);
        lin2 = rootView.findViewById(R.id.lin2);

        list = rootView.findViewById(R.id.list);

        pernr_no = (EditText) rootView.findViewById(R.id.pernr_no);


        // Capture Text in EditText
        pernr_no.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = pernr_no.getText().toString().toLowerCase();
                try {

                    adapterEmployeeList.filter(text);


                       /* searchArrayList = new ArrayList<TravelHeadBean>();
                        int searchListLength = travelHeadBeanArrayList.size();
                        Log.e("searchSize","&&&"+se)
                        for (int i = 0; i < searchListLength; i++) {

                            if (travelHeadBeanArrayList.get(i).getAntrg_txt().equalsIgnoreCase(text) || travelHeadBeanArrayList.get(i).getDatv1_char().equalsIgnoreCase(text) || travelHeadBeanArrayList.get(i).getDatb1_char().equalsIgnoreCase(text) || travelHeadBeanArrayList.get(i).getPernr().equalsIgnoreCase(text) || travelHeadBeanArrayList.get(i).getReinr().equalsIgnoreCase(text) || travelHeadBeanArrayList.get(i).getTrip_total().equalsIgnoreCase(text) || travelHeadBeanArrayList.get(i).getZort1().equalsIgnoreCase(text) || travelHeadBeanArrayList.get(i).getZland_txt().equalsIgnoreCase(text)) {
                                //Do whatever you want here
                                searchArrayList.add(travelHeadBeanArrayList.get(i));

                                ja_successSearch.add(ja_success.get(i));
                                ja_successSearch1.add(ja_success1.get(i));

                            }
                        }


                        if(searchArrayList.size() > 0)
                        {

                            if(adapterEmployeeList != null)
                                adapterEmployeeList = null;


                            adapterEmployeeList = new Adapter_report_list1(context, count, count1,searchArrayList, ja_successSearch, ja_successSearch1, from, onclick);
                            layoutManagerSubCategory = new LinearLayoutManager(context);
                            layoutManagerSubCategory.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManagerSubCategory);
                            recyclerView.setAdapter(adapterEmployeeList);
                            adapterEmployeeList.notifyDataSetChanged();

                        }*/


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Data not Available", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });


        return rootView;
    }


    @Override
    public void onClick(View view) {

    }


  /*  private void checkDataValtidation() {

        try {

            mPernr = pernr_no.getText().toString().trim();


            if (mPernr == null || mPernr.equalsIgnoreCase("") || mPernr.equalsIgnoreCase(null)) {

                pernr_no.setFocusable(true);
                pernr_no.requestFocus();
                Toast.makeText(context, getResources().getString(R.string.Please_enter_pernr), Toast.LENGTH_SHORT).show();
                if (progressDialog != null)
                    progressDialog.dismiss();

            } else {

                GetTravelPendingList_Task();
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }


    }
*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void GetTravelPendingList_Task() {

        from = "pen";


        try {


            progressDialog = ProgressDialog.show(context, "Loading...", "please wait !");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

                    param.add(new BasicNameValuePair("pernr", mUserID));
                    param.add(new BasicNameValuePair("type", type));
                    try {

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                        StrictMode.setThreadPolicy(policy);
//                           Toast.makeText(LoginActivity.this, "you clicked login", Toast.LENGTH_SHORT).show();
                        obj = CustomHttpClient.executeHttpPost1(SapUrl.TRIP_HOD_EXP_VIEW, param);
                        Log.d("login", param.toString());
                        Log.d("login_obj", obj);

                        if (obj != "") {


                            progressDialog.dismiss();
                            JSONObject jo_success = new JSONObject(obj);

                            Log.e("DATA", "&&&&" + jo_success.toString());
                           /*  ja_success = jo_success.getJSONArray("travel_head");
                             ja_success1 = jo_success.getJSONArray("travel_item"); */

                            count = jo_success.getJSONArray("travel_head").length();
                            count1 = jo_success.getJSONArray("travel_item").length();

                            Log.e("TRAVELHEADSIZE", "%%%%%" + jo_success.getJSONArray("travel_head").length());
                            Log.e("TRAVELITEMSIZE", "%%%%%" + jo_success.getJSONArray("travel_item").length());
                            db.deleteTravelHeadData();

                            for (int i = 0; i < count; i++) {
                                ja_success.add(jo_success.getJSONArray("travel_head"));
                            }

                            for (int i = 0; i < count1; i++) {
                                ja_success1.add(jo_success.getJSONArray("travel_item"));
                            }


                            if (ja_success.size() > 0) {

                                Log.e("DATA", "&&&&" + ja_success.size());


                                //JSONArray jArrAnswer = new JSONArray(ja_success);
                                for (int i = 0; i < ja_success.size(); i++) {

                                    //JSONObject jo = jArrAnswer.getJSONObject(i);

                                    JSONObject jo = ja_success.get(i).getJSONObject(i);

                                    final String link = jo.getString("link");
                                    final String status = jo.getString("antrg_txt");
                                    final String startdate = jo.getString("datv1_char");
                                    final String enddate = jo.getString("datb1_char");
                                    final String triptotal = jo.getString("trip_total");
                                    final String pernr = jo.getString("pernr");
                                    final String reinr = jo.getString("reinr");
                                    final String city = jo.getString("zort1");
                                    final String country_text = jo.getString("zland_txt");


                                    getActivity().runOnUiThread(new Runnable() {
                                        public void run() {

                                            list.setVisibility(View.VISIBLE);
                                            lin2.setVisibility(View.GONE);


                                            travelHeadBean = new TravelHeadBean(link, status, startdate, enddate, triptotal, pernr, reinr, city, country_text);

                                            db.insertTravelHead(travelHeadBean);

                                            ArrayList<TravelHeadBean> travelHeadBeanArrayList = new ArrayList<TravelHeadBean>();
                                            travelHeadBeanArrayList = db.getTravelHead(context);


                                            adapterEmployeeList = new Adapter_report_list1(context, count, count1, travelHeadBeanArrayList, ja_success, ja_success1, from, new NotifyInterface() {
                                                @Override
                                                public void onSuccessNotify() {
                                                    adapterEmployeeList.notifyDataSetChanged();
                                                }
                                            });
                                            listView.setAdapter(adapterEmployeeList);
                                            adapterEmployeeList.notifyDataSetChanged();
                                        }

                                    });

                                }


                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {

                                        progressDialog.dismiss();
                                        list.setVisibility(View.GONE);
                                        lin2.setVisibility(View.VISIBLE);

                                    }

                                });
                            }

                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {

                                    progressDialog.dismiss();
                                    list.setVisibility(View.GONE);
                                    lin2.setVisibility(View.VISIBLE);

                                }

                            });
                        }

                    } catch (Exception e) {

                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
//
                }
            }).start();


        } catch (Exception e) {

            progressDialog.dismiss();
            Log.d("msg", "" + e);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        ja_success.clear();
        ja_success1.clear();
        if (CustomUtility.isInternetOn(context)) {

            GetTravelPendingList_Task();

        } else {
            Toast.makeText(context, "No Internet Connection Found, You Are In Offline Mode.", Toast.LENGTH_SHORT).show();

        }
    }

}
