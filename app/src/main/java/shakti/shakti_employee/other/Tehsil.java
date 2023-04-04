package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class Tehsil {
    String land1 = null;
    String regio = null;
    String district = null;
    String tehsil = null;
    String tehsil_txt = null;

    boolean selected = false;

    public Tehsil() {
    }

    public Tehsil(String land1, String regio, String district, String tehsil, String tehsil_txt, boolean selected) {
        super();
        this.land1 = land1;
        this.regio = regio;
        this.district = district;
        this.tehsil = tehsil;
        this.tehsil_txt = tehsil_txt;

        this.selected = selected;
    }

    public String getLand1() {
        return land1;
    }

    public void setLand1(String land1) {
        this.land1 = land1;
    }

    public String getRegio() {
        return regio;
    }

    public void setRegio(String regio) {
        this.regio = regio;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getTehsil_txt() {
        return tehsil_txt;
    }

    public void setTehsil_txt(String tehsil_txt) {
        this.tehsil_txt = tehsil_txt;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

