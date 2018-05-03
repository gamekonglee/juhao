package bc.juhao.com.cons;

/**
 * Created by gamekonglee on 2018/4/3.
 */

public class ContactsInfo {
    public int _id;
    public String name;
    public String phone;
    public String email;
    public ContactsInfo() {
        super();
    }

    public ContactsInfo(int _id, String name, String phone, String email) {
        super();
        this._id = _id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String toString() {
        return "PersonInfo [_id=" + _id + ", name=" + name + ", phone=" + phone
                + ", email=" + email + "]";
    }
}
