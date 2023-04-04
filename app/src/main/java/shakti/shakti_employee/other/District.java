package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class District {
    String land1 = null;
    String regio = null;
    String cityc = null;
    String bezei = null;

    boolean selected = false;

    public District() {
    }

    public District(String land1, String regio, String cityc, String bezei, boolean selected) {
        super();
        this.land1 = land1;
        this.regio = regio;
        this.cityc = cityc;
        this.bezei = bezei;

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

    public String getCityc() {
        return cityc;
    }

    public void setCityc(String cityc) {
        this.cityc = cityc;
    }

    public String getBezei() {
        return bezei;
    }

    public void setBezei(String bezei) {
        this.bezei = bezei;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

