package shakti.shakti_employee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripListResponse {

    @SerializedName("pernr")
    @Expose
    private String pernr;
    @SerializedName("reinr")
    @Expose
    private String reinr;
    @SerializedName("receiptno")
    @Expose
    private String receiptno;
    @SerializedName("exp_type")
    @Expose
    private String expType;
    @SerializedName("rec_amount")
    @Expose
    private String recAmount;
    @SerializedName("rec_curr")
    @Expose
    private String recCurr;
    @SerializedName("tax_code")
    @Expose
    private String taxCode;
    @SerializedName("rec_date")
    @Expose
    private String recDate;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("land1")
    @Expose
    private String land1;
    @SerializedName("from_date1")
    @Expose
    private String fromDate1;
    @SerializedName("to_date1")
    @Expose
    private String toDate1;
    @SerializedName("from_date")
    @Expose
    private String fromDate;
    @SerializedName("to_date")
    @Expose
    private String toDate;
    @SerializedName("descript")
    @Expose
    private String descript;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("p_doc")
    @Expose
    private String pDoc;

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

    public String getReceiptno() {
        return receiptno;
    }

    public void setReceiptno(String receiptno) {
        this.receiptno = receiptno;
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public String getRecAmount() {
        return recAmount;
    }

    public void setRecAmount(String recAmount) {
        this.recAmount = recAmount;
    }

    public String getRecCurr() {
        return recCurr;
    }

    public void setRecCurr(String recCurr) {
        this.recCurr = recCurr;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLand1() {
        return land1;
    }

    public void setLand1(String land1) {
        this.land1 = land1;
    }

    public String getFromDate1() {
        return fromDate1;
    }

    public void setFromDate1(String fromDate1) {
        this.fromDate1 = fromDate1;
    }

    public String getToDate1() {
        return toDate1;
    }

    public void setToDate1(String toDate1) {
        this.toDate1 = toDate1;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPDoc() {
        return pDoc;
    }

    public void setPDoc(String pDoc) {
        this.pDoc = pDoc;
    }
}
