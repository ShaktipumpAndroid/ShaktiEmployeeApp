package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class Currency {
    String waser = null;
    String ltext = null;


    boolean selected = false;

    public Currency() {
    }

    public Currency(String waser, String ltext, boolean selected) {
        super();
        this.waser = waser;
        this.ltext = ltext;

        this.selected = selected;
    }

    public String getWaser() {
        return waser;
    }

    public void setWaser(String waser) {
        this.waser = waser;
    }

    public String getLtext() {
        return ltext;
    }

    public void setLtext(String ltext) {
        this.ltext = ltext;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

