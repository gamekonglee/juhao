package bc.juhao.com.bean;

/**
 * Created by gamekonglee on 2018/7/16.
 */

public class BaseJson {
    String code;
    int error_code;
    String debug_id="";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getDebug_id() {
        return debug_id;
    }

    public void setDebug_id(String debug_id) {
        this.debug_id = debug_id;
    }

    public BaseJson(String code, int error_code, String debug_id) {
        this.code = code;
        this.error_code = error_code;
        this.debug_id = debug_id;
    }
}
