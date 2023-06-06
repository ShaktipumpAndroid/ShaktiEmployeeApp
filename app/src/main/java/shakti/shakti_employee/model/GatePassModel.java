package shakti.shakti_employee.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GatePassModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private List<Response> response;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }

    public static class Response {

        @SerializedName("mandt")
        @Expose
        private String mandt;
        @SerializedName("docno")
        @Expose
        private String docno;
        @SerializedName("bukrs")
        @Expose
        private String bukrs;
        @SerializedName("vstpye")
        @Expose
        private String vstpye;
        @SerializedName("pass_date")
        @Expose
        private String passDate;
        @SerializedName("pass_time")
        @Expose
        private String passTime;
        @SerializedName("leave_date")
        @Expose
        private String leaveDate;
        @SerializedName("leave_time")
        @Expose
        private String leaveTime;
        @SerializedName("name_visitor")
        @Expose
        private String nameVisitor;
        @SerializedName("tel_number")
        @Expose
        private String telNumber;
        @SerializedName("company")
        @Expose
        private String company;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("purpose")
        @Expose
        private String purpose;
        @SerializedName("purpose_list")
        @Expose
        private String purposeList;
        @SerializedName("contact_person")
        @Expose
        private String contactPerson;
        @SerializedName("vehicle_no")
        @Expose
        private String vehicleNo;
        @SerializedName("vend_typ")
        @Expose
        private String vendTyp;
        @SerializedName("lifnr")
        @Expose
        private String lifnr;
        @SerializedName("land1")
        @Expose
        private String land1;
        @SerializedName("name1")
        @Expose
        private String name1;
        @SerializedName("ort01")
        @Expose
        private String ort01;
        @SerializedName("ort02")
        @Expose
        private String ort02;
        @SerializedName("regio")
        @Expose
        private String regio;
        @SerializedName("stras")
        @Expose
        private String stras;
        @SerializedName("telf2")
        @Expose
        private String telf2;
        @SerializedName("matkl")
        @Expose
        private String matkl;
        @SerializedName("mat_with_vis")
        @Expose
        private String matWithVis;
        @SerializedName("license_no")
        @Expose
        private String licenseNo;
        @SerializedName("pollutn_cert")
        @Expose
        private String pollutnCert;
        @SerializedName("three_trilas")
        @Expose
        private String threeTrilas;
        @SerializedName("fitns_cert")
        @Expose
        private String fitnsCert;
        @SerializedName("permit_expiry")
        @Expose
        private String permitExpiry;
        @SerializedName("vac_occ")
        @Expose
        private String vacOcc;
        @SerializedName("vac_occ_out")
        @Expose
        private String vacOccOut;
        @SerializedName("canteen_card_no")
        @Expose
        private String canteenCardNo;
        @SerializedName("pernr")
        @Expose
        private String pernr;
        @SerializedName("vehicle_type")
        @Expose
        private String vehicleType;
        @SerializedName("bus_driver_name")
        @Expose
        private String busDriverName;
        @SerializedName("bus_helper_name")
        @Expose
        private String busHelperName;
        @SerializedName("bus_driver_id")
        @Expose
        private String busDriverId;
        @SerializedName("bus_helper_id")
        @Expose
        private String busHelperId;
        @SerializedName("no_of_passenger")
        @Expose
        private String noOfPassenger;
        @SerializedName("shift")
        @Expose
        private String shift;
        @SerializedName("prev_day_ind")
        @Expose
        private String prevDayInd;
        @SerializedName("location_frm")
        @Expose
        private String locationFrm;
        @SerializedName("location_to")
        @Expose
        private String locationTo;
        @SerializedName("km")
        @Expose
        private String km;
        @SerializedName("pernr1")
        @Expose
        private String pernr1;
        @SerializedName("pernr2")
        @Expose
        private String pernr2;
        @SerializedName("pernr3")
        @Expose
        private String pernr3;
        @SerializedName("pernr4")
        @Expose
        private String pernr4;
        @SerializedName("pernr5")
        @Expose
        private String pernr5;
        @SerializedName("pernr6")
        @Expose
        private String pernr6;
        @SerializedName("pernr7")
        @Expose
        private String pernr7;
        @SerializedName("abkrs")
        @Expose
        private String abkrs;
        @SerializedName("entry_by")
        @Expose
        private String entryBy;
        @SerializedName("out_remark")
        @Expose
        private String outRemark;
        @SerializedName("place_to_visit")
        @Expose
        private String placeToVisit;
        @SerializedName("werks")
        @Expose
        private String werks;

        boolean isChecked;


        public String getMandt() {
            return mandt;
        }

        public void setMandt(String mandt) {
            this.mandt = mandt;
        }

        public String getDocno() {
            return docno;
        }

        public void setDocno(String docno) {
            this.docno = docno;
        }

        public String getBukrs() {
            return bukrs;
        }

        public void setBukrs(String bukrs) {
            this.bukrs = bukrs;
        }

        public String getVstpye() {
            return vstpye;
        }

        public void setVstpye(String vstpye) {
            this.vstpye = vstpye;
        }

        public String getPassDate() {
            return passDate;
        }

        public void setPassDate(String passDate) {
            this.passDate = passDate;
        }

        public String getPassTime() {
            return passTime;
        }

        public void setPassTime(String passTime) {
            this.passTime = passTime;
        }

        public String getLeaveDate() {
            return leaveDate;
        }

        public void setLeaveDate(String leaveDate) {
            this.leaveDate = leaveDate;
        }

        public String getLeaveTime() {
            return leaveTime;
        }

        public void setLeaveTime(String leaveTime) {
            this.leaveTime = leaveTime;
        }

        public String getNameVisitor() {
            return nameVisitor;
        }

        public void setNameVisitor(String nameVisitor) {
            this.nameVisitor = nameVisitor;
        }

        public String getTelNumber() {
            return telNumber;
        }

        public void setTelNumber(String telNumber) {
            this.telNumber = telNumber;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPurpose() {
            return purpose;
        }

        public void setPurpose(String purpose) {
            this.purpose = purpose;
        }

        public String getPurposeList() {
            return purposeList;
        }

        public void setPurposeList(String purposeList) {
            this.purposeList = purposeList;
        }

        public String getContactPerson() {
            return contactPerson;
        }

        public void setContactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
        }

        public String getVehicleNo() {
            return vehicleNo;
        }

        public void setVehicleNo(String vehicleNo) {
            this.vehicleNo = vehicleNo;
        }

        public String getVendTyp() {
            return vendTyp;
        }

        public void setVendTyp(String vendTyp) {
            this.vendTyp = vendTyp;
        }

        public String getLifnr() {
            return lifnr;
        }

        public void setLifnr(String lifnr) {
            this.lifnr = lifnr;
        }

        public String getLand1() {
            return land1;
        }

        public void setLand1(String land1) {
            this.land1 = land1;
        }

        public String getName1() {
            return name1;
        }

        public void setName1(String name1) {
            this.name1 = name1;
        }

        public String getOrt01() {
            return ort01;
        }

        public void setOrt01(String ort01) {
            this.ort01 = ort01;
        }

        public String getOrt02() {
            return ort02;
        }

        public void setOrt02(String ort02) {
            this.ort02 = ort02;
        }

        public String getRegio() {
            return regio;
        }

        public void setRegio(String regio) {
            this.regio = regio;
        }

        public String getStras() {
            return stras;
        }

        public void setStras(String stras) {
            this.stras = stras;
        }

        public String getTelf2() {
            return telf2;
        }

        public void setTelf2(String telf2) {
            this.telf2 = telf2;
        }

        public String getMatkl() {
            return matkl;
        }

        public void setMatkl(String matkl) {
            this.matkl = matkl;
        }

        public String getMatWithVis() {
            return matWithVis;
        }

        public void setMatWithVis(String matWithVis) {
            this.matWithVis = matWithVis;
        }

        public String getLicenseNo() {
            return licenseNo;
        }

        public void setLicenseNo(String licenseNo) {
            this.licenseNo = licenseNo;
        }

        public String getPollutnCert() {
            return pollutnCert;
        }

        public void setPollutnCert(String pollutnCert) {
            this.pollutnCert = pollutnCert;
        }

        public String getThreeTrilas() {
            return threeTrilas;
        }

        public void setThreeTrilas(String threeTrilas) {
            this.threeTrilas = threeTrilas;
        }

        public String getFitnsCert() {
            return fitnsCert;
        }

        public void setFitnsCert(String fitnsCert) {
            this.fitnsCert = fitnsCert;
        }

        public String getPermitExpiry() {
            return permitExpiry;
        }

        public void setPermitExpiry(String permitExpiry) {
            this.permitExpiry = permitExpiry;
        }

        public String getVacOcc() {
            return vacOcc;
        }

        public void setVacOcc(String vacOcc) {
            this.vacOcc = vacOcc;
        }

        public String getVacOccOut() {
            return vacOccOut;
        }

        public void setVacOccOut(String vacOccOut) {
            this.vacOccOut = vacOccOut;
        }

        public String getCanteenCardNo() {
            return canteenCardNo;
        }

        public void setCanteenCardNo(String canteenCardNo) {
            this.canteenCardNo = canteenCardNo;
        }

        public String getPernr() {
            return pernr;
        }

        public void setPernr(String pernr) {
            this.pernr = pernr;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getBusDriverName() {
            return busDriverName;
        }

        public void setBusDriverName(String busDriverName) {
            this.busDriverName = busDriverName;
        }

        public String getBusHelperName() {
            return busHelperName;
        }

        public void setBusHelperName(String busHelperName) {
            this.busHelperName = busHelperName;
        }

        public String getBusDriverId() {
            return busDriverId;
        }

        public void setBusDriverId(String busDriverId) {
            this.busDriverId = busDriverId;
        }

        public String getBusHelperId() {
            return busHelperId;
        }

        public void setBusHelperId(String busHelperId) {
            this.busHelperId = busHelperId;
        }

        public String getNoOfPassenger() {
            return noOfPassenger;
        }

        public void setNoOfPassenger(String noOfPassenger) {
            this.noOfPassenger = noOfPassenger;
        }

        public String getShift() {
            return shift;
        }

        public void setShift(String shift) {
            this.shift = shift;
        }

        public String getPrevDayInd() {
            return prevDayInd;
        }

        public void setPrevDayInd(String prevDayInd) {
            this.prevDayInd = prevDayInd;
        }

        public String getLocationFrm() {
            return locationFrm;
        }

        public void setLocationFrm(String locationFrm) {
            this.locationFrm = locationFrm;
        }

        public String getLocationTo() {
            return locationTo;
        }

        public void setLocationTo(String locationTo) {
            this.locationTo = locationTo;
        }

        public String getKm() {
            return km;
        }

        public void setKm(String km) {
            this.km = km;
        }

        public String getPernr1() {
            return pernr1;
        }

        public void setPernr1(String pernr1) {
            this.pernr1 = pernr1;
        }

        public String getPernr2() {
            return pernr2;
        }

        public void setPernr2(String pernr2) {
            this.pernr2 = pernr2;
        }

        public String getPernr3() {
            return pernr3;
        }

        public void setPernr3(String pernr3) {
            this.pernr3 = pernr3;
        }

        public String getPernr4() {
            return pernr4;
        }

        public void setPernr4(String pernr4) {
            this.pernr4 = pernr4;
        }

        public String getPernr5() {
            return pernr5;
        }

        public void setPernr5(String pernr5) {
            this.pernr5 = pernr5;
        }

        public String getPernr6() {
            return pernr6;
        }

        public void setPernr6(String pernr6) {
            this.pernr6 = pernr6;
        }

        public String getPernr7() {
            return pernr7;
        }

        public void setPernr7(String pernr7) {
            this.pernr7 = pernr7;
        }

        public String getAbkrs() {
            return abkrs;
        }

        public void setAbkrs(String abkrs) {
            this.abkrs = abkrs;
        }

        public String getEntryBy() {
            return entryBy;
        }

        public void setEntryBy(String entryBy) {
            this.entryBy = entryBy;
        }

        public String getOutRemark() {
            return outRemark;
        }

        public void setOutRemark(String outRemark) {
            this.outRemark = outRemark;
        }

        public String getPlaceToVisit() {
            return placeToVisit;
        }

        public void setPlaceToVisit(String placeToVisit) {
            this.placeToVisit = placeToVisit;
        }

        public String getWerks() {
            return werks;
        }

        public void setWerks(String werks) {
            this.werks = werks;
        }


        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

    }
}
