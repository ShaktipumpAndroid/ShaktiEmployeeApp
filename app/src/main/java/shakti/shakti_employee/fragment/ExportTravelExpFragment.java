package shakti.shakti_employee.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import adapter.CountryAdapter;
import shakti.shakti_employee.R;
import shakti.shakti_employee.activity.DashboardActivity;
import shakti.shakti_employee.bean.TravelEntryExpDocBean;
import shakti.shakti_employee.connect.CustomHttpClient;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.SapUrl;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain mContext fragment must implement the
 * {@link ExportTravelExpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExportTravelExpFragment#newInstance} factory method to
 * create an instance of mContext fragment.
 */
public class ExportTravelExpFragment extends Fragment {

    DatabaseHelper dataHelper = null;
    ArrayList<String> CountryName;
    ArrayList<String> Expenses;
    ArrayList<String> Currency;
    ArrayList<String> Taxcode;
    LinearLayout lvlMainParentLayoutID;
    JSONArray mJsonArray = null;
    ImageView imgCancleBTNID;
    List<CardView> mCardView_MainCardIDList;
    List<ImageView> mImageView_CancelIDList;
    List<EditText> mEditText_FromIDList;
    List<EditText> mEditText_ToIDList;
    List<AutoCompleteTextView> mEditText_ExpensesIDList;
    List<EditText> mEditText_AmountIDList;
    List<AutoCompleteTextView> mEditText_CurrencyIDList;
    List<AutoCompleteTextView> mEditText_RegionIDList;
    List<EditText> mEditText_DescriptionIDList;
    List<EditText> mEditText_LocationIDList;
    List<EditText> mEditText_GSTINIDList;
    List<EditText> mEditText_FromIDList_Old;
    List<EditText> mEditText_ToIDList_Old;
    List<AutoCompleteTextView> mEditText_ExpensesIDList_Old;
    List<EditText> mEditText_AmountIDList_Old;
    List<AutoCompleteTextView> mEditText_CurrencyIDList_Old;
    List<AutoCompleteTextView> mEditText_RegionIDList_Old;
    List<EditText> mEditText_DescriptionIDList_Old;
    List<EditText> mEditText_LocationIDList_Old;
    List<EditText> mEditText_GSTINIDList_Old;
    int mIDIndexOnesRun = -1;
    int mIDIndex = 0;
    int mTagIDIndex = 0;
    LinearLayout iv_sub_linearlayout12;
    boolean connected = false;
    ProgressDialog progressBar;
    int exp, curr, coun;
    Dialog country_dialog, expenses_dialog, taxcode_dialog, currency_dialog;
    TravelEntryExpDocBean travelEntryExpDocBean;
    private OnFragmentInteractionListener mListener;
    private DashboardActivity dashboardActivity;
    private LoggedInUser userModel;
    private Context mContext;
    android.os.Handler mHand = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();

        }
    };
    android.os.Handler mHandler1 = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            country_dialog = new Dialog(mContext);
            country_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            country_dialog.setContentView(R.layout.city_state_dialog);
            country_dialog.setCancelable(true);
            country_dialog.show();

            TextView title_tv = (TextView) country_dialog.findViewById(R.id.title_tv);
            title_tv.setText("Select Country");
            final EditText autoCompleteTextView = (EditText) country_dialog.findViewById(R.id.autoCompleteTextView);
            ListView listview = (ListView) country_dialog.findViewById(R.id.listview);
            final AutoCompleteTextView autocompletetextview = (AutoCompleteTextView) country_dialog
                    .findViewById(R.id.autoCompleteTextView);
            try {


                final CountryAdapter adapter = new CountryAdapter(mContext, CountryName);
                listview.setAdapter(adapter);
                listview.setTextFilterEnabled(true);


                try {
                    ArrayAdapter adapter1 = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, CountryName);
                    autocompletetextview.setAdapter(adapter1);
                    autocompletetextview.setThreshold(1);
                    autocompletetextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            // TODO Auto-generated method stub
                            country_dialog.dismiss();
                            mEditText_RegionIDList.get(coun).setText(autocompletetextview.getText().toString());

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> v, View view,
                                        int pos, long id) {
                    country_dialog.dismiss();

                    String country = dataHelper.getCountry().get(pos).getLandx();
                    String country1 = dataHelper.getCountry().get(pos).getLand1();

                    mEditText_RegionIDList.get(coun).setText(country1 + " " + country);
                    country_dialog = null;

                }
            });

        }
    };
    android.os.Handler mHandlerexp = new android.os.Handler() {
        @Override
        public void handleMessage(final Message msg) {

            expenses_dialog = new Dialog(mContext);
            expenses_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            expenses_dialog.setContentView(R.layout.city_state_dialog);
            expenses_dialog.setCancelable(true);
            expenses_dialog.show();

            TextView title_tv = (TextView) expenses_dialog.findViewById(R.id.title_tv);
            title_tv.setText("Select Expenses");
            final EditText autoCompleteTextView = (EditText) expenses_dialog.findViewById(R.id.autoCompleteTextView);
            ListView listview = (ListView) expenses_dialog.findViewById(R.id.listview);
            final AutoCompleteTextView autocompletetextview = (AutoCompleteTextView) expenses_dialog
                    .findViewById(R.id.autoCompleteTextView);
            try {

                final CountryAdapter adapter = new CountryAdapter(mContext, Expenses);
                listview.setAdapter(adapter);
                listview.setTextFilterEnabled(true);


                try {
                    ArrayAdapter adapter1 = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, Expenses);
                    autocompletetextview.setAdapter(adapter1);
                    autocompletetextview.setThreshold(1);
                    autocompletetextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            // TODO Auto-generated method stub
                            expenses_dialog.dismiss();
                            //edtExpensesID.setText(autocompletetextview.getText().toString());
                            mEditText_ExpensesIDList.get(exp).setText(autocompletetextview.getText().toString());

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> v, View view,
                                        int pos, long id) {
                    expenses_dialog.dismiss();

                    String expenses = dataHelper.getExpenses().get(pos).getSptxt();
                    String expenses1 = dataHelper.getExpenses().get(pos).getSpkzl();

                    //edtExpensesID.setText(expenses1+" "+expenses);
                    mEditText_ExpensesIDList.get(exp).setText(expenses1 + " " + expenses);
                    expenses_dialog = null;

                }
            });


        }
    };
    android.os.Handler mHandlercurr = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            currency_dialog = new Dialog(mContext);
            currency_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            currency_dialog.setContentView(R.layout.city_state_dialog);
            currency_dialog.setCancelable(true);
            currency_dialog.show();

            TextView title_tv = (TextView) currency_dialog.findViewById(R.id.title_tv);
            title_tv.setText("Select Currency");
            final EditText autoCompleteTextView = (EditText) currency_dialog.findViewById(R.id.autoCompleteTextView);
            ListView listview = (ListView) currency_dialog.findViewById(R.id.listview);
            final AutoCompleteTextView autocompletetextview = (AutoCompleteTextView) currency_dialog
                    .findViewById(R.id.autoCompleteTextView);
            try {

                final CountryAdapter adapter = new CountryAdapter(mContext, Currency);
                listview.setAdapter(adapter);
                listview.setTextFilterEnabled(true);


                try {
                    ArrayAdapter adapter1 = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, Currency);
                    autocompletetextview.setAdapter(adapter1);
                    autocompletetextview.setThreshold(1);
                    autocompletetextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            // TODO Auto-generated method stub
                            currency_dialog.dismiss();
                            mEditText_CurrencyIDList.get(curr).setText(autocompletetextview.getText().toString());

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> v, View view,
                                        int pos, long id) {
                    currency_dialog.dismiss();

                    String currency = dataHelper.getCurrency().get(pos).getLtext();
                    String currency1 = dataHelper.getCurrency().get(pos).getWaser();


                    mEditText_CurrencyIDList.get(curr).setText(currency1 + " " + currency);
                    currency_dialog = null;

                }
            });


        }
    };
    private ProgressDialog progressDialog;
    private AutoCompleteTextView trav_country;
    android.os.Handler mHandl = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            country_dialog = new Dialog(mContext);
            country_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            country_dialog.setContentView(R.layout.city_state_dialog);
            country_dialog.setCancelable(true);
            country_dialog.show();

            TextView title_tv = (TextView) country_dialog.findViewById(R.id.title_tv);
            title_tv.setText("Select Country");
            final EditText autoCompleteTextView = (EditText) country_dialog.findViewById(R.id.autoCompleteTextView);
            ListView listview = (ListView) country_dialog.findViewById(R.id.listview);
            final AutoCompleteTextView autocompletetextview = (AutoCompleteTextView) country_dialog
                    .findViewById(R.id.autoCompleteTextView);
            try {


                final CountryAdapter adapter = new CountryAdapter(mContext, CountryName);
                listview.setAdapter(adapter);
                listview.setTextFilterEnabled(true);


                try {
                    ArrayAdapter adapter1 = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, CountryName);
                    autocompletetextview.setAdapter(adapter1);
                    autocompletetextview.setThreshold(1);
                    autocompletetextview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            // TODO Auto-generated method stub
                            country_dialog.dismiss();

                            trav_country.setText(autoCompleteTextView.getText().toString());

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> v, View view,
                                        int pos, long id) {
                    country_dialog.dismiss();

                    String country = dataHelper.getCountry().get(pos).getLandx();
                    String country1 = dataHelper.getCountry().get(pos).getLand1();

                    trav_country.setText(country1 + " " + country);
                    country_dialog = null;

                }
            });

        }
    };
    private String mFromAddress, mToAddress, mExpenses, mAmount, mCurrency, mDescription, mLocation1, mTaxcode, mRegion, mGSTIN, mStartDate, mEndDate, mLocation, mCountry, mUserID, mType, mSerialNo;
    private ImageButton trav_date_img/*,trav_time_img*/, end_date_img/*,end_time_img*/;
    private TextView txtAddTripBTNID;
    private TextView txtAddModeDestinantionTripBTNID;
    private String mExpenses1, mCurrency1, mCountry1, mTaxcode1, sync_msg, sync_value;
    private CardView cardAddMoreDestinationID;
    private int mTripsTypeClickValue = 1;
    private CardView cardViewAddDynamicViewID;
    private RelativeLayout rlvMainDynamicViewID;
    private AutoCompleteTextView edtExpensesID, edtCurrencyID, edtRegionID;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, CountryName);
            trav_country.setAdapter(adapter);
            trav_country.setThreshold(1);

            ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, Expenses);
            edtExpensesID.setAdapter(adapter1);
            edtExpensesID.setThreshold(1);


            ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, Currency);
            edtCurrencyID.setAdapter(adapter2);
            edtCurrencyID.setThreshold(1);


            ArrayAdapter adapter4 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, CountryName);
            edtRegionID.setAdapter(adapter4);
            edtRegionID.setThreshold(1);


        }
    };
    private EditText edtAmountID, edtFromDateID, edtToDateID, edtDescriptionID, edtLocationID, edtGstInID, location;
    private EditText trav_date/*,trav_time*/, end_date/*,end_time*/;
    private String count;

    public ExportTravelExpFragment() {
        // Required empty public constructor
    }

    public static ExportTravelExpFragment newInstance() {
        ExportTravelExpFragment fragment = new ExportTravelExpFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for mContext fragment
        getActivity().setTitle("Travel Trip Expenses");

        View v = inflater.inflate(R.layout.fragment_domtrav_entry, container, false);

        userModel = new LoggedInUser(getActivity());
        Log.d("uidd", "" + userModel.uid);

        mUserID = userModel.uid;

        initAddTripDynamicView(v);

        mCardView_MainCardIDList = new ArrayList<CardView>();
        mImageView_CancelIDList = new ArrayList<ImageView>();

        mEditText_FromIDList = new ArrayList<EditText>();
        mEditText_ToIDList = new ArrayList<EditText>();
        mEditText_ExpensesIDList = new ArrayList<AutoCompleteTextView>();
        mEditText_AmountIDList = new ArrayList<EditText>();
        mEditText_CurrencyIDList = new ArrayList<AutoCompleteTextView>();
        mEditText_RegionIDList = new ArrayList<AutoCompleteTextView>();
        mEditText_DescriptionIDList = new ArrayList<EditText>();
        mEditText_LocationIDList = new ArrayList<EditText>();
        mEditText_GSTINIDList = new ArrayList<EditText>();


        mEditText_FromIDList_Old = new ArrayList<EditText>();
        mEditText_ToIDList_Old = new ArrayList<EditText>();
        mEditText_ExpensesIDList_Old = new ArrayList<AutoCompleteTextView>();
        mEditText_AmountIDList_Old = new ArrayList<EditText>();
        mEditText_CurrencyIDList_Old = new ArrayList<AutoCompleteTextView>();
        mEditText_RegionIDList_Old = new ArrayList<AutoCompleteTextView>();
        mEditText_DescriptionIDList_Old = new ArrayList<EditText>();
        mEditText_LocationIDList_Old = new ArrayList<EditText>();
        mEditText_GSTINIDList_Old = new ArrayList<EditText>();

        trav_date_img = (ImageButton) v.findViewById(R.id.trav_date_img);

        end_date_img = (ImageButton) v.findViewById(R.id.end_date_img);


        trav_date = (EditText) v.findViewById(R.id.trav_date);

        end_date = (EditText) v.findViewById(R.id.end_date);
        location = (EditText) v.findViewById(R.id.gp_visitplace);

        trav_country = (AutoCompleteTextView) v.findViewById(R.id.trav_country);
        trav_country.setFocusable(false);

        travelEntryExpDocBean = new TravelEntryExpDocBean();
        travelEntryExpDocBean = dataHelper.getExpTravelEntryInformation1(mUserID);


        trav_date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        trav_date.setText(i2 + "/" + i1 + "/" + i);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Start Date");
                datePickerDialog.show();
            }
        });


        // Date help for leave to
        end_date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate;
                int mDay, mMonth, mYear;
                currentDate = Calendar.getInstance();

                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
                mMonth = currentDate.get(Calendar.MONTH);
                mYear = currentDate.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1 = i1 + 1;
                        end_date.setText(i2 + "/" + i1 + "/" + i);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("End Date");
                datePickerDialog.show();
            }
        });

        trav_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Country2(mContext).execute();
            }
        });


        Log.e("SERIAL NUMBER", "&&&&&" + dataHelper.cursorexp1.getCount());
        if (dataHelper.cursorexp1.getCount() >= 1) {
            for (int i = 0; i < dataHelper.cursorexp1.getCount(); i++) {
                addDynamicViewPro(v);
                count = String.valueOf(i + 1);
                travelEntryExpDocBean = new TravelEntryExpDocBean();
                travelEntryExpDocBean = dataHelper.getExpTravelEntryInformation(mUserID, count);
                setData();
            }
        } else {
            addDynamicViewPro(v);
            count = String.valueOf(1);
            travelEntryExpDocBean = new TravelEntryExpDocBean();
            travelEntryExpDocBean = dataHelper.getExpTravelEntryInformation(mUserID, count);
            //setData();
        }

        return v;
    }

    public void saveData() {

        Log.e("SIZE", "&&&&&" + mEditText_FromIDList.size());

        dataHelper.deleteExpTravelData();

        for (int i = 0; i < mEditText_FromIDList.size(); i++) {

            mUserID = userModel.uid;
            mSerialNo = String.valueOf(i + 1);
            mStartDate = trav_date.getText().toString().trim();
            mEndDate = end_date.getText().toString().trim();
            mCountry = trav_country.getText().toString().trim();

            mLocation = location.getText().toString().trim();
            mFromAddress = mEditText_FromIDList.get(i).getText().toString().trim();
            mToAddress = mEditText_ToIDList.get(i).getText().toString().trim();
            mExpenses = mEditText_ExpensesIDList.get(i).getText().toString().trim();
            mCurrency = mEditText_CurrencyIDList.get(i).getText().toString().trim();
            mTaxcode = "";
            mLocation1 = mEditText_LocationIDList.get(i).getText().toString().trim();
            mRegion = mEditText_RegionIDList.get(i).getText().toString().trim();
            mAmount = mEditText_AmountIDList.get(i).getText().toString().trim();
            mDescription = mEditText_DescriptionIDList.get(i).getText().toString().trim();
            mGSTIN = mEditText_GSTINIDList.get(i).getText().toString().trim();
            mType = "E";


            TravelEntryExpDocBean travelEntryExpDocBean = new TravelEntryExpDocBean(mUserID, mSerialNo,
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
                    mType);

            if (dataHelper.isRecordExist(dataHelper.TABLE_TRAVEL_EXP_EXPENSES, dataHelper.SERIALNO, mSerialNo)) {
                dataHelper.updateEXPTravelEntryDocument(travelEntryExpDocBean, mSerialNo);
            } else {
                dataHelper.insertEXPTravelEntryDocument(travelEntryExpDocBean);
            }

        }

    }

    private void initAddTripDynamicView(View v) {

        lvlMainParentLayoutID = v.findViewById(R.id.lvlMainParentLayoutID);

        txtAddTripBTNID = v.findViewById(R.id.txtAddTripBTNID);

        cardAddMoreDestinationID = v.findViewById(R.id.cardAddMoreDestinationID);


        txtAddModeDestinantionTripBTNID = v.findViewById(R.id.txtAddModeDestinantionTripBTNID);


        txtAddModeDestinantionTripBTNID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDynamicViewPro(v);

            }
        });


        txtAddTripBTNID.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                mStartDate = trav_date.getText().toString().trim();
                mEndDate = end_date.getText().toString().trim();
                mCountry = trav_country.getText().toString().trim();
                mLocation = location.getText().toString().trim();

                if (CustomUtility.isInternetOn(mContext)) {

                    saveData();

                    checkDataValtidation();
                } else {
                    saveData();
                    changeButtonVisibility(false, 0.5f, txtAddTripBTNID);
                    Toast.makeText(mContext, "No Internet Connection Found, You Are In Offline Mode.", Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity()).finish();
                }

            }
        });

    }

    private void addDynamicViewPro(View v) {

        ++mIDIndexOnesRun;

        mIDIndex = mIDIndexOnesRun;

        cardViewAddDynamicViewID = new CardView(mContext);
        CardView.LayoutParams cardViewAddDynamicViewIDoutparams12 = new CardView.LayoutParams
                ((int) CardView.LayoutParams.MATCH_PARENT, (int) CardView.LayoutParams.WRAP_CONTENT);
        cardViewAddDynamicViewIDoutparams12.setMarginEnd(5);
        cardViewAddDynamicViewIDoutparams12.setMarginStart(5);
        cardViewAddDynamicViewIDoutparams12.setMargins(5, 10, 5, 10);
        cardViewAddDynamicViewID.setCardBackgroundColor(getResources().getColor(R.color.white));
        cardViewAddDynamicViewID.setRadius(10);
        cardViewAddDynamicViewID.setId(mIDIndex + 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardViewAddDynamicViewID.setElevation(5);
        }
        cardViewAddDynamicViewID.setLayoutParams(cardViewAddDynamicViewIDoutparams12);


        mCardView_MainCardIDList.add(cardViewAddDynamicViewID);


        rlvMainDynamicViewID = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams rlvMainDynamicViewIDParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);

        rlvMainDynamicViewIDParam.setMargins(10, 0, 10, 5);
        rlvMainDynamicViewIDParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        rlvMainDynamicViewID.setLayoutParams(rlvMainDynamicViewIDParam);


        iv_sub_linearlayout12 = new LinearLayout(mContext);
        LinearLayout.LayoutParams iv_outparams12 = new LinearLayout.LayoutParams
                ((int) LinearLayout.LayoutParams.MATCH_PARENT, (int) LinearLayout.LayoutParams.MATCH_PARENT);
        iv_outparams12.setMarginEnd(0);
        iv_outparams12.setMarginStart(0);
        iv_outparams12.setMargins(0, 15, 0, 5);
        iv_sub_linearlayout12.setBackgroundColor(getResources().getColor(R.color.white));
        iv_sub_linearlayout12.setOrientation(LinearLayout.VERTICAL);
        iv_sub_linearlayout12.setLayoutParams(iv_outparams12);

        //////////////////to expenses
        TextView txtExpTextHead = new TextView(mContext);
        RelativeLayout.LayoutParams txtExpTextHeadParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtExpTextHeadParam.setMarginStart(20);
        txtExpTextHeadParam.setMargins(0, 10, 0, 5);
        txtExpTextHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        txtExpTextHead.setGravity(Gravity.CENTER_VERTICAL);
        //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
        txtExpTextHead.setText(getResources().getString(R.string.Expensestxt));
        txtExpTextHead.setTextSize((float) 15);
        txtExpTextHead.setTypeface(null, Typeface.NORMAL);
        //  tv_Code.setId(i + 2);
        txtExpTextHead.setTextColor(getResources().getColor(R.color.black));
        txtExpTextHead.setLayoutParams(txtExpTextHeadParam);
        iv_sub_linearlayout12.addView(txtExpTextHead);


        edtExpensesID = new AutoCompleteTextView(mContext);
        RelativeLayout.LayoutParams edtExpensesParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        edtExpensesParam.setMarginStart(20);
        edtExpensesParam.setMarginEnd(20);
        edtExpensesParam.addRule(RelativeLayout.CENTER_VERTICAL);
        edtExpensesID.setGravity(Gravity.CENTER_VERTICAL);
        // edtFomeCountryID.setHint("Select City ,State, Country");
        edtExpensesID.setHint(getResources().getString(R.string.Expensestxt));
        edtExpensesID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
        edtExpensesID.setTextColor(getResources().getColor(R.color.black));
        edtExpensesID.setBackgroundColor(getResources().getColor(R.color.white));
        edtExpensesID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
        edtExpensesID.setTextSize((float) 13);
        edtExpensesID.setFocusable(false);
        edtExpensesID.setFocusableInTouchMode(false);
        edtExpensesID.requestFocus();
        edtExpensesID.setClickable(true);
        edtExpensesID.setId(mIDIndex + 2);
        edtExpensesID.setTypeface(null, Typeface.NORMAL);
        edtExpensesID.setLayoutParams(edtExpensesParam);

        edtExpensesID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iii = view.getId();
                exp = iii - 2;
                new Expenses(mContext, exp).execute();
            }
        });

        mEditText_ExpensesIDList.add(edtExpensesID);/////List

        iv_sub_linearlayout12.addView(edtExpensesID);


        View mView = new View(mContext);
        RelativeLayout.LayoutParams mParamView = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
        mParamView.setMarginEnd(0);
        mParamView.setMarginStart(0);
        mParamView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mView.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
        mView.setLayoutParams(mParamView);
        iv_sub_linearlayout12.addView(mView);


        //////////////////to Amount

        TextView txtAmtTextHead = new TextView(mContext);
        RelativeLayout.LayoutParams txtAmtTextHeadParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtAmtTextHeadParam.setMarginStart(20);
        txtAmtTextHeadParam.setMargins(0, 10, 0, 5);
        txtAmtTextHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        txtAmtTextHead.setGravity(Gravity.CENTER_VERTICAL);
        //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
        txtAmtTextHead.setText(getResources().getString(R.string.Amounttxt));
        txtAmtTextHead.setTextSize((float) 15);
        txtAmtTextHead.setTypeface(null, Typeface.NORMAL);
        //  tv_Code.setId(i + 2);
        txtAmtTextHead.setTextColor(getResources().getColor(R.color.black));
        txtAmtTextHead.setLayoutParams(txtAmtTextHeadParam);
        iv_sub_linearlayout12.addView(txtAmtTextHead);


        edtAmountID = new EditText(mContext);
        RelativeLayout.LayoutParams edtAmountParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        edtAmountParam.setMarginStart(20);
        edtAmountParam.setMarginEnd(20);
        edtAmountParam.addRule(RelativeLayout.CENTER_VERTICAL);
        edtAmountID.setGravity(Gravity.CENTER_VERTICAL);
        edtAmountID.setHint(getResources().getString(R.string.Amounttxt));
        edtAmountID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
        edtAmountID.setTextColor(getResources().getColor(R.color.black));
        edtAmountID.setBackgroundColor(getResources().getColor(R.color.white));
        edtAmountID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
        edtAmountID.setTextSize((float) 13);
        edtAmountID.setFocusable(true);
        edtAmountID.setFocusableInTouchMode(true);
        edtAmountID.setClickable(true);
        edtAmountID.setId(mIDIndex + 3);
        edtAmountID.setTypeface(null, Typeface.NORMAL);
        edtAmountID.setLayoutParams(edtAmountParam);


        mEditText_AmountIDList.add(edtAmountID);/////List


        iv_sub_linearlayout12.addView(edtAmountID);


        View mViewAmt = new View(mContext);
        RelativeLayout.LayoutParams mParamViewAmt = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
        mParamViewAmt.setMarginEnd(0);
        mParamViewAmt.setMarginStart(0);
        mParamViewAmt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mViewAmt.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
        mViewAmt.setLayoutParams(mParamViewAmt);
        iv_sub_linearlayout12.addView(mViewAmt);


        //////////////////Currency

        TextView txtCurrTextHead = new TextView(mContext);
        RelativeLayout.LayoutParams txtCurrHeadParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtCurrHeadParam.setMarginStart(20);
        txtCurrHeadParam.setMargins(0, 10, 0, 5);
        txtCurrHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        txtCurrTextHead.setGravity(Gravity.CENTER_VERTICAL);
        //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
        txtCurrTextHead.setText(getResources().getString(R.string.Currencytxt));
        txtCurrTextHead.setTextSize((float) 15);
        txtCurrTextHead.setTypeface(null, Typeface.NORMAL);
        //  tv_Code.setId(i + 2);
        txtCurrTextHead.setTextColor(getResources().getColor(R.color.black));
        txtCurrTextHead.setLayoutParams(txtCurrHeadParam);
        iv_sub_linearlayout12.addView(txtCurrTextHead);

        edtCurrencyID = new AutoCompleteTextView(mContext);
        RelativeLayout.LayoutParams edtCurrencyParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        edtCurrencyParam.setMarginStart(20);
        edtCurrencyParam.setMarginEnd(20);
        edtCurrencyParam.addRule(RelativeLayout.CENTER_VERTICAL);
        edtCurrencyID.setGravity(Gravity.CENTER_VERTICAL);
        //edtArrrivalDateCountryID.setHint("Select Arrival Date");
        edtCurrencyID.setHint(getResources().getString(R.string.Currencytxt));
        edtCurrencyID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
        edtCurrencyID.setTextColor(getResources().getColor(R.color.black));
        edtCurrencyID.setBackgroundColor(getResources().getColor(R.color.white));
        edtCurrencyID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
        edtCurrencyID.setTextSize((float) 13);
        edtCurrencyID.setFocusable(false);
        edtCurrencyID.setFocusableInTouchMode(false);
        edtCurrencyID.setClickable(true);
        edtCurrencyID.setId(mIDIndex + 4);
        edtCurrencyID.setTypeface(null, Typeface.NORMAL);
        edtCurrencyID.setLayoutParams(edtCurrencyParam);
        edtCurrencyID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iii = view.getId();
                curr = iii - 4;
                new Currency(mContext).execute();
            }
        });

        mEditText_CurrencyIDList.add(edtCurrencyID);/////List

        iv_sub_linearlayout12.addView(edtCurrencyID);


        View mViewCurrency = new View(mContext);
        RelativeLayout.LayoutParams mParamViewCurrency = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
        mParamViewCurrency.setMarginEnd(0);
        mParamViewCurrency.setMarginStart(0);
        mParamViewCurrency.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mViewCurrency.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
        mViewCurrency.setLayoutParams(mParamViewCurrency);
        iv_sub_linearlayout12.addView(mViewCurrency);

        //from date

        TextView txtFromDateTextHead = new TextView(mContext);
        RelativeLayout.LayoutParams txtFromDateHeadParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtFromDateHeadParam.setMarginStart(20);
        txtFromDateHeadParam.setMargins(0, 10, 0, 5);
        txtFromDateHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        txtFromDateTextHead.setGravity(Gravity.CENTER_VERTICAL);
        //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
        txtFromDateTextHead.setText(getResources().getString(R.string.Fromtxt));
        txtFromDateTextHead.setTextSize((float) 15);
        txtFromDateTextHead.setTypeface(null, Typeface.NORMAL);
        //  tv_Code.setId(i + 2);
        txtFromDateTextHead.setTextColor(getResources().getColor(R.color.black));
        txtFromDateTextHead.setLayoutParams(txtFromDateHeadParam);
        iv_sub_linearlayout12.addView(txtFromDateTextHead);

        edtFromDateID = new EditText(mContext);
        RelativeLayout.LayoutParams txtFromDateTextHeadParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtFromDateTextHeadParam.setMarginStart(20);
        txtFromDateTextHeadParam.setMarginEnd(20);
        txtFromDateTextHeadParam.addRule(RelativeLayout.CENTER_VERTICAL);
        edtFromDateID.setGravity(Gravity.CENTER_VERTICAL);
        //edtArrrivalDateCountryID.setHint("Select Arrival Date");
        edtFromDateID.setHint(getResources().getString(R.string.Fromtxt));
        edtFromDateID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
        edtFromDateID.setTextColor(getResources().getColor(R.color.black));
        edtFromDateID.setBackgroundColor(getResources().getColor(R.color.white));
        edtFromDateID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
        edtFromDateID.setTextSize((float) 13);
        edtFromDateID.setFocusable(false);
        edtFromDateID.setFocusableInTouchMode(false);
        edtFromDateID.setClickable(true);
        edtFromDateID.setId(mIDIndex + 6);
        edtFromDateID.setTypeface(null, Typeface.NORMAL);
        edtFromDateID.setLayoutParams(txtFromDateTextHeadParam);

        mEditText_FromIDList.add(edtFromDateID);

        edtFromDateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int iii = v.getId();
                final int vvv = iii - 6;

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
                        //edtFromDateID.setText(i2 + "/" + i1 + "/" + i);
                        mEditText_FromIDList.get(vvv).setText(i2 + "/" + i1 + "/" + i);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Start Date");
                edtToDateID.requestFocus();
                datePickerDialog.show();


            }
        });
        iv_sub_linearlayout12.addView(edtFromDateID);

        View mViewFromDate = new View(mContext);
        RelativeLayout.LayoutParams mParamViewFromDate = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
        mParamViewFromDate.setMarginEnd(0);
        mParamViewFromDate.setMarginStart(0);
        mParamViewFromDate.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mViewFromDate.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
        mViewFromDate.setLayoutParams(mParamViewFromDate);
        iv_sub_linearlayout12.addView(mViewFromDate);


        //to date

        TextView txtToDateTextHead = new TextView(mContext);
        RelativeLayout.LayoutParams txtToDateHeadParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtToDateHeadParam.setMarginStart(20);
        txtToDateHeadParam.setMargins(0, 10, 0, 5);
        txtToDateHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        txtToDateTextHead.setGravity(Gravity.CENTER_VERTICAL);
        //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
        txtToDateTextHead.setText(getResources().getString(R.string.Totxt));
        txtToDateTextHead.setTextSize((float) 15);
        txtToDateTextHead.setTypeface(null, Typeface.NORMAL);
        //  tv_Code.setId(i + 2);
        txtToDateTextHead.setTextColor(getResources().getColor(R.color.black));
        txtToDateTextHead.setLayoutParams(txtToDateHeadParam);
        iv_sub_linearlayout12.addView(txtToDateTextHead);

        edtToDateID = new EditText(mContext);
        RelativeLayout.LayoutParams edtToDateCountryParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        edtToDateCountryParam.setMarginStart(20);
        edtToDateCountryParam.setMarginEnd(20);
        edtToDateCountryParam.addRule(RelativeLayout.CENTER_VERTICAL);
        edtToDateID.setGravity(Gravity.CENTER_VERTICAL);
        // edtDepartureDateCountryID.setHint("Select Departure Date");
        edtToDateID.setHint(getResources().getString(R.string.Totxt));
        edtToDateID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
        edtToDateID.setTextColor(getResources().getColor(R.color.black));
        edtToDateID.setBackgroundColor(getResources().getColor(R.color.white));
        edtToDateID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
        edtToDateID.setTextSize((float) 13);
        edtToDateID.setFocusable(false);
        edtToDateID.setId(mIDIndex + 7);


        edtToDateID.setFocusableInTouchMode(false);
        edtToDateID.setClickable(true);
        edtToDateID.setTypeface(null, Typeface.NORMAL);
        edtToDateID.setLayoutParams(edtToDateCountryParam);


        mEditText_ToIDList.add(edtToDateID);/////List

        edtToDateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int iii = v.getId();
                final int vvv = iii - 7;

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
                        //edtToDateID.setText(i2 + "/" + i1 + "/" + i);
                        mEditText_ToIDList.get(vvv).setText(i2 + "/" + i1 + "/" + i);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("End Date");
                edtRegionID.requestFocus();
                datePickerDialog.show();
            }
        });

        iv_sub_linearlayout12.addView(edtToDateID);


        View mViewToDate = new View(mContext);
        RelativeLayout.LayoutParams mParamViewToDate = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
        mParamViewToDate.setMarginEnd(0);
        mParamViewToDate.setMarginStart(0);
        mParamViewToDate.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mViewToDate.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
        mViewToDate.setLayoutParams(mParamViewToDate);
        iv_sub_linearlayout12.addView(mViewToDate);

        //////////////////Region

        TextView txtRegionTextHead = new TextView(mContext);
        RelativeLayout.LayoutParams txtRegionHeadParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtRegionHeadParam.setMarginStart(20);
        txtRegionHeadParam.setMargins(0, 10, 0, 5);
        txtRegionHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        txtRegionTextHead.setGravity(Gravity.CENTER_VERTICAL);
        //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
        txtRegionTextHead.setText(getResources().getString(R.string.country));
        txtRegionTextHead.setTextSize((float) 15);
        txtRegionTextHead.setTypeface(null, Typeface.NORMAL);
        //  tv_Code.setId(i + 2);
        txtRegionTextHead.setTextColor(getResources().getColor(R.color.black));
        txtRegionTextHead.setLayoutParams(txtRegionHeadParam);
        iv_sub_linearlayout12.addView(txtRegionTextHead);

        edtRegionID = new AutoCompleteTextView(mContext);
        RelativeLayout.LayoutParams edtRegionParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        edtRegionParam.setMarginStart(20);
        edtRegionParam.setMarginEnd(20);
        edtRegionParam.addRule(RelativeLayout.CENTER_VERTICAL);
        edtRegionID.setGravity(Gravity.CENTER_VERTICAL);
        // edtDepartureDateCountryID.setHint("Select Departure Date");
        edtRegionID.setHint(getResources().getString(R.string.country));
        edtRegionID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
        edtRegionID.setTextColor(getResources().getColor(R.color.black));
        edtRegionID.setBackgroundColor(getResources().getColor(R.color.white));
        edtRegionID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
        edtRegionID.setTextSize((float) 13);
        edtRegionID.setFocusable(false);
        edtRegionID.setId(mIDIndex + 8);
        edtRegionID.setFocusableInTouchMode(false);
        edtRegionID.setClickable(true);
        edtRegionID.setTypeface(null, Typeface.NORMAL);
        edtRegionID.setLayoutParams(edtRegionParam);

        edtRegionID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iii = view.getId();
                coun = iii - 8;

                new Country1(mContext).execute();
            }
        });
        mEditText_RegionIDList.add(edtRegionID);/////List


        iv_sub_linearlayout12.addView(edtRegionID);


        View mViewRegion = new View(mContext);
        RelativeLayout.LayoutParams mParamViewRegion = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
        mParamViewRegion.setMarginEnd(0);
        mParamViewRegion.setMarginStart(0);
        mParamViewRegion.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mViewRegion.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
        mViewRegion.setLayoutParams(mParamViewRegion);
        iv_sub_linearlayout12.addView(mViewRegion);

        //////////////////Description


        TextView txtDescriptionTextHead = new TextView(mContext);
        RelativeLayout.LayoutParams txtDescriptionHeadParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtDescriptionHeadParam.setMarginStart(20);
        txtDescriptionHeadParam.setMargins(0, 10, 0, 5);
        txtDescriptionHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        txtDescriptionTextHead.setGravity(Gravity.CENTER_VERTICAL);
        //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
        txtDescriptionTextHead.setText(getResources().getString(R.string.Description));
        txtDescriptionTextHead.setTextSize((float) 15);
        txtDescriptionTextHead.setTypeface(null, Typeface.NORMAL);
        //  tv_Code.setId(i + 2);
        txtDescriptionTextHead.setTextColor(getResources().getColor(R.color.black));
        txtDescriptionTextHead.setLayoutParams(txtDescriptionHeadParam);
        iv_sub_linearlayout12.addView(txtDescriptionTextHead);

        edtDescriptionID = new EditText(mContext);
        RelativeLayout.LayoutParams edtDescriptionParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        edtDescriptionParam.setMarginStart(20);
        edtDescriptionParam.setMarginEnd(20);
        edtDescriptionParam.addRule(RelativeLayout.CENTER_VERTICAL);
        edtDescriptionID.setGravity(Gravity.CENTER_VERTICAL);
        // edtDepartureDateCountryID.setHint("Select Departure Date");
        edtDescriptionID.setHint(getResources().getString(R.string.Description));
        edtDescriptionID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
        edtDescriptionID.setTextColor(getResources().getColor(R.color.black));
        edtDescriptionID.setBackgroundColor(getResources().getColor(R.color.white));
        edtDescriptionID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
        edtDescriptionID.setTextSize((float) 13);
        edtDescriptionID.setFocusable(true);
        edtDescriptionID.setId(mIDIndex + 9);


        edtDescriptionID.setFocusableInTouchMode(true);
        edtDescriptionID.setClickable(true);
        edtDescriptionID.setTypeface(null, Typeface.NORMAL);
        edtDescriptionID.setLayoutParams(edtDescriptionParam);


        mEditText_DescriptionIDList.add(edtDescriptionID);/////List

        iv_sub_linearlayout12.addView(edtDescriptionID);


        View mViewDescription = new View(mContext);
        RelativeLayout.LayoutParams mParamViewDescription = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
        mParamViewDescription.setMarginEnd(0);
        mParamViewDescription.setMarginStart(0);
        mParamViewDescription.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mViewDescription.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
        mViewDescription.setLayoutParams(mParamViewDescription);
        iv_sub_linearlayout12.addView(mViewDescription);


        //////////////////Location

        TextView txtLocationTextHead = new TextView(mContext);
        RelativeLayout.LayoutParams txtLocationHeadParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtLocationHeadParam.setMarginStart(20);
        txtLocationHeadParam.setMargins(0, 10, 0, 5);
        txtLocationHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        txtLocationTextHead.setGravity(Gravity.CENTER_VERTICAL);
        //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
        txtLocationTextHead.setText(getResources().getString(R.string.Location));
        txtLocationTextHead.setTextSize((float) 15);
        txtLocationTextHead.setTypeface(null, Typeface.NORMAL);
        //  tv_Code.setId(i + 2);
        txtLocationTextHead.setTextColor(getResources().getColor(R.color.black));
        txtLocationTextHead.setLayoutParams(txtLocationHeadParam);
        iv_sub_linearlayout12.addView(txtLocationTextHead);

        edtLocationID = new EditText(mContext);
        RelativeLayout.LayoutParams edtLocationParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        edtLocationParam.setMarginStart(20);
        edtLocationParam.setMarginEnd(20);
        edtLocationParam.addRule(RelativeLayout.CENTER_VERTICAL);
        edtLocationID.setGravity(Gravity.CENTER_VERTICAL);
        // edtDepartureDateCountryID.setHint("Select Departure Date");
        edtLocationID.setHint(getResources().getString(R.string.Location));
        edtLocationID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
        edtLocationID.setTextColor(getResources().getColor(R.color.black));
        edtLocationID.setBackgroundColor(getResources().getColor(R.color.white));
        edtLocationID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
        edtLocationID.setTextSize((float) 13);
        edtLocationID.setFocusable(true);
        edtLocationID.setId(mIDIndex + 11);


        edtLocationID.setFocusableInTouchMode(true);
        edtLocationID.setClickable(true);
        edtLocationID.setTypeface(null, Typeface.NORMAL);
        edtLocationID.setLayoutParams(edtLocationParam);


        mEditText_LocationIDList.add(edtLocationID);/////List

        iv_sub_linearlayout12.addView(edtLocationID);


        View mViewLocation = new View(mContext);
        RelativeLayout.LayoutParams mParamViewLocation = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
        mParamViewLocation.setMarginEnd(0);
        mParamViewLocation.setMarginStart(0);
        mParamViewLocation.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mViewLocation.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
        mViewLocation.setLayoutParams(mParamViewLocation);
        iv_sub_linearlayout12.addView(mViewLocation);


        //////////////////////////////GSTIN NO

        TextView txtGstTextHead = new TextView(mContext);
        RelativeLayout.LayoutParams txtGstHeadParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        txtGstHeadParam.setMarginStart(20);
        txtGstHeadParam.setMargins(0, 10, 0, 5);
        txtGstHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
        txtGstTextHead.setGravity(Gravity.CENTER_VERTICAL);
        //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
        txtGstTextHead.setText(getResources().getString(R.string.card));
        txtGstTextHead.setTextSize((float) 15);
        txtGstTextHead.setTypeface(null, Typeface.NORMAL);
        //  tv_Code.setId(i + 2);
        txtGstTextHead.setTextColor(getResources().getColor(R.color.black));
        txtGstTextHead.setLayoutParams(txtGstHeadParam);
        iv_sub_linearlayout12.addView(txtGstTextHead);

        edtGstInID = new EditText(mContext);
        RelativeLayout.LayoutParams edtGSTParam = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
        edtGSTParam.setMarginStart(20);
        edtGSTParam.setMarginEnd(20);
        edtGSTParam.addRule(RelativeLayout.CENTER_VERTICAL);
        edtGstInID.setGravity(Gravity.CENTER_VERTICAL);
        // edtDepartureDateCountryID.setHint("Select Departure Date");
        edtGstInID.setHint(getResources().getString(R.string.card));
        edtGstInID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
        edtGstInID.setTextColor(getResources().getColor(R.color.black));
        edtGstInID.setBackgroundColor(getResources().getColor(R.color.white));
        // edtGstInID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
        edtGstInID.setTextSize((float) 13);
        edtGstInID.setFocusable(true);
        edtGstInID.setId(mIDIndex + 11);


        edtGstInID.setFocusableInTouchMode(true);
        edtGstInID.setClickable(true);
        edtGstInID.setTypeface(null, Typeface.NORMAL);
        edtGstInID.setLayoutParams(edtLocationParam);


        mEditText_GSTINIDList.add(edtGstInID);/////List

        iv_sub_linearlayout12.addView(edtGstInID);


        View mViewGST = new View(mContext);
        RelativeLayout.LayoutParams mParamViewGST = new RelativeLayout.LayoutParams
                ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
        mParamViewGST.setMarginEnd(0);
        mParamViewGST.setMarginStart(0);
        mParamViewGST.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mViewGST.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
        mViewGST.setLayoutParams(mParamViewGST);
        iv_sub_linearlayout12.addView(mViewGST);

        rlvMainDynamicViewID.addView(iv_sub_linearlayout12);

        if (mIDIndex > 0) {
            Log.e("INDEX", "&&&&" + mIDIndex);
            imgCancleBTNID = new ImageView(mContext);
            RelativeLayout.LayoutParams imgCancleBTNIDParam = new RelativeLayout.LayoutParams
                    ((int) 90, (int) 90);
            imgCancleBTNIDParam.setMarginEnd(10);
            imgCancleBTNIDParam.setMargins(0, 10, 0, 5);
            imgCancleBTNIDParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            imgCancleBTNID.setImageResource(R.drawable.ic_add_trip_close);
            imgCancleBTNID.setLayoutParams(imgCancleBTNIDParam);
            imgCancleBTNID.setPadding(30, 1, 1, 30);
            imgCancleBTNID.setId(mIDIndex + 11);
            imgCancleBTNID.setTag(mTagIDIndex);

            imgCancleBTNID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // cardViewAddDynamicViewID.removeView(rlvMainDynamicViewID);
                    int mmiii = v.getId();

                    Log.e("ID1", "1111" + mmiii);

                    int mmoo = mmiii - 11;

                    Log.e("ID2", "2222" + mmoo);
                    dataHelper.deleteExpTravelEntry(String.valueOf(mmoo + 1));
                    mEditText_ExpensesIDList.remove(mmoo);
                    mEditText_AmountIDList.remove(mmoo);
                    mEditText_CurrencyIDList.remove(mmoo);
                    mEditText_FromIDList.remove(mmoo);
                    mEditText_ToIDList.remove(mmoo);
                    mEditText_RegionIDList.remove(mmoo);
                    mEditText_DescriptionIDList.remove(mmoo);
                    mEditText_LocationIDList.remove(mmoo);
                    mEditText_GSTINIDList.remove(mmoo);

                    mCardView_MainCardIDList.remove(mmoo);
                    mImageView_CancelIDList.remove(mmoo);

                    if (mEditText_ExpensesIDList_Old.size() > 0) {
                        mEditText_ExpensesIDList_Old.clear();
                        mEditText_AmountIDList_Old.clear();
                        mEditText_CurrencyIDList_Old.clear();
                        mEditText_FromIDList_Old.clear();
                        mEditText_ToIDList_Old.clear();
                        mEditText_RegionIDList_Old.clear();
                        mEditText_DescriptionIDList_Old.clear();
                        mEditText_LocationIDList_Old.clear();
                        mEditText_GSTINIDList_Old.clear();
                    }


                    mEditText_ExpensesIDList_Old.addAll(mEditText_ExpensesIDList);
                    mEditText_AmountIDList_Old.addAll(mEditText_AmountIDList);
                    mEditText_CurrencyIDList_Old.addAll(mEditText_CurrencyIDList);
                    mEditText_FromIDList_Old.addAll(mEditText_FromIDList);
                    mEditText_ToIDList_Old.addAll(mEditText_ToIDList);
                    mEditText_RegionIDList_Old.addAll(mEditText_RegionIDList);
                    mEditText_DescriptionIDList_Old.addAll(mEditText_DescriptionIDList);
                    mEditText_LocationIDList_Old.addAll(mEditText_LocationIDList);
                    mEditText_GSTINIDList_Old.addAll(mEditText_GSTINIDList);


                    mEditText_ExpensesIDList.clear();
                    mEditText_AmountIDList.clear();
                    mEditText_CurrencyIDList.clear();
                    mEditText_RegionIDList.clear();
                    mEditText_DescriptionIDList.clear();
                    mEditText_LocationIDList.clear();
                    mEditText_GSTINIDList.clear();
                    mEditText_FromIDList.clear();
                    mEditText_ToIDList.clear();
                    mCardView_MainCardIDList.clear();
                    mImageView_CancelIDList.clear();

                    addDynamicAfterDeleteViewPro();

                }
            });

            mImageView_CancelIDList.add(imgCancleBTNID);

            rlvMainDynamicViewID.addView(imgCancleBTNID);
        } else {
            mImageView_CancelIDList.add(imgCancleBTNID);
        }


        mTagIDIndex++;


        cardViewAddDynamicViewID.addView(rlvMainDynamicViewID);
        lvlMainParentLayoutID.addView(cardViewAddDynamicViewID);

    }

    public void setData() {


        Log.e("11111", "22" + travelEntryExpDocBean.getStart_date());
        Log.e("11111", "33" + travelEntryExpDocBean.getEnd_date());
        Log.e("11111", "44" + travelEntryExpDocBean.getLocation());
        Log.e("11111", "55" + travelEntryExpDocBean.getCountry());
        Log.e("11111", "66" + travelEntryExpDocBean.getSerialno());
        Log.e("11111", "77" + travelEntryExpDocBean.getExpenses_type());
        Log.e("11111", "88" + travelEntryExpDocBean.getAmount());
        Log.e("11111", "99" + travelEntryExpDocBean.getCurrency());
        Log.e("11111", "00" + travelEntryExpDocBean.getTax_code());
        Log.e("11111", "11" + travelEntryExpDocBean.getRegion());

        if (!TextUtils.isEmpty(travelEntryExpDocBean.getStart_date()) || !travelEntryExpDocBean.getStart_date().equals("null")) {
            travelEntryExpDocBean.setStart_date(travelEntryExpDocBean.getStart_date());
            trav_date.setText(travelEntryExpDocBean.getStart_date());

        }

        if (!TextUtils.isEmpty(travelEntryExpDocBean.getEnd_date()) || !travelEntryExpDocBean.getEnd_date().equals("null")) {
            travelEntryExpDocBean.setEnd_date(travelEntryExpDocBean.getEnd_date());
            end_date.setText(travelEntryExpDocBean.getEnd_date());
        }


        if (!TextUtils.isEmpty(travelEntryExpDocBean.getCountry()) || !travelEntryExpDocBean.getCountry().equals("null")) {
            travelEntryExpDocBean.setCountry(travelEntryExpDocBean.getCountry());
            trav_country.setText(travelEntryExpDocBean.getCountry());
        }


        if (!TextUtils.isEmpty(travelEntryExpDocBean.getLocation()) || !travelEntryExpDocBean.getLocation().equals("null")) {
            travelEntryExpDocBean.setLocation(travelEntryExpDocBean.getLocation());
            location.setText(travelEntryExpDocBean.getLocation());
        }

        for (int i = 0; i < mEditText_FromIDList.size(); i++) {

            if (!TextUtils.isEmpty(travelEntryExpDocBean.getExpenses_type()) || !travelEntryExpDocBean.getExpenses_type().equals("null")) {
                travelEntryExpDocBean.setExpenses_type(travelEntryExpDocBean.getExpenses_type());
                edtExpensesID.setText(travelEntryExpDocBean.getExpenses_type());
            }

            if (!TextUtils.isEmpty(travelEntryExpDocBean.getAmount()) || !travelEntryExpDocBean.getAmount().equals("null")) {
                travelEntryExpDocBean.setAmount(travelEntryExpDocBean.getAmount());
                edtAmountID.setText(travelEntryExpDocBean.getAmount());
            }

            if (!TextUtils.isEmpty(travelEntryExpDocBean.getCurrency()) || !travelEntryExpDocBean.getCurrency().equals("null")) {
                travelEntryExpDocBean.setCurrency(travelEntryExpDocBean.getCurrency());
                edtCurrencyID.setText(travelEntryExpDocBean.getCurrency());
            }

            if (!TextUtils.isEmpty(travelEntryExpDocBean.getFrom_date()) || !travelEntryExpDocBean.getTo_date().equals("null")) {
                travelEntryExpDocBean.setFrom_date(travelEntryExpDocBean.getFrom_date());
                edtFromDateID.setText(travelEntryExpDocBean.getFrom_date());
            }

            if (!TextUtils.isEmpty(travelEntryExpDocBean.getTo_date()) || !travelEntryExpDocBean.getTo_date().equals("null")) {
                travelEntryExpDocBean.setTo_date(travelEntryExpDocBean.getTo_date());
                edtToDateID.setText(travelEntryExpDocBean.getTo_date());
            }


            if (!TextUtils.isEmpty(travelEntryExpDocBean.getRegion()) || !travelEntryExpDocBean.getRegion().equals("null")) {
                travelEntryExpDocBean.setRegion(travelEntryExpDocBean.getRegion());
                edtRegionID.setText(travelEntryExpDocBean.getRegion());
            }

            if (!TextUtils.isEmpty(travelEntryExpDocBean.getDescription()) || !travelEntryExpDocBean.getDescription().equals("null")) {
                travelEntryExpDocBean.setDescription(travelEntryExpDocBean.getDescription());
                edtDescriptionID.setText(travelEntryExpDocBean.getDescription());
            }

            if (!TextUtils.isEmpty(travelEntryExpDocBean.getLocation1()) || !travelEntryExpDocBean.getLocation1().equals("null")) {
                travelEntryExpDocBean.setLocation1(travelEntryExpDocBean.getLocation1());
                edtLocationID.setText(travelEntryExpDocBean.getLocation1());
            }

            if (!TextUtils.isEmpty(travelEntryExpDocBean.getCardinfo()) || !travelEntryExpDocBean.getCardinfo().equals("null")) {
                travelEntryExpDocBean.setCardinfo(travelEntryExpDocBean.getCardinfo());
                edtGstInID.setText(travelEntryExpDocBean.getCardinfo());
            }

        }

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

    private void addDynamicAfterDeleteViewPro() {


        try {
            // lvlDynamicPriceLayout.addView(iv_sub_linearlayout12);

            lvlMainParentLayoutID.removeAllViews();


        } catch (Exception exp) {
            exp.printStackTrace();
        }

        for (int k = 0; k < mEditText_FromIDList_Old.size(); k++) {

            mIDIndexOnesRun = k;
            mIDIndex = mIDIndexOnesRun;


            cardViewAddDynamicViewID = new CardView(mContext);
            CardView.LayoutParams cardViewAddDynamicViewIDoutparams12 = new CardView.LayoutParams
                    ((int) CardView.LayoutParams.MATCH_PARENT, (int) CardView.LayoutParams.WRAP_CONTENT);
            cardViewAddDynamicViewIDoutparams12.setMarginEnd(5);
            cardViewAddDynamicViewIDoutparams12.setMarginStart(5);
            cardViewAddDynamicViewIDoutparams12.setMargins(5, 10, 5, 10);
            cardViewAddDynamicViewID.setCardBackgroundColor(getResources().getColor(R.color.white));
            cardViewAddDynamicViewID.setRadius(10);
            cardViewAddDynamicViewID.setId(mIDIndex + 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cardViewAddDynamicViewID.setElevation(5);
            }
            cardViewAddDynamicViewID.setLayoutParams(cardViewAddDynamicViewIDoutparams12);


            mCardView_MainCardIDList.add(cardViewAddDynamicViewID);


            rlvMainDynamicViewID = new RelativeLayout(mContext);
            RelativeLayout.LayoutParams rlvMainDynamicViewIDParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);

            rlvMainDynamicViewIDParam.setMargins(10, 0, 10, 5);
            rlvMainDynamicViewIDParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            rlvMainDynamicViewID.setLayoutParams(rlvMainDynamicViewIDParam);

            // cardViewAddDynamicViewID.addView(rlvMainDynamicViewID);


            iv_sub_linearlayout12 = new LinearLayout(mContext);
            LinearLayout.LayoutParams iv_outparams12 = new LinearLayout.LayoutParams
                    ((int) LinearLayout.LayoutParams.MATCH_PARENT, (int) LinearLayout.LayoutParams.MATCH_PARENT);
            iv_outparams12.setMarginEnd(0);
            iv_outparams12.setMarginStart(0);
            iv_outparams12.setMargins(0, 15, 0, 5);
            iv_sub_linearlayout12.setBackgroundColor(getResources().getColor(R.color.white));
            iv_sub_linearlayout12.setOrientation(LinearLayout.VERTICAL);
            iv_sub_linearlayout12.setLayoutParams(iv_outparams12);

            //////////////////to expenses
            TextView txtExpTextHead = new TextView(mContext);
            RelativeLayout.LayoutParams txtExpTextHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtExpTextHeadParam.setMarginStart(20);
            txtExpTextHeadParam.setMargins(0, 10, 0, 5);
            txtExpTextHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtExpTextHead.setGravity(Gravity.CENTER_VERTICAL);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtExpTextHead.setText(getResources().getString(R.string.Expensestxt));
            txtExpTextHead.setTextSize((float) 15);
            txtExpTextHead.setTypeface(null, Typeface.NORMAL);
            //  tv_Code.setId(i + 2);
            txtExpTextHead.setTextColor(getResources().getColor(R.color.black));
            txtExpTextHead.setLayoutParams(txtExpTextHeadParam);
            iv_sub_linearlayout12.addView(txtExpTextHead);


            edtExpensesID = new AutoCompleteTextView(mContext);
            RelativeLayout.LayoutParams edtExpensesParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            edtExpensesParam.setMarginStart(20);
            edtExpensesParam.setMarginEnd(20);
            edtExpensesParam.addRule(RelativeLayout.CENTER_VERTICAL);
            edtExpensesID.setGravity(Gravity.CENTER_VERTICAL);
            // edtFomeCountryID.setHint("Select City ,State, Country");
            edtExpensesID.setHint(getResources().getString(R.string.Expensestxt));
            edtExpensesID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
            edtExpensesID.setTextColor(getResources().getColor(R.color.black));
            edtExpensesID.setBackgroundColor(getResources().getColor(R.color.white));
            edtExpensesID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
            edtExpensesID.setTextSize((float) 13);
            edtExpensesID.setText(mEditText_ExpensesIDList_Old.get(k).getText().toString().trim());
            edtExpensesID.setFocusable(true);
            edtExpensesID.setFocusableInTouchMode(false);
            edtExpensesID.setClickable(true);
            edtExpensesID.setId(mIDIndex + 2);
            edtExpensesID.setTypeface(null, Typeface.NORMAL);
            edtExpensesID.setLayoutParams(edtExpensesParam);

            edtExpensesID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iii = view.getId();
                    exp = iii - 2;
                    new Expenses(mContext, exp).execute();
                }
            });
            mEditText_ExpensesIDList.add(edtExpensesID);/////List

            iv_sub_linearlayout12.addView(edtExpensesID);


            View mView = new View(mContext);
            RelativeLayout.LayoutParams mParamView = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
            mParamView.setMarginEnd(0);
            mParamView.setMarginStart(0);
            mParamView.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mView.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
            mView.setLayoutParams(mParamView);
            iv_sub_linearlayout12.addView(mView);


            //////////////////to Amount

            TextView txtAmtTextHead = new TextView(mContext);
            RelativeLayout.LayoutParams txtAmtTextHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtAmtTextHeadParam.setMarginStart(20);
            txtAmtTextHeadParam.setMargins(0, 10, 0, 5);
            txtAmtTextHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtAmtTextHead.setGravity(Gravity.CENTER_VERTICAL);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtAmtTextHead.setText(getResources().getString(R.string.Amounttxt));
            txtAmtTextHead.setTextSize((float) 15);
            txtAmtTextHead.setTypeface(null, Typeface.NORMAL);
            //  tv_Code.setId(i + 2);
            txtAmtTextHead.setTextColor(getResources().getColor(R.color.black));
            txtAmtTextHead.setLayoutParams(txtAmtTextHeadParam);
            iv_sub_linearlayout12.addView(txtAmtTextHead);


            edtAmountID = new EditText(mContext);
            RelativeLayout.LayoutParams edtAmountParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            edtAmountParam.setMarginStart(20);
            edtAmountParam.setMarginEnd(20);
            edtAmountParam.addRule(RelativeLayout.CENTER_VERTICAL);
            edtAmountID.setGravity(Gravity.CENTER_VERTICAL);
            edtAmountID.setHint(getResources().getString(R.string.Amounttxt));
            edtAmountID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
            edtAmountID.setTextColor(getResources().getColor(R.color.black));
            edtAmountID.setBackgroundColor(getResources().getColor(R.color.white));
            edtAmountID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
            edtAmountID.setTextSize((float) 13);
            edtAmountID.setText(mEditText_AmountIDList_Old.get(k).getText().toString().trim());
            edtAmountID.setFocusable(true);
            edtAmountID.setFocusableInTouchMode(true);
            edtAmountID.setClickable(true);
            edtAmountID.setId(mIDIndex + 3);
            edtAmountID.setTypeface(null, Typeface.NORMAL);
            edtAmountID.setLayoutParams(edtAmountParam);


            mEditText_AmountIDList.add(edtAmountID);/////List


            iv_sub_linearlayout12.addView(edtAmountID);


            View mViewAmt = new View(mContext);
            RelativeLayout.LayoutParams mParamViewAmt = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
            mParamViewAmt.setMarginEnd(0);
            mParamViewAmt.setMarginStart(0);
            mParamViewAmt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mViewAmt.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
            mViewAmt.setLayoutParams(mParamViewAmt);
            iv_sub_linearlayout12.addView(mViewAmt);


            //////////////////Currency

            TextView txtCurrTextHead = new TextView(mContext);
            RelativeLayout.LayoutParams txtCurrHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtCurrHeadParam.setMarginStart(20);
            txtCurrHeadParam.setMargins(0, 10, 0, 5);
            txtCurrHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtCurrTextHead.setGravity(Gravity.CENTER_VERTICAL);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtCurrTextHead.setText(getResources().getString(R.string.Currencytxt));
            txtCurrTextHead.setTextSize((float) 15);
            txtCurrTextHead.setTypeface(null, Typeface.NORMAL);
            //  tv_Code.setId(i + 2);
            txtCurrTextHead.setTextColor(getResources().getColor(R.color.black));
            txtCurrTextHead.setLayoutParams(txtCurrHeadParam);
            iv_sub_linearlayout12.addView(txtCurrTextHead);

            edtCurrencyID = new AutoCompleteTextView(mContext);
            RelativeLayout.LayoutParams edtCurrencyParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            edtCurrencyParam.setMarginStart(20);
            edtCurrencyParam.setMarginEnd(20);
            edtCurrencyParam.addRule(RelativeLayout.CENTER_VERTICAL);
            edtCurrencyID.setGravity(Gravity.CENTER_VERTICAL);
            //edtArrrivalDateCountryID.setHint("Select Arrival Date");
            edtCurrencyID.setHint(getResources().getString(R.string.Currencytxt));
            edtCurrencyID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
            edtCurrencyID.setTextColor(getResources().getColor(R.color.black));
            edtCurrencyID.setBackgroundColor(getResources().getColor(R.color.white));
            edtCurrencyID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
            edtCurrencyID.setTextSize((float) 13);
            edtCurrencyID.setText(mEditText_CurrencyIDList_Old.get(k).getText().toString().trim());
            edtCurrencyID.setFocusable(true);
            edtCurrencyID.setFocusableInTouchMode(false);
            edtCurrencyID.setClickable(true);
            edtCurrencyID.setId(mIDIndex + 4);
            edtCurrencyID.setTypeface(null, Typeface.NORMAL);
            edtCurrencyID.setLayoutParams(edtCurrencyParam);

            edtCurrencyID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int iii = view.getId();
                    exp = iii - 4;
                    new Currency(mContext).execute();
                }
            });
            mEditText_CurrencyIDList.add(edtCurrencyID);/////List

            iv_sub_linearlayout12.addView(edtCurrencyID);


            View mViewCurrency = new View(mContext);
            RelativeLayout.LayoutParams mParamViewCurrency = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
            mParamViewCurrency.setMarginEnd(0);
            mParamViewCurrency.setMarginStart(0);
            mParamViewCurrency.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mViewCurrency.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
            mViewCurrency.setLayoutParams(mParamViewCurrency);
            iv_sub_linearlayout12.addView(mViewCurrency);

            //from date

            TextView txtFromDateTextHead = new TextView(mContext);
            RelativeLayout.LayoutParams txtFromDateHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtFromDateHeadParam.setMarginStart(20);
            txtFromDateHeadParam.setMargins(0, 10, 0, 5);
            txtFromDateHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtFromDateTextHead.setGravity(Gravity.CENTER_VERTICAL);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtFromDateTextHead.setText(getResources().getString(R.string.Fromtxt));
            txtFromDateTextHead.setTextSize((float) 15);
            txtFromDateTextHead.setTypeface(null, Typeface.NORMAL);
            //  tv_Code.setId(i + 2);
            txtFromDateTextHead.setTextColor(getResources().getColor(R.color.black));
            txtFromDateTextHead.setLayoutParams(txtFromDateHeadParam);
            iv_sub_linearlayout12.addView(txtFromDateTextHead);

            edtFromDateID = new EditText(mContext);
            RelativeLayout.LayoutParams txtFromDateTextHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtFromDateTextHeadParam.setMarginStart(20);
            txtFromDateTextHeadParam.setMarginEnd(20);
            txtFromDateTextHeadParam.addRule(RelativeLayout.CENTER_VERTICAL);
            edtFromDateID.setGravity(Gravity.CENTER_VERTICAL);
            //edtArrrivalDateCountryID.setHint("Select Arrival Date");
            edtFromDateID.setHint(getResources().getString(R.string.Fromtxt));
            edtFromDateID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
            edtFromDateID.setTextColor(getResources().getColor(R.color.black));
            edtFromDateID.setBackgroundColor(getResources().getColor(R.color.white));
            edtFromDateID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
            edtFromDateID.setTextSize((float) 13);
            edtFromDateID.setText(mEditText_FromIDList_Old.get(k).getText().toString().trim());
            edtFromDateID.setFocusable(false);
            edtFromDateID.setFocusableInTouchMode(false);
            edtFromDateID.setClickable(true);
            edtFromDateID.setId(mIDIndex + 6);
            edtFromDateID.setTypeface(null, Typeface.NORMAL);
            edtFromDateID.setLayoutParams(txtFromDateTextHeadParam);

            mEditText_FromIDList.add(edtFromDateID);

            edtFromDateID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int iii = v.getId();
                    final int vvv = iii - 6;

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
                            //edtFromDateID.setText(i2 + "/" + i1 + "/" + i);
                            mEditText_FromIDList.get(vvv).setText(i2 + "/" + i1 + "/" + i);

                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.setTitle("From Date");
                    edtToDateID.requestFocus();
                    datePickerDialog.show();


                }
            });
            iv_sub_linearlayout12.addView(edtFromDateID);

            View mViewFromDate = new View(mContext);
            RelativeLayout.LayoutParams mParamViewFromDate = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
            mParamViewFromDate.setMarginEnd(0);
            mParamViewFromDate.setMarginStart(0);
            mParamViewFromDate.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mViewFromDate.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
            mViewFromDate.setLayoutParams(mParamViewFromDate);
            iv_sub_linearlayout12.addView(mViewFromDate);


            //to date

            TextView txtToDateTextHead = new TextView(mContext);
            RelativeLayout.LayoutParams txtToDateHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtToDateHeadParam.setMarginStart(20);
            txtToDateHeadParam.setMargins(0, 10, 0, 5);
            txtToDateHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtToDateTextHead.setGravity(Gravity.CENTER_VERTICAL);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtToDateTextHead.setText(getResources().getString(R.string.Totxt));
            txtToDateTextHead.setTextSize((float) 15);
            txtToDateTextHead.setTypeface(null, Typeface.NORMAL);
            //  tv_Code.setId(i + 2);
            txtToDateTextHead.setTextColor(getResources().getColor(R.color.black));
            txtToDateTextHead.setLayoutParams(txtToDateHeadParam);
            iv_sub_linearlayout12.addView(txtToDateTextHead);

            edtToDateID = new EditText(mContext);
            RelativeLayout.LayoutParams edtToDateCountryParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            edtToDateCountryParam.setMarginStart(20);
            edtToDateCountryParam.setMarginEnd(20);
            edtToDateCountryParam.addRule(RelativeLayout.CENTER_VERTICAL);
            edtToDateID.setGravity(Gravity.CENTER_VERTICAL);
            // edtDepartureDateCountryID.setHint("Select Departure Date");
            edtToDateID.setHint(getResources().getString(R.string.Totxt));
            edtToDateID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
            edtToDateID.setTextColor(getResources().getColor(R.color.black));
            edtToDateID.setBackgroundColor(getResources().getColor(R.color.white));
            edtToDateID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
            edtToDateID.setTextSize((float) 13);
            edtToDateID.setText(mEditText_ToIDList_Old.get(k).getText().toString().trim());
            edtToDateID.setFocusable(false);
            edtToDateID.setId(mIDIndex + 7);


            edtToDateID.setFocusableInTouchMode(false);
            edtToDateID.setClickable(true);
            edtToDateID.setTypeface(null, Typeface.NORMAL);
            edtToDateID.setLayoutParams(edtToDateCountryParam);


            mEditText_ToIDList.add(edtToDateID);/////List

            edtToDateID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int iii = v.getId();
                    final int vvv = iii - 7;

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
                            //edtToDateID.setText(i2 + "/" + i1 + "/" + i);
                            mEditText_ToIDList.get(vvv).setText(i2 + "/" + i1 + "/" + i);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.setTitle("To Date");
                    edtRegionID.requestFocus();
                    datePickerDialog.show();
                }
            });

            iv_sub_linearlayout12.addView(edtToDateID);


            View mViewToDate = new View(mContext);
            RelativeLayout.LayoutParams mParamViewToDate = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
            mParamViewToDate.setMarginEnd(0);
            mParamViewToDate.setMarginStart(0);
            mParamViewToDate.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mViewToDate.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
            mViewToDate.setLayoutParams(mParamViewToDate);
            iv_sub_linearlayout12.addView(mViewToDate);

            //////////////////Region

            TextView txtRegionTextHead = new TextView(mContext);
            RelativeLayout.LayoutParams txtRegionHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtRegionHeadParam.setMarginStart(20);
            txtRegionHeadParam.setMargins(0, 10, 0, 5);
            txtRegionHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtRegionTextHead.setGravity(Gravity.CENTER_VERTICAL);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtRegionTextHead.setText(getResources().getString(R.string.country));
            txtRegionTextHead.setTextSize((float) 15);
            txtRegionTextHead.setTypeface(null, Typeface.NORMAL);
            //  tv_Code.setId(i + 2);
            txtRegionTextHead.setTextColor(getResources().getColor(R.color.black));
            txtRegionTextHead.setLayoutParams(txtRegionHeadParam);
            iv_sub_linearlayout12.addView(txtRegionTextHead);

            edtRegionID = new AutoCompleteTextView(mContext);
            RelativeLayout.LayoutParams edtRegionParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            edtRegionParam.setMarginStart(20);
            edtRegionParam.setMarginEnd(20);
            edtRegionParam.addRule(RelativeLayout.CENTER_VERTICAL);
            edtRegionID.setGravity(Gravity.CENTER_VERTICAL);
            // edtDepartureDateCountryID.setHint("Select Departure Date");
            edtRegionID.setHint(getResources().getString(R.string.country));
            edtRegionID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
            edtRegionID.setTextColor(getResources().getColor(R.color.black));
            edtRegionID.setBackgroundColor(getResources().getColor(R.color.white));
            edtRegionID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
            edtRegionID.setTextSize((float) 13);
            edtRegionID.setText(mEditText_RegionIDList_Old.get(k).getText().toString().trim());
            edtRegionID.setFocusable(false);
            edtRegionID.setId(mIDIndex + 8);
            edtRegionID.setFocusableInTouchMode(false);
            edtRegionID.setClickable(true);
            edtRegionID.setTypeface(null, Typeface.NORMAL);
            edtRegionID.setLayoutParams(edtRegionParam);
            edtRegionID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int iii = view.getId();
                    coun = iii - 8;

                    new Country1(mContext).execute();
                }
            });

            mEditText_RegionIDList.add(edtRegionID);/////List

            iv_sub_linearlayout12.addView(edtRegionID);


            View mViewRegion = new View(mContext);
            RelativeLayout.LayoutParams mParamViewRegion = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
            mParamViewRegion.setMarginEnd(0);
            mParamViewRegion.setMarginStart(0);
            mParamViewRegion.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mViewRegion.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
            mViewRegion.setLayoutParams(mParamViewRegion);
            iv_sub_linearlayout12.addView(mViewRegion);

            //////////////////Description


            TextView txtDescriptionTextHead = new TextView(mContext);
            RelativeLayout.LayoutParams txtDescriptionHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtDescriptionHeadParam.setMarginStart(20);
            txtDescriptionHeadParam.setMargins(0, 10, 0, 5);
            txtDescriptionHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtDescriptionTextHead.setGravity(Gravity.CENTER_VERTICAL);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtDescriptionTextHead.setText(getResources().getString(R.string.Description));
            txtDescriptionTextHead.setTextSize((float) 15);
            txtDescriptionTextHead.setTypeface(null, Typeface.NORMAL);
            //  tv_Code.setId(i + 2);
            txtDescriptionTextHead.setTextColor(getResources().getColor(R.color.black));
            txtDescriptionTextHead.setLayoutParams(txtDescriptionHeadParam);
            iv_sub_linearlayout12.addView(txtDescriptionTextHead);

            edtDescriptionID = new EditText(mContext);
            RelativeLayout.LayoutParams edtDescriptionParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            edtDescriptionParam.setMarginStart(20);
            edtDescriptionParam.setMarginEnd(20);
            edtDescriptionParam.addRule(RelativeLayout.CENTER_VERTICAL);
            edtDescriptionID.setGravity(Gravity.CENTER_VERTICAL);
            // edtDepartureDateCountryID.setHint("Select Departure Date");
            edtDescriptionID.setHint(getResources().getString(R.string.Description));
            edtDescriptionID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
            edtDescriptionID.setTextColor(getResources().getColor(R.color.black));
            edtDescriptionID.setBackgroundColor(getResources().getColor(R.color.white));
            edtDescriptionID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
            edtDescriptionID.setTextSize((float) 13);
            edtDescriptionID.setText(mEditText_DescriptionIDList_Old.get(k).getText().toString().trim());
            edtDescriptionID.setFocusable(true);
            edtDescriptionID.setId(mIDIndex + 9);


            edtDescriptionID.setFocusableInTouchMode(true);
            edtDescriptionID.setClickable(true);
            edtDescriptionID.setTypeface(null, Typeface.NORMAL);
            edtDescriptionID.setLayoutParams(edtDescriptionParam);


            mEditText_DescriptionIDList.add(edtDescriptionID);/////List

            iv_sub_linearlayout12.addView(edtDescriptionID);


            View mViewDescription = new View(mContext);
            RelativeLayout.LayoutParams mParamViewDescription = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
            mParamViewDescription.setMarginEnd(0);
            mParamViewDescription.setMarginStart(0);
            mParamViewDescription.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mViewDescription.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
            mViewDescription.setLayoutParams(mParamViewDescription);
            iv_sub_linearlayout12.addView(mViewDescription);


            //////////////////Location

            TextView txtLocationTextHead = new TextView(mContext);
            RelativeLayout.LayoutParams txtLocationHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtLocationHeadParam.setMarginStart(20);
            txtLocationHeadParam.setMargins(0, 10, 0, 5);
            txtLocationHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtLocationTextHead.setGravity(Gravity.CENTER_VERTICAL);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtLocationTextHead.setText(getResources().getString(R.string.Location));
            txtLocationTextHead.setTextSize((float) 15);
            txtLocationTextHead.setTypeface(null, Typeface.NORMAL);
            //  tv_Code.setId(i + 2);
            txtLocationTextHead.setTextColor(getResources().getColor(R.color.black));
            txtLocationTextHead.setLayoutParams(txtLocationHeadParam);
            iv_sub_linearlayout12.addView(txtLocationTextHead);

            edtLocationID = new EditText(mContext);
            RelativeLayout.LayoutParams edtLocationParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            edtLocationParam.setMarginStart(20);
            edtLocationParam.setMarginEnd(20);
            edtLocationParam.addRule(RelativeLayout.CENTER_VERTICAL);
            edtLocationID.setGravity(Gravity.CENTER_VERTICAL);
            // edtDepartureDateCountryID.setHint("Select Departure Date");
            edtLocationID.setHint(getResources().getString(R.string.Location));
            edtLocationID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
            edtLocationID.setTextColor(getResources().getColor(R.color.black));
            edtLocationID.setBackgroundColor(getResources().getColor(R.color.white));
            edtLocationID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
            edtLocationID.setTextSize((float) 13);
            edtLocationID.setText(mEditText_LocationIDList_Old.get(k).getText().toString().trim());
            edtLocationID.setFocusable(true);
            edtLocationID.setId(mIDIndex + 11);
            edtLocationID.setFocusableInTouchMode(true);
            edtLocationID.setClickable(true);
            edtLocationID.setTypeface(null, Typeface.NORMAL);
            edtLocationID.setLayoutParams(edtLocationParam);


            mEditText_LocationIDList.add(edtLocationID);

            iv_sub_linearlayout12.addView(edtLocationID);


            View mViewLocation = new View(mContext);
            RelativeLayout.LayoutParams mParamViewLocation = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
            mParamViewLocation.setMarginEnd(0);
            mParamViewLocation.setMarginStart(0);
            mParamViewLocation.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mViewLocation.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
            mViewLocation.setLayoutParams(mParamViewLocation);
            iv_sub_linearlayout12.addView(mViewLocation);


            //////////////////////////////GSTIN NO

            TextView txtGstTextHead = new TextView(mContext);
            RelativeLayout.LayoutParams txtGstHeadParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.WRAP_CONTENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            txtGstHeadParam.setMarginStart(20);
            txtGstHeadParam.setMargins(0, 10, 0, 5);
            txtGstHeadParam.addRule(RelativeLayout.ALIGN_PARENT_START);
            txtGstTextHead.setGravity(Gravity.CENTER_VERTICAL);
            //   tv_Code.setText( mNRKProductDetailsVariantResponseListin.get(i).getVariantModalNumber());
            txtGstTextHead.setText(getResources().getString(R.string.card));
            txtGstTextHead.setTextSize((float) 15);
            txtGstTextHead.setTypeface(null, Typeface.NORMAL);
            //  tv_Code.setId(i + 2);
            txtGstTextHead.setTextColor(getResources().getColor(R.color.black));
            txtGstTextHead.setLayoutParams(txtGstHeadParam);
            iv_sub_linearlayout12.addView(txtGstTextHead);

            edtGstInID = new EditText(mContext);
            RelativeLayout.LayoutParams edtGSTParam = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) RelativeLayout.LayoutParams.WRAP_CONTENT);
            edtGSTParam.setMarginStart(20);
            edtGSTParam.setMarginEnd(20);
            edtGSTParam.addRule(RelativeLayout.CENTER_VERTICAL);
            edtGstInID.setGravity(Gravity.CENTER_VERTICAL);
            // edtDepartureDateCountryID.setHint("Select Departure Date");
            edtGstInID.setHint(getResources().getString(R.string.card));
            edtGstInID.setHintTextColor(getResources().getColor(R.color.colorSeprator));
            edtGstInID.setTextColor(getResources().getColor(R.color.black));
            edtGstInID.setBackgroundColor(getResources().getColor(R.color.white));
            edtGstInID.setTextSize((float) 13);
            edtGstInID.setText(mEditText_GSTINIDList_Old.get(k).getText().toString().trim());
            // edtGstInID.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mendotry, 0);
            edtGstInID.setFocusable(true);
            edtGstInID.setId(mIDIndex + 11);


            edtGstInID.setFocusableInTouchMode(true);
            edtGstInID.setClickable(true);
            edtGstInID.setTypeface(null, Typeface.NORMAL);
            edtGstInID.setLayoutParams(edtLocationParam);


            mEditText_GSTINIDList.add(edtGstInID);

            iv_sub_linearlayout12.addView(edtGstInID);


            View mViewGST = new View(mContext);
            RelativeLayout.LayoutParams mParamViewGST = new RelativeLayout.LayoutParams
                    ((int) RelativeLayout.LayoutParams.MATCH_PARENT, (int) 1);
            mParamViewGST.setMarginEnd(0);
            mParamViewGST.setMarginStart(0);
            mParamViewGST.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mViewGST.setBackgroundColor(getResources().getColor(R.color.ViewDividerColor));
            mViewGST.setLayoutParams(mParamViewGST);
            iv_sub_linearlayout12.addView(mViewGST);

            rlvMainDynamicViewID.addView(iv_sub_linearlayout12);

            if (mIDIndex > 0) {
                Log.e("INDEX", "&&&&" + mIDIndex);
                imgCancleBTNID = new ImageView(mContext);
                RelativeLayout.LayoutParams imgCancleBTNIDParam = new RelativeLayout.LayoutParams
                        ((int) 80, (int) 80);
                imgCancleBTNIDParam.setMarginEnd(10);
                imgCancleBTNIDParam.setMargins(0, 10, 0, 5);
                imgCancleBTNIDParam.addRule(RelativeLayout.ALIGN_PARENT_END);
                imgCancleBTNID.setImageResource(R.drawable.ic_add_trip_close);
                imgCancleBTNID.setLayoutParams(imgCancleBTNIDParam);
                imgCancleBTNID.setPadding(30, 1, 1, 30);
                imgCancleBTNID.setId(mIDIndex + 11);
                imgCancleBTNID.setTag(mTagIDIndex);

                imgCancleBTNID.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // cardViewAddDynamicViewID.removeView(rlvMainDynamicViewID);

                        int mmiii = v.getId();

                        Log.e("ID3", "3333" + mmiii);

                        int mmoo = mmiii - 11;

                        Log.e("ID4", "4444" + mmoo);
                        dataHelper.deleteExpTravelEntry(String.valueOf(mmoo + 1));
                        mEditText_ExpensesIDList.remove(mmoo);
                        mEditText_AmountIDList.remove(mmoo);
                        mEditText_CurrencyIDList.remove(mmoo);

                        mEditText_FromIDList.remove(mmoo);
                        mEditText_ToIDList.remove(mmoo);
                        mEditText_RegionIDList.remove(mmoo);
                        mEditText_DescriptionIDList.remove(mmoo);
                        mEditText_LocationIDList.remove(mmoo);
                        mEditText_GSTINIDList.remove(mmoo);

                        mCardView_MainCardIDList.remove(mmoo);
                        mImageView_CancelIDList.remove(mmoo);

                        mEditText_ExpensesIDList_Old.clear();
                        mEditText_AmountIDList_Old.clear();
                        mEditText_CurrencyIDList_Old.clear();
                        mEditText_FromIDList_Old.clear();
                        mEditText_ToIDList_Old.clear();
                        mEditText_RegionIDList_Old.clear();
                        mEditText_DescriptionIDList_Old.clear();
                        mEditText_LocationIDList_Old.clear();
                        mEditText_GSTINIDList_Old.clear();

                        mEditText_ExpensesIDList_Old.addAll(mEditText_ExpensesIDList);
                        mEditText_AmountIDList_Old.addAll(mEditText_AmountIDList);
                        mEditText_CurrencyIDList_Old.addAll(mEditText_CurrencyIDList);
                        mEditText_FromIDList_Old.addAll(mEditText_FromIDList);
                        mEditText_ToIDList_Old.addAll(mEditText_ToIDList);
                        mEditText_RegionIDList_Old.addAll(mEditText_RegionIDList);
                        mEditText_DescriptionIDList_Old.addAll(mEditText_DescriptionIDList);
                        mEditText_LocationIDList_Old.addAll(mEditText_LocationIDList);
                        mEditText_GSTINIDList_Old.addAll(mEditText_GSTINIDList);


                        mEditText_ExpensesIDList.clear();
                        mEditText_AmountIDList.clear();
                        mEditText_CurrencyIDList.clear();
                        mEditText_RegionIDList.clear();
                        mEditText_DescriptionIDList.clear();
                        mEditText_LocationIDList.clear();
                        mEditText_GSTINIDList.clear();
                        mEditText_FromIDList.clear();
                        mEditText_ToIDList.clear();
                        mCardView_MainCardIDList.clear();
                        mImageView_CancelIDList.clear();

                        addDynamicAfterDeleteViewPro();

                    }
                });

                mImageView_CancelIDList.add(imgCancleBTNID);

                rlvMainDynamicViewID.addView(imgCancleBTNID);
            } else {
                mImageView_CancelIDList.add(imgCancleBTNID);
            }


            mTagIDIndex++;


            cardViewAddDynamicViewID.addView(rlvMainDynamicViewID);
            lvlMainParentLayoutID.addView(cardViewAddDynamicViewID);

        }


        // lvlDynamicMainViewID.addView(iv_sub_linearlayout12);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void checkDataValtidation() {

        try {

            for (int i = 0; i < mEditText_FromIDList.size(); i++) {

                mStartDate = trav_date.getText().toString().trim();
                mEndDate = end_date.getText().toString().trim();
                mCountry = trav_country.getText().toString().trim();
                mLocation = location.getText().toString().trim();
                mFromAddress = mEditText_FromIDList.get(i).getText().toString().trim();
                mToAddress = mEditText_ToIDList.get(i).getText().toString().trim();
                mExpenses = mEditText_ExpensesIDList.get(i).getText().toString().trim();

                mCurrency = mEditText_CurrencyIDList.get(i).getText().toString().trim();
                mLocation1 = mEditText_LocationIDList.get(i).getText().toString().trim();
                mRegion = mEditText_RegionIDList.get(i).getText().toString().trim();
                mAmount = mEditText_AmountIDList.get(i).getText().toString().trim();
                mDescription = mEditText_DescriptionIDList.get(i).getText().toString().trim();
                mGSTIN = mEditText_GSTINIDList.get(i).getText().toString().trim();

                parseDateToddMMyyyy(mStartDate);
                parseDateToddMMyyyy1(mEndDate);
                parseDateToddMMyyyy2(mFromAddress);
                parseDateToddMMyyyy3(mToAddress);


                if (mStartDate == null || mStartDate.equalsIgnoreCase("") || mStartDate.equalsIgnoreCase(null)) {

                    trav_date.setFocusable(true);
                    trav_date.requestFocus();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_start), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } else if (mEndDate == null || mEndDate.equalsIgnoreCase("") || mEndDate.equalsIgnoreCase(null)) {

                    end_date.setFocusable(true);
                    end_date.requestFocus();

                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_end), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } else if (mCountry == null || mCountry.equalsIgnoreCase("") || mCountry.equalsIgnoreCase(null)) {

                    trav_country.setFocusable(true);
                    trav_country.requestFocus();

                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_coun), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } else if (mLocation == null || mLocation.equalsIgnoreCase("") || mLocation.equalsIgnoreCase(null)) {

                    location.setFocusable(true);
                    location.requestFocus();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_loc), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } else if (mExpenses == null || mExpenses.equalsIgnoreCase("") || mExpenses.equalsIgnoreCase(null)) {
                    mEditText_ExpensesIDList.get(i).setFocusable(true);
                    mEditText_ExpensesIDList.get(i).requestFocus();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_exp), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } else if (mAmount == null || mAmount.equalsIgnoreCase("") || mAmount.equalsIgnoreCase(null)) {
                    mEditText_AmountIDList.get(i).setFocusable(true);
                    mEditText_AmountIDList.get(i).requestFocus();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_amt), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } else if (mCurrency == null || mCurrency.equalsIgnoreCase("") || mCurrency.equalsIgnoreCase(null)) {
                    mEditText_CurrencyIDList.get(i).setFocusable(true);
                    mEditText_CurrencyIDList.get(i).requestFocus();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_curr), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } else if (mFromAddress == null || mFromAddress.equalsIgnoreCase("") || mFromAddress.equalsIgnoreCase(null)) {

                    mEditText_FromIDList.get(i).setFocusable(true);
                    mEditText_FromIDList.get(i).requestFocus();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_from_date), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;

                } else if (mToAddress == null || mToAddress.equalsIgnoreCase("") || mToAddress.equalsIgnoreCase(null)) {

                    mEditText_ToIDList.get(i).setFocusable(true);
                    mEditText_ToIDList.get(i).requestFocus();
                    // mEditText_ToIDList.get(i).setError();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_to_date), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } else if (mRegion == null || mRegion.equalsIgnoreCase("") || mRegion.equalsIgnoreCase(null)) {
                    mEditText_RegionIDList.get(i).setFocusable(true);
                    mEditText_RegionIDList.get(i).requestFocus();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_reg), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } else if (mDescription == null || mDescription.equalsIgnoreCase("") || mDescription.equalsIgnoreCase(null)) {
                    mEditText_DescriptionIDList.get(i).setFocusable(true);
                    mEditText_DescriptionIDList.get(i).requestFocus();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_des), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } else if (mLocation1 == null || mLocation1.equalsIgnoreCase("") || mLocation1.equalsIgnoreCase(null)) {
                    mEditText_LocationIDList.get(i).setFocusable(true);
                    mEditText_LocationIDList.get(i).requestFocus();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_select_loc), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                } /*else if (mGSTIN == null || mGSTIN.equalsIgnoreCase("") || mGSTIN.equalsIgnoreCase(null)) {
                    mEditText_GSTINIDList.get(i).setFocusable(true);
                    mEditText_GSTINIDList.get(i).requestFocus();
                    Toast.makeText(mContext, getResources().getString(R.string.Please_enter_gstno), Toast.LENGTH_SHORT).show();
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    break;
                }*/ else {
                    if (mEditText_FromIDList.size() - 1 == i) {

                        changeButtonVisibility(false, 0.5f, txtAddTripBTNID);
                        // changeButtonVisibility(true,1.0f, txtAddTripBTNID);
                        callAddTripAPI();
                        break;
                    }
                }

            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }


    }

    private void changeButtonVisibility(boolean state, float alphaRate, TextView txtSubmiteOrderID) {
        txtSubmiteOrderID.setEnabled(state);
        txtSubmiteOrderID.setAlpha(alphaRate);
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            mStartDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String parseDateToddMMyyyy1(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            mEndDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String parseDateToddMMyyyy2(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            mFromAddress = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String parseDateToddMMyyyy3(String time) {
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            mToAddress = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void callAddTripAPI() {
        try {

            JSONObject jsonObject1 = new JSONObject();
            JSONObject jsonObject = null;
            mJsonArray = new JSONArray();
            try {
                ////Put input parameter here
                for (int i = 0; i < mEditText_FromIDList.size(); i++) {
                    jsonObject = new JSONObject();

                    Log.e("ID", "1234" + mUserID);

                    mCountry = trav_country.getText().toString().trim();
                    String[] splitStr0 = mCountry.split("\\s+");
                    mCountry = splitStr0[0];

                    mLocation = location.getText().toString().trim();

                    mExpenses = mEditText_ExpensesIDList.get(i).getText().toString().trim();
                    String[] splitStr1 = mExpenses.split("\\s+");
                    mExpenses = splitStr1[0];

                    mCurrency = mEditText_CurrencyIDList.get(i).getText().toString().trim();
                    String[] splitStr2 = mCurrency.split("\\s+");
                    mCurrency = splitStr2[0];

                    mTaxcode = "";

                    mLocation1 = mEditText_LocationIDList.get(i).getText().toString().trim();
                    mRegion = "";
                    mAmount = mEditText_AmountIDList.get(i).getText().toString().trim();
                    mDescription = mEditText_DescriptionIDList.get(i).getText().toString().trim();
                    mGSTIN = mEditText_GSTINIDList.get(i).getText().toString().trim();
                    mType = "E";


                    jsonObject.put("pernr", mUserID);
                    jsonObject.put("begda", mStartDate);
                    jsonObject.put("endda", mEndDate);
                    jsonObject.put("location", mLocation);
                    jsonObject.put("land1", mCountry);
                    jsonObject.put("exp_type", mExpenses);
                    jsonObject.put("rec_amount", mAmount);
                    jsonObject.put("loc_curr", mCurrency);
                    jsonObject.put("tax_code", mTaxcode);
                    jsonObject.put("from_date", mFromAddress);
                    jsonObject.put("to_date", mToAddress);
                    jsonObject.put("region", mRegion);
                    jsonObject.put("descript", mDescription);
                    jsonObject.put("gstin", mGSTIN);
                    jsonObject.put("type", mType);
                    jsonObject.put("location1", mLocation1);

                    mJsonArray.put(jsonObject);
                }


                progressDialog = ProgressDialog.show(mContext, "", "Connecting to server..please wait !");

                new Thread() {

                    public void run() {

                        final ArrayList<NameValuePair> param1 = new ArrayList<NameValuePair>();
                        param1.add(new BasicNameValuePair("DATA", String.valueOf(mJsonArray)));
                        try {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                            StrictMode.setThreadPolicy(policy);

                            String obj2 = CustomHttpClient.executeHttpPost1(SapUrl.CREATE_TRAVEL_EXP, param1);


                            if (obj2 != "") {

                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                JSONObject jo_success = new JSONObject(obj2);
                                Log.e("DATA", "&&&&" + jo_success.toString());
                                JSONArray ja_success = jo_success.getJSONArray("travel_create");

                                for (int i = 0; i < ja_success.length(); i++) {


                                    JSONObject jo = ja_success.getJSONObject(i);

                                    sync_msg = jo.getString("msgtyp");
                                    if (sync_msg.equalsIgnoreCase("S")) {
                                        if (progressDialog != null)
                                            progressDialog.dismiss();
                                        sync_value = jo.getString("text");
                                        Message msg = new Message();
                                        msg.obj = sync_value;
                                        mHand.sendMessage(msg);
                                        dataHelper.deleteExpTravelData();
                                        Objects.requireNonNull(getActivity()).finish();
                                    } else {
                                        if (progressDialog != null)
                                            progressDialog.dismiss();
                                        sync_value = jo.getString("text");
                                        Message msg = new Message();
                                        msg.obj = sync_value;
                                        mHand.sendMessage(msg);
                                    }
                                }

                            }


                        } catch (Exception e) {
                            if (progressDialog != null)
                                progressDialog.dismiss();

                            e.printStackTrace();
                        }

                    }

                }.start();

            } catch (Exception e) {
                if (progressDialog != null)
                    progressDialog.dismiss();

                e.printStackTrace();
            }

        } catch (Exception ee) {
            if (progressDialog != null)
                progressDialog.dismiss();

            ee.printStackTrace();
        }
    }

    /**
     * mContext interface must be implemented by activities that contain mContext
     * fragment to allow an interaction in mContext fragment to be communicated
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

    public class Country2 extends AsyncTask<Void, Void, String> {
        String data = null;
        private ProgressDialog mProgressDialog;
        private Context mContext;

        public Country2(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = ProgressDialog.show(mContext, "", "Getting Country Data ...");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dlg) {
                    Country2.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                CountryName = new ArrayList<>();

                for (int i = 0; i < dataHelper.getCountry().size(); i++) {

                    String country = dataHelper.getCountry().get(i).getLandx();
                    String country1 = dataHelper.getCountry().get(i).getLand1();

                    CountryName.add(country1 + " " + country);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.obj = "";
            mHandl.sendMessage(msg);

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

    public class Country1 extends AsyncTask<Void, Void, String> {
        String data = null;
        private ProgressDialog mProgressDialog;
        private Context mContext;

        public Country1(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = ProgressDialog.show(mContext, "", "Getting Country Data ...");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dlg) {
                    Country1.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                CountryName = new ArrayList<>();

                for (int i = 0; i < dataHelper.getCountry().size(); i++) {

                    String country = dataHelper.getCountry().get(i).getLandx();
                    String country1 = dataHelper.getCountry().get(i).getLand1();

                    CountryName.add(country1 + " " + country);

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

    public class Expenses extends AsyncTask<Void, Void, String> {
        String data = null;
        private ProgressDialog mProgressDialog;
        private Context mContext;
        private int ID;

        public Expenses(Context context, int id) {
            this.mContext = context;
            this.ID = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = ProgressDialog.show(mContext, "", "Getting Expenses Data ...");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dlg) {
                    Expenses.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                Expenses = new ArrayList<>();

                for (int i = 0; i < dataHelper.getExpenses().size(); i++) {

                    String expenses = dataHelper.getExpenses().get(i).getSptxt();
                    String expenses1 = dataHelper.getExpenses().get(i).getSpkzl();
                    Expenses.add(expenses1 + " " + expenses);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.obj = "";
            mHandlerexp.sendMessage(msg);

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

    public class Currency extends AsyncTask<Void, Void, String> {
        String data = null;
        private ProgressDialog mProgressDialog;
        private Context mContext;

        public Currency(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = ProgressDialog.show(mContext, "", "Getting Currency Data ...");
            mProgressDialog.setCancelable(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dlg) {
                    Currency.this.cancel(true);
                }
            });
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                Currency = new ArrayList<>();

                for (int i = 0; i < dataHelper.getCurrency().size(); i++) {

                    String currency = dataHelper.getCurrency().get(i).getLtext();
                    String currency1 = dataHelper.getCurrency().get(i).getWaser();
                    Currency.add(currency1 + " " + currency);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Message msg = new Message();
            msg.obj = "";
            mHandlercurr.sendMessage(msg);

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
