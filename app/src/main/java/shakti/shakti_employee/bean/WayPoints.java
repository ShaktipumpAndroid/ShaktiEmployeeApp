package shakti.shakti_employee.bean;

public class WayPoints {
    String  pernr = "", begda = "", endda = "", from_time = "", to_time = "", WayPoints;

    public WayPoints() {
    }

    public WayPoints(String pernr, String begda, String endda, String from_time, String to_time, String wayPoints) {
        this.pernr = pernr;
        this.begda = begda;
        this.endda = endda;
        this.from_time = from_time;
        this.to_time = to_time;
        WayPoints = wayPoints;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getBegda() {
        return begda;
    }

    public void setBegda(String begda) {
        this.begda = begda;
    }

    public String getEndda() {
        return endda;
    }

    public void setEndda(String endda) {
        this.endda = endda;
    }

    public String getFrom_time() {
        return from_time;
    }

    public void setFrom_time(String from_time) {
        this.from_time = from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public String getWayPoints() {
        return WayPoints;
    }

    public void setWayPoints(String wayPoints) {
        WayPoints = wayPoints;
    }
}
