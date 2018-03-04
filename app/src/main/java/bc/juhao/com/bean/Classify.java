package bc.juhao.com.bean;

import java.io.Serializable;

public class Classify implements Serializable {
    private boolean isChecked;
    private String str;

    public boolean isChecked() {
        return isChecked;
    }
    public Classify(String str){
        this.str=str;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public String getStr() {
        return str;
    }
    public void setStr(String str1) {
        this.str = str1;
    }
}
