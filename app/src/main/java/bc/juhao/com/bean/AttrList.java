package bc.juhao.com.bean;

import java.io.Serializable;

/**
 * @author: Jun
 * @date : 2017/3/8 11:57
 * @description :
 */
public class AttrList implements Serializable {
    private String attr_value;
    private int id;

    public String getAttr_value() {
        return attr_value;
    }

    public void setAttr_value(String attr_value) {
        this.attr_value = attr_value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AttrList{" +
                "attr_value='" + attr_value + '\'' +
                ", id=" + id +
                '}';
    }
}
