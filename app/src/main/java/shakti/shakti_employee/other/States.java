package shakti.shakti_employee.other;

/**
 * Created by shakti on 1/3/2017.
 */
public class States {
    String leave_no = null;
    String horo = null;
    String ename = null;
    String leave_from = null;
    String leave_to = null;
    String leave_type = null;
    String leave_reason = null;
    boolean selected = false;

    public States() {
    }

    public States(String leave_no, String name, String horo, String leave_type,
                  String leave_from, String leave_to, String leave_reason, boolean selected) {
        super();
        this.leave_no = leave_no;
        this.ename = name;
        this.horo = horo;
        this.leave_type = leave_type;
        this.leave_from = leave_from;
        this.leave_to = leave_to;
        this.leave_reason = leave_reason;
        this.selected = selected;
    }

    public String getLeaveNo() {
        return leave_no;
    }

    public void setCode(String leave_no) {
        this.leave_no = leave_no;
    }

    public String getHoro() {
        return horo;
    }

    public void setHoro(String horo) {
        this.horo = horo;
    }

    public String getName() {
        return ename;
    }

    public void setName(String name) {
        this.ename = name;
    }

    public String getLeave_type() {
        return leave_type;
    }

    public void setLeave_type(String leave_type) {
        this.leave_type = leave_type;
    }

    public String getLeave_from() {
        return leave_from;
    }

    public void setLeave_from(String date_from) {
        this.leave_from = date_from;
    }

    public String getLeave_to() {
        return leave_to;
    }

    public void setLeave_to(String date_to) {
        this.leave_to = date_to;
    }

    public String getLeave_reason() {
        return leave_reason;
    }

    public void setLeave_reason(String leave_reason) {
        this.leave_reason = leave_reason;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}

