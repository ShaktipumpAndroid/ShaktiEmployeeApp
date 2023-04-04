package shakti.shakti_employee.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OfficialDutyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OfficialDutyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OfficialDutyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseHelper dataHelper;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    private LoggedInUser userModel;

    public OfficialDutyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OfficialDutyFragment newInstance(String param1, String param2) {
        OfficialDutyFragment fragment = new OfficialDutyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_officialduty, container, false);


//        Toast.makeText(getActivity(), "OD report", Toast.LENGTH_SHORT).show();
        userModel = new LoggedInUser(getActivity());
        Log.d("uidd", "" + userModel.uid);
//        selectAttendanceData(userModel.uid);

        TableLayout tableLayout = (TableLayout) v.findViewById(R.id.tablelayout);
        // Add header row

        TableRow rowHeader = new TableRow(getActivity());
//        rowHeader.setBackgroundColor(Color.parseColor("#8b8b8c"));

        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText = {"OD No", "App Status", "Duration", "Strt Dt", "End Dt", "Atnd Status", "Visit Place", "Purpose"};

        for (String c : headerText) {

            TableRow tr = new TableRow(getActivity());
            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.parseColor("#0082c6"));
            tv.setPadding(50, 5, 5, 5);
            tv.setTextSize(14);
            tv.setText(c);
            tableLayout.addView(tr);
            rowHeader.addView(tv);

        }
        tableLayout.addView(rowHeader);

        // Get data from sqlite database and add them to the table
        // Open the database for reading
        // Create DatabaseHelper instance

        dataHelper = new DatabaseHelper(getActivity());

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {


            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_OD
                    + " WHERE " + DatabaseHelper.R_OD_PERNR + " = '" + userModel.uid + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.d("lev_c", "" + cursor.getCount());

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    // Read columns data

                    String odno = cursor.getString(cursor.getColumnIndex("odno"));
                    String approved = cursor.getString(cursor.getColumnIndex("approved"));
                    String horo = cursor.getString(cursor.getColumnIndex("horo"));
                    String odstdate_c = cursor.getString(cursor.getColumnIndex("odstdate_c"));
                    String odedate_c = cursor.getString(cursor.getColumnIndex("odedate_c"));
                    String atn_status = cursor.getString(cursor.getColumnIndex("atn_status"));
                    String vplace = cursor.getString(cursor.getColumnIndex("vplace"));
                    String purpose1 = cursor.getString(cursor.getColumnIndex("purpose1"));

                    // dara rows
                    TableRow row = new TableRow(getActivity());
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {odno + "", approved, horo, odstdate_c, odedate_c, atn_status, vplace, purpose1};


                    for (String text : colText) {
                        TextView tv = new TextView(getActivity());
                        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tv.setGravity(Gravity.CENTER);
                        tv.setTextSize(14);
                        // tv.setPadding(5, 5, 5, 5);
                        tv.setPadding(40, 30, 10, 5);
                        tv.setText(text);
                        row.addView(tv);

                    }
                    tableLayout.addView(row);


                    //              draw seprator between rows
                    View line = new View(getActivity());
                    line.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 1));
                    line.setBackgroundColor(Color.parseColor("#8b8b8c"));
                    tableLayout.addView(line);


                }

            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database

        }

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
/*        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
