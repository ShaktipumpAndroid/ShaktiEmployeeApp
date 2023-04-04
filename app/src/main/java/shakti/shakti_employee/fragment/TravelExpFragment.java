package shakti.shakti_employee.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.utility.Utility;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DomesticTravelExpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DomesticTravelExpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class TravelExpFragment extends Fragment {

    DatabaseHelper dataHelper = null;
    boolean connected = false;
    ArrayList<String> CountryName;
    ArrayList<String> RegionName;
    ArrayList<String> DistrictName;
    ArrayList<String> TehsilName;
    ArrayList<String> Expenses;
    ArrayList<String> Currency;
    ArrayList<String> Taxcode;
    private OnFragmentInteractionListener mListener;
    private DashboardActivity dashboardActivity;
    private Spinner trav_country1, trav_region1, trav_district1, trav_tehsil1;
    private ProgressDialog progressDialog;
    private LoggedInUser userModel;
    private Context mContext;
    android.os.Handler mHandler1 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, CountryName);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trav_country1.setAdapter(dataAdapter3);

        }
    };
    android.os.Handler mHandler2 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, RegionName);
            dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trav_region1.setAdapter(dataAdapter4);

        }
    };
    android.os.Handler mHandler3 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, DistrictName);
            dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trav_district1.setAdapter(dataAdapter5);

        }
    };
    android.os.Handler mHandler4 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayAdapter<String> dataAdapter6 = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, TehsilName);
            dataAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            trav_tehsil1.setAdapter(dataAdapter6);

        }
    };
    private TextView txt, done;
    private LinearLayout lin6;
    private ImageButton trav_date_img, trav_time_img, region;
    private String count;
    private AutoCompleteTextView trav_exp, trav_curn, trav_taxcode;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, Expenses);
            trav_exp.setAdapter(adapter1);
            trav_exp.setThreshold(1);


            ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, Currency);
            trav_curn.setAdapter(adapter2);
            trav_curn.setThreshold(1);


            ArrayAdapter adapter3 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, Taxcode);
            trav_taxcode.setAdapter(adapter3);
            trav_taxcode.setThreshold(1);


        }
    };
    private EditText trav_from_date, trav_to_date, trav_region;


    public TravelExpFragment() {
        // Required empty public constructor
    }

    public static TravelExpFragment newInstance() {
        TravelExpFragment fragment = new TravelExpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setDashBoard(DashboardActivity dashBoard) {
        this.dashboardActivity = dashBoard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataHelper = new DatabaseHelper(getActivity());


        mContext = getActivity();
        userModel = new LoggedInUser(mContext);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setTitle("Travel Expenses Entry");

        View v = inflater.inflate(R.layout.fragment_travel_expenses, container, false);

        //        Toast.makeText(getActivity(), "Attendance report", Toast.LENGTH_SHORT).show();
        userModel = new LoggedInUser(getActivity());
        Log.d("uidd", "" + userModel.uid);
//        selectAttendanceData(userModel.uid);

        txt = (TextView) v.findViewById(R.id.txt);
        trav_region = (EditText) v.findViewById(R.id.trav_region);

        lin6 = (LinearLayout) v.findViewById(R.id.lin6);

        trav_date_img = (ImageButton) v.findViewById(R.id.trav_date_img);
        trav_time_img = (ImageButton) v.findViewById(R.id.trav_time_img);
        region = (ImageButton) v.findViewById(R.id.region);

        trav_from_date = (EditText) v.findViewById(R.id.from_date);
        trav_to_date = (EditText) v.findViewById(R.id.to_date);

        trav_curn = (AutoCompleteTextView) v.findViewById(R.id.trav_currency);
        trav_exp = (AutoCompleteTextView) v.findViewById(R.id.trav_expense);
        trav_taxcode = (AutoCompleteTextView) v.findViewById(R.id.trav_tax_code);

        txt.setText("Serial No. " + Utility.getSharedPreferences(mContext, "count"));

        trav_date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        trav_from_date.setText(i2 + "/" + i1 + "/" + i);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Start Date");
                datePickerDialog.show();
            }
        });


        trav_time_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        trav_to_date.setText(i2 + "/" + i1 + "/" + i);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Start Date");
                datePickerDialog.show();
            }
        });

        region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                @SuppressLint("InflateParams")
                View view1 = ((Activity) mContext).getLayoutInflater().inflate(R.layout.regiondialog, null);
                builder.setView(view1);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                trav_country1 = (Spinner) alertDialog.findViewById(R.id.trav_country1);
                trav_region1 = (Spinner) alertDialog.findViewById(R.id.trav_region1);
                trav_district1 = (Spinner) alertDialog.findViewById(R.id.trav_district1);
                trav_tehsil1 = (Spinner) alertDialog.findViewById(R.id.trav_tehsil1);
                done = (TextView) alertDialog.findViewById(R.id.done);

                new Country(mContext).execute();

                trav_country1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                        if (trav_country1.getSelectedItem().toString().equalsIgnoreCase("Select Country")) {
                            trav_region1.setVisibility(View.GONE);
                            trav_district1.setVisibility(View.GONE);
                            trav_tehsil1.setVisibility(View.GONE);
                            done.setVisibility(View.GONE);
                        } else {
                            trav_country1.setVisibility(View.VISIBLE);
                            trav_region1.setVisibility(View.VISIBLE);
                            String cland1 = "IN";
                            Utility.setSharedPreference(mContext, "land1", cland1);
                            Log.e("DATA", "****" + cland1);
                            dataHelper.getRegion(cland1);

                            new Region(mContext, cland1).execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                trav_region1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                        if (trav_region1.getSelectedItem().toString().equalsIgnoreCase("Select Region")) {
                            trav_district1.setVisibility(View.GONE);
                            trav_tehsil1.setVisibility(View.GONE);
                            done.setVisibility(View.GONE);
                        } else {
                            trav_country1.setVisibility(View.VISIBLE);
                            trav_region1.setVisibility(View.VISIBLE);
                            trav_district1.setVisibility(View.VISIBLE);
                            String land1 = dataHelper.getRegion(Utility.getSharedPreferences(mContext, "land1")).get(i - 1).getLand1();
                            String regio1 = dataHelper.getRegion(Utility.getSharedPreferences(mContext, "land1")).get(i - 1).getRegio();
                            Utility.setSharedPreference(mContext, "land2", land1);
                            Utility.setSharedPreference(mContext, "regio1", regio1);
                            dataHelper.getDistrict(land1, regio1);

                            new District(mContext, land1, regio1).execute();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                trav_district1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                        if (trav_district1.getSelectedItem().toString().equalsIgnoreCase("Select District")) {
                            trav_tehsil1.setVisibility(View.GONE);
                            done.setVisibility(View.GONE);
                        } else {
                            trav_country1.setVisibility(View.VISIBLE);
                            trav_region1.setVisibility(View.VISIBLE);
                            trav_district1.setVisibility(View.VISIBLE);
                            trav_tehsil1.setVisibility(View.VISIBLE);
                            Log.e("LAND1", "$$$$" + Utility.getSharedPreferences(mContext, "land2"));
                            Log.e("regio1", "$$$$" + Utility.getSharedPreferences(mContext, "regio1"));
                            String land2 = dataHelper.getDistrict(Utility.getSharedPreferences(mContext, "land2"), Utility.getSharedPreferences(mContext, "regio1")).get(i - 1).getLand1();
                            String regio2 = dataHelper.getDistrict(Utility.getSharedPreferences(mContext, "land2"), Utility.getSharedPreferences(mContext, "regio1")).get(i - 1).getRegio();
                            String cityc = dataHelper.getDistrict(Utility.getSharedPreferences(mContext, "land2"), Utility.getSharedPreferences(mContext, "regio1")).get(i - 1).getCityc();
                            Log.e("LAND2", "$$$$" + land2);
                            Log.e("regio2", "$$$$" + regio2);
                            Utility.setSharedPreference(mContext, "land2", land2);
                            Utility.setSharedPreference(mContext, "regio2", regio2);
                            Utility.setSharedPreference(mContext, "cityc", cityc);

                            Log.e("cityc", "$$$$" + cityc);
                            dataHelper.getTehsil(cityc);

                            new Tehsil(mContext, cityc).execute();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                trav_tehsil1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        done.setVisibility(View.VISIBLE);

                        if (trav_tehsil1.getSelectedItem().toString().equalsIgnoreCase("Select Tehsil")) {
                            done.setVisibility(View.GONE);
                        } else {
                            trav_country1.setVisibility(View.VISIBLE);
                            trav_region1.setVisibility(View.VISIBLE);
                            trav_district1.setVisibility(View.VISIBLE);
                            trav_tehsil1.setVisibility(View.VISIBLE);
                            done.setVisibility(View.VISIBLE);
                            String tehsil = dataHelper.getTehsil(Utility.getSharedPreferences(mContext, "cityc")).get(i - 1).getTehsil();
                            Log.e("Tehsil", "&&&&" + tehsil);

                            Utility.setSharedPreference(mContext, "tehsil", tehsil);


                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        trav_region.setText(Utility.getSharedPreferences(mContext, "tehsil"));
                        alertDialog.dismiss();
                    }

                });
            }
        });

        new DownloadTask(mContext).execute();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class DownloadTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;

        private Context mContext;

        public DownloadTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = ProgressDialog.show(mContext, "", "Getting Data ...");
            mProgressDialog.setCanceledOnTouchOutside(false); // main method that force user cannot click outside
            mProgressDialog.setCancelable(true);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dlg) {
                    DownloadTask.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {

            String data = null;
            // do some background work here
            try {

                Expenses = new ArrayList<>();
                Currency = new ArrayList<>();
                Taxcode = new ArrayList<>();
                Expenses.add("Expense Type");
                Currency.add("Currency Type");
                Taxcode.add("Taxcode Type");

                CountryName = new ArrayList<>();
                CountryName.add("Select Country");

                for (int i = 0; i < dataHelper.getCountry().size(); i++) {

                    String country = dataHelper.getCountry().get(i).getLandx();
                    CountryName.add(country);
                }
                for (int i = 0; i < dataHelper.getTaxcode().size(); i++) {

                    String taxcode = dataHelper.getTaxcode().get(i).getTaxcode() + "-" + dataHelper.getTaxcode().get(i).getText();
                    Taxcode.add(taxcode);
                }
                for (int i = 0; i < dataHelper.getExpenses().size(); i++) {

                    String expenses = dataHelper.getExpenses().get(i).getSptxt();
                    Expenses.add(expenses);
                }
                for (int i = 0; i < dataHelper.getCurrency().size(); i++) {

                    String currency = dataHelper.getCurrency().get(i).getLtext();
                    Currency.add(currency);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.obj = "";
            mHandler.sendMessage(msg);

            return data;

        }

        @Override
        protected void onPostExecute(String result) {
            if (this.isCancelled()) {
                result = null;
                return;
            }

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

        }
    }

    public class Country extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;

        private Context mContext;

        public Country(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = ProgressDialog.show(mContext, "", "Getting Data ...");
            mProgressDialog.setCanceledOnTouchOutside(false); // main method that force user cannot click outside
            mProgressDialog.setCancelable(true);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dlg) {
                    Country.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {

            String data = null;
            // do some background work here
            try {


                CountryName = new ArrayList<>();
                CountryName.add("Select Country");

                for (int i = 0; i < dataHelper.getCountry().size(); i++) {

                    String country = dataHelper.getCountry().get(i).getLandx();
                    if (country.equalsIgnoreCase("India")) {
                        CountryName.add(country);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.obj = "";
            mHandler1.sendMessage(msg);

            return data;

        }

        @Override
        protected void onPostExecute(String result) {
            if (this.isCancelled()) {
                result = null;
                return;
            }

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

        }
    }

    public class Region extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;

        private Context mContext;
        private String Land1;

        public Region(Context context, String land1) {
            this.mContext = context;
            this.Land1 = land1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = ProgressDialog.show(mContext, "", "Getting Data ...");
            mProgressDialog.setCanceledOnTouchOutside(false); // main method that force user cannot click outside
            mProgressDialog.setCancelable(true);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dlg) {
                    Region.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {

            String data = null;
            // do some background work here
            try {


                RegionName = new ArrayList<>();
                RegionName.add("Select Region");

                for (int i = 0; i < dataHelper.getRegion(Land1).size(); i++) {

                    String region = dataHelper.getRegion(Land1).get(i).getBezei();
                    RegionName.add(region);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.obj = "";
            mHandler2.sendMessage(msg);

            return data;

        }

        @Override
        protected void onPostExecute(String result) {
            if (this.isCancelled()) {
                result = null;
                return;
            }

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

        }
    }

    public class District extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;

        private Context mContext;
        private String Land1, Regio1;

        public District(Context context, String land1, String regio) {
            this.mContext = context;
            this.Land1 = land1;
            this.Regio1 = regio;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = ProgressDialog.show(mContext, "", "Getting Data ...");
            mProgressDialog.setCanceledOnTouchOutside(false); // main method that force user cannot click outside
            mProgressDialog.setCancelable(true);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dlg) {
                    District.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {

            String data = null;
            // do some background work here
            try {


                DistrictName = new ArrayList<>();
                DistrictName.add("Select District");

                for (int i = 0; i < dataHelper.getDistrict(Land1, Regio1).size(); i++) {

                    String district = dataHelper.getDistrict(Land1, Regio1).get(i).getBezei();
                    DistrictName.add(district);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.obj = "";
            mHandler3.sendMessage(msg);

            return data;

        }

        @Override
        protected void onPostExecute(String result) {
            if (this.isCancelled()) {
                result = null;
                return;
            }

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

        }
    }

    public class Tehsil extends AsyncTask<Void, Void, String> {
        private ProgressDialog mProgressDialog;

        private Context mContext;
        private String Land1, Regio1, Cityc1;

        public Tehsil(Context context, String cityc) {
            this.mContext = context;
            this.Cityc1 = cityc;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = ProgressDialog.show(mContext, "", "Getting Data ...");
            mProgressDialog.setCanceledOnTouchOutside(false); // main method that force user cannot click outside
            mProgressDialog.setCancelable(true);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dlg) {
                    Tehsil.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {

            String data = null;
            // do some background work here
            try {


                TehsilName = new ArrayList<>();
                TehsilName.add("Select Tehsil");

                for (int i = 0; i < dataHelper.getTehsil(Cityc1).size(); i++) {

                    String tehsil = dataHelper.getTehsil(Cityc1).get(i).getTehsil_txt();

                    Log.e("Tehsil", "&&&&" + tehsil);
                    TehsilName.add(tehsil);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.obj = "";
            mHandler4.sendMessage(msg);

            return data;

        }

        @Override
        protected void onPostExecute(String result) {
            if (this.isCancelled()) {
                result = null;
                return;
            }

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }

        }
    }

}
