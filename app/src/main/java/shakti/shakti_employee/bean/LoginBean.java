package shakti.shakti_employee.bean;

/**
 * Created by shakti on 10/7/2016.
 */

public class LoginBean {

    public static String userid;
    public static String username;
    public static String mob_atd;
    public static String trvel;
    public static String hd;

    public void setLogin(String id, String name, String mob_atnd, String travel, String hod) {
        userid = id;
        username = name;
        mob_atd = mob_atnd;
        trvel = travel;
        hd = hod;
    }


    public String getUsername() {
        return username;
    }

    public static String getUseid() {
        return userid;
    }

    public String getMob_atnd() {
        return mob_atd;
    }

    public String getTravel() {
        return trvel;
    }

    public String getHd() {
        return hd;
    }

}
