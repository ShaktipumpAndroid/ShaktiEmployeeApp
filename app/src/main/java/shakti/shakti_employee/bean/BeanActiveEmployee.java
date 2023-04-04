package shakti.shakti_employee.bean;

/**
 * Created by Administrator on 10/17/2016.
 */
public class BeanActiveEmployee {

    String pernr, ename, btext;

    public BeanActiveEmployee() {

    }

    public BeanActiveEmployee(String pernr, String ename, String btext) {
        this.pernr = pernr;
        this.ename = ename;
        this.btext = btext;


    }


    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }


    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }


    public String getBtext() {
        return btext;
    }

    public void setBtext(String btext) {
        this.btext = btext;
    }

}
