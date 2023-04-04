package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class Region {
    String land1 = null;
    String bland = null;
    String regio = null;
    String bezei = null;

    boolean selected = false;

    public Region() {
    }

    public Region(String land1, String bland, String regio, String bezei, boolean selected) {
        super();
        this.land1 = land1;
        this.bland = bland;
        this.regio = regio;
        this.bezei = bezei;

        this.selected = selected;
    }

    public String getLand1() {
        return land1;
    }

    public void setLand1(String land1) {
        this.land1 = land1;
    }

    public String getBland() {
        return bland;
    }

    public void setBland(String bland) {
        this.bland = bland;
    }

    public String getRegio() {
        return regio;
    }

    public void setRegio(String regio) {
        this.regio = regio;
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

