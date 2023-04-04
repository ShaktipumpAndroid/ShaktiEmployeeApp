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

import java.util.ArrayList;

import shakti.shakti_employee.R;
import shakti.shakti_employee.database.DatabaseHelper;
import shakti.shakti_employee.model.LoggedInUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AttendanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AttendanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AttendanceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public Context mContext;
    ArrayList<String> al;
    DatabaseHelper dataHelper;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LoggedInUser userModel;
    private OnFragmentInteractionListener mListener;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AttendanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
//        Toast.makeText(getActivity(), "Attendance report", Toast.LENGTH_SHORT).show();
//        userModel = new LoggedInUser(getActivity());
//        Log.d("uidd", ""+userModel.uid);
//        selectAttendanceData(userModel.uid);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//
//            userModel = new LoggedInUser(getActivity());
//
//
////            insertAttendanceData();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_attendance, container, false);

//        Toast.makeText(getActivity(), "Attendance report", Toast.LENGTH_SHORT).show();
        userModel = new LoggedInUser(getActivity());
        Log.d("uidd", "" + userModel.uid);
//        selectAttendanceData(userModel.uid);

        TableLayout tableLayout = (TableLayout) v.findViewById(R.id.tablelayout);
        // Add header row

        TableRow rowHeader = new TableRow(getActivity());
//        rowHeader.setBackgroundColor(Color.parseColor("#8b8b8c"));

        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText = {"Date", "In Time", "Out Time", "Working Hrs", "Status", "Leave Type"};

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


            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_ATTENDANCE
                    + " WHERE " + DatabaseHelper.KEY_PERNR + " = '" + userModel.uid + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.d("att_c", "" + cursor.getCount());

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    // Read columns data

                    String begdat = cursor.getString(cursor.getColumnIndex("begdat"));
                    String indz = cursor.getString(cursor.getColumnIndex("indz"));
                    String iodz = cursor.getString(cursor.getColumnIndex("iodz"));
                    String totdz = cursor.getString(cursor.getColumnIndex("totdz"));
                    String atn_status = cursor.getString(cursor.getColumnIndex("atn_status"));
                    String leave_typ = cursor.getString(cursor.getColumnIndex("leave_typ"));
                    Log.d("leave_typ", " " + leave_typ);
                    // dara rows
                    TableRow row = new TableRow(getActivity());
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText = {begdat + "", indz, iodz, totdz, atn_status, leave_typ};


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

    public void selectAttendanceData(String penr) {
        /*Toast.makeText(getActivity(), "Attendance report 11", Toast.LENGTH_SHORT).show();
         *//**********************************Syncing attendance while loging ********************************//*
         *//**********************************processing table data *****************************************//*

//        TableLayout tableLayout = (TableLayout) v.findViewById(R.id.tablelayout);
        // Add header row

        TableRow rowHeader = new TableRow(getActivity());
//        rowHeader.setBackgroundColor(Color.parseColor("#8b8b8c"));

        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        String[] headerText = {"Date", "In Time", "Out Time", "Working Hrs", "Status", "Leave Type"};

        for (String c : headerText) {

            TableRow tr = new TableRow(getActivity());
            TextView tv = new TextView(getActivity());
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setPadding(50, 5, 5, 5);
            tv.setTextSize(14);
            tv.setText(c);
            rowHeader.addView(tv);
        }
         tableLayout.addView(rowHeader);

        // Get data from sqlite database and add them to the table
        // Open the database for reading
        // Create DatabaseHelper instance

        DatabaseHelper dataHelper=new DatabaseHelper(getActivity());

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try
        {



            String selectQuery = "SELECT * FROM "+ DatabaseHelper.TABLE_ATTENDANCE
                    +  " WHERE " +  DatabaseHelper.KEY_PERNR +  " = '" + penr + "'"   ;
            Cursor cursor = db.rawQuery(selectQuery,null);

            Log.d("att_c", "" +cursor.getCount());

            if(cursor.getCount() >0)
            {
                while (cursor.moveToNext()) {

                    // Read columns data

                    String begdat= cursor.getString(cursor.getColumnIndex("begdat"));
                    String indz= cursor.getString(cursor.getColumnIndex("indz"));
                    String iodz= cursor.getString(cursor.getColumnIndex("iodz"));
                    String totdz= cursor.getString(cursor.getColumnIndex("totdz"));
                    String atn_status= cursor.getString(cursor.getColumnIndex("atn_status"));
                    String leave_typ= cursor.getString(cursor.getColumnIndex("leave_typ"));
                    Log.d("leave_typ"," " +  leave_typ);
                    // dara rows
                    TableRow row = new TableRow(getActivity());
                    row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT));

                    String[] colText={begdat+"",indz,iodz,totdz,atn_status,leave_typ};


                    for(String text:colText) {
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

        }
        catch (SQLiteException e)
        {
            e.printStackTrace();

        }*/
//        finally
//        {
//            db.endTransaction();
//            // End the transaction.
//            db.close();
//            // Close database
//
//        }

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


}
