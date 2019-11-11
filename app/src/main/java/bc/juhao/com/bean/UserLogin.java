package bc.juhao.com.bean;

/**
 * Created by gamekonglee on 2018/7/9.
 */

public class UserLogin  {
    String name;
    String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UserLogin(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
