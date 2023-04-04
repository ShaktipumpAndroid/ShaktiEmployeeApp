package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class Taxcode {
    String mandt = null;
    String taxcode = null;
    String text = null;


    boolean selected = false;

    public Taxcode() {
    }

    public Taxcode(String mandt, String taxcode, String text, boolean selected) {
        super();
        this.mandt = mandt;
        this.taxcode = taxcode;
        this.text = text;

        this.selected = selected;
    }

    public String getMandt() {
        return mandt;
    }

    public void setMandt(String mandt) {
        this.mandt = mandt;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

