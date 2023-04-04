package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class Expenses {
    String spkzl = null;
    String sptxt = null;


    boolean selected = false;

    public Expenses() {
    }

    public Expenses(String spkzl, String sptxt, boolean selected) {
        super();
        this.spkzl = spkzl;
        this.sptxt = sptxt;

        this.selected = selected;
    }

    public String getSpkzl() {
        return spkzl;
    }

    public void setSpkzl(String spkzl) {
        this.spkzl = spkzl;
    }

    public String getSptxt() {
        return sptxt;
    }

    public void setSptxt(String sptxt) {
        this.sptxt = sptxt;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

