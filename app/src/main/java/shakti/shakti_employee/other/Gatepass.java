package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class Gatepass {
    String gp_no = null;
    String pernr = null;
    String ename = null;
    String date = null;
    String time = null;
    String gp_type = null;
    String req_type = null;
    String direct_indirect = null;

    public Gatepass() {
    }

    public Gatepass(String gp_no, String pernr, String ename, String date,
                    String time, String gp_type, String req_type, String direct_indirect) {
        super();
        this.gp_no = gp_no;
        this.pernr = pernr;
        this.ename = ename;
        this.date = date;
        this.time = time;
        this.gp_type = gp_type;
        this.req_type = req_type;
        this.direct_indirect = direct_indirect;
    }

    public String getGp_no() {
        return gp_no;
    }

    public void setGp_no(String gp_no) {
        this.gp_no = gp_no;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGp_type() {
        return gp_type;
    }

    public void setGp_type(String gp_type) {
        this.gp_type = gp_type;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public String getDirect_indirect() {
        return direct_indirect;
    }

    public void setDirect_indirect(String direct_indirect) {
        this.direct_indirect = direct_indirect;
    }


}

