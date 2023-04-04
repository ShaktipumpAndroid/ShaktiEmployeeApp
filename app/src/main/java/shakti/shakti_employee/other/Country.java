package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class Country {
    String land1 = null;
    String landx = null;

    boolean selected = false;

    public Country() {
    }

    public Country(String land1, String landx, boolean selected) {
        super();
        this.land1 = land1;
        this.landx = landx;

        this.selected = selected;
    }

    public String getLand1() {
        return land1;
    }

    public void setLand1(String land1) {
        this.land1 = land1;
    }

    public String getLandx() {
        return landx;
    }

    public void setLandx(String landx) {
        this.landx = landx;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

