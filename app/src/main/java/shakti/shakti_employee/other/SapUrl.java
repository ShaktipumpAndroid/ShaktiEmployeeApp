package shakti.shakti_employee.other;

import shakti.shakti_employee.BuildConfig;

/**
 * Created by shakti on 12/30/2016.
 */

public class SapUrl {
    ////
////    // SAP Development URL'S
/*    public static final String login_url = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/login.htm";
    public static final String APP_VERSION = "http://shaktidev.shaktipumps.com:8000/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zhr_emp_app_1/app_version.htm";
    public static final String active_employee = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/active_employee.htm";
    public static final String leave_create = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/leave_create.htm";
    public static final String leave_balance = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/leave_balances.htm";
    public static final String od_request = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/od_create.htm";
    public static final String gp_request = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/gp_create.htm";
    public static final String pending_leave = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/leave_approval_pending.htm";
    public static final String pending_Gatepass = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/gatepass_approval_pending.htm";
    public static final String approval_Gatepass = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/gatepass_approval.htm";
    public static final String approve_leave = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/leave_approve.htm";
    public static final String pending_od = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/od_approval_pending.htm";
    public static final String approve_od = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/od_approve.htm";
    public static final String attendance_report = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/attendance_report.htm";
    public static final String leave_report = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/leave_report.htm";
    public static final String od_report = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/od_report.htm";
    public static final String employee_info = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/employee_info.htm";
    public static final String employee_info_edit = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/employee_info_edit.htm";
    public static final String forgotpass_url = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/forgot_password.htm";
    public static final String payslip = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/employee_payslip.htm";
    public static final String SYNC_OFFLINE_DATA_TO_SAP = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/sync_offline_data.htm";
    public static final String SYNC_ANDROID_TO_SAP = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/sync_android_to_sap.htm";
    public static final String CREATE_TRAVEL_EXP = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/travel_expense_create.htm";
    public static final String EDIT_TRAVEL_EXP = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/travel_expense_change.htm";
    public static final String TRAVEL_EXP_VIEW = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/travel_expense_view.htm";
    public static final String WEB_VIEW = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_web/dashboard.htm";
    public static final String TRIP_COMPLETE_APPROVE = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/travel_expense_status_change.htm";
    public static final String TRIP_HOD_EXP_VIEW = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/travel_expense_hod_view.htm";
    public static final String LOCAL_CONVENIENVCE = "http://shaktidev.shaktipumps.com:8000/sap/bc/bsp/sap/zhr_emp_app_1/start_end_location.htm";*/
    //
////    //    Common Delcaration
    public static final String IMAGE_DIRECTORY_NAME = "Shakti App Image";

    public static final String BASEURL = BuildConfig.baseUrl;
    public static final String WEBBASEURL = BuildConfig.webbaseUrl;
    // SAP Production URL'S
    public static final String login_url = BASEURL + "login.htm";
    public static final String APP_VERSION = BASEURL + "app_version.htm";
    public static final String active_employee = BASEURL + "active_employee.htm";
    public static final String leave_create = BASEURL + "leave_create.htm";
    public static final String leave_balance = BASEURL + "leave_balances.htm";
    public static final String od_request = BASEURL + "od_create.htm";
    public static final String gp_request = BASEURL + "gp_create.htm";
    public static final String pending_leave = BASEURL + "leave_approval_pending.htm";
    public static final String pending_Gatepass = BASEURL + "gatepass_approval_pending.htm";
    public static final String approval_Gatepass = BASEURL + "gatepass_approval.htm";
    public static final String approve_leave = BASEURL + "leave_approve.htm";
    public static final String pending_od = BASEURL + "od_approval_pending.htm";
    public static final String approve_od = BASEURL + "od_approve.htm";
    public static final String attendance_report = BASEURL + "attendance_report.htm";
    public static final String leave_report = BASEURL + "leave_report.htm";
    public static final String od_report = BASEURL + "od_report.htm";
    public static final String employee_info = BASEURL + "employee_info.htm";
    public static final String employee_info_edit = BASEURL + "employee_info_edit.htm";
    public static final String forgotpass_url = BASEURL + "forgot_password.htm";
    public static final String payslip = BASEURL + "employee_payslip.htm";
    public static final String SYNC_OFFLINE_DATA_TO_SAP = BASEURL + "sync_offline_data.htm";
    public static final String SYNC_ANDROID_TO_SAP = BASEURL + "sync_android_to_sap.htm";
    public static final String CREATE_TRAVEL_EXP = BASEURL + "travel_expense_create.htm";
    public static final String EDIT_TRAVEL_EXP = BASEURL + "travel_expense_change.htm";
    public static final String TRAVEL_EXP_VIEW = BASEURL + "travel_expense_view.htm";
    public static final String WEB_VIEW = WEBBASEURL + "dashboard.htm";
    public static final String TRIP_COMPLETE_APPROVE = BASEURL + "travel_expense_status_change.htm";
    public static final String TRIP_HOD_EXP_VIEW = BASEURL + "travel_expense_hod_view.htm";
    public static final String LOCAL_CONVENIENVCE = BASEURL + "start_end_location.htm";

    public static final String DailyReportAPI = BASEURL + "mom_daily_reportinf.htm";

    public static final String VendorList = BASEURL + "vendor_details.htm";

    public static final String OpenGatePassList = BASEURL + "vendor_open_gatepass.htm";


}
