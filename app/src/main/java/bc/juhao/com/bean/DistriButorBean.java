package bc.juhao.com.bean;

/**
 * @author: Jun
 * @date : 2017/9/19 14:51
 * @description :
 */
public class DistriButorBean  {
    private int id;
    private String username;
    private int level;
    private String joined_at;
    private String sign;
    private String tel;

    public DistriButorBean(int id,String username,int level,String joined_at,String sign,String tel){
        this.id=id;
        this.username=username;
        this.level=level;
        this.joined_at=joined_at;
        this.sign=sign;
        this.tel=tel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(String joined_at) {
        this.joined_at = joined_at;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
