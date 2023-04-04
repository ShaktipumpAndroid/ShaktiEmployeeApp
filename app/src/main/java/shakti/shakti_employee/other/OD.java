package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class OD {

    String od_no = null;
    String horo = null;
    String ename = null;
    String od_from = null;
    String od_to = null;
    String work_status = null;
    String visit_place = null;
    String purpose1 = null;
    String remark = null;
    boolean selected = false;


    public OD() {
    }


    public OD(String od_no, String horo, String ename, String work_status,
              String od_from, String od_to, String visit_place, String purpose1, boolean selected) {
        super();
        this.od_no = od_no;
        this.horo = horo;
        this.ename = ename;
        this.work_status = work_status;
        this.od_from = od_from;
        this.od_to = od_to;
        this.visit_place = visit_place;
        this.purpose1 = purpose1;
        this.remark = remark;
    }


    public String getOd_no() {
        return od_no;
    }

    public void setOd_no(String od_no) {
        this.od_no = od_no;
    }

    public String getHoro() {
        return horo;
    }

    public void setHoro(String horo) {
        this.horo = horo;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getOd_from() {
        return od_from;
    }

    public void setOd_from(String od_from) {
        this.od_from = od_from;
    }

    public String getWork_status() {
        return work_status;
    }

    public void setWork_status(String work_status) {
        this.work_status = work_status;
    }

    public String getOd_to() {
        return od_to;
    }

    public void setOd_to(String od_to) {
        this.od_to = od_to;
    }

    public String getVisit_place() {
        return visit_place;
    }

    public void setVisit_place(String visit_place) {
        this.visit_place = visit_place;
    }

    public String isPurpose1() {
        return purpose1;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.purpose1 = remark;
    }

    public void setPurpose1(String purpose1) {
        this.purpose1 = purpose1;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}