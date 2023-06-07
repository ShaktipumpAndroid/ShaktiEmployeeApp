package shakti.shakti_employee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppConfig {

    @SerializedName("minEmployeeAppVersion")
    @Expose
    private String minEmployeeAppVersion;
    @SerializedName("employeeAppUrl")
    @Expose
    private String employeeAppUrl;

    public String getMinEmployeeAppVersion() {
        return minEmployeeAppVersion;
    }

    public void setMinEmployeeAppVersion(String minEmployeeAppVersion) {
        this.minEmployeeAppVersion = minEmployeeAppVersion;
    }

    public String getEmployeeAppUrl() {
        return employeeAppUrl;
    }

    public void setEmployeeAppUrl(String employeeAppUrl) {
        this.employeeAppUrl = employeeAppUrl;
    }
}
