package shakti.shakti_employee.bean;

/**
 * Created by shahid on 30-06-2016.
 */
public class LocalConvenienceBean1 {




    private String pernr = "";
    private String begda = "";
    private String endda = "";
    private String from_time = "";
    private String to_time = "";
    private String from_lat = "";
    private String to_lat = "";
    private String from_lng = "";
    private String to_lng = "";
    private String start_loc = "";
    private String end_loc = "";
    private String distance = "";



    public LocalConvenienceBean1() {


    }


    public LocalConvenienceBean1(String pernr, String begda, String endda, String from_time, String to_time, String from_lat, String to_lat, String from_lng, String to_lng, String start_loc, String end_loc, String distance) {

        this.pernr = pernr;
        this.begda = begda;
        this.endda = endda;
        this.from_time = from_time;
        this.to_time = to_time;
        this.from_lat = from_lat;
        this.to_lat = to_lat;
        this.from_lng = from_lng;
        this.to_lng = to_lng;
        this.start_loc = start_loc;
        this.end_loc = end_loc;
        this.distance = distance;

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

    public String getFrom_lat() {
        return from_lat;
    }

    public void setFrom_lat(String from_lat) {
        this.from_lat = from_lat;
    }

    public String getTo_lat() {
        return to_lat;
    }

    public void setTo_lat(String to_lat) {
        this.to_lat = to_lat;
    }

    public String getFrom_lng() {
        return from_lng;
    }

    public void setFrom_lng(String from_lng) {
        this.from_lng = from_lng;
    }

    public String getTo_lng() {
        return to_lng;
    }

    public void setTo_lng(String to_lng) {
        this.to_lng = to_lng;
    }

    public String getStart_loc() {
        return start_loc;
    }

    public void setStart_loc(String start_loc) {
        this.start_loc = start_loc;
    }

    public String getEnd_loc() {
        return end_loc;
    }

    public void setEnd_loc(String end_loc) {
        this.end_loc = end_loc;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
