package bc.juhao.com.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Jun
 * @date : 2017/3/13 10:10
 * @description :
 */
public class Programme implements Serializable {
    private String attr_name;
    private List<String> attrVal;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public List<String> getAttrVal() {
        return attrVal;
    }

    public void setAttrVal(List<String> attrVal) {
        this.attrVal = attrVal;
    }


}
