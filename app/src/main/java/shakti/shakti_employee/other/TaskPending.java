package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class TaskPending {

    String pernr = "";
    String currentDate = "";
    String currentTime = "";
    String description = "";
    String task_assign_to = "";
    String fromDateEtxt = "";
    String toDateEtxt = "";
    String sync = null;
    String mrc_type = "";
    String department = "";
    String checker = "";
    String srno = "";
    String dno = "";
    String remark = "";


    public TaskPending() {
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTask_assign_to() {
        return task_assign_to;
    }

    public void setTask_assign_to(String task_assign_to) {
        this.task_assign_to = task_assign_to;
    }

    public String getFromDateEtxt() {
        return fromDateEtxt;
    }

    public void setFromDateEtxt(String fromDateEtxt) {
        this.fromDateEtxt = fromDateEtxt;
    }

    public String getToDateEtxt() {
        return toDateEtxt;
    }

    public void setToDateEtxt(String toDateEtxt) {
        this.toDateEtxt = toDateEtxt;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getMrc_type() {
        return mrc_type;
    }

    public void setMrc_type(String mrc_type) {
        this.mrc_type = mrc_type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getDno() {
        return dno;
    }

    public void setDno(String dno) {
        this.dno = dno;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}