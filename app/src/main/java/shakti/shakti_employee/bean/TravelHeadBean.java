package shakti.shakti_employee.bean;

/**
 * Created by Administrator on 12/30/2016.
 */
public class TravelHeadBean {

    public String link = "";
    public String antrg_txt = "";
    public String datv1_char = "";
    public String datb1_char = "";
    public String trip_total = "";
    public String pernr = "";
    public String reinr = "";
    public String zort1 = "";
    public String zland_txt = "";


    public TravelHeadBean(String link,
                          String antrg_txt,
                          String datv1_char,
                          String datb1_char,
                          String trip_total,
                          String pernr,
                          String reinr,
                          String zort1,
                          String zland_txt) {
        this.link = link;
        this.antrg_txt = antrg_txt;
        this.datv1_char = datv1_char;
        this.datb1_char = datb1_char;
        this.trip_total = trip_total;
        this.pernr = pernr;
        this.reinr = reinr;
        this.zort1 = zort1;
        this.zland_txt = zland_txt;
    }

    public TravelHeadBean() {

    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAntrg_txt() {
        return antrg_txt;
    }

    public void setAntrg_txt(String antrg_txt) {
        this.antrg_txt = antrg_txt;
    }

    public String getDatv1_char() {
        return datv1_char;
    }

    public void setDatv1_char(String datv1_char) {
        this.datv1_char = datv1_char;
    }

    public String getDatb1_char() {
        return datb1_char;
    }

    public void setDatb1_char(String datb1_char) {
        this.datb1_char = datb1_char;
    }

    public String getTrip_total() {
        return trip_total;
    }

    public void setTrip_total(String trip_total) {
        this.trip_total = trip_total;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getReinr() {
        return reinr;
    }

    public void setReinr(String reinr) {
        this.reinr = reinr;
    }

    public String getZort1() {
        return zort1;
    }

    public void setZort1(String zort1) {
        this.zort1 = zort1;
    }

    public String getZland_txt() {
        return zland_txt;
    }

    public void setZland_txt(String zland_txt) {
        this.zland_txt = zland_txt;
    }
}




