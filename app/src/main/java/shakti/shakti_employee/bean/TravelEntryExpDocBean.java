package shakti.shakti_employee.bean;

/**
 * Created by shakti on 10/7/2016.
 */

public class TravelEntryExpDocBean {

    public String pernr = "";
    public String serialno = "";
    public String start_date = "";
    public String end_date = "";
    public String country = "";
    public String location = "";
    public String expenses_type = "";
    public String amount = "";
    public String currency = "";
    public String tax_code = "";
    public String from_date = "";
    public String to_date = "";
    public String region = "";
    public String description = "";
    public String location1 = "";
    public String cardinfo = "";
    public String type = "";


    public TravelEntryExpDocBean(String pernr, String serialno, String start_date, String end_date, String country, String location, String expenses_type, String amount, String currency, String tax_code, String from_date, String to_date, String region, String description, String location1, String cardinfo, String type) {
        this.pernr = pernr;
        this.serialno = serialno;
        this.start_date = start_date;
        this.end_date = end_date;
        this.country = country;
        this.location = location;
        this.expenses_type = expenses_type;
        this.amount = amount;
        this.currency = currency;
        this.tax_code = tax_code;
        this.from_date = from_date;
        this.to_date = to_date;
        this.region = region;
        this.description = description;
        this.location1 = location1;
        this.cardinfo = cardinfo;
        this.type = type;
    }

    public TravelEntryExpDocBean() {


    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExpenses_type() {
        return expenses_type;
    }

    public void setExpenses_type(String expenses_type) {
        this.expenses_type = expenses_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTax_code() {
        return tax_code;
    }

    public void setTax_code(String tax_code) {
        this.tax_code = tax_code;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation1() {
        return location1;
    }

    public void setLocation1(String location1) {
        this.location1 = location1;
    }

    public String getCardinfo() {
        return cardinfo;
    }

    public void setCardinfo(String cardinfo) {
        this.cardinfo = cardinfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
