package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VendorListModel {

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

        @SerializedName("lifnr")
        @Expose
        private String lifnr;
        @SerializedName("name1")
        @Expose
        private String name1;
        @SerializedName("stras")
        @Expose
        private String stras;
        @SerializedName("ort01")
        @Expose
        private String ort01;
        @SerializedName("ort02")
        @Expose
        private String ort02;
        @SerializedName("telf1")
        @Expose
        private String telf1;
        @SerializedName("add")
        @Expose
        private String add;

        public String getLifnr() {
            return lifnr;
        }

        public void setLifnr(String lifnr) {
            this.lifnr = lifnr;
        }

        public String getName1() {
            return name1;
        }

        public void setName1(String name1) {
            this.name1 = name1;
        }

        public String getStras() {
            return stras;
        }

        public void setStras(String stras) {
            this.stras = stras;
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

        public String getTelf1() {
            return telf1;
        }

        public void setTelf1(String telf1) {
            this.telf1 = telf1;
        }

        public String getAdd() {
            return add;
        }

        public void setAdd(String add) {
            this.add = add;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "lifnr='" + lifnr + '\'' +
                    ", name1='" + name1 + '\'' +
                    ", stras='" + stras + '\'' +
                    ", ort01='" + ort01 + '\'' +
                    ", ort02='" + ort02 + '\'' +
                    ", telf1='" + telf1 + '\'' +
                    ", add='" + add + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "VendorListModel{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }
}
