package shakti.shakti_employee.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import models.VendorListModel;
import shakti.shakti_employee.bean.AttendanceBean;
import shakti.shakti_employee.bean.BeanActiveEmployee;
import shakti.shakti_employee.bean.EmployeeGPSActivityBean;
import shakti.shakti_employee.bean.LocalConvenienceBean;
import shakti.shakti_employee.bean.LoginBean;
import shakti.shakti_employee.bean.TravelEntryDomDocBean;
import shakti.shakti_employee.bean.TravelEntryExpDocBean;
import shakti.shakti_employee.bean.TravelHeadBean;
import shakti.shakti_employee.bean.TravelTripDomDocBean;
import shakti.shakti_employee.bean.TravelTripExpDocBean;
import shakti.shakti_employee.bean.WayPoints;
import shakti.shakti_employee.model.GatePassModel;
import shakti.shakti_employee.model.LoggedInUser;
import shakti.shakti_employee.other.Country;
import shakti.shakti_employee.other.Currency;
import shakti.shakti_employee.other.CustomUtility;
import shakti.shakti_employee.other.District;
import shakti.shakti_employee.other.Expenses;
import shakti.shakti_employee.other.Gatepass;
import shakti.shakti_employee.other.OD;
import shakti.shakti_employee.other.PersonalInfo;
import shakti.shakti_employee.other.Region;
import shakti.shakti_employee.other.States;
import shakti.shakti_employee.other.TaskCreated;
import shakti.shakti_employee.other.TaskPending;
import shakti.shakti_employee.other.Taxcode;
import shakti.shakti_employee.other.Tehsil;

/**
 * Created by shakti on 10/19/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "db_employee";
    // Database Version
    public static final int DATABASE_VERSION = 6;

    public static final String KEY_PHOTO1 = "photo1";
    public static final String KEY_PHOTO2 = "photo2";
    public static final String TABLE_LOGIN = "tbl_login";
    public static final String TABLE_ACTIVE_EMPLOYEE = "tbl_active_employee";
    public static final String TABLE_LEAVE_BALANCE = "tbl_leave_balance";
    public static final String TABLE_PENDING_LEAVE = "tbl_pending_leave";
    public static final String TABLE_PENDING_GATEPASS = "tbl_pending_gatepass";
    public static final String TABLE_PENDING_OD = "tbl_pending_od";
    public static final String TABLE_ATTENDANCE = "tbl_attendance";
    public static final String TABLE_LEAVE = "tbl_leave";
    public static final String TABLE_OD = "tbl_od";
    public static final String TABLE_EMPLOYEE_INFO = "tbl_emp_info";
    public static final String TABLE_MARK_ATTENDANCE = "tbl_mark_attendance";
    public static final String TABLE_TRAVEL_PARAMETERS = "tbl_travel_parameters";
    public static final String TABLE_TRAVEL_DOM_EXPENSES = "tbl_travel_dom_exp";
    public static final String TABLE_TRAVEL_DOM_EXPENSES1 = "tbl_travel_dom_exp1";
    public static final String TABLE_TRAVEL_EXP_EXPENSES = "tbl_travel_exp_exp";
    public static final String TABLE_TRAVEL_EXP_EXPENSES1 = "tbl_travel_exp_exp1";

    public static final String KEY_PERNR = "pernr";
    public static final String KEY_ENAME = "ename";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_HOD = "hod";
    public static final String KEY_MOB_ATND = "mob_atnd";
    public static final String KEY_TRAVEL = "travel";
    public static final String KEY_BTEXT = "btext";
    public static final String KEY_LEAVETYPE = "leavetype";
    public static final String KEY_LEV_NO = "leav_no";
    public static final String HORO = "horo";
    public static final String ENAME = "name";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String GP_TYPE = "gp_type";
    public static final String REQ_TYPE = "req_type";
    public static final String D_IN = "direct_indirect";
    public static final String LEV_TYP = "lev_typ";
    public static final String LEV_FRM = "lev_fr";
    public static final String LEV_TO = "lev_t";
    public static final String REASON = "reason";
    public static final String CHRG_NAME1 = "nameperl";
    public static final String CHRG_NAME2 = "nameperl2";
    public static final String CHRG_NAME3 = "nameperl3";
    public static final String CHRG_NAME4 = "nameperl4";
    public static final String DIRECT_INDIRECT = "direct_indirect";
    public static final String SELECTED = "selected";
    public static final String KEY_OD_NO = "odno";
    public static final String OD_HORO = "horo";
    // Pending LeaveTable - column name
    public static final String OD_ENAME = "name";
    public static final String OD_WORK_STATUS = "work_status";
    public static final String OD_FRM = "od_fr";
    public static final String OD_TO = "od_t";
    public static final String VISIT_PLACE = "visit_place";
    public static final String PURPOSE1 = "purpose1";
    public static final String PURPOSE2 = "purpose2";
    public static final String PURPOSE3 = "purpose3";
    public static final String REMARK = "remark";
    public static final String OD_DIRECT_INDIRECT = "od_direct_indirect";
    public static final String ATT_KEY_PERNR = "pernr";
    public static final String LEV_KEY_PERNR = "pernr";
    // OD Table - column name
    public static final String R_OD_PERNR = "pernr";
    public static final String R_OD_NO = "odno";
    public static final String R_OD_APPROVED = "approved";
    public static final String R_OD_HORO = "horo";
    public static final String R_OD_WORK_STATUS = "atn_status";
    public static final String R_OD_FRM = "odstdate_c";
    // Pending OD Table - column name
    public static final String R_OD_TO = "odedate_c";
    public static final String R_VISIT_PLACE = "vplace";
    public static final String R_PURPOSE = "purpose1";
    // Emp Info Table - column name
    public static final String E_PERNR = "pernr";
    public static final String E_BTRTLTXT = "btrtlTxt";
    public static final String E_PERSKTXT = "perskTxt";
    public static final String E_TELNR = "telnr";
    public static final String E_EMAILSHKT = "emailShkt";
    public static final String E_HODNAME = "hodEname";
    public static final String E_ADDRESS = "address";
    public static final String E_BIRTH1 = "birth1";
    public static final String E_BANKN = "bankn";
    // Attendance Table - column name
    public static final String E_BANKTXT = "bankTxt";
    public static final String TYPE = "TYPE";
    public static final String PERNR = "PERNR";
    public static final String SERIALNO = "serialno";
    public static final String REINR = "reinr";
    public static final String BEGDA = "BEGDA";
    public static final String SERVER_DATE_IN = "SERVER_DATE_IN";
    public static final String SERVER_TIME_IN = "SERVER_TIME_IN";
    public static final String SERVER_DATE_OUT = "SERVER_DATE_OUT";
    // Leave Table - column name
    public static final String SERVER_TIME_OUT = "SERVER_TIME_OUT";
    public static final String IN_ADDRESS = "IN_ADDRESS";
    public static final String OUT_ADDRESS = "OUT_ADDRESS";
    public static final String IN_TIME = "IN_TIME";
    public static final String OUT_TIME = "OUT_TIME";
    public static final String WORKING_HOURS = "WORKING_HOURS";
    public static final String IMAGE_DATA = "IMAGE_DATA";
    public static final String CURRENT_MILLIS = "CURRENT_MILLIS";
    public static final String IN_LAT_LONG = "IN_LAT_LONG";
    public static final String OUT_LAT_LONG = "OUT_LAT_LONG";
    public static final String IN_FILE_NAME = "IN_FILE_NAME";
    public static final String IN_FILE_LENGTH = "IN_FILE_LENGTH";
    public static final String IN_FILE_VALUE = "IN_FILE_VALUE";
    public static final String OUT_FILE_NAME = "OUT_FILE_NAME";
    public static final String OUT_FILE_LENGTH = "OUT_FILE_LENGTH";
    public static final String OUT_FILE_VALUE = "OUT_FILE_VALUE";
    public static final String IN_STATUS = "IN_STATUS";
    public static final String OUT_STATUS = "OUT_STATUS";
    public static final String IN_IMAGE = "IN_IMAGE";
    public static final String OUT_IMAGE = "OUT_IMAGE";
    // GPS table fields
    public static final String KEY_BUDAT = "budat";
    public static final String KEY_TIME_IN = "time_in";
    public static final String KEY_PHONE_NUMBER = "phone_number";

    //Travel Head Field

    public static final String KEY_LINK = "link";
    public static final String KEY_STATUS = "antrg_txt";
    public static final String KEY_START_DATE = "datv1_char";
    public static final String KEY_END_DATE = "datb1_char";
    public static final String KEY_TRIP_TOTAL = "trip_total";
    public static final String KEY_PERNR1 = "pernr";
    public static final String KEY_REINR = "reinr";
    public static final String KEY_CITY = "zort1";
    public static final String KEY_COUNTRY_TEXT = "zland_txt";
    //country field
    public static final String KEY_LAND1 = "land1";
    public static final String KEY_LANDX = "landx";
    //region field
    public static final String KEY_BLAND = "bland";
    public static final String KEY_REGIO = "regio";
    public static final String KEY_BEZEI = "bezei";
    // attendance table field
    //district field
    public static final String KEY_CITYC = "cityc";
    //tehsil field
    public static final String KEY_DISTRICT = "district";
    public static final String KEY_TEHSIL = "tehsil";
    public static final String KEY_TEHSIL_TEXT = "tehsil_text";
    //taxcode field
    public static final String KEY_MANDT = "mandt";

    public static final String KEY_VENDOR_CODE = "vendorCode";

    public static final String KEY_VENDOR_NAME = "vendorName";

    public static final String KEY_VENDOR_ADDRESS = "vendorAddress";

    public static final String KEY_VENDOR_CONTACT_NO = "vendorContact";
    public static final String KEY_VENDOR_STREET_ADDRESS = "streetAddress";
    public static final String KEY_VENDOR_REGION  = "vendorRegion";
    public static final String KEY_VENDOR_CITY = "vendorCITY";


    public static final String KEY_GATEPASS_NO = "gatepassno";
    public static final String KEY_GATEPASS_VISITORNAME = "visitorName";


    public static final String KEY_TAXCODE = "tax_code";
    public static final String KEY_TEXT = "text";
    //exp field
    public static final String KEY_SPKZL = "spkzl";
    public static final String KEY_SPTXT = "sptxt";
    //curr field
    public static final String KEY_WAERS = "waers";
    public static final String KEY_LTEXT = "ltext";
    // Task create fields
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_TASK_FOR = "task_for";
    // Expenses fields
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String TABLE_TRAVEL_HEAD = "tbl_trav_head";
    private static final String COUNTRY_NAME1 = "country1";
    private static final String COUNTRY_NAME = "country";
    private static final String LOCATION = "location";
    private static final String LOCATION1 = "location1";
    private static final String EXPENSES_TYPE = "exp_type";
    private static final String AMOUNT = "amount";
    private static final String CURRENCY = "currency";
    private static final String TAX_CODE = "tax_code";
    private static final String FROM_DATE = "from_date";
    private static final String TO_DATE = "to_date";
    private static final String REGION = "region";
    private static final String DESCRIPTION = "description";
    private static final String GSTIN_NO = "gstin_no";
    private static final String TRAV_TYPE = "trav_type";
    private static final String CARDINFO = "card_info";
    private static final String CURR_TEXT = "rec_curr_txt";
    private static final String ZLAND_TEXT = "land1_txt";
    private static final String EXP_TEXT = "exp_type_txt";
    private static final String TABLE_EMPLOYEE_GPS_ACTIVITY = "tbl_employee_activity";
    private static final String TABLE_TASK_CREATE = "tbl_task_create";
    private static final String TABLE_TASK_PENDING = "tbl_task_pending";
    public static final String  TABLE_LOCAL_CONVENIENCE = "tbl_local_convenience";
    private static final String TABLE_COUNTRY = "tbl_country";
    private static final String TABLE_REGION = "tbl_region";
    private static final String TABLE_DISTRICT = "tbl_district";
    private static final String TABLE_TEHSIL = "tbl_tehsil";
    private static final String TABLE_TAXCODE = "tbl_taxcode";
    private static final String TABLE_VENDORCODE = "tbl_vendorcode";

    private static final String TABLE_OPEN_GATE_PASS = "tbl_opengatepass";
    private static final String TABLE_EXPTYPE = "tbl_exptype";
    private static final String TABLE_CURRENCY = "tbl_curr";

    private static final String TABLE_WayPoints = "wayPoints";
    // Common column names
    private static final String KEY_ID = "id";

    public static final String KEY_WayPoints = "wayPoints";

    //Domestic Expenses table
    public static final String CREATE_TABLE_DOM_EXPENSES = "CREATE TABLE IF NOT EXISTS  "
            + TABLE_TRAVEL_DOM_EXPENSES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT,"
            + PERNR + " TEXT,"
            + SERIALNO + " TEXT,"
            + START_DATE + " TEXT,"
            + END_DATE + " TEXT,"
            + COUNTRY_NAME + " TEXT,"
            + LOCATION + " TEXT,"
            + EXPENSES_TYPE + " TEXT,"
            + AMOUNT + " TEXT,"
            + CURRENCY + " TEXT,"
            + TAX_CODE + " TEXT,"
            + FROM_DATE + " TEXT,"
            + TO_DATE + " TEXT,"
            + REGION + " TEXT,"
            + DESCRIPTION + " TEXT,"
            + LOCATION1 + " TEXT,"
            + TRAV_TYPE + " TEXT,"
            + GSTIN_NO + " TEXT)";
    public static final String CREATE_TABLE_DOM_EXPENSES1 = "CREATE TABLE IF NOT EXISTS  "
            + TABLE_TRAVEL_DOM_EXPENSES1 + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT,"
            + PERNR + " TEXT,"
            + SERIALNO + " TEXT,"
            + START_DATE + " TEXT,"
            + END_DATE + " TEXT,"
            + COUNTRY_NAME + " TEXT,"
            + LOCATION + " TEXT,"
            + EXPENSES_TYPE + " TEXT,"
            + AMOUNT + " TEXT,"
            + CURRENCY + " TEXT,"
            + TAX_CODE + " TEXT,"
            + FROM_DATE + " TEXT,"
            + TO_DATE + " TEXT,"
            + REGION + " TEXT,"
            + DESCRIPTION + " TEXT,"
            + LOCATION1 + " TEXT,"
            + TRAV_TYPE + " TEXT,"
            + REINR + " TEXT,"
            + CURR_TEXT + " TEXT,"
            + ZLAND_TEXT + " TEXT,"
            + EXP_TEXT + " TEXT,"
            + GSTIN_NO + " TEXT)";
    //Exp Expenses table
    public static final String CREATE_TABLE_EXP_EXPENSES = "CREATE TABLE IF NOT EXISTS  "
            + TABLE_TRAVEL_EXP_EXPENSES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT,"
            + PERNR + " TEXT,"
            + SERIALNO + " TEXT,"
            + START_DATE + " TEXT,"
            + END_DATE + " TEXT,"
            + COUNTRY_NAME + " TEXT,"
            + LOCATION + " TEXT,"
            + EXPENSES_TYPE + " TEXT,"
            + AMOUNT + " TEXT,"
            + CURRENCY + " TEXT,"
            + TAX_CODE + " TEXT,"
            + FROM_DATE + " TEXT,"
            + TO_DATE + " TEXT,"
            + COUNTRY_NAME1 + " TEXT,"
            + DESCRIPTION + " TEXT,"
            + LOCATION1 + " TEXT,"
            + TRAV_TYPE + " TEXT,"
            + CARDINFO + " TEXT)";
    public static final String CREATE_TABLE_EXP_EXPENSES1 = "CREATE TABLE IF NOT EXISTS  "
            + TABLE_TRAVEL_EXP_EXPENSES1 + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT,"
            + PERNR + " TEXT,"
            + SERIALNO + " TEXT,"
            + START_DATE + " TEXT,"
            + END_DATE + " TEXT,"
            + COUNTRY_NAME + " TEXT,"
            + LOCATION + " TEXT,"
            + EXPENSES_TYPE + " TEXT,"
            + AMOUNT + " TEXT,"
            + CURRENCY + " TEXT,"
            + TAX_CODE + " TEXT,"
            + FROM_DATE + " TEXT,"
            + TO_DATE + " TEXT,"
            + COUNTRY_NAME1 + " TEXT,"
            + DESCRIPTION + " TEXT,"
            + LOCATION1 + " TEXT,"
            + TRAV_TYPE + " TEXT,"
            + REINR + " TEXT,"
            + CURR_TEXT + " TEXT,"
            + ZLAND_TEXT + " TEXT,"
            + EXP_TEXT + " TEXT,"
            + CARDINFO + " TEXT)";
    public static final String CREATE_TABLE_MARK_ATTENDANCE = "CREATE TABLE IF NOT EXISTS  "
            + TABLE_MARK_ATTENDANCE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPE + " TEXT,"
            + PERNR + " TEXT,"
            + BEGDA + " TEXT,"
            + SERVER_DATE_IN + " TEXT,"
            + SERVER_TIME_IN + " TEXT,"
            + SERVER_DATE_OUT + " TEXT,"
            + SERVER_TIME_OUT + " TEXT,"
            + IN_ADDRESS + " TEXT,"
            + OUT_ADDRESS + " TEXT,"
            + IN_TIME + " TEXT,"
            + OUT_TIME + " TEXT,"
            + WORKING_HOURS + " TEXT,"
            + IMAGE_DATA + " TEXT,"
            + CURRENT_MILLIS + " TEXT,"
            + IN_LAT_LONG + " TEXT,"
            + OUT_LAT_LONG + " TEXT,"
            + IN_FILE_NAME + " TEXT,"
            + IN_FILE_LENGTH + " TEXT,"
            + IN_FILE_VALUE + " TEXT,"
            + OUT_FILE_NAME + " TEXT,"
            + IN_STATUS + " TEXT,"
            + OUT_STATUS + " TEXT,"
            + IN_IMAGE + " TEXT,"
            + OUT_IMAGE + " TEXT,"
            + OUT_FILE_LENGTH + " TEXT,"
            + OUT_FILE_VALUE + " TEXT)";




    private static final String KEY_CREATED_AT = "created_at";
    private static final String ATT_KEY_BEGDAT = "begdat";
    private static final String ATT_KEY_INDZ = "indz";
    private static final String ATT_KEY_IODZ = "iodz";
    private static final String ATT_KEY_TOTDZ = "totdz";
    private static final String ATT_KEY_ATN_STATUS = "atn_status";
    private static final String ATT_KEY_LEAVE_TYP = "leave_typ";
    private static final String LEV_KEY_LEV_NO = "leav_no";
    private static final String LEV_KEY_HORO = "horo";
    private static final String LEV_KEY_LEV_FRM = "lev_frm";
    private static final String LEV_KEY_LEV_TO = "lev_to";
    private static final String LEV_KEY_LEV_TYP = "lev_typ";
    private static final String LEV_KEY_APPHOD = "apphod";
    private static final String LEV_KEY_DELE = "dele";
    private static final String LEV_KEY_REASON = "reason";
    private static final String KEY_EVENT = "event";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_TASK_DATE_FROM = "date_from";
    private static final String KEY_TASK_DATE_TO = "date_to";
    private static final String KEY_SYNC = "sync";
    private static final String KEY_MRC_TYPE = "mrc_type";
    private static final String KEY_DEPARTMENT = "department";
    private static final String KEY_CHECKER = "checker_id";
    private static final String KEY_DNO = "dno";
    private static final String KEY_SRNO = "srno";
    private static final String KEY_REMARK = "remark";

    //Local convenience

    public static final String KEY_BEGDA = "begda";
    public static final String KEY_ENDDA = "endda";
    public static final String KEY_FROM_TIME = "start_time";
    public static final String KEY_TO_TIME = "end_time";
    private static final String KEY_FROM_LAT = "start_lat";
    private static final String KEY_TO_LAT = "end_lat";
    private static final String KEY_FROM_LNG = "start_long";
    private static final String KEY_TO_LNG = "end_long";
    private static final String KEY_START_LOC = "start_location";
    private static final String KEY_END_LOC= "end_location";
    public static final String KEY_DISTANCE = "distance";

    //  Login Details
    private static final String CREATE_TABLE_LOGIN = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOGIN + "(" + KEY_PERNR + " PRIMARY KEY ,"
            + KEY_ENAME + " TEXT," + KEY_MOB_ATND + " TEXT," + KEY_TRAVEL + " TEXT," + KEY_HOD + " TEXT," + KEY_PASSWORD + " TEXT)";
    // Active Employee search help  table create statement
    private static final String CREATE_TABLE_ACTIVE_EMPLOYEE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ACTIVE_EMPLOYEE + "(" + KEY_PERNR + " PRIMARY KEY ," + KEY_ENAME + " TEXT," + KEY_BTEXT + " TEXT)";
    // leave balance search help
    private static final String CREATE_TABLE_LEAVE_BALANCE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LEAVE_BALANCE + "(" + KEY_LEAVETYPE + " PRIMARY KEY )";
    // Pending leave table
    private static final String CREATE_TABLE_PENDING_LEAVE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PENDING_LEAVE + "(" + KEY_LEV_NO + " PRIMARY KEY ,"
            + HORO + " TEXT,"
            + ENAME + " TEXT,"
            + LEV_TYP + " TEXT,"
            + LEV_FRM + " TEXT,"
            + LEV_TO + " TEXT,"
            + REASON + " TEXT,"
            + CHRG_NAME1 + " TEXT,"
            + CHRG_NAME2 + " TEXT,"
            + CHRG_NAME3 + " TEXT,"
            + CHRG_NAME4 + " TEXT,"
            + DIRECT_INDIRECT + " TEXT)";
    // Pending leave table
    private static final String CREATE_TABLE_PENDING_GATEPASS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PENDING_GATEPASS + "(" + KEY_LEV_NO + " PRIMARY KEY ,"
            + PERNR + " TEXT,"
            + ENAME + " TEXT,"
            + DATE + " TEXT,"
            + TIME + " TEXT,"
            + GP_TYPE + " TEXT,"
            + REQ_TYPE + " TEXT,"
            + D_IN + " TEXT)";
    // Table Create Statements
    // Pending OD table
    private static final String CREATE_TABLE_PENDING_OD = "CREATE TABLE IF NOT EXISTS "
            + TABLE_PENDING_OD + "(" + KEY_OD_NO + " PRIMARY KEY ,"
            + OD_HORO + " TEXT,"
            + OD_ENAME + " TEXT,"
            + OD_WORK_STATUS + " TEXT,"
            + OD_FRM + " TEXT,"
            + OD_TO + " TEXT,"
            + VISIT_PLACE + " TEXT,"
            + PURPOSE1 + " TEXT,"
            + PURPOSE2 + " TEXT,"
            + PURPOSE3 + " TEXT,"
            + REMARK + " TEXT,"
            + OD_DIRECT_INDIRECT + " TEXT)";
    // Attendance table
    private static final String CREATE_TABLE_ATTENDANCE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ATTENDANCE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ATT_KEY_PERNR + " TEXT,"
            + ATT_KEY_BEGDAT + " TEXT,"
            + ATT_KEY_INDZ + " TEXT,"
            + ATT_KEY_IODZ + " TEXT,"
            + ATT_KEY_TOTDZ + " TEXT,"
            + ATT_KEY_ATN_STATUS + " TEXT,"
            + ATT_KEY_LEAVE_TYP + " TEXT)";
    private static final String CREATE_TABLE_LEAVE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LEAVE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LEV_KEY_PERNR + " TEXT,"
            + LEV_KEY_LEV_NO + " TEXT,"
            + LEV_KEY_HORO + " TEXT,"
            + LEV_KEY_LEV_FRM + " TEXT,"
            + LEV_KEY_LEV_TO + " TEXT,"
            + LEV_KEY_LEV_TYP + " TEXT,"
            + LEV_KEY_APPHOD + " TEXT,"
            + LEV_KEY_DELE + " TEXT,"
            + LEV_KEY_REASON + " TEXT)";


    private static final String CREATE_TABLE_OD = "CREATE TABLE IF NOT EXISTS "
            + TABLE_OD + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + R_OD_PERNR + " TEXT,"
            + R_OD_NO + " TEXT,"
            + R_OD_APPROVED + " TEXT,"
            + R_OD_HORO + " TEXT,"
            + R_OD_WORK_STATUS + " TEXT,"
            + R_OD_FRM + " TEXT,"
            + R_OD_TO + " TEXT,"
            + R_VISIT_PLACE + " TEXT,"
            + R_PURPOSE + " TEXT)";
    private static final String CREATE_TABLE_EMPLOYEE_INFO = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EMPLOYEE_INFO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + E_PERNR + " TEXT,"
            + E_BTRTLTXT + " TEXT,"
            + E_PERSKTXT + " TEXT,"
            + E_TELNR + " TEXT,"
            + E_EMAILSHKT + " TEXT,"
            + E_HODNAME + " TEXT,"
            + E_ADDRESS + " TEXT,"
            + E_BIRTH1 + " TEXT,"
            + E_BANKN + " TEXT,"
            + E_BANKTXT + " TEXT)";
    //  gps tracking
    private static final String CREATE_TABLE_EMPLOYEE_GPS_ACTIVITY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EMPLOYEE_GPS_ACTIVITY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_EVENT + " TEXT,"
            + KEY_PHONE_NUMBER + " TEXT,"

            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGITUDE + " TEXT)";
    //  task Create table
    private static final String CREATE_TABLE_TASK_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TASK_CREATE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_TASK_FOR + " TEXT,"
            + KEY_TASK_DATE_FROM + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_MRC_TYPE + " TEXT,"
            + KEY_DEPARTMENT + " TEXT,"
            + KEY_TASK_DATE_TO + " TEXT)";

    // Local convenience table
    private static final String CREATE_TABLE_LOCAL_CONVENIENCE= "CREATE TABLE IF NOT EXISTS "
            + TABLE_LOCAL_CONVENIENCE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BEGDA + " TEXT,"
            + KEY_ENDDA + " TEXT,"
            + KEY_FROM_TIME + " TEXT,"
            + KEY_TO_TIME + " TEXT,"
            + KEY_FROM_LAT + " TEXT,"
            + KEY_FROM_LNG + " TEXT,"
            + KEY_TO_LAT + " TEXT,"
            + KEY_TO_LNG + " TEXT,"
            + KEY_START_LOC + " TEXT,"
            + KEY_END_LOC + " TEXT,"
            + KEY_DISTANCE+ " TEXT,"
            + KEY_PHOTO1 + " BLOB,"
            + KEY_PHOTO2 + " BLOB,"
            + KEY_TASK_DATE_TO + " TEXT)";
    //  task pending table
    private static final String CREATE_TABLE_TASK_PENDING = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TASK_PENDING + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PERNR + " TEXT,"
            + KEY_BUDAT + " TEXT,"
            + KEY_TIME_IN + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_TASK_FOR + " TEXT,"
            + KEY_TASK_DATE_FROM + " TEXT,"
            + KEY_SYNC + " TEXT,"
            + KEY_MRC_TYPE + " TEXT,"
            + KEY_DEPARTMENT + " TEXT,"
            + KEY_CHECKER + " TEXT,"
            + KEY_SRNO + " TEXT,"
            + KEY_DNO + " TEXT,"
            + KEY_REMARK + " TEXT,"
            + KEY_TASK_DATE_TO + " TEXT)";
    // OD table
    //  country table
    private static final String CREATE_COUNTRY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_COUNTRY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_LANDX + " TEXT)";
    // Employee Info table
    //  region table
    private static final String CREATE_REGION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_REGION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_BLAND + " TEXT,"
            + KEY_REGIO + " TEXT,"
            + KEY_BEZEI + " TEXT)";
    // attendance table
    //  district table
    private static final String CREATE_DISTRICT = "CREATE TABLE IF NOT EXISTS "
            + TABLE_DISTRICT + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_REGIO + " TEXT,"
            + KEY_CITYC + " TEXT,"
            + KEY_BEZEI + " TEXT)";
    //  tehsil table
    private static final String CREATE_TEHSIL = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TEHSIL + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LAND1 + " TEXT,"
            + KEY_REGIO + " TEXT,"
            + KEY_DISTRICT + " TEXT,"
            + KEY_TEHSIL + " TEXT,"
            + KEY_TEHSIL_TEXT + " TEXT)";
    //  Travel table
    private static final String CREATE_TABLE_TRAVEL_HEAD = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRAVEL_HEAD + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_LINK + " TEXT,"
            + KEY_STATUS + " TEXT,"
            + KEY_START_DATE + " TEXT,"
            + KEY_END_DATE + " TEXT,"
            + KEY_TRIP_TOTAL + " TEXT,"
            + KEY_PERNR1 + " TEXT,"
            + KEY_REINR + " TEXT,"
            + KEY_CITY + " TEXT,"
            + KEY_COUNTRY_TEXT + " TEXT)";
    //  country table
    private static final String CREATE_TAXCODE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TAXCODE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MANDT + " TEXT,"
            + KEY_TAXCODE + " TEXT,"
            + KEY_TEXT + " TEXT)";


    private static final String CREATETABLE_VENDORCODE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_VENDORCODE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_VENDOR_CODE + " TEXT,"
            + KEY_VENDOR_NAME + " TEXT,"
            + KEY_VENDOR_ADDRESS + " TEXT,"
            + KEY_VENDOR_CONTACT_NO + " TEXT,"
            + KEY_VENDOR_STREET_ADDRESS + " TEXT,"
            + KEY_VENDOR_REGION + " TEXT,"
            + KEY_VENDOR_CITY + " TEXT)";


    private static final String CREATETABLE_OPENGATEPASS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_OPEN_GATE_PASS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_GATEPASS_NO + " TEXT,"
            + KEY_GATEPASS_VISITORNAME + " TEXT)";
    //  region table
    private static final String CREATE_EXPTYPE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EXPTYPE + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_SPKZL + " TEXT,"
            + KEY_SPTXT + " TEXT)";
    //  district table
    private static final String CREATE_CURRENCY = "CREATE TABLE IF NOT EXISTS "
            + TABLE_CURRENCY + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_WAERS + " TEXT,"
            + KEY_LTEXT + " TEXT)";
    private static final String CREATE_TABLE_TRAVEL_PARAMETERS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_TRAVEL_PARAMETERS + "(" + KEY_PERNR + " PRIMARY KEY ,"
            + CHRG_NAME4 + " TEXT,"
            + DIRECT_INDIRECT + " TEXT)";

    private static final String CREATE_TABLE_WayPoints = "CREATE TABLE IF NOT EXISTS "
            + TABLE_WayPoints + "(" + KEY_ID + " PRIMARY KEY ,"
            + KEY_PERNR + " TEXT,"
            + KEY_BEGDA + " TEXT,"
            + KEY_ENDDA + " TEXT,"
            + KEY_FROM_TIME + " TEXT,"
            + KEY_TO_TIME + " TEXT,"
            + KEY_WayPoints + " TEXT)";

    /*CREATE_TABLE_WayPoints*/
    public Cursor cursordom = null, cursordom1 = null, cursordom2 = null, cursordom3 = null;
    public Cursor cursorexp = null, cursorexp1 = null, cursorexp2 = null, cursorexp3 = null;
    String obj_active_employee;
    String obj_leave_balance;
    ArrayList<String> al;
    // Active employee Table - column name
    String pernr, ename, btext;
    String activeemp;
    // Travel Parameters
    // Leave Balance Table - column name
    String leavetype;
    private LoggedInUser userModel;
    private Context mContext;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_ACTIVE_EMPLOYEE);
        db.execSQL(CREATE_TABLE_LEAVE_BALANCE);
        db.execSQL(CREATE_TABLE_PENDING_LEAVE);
        db.execSQL(CREATE_TABLE_PENDING_GATEPASS);
        db.execSQL(CREATE_TABLE_PENDING_OD);
        db.execSQL(CREATE_TABLE_ATTENDANCE);
        db.execSQL(CREATE_TABLE_LEAVE);
        db.execSQL(CREATE_TABLE_OD);
        db.execSQL(CREATE_TABLE_EMPLOYEE_INFO);
        db.execSQL(CREATE_TABLE_MARK_ATTENDANCE);
        db.execSQL(CREATE_TABLE_EMPLOYEE_GPS_ACTIVITY);
        db.execSQL(CREATE_TABLE_TASK_CREATE);
        db.execSQL(CREATE_TABLE_TASK_PENDING);
        db.execSQL(CREATE_COUNTRY);
        db.execSQL(CREATE_REGION);
        db.execSQL(CREATE_DISTRICT);
        db.execSQL(CREATE_TEHSIL);
        db.execSQL(CREATE_TAXCODE);
        db.execSQL(CREATE_EXPTYPE);
        db.execSQL(CREATE_CURRENCY);
        db.execSQL(CREATE_TABLE_TRAVEL_PARAMETERS);
        db.execSQL(CREATE_TABLE_EXP_EXPENSES);
        db.execSQL(CREATE_TABLE_DOM_EXPENSES);
        db.execSQL(CREATE_TABLE_EXP_EXPENSES1);
        db.execSQL(CREATE_TABLE_DOM_EXPENSES1);
        db.execSQL(CREATE_TABLE_TRAVEL_HEAD);
        db.execSQL(CREATE_TABLE_LOCAL_CONVENIENCE);
        db.execSQL(CREATE_TABLE_WayPoints);
        db.execSQL(CREATETABLE_VENDORCODE);
        db.execSQL(CREATETABLE_OPENGATEPASS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables

        if(newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVE_EMPLOYEE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAVE_BALANCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING_LEAVE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING_GATEPASS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING_OD);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAVE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OD);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE_INFO);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARK_ATTENDANCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVEL_PARAMETERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVEL_DOM_EXPENSES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVEL_DOM_EXPENSES1);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVEL_EXP_EXPENSES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVEL_EXP_EXPENSES1);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVEL_HEAD);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE_GPS_ACTIVITY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_CREATE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_PENDING);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRICT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEHSIL);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAXCODE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENDORCODE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPTYPE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENCY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCAL_CONVENIENCE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WayPoints);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPEN_GATE_PASS);


            // create new tables
            onCreate(db);
        }
    }


    // Delete existing data from tables

    public void deleteLoginDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_LOGIN)) {
        db.delete(TABLE_LOGIN, null, null);
    }
    }


    public void deleteLocalconvenienceDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_LOCAL_CONVENIENCE)) {
            db.delete(TABLE_LOCAL_CONVENIENCE, null, null);
        }
    }

    public void deleteLocalconvenienceDetail1(String enddt,String endtm) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_LOCAL_CONVENIENCE)) {
        db.execSQL("DELETE FROM " + TABLE_LOCAL_CONVENIENCE + " WHERE " + KEY_ENDDA + "='" + enddt + "'" + " AND " + KEY_TO_TIME + " = '" + endtm + "'");
    }}
    public void deleteWayPointsDetail() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_WayPoints)) {
            db.execSQL("DELETE FROM " + TABLE_WayPoints);
        }
    }
    public void deleteWayPointsDetail1(String enddt,String endtm) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_WayPoints)) {
            db.execSQL("DELETE FROM " + TABLE_WayPoints + " WHERE " + KEY_ENDDA + "='" + enddt + "'" + " AND " + KEY_TO_TIME + " = '" + endtm + "'");
        }
    }


    public void deleteTravelHeadData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TRAVEL_HEAD)) {
            db.delete(TABLE_TRAVEL_HEAD, null, null);
        }
    }

    public void deleteDomTravelData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TRAVEL_DOM_EXPENSES)) {
            db.execSQL("delete from " + TABLE_TRAVEL_DOM_EXPENSES);
            String selectQuery = "SELECT  *  FROM " + TABLE_TRAVEL_DOM_EXPENSES;

            cursorexp3 = db.rawQuery(selectQuery, null);

            Log.e("DOMESTIC", "%%%%%" + cursorexp3.getCount());
        }

    }

    public void deleteDomTravelData1() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TRAVEL_DOM_EXPENSES1)) {
            db.execSQL("delete from " + TABLE_TRAVEL_DOM_EXPENSES1);
            db.close();
        }
    }

    public void deleteExpTravelData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TRAVEL_EXP_EXPENSES)) {
            db.execSQL("delete from " + TABLE_TRAVEL_EXP_EXPENSES);
            db.close();
        }

    }

    public void deleteExpTravelData1() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TRAVEL_EXP_EXPENSES1)) {
            db.execSQL("delete from " + TABLE_TRAVEL_EXP_EXPENSES1);
            db.close();
        }
    }


    public void deleteActiveEmployee() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_ACTIVE_EMPLOYEE)) {
            db.delete(TABLE_ACTIVE_EMPLOYEE, null, null);
            db.close();
        }
    }

    public void deleteleaveBalance() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_LEAVE_BALANCE)) {
            db.delete(TABLE_LEAVE_BALANCE, null, null);
            db.close();
        }
    }

    public void deletependingleave() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_PENDING_LEAVE)) {
            db.delete(TABLE_PENDING_LEAVE, null, null);
            db.close();
        }
    }

    public void deletependingod() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_PENDING_OD)) {
            db.delete(TABLE_PENDING_OD, null, null);
            db.close();
        }
    }

    public void deleteattendance() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_ATTENDANCE)) {
            db.delete(TABLE_ATTENDANCE, null, null);
            db.close();
        }
    }

    public void deleteleave() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_LEAVE)) {
            db.delete(TABLE_LEAVE, null, null);
            db.close();
        }
    }

    public void deletePendingLeaveWhere(String del_lev) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_PENDING_LEAVE)) {
            db.execSQL("DELETE FROM " + TABLE_PENDING_LEAVE + " WHERE " + KEY_LEV_NO + "='" + del_lev + "'");
            db.close();
        }
    }

    public void deletePendingLeave() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_PENDING_LEAVE)) {
            db.execSQL("DELETE FROM " + TABLE_PENDING_LEAVE);
            db.close();
        }

    }

    public void deletePendingGatepass() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_PENDING_GATEPASS)) {
            db.execSQL("DELETE FROM " + TABLE_PENDING_GATEPASS);
            db.close();
        }
    }


    public void deletePendingOdWhere(String del_od) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_PENDING_OD)) {
            db.execSQL("DELETE FROM " + TABLE_PENDING_OD + " WHERE " + KEY_OD_NO + "='" + del_od + "'");
            db.close();
        }
    }

    public void deletePendingOd() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_PENDING_OD)) {
            db.execSQL("DELETE FROM " + TABLE_PENDING_OD);
            db.close();
        }
    }

    public void deleteSyncedCreatedTaskWhere() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TASK_CREATE)) {
            db.execSQL("DELETE FROM " + TABLE_TASK_CREATE + " WHERE " + KEY_SYNC + "='X'");
            db.close();
        }
    }

    public void deleteSyncedCompletedTaskWhere() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TASK_PENDING)) {
            db.execSQL("DELETE FROM " + TABLE_TASK_PENDING + " WHERE " + KEY_SYNC + "='X'");
            db.close();
        }
    }


    public void deleteod() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_OD)) {
            db.delete(TABLE_OD, null, null);
            db.close();
        }
    }


    public void deleteempinfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_EMPLOYEE_INFO)) {
            db.delete(TABLE_EMPLOYEE_INFO, null, null);
            db.close();
        }
    }


    public void deletemarkattendance() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_MARK_ATTENDANCE)) {
            db.delete(TABLE_MARK_ATTENDANCE, null, null);
            db.close();
        }
    }


    public void deleteEmployeeGPSActivity() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_EMPLOYEE_GPS_ACTIVITY)) {
            db.delete(TABLE_EMPLOYEE_GPS_ACTIVITY, null, null);
            db.close();
        }
    }

    public void deletePendingTask() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TASK_PENDING)) {
            db.delete(TABLE_TASK_PENDING, null, null);
            db.close();
        }
    }

    public void deleteCountryData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_COUNTRY)) {
            db.delete(TABLE_COUNTRY, null, null);
            db.close();
        }
    }


    public void deleteRegionData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_REGION)) {
            db.delete(TABLE_REGION, null, null);
            db.close();
        }
    }


    public void deleteDistrictData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_DISTRICT)) {
            db.delete(TABLE_DISTRICT, null, null);
            db.close();
        }
    }


    public void deleteTehsilData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TEHSIL)) {
            db.delete(TABLE_TEHSIL, null, null);
            db.close();
        }
    }

    public void deleteTaxcodeData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TAXCODE)) {
            db.delete(TABLE_TAXCODE, null, null);
            db.close();
        }
    }

    public void deleteVendorcodeData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_VENDORCODE)) {
            db.delete(TABLE_VENDORCODE, null, null);
            db.close();
        }
    }

    public void deleteOpenGatePassData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_OPEN_GATE_PASS)) {
            db.delete(TABLE_OPEN_GATE_PASS, null, null);
            db.close();
        }
    }

    public void deleteExpensesData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_EXPTYPE)) {
            db.delete(TABLE_EXPTYPE, null, null);
            db.close();
        }
    }

    public void deleteCurrencyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_CURRENCY)) {
            db.delete(TABLE_CURRENCY, null, null);
            db.close();
        }
    }

    public void deleteDomTravelEntry(String value) {

        String where = "";
        SQLiteDatabase db = this.getWritableDatabase();

        where = SERIALNO + "='" + value + "'";
        if(CustomUtility.doesTableExist(db,TABLE_TRAVEL_DOM_EXPENSES)) {
            db.delete(TABLE_TRAVEL_DOM_EXPENSES, where, null);
            db.close();
        }

    }

    public void deleteDomTravelEntry1(String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";

        where = SERIALNO + "='" + value + "'";
        if(CustomUtility.doesTableExist(db,TABLE_TRAVEL_DOM_EXPENSES1)) {

            db.delete(TABLE_TRAVEL_DOM_EXPENSES1, where, null);
            db.close();
        }

    }

    public void deleteExpTravelEntry(String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";

        where = SERIALNO + "='" + value + "'";

        if(CustomUtility.doesTableExist(db,TABLE_TRAVEL_EXP_EXPENSES)) {
            db.delete(TABLE_TRAVEL_EXP_EXPENSES, where, null);
            db.close();
        }

    }

    public void deleteExpTravelEntry1(String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";

        where = SERIALNO + "='" + value + "'";

        if(CustomUtility.doesTableExist(db,TABLE_TRAVEL_EXP_EXPENSES1)) {
            db.delete(TABLE_TRAVEL_EXP_EXPENSES1, where, null);
            db.close();
        }
    }


    public void deleteTaskCreate() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TASK_CREATE)) {
            db.delete(TABLE_TASK_CREATE, null, null);
            db.close();
        }
    }

    public void deleteTaskCompleted() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(db,TABLE_TASK_PENDING)) {
            db.delete(TABLE_TASK_PENDING, null, null);
            db.close();
        }
    }

    public void deleteTaskCompletedEntry(String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        String where = "";

        where = KEY_DNO + "='" + value + "'";

        if(CustomUtility.doesTableExist(db,TABLE_TASK_PENDING)) {
            db.delete(TABLE_TASK_PENDING, where, null);
            db.close();
        }

    }


    // Insert Data to Data Base


    // Insert Active employee
    public void insertActiveEmployee(BeanActiveEmployee beanproduct) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_PERNR, beanproduct.getPernr());
        values.put(KEY_ENAME, beanproduct.getEname());
        values.put(KEY_BTEXT, beanproduct.getBtext());

        // insert row
        Log.d("data_of_values", " " + values);
        Long result = db.insert(TABLE_ACTIVE_EMPLOYEE, null, values);

        Log.d("data_of_table", " " + result);

    }

    // Insert Leave balance
    public void createLeaveBalance(String leavetype) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_LEAVETYPE, leavetype);
        // insert row
        Log.d("data_of_values", " " + values);
        try {
            db.beginTransactionNonExclusive();
            Long result = db.insert(TABLE_LEAVE_BALANCE, null, values);
            Log.d("data_of_leave_bal", " " + result);
            Log.d("res_of_leavetype1", " " + result);
        } finally {
            db.setTransactionSuccessful();
            db.endTransaction();
        }

    }


    public void insertLocalconvenienceData(LocalConvenienceBean localconvenienceBean) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_PERNR, localconvenienceBean.getPernr());
            values.put(KEY_BEGDA, localconvenienceBean.getBegda());
            values.put(KEY_ENDDA, localconvenienceBean.getEndda());
            values.put(KEY_FROM_TIME, localconvenienceBean.getFrom_time());
            values.put(KEY_TO_TIME, localconvenienceBean.getTo_time());
            values.put(KEY_FROM_LAT, localconvenienceBean.getFrom_lat());
            values.put(KEY_TO_LAT, localconvenienceBean.getTo_lat());
            values.put(KEY_FROM_LNG, localconvenienceBean.getFrom_lng());
            values.put(KEY_TO_LNG, localconvenienceBean.getTo_lng());
            values.put(KEY_START_LOC, localconvenienceBean.getStart_loc());
            values.put(KEY_END_LOC, localconvenienceBean.getEnd_loc());
            values.put(KEY_DISTANCE, localconvenienceBean.getDistance());
            values.put(KEY_PHOTO1, localconvenienceBean.getPhoto1());
            values.put(KEY_PHOTO2, localconvenienceBean.getPhoto2());

            // Insert Row
          db.insert(TABLE_LOCAL_CONVENIENCE, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }


    }

    public void updateLocalconvenienceData(LocalConvenienceBean localconvenienceBean) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, localconvenienceBean.getPernr());
            values.put(KEY_BEGDA, localconvenienceBean.getBegda());

            values.put(KEY_ENDDA, localconvenienceBean.getEndda());
            values.put(KEY_FROM_TIME, localconvenienceBean.getFrom_time());
            values.put(KEY_TO_TIME, localconvenienceBean.getTo_time());
            values.put(KEY_FROM_LAT, localconvenienceBean.getFrom_lat());
            values.put(KEY_TO_LAT, localconvenienceBean.getTo_lat());
            values.put(KEY_FROM_LNG, localconvenienceBean.getFrom_lng());
            values.put(KEY_TO_LNG, localconvenienceBean.getTo_lng());
            values.put(KEY_START_LOC, localconvenienceBean.getStart_loc());
            values.put(KEY_END_LOC, localconvenienceBean.getEnd_loc());
            values.put(KEY_DISTANCE, localconvenienceBean.getDistance());
            values.put(KEY_PHOTO1, localconvenienceBean.getPhoto1());
            values.put(KEY_PHOTO2, localconvenienceBean.getPhoto2());

            where = KEY_PERNR + "='" + localconvenienceBean.getPernr() + "'" + " AND " +
                    KEY_BEGDA + "='" + localconvenienceBean.getBegda() + "'" + " AND " +
                    KEY_FROM_TIME + "='" + localconvenienceBean.getFrom_time() + "'" + " AND " +
                    KEY_FROM_LAT + "='" + localconvenienceBean.getFrom_lat() + "'" + " AND " +
                    KEY_FROM_LNG + "='" + localconvenienceBean.getFrom_lng() + "'";

            // update Row
            long i = db.update(TABLE_LOCAL_CONVENIENCE, values, where, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }

    public void insertWayPointsData(WayPoints wayPoints) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransactionNonExclusive();
        ContentValues values;
        Log.e("wayPoint2",wayPoints.getWayPoints());
        try {
            values = new ContentValues();
            values.put(KEY_PERNR, wayPoints.getPernr());
            values.put(KEY_BEGDA, wayPoints.getBegda());
            values.put(KEY_ENDDA, wayPoints.getEndda());
            values.put(KEY_FROM_TIME, wayPoints.getFrom_time());
            values.put(KEY_TO_TIME, wayPoints.getTo_time());
            values.put(KEY_WayPoints, wayPoints.getWayPoints());
            db.insert(TABLE_WayPoints, null, values);

            db.setTransactionSuccessful();
            Log.e("wayPoint3",wayPoints.getWayPoints());
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void updateWayPointData(WayPoints wayPoints) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();

            values.put(KEY_WayPoints, wayPoints.getWayPoints());

            where = KEY_PERNR + "='" + wayPoints.getPernr() + "'" + " AND " +
                    KEY_BEGDA + "='" + wayPoints.getBegda() + "'" + " AND " +
                    KEY_FROM_TIME + "='" + wayPoints.getFrom_time() + "'";

            // update Row
            long i = db.update(TABLE_WayPoints, values, where, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }
    public void updateWayPointData1(WayPoints wayPoints) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();

            values.put(KEY_PERNR, wayPoints.getPernr());
            values.put(KEY_BEGDA, wayPoints.getBegda());
            values.put(KEY_ENDDA, wayPoints.getEndda());
            values.put(KEY_FROM_TIME, wayPoints.getFrom_time());
            values.put(KEY_TO_TIME, wayPoints.getTo_time());
            values.put(KEY_WayPoints, wayPoints.getWayPoints());

            where = KEY_PERNR + "='" + wayPoints.getPernr() + "'" + " AND " +
                    KEY_BEGDA + "='" + wayPoints.getBegda() + "'" + " AND " +
                    KEY_FROM_TIME + "='" + wayPoints.getFrom_time() + "'";

            // update Row
            db.update(TABLE_WayPoints, values, where, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }


    public void insertDOMTravelEntryDocument(TravelEntryDomDocBean travelEntryDomDocBean) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(PERNR, travelEntryDomDocBean.getPernr());
            values.put(SERIALNO, travelEntryDomDocBean.getSerialno());
            values.put(START_DATE, travelEntryDomDocBean.getStart_date());
            values.put(END_DATE, travelEntryDomDocBean.getEnd_date());
            values.put(COUNTRY_NAME, travelEntryDomDocBean.getCountry());
            values.put(LOCATION, travelEntryDomDocBean.getLocation());
            values.put(EXPENSES_TYPE, travelEntryDomDocBean.getExpenses_type());
            values.put(AMOUNT, travelEntryDomDocBean.getAmount());
            values.put(CURRENCY, travelEntryDomDocBean.getCurrency());
            values.put(TAX_CODE, travelEntryDomDocBean.getTax_code());
            values.put(FROM_DATE, travelEntryDomDocBean.getFrom_date());
            values.put(TO_DATE, travelEntryDomDocBean.getTo_date());
            values.put(REGION, travelEntryDomDocBean.getRegion());
            values.put(DESCRIPTION, travelEntryDomDocBean.getDescription());
            values.put(LOCATION1, travelEntryDomDocBean.getLocation1());
            values.put(TRAV_TYPE, travelEntryDomDocBean.getType());
            values.put(GSTIN_NO, travelEntryDomDocBean.getGstin_no());


            // Insert Row
            long i = db.insert(TABLE_TRAVEL_DOM_EXPENSES, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }

    public void insertDOMTravelTripEntryDocument1(TravelTripDomDocBean travelTripDomDocBean, String userid, String reinr) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        String where = "";

        try {
            values = new ContentValues();
            values.put(PERNR, travelTripDomDocBean.getPernr());
            values.put(SERIALNO, travelTripDomDocBean.getSerialno());
            values.put(START_DATE, travelTripDomDocBean.getStart_date());
            values.put(END_DATE, travelTripDomDocBean.getEnd_date());
            values.put(COUNTRY_NAME, travelTripDomDocBean.getCountry());
            values.put(LOCATION, travelTripDomDocBean.getLocation());
            values.put(EXPENSES_TYPE, travelTripDomDocBean.getExpenses_type());
            values.put(AMOUNT, travelTripDomDocBean.getAmount());
            values.put(CURRENCY, travelTripDomDocBean.getCurrency());
            values.put(TAX_CODE, travelTripDomDocBean.getTax_code());
            values.put(FROM_DATE, travelTripDomDocBean.getFrom_date());
            values.put(TO_DATE, travelTripDomDocBean.getTo_date());
            values.put(REGION, travelTripDomDocBean.getRegion());
            values.put(DESCRIPTION, travelTripDomDocBean.getDescription());
            values.put(LOCATION1, travelTripDomDocBean.getLocation1());
            values.put(TRAV_TYPE, travelTripDomDocBean.getType());
            values.put(REINR, travelTripDomDocBean.getReinr());
            values.put(CURR_TEXT, travelTripDomDocBean.getRec_curr_txt());
            values.put(ZLAND_TEXT, travelTripDomDocBean.getLand1_txt());
            values.put(EXP_TEXT, travelTripDomDocBean.getExp_type_txt());
            values.put(GSTIN_NO, travelTripDomDocBean.getGstin_no());

            Log.e("INSSERIALNO.", "&&&&" + travelTripDomDocBean.getSerialno());
            Log.e("INSEXPENSES.", "&&&&" + travelTripDomDocBean.getExpenses_type());
            Log.e("INSREINR.", "&&&&" + travelTripDomDocBean.getReinr());
            Log.e("INSAMOUNT.", "&&&&" + travelTripDomDocBean.getAmount());

            where = PERNR + " = ' " + userid + "'" + " AND " + REINR + " = '" + reinr + "'";
            // Insert Row
            long i = db.insert(TABLE_TRAVEL_DOM_EXPENSES1, where, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }

    public void insertEXPTravelEntryDocument(TravelEntryExpDocBean travelEntryDomDocBean) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(PERNR, travelEntryDomDocBean.getPernr());
            values.put(SERIALNO, travelEntryDomDocBean.getSerialno());
            values.put(START_DATE, travelEntryDomDocBean.getStart_date());
            values.put(END_DATE, travelEntryDomDocBean.getEnd_date());
            values.put(COUNTRY_NAME, travelEntryDomDocBean.getCountry());
            values.put(LOCATION, travelEntryDomDocBean.getLocation());
            values.put(EXPENSES_TYPE, travelEntryDomDocBean.getExpenses_type());
            values.put(AMOUNT, travelEntryDomDocBean.getAmount());
            values.put(CURRENCY, travelEntryDomDocBean.getCurrency());
            values.put(TAX_CODE, travelEntryDomDocBean.getTax_code());
            values.put(FROM_DATE, travelEntryDomDocBean.getFrom_date());
            values.put(TO_DATE, travelEntryDomDocBean.getTo_date());
            values.put(COUNTRY_NAME1, travelEntryDomDocBean.getRegion());
            values.put(DESCRIPTION, travelEntryDomDocBean.getDescription());
            values.put(LOCATION1, travelEntryDomDocBean.getLocation1());
            values.put(TRAV_TYPE, travelEntryDomDocBean.getType());
            values.put(CARDINFO, travelEntryDomDocBean.getCardinfo());


            // Insert Row
            long i = db.insert(TABLE_TRAVEL_EXP_EXPENSES, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }

    public void insertEXPTravelTripEntryDocument1(TravelTripExpDocBean travelTripExpDocBean, String userid, String reinr) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();
            values.put(PERNR, travelTripExpDocBean.getPernr());
            values.put(SERIALNO, travelTripExpDocBean.getSerialno());
            values.put(START_DATE, travelTripExpDocBean.getStart_date());
            values.put(END_DATE, travelTripExpDocBean.getEnd_date());
            values.put(COUNTRY_NAME, travelTripExpDocBean.getCountry());
            values.put(LOCATION, travelTripExpDocBean.getLocation());
            values.put(EXPENSES_TYPE, travelTripExpDocBean.getExpenses_type());
            values.put(AMOUNT, travelTripExpDocBean.getAmount());
            values.put(CURRENCY, travelTripExpDocBean.getCurrency());
            values.put(TAX_CODE, travelTripExpDocBean.getTax_code());
            values.put(FROM_DATE, travelTripExpDocBean.getFrom_date());
            values.put(TO_DATE, travelTripExpDocBean.getTo_date());
            values.put(COUNTRY_NAME1, travelTripExpDocBean.getRegion());
            values.put(DESCRIPTION, travelTripExpDocBean.getDescription());
            values.put(LOCATION1, travelTripExpDocBean.getLocation1());
            values.put(TRAV_TYPE, travelTripExpDocBean.getType());
            values.put(REINR, travelTripExpDocBean.getReinr());
            values.put(CURR_TEXT, travelTripExpDocBean.getRec_curr_txt());
            values.put(ZLAND_TEXT, travelTripExpDocBean.getLand1_txt());
            values.put(EXP_TEXT, travelTripExpDocBean.getExp_type_txt());
            values.put(CARDINFO, travelTripExpDocBean.getCardinfo());


            where = PERNR + " = ' " + userid + "'" + "AND " + REINR + " = '" + reinr + "'";

            // Insert Row
            long i = db.insert(TABLE_TRAVEL_EXP_EXPENSES1, where, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }


    public void updateDOMTravelEntryDocument(TravelEntryDomDocBean travelEntryDomDocBean, String serialno) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();
            values.put(PERNR, travelEntryDomDocBean.getPernr());
            values.put(SERIALNO, travelEntryDomDocBean.getSerialno());
            Log.e("SERIALNO.", "&&&&" + travelEntryDomDocBean.getSerialno());
            values.put(START_DATE, travelEntryDomDocBean.getStart_date());
            values.put(END_DATE, travelEntryDomDocBean.getEnd_date());
            values.put(COUNTRY_NAME, travelEntryDomDocBean.getCountry());
            values.put(LOCATION, travelEntryDomDocBean.getLocation());
            values.put(EXPENSES_TYPE, travelEntryDomDocBean.getExpenses_type());
            values.put(AMOUNT, travelEntryDomDocBean.getAmount());
            values.put(CURRENCY, travelEntryDomDocBean.getCurrency());
            values.put(TAX_CODE, travelEntryDomDocBean.getTax_code());
            values.put(FROM_DATE, travelEntryDomDocBean.getFrom_date());
            values.put(TO_DATE, travelEntryDomDocBean.getTo_date());
            values.put(REGION, travelEntryDomDocBean.getRegion());
            values.put(DESCRIPTION, travelEntryDomDocBean.getDescription());
            values.put(LOCATION1, travelEntryDomDocBean.getLocation1());
            values.put(TRAV_TYPE, travelEntryDomDocBean.getType());
            values.put(GSTIN_NO, travelEntryDomDocBean.getGstin_no());

            // update Row
            long i = db.update(TABLE_TRAVEL_DOM_EXPENSES, values, null, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }

    public void updateDOMTravelTripEntryDocument1(TravelTripDomDocBean travelTripDomDocBean, String serialno) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();
            values.put(PERNR, travelTripDomDocBean.getPernr());
            values.put(SERIALNO, travelTripDomDocBean.getSerialno());
            Log.e("SERIALNO.", "&&&&" + travelTripDomDocBean.getSerialno());
            values.put(START_DATE, travelTripDomDocBean.getStart_date());
            values.put(END_DATE, travelTripDomDocBean.getEnd_date());
            values.put(COUNTRY_NAME, travelTripDomDocBean.getCountry());
            values.put(LOCATION, travelTripDomDocBean.getLocation());
            values.put(EXPENSES_TYPE, travelTripDomDocBean.getExpenses_type());
            values.put(AMOUNT, travelTripDomDocBean.getAmount());
            values.put(CURRENCY, travelTripDomDocBean.getCurrency());
            values.put(TAX_CODE, travelTripDomDocBean.getTax_code());
            values.put(FROM_DATE, travelTripDomDocBean.getFrom_date());
            values.put(TO_DATE, travelTripDomDocBean.getTo_date());
            values.put(REGION, travelTripDomDocBean.getRegion());
            values.put(DESCRIPTION, travelTripDomDocBean.getDescription());
            values.put(LOCATION1, travelTripDomDocBean.getLocation1());
            values.put(TRAV_TYPE, travelTripDomDocBean.getType());
            values.put(REINR, travelTripDomDocBean.getReinr());
            values.put(GSTIN_NO, travelTripDomDocBean.getGstin_no());

            // update Row
            long i = db.update(TABLE_TRAVEL_DOM_EXPENSES1, values, null, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }


    public void updateEXPTravelEntryDocument(TravelEntryExpDocBean travelEntryExpDocBean, String serialno) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();
            values.put(PERNR, travelEntryExpDocBean.getPernr());
            values.put(SERIALNO, travelEntryExpDocBean.getSerialno());
            Log.e("SERIALNO.", "&&&&" + travelEntryExpDocBean.getSerialno());
            values.put(START_DATE, travelEntryExpDocBean.getStart_date());
            values.put(END_DATE, travelEntryExpDocBean.getEnd_date());
            values.put(COUNTRY_NAME, travelEntryExpDocBean.getCountry());
            values.put(LOCATION, travelEntryExpDocBean.getLocation());
            values.put(EXPENSES_TYPE, travelEntryExpDocBean.getExpenses_type());
            values.put(AMOUNT, travelEntryExpDocBean.getAmount());
            values.put(CURRENCY, travelEntryExpDocBean.getCurrency());
            values.put(TAX_CODE, travelEntryExpDocBean.getTax_code());
            values.put(FROM_DATE, travelEntryExpDocBean.getFrom_date());
            values.put(TO_DATE, travelEntryExpDocBean.getTo_date());
            values.put(COUNTRY_NAME1, travelEntryExpDocBean.getRegion());
            values.put(DESCRIPTION, travelEntryExpDocBean.getDescription());
            values.put(LOCATION1, travelEntryExpDocBean.getLocation1());
            values.put(TRAV_TYPE, travelEntryExpDocBean.getType());
            values.put(CARDINFO, travelEntryExpDocBean.getCardinfo());

            // update Row
            long i = db.update(TABLE_TRAVEL_EXP_EXPENSES, values, null, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }

    public void updateEXPTravelTripEntryDocument1(TravelTripExpDocBean travelTripExpDocBean, String serialno) {

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        String selectQuery = null;
        ContentValues values;
        String where = "";

        try {
            values = new ContentValues();
            values.put(PERNR, travelTripExpDocBean.getPernr());
            values.put(SERIALNO, travelTripExpDocBean.getSerialno());
            Log.e("SERIALNO.", "&&&&" + travelTripExpDocBean.getSerialno());
            values.put(START_DATE, travelTripExpDocBean.getStart_date());
            values.put(END_DATE, travelTripExpDocBean.getEnd_date());
            values.put(COUNTRY_NAME, travelTripExpDocBean.getCountry());
            values.put(LOCATION, travelTripExpDocBean.getLocation());
            values.put(EXPENSES_TYPE, travelTripExpDocBean.getExpenses_type());
            values.put(AMOUNT, travelTripExpDocBean.getAmount());
            values.put(CURRENCY, travelTripExpDocBean.getCurrency());
            values.put(TAX_CODE, travelTripExpDocBean.getTax_code());
            values.put(FROM_DATE, travelTripExpDocBean.getFrom_date());
            values.put(TO_DATE, travelTripExpDocBean.getTo_date());
            values.put(COUNTRY_NAME1, travelTripExpDocBean.getRegion());
            values.put(DESCRIPTION, travelTripExpDocBean.getDescription());
            values.put(LOCATION1, travelTripExpDocBean.getLocation1());
            values.put(TRAV_TYPE, travelTripExpDocBean.getType());
            values.put(REINR, travelTripExpDocBean.getReinr());
            values.put(CARDINFO, travelTripExpDocBean.getCardinfo());

            // update Row
            long i = db.update(TABLE_TRAVEL_EXP_EXPENSES1, values, null, null);
            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }

    }


    public boolean isRecordExist(String tablename, String field, String fieldvalue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor c;

        String Query = "SELECT * FROM " + tablename + " WHERE " +  field +  " = '" +  fieldvalue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }



    // Insert Pending leave
    public void createPendingLeave(
            String key_lev_no,
            String horo,
            String ename,
            String lev_typ,
            String lev_frm,
            String lev_to,
            String reason,
            String chrg_name1,
            String chrg_name2,
            String chrg_name3,
            String chrg_name4,
            String direct_indirect) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_LEV_NO, key_lev_no);
        values.put(HORO, horo);
        values.put(ENAME, ename);
        values.put(LEV_TYP, lev_typ);
        values.put(LEV_FRM, lev_frm);
        values.put(LEV_TO, lev_to);
        values.put(REASON, reason);
        values.put(CHRG_NAME1, chrg_name1);
        values.put(CHRG_NAME2, chrg_name2);
        values.put(CHRG_NAME3, chrg_name3);
        values.put(CHRG_NAME4, chrg_name4);
        values.put(DIRECT_INDIRECT, direct_indirect);

        // insert row
        Log.d("data_of_values", " " + values);

        Long result = db.insert(TABLE_PENDING_LEAVE, null, values);

        Log.d("data_of_table", " " + result);

    }

    // Insert Pending leave
    public void createPendingGatepass(
            String gp_no,
            String pernr,
            String ename,
            String gpdat1,
            String gptime1,
            String gptypeTxt,
            String reqtypeTxt,
            String directIndirect) {

        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values = new ContentValues();


        try {
            values.put(KEY_LEV_NO, gp_no);
            values.put(PERNR, pernr);
            values.put(ENAME, ename);
            values.put(DATE, gpdat1);
            values.put(TIME, gptime1);
            values.put(GP_TYPE, gptypeTxt);
            values.put(REQ_TYPE, reqtypeTxt);
            values.put(D_IN, directIndirect);

            // insert row
            Log.d("data_of_gp", " " + values);

            Long result = db.insert(TABLE_PENDING_GATEPASS, null, values);

            Log.d("data_of_tgp", " " + result);
            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }

    }


    // Insert Pending OD
    public void createPendingOD(

            String key_od_no,
            String od_horo,
            String od_ename,
            String od_work_status,
            String od_frm,
            String od_to,
            String visit_place,
            String purpose1,
            String purpose2,
            String purpose3,
            String od_remark,
            String od_direct_indirect) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(KEY_OD_NO, key_od_no);
        values.put(OD_HORO, od_horo);
        values.put(OD_ENAME, od_ename);
        values.put(OD_WORK_STATUS, od_work_status);
        values.put(OD_FRM, od_frm);
        values.put(OD_TO, od_to);
        values.put(VISIT_PLACE, visit_place);
        values.put(PURPOSE1, purpose1);
        values.put(PURPOSE2, purpose2);
        values.put(PURPOSE3, purpose3);
        values.put(REMARK, od_remark);
        values.put(OD_DIRECT_INDIRECT, od_direct_indirect);

        // insert row
        Log.d("data_of_values_od", " " + values);

        Long result = db.insert(TABLE_PENDING_OD, null, values);

        Log.d("data_of_table_od", " " + result);

    }


    // Insert Attendance

    public void insertAttendance(
            String pernr,
            String begdat,
            String indz,
            String iodz,
            String totdz,
            String atn_status,
            String leave_typ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();

            values.put(ATT_KEY_PERNR, pernr);
            values.put(ATT_KEY_BEGDAT, begdat);
            values.put(ATT_KEY_INDZ, indz);
            values.put(ATT_KEY_IODZ, iodz);
            values.put(ATT_KEY_TOTDZ, totdz);
            values.put(ATT_KEY_ATN_STATUS, atn_status);
            values.put(ATT_KEY_LEAVE_TYP, leave_typ);


            // db.delete(TABLE_ATTENDANCE,null,null);

            // Insert Row

            long i = db.insert(TABLE_ATTENDANCE, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {

            e.printStackTrace();

        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();


        }
    }


    // Insert Leave

    public void insertLeave(String pernr, String leav_no, String horo, String lev_frm, String lev_to, String lev_typ,
            String apphod, String dele, String reason) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LEV_KEY_PERNR, pernr);
        values.put(LEV_KEY_LEV_NO, leav_no);
        values.put(LEV_KEY_HORO, horo);
        values.put(LEV_KEY_LEV_FRM, lev_frm);
        values.put(LEV_KEY_LEV_TO, lev_to);
        values.put(LEV_KEY_LEV_TYP, lev_typ);
        values.put(LEV_KEY_APPHOD, apphod);
        values.put(LEV_KEY_DELE, dele);
        values.put(LEV_KEY_REASON, reason);
        // insert row
        Log.d("data_of_values_leave", " " + values);

        Long result = db.insert(TABLE_LEAVE, null, values);

        Log.d("data_of_table_leave", " " + result);


    }


    // Insert OD

    public void insertOD(
            String pernr,
            String od_no,
            String od_approved,
            String horo,
            String odstdate_c,
            String odedate_c,
            String atn_status,
            String vplace,
            String purpose1) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(R_OD_PERNR, pernr);
        values.put(R_OD_NO, od_no);
        values.put(R_OD_APPROVED, od_approved);
        values.put(R_OD_HORO, horo);
        values.put(R_OD_FRM, odstdate_c);
        values.put(R_OD_TO, odedate_c);
        values.put(R_OD_WORK_STATUS, atn_status);
        values.put(R_VISIT_PLACE, vplace);
        values.put(R_PURPOSE, purpose1);

        // insert row
        Log.d("data_of_values_od", " " + values);

        Long result = db.insert(TABLE_OD, null, values);

        Log.d("data_of_table_od", " " + result);

    }


    // Insert Employee Info

    public void insertEmployeeInfo(
            String att_emp,
            String btrtlTxt,
            String perskTxt,
            String telnr,
            String emailShkt,
            String hodEname,
            String address,
            String birth1,
            String pernr,
            String bankTxt) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(E_PERNR, att_emp);
        values.put(E_BTRTLTXT, btrtlTxt);
        values.put(E_PERSKTXT, perskTxt);
        values.put(E_TELNR, telnr);
        values.put(E_EMAILSHKT, emailShkt);
        values.put(E_HODNAME, hodEname);
        values.put(E_ADDRESS, address);
        values.put(E_BIRTH1, birth1);
        values.put(E_BANKN, pernr);
        values.put(E_BANKTXT, bankTxt);


        // insert row
        Log.d("data_of_values_emp", " " + values);

        Long result = db.insert(TABLE_EMPLOYEE_INFO, null, values);

        Log.d("data_of_table_emp", " " + result);

    }

    public void insertTaskPending(String assigner, String mrc_date, String mrc_time, String agenda, String pernr, String date_from, String date_to,
            String mrc_type, String dep_name, String dno, String srno, String checker_id) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        att_emp
        values.put(KEY_PERNR, assigner);
        values.put(KEY_BUDAT, mrc_date);
        values.put(KEY_TIME_IN, mrc_time);
        values.put(KEY_DESCRIPTION, agenda);
        values.put(KEY_TASK_FOR, pernr);
        values.put(KEY_TASK_DATE_FROM, date_from);
        values.put(KEY_TASK_DATE_TO, date_to);
        values.put(KEY_MRC_TYPE, mrc_type);
        values.put(KEY_DEPARTMENT, dep_name);
        values.put(KEY_DNO, dno);
        values.put(KEY_SRNO, srno);
        values.put(KEY_CHECKER, "");
        values.put(KEY_SYNC, "");
        values.put(KEY_REMARK, "");


        // insert row
        Log.d("data_task_ending", " " + values);

        Long result = db.insert(TABLE_TASK_PENDING, null, values);

        Log.d("data_tbl_task_pending", " " + result);

    }

    //Insert Country Data

    public void insertCountry(String land1, String landx) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LAND1, land1);
        values.put(KEY_LANDX, landx);
        // insert row
        Log.d("data_country", " " + values);

        Long result = db.insert(TABLE_COUNTRY, null, values);

        Log.d("data_country", " " + result);

    }

    //Insert Region Data

    public void insertRegion(String land1, String bland, String regio, String bezei) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LAND1, land1);
        values.put(KEY_BLAND, bland);
        values.put(KEY_REGIO, regio);
        values.put(KEY_BEZEI, bezei);

        // insert row
        Log.d("data_region", " " + values);

        Long result = db.insert(TABLE_REGION, null, values);

        Log.d("data_region", " " + result);

    }

    //insert District Data

    public void insertDistrict(String land1, String regio, String cityc, String bezei) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LAND1, land1);
        values.put(KEY_REGIO, regio);
        values.put(KEY_CITYC, cityc);
        values.put(KEY_BEZEI, bezei);

        Long result = db.insert(TABLE_DISTRICT, null, values);


    }

    //insert Tehsil Data

    public void insertTehsil(String land1, String regio, String district, String tehsil, String tehsil_txt) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LAND1, land1);
        values.put(KEY_REGIO, regio);
        values.put(KEY_DISTRICT, district);
        values.put(KEY_TEHSIL, tehsil);
        values.put(KEY_TEHSIL_TEXT, tehsil_txt);

        Long result = db.insert(TABLE_TEHSIL, null, values);


    }

    public void insertTaxcode(String mandt, String taxcode, String text) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MANDT, mandt);
        values.put(KEY_TAXCODE, taxcode);
        values.put(KEY_TEXT, text);
       db.insert(TABLE_TAXCODE, null, values);
    }

    public void insertVendorcode(VendorListModel.Response response) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_VENDOR_CODE, response.getLifnr());
        values.put(KEY_VENDOR_NAME, response.getName1());
        values.put(KEY_VENDOR_ADDRESS, response.getAdd());
        values.put(KEY_VENDOR_CONTACT_NO, response.getTelf1());
        values.put(KEY_VENDOR_STREET_ADDRESS, response.getStras());
        values.put(KEY_VENDOR_REGION, response.getOrt01());
        values.put(KEY_VENDOR_CITY, response.getOrt02());
        db.insert(TABLE_VENDORCODE, null, values);
    }

    public void inserGatePassData(GatePassModel.Response response) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_GATEPASS_NO, response.getDocno());
        values.put(KEY_GATEPASS_VISITORNAME, response.getNameVisitor());
        db.insert(TABLE_OPEN_GATE_PASS, null, values);
    }

    public void insertExpenses(String spkzl, String sptxt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SPKZL, spkzl);
        values.put(KEY_SPTXT, sptxt);
        Long result = db.insert(TABLE_EXPTYPE, null, values);

    }

    public void insertCurrency(String waers, String ltext) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_WAERS, waers);
        values.put(KEY_LTEXT, ltext);

        Long result = db.insert(TABLE_CURRENCY, null, values);


    }


    // Insert Task Create Data
    public void createTask(String key_pernr, String key_budat, String key_time_in, String key_description, String key_task_for, String key_task_date_from, String key_task_date_to, String key_task_mrc_type, String key_task_department) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PERNR, key_pernr);
        values.put(KEY_BUDAT, key_budat);
        values.put(KEY_TIME_IN, key_time_in);
        values.put(KEY_DESCRIPTION, key_description);
        values.put(KEY_TASK_FOR, key_task_for);
        values.put(KEY_TASK_DATE_FROM, key_task_date_from);
        values.put(KEY_TASK_DATE_TO, key_task_date_to);
        values.put(KEY_MRC_TYPE, key_task_mrc_type);
        values.put(KEY_DEPARTMENT, key_task_department);

        Long result = db.insert(TABLE_TASK_CREATE, null, values);



    }


    // Select data from Data base


    //Active employee data
    @SuppressLint("Range")
    public ArrayList<String> getDiscription() {


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_ACTIVE_EMPLOYEE;
        /*+ " WHERE " + KEY_MAKTX + " = '"+maktx+"'" + " AND " + KEY_KUNNR + " = '"+kunnr+"'";*/
        /*"SELECT "+ KEY_ENAME  + " FROM " + TABLE_ACTIVE_EMPLOYEE;*/
        /*+ " WHERE " + KEY_EXTWG + " = '"+model+"'";*/

        Cursor c = db.rawQuery(selectQuery, null);

        Log.d("res_of_select", " " + c.getCount());

        ArrayList<String> discription = new ArrayList<String>();

        discription.add("Select Person");

        if(CustomUtility.doesTableExist(db,TABLE_ACTIVE_EMPLOYEE)) {
            if (c.moveToFirst()) {
                while (!c.isAfterLast()) {
                    /*  Log.d("discription",c.getString(c.getColumnIndex(KEY_ENAME)));*/
                    discription.add(c.getString(c.getColumnIndex(KEY_ENAME)));
                    c.moveToNext();
                }
            }

            Log.d("discription", "" + discription);
        }
        return discription;
    }


    @SuppressLint("Range")
    public ArrayList<String> getLeaveBalance() {


        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_LEAVE_BALANCE;
        /*+ " WHERE " + KEY_MAKTX + " = '"+maktx+"'" + " AND " + KEY_KUNNR + " = '"+kunnr+"'";*/
        /*"SELECT "+ KEY_ENAME  + " FROM " + TABLE_ACTIVE_EMPLOYEE;*/
        /*+ " WHERE " + KEY_EXTWG + " = '"+model+"'";*/

        Cursor d = db.rawQuery(selectQuery, null);


        ArrayList<String> leavebalance = new ArrayList<String>();

        leavebalance.add("Select Leave Type");
        if(CustomUtility.doesTableExist(db,TABLE_LEAVE_BALANCE)) {

            if (d.moveToFirst()) {
                while (!d.isAfterLast()) {
                    leavebalance.add(d.getString(d.getColumnIndex(KEY_LEAVETYPE)));
                    d.moveToNext();
                }
            }

            Log.d("leavebalance", "" + leavebalance);
        }
        return leavebalance;
    }



    // Select Pending Leave Direct Reporting data from database table and put into array

    @SuppressLint("Range")
    public ArrayList<States> getPendingLeaveDirect() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PENDING_LEAVE + " where  DIRECT_INDIRECT = 'X' ";

        Cursor d = db.rawQuery(selectQuery, null);

        // Log.d("res_of_pending_leave", " " + d.getCount());

        ArrayList<States> pendingLeaveList = new ArrayList<States>();

        if(CustomUtility.doesTableExist(db,TABLE_PENDING_LEAVE)) {
            if (d.moveToFirst()) {
                while (!d.isAfterLast()) {
                    States state = new States();
                    state.setCode(d.getString(d.getColumnIndex(KEY_LEV_NO)));
                    state.setHoro(d.getString(d.getColumnIndex(HORO)));
                    state.setName(d.getString(d.getColumnIndex(ENAME)));
                    state.setLeave_type(d.getString(d.getColumnIndex(LEV_TYP)));
                    state.setLeave_from(d.getString(d.getColumnIndex(LEV_FRM)));
                    state.setLeave_to(d.getString(d.getColumnIndex(LEV_TO)));
                    state.setLeave_reason(d.getString(d.getColumnIndex(REASON)));
                    pendingLeaveList.add(state);
                    d.moveToNext();
                }
            }

            Log.d("pending_leave_list", "" + pendingLeaveList);
        }
        return pendingLeaveList;
    }


    @SuppressLint("Range")
    public ArrayList<Country> getCountry() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_COUNTRY;

        Cursor d = db.rawQuery(selectQuery, null);

        // Log.d("res_country", " " + d.getCount());

        ArrayList<Country> countryArrayList = new ArrayList<Country>();

        if(CustomUtility.doesTableExist(db,TABLE_COUNTRY)) {
            if (d.moveToFirst()) {
                while (!d.isAfterLast()) {
                    Country country = new Country();
                    country.setLand1(d.getString(d.getColumnIndex(KEY_LAND1)));
                    country.setLandx(d.getString(d.getColumnIndex(KEY_LANDX)));

                    countryArrayList.add(country);
                    d.moveToNext();
                }
            }

            Log.d("country_list", "" + countryArrayList);
        }
        return countryArrayList;
    }

    @SuppressLint("Range")
    public ArrayList<Region> getRegion(String land1) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_REGION + " WHERE " + KEY_LAND1 + "='" + land1 + "'";

        Cursor d = db.rawQuery(selectQuery, null);

        // Log.d("res_region", " " + d.getCount());

        ArrayList<Region> regionArrayList = new ArrayList<Region>();

        if(CustomUtility.doesTableExist(db,TABLE_REGION)) {
            if (d.moveToFirst()) {
                while (!d.isAfterLast()) {
                    Region region = new Region();
                    region.setLand1(d.getString(d.getColumnIndex(KEY_LAND1)));
                    region.setBland(d.getString(d.getColumnIndex(KEY_BLAND)));
                    region.setRegio(d.getString(d.getColumnIndex(KEY_REGIO)));
                    region.setBezei(d.getString(d.getColumnIndex(KEY_BEZEI)));

                    regionArrayList.add(region);
                    d.moveToNext();
                }
            }

            Log.d("region_list", "" + regionArrayList);
        }
        return regionArrayList;
    }


    @SuppressLint("Range")
    public ArrayList<District> getDistrict(String land1, String regio) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DISTRICT + " WHERE " + KEY_LAND1 + "='" + land1 + "'" + "AND " + KEY_REGIO + "='" + regio + "'";

        Cursor d = db.rawQuery(selectQuery, null);

        ArrayList<District> districtArrayList = new ArrayList<District>();

        if(CustomUtility.doesTableExist(db,TABLE_DISTRICT)) {
            if (d.moveToFirst()) {
                while (!d.isAfterLast()) {
                    District district = new District();
                    district.setLand1(d.getString(d.getColumnIndex(KEY_LAND1)));
                    district.setRegio(d.getString(d.getColumnIndex(KEY_REGIO)));
                    district.setCityc(d.getString(d.getColumnIndex(KEY_CITYC)));
                    district.setBezei(d.getString(d.getColumnIndex(KEY_BEZEI)));

                    districtArrayList.add(district);
                    d.moveToNext();
                }
            }

            Log.d("district_list", "" + districtArrayList);
        }
        return districtArrayList;
    }


    @SuppressLint("Range")
    public ArrayList<Tehsil> getTehsil(String cityc) {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TEHSIL + " WHERE " + KEY_DISTRICT + "='" + cityc + "'";

        Cursor d = db.rawQuery(selectQuery, null);
        ArrayList<Tehsil> tehsilArrayList = new ArrayList<Tehsil>();

        if(CustomUtility.doesTableExist(db,TABLE_TEHSIL)) {
            if (d.moveToFirst()) {
                while (!d.isAfterLast()) {
                    Tehsil tehsil = new Tehsil();
                    tehsil.setLand1(d.getString(d.getColumnIndex(KEY_LAND1)));
                    tehsil.setRegio(d.getString(d.getColumnIndex(KEY_REGIO)));
                    tehsil.setDistrict(d.getString(d.getColumnIndex(KEY_DISTRICT)));
                    tehsil.setTehsil(d.getString(d.getColumnIndex(KEY_TEHSIL)));
                    tehsil.setTehsil_txt(d.getString(d.getColumnIndex(KEY_TEHSIL_TEXT)));

                    tehsilArrayList.add(tehsil);
                    d.moveToNext();
                }
            }

            Log.d("tehsil_list", "" + tehsilArrayList);
        }
        return tehsilArrayList;
    }


    @SuppressLint("Range")
    public ArrayList<Taxcode> getTaxcode() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_TAXCODE;
        Cursor d = db.rawQuery(selectQuery, null);
        ArrayList<Taxcode> taxcodeArrayList = new ArrayList<Taxcode>();
        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                Taxcode taxcode = new Taxcode();
                taxcode.setMandt(d.getString(d.getColumnIndex(KEY_MANDT)));
                taxcode.setTaxcode(d.getString(d.getColumnIndex(KEY_TAXCODE)));
                taxcode.setText(d.getString(d.getColumnIndex(KEY_TEXT)));
                taxcodeArrayList.add(taxcode);
                d.moveToNext();
            }
        }
        return taxcodeArrayList;
    }

    @SuppressLint("Range")
    public ArrayList<VendorListModel.Response> getVendorcode(String code) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<VendorListModel.Response> taxcodeArrayList = new ArrayList<VendorListModel.Response>();
        if(CustomUtility.doesTableExist(db,TABLE_VENDORCODE)){
        String selectQuery = "SELECT  * FROM " + TABLE_VENDORCODE+ " WHERE " + KEY_VENDOR_CODE + " LIKE '%" + code + "%'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                VendorListModel.Response vendorListModel = new VendorListModel.Response();
                vendorListModel.setLifnr(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_CODE)));
                vendorListModel.setName1(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_NAME)));
                vendorListModel.setAdd(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_ADDRESS)));
                vendorListModel.setTelf1(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_CONTACT_NO)));
                vendorListModel.setStras(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_STREET_ADDRESS)));
                vendorListModel.setOrt01(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_REGION)));
                vendorListModel.setOrt02(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_CITY)));
                taxcodeArrayList.add(vendorListModel);
                cursor.moveToNext();
            }
        }
        }
        return taxcodeArrayList;
    }


    @SuppressLint("Range")
    public ArrayList<GatePassModel.Response> gatePassList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_OPEN_GATE_PASS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<GatePassModel.Response> taxcodeArrayList = new ArrayList<GatePassModel.Response>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                GatePassModel.Response gatePassModel = new GatePassModel.Response();
                gatePassModel.setDocno(cursor.getString(cursor.getColumnIndex(KEY_GATEPASS_NO)));
                gatePassModel.setNameVisitor(cursor.getString(cursor.getColumnIndex(KEY_GATEPASS_VISITORNAME)));
                taxcodeArrayList.add(gatePassModel);
                cursor.moveToNext();
            }
        }
        return taxcodeArrayList;
    }


    @SuppressLint("Range")
    public ArrayList<Expenses> getExpenses() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_EXPTYPE;

        Cursor d = db.rawQuery(selectQuery, null);

        //Log.d("res_exp", " " + d.getCount());

        ArrayList<Expenses> expensesArrayList = new ArrayList<Expenses>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                Expenses expenses = new Expenses();
                expenses.setSpkzl(d.getString(d.getColumnIndex(KEY_SPKZL)));
                expenses.setSptxt(d.getString(d.getColumnIndex(KEY_SPTXT)));


                expensesArrayList.add(expenses);
                d.moveToNext();
            }
        }

        Log.d("expenses", "" + expensesArrayList);
        return expensesArrayList;
    }

    @SuppressLint("Range")
    public ArrayList<Currency> getCurrency() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CURRENCY;

        Cursor d = db.rawQuery(selectQuery, null);

        //Log.d("res_curr", " " + d.getCount());

        ArrayList<Currency> currencyArrayList = new ArrayList<Currency>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                Currency currency = new Currency();
                currency.setWaser(d.getString(d.getColumnIndex(KEY_WAERS)));
                currency.setLtext(d.getString(d.getColumnIndex(KEY_LTEXT)));


                currencyArrayList.add(currency);
                d.moveToNext();
            }
        }

        Log.d("currency", "" + currencyArrayList);
        return currencyArrayList;
    }


    @SuppressLint("Range")
    public ArrayList<Gatepass> getPendingGatepassDirect() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PENDING_GATEPASS + " where  DIRECT_INDIRECT = 'D' ";

        Cursor d = db.rawQuery(selectQuery, null);

        //Log.d("res_of_pending_gp", " " + d.getCount());

        ArrayList<Gatepass> pendingGatepassList = new ArrayList<Gatepass>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                Gatepass gp = new Gatepass();
                gp.setGp_no(d.getString(d.getColumnIndex(KEY_LEV_NO)));
                gp.setPernr(d.getString(d.getColumnIndex(PERNR)));
                gp.setEname(d.getString(d.getColumnIndex(ENAME)));
                gp.setDate(d.getString(d.getColumnIndex(DATE)));
                gp.setTime(d.getString(d.getColumnIndex(TIME)));
                gp.setReq_type(d.getString(d.getColumnIndex(REQ_TYPE)));
                gp.setGp_type(d.getString(d.getColumnIndex(GP_TYPE)));
                gp.setDirect_indirect(d.getString(d.getColumnIndex(DIRECT_INDIRECT)));
                pendingGatepassList.add(gp);
                d.moveToNext();
            }
        }

        Log.d("pending_gp_list", "" + pendingGatepassList);
        return pendingGatepassList;
    }


    @SuppressLint("Range")
    public ArrayList<Gatepass> getPendingGatepassInDirect() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PENDING_GATEPASS + " where  DIRECT_INDIRECT = 'I' ";

        Cursor d = db.rawQuery(selectQuery, null);

        //Log.d("res_of_pending_gp", " " + d.getCount());

        ArrayList<Gatepass> pendingGatepassList = new ArrayList<Gatepass>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                Gatepass gp = new Gatepass();
                gp.setGp_no(d.getString(d.getColumnIndex(KEY_LEV_NO)));
                gp.setPernr(d.getString(d.getColumnIndex(PERNR)));
                gp.setEname(d.getString(d.getColumnIndex(ENAME)));
                gp.setDate(d.getString(d.getColumnIndex(DATE)));
                gp.setTime(d.getString(d.getColumnIndex(TIME)));
                gp.setReq_type(d.getString(d.getColumnIndex(REQ_TYPE)));
                gp.setGp_type(d.getString(d.getColumnIndex(GP_TYPE)));
                gp.setDirect_indirect(d.getString(d.getColumnIndex(DIRECT_INDIRECT)));
                pendingGatepassList.add(gp);
                d.moveToNext();
            }
        }

        Log.d("pending_gp_list", "" + pendingGatepassList);
        return pendingGatepassList;
    }


    // Select Pending Leave InDirect Reporting data from database table and put into array
    @SuppressLint("Range")
    public ArrayList<States> getPendingLeaveInDirect() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PENDING_LEAVE + " where  DIRECT_INDIRECT = 'Y' ";

        Cursor d = db.rawQuery(selectQuery, null);

        //Log.d("res_of_pending_leave", " " + d.getCount());

        ArrayList<States> pendingLeaveList = new ArrayList<States>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                States state = new States();
                state.setCode(d.getString(d.getColumnIndex(KEY_LEV_NO)));
                state.setHoro(d.getString(d.getColumnIndex(HORO)));
                state.setName(d.getString(d.getColumnIndex(ENAME)));
                state.setLeave_type(d.getString(d.getColumnIndex(LEV_TYP)));
                state.setLeave_from(d.getString(d.getColumnIndex(LEV_FRM)));
                state.setLeave_to(d.getString(d.getColumnIndex(LEV_TO)));
                state.setLeave_reason(d.getString(d.getColumnIndex(REASON)));
                pendingLeaveList.add(state);
                d.moveToNext();
            }
        }

        Log.d("pending_leave_list", "" + pendingLeaveList);
        return pendingLeaveList;
    }


    // Select Pending OD data from database table and put into array
    @SuppressLint("Range")
    public ArrayList<OD> getPendingOdDirect() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PENDING_OD + " where  OD_DIRECT_INDIRECT = 'Y' ";

        Cursor d = db.rawQuery(selectQuery, null);

        // Log.d("res_of_pending_od", " " + d.getCount());

        ArrayList<OD> pendingODList = new ArrayList<OD>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                OD od = new OD();
                od.setOd_no(d.getString(d.getColumnIndex(KEY_OD_NO)));
                od.setHoro(d.getString(d.getColumnIndex(OD_HORO)));
                od.setEname(d.getString(d.getColumnIndex(OD_ENAME)));
                od.setOd_from(d.getString(d.getColumnIndex(OD_FRM)));
                od.setOd_to(d.getString(d.getColumnIndex(OD_TO)));
                od.setWork_status(d.getString(d.getColumnIndex(OD_WORK_STATUS)));
                od.setVisit_place(d.getString(d.getColumnIndex(VISIT_PLACE)));
                od.setPurpose1(d.getString(d.getColumnIndex(PURPOSE1)));
                pendingODList.add(od);
                d.moveToNext();
            }
        }

        Log.d("pending_leave_list", "" + pendingODList);
        return pendingODList;
    }


    // Select Pending Indirect Reporting OD data from database table and put into array

    @SuppressLint("Range")
    public ArrayList<OD> getPendingOdIndirect() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PENDING_OD + " where  OD_DIRECT_INDIRECT = 'X' ";

        Cursor d = db.rawQuery(selectQuery, null);

        // Log.d("res_of_pending_od", " " + d.getCount());

        ArrayList<OD> pendingODList = new ArrayList<OD>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                OD od = new OD();
                od.setOd_no(d.getString(d.getColumnIndex(KEY_OD_NO)));
                od.setHoro(d.getString(d.getColumnIndex(OD_HORO)));
                od.setEname(d.getString(d.getColumnIndex(OD_ENAME)));
                od.setOd_from(d.getString(d.getColumnIndex(OD_FRM)));
                od.setOd_to(d.getString(d.getColumnIndex(OD_TO)));
                od.setWork_status(d.getString(d.getColumnIndex(OD_WORK_STATUS)));
                od.setVisit_place(d.getString(d.getColumnIndex(VISIT_PLACE)));
                od.setPurpose1(d.getString(d.getColumnIndex(PURPOSE1)));
                pendingODList.add(od);
                d.moveToNext();
            }
        }

        Log.d("pending_leave_list", "" + pendingODList);
        return pendingODList;
    }


    // Select Personal Info data from database table and put into array

    @SuppressLint("Range")
    public ArrayList<PersonalInfo> getPersonalData() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEE_INFO;

        Cursor d = db.rawQuery(selectQuery, null);

        //Log.d("res_of_emp_info", " " + d.getCount());

        ArrayList<PersonalInfo> personalInfoArrayList = new ArrayList<PersonalInfo>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {

                PersonalInfo personalInfo = new PersonalInfo();
                personalInfo.setDept(d.getString(d.getColumnIndex(E_BTRTLTXT)));
                personalInfo.setDesig(d.getString(d.getColumnIndex(E_PERSKTXT)));
                personalInfo.setMob(d.getString(d.getColumnIndex(E_TELNR)));
                personalInfo.setEmail(d.getString(d.getColumnIndex(E_EMAILSHKT)));
                personalInfo.setHod(d.getString(d.getColumnIndex(E_HODNAME)));
                personalInfo.setAdd(d.getString(d.getColumnIndex(E_ADDRESS)));
                personalInfo.setDob(d.getString(d.getColumnIndex(E_BIRTH1)));
                personalInfo.setAcno(d.getString(d.getColumnIndex(E_BANKN)));
                personalInfo.setBank(d.getString(d.getColumnIndex(E_BANKTXT)));
                personalInfoArrayList.add(personalInfo);
                d.moveToNext();
            }
        }

        Log.d("Personal Info", "" + personalInfoArrayList);
        return personalInfoArrayList;
    }


    // Select Task Created from database table and put into array
    @SuppressLint("Range")
    public ArrayList<TaskCreated> getTaskCreated() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASK_CREATE + " where SYNC IS NULL ";

        Cursor d = db.rawQuery(selectQuery, null);

        // Log.d("res_of_task_created", " " + d.getCount());

        ArrayList<TaskCreated> task_created = new ArrayList<TaskCreated>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                TaskCreated task = new TaskCreated();
                task.setPernr(d.getString(d.getColumnIndex(KEY_PERNR)));
                task.setCurrentDate(d.getString(d.getColumnIndex(KEY_BUDAT)));
                task.setCurrentTime(d.getString(d.getColumnIndex(KEY_TIME_IN)));
                task.setDescription(d.getString(d.getColumnIndex(KEY_DESCRIPTION)));
                task.setTask_assign_to(d.getString(d.getColumnIndex(KEY_TASK_FOR)));
                task.setFromDateEtxt(d.getString(d.getColumnIndex(KEY_TASK_DATE_FROM)));
                task.setToDateEtxt(d.getString(d.getColumnIndex(KEY_TASK_DATE_TO)));
                task.setSync(d.getString(d.getColumnIndex(KEY_SYNC)));
                task.setMrc_type(d.getString(d.getColumnIndex(KEY_MRC_TYPE)));
                task.setDepartment(d.getString(d.getColumnIndex(KEY_DEPARTMENT)));
                task_created.add(task);
                d.moveToNext();
            }
        }

        Log.d("task_created", "" + task_created);
        return task_created;
    }


    // Select Task Pending from database table and put into array
    @SuppressLint("Range")
    public ArrayList<TaskPending> getPendingTask() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASK_PENDING + " where SYNC ='' AND CHECKER_ID=''";
//        AND CHECKER_ID IS NULL
        Cursor d = db.rawQuery(selectQuery, null);

        // Log.d("res_of_task_pending", " " + d.getCount());

        ArrayList<TaskPending> task_pending = new ArrayList<TaskPending>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                TaskPending task = new TaskPending();
                task.setPernr(d.getString(d.getColumnIndex(KEY_PERNR)));
                task.setCurrentDate(d.getString(d.getColumnIndex(KEY_BUDAT)));
                task.setCurrentTime(d.getString(d.getColumnIndex(KEY_TIME_IN)));
                task.setDescription(d.getString(d.getColumnIndex(KEY_DESCRIPTION)));
                task.setTask_assign_to(d.getString(d.getColumnIndex(KEY_TASK_FOR)));
                task.setFromDateEtxt(d.getString(d.getColumnIndex(KEY_TASK_DATE_FROM)));
                task.setToDateEtxt(d.getString(d.getColumnIndex(KEY_TASK_DATE_TO)));
                task.setSync(d.getString(d.getColumnIndex(KEY_SYNC)));
                task.setMrc_type(d.getString(d.getColumnIndex(KEY_MRC_TYPE)));
                task.setDepartment(d.getString(d.getColumnIndex(KEY_DEPARTMENT)));
                task.setSrno(d.getString(d.getColumnIndex(KEY_SRNO)));
                task.setDno(d.getString(d.getColumnIndex(KEY_DNO)));
                task.setChecker(d.getString(d.getColumnIndex(KEY_CHECKER)));
                task_pending.add(task);
                d.moveToNext();
            }
        }

        Log.d("task_pending", "" + task_pending);
        return task_pending;
    }


    // Select Task Completed from database table and put into array
    @SuppressLint("Range")
    public ArrayList<TaskPending> getCompletedTask() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASK_PENDING + " where CHECKER_ID <> '' AND SYNC ='' ";

        Cursor d = db.rawQuery(selectQuery, null);

        // Log.d("res_of_task_completed", " " + d.getCount());

        ArrayList<TaskPending> task_completed = new ArrayList<TaskPending>();


        if (d.moveToFirst()) {
            while (!d.isAfterLast()) {
                TaskPending task = new TaskPending();

                task.setSrno(d.getString(d.getColumnIndex(KEY_SRNO)));
                task.setDno(d.getString(d.getColumnIndex(KEY_DNO)));
                task.setRemark(d.getString(d.getColumnIndex(KEY_REMARK)));
                task_completed.add(task);
                d.moveToNext();
            }
        }

        Log.d("task_completed", "" + task_completed);
        return task_completed;
    }

    // Insert Leave Balance Data to table

    /**********************  insert login detail ************************************/
    public void insertLoginData(
            String lv_pernr,
            String lv_ename,
            String lv_password,
            String lv_mob_atnd,
            String lv_travel,
            String lv_hod
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(KEY_PERNR, lv_pernr);
            values.put(KEY_ENAME, lv_ename);
            values.put(KEY_MOB_ATND, lv_mob_atnd);
            values.put(KEY_TRAVEL, lv_travel);
            values.put(KEY_PASSWORD, lv_password);
            values.put(KEY_HOD, lv_hod);


            // Insert Row
            long i = db.insert(TABLE_LOGIN, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }


    /**********************************Travel Head Data *******************************************/
    public void insertTravelHead(TravelHeadBean travelHeadBean) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_LINK, travelHeadBean.getLink());
            values.put(KEY_STATUS, travelHeadBean.getAntrg_txt());
            values.put(KEY_START_DATE, travelHeadBean.getDatv1_char());
            values.put(KEY_END_DATE, travelHeadBean.getDatb1_char());
            values.put(KEY_TRIP_TOTAL, travelHeadBean.getTrip_total());
            values.put(KEY_PERNR, travelHeadBean.getPernr());
            values.put(KEY_REINR, travelHeadBean.getReinr());
            values.put(KEY_CITY, travelHeadBean.getZort1());
            values.put(KEY_COUNTRY_TEXT, travelHeadBean.getZland_txt());

            // Insert Row
            long i = db.insert(TABLE_TRAVEL_HEAD, null, values);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }


    }


    public void updateTravelHead(TravelHeadBean travelHeadBean) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {
            values = new ContentValues();
            values.put(KEY_LINK, travelHeadBean.getLink());
            values.put(KEY_STATUS, travelHeadBean.getAntrg_txt());
            values.put(KEY_START_DATE, travelHeadBean.getDatv1_char());
            values.put(KEY_END_DATE, travelHeadBean.getDatb1_char());
            values.put(KEY_TRIP_TOTAL, travelHeadBean.getTrip_total());
            values.put(KEY_PERNR, travelHeadBean.getPernr());
            values.put(KEY_REINR, travelHeadBean.getReinr());
            values.put(KEY_CITY, travelHeadBean.getZort1());
            values.put(KEY_COUNTRY_TEXT, travelHeadBean.getZland_txt());

            // Insert Row
            long i = db.update(TABLE_TRAVEL_HEAD, values, null, null);

            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }


    }


    /*********************************** select login data  ***************************************/
    @SuppressLint("Range")
    public boolean getLogin() {
        long t = 0;
        SQLiteDatabase db = null;
        String selectQuery = null;
        Cursor c = null;

        try {
            db = this.getReadableDatabase();
            selectQuery = "SELECT  * FROM " + TABLE_LOGIN;


            c = db.rawQuery(selectQuery, null);
            LoginBean lb = new LoginBean();
            Log.d("login", "" + c.getCount());

            if (c.getCount() > 0) {

                if (c.moveToFirst()) {
                    lb.setLogin(c.getString(c.getColumnIndex(KEY_PERNR)),
                            c.getString(c.getColumnIndex(KEY_ENAME)),
                            c.getString(c.getColumnIndex(KEY_MOB_ATND)), c.getString(c.getColumnIndex(KEY_TRAVEL)), c.getString(c.getColumnIndex(KEY_HOD)));

                }
                return true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }


        return false;
    }


    /*********************************** select emp data  ***************************************/

    public boolean getemp() {
        long t = 0;
        SQLiteDatabase db = null;
        String selectQuery = null;
        Cursor c = null;

        try {
            db = this.getReadableDatabase();
            selectQuery = "SELECT  * FROM " + TABLE_ACTIVE_EMPLOYEE;


            c = db.rawQuery(selectQuery, null);
            LoginBean lb = new LoginBean();
            Log.d("login", "" + c.getCount());

            if (c.getCount() > 0) {

                return true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            c.close();
            db.close();
        }


        return false;
    }


    @SuppressLint("Range")
    public AttendanceBean getMarkAttendanceByDate(String date, Context mContext) {
        AttendanceBean array_list = new AttendanceBean();
        Cursor res = null;
        SQLiteDatabase db = null;
        try {

            userModel = new LoggedInUser(mContext);


            db = this.getReadableDatabase();

//            String query = "select * from " + TABLE_MARK_ATTENDANCE ;


            Log.d("get_atnd", userModel.uid + "---" + date);

            String query = "select * from " + TABLE_MARK_ATTENDANCE + " where  BEGDA = " + "'" + date + "'" +
                    " AND " + PERNR + " = '" + userModel.uid + "'";


////             String query = "select * from " + TABLE_MARK_ATTENDANCE+" where  BEGDA = "+"'"+date+"'";
////        +  " WHERE " + KEY_PERNR +  " = '" +userid.trim()+"'"
////                + " AND " + KEY_CUSTOMER_CATAGORY  + " = '" + "NEW" + "'" ;


            res = db.rawQuery(query, null);

            res.moveToFirst();
            while (res.isAfterLast() == false) {
                AttendanceBean bean = new AttendanceBean();
                // bean.ID = (res.getString(res.getColumnIndex(AttendanceDBFields.ID)));
                bean.TYPE = (res.getString(res.getColumnIndex(TYPE)));
                bean.PERNR = (res.getString(res.getColumnIndex(PERNR)));
                bean.BEGDA = (res.getString(res.getColumnIndex(BEGDA)));
                bean.SERVER_DATE_IN = (res.getString(res.getColumnIndex(SERVER_DATE_IN)));
                bean.SERVER_TIME_IN = (res.getString(res.getColumnIndex(SERVER_TIME_IN)));
                bean.SERVER_DATE_OUT = (res.getString(res.getColumnIndex(SERVER_DATE_OUT)));
                bean.SERVER_TIME_OUT = (res.getString(res.getColumnIndex(SERVER_TIME_OUT)));
                bean.IN_ADDRESS = (res.getString(res.getColumnIndex(IN_ADDRESS)));
                bean.OUT_ADDRESS = (res.getString(res.getColumnIndex(OUT_ADDRESS)));
                bean.IN_TIME = (res.getString(res.getColumnIndex(IN_TIME)));
                bean.OUT_TIME = (res.getString(res.getColumnIndex(OUT_TIME)));
                bean.WORKING_HOURS = (res.getString(res.getColumnIndex(WORKING_HOURS)));
                bean.IMAGE_DATA = (res.getString(res.getColumnIndex(IMAGE_DATA)));

                try {
                    bean.CURRENT_MILLIS = (Long.parseLong(res.getString(res.getColumnIndex(CURRENT_MILLIS))));
                } catch (Exception e) {
                    System.out.println("" + e.getMessage());
                }
                bean.IN_LAT_LONG = (res.getString(res.getColumnIndex(IN_LAT_LONG)));
                bean.OUT_LAT_LONG = (res.getString(res.getColumnIndex(OUT_LAT_LONG)));
                bean.IN_FILE_NAME = (res.getString(res.getColumnIndex(IN_FILE_NAME)));
                bean.IN_FILE_LENGTH = (res.getString(res.getColumnIndex(IN_FILE_LENGTH)));
                bean.IN_FILE_VALUE = (res.getString(res.getColumnIndex(IN_FILE_VALUE)));
                bean.OUT_FILE_NAME = (res.getString(res.getColumnIndex(OUT_FILE_NAME)));
                bean.OUT_FILE_LENGTH = (res.getString(res.getColumnIndex(OUT_FILE_LENGTH)));
                bean.OUT_FILE_VALUE = (res.getString(res.getColumnIndex(OUT_FILE_VALUE)));

                bean.IN_STATUS = (res.getString(res.getColumnIndex(IN_STATUS)));
                bean.OUT_STATUS = (res.getString(res.getColumnIndex(OUT_STATUS)));


                //array_list.add(bean);
                array_list = bean;
                res.moveToNext();
            }
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        } finally {

//            res.close();
            db.close();
        }


        return array_list;
    }


    /*   get all attendance */

    @SuppressLint("Range")
    public ArrayList<AttendanceBean> getAllAttendance() {
        ArrayList<AttendanceBean> array_list = new ArrayList<AttendanceBean>();
        SQLiteDatabase db = null;
        Cursor res = null;
        try {
            db = this.getReadableDatabase();
            res = db.rawQuery("select * from " + TABLE_MARK_ATTENDANCE, null);

            res.moveToFirst();
            while (res.isAfterLast() == false) {
                AttendanceBean bean = new AttendanceBean();
                //   bean.ID = (res.getString(res.getColumnIndex(AttendanceDBFields.ID)));
                bean.TYPE = (res.getString(res.getColumnIndex(TYPE)));
                bean.PERNR = (res.getString(res.getColumnIndex(PERNR)));
                bean.BEGDA = (res.getString(res.getColumnIndex(BEGDA)));
                bean.SERVER_DATE_IN = (res.getString(res.getColumnIndex(SERVER_DATE_IN)));
                bean.SERVER_TIME_IN = (res.getString(res.getColumnIndex(SERVER_TIME_IN)));
                bean.SERVER_DATE_OUT = (res.getString(res.getColumnIndex(SERVER_DATE_OUT)));
                bean.SERVER_TIME_OUT = (res.getString(res.getColumnIndex(SERVER_TIME_OUT)));
                bean.IN_ADDRESS = (res.getString(res.getColumnIndex(IN_ADDRESS)));
                bean.OUT_ADDRESS = (res.getString(res.getColumnIndex(OUT_ADDRESS)));
                bean.IN_TIME = (res.getString(res.getColumnIndex(IN_TIME)));
                bean.OUT_TIME = (res.getString(res.getColumnIndex(OUT_TIME)));
                bean.WORKING_HOURS = (res.getString(res.getColumnIndex(WORKING_HOURS)));
                bean.IMAGE_DATA = (res.getString(res.getColumnIndex(IMAGE_DATA)));

// add by mayank 31.12.2016
                bean.IN_IMAGE = (res.getString(res.getColumnIndex(IN_IMAGE)));
                bean.OUT_IMAGE = (res.getString(res.getColumnIndex(OUT_IMAGE)));

                //  Log.d("select_image_in",""+bean.IN_IMAGE);

                //  Log.d("select_image_out",""+bean.OUT_IMAGE);

// add by mayank 31.12.2016
                try {
                    bean.CURRENT_MILLIS = (Long.parseLong(res.getString(res.getColumnIndex(CURRENT_MILLIS))));
                } catch (Exception e) {
                    System.out.println("" + e.getMessage());
                }
                bean.IN_LAT_LONG = (res.getString(res.getColumnIndex(IN_LAT_LONG)));
                bean.OUT_LAT_LONG = (res.getString(res.getColumnIndex(OUT_LAT_LONG)));
                bean.IN_FILE_NAME = (res.getString(res.getColumnIndex(IN_FILE_NAME)));
                bean.IN_FILE_LENGTH = (res.getString(res.getColumnIndex(IN_FILE_LENGTH)));
                bean.IN_FILE_VALUE = (res.getString(res.getColumnIndex(IN_FILE_VALUE)));
                bean.OUT_FILE_NAME = (res.getString(res.getColumnIndex(OUT_FILE_NAME)));
                bean.OUT_FILE_LENGTH = (res.getString(res.getColumnIndex(OUT_FILE_LENGTH)));
                bean.OUT_FILE_VALUE = (res.getString(res.getColumnIndex(OUT_FILE_VALUE)));

                bean.IN_STATUS = (res.getString(res.getColumnIndex(IN_STATUS)));
                bean.OUT_STATUS = (res.getString(res.getColumnIndex(OUT_STATUS)));
                array_list.add(bean);
                res.moveToNext();
            }
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        } finally {
            res.close();
            db.close();
        }

        return array_list;
    }


    /***********************************  insert mark attendance ******************************/
    public long insertMarkAttendance(AttendanceBean attendanceBean) {

        SQLiteDatabase db = this.getWritableDatabase();
        long t = 0;

        try {

            Log.d("atnd_new_in", attendanceBean.SERVER_DATE_IN + "---" + attendanceBean.SERVER_TIME_IN + "---" +
                    attendanceBean.SERVER_DATE_OUT + "---" + attendanceBean.SERVER_TIME_OUT);

            ContentValues contentValues = new ContentValues();
            contentValues.put(TYPE, attendanceBean.TYPE);
            contentValues.put(PERNR, attendanceBean.PERNR);
            contentValues.put(BEGDA, attendanceBean.BEGDA);
            contentValues.put(SERVER_DATE_IN, attendanceBean.SERVER_DATE_IN);
            contentValues.put(SERVER_TIME_IN, attendanceBean.SERVER_TIME_IN);
            contentValues.put(SERVER_DATE_OUT, attendanceBean.SERVER_DATE_OUT);
            contentValues.put(SERVER_TIME_OUT, attendanceBean.SERVER_TIME_OUT);
            contentValues.put(IN_ADDRESS, attendanceBean.IN_ADDRESS);
            contentValues.put(OUT_ADDRESS, attendanceBean.OUT_ADDRESS);
            contentValues.put(IN_TIME, attendanceBean.IN_TIME);
            contentValues.put(OUT_TIME, attendanceBean.OUT_TIME);
            contentValues.put(WORKING_HOURS, attendanceBean.WORKING_HOURS);
            contentValues.put(IMAGE_DATA, attendanceBean.IMAGE_DATA);
            contentValues.put(CURRENT_MILLIS, attendanceBean.CURRENT_MILLIS);
            contentValues.put(IN_LAT_LONG, attendanceBean.IN_LAT_LONG);
            contentValues.put(OUT_LAT_LONG, attendanceBean.OUT_LAT_LONG);
            contentValues.put(IN_FILE_NAME, attendanceBean.IN_FILE_NAME);
            contentValues.put(IN_FILE_LENGTH, attendanceBean.IN_FILE_LENGTH);
            contentValues.put(IN_FILE_VALUE, attendanceBean.IN_FILE_VALUE);
            contentValues.put(OUT_FILE_NAME, attendanceBean.OUT_FILE_NAME);
            contentValues.put(OUT_FILE_LENGTH, attendanceBean.OUT_FILE_LENGTH);
            contentValues.put(OUT_FILE_VALUE, attendanceBean.OUT_FILE_VALUE);
            contentValues.put(IN_STATUS, attendanceBean.IN_STATUS);
            contentValues.put(OUT_STATUS, attendanceBean.OUT_STATUS);

            contentValues.put(IN_IMAGE, attendanceBean.IN_IMAGE);
            contentValues.put(OUT_IMAGE, attendanceBean.OUT_IMAGE);


            t = db.insert(TABLE_MARK_ATTENDANCE, null, contentValues);

            //System.out.println(t);
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            // db.endTransaction();

            db.close();
        }

        return t;
    }

    @SuppressLint("Range")
    //Dom Expenses Data
    public TravelEntryDomDocBean getDomTravelEntryInformation(String enq_docno, String serialno) {

        TravelEntryDomDocBean travelEntryDomDocBean = new TravelEntryDomDocBean();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT  *  FROM " + TABLE_TRAVEL_DOM_EXPENSES + " WHERE " + PERNR + "='" + enq_docno + "'" + " AND " + SERIALNO + " = '" + serialno + "'";

            cursordom = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom.getCount());

            if (cursordom.getCount() > 0) {
                if (cursordom.moveToFirst()) {
                    while (!cursordom.isAfterLast()) {
                        travelEntryDomDocBean = new TravelEntryDomDocBean();

                        travelEntryDomDocBean.setPernr(cursordom.getString(cursordom.getColumnIndex(PERNR)));
                        travelEntryDomDocBean.setSerialno(cursordom.getString(cursordom.getColumnIndex(SERIALNO)));
                        travelEntryDomDocBean.setStart_date(cursordom.getString(cursordom.getColumnIndex(START_DATE)));
                        travelEntryDomDocBean.setEnd_date(cursordom.getString(cursordom.getColumnIndex(END_DATE)));
                        travelEntryDomDocBean.setCountry(cursordom.getString(cursordom.getColumnIndex(COUNTRY_NAME)));
                        travelEntryDomDocBean.setLocation(cursordom.getString(cursordom.getColumnIndex(LOCATION)));
                        travelEntryDomDocBean.setExpenses_type(cursordom.getString(cursordom.getColumnIndex(EXPENSES_TYPE)));
                        travelEntryDomDocBean.setAmount(cursordom.getString(cursordom.getColumnIndex(AMOUNT)));
                        travelEntryDomDocBean.setCurrency(cursordom.getString(cursordom.getColumnIndex(CURRENCY)));
                        travelEntryDomDocBean.setTax_code(cursordom.getString(cursordom.getColumnIndex(TAX_CODE)));
                        travelEntryDomDocBean.setFrom_date(cursordom.getString(cursordom.getColumnIndex(FROM_DATE)));
                        travelEntryDomDocBean.setTo_date(cursordom.getString(cursordom.getColumnIndex(TO_DATE)));
                        travelEntryDomDocBean.setRegion(cursordom.getString(cursordom.getColumnIndex(REGION)));
                        travelEntryDomDocBean.setDescription(cursordom.getString(cursordom.getColumnIndex(DESCRIPTION)));
                        travelEntryDomDocBean.setLocation1(cursordom.getString(cursordom.getColumnIndex(LOCATION1)));
                        travelEntryDomDocBean.setType(cursordom.getString(cursordom.getColumnIndex(TRAV_TYPE)));
                        travelEntryDomDocBean.setGstin_no(cursordom.getString(cursordom.getColumnIndex(GSTIN_NO)));

                        cursordom.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return travelEntryDomDocBean;
    }



    @SuppressLint("Range")
    public LocalConvenienceBean getLocalConvinienceData(String endat ,String endtm) {

        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        Cursor cursordom;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT * FROM " + TABLE_LOCAL_CONVENIENCE + " WHERE " + KEY_ENDDA + " = '" + endat + "'" + " AND " + KEY_TO_TIME + " = '" + endtm + "'";

            cursordom = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom.getCount());

            if (cursordom.getCount() > 0) {
                if (cursordom.moveToFirst()) {
                    while (!cursordom.isAfterLast()) {
                        localConvenienceBean = new LocalConvenienceBean();

                        localConvenienceBean.setPernr(cursordom.getString(cursordom.getColumnIndex(KEY_PERNR)));
                        localConvenienceBean.setBegda(cursordom.getString(cursordom.getColumnIndex(KEY_BEGDA)));
                        localConvenienceBean.setEndda(cursordom.getString(cursordom.getColumnIndex(KEY_ENDDA)));
                        localConvenienceBean.setFrom_time(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_TIME)));
                        localConvenienceBean.setTo_time(cursordom.getString(cursordom.getColumnIndex(KEY_TO_TIME)));
                        localConvenienceBean.setFrom_lat(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LAT)));
                        localConvenienceBean.setFrom_lng(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LNG)));
                        localConvenienceBean.setTo_lat(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LAT)));
                        localConvenienceBean.setTo_lng(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LNG)));
                        localConvenienceBean.setStart_loc(cursordom.getString(cursordom.getColumnIndex(KEY_START_LOC)));
                        localConvenienceBean.setEnd_loc(cursordom.getString(cursordom.getColumnIndex(KEY_END_LOC)));
                        localConvenienceBean.setDistance(cursordom.getString(cursordom.getColumnIndex(KEY_DISTANCE)));
                        localConvenienceBean.setPhoto1(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO1)));
                        localConvenienceBean.setPhoto2(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO2)));


                        cursordom.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return localConvenienceBean;
    }


    @SuppressLint("Range")
    public LocalConvenienceBean getLocalConvinienceData() {

        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        Cursor cursordom;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT * FROM " + TABLE_LOCAL_CONVENIENCE;

            cursordom = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom.getCount());

            if (cursordom.getCount() > 0) {
                if (cursordom.moveToFirst()) {
                    while (!cursordom.isAfterLast()) {
                        localConvenienceBean = new LocalConvenienceBean();

                        localConvenienceBean.setPernr(cursordom.getString(cursordom.getColumnIndex(KEY_PERNR)));
                        localConvenienceBean.setBegda(cursordom.getString(cursordom.getColumnIndex(KEY_BEGDA)));
                        localConvenienceBean.setEndda(cursordom.getString(cursordom.getColumnIndex(KEY_ENDDA)));
                        localConvenienceBean.setFrom_time(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_TIME)));
                        localConvenienceBean.setTo_time(cursordom.getString(cursordom.getColumnIndex(KEY_TO_TIME)));
                        localConvenienceBean.setFrom_lat(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LAT)));
                        localConvenienceBean.setFrom_lng(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_LNG)));
                        localConvenienceBean.setTo_lat(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LAT)));
                        localConvenienceBean.setTo_lng(cursordom.getString(cursordom.getColumnIndex(KEY_TO_LNG)));
                        localConvenienceBean.setStart_loc(cursordom.getString(cursordom.getColumnIndex(KEY_START_LOC)));
                        localConvenienceBean.setEnd_loc(cursordom.getString(cursordom.getColumnIndex(KEY_END_LOC)));
                        localConvenienceBean.setDistance(cursordom.getString(cursordom.getColumnIndex(KEY_DISTANCE)));
                        localConvenienceBean.setPhoto1(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO1)));
                        localConvenienceBean.setPhoto2(cursordom.getString(cursordom.getColumnIndex(KEY_PHOTO2)));


                        cursordom.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return localConvenienceBean;
    }


    @SuppressLint("Range")
    public WayPoints getWayPointsData(String begda, String from_time) {

        WayPoints wayPoints = new WayPoints();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        Cursor cursordom;
        db.beginTransactionNonExclusive();
        try {


            selectQuery = "SELECT * FROM " + TABLE_WayPoints + " WHERE " + KEY_BEGDA + " = '" + begda + "'" + " AND " + KEY_FROM_TIME + " = '" + from_time + "'";


            cursordom = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom.getCount());

            if (cursordom.getCount() > 0) {
                if (cursordom.moveToFirst()) {
                    while (!cursordom.isAfterLast()) {
                        wayPoints = new WayPoints();
                        wayPoints.setPernr(cursordom.getString(cursordom.getColumnIndex(KEY_PERNR)));
                        wayPoints.setBegda(cursordom.getString(cursordom.getColumnIndex(KEY_BEGDA)));
                        wayPoints.setEndda(cursordom.getString(cursordom.getColumnIndex(KEY_ENDDA)));
                        wayPoints.setFrom_time(cursordom.getString(cursordom.getColumnIndex(KEY_FROM_TIME)));
                        wayPoints.setTo_time(cursordom.getString(cursordom.getColumnIndex(KEY_TO_TIME)));
                        wayPoints.setWayPoints(cursordom.getString(cursordom.getColumnIndex(KEY_WayPoints)));

                        cursordom.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return wayPoints;
    }

    @SuppressLint("Range")
    public ArrayList<LocalConvenienceBean> getLocalConveyance() {

        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean();
        ArrayList<LocalConvenienceBean> list_document = new ArrayList<>();
        list_document.clear();
        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransactionNonExclusive();
        try {

            String selectQuery = "SELECT  *  FROM " + TABLE_LOCAL_CONVENIENCE;


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("CURSORCOUNT", "&&&&" + cursor.getCount() + " " + selectQuery);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        localConvenienceBean = new LocalConvenienceBean();

                        localConvenienceBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        localConvenienceBean.setBegda(cursor.getString(cursor.getColumnIndex(KEY_BEGDA)));
                        localConvenienceBean.setEndda(cursor.getString(cursor.getColumnIndex(KEY_ENDDA)));
                        localConvenienceBean.setFrom_time(cursor.getString(cursor.getColumnIndex(KEY_FROM_TIME)));
                        localConvenienceBean.setTo_time(cursor.getString(cursor.getColumnIndex(KEY_TO_TIME)));
                        localConvenienceBean.setFrom_lat(cursor.getString(cursor.getColumnIndex(KEY_FROM_LAT)));
                        localConvenienceBean.setFrom_lng(cursor.getString(cursor.getColumnIndex(KEY_FROM_LNG)));
                        localConvenienceBean.setTo_lat(cursor.getString(cursor.getColumnIndex(KEY_TO_LAT)));
                        localConvenienceBean.setTo_lng(cursor.getString(cursor.getColumnIndex(KEY_TO_LNG)));
                        localConvenienceBean.setStart_loc(cursor.getString(cursor.getColumnIndex(KEY_START_LOC)));
                        localConvenienceBean.setEnd_loc(cursor.getString(cursor.getColumnIndex(KEY_END_LOC)));
                        localConvenienceBean.setDistance(cursor.getString(cursor.getColumnIndex(KEY_DISTANCE)));
                        localConvenienceBean.setPhoto1(cursor.getString(cursor.getColumnIndex(KEY_PHOTO1)));
                        localConvenienceBean.setPhoto2(cursor.getString(cursor.getColumnIndex(KEY_PHOTO2)));

                        list_document.add(localConvenienceBean);


                        cursor.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return list_document;
    }


    @SuppressLint("Range")
    public TravelEntryDomDocBean getDomTravelEntryInformation1(String enq_docno) {

        TravelEntryDomDocBean travelEntryDomDocBean = new TravelEntryDomDocBean();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT  *  FROM " + TABLE_TRAVEL_DOM_EXPENSES + " WHERE " + PERNR + "='" + enq_docno + "'";

            cursordom1 = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom1.getCount());

            if (cursordom1.getCount() > 0) {
                if (cursordom1.moveToFirst()) {
                    while (!cursordom1.isAfterLast()) {
                        travelEntryDomDocBean = new TravelEntryDomDocBean();

                        travelEntryDomDocBean.setPernr(cursordom1.getString(cursordom1.getColumnIndex(PERNR)));
                        travelEntryDomDocBean.setSerialno(cursordom1.getString(cursordom1.getColumnIndex(SERIALNO)));
                        travelEntryDomDocBean.setStart_date(cursordom1.getString(cursordom1.getColumnIndex(START_DATE)));
                        travelEntryDomDocBean.setEnd_date(cursordom1.getString(cursordom1.getColumnIndex(END_DATE)));
                        travelEntryDomDocBean.setCountry(cursordom1.getString(cursordom1.getColumnIndex(COUNTRY_NAME)));
                        travelEntryDomDocBean.setLocation(cursordom1.getString(cursordom1.getColumnIndex(LOCATION)));
                        travelEntryDomDocBean.setExpenses_type(cursordom1.getString(cursordom1.getColumnIndex(EXPENSES_TYPE)));
                        travelEntryDomDocBean.setAmount(cursordom1.getString(cursordom1.getColumnIndex(AMOUNT)));
                        travelEntryDomDocBean.setCurrency(cursordom1.getString(cursordom1.getColumnIndex(CURRENCY)));
                        travelEntryDomDocBean.setTax_code(cursordom1.getString(cursordom1.getColumnIndex(TAX_CODE)));
                        travelEntryDomDocBean.setFrom_date(cursordom1.getString(cursordom1.getColumnIndex(FROM_DATE)));
                        travelEntryDomDocBean.setTo_date(cursordom1.getString(cursordom1.getColumnIndex(TO_DATE)));
                        travelEntryDomDocBean.setRegion(cursordom1.getString(cursordom1.getColumnIndex(REGION)));
                        travelEntryDomDocBean.setDescription(cursordom1.getString(cursordom1.getColumnIndex(DESCRIPTION)));
                        travelEntryDomDocBean.setLocation1(cursordom1.getString(cursordom1.getColumnIndex(LOCATION1)));
                        travelEntryDomDocBean.setType(cursordom1.getString(cursordom1.getColumnIndex(TRAV_TYPE)));
                        travelEntryDomDocBean.setGstin_no(cursordom1.getString(cursordom1.getColumnIndex(GSTIN_NO)));

                        cursordom1.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return travelEntryDomDocBean;
    }

    @SuppressLint("Range")
    public List<TravelTripDomDocBean> getDomTravelEntryInformation3(String enq_docno, String reinr) {

        List<TravelTripDomDocBean> travelTripDomDocBeanArrayList = new ArrayList<TravelTripDomDocBean>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT  *  FROM " + TABLE_TRAVEL_DOM_EXPENSES1 + " WHERE " + PERNR + "='" + enq_docno + "'" + " AND " + REINR + " = '" + reinr + "'";

            cursordom3 = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursordom3.getCount());

            if (cursordom3.getCount() > 0) {
                if (cursordom3.moveToFirst()) {
                    while (!cursordom3.isAfterLast()) {
                        TravelTripDomDocBean travelTripDomDocBean = new TravelTripDomDocBean();

                        String vikas1 = cursordom3.getString(cursordom3.getColumnIndex(SERIALNO));
                        System.out.println("vikas1===>>>" + vikas1);

                        String vikas2 = cursordom3.getString(cursordom3.getColumnIndex(EXPENSES_TYPE));
                        System.out.println("vikas2===>>>" + vikas2);

                        travelTripDomDocBean.setPernr(cursordom3.getString(cursordom3.getColumnIndex(PERNR)));
                        travelTripDomDocBean.setSerialno(cursordom3.getString(cursordom3.getColumnIndex(SERIALNO)));
                        Log.e("SERIALNO.", "&&&&" + cursordom3.getString(cursordom3.getColumnIndex(SERIALNO)));
                        travelTripDomDocBean.setStart_date(cursordom3.getString(cursordom3.getColumnIndex(START_DATE)));
                        travelTripDomDocBean.setEnd_date(cursordom3.getString(cursordom3.getColumnIndex(END_DATE)));
                        travelTripDomDocBean.setCountry(cursordom3.getString(cursordom3.getColumnIndex(COUNTRY_NAME)));
                        travelTripDomDocBean.setLocation(cursordom3.getString(cursordom3.getColumnIndex(LOCATION)));
                        travelTripDomDocBean.setExpenses_type(cursordom3.getString(cursordom3.getColumnIndex(EXPENSES_TYPE)));
                        Log.e("EXPTYPE", "&&&&" + cursordom3.getString(cursordom3.getColumnIndex(EXPENSES_TYPE)));
                        travelTripDomDocBean.setAmount(cursordom3.getString(cursordom3.getColumnIndex(AMOUNT)));
                        Log.e("AMTTYPE", "&&&&" + cursordom3.getString(cursordom3.getColumnIndex(AMOUNT)));
                        travelTripDomDocBean.setCurrency(cursordom3.getString(cursordom3.getColumnIndex(CURRENCY)));
                        travelTripDomDocBean.setTax_code(cursordom3.getString(cursordom3.getColumnIndex(TAX_CODE)));
                        travelTripDomDocBean.setFrom_date(cursordom3.getString(cursordom3.getColumnIndex(FROM_DATE)));
                        travelTripDomDocBean.setTo_date(cursordom3.getString(cursordom3.getColumnIndex(TO_DATE)));
                        travelTripDomDocBean.setRegion(cursordom3.getString(cursordom3.getColumnIndex(REGION)));
                        travelTripDomDocBean.setDescription(cursordom3.getString(cursordom3.getColumnIndex(DESCRIPTION)));
                        travelTripDomDocBean.setLocation1(cursordom3.getString(cursordom3.getColumnIndex(LOCATION1)));
                        travelTripDomDocBean.setType(cursordom3.getString(cursordom3.getColumnIndex(TRAV_TYPE)));
                        travelTripDomDocBean.setReinr(cursordom3.getString(cursordom3.getColumnIndex(REINR)));
                        Log.e("REINR", "&&&&" + cursordom3.getString(cursordom3.getColumnIndex(REINR)));
                        travelTripDomDocBean.setGstin_no(cursordom3.getString(cursordom3.getColumnIndex(GSTIN_NO)));
                        travelTripDomDocBean.setRec_curr_txt(cursordom3.getString(cursordom3.getColumnIndex(CURR_TEXT)));
                        travelTripDomDocBean.setLand1_txt(cursordom3.getString(cursordom3.getColumnIndex(ZLAND_TEXT)));
                        travelTripDomDocBean.setExp_type_txt(cursordom3.getString(cursordom3.getColumnIndex(EXP_TEXT)));

                        travelTripDomDocBeanArrayList.add(travelTripDomDocBean);

                        cursordom3.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return travelTripDomDocBeanArrayList;
    }


    @SuppressLint("Range")
    public TravelEntryExpDocBean getExpTravelEntryInformation(String enq_docno, String serialno) {

        TravelEntryExpDocBean travelEntryDomDocBean = new TravelEntryExpDocBean();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT  *  FROM " + TABLE_TRAVEL_EXP_EXPENSES + " WHERE " + PERNR + "='" + enq_docno + "'" + " AND " + SERIALNO + " = '" + serialno + "'";

            cursorexp = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursorexp.getCount());

            if (cursorexp.getCount() > 0) {
                if (cursorexp.moveToFirst()) {
                    while (!cursorexp.isAfterLast()) {
                        travelEntryDomDocBean = new TravelEntryExpDocBean();

                        travelEntryDomDocBean.setPernr(cursorexp.getString(cursorexp.getColumnIndex(PERNR)));
                        travelEntryDomDocBean.setSerialno(cursorexp.getString(cursorexp.getColumnIndex(SERIALNO)));
                        travelEntryDomDocBean.setStart_date(cursorexp.getString(cursorexp.getColumnIndex(START_DATE)));
                        travelEntryDomDocBean.setEnd_date(cursorexp.getString(cursorexp.getColumnIndex(END_DATE)));
                        travelEntryDomDocBean.setCountry(cursorexp.getString(cursorexp.getColumnIndex(COUNTRY_NAME)));
                        travelEntryDomDocBean.setLocation(cursorexp.getString(cursorexp.getColumnIndex(LOCATION)));
                        travelEntryDomDocBean.setExpenses_type(cursorexp.getString(cursorexp.getColumnIndex(EXPENSES_TYPE)));
                        travelEntryDomDocBean.setAmount(cursorexp.getString(cursorexp.getColumnIndex(AMOUNT)));
                        travelEntryDomDocBean.setCurrency(cursorexp.getString(cursorexp.getColumnIndex(CURRENCY)));
                        travelEntryDomDocBean.setTax_code(cursorexp.getString(cursorexp.getColumnIndex(TAX_CODE)));
                        travelEntryDomDocBean.setFrom_date(cursorexp.getString(cursorexp.getColumnIndex(FROM_DATE)));
                        travelEntryDomDocBean.setTo_date(cursorexp.getString(cursorexp.getColumnIndex(TO_DATE)));
                        travelEntryDomDocBean.setRegion(cursorexp.getString(cursorexp.getColumnIndex(COUNTRY_NAME1)));
                        travelEntryDomDocBean.setDescription(cursorexp.getString(cursorexp.getColumnIndex(DESCRIPTION)));
                        travelEntryDomDocBean.setLocation1(cursorexp.getString(cursorexp.getColumnIndex(LOCATION1)));
                        travelEntryDomDocBean.setType(cursorexp.getString(cursorexp.getColumnIndex(TRAV_TYPE)));
                        travelEntryDomDocBean.setCardinfo(cursorexp.getString(cursorexp.getColumnIndex(CARDINFO)));

                        cursorexp.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return travelEntryDomDocBean;
    }


    @SuppressLint("Range")
    public TravelEntryExpDocBean getExpTravelEntryInformation1(String enq_docno) {

        TravelEntryExpDocBean travelEntryDomDocBean = new TravelEntryExpDocBean();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT  *  FROM " + TABLE_TRAVEL_EXP_EXPENSES + " WHERE " + PERNR + "='" + enq_docno + "'";

            cursorexp1 = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursorexp1.getCount());

            if (cursorexp1.getCount() > 0) {
                if (cursorexp1.moveToFirst()) {
                    while (!cursorexp1.isAfterLast()) {
                        travelEntryDomDocBean = new TravelEntryExpDocBean();

                        travelEntryDomDocBean.setPernr(cursorexp1.getString(cursorexp1.getColumnIndex(PERNR)));
                        travelEntryDomDocBean.setSerialno(cursorexp1.getString(cursorexp1.getColumnIndex(SERIALNO)));
                        travelEntryDomDocBean.setStart_date(cursorexp1.getString(cursorexp1.getColumnIndex(START_DATE)));
                        travelEntryDomDocBean.setEnd_date(cursorexp1.getString(cursorexp1.getColumnIndex(END_DATE)));
                        travelEntryDomDocBean.setCountry(cursorexp1.getString(cursorexp1.getColumnIndex(COUNTRY_NAME)));
                        travelEntryDomDocBean.setLocation(cursorexp1.getString(cursorexp1.getColumnIndex(LOCATION)));
                        travelEntryDomDocBean.setExpenses_type(cursorexp1.getString(cursorexp1.getColumnIndex(EXPENSES_TYPE)));
                        travelEntryDomDocBean.setAmount(cursorexp1.getString(cursorexp1.getColumnIndex(AMOUNT)));
                        travelEntryDomDocBean.setCurrency(cursorexp1.getString(cursorexp1.getColumnIndex(CURRENCY)));
                        travelEntryDomDocBean.setTax_code(cursorexp1.getString(cursorexp1.getColumnIndex(TAX_CODE)));
                        travelEntryDomDocBean.setFrom_date(cursorexp1.getString(cursorexp1.getColumnIndex(FROM_DATE)));
                        travelEntryDomDocBean.setTo_date(cursorexp1.getString(cursorexp1.getColumnIndex(TO_DATE)));
                        travelEntryDomDocBean.setRegion(cursorexp1.getString(cursorexp1.getColumnIndex(COUNTRY_NAME1)));
                        travelEntryDomDocBean.setDescription(cursorexp1.getString(cursorexp1.getColumnIndex(DESCRIPTION)));
                        travelEntryDomDocBean.setLocation1(cursorexp1.getString(cursorexp1.getColumnIndex(LOCATION1)));
                        travelEntryDomDocBean.setType(cursorexp1.getString(cursorexp1.getColumnIndex(TRAV_TYPE)));
                        travelEntryDomDocBean.setCardinfo(cursorexp1.getString(cursorexp1.getColumnIndex(CARDINFO)));

                        cursorexp1.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return travelEntryDomDocBean;
    }

    @SuppressLint("Range")
    public List<TravelTripExpDocBean> getExpTravelEntryInformation3(String enq_docno, String reinr) {

        List<TravelTripExpDocBean> travelTripExpDocBeanArrayList = new ArrayList<TravelTripExpDocBean>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = null;
        db.beginTransactionNonExclusive();
        try {

            selectQuery = "SELECT  *  FROM " + TABLE_TRAVEL_EXP_EXPENSES1 + " WHERE " + PERNR + "='" + enq_docno + "'" + " AND " + REINR + " = '" + reinr + "'";

            cursorexp3 = db.rawQuery(selectQuery, null);

            Log.e("COUNTSIZE", "%%%%%" + cursorexp3.getCount());

            if (cursorexp3.getCount() > 0) {
                if (cursorexp3.moveToFirst()) {
                    while (!cursorexp3.isAfterLast()) {
                        TravelTripExpDocBean travelTripExpDocBean = new TravelTripExpDocBean();

                        travelTripExpDocBean.setPernr(cursorexp3.getString(cursorexp3.getColumnIndex(PERNR)));
                        travelTripExpDocBean.setSerialno(cursorexp3.getString(cursorexp3.getColumnIndex(SERIALNO)));
                        travelTripExpDocBean.setStart_date(cursorexp3.getString(cursorexp3.getColumnIndex(START_DATE)));
                        travelTripExpDocBean.setEnd_date(cursorexp3.getString(cursorexp3.getColumnIndex(END_DATE)));
                        travelTripExpDocBean.setCountry(cursorexp3.getString(cursorexp3.getColumnIndex(COUNTRY_NAME)));
                        travelTripExpDocBean.setLocation(cursorexp3.getString(cursorexp3.getColumnIndex(LOCATION)));
                        travelTripExpDocBean.setExpenses_type(cursorexp3.getString(cursorexp3.getColumnIndex(EXPENSES_TYPE)));
                        travelTripExpDocBean.setAmount(cursorexp3.getString(cursorexp3.getColumnIndex(AMOUNT)));
                        travelTripExpDocBean.setCurrency(cursorexp3.getString(cursorexp3.getColumnIndex(CURRENCY)));
                        travelTripExpDocBean.setTax_code(cursorexp3.getString(cursorexp3.getColumnIndex(TAX_CODE)));
                        travelTripExpDocBean.setFrom_date(cursorexp3.getString(cursorexp3.getColumnIndex(FROM_DATE)));
                        travelTripExpDocBean.setTo_date(cursorexp3.getString(cursorexp3.getColumnIndex(TO_DATE)));
                        travelTripExpDocBean.setRegion(cursorexp3.getString(cursorexp3.getColumnIndex(COUNTRY_NAME1)));
                        travelTripExpDocBean.setDescription(cursorexp3.getString(cursorexp3.getColumnIndex(DESCRIPTION)));
                        travelTripExpDocBean.setLocation1(cursorexp3.getString(cursorexp3.getColumnIndex(LOCATION1)));
                        travelTripExpDocBean.setType(cursorexp3.getString(cursorexp3.getColumnIndex(TRAV_TYPE)));
                        travelTripExpDocBean.setReinr(cursorexp3.getString(cursorexp3.getColumnIndex(REINR)));
                        travelTripExpDocBean.setCardinfo(cursorexp3.getString(cursorexp3.getColumnIndex(CARDINFO)));
                        travelTripExpDocBean.setRec_curr_txt(cursorexp3.getString(cursorexp3.getColumnIndex(CURR_TEXT)));
                        travelTripExpDocBean.setLand1_txt(cursorexp3.getString(cursorexp3.getColumnIndex(ZLAND_TEXT)));
                        travelTripExpDocBean.setExp_type_txt(cursorexp3.getString(cursorexp3.getColumnIndex(EXP_TEXT)));
                        travelTripExpDocBeanArrayList.add(travelTripExpDocBean);

                        cursorexp3.moveToNext();

                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return travelTripExpDocBeanArrayList;
    }


    /**************************update mark atendnace ********************************************/

    @SuppressLint("Range")
    public boolean updateMarkAttendance(AttendanceBean attendanceBean, Context context) {

        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();
        try {

            userModel = new LoggedInUser(context);

            db = this.getWritableDatabase();

            // contentValues.put(TYPE, attendanceBean.TYPE);
            contentValues.put(PERNR, attendanceBean.PERNR);
            contentValues.put(BEGDA, attendanceBean.BEGDA);
            //  contentValues.put(SERVER_DATE_IN, attendanceBean.SERVER_DATE_IN);
            //  contentValues.put(SERVER_TIME_IN, attendanceBean.SERVER_TIME_IN);
            contentValues.put(SERVER_DATE_OUT, attendanceBean.SERVER_DATE_OUT);
            contentValues.put(SERVER_TIME_OUT, attendanceBean.SERVER_TIME_OUT);
            // contentValues.put(IN_ADDRESS, attendanceBean.IN_ADDRESS);
            contentValues.put(OUT_ADDRESS, attendanceBean.OUT_ADDRESS);
            // contentValues.put(IN_TIME, attendanceBean.IN_TIME);
            contentValues.put(OUT_TIME, attendanceBean.OUT_TIME);
            //  contentValues.put(WORKING_HOURS, attendanceBean.WORKING_HOURS);
            contentValues.put(IMAGE_DATA, attendanceBean.IMAGE_DATA);
            contentValues.put(CURRENT_MILLIS, attendanceBean.CURRENT_MILLIS);
            // contentValues.put(IN_LAT_LONG, attendanceBean.IN_LAT_LONG);
            contentValues.put(OUT_LAT_LONG, attendanceBean.OUT_LAT_LONG);
            // contentValues.put(IN_FILE_NAME, attendanceBean.IN_FILE_NAME);
            // contentValues.put(IN_FILE_LENGTH, attendanceBean.IN_FILE_LENGTH);
            // contentValues.put(IN_FILE_VALUE, attendanceBean.IN_FILE_VALUE);
            contentValues.put(OUT_FILE_NAME, attendanceBean.OUT_FILE_NAME);
            contentValues.put(OUT_FILE_LENGTH, attendanceBean.OUT_FILE_LENGTH);
            contentValues.put(OUT_FILE_VALUE, attendanceBean.OUT_FILE_VALUE);

            contentValues.put(OUT_IMAGE, attendanceBean.OUT_IMAGE);

            // contentValues.put(IN_STATUS, attendanceBean.IN_STATUS);
            contentValues.put(OUT_STATUS, attendanceBean.OUT_STATUS);

            //   long t = db.update(AttendanceDBFields.ATTENDANCE_TABLE_NAME, null, contentValues);
            //  long t = db.update(TABLE_MARK_ATTENDANCE, contentValues, BEGDA + "='" + attendanceBean.BEGDA+"'", null);

            Log.d("atnd_new_out", attendanceBean.SERVER_DATE_IN + "---" + attendanceBean.SERVER_TIME_IN + "---" +
                    attendanceBean.SERVER_DATE_OUT + "---" + attendanceBean.SERVER_TIME_OUT);

            long t = db.update(TABLE_MARK_ATTENDANCE, contentValues, BEGDA + "='" + attendanceBean.BEGDA + "'" + " AND " +
                    PERNR + "='" + userModel.uid + "'", null);
//                    PERNR + "='" + LoginBean.getUseid() +"'", null);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            db.close();
        }

        return true;
    }


    /**************************update Task Completed Synced ********************************************/
    @SuppressLint("Range")
    public boolean updatePendingTaskToComplete(String checker_id, String dno, String srno, String remark) {

        SQLiteDatabase db = null;
        ContentValues contentValues = new ContentValues();
        try {

            db = this.getWritableDatabase();


            contentValues.put(KEY_CHECKER, checker_id);
            contentValues.put(KEY_DNO, dno);
            contentValues.put(KEY_SRNO, srno);
            contentValues.put(KEY_REMARK, remark);

            long t = db.update(TABLE_TASK_PENDING, contentValues, KEY_CHECKER + "='' AND " + KEY_DNO + "='" + dno + "' AND " + KEY_SRNO + "='" + srno + "'", null);


        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {

            db.close();
        }

        return true;
    }

    @SuppressLint("Range")
    public Integer getPendinLeaveCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PENDING_LEAVE ;
//        String selectQuery =  "SELECT  * FROM " + TABLE_PENDING_LEAVE;

        Cursor d = db.rawQuery(selectQuery, null);

        Log.d("pending_leave_count", " " + d.getCount());

        return d.getCount();

    }


    @SuppressLint("Range")
    public Integer getPendinGatePassCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PENDING_GATEPASS ;


        Cursor d = db.rawQuery(selectQuery, null);

        Log.d("pending_leave_count", " " + d.getCount());

        return d.getCount();

    }

    @SuppressLint("Range")
    public Integer getPendingOdCount() {

        SQLiteDatabase db = this.getReadableDatabase();

//        String selectQuery =  "SELECT  * FROM " + TABLE_PENDING_OD;
        String selectQuery = "SELECT  * FROM " + TABLE_PENDING_OD;

        Cursor d = db.rawQuery(selectQuery, null);

        Log.d("pending_od_count", " " + d.getCount());

        return d.getCount();

    }


    @SuppressLint("Range")
    public Integer getPendinTaskCount() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TASK_PENDING ;

        Cursor d = db.rawQuery(selectQuery, null);

        Log.d("pending_task_count", " " + d.getCount());

        return d.getCount();

    }


    /**********************  insert Employee GPS Activity ************************************/
    public void insertEmployeeGPSActivity(
            String pernr,
            String budat,
            String time,
            String event,
            String latitude,
            String longitude,
            Context context,
            String phone_number
    ) {
        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        ContentValues values;

        try {


            values = new ContentValues();
            values.put(KEY_PERNR, pernr);
            values.put(KEY_BUDAT, budat);
            values.put(KEY_TIME_IN, time);
            values.put(KEY_EVENT, event);
            values.put(KEY_LATITUDE, latitude);
            values.put(KEY_LONGITUDE, longitude);
            values.put(KEY_PHONE_NUMBER, phone_number);

            // Insert Row
            long i = db.insert(TABLE_EMPLOYEE_GPS_ACTIVITY, null, values);

//               Toast.makeText(context,String.valueOf( "gps_ins"+ i) , Toast.LENGTH_SHORT).show();


            // Insert into database successfully.
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            // End the transaction.
            db.endTransaction();
            // Close database
            db.close();
        }
    }

    /*********************************** get Travel Head  *****************************/
    @SuppressLint("Range")
    public ArrayList<TravelHeadBean> getTravelHead(Context context) {


        ArrayList<TravelHeadBean> travelHeadBeanArrayList = new ArrayList<TravelHeadBean>();
        travelHeadBeanArrayList.clear();

        SQLiteDatabase db = this.getReadableDatabase();

        db.beginTransactionNonExclusive();
        try {


            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_TRAVEL_HEAD;

            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("SIZECURSOR", "****" + cursor.getCount());


            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        TravelHeadBean travelHeadBean = new TravelHeadBean();
                        travelHeadBean.setLink(cursor.getString(cursor.getColumnIndex(KEY_LINK)));
                        travelHeadBean.setAntrg_txt(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                        travelHeadBean.setDatv1_char(cursor.getString(cursor.getColumnIndex(KEY_START_DATE)));
                        travelHeadBean.setDatb1_char(cursor.getString(cursor.getColumnIndex(KEY_END_DATE)));
                        travelHeadBean.setTrip_total(cursor.getString(cursor.getColumnIndex(KEY_TRIP_TOTAL)));
                        travelHeadBean.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                        travelHeadBean.setReinr(cursor.getString(cursor.getColumnIndex(KEY_REINR)));
                        travelHeadBean.setZort1(cursor.getString(cursor.getColumnIndex(KEY_CITY)));
                        travelHeadBean.setZland_txt(cursor.getString(cursor.getColumnIndex(KEY_COUNTRY_TEXT)));
                        travelHeadBeanArrayList.add(travelHeadBean);
                        cursor.moveToNext();
                    }
                }
                db.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }

        return travelHeadBeanArrayList;
    }


    /*********************************** select employee gps activity  *****************************/

    @SuppressLint("Range")
    public ArrayList<EmployeeGPSActivityBean> getEmployeeGpsActivity(Context context) {
        LoginBean lb = new LoginBean();
        String userid = LoginBean.getUseid();
        ArrayList<EmployeeGPSActivityBean> list_employeeGPSActivity = new ArrayList<>();
        list_employeeGPSActivity.clear();


        SharedPreferences pref = context.getSharedPreferences("MyPref", 0);


        userid = pref.getString("key_username", "username");


        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = null;
        Cursor cursor = null;
        try {

            selectQuery = "SELECT * FROM " + TABLE_EMPLOYEE_GPS_ACTIVITY;
//                    +  " WHERE " + KEY_PERNR +  " = '" +userid+"'";


            cursor = db.rawQuery(selectQuery, null);

            // Log.d("gps_person_select",""+ userid);

            if (cursor.getCount() > 0) {

                //   Log.d("gps_count",""+ cursor.getCount());

                while (cursor.moveToNext()) {

                    EmployeeGPSActivityBean employeeGPSActivity = new EmployeeGPSActivityBean();

//                    Log.d("gps_count1",""+ cursor.getString(cursor.getColumnIndex(KEY_PERNR)) +
//                            cursor.getString(cursor.getColumnIndex(KEY_BUDAT)) +
//                            cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
//


                    employeeGPSActivity.setPernr(cursor.getString(cursor.getColumnIndex(KEY_PERNR)));
                    employeeGPSActivity.setBudat(cursor.getString(cursor.getColumnIndex(KEY_BUDAT)));
                    employeeGPSActivity.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME_IN)));
                    employeeGPSActivity.setEvent(cursor.getString(cursor.getColumnIndex(KEY_EVENT)));
                    employeeGPSActivity.setLatitude(cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)));
                    employeeGPSActivity.setLongitude(cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)));
                    employeeGPSActivity.setPhone_number(cursor.getString(cursor.getColumnIndex(KEY_PHONE_NUMBER)));


                    list_employeeGPSActivity.add(employeeGPSActivity);
                }

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }


            db.close();
        }
        return list_employeeGPSActivity;
    }

    @SuppressLint("Range")
    public List<VendorListModel.Response> getVendorName(String searchValue) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<VendorListModel.Response> taxcodeArrayList = new ArrayList<VendorListModel.Response>();
        if(CustomUtility.doesTableExist(db,TABLE_VENDORCODE)){
            String selectQuery = "SELECT  * FROM " + TABLE_VENDORCODE+ " WHERE " + KEY_VENDOR_NAME + " LIKE '%" + searchValue + "%'";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    VendorListModel.Response vendorListModel = new VendorListModel.Response();
                    vendorListModel.setLifnr(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_CODE)));
                    vendorListModel.setName1(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_NAME)));
                    vendorListModel.setAdd(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_ADDRESS)));
                    vendorListModel.setTelf1(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_CONTACT_NO)));
                    vendorListModel.setStras(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_STREET_ADDRESS)));
                    vendorListModel.setOrt01(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_REGION)));
                    vendorListModel.setOrt02(cursor.getString(cursor.getColumnIndex(KEY_VENDOR_CITY)));
                    taxcodeArrayList.add(vendorListModel);
                    cursor.moveToNext();
                }
            }
        }
        return taxcodeArrayList;
    }
}



