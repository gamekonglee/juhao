package bc.juhao.com.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gamekonglee on 2019/5/17.
 */

public class DistribuBean {
    private int id;
    private String nickname;
    private String username;
    private String mobile;
    private String level;
    private String joined_at;
    private List<DistribuBean> parent=new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(String joined_at) {
        this.joined_at = joined_at;
    }

    public List<DistribuBean> getParent() {
        return parent;
    }

    public void setParent(List<DistribuBean> parent) {
        this.parent = parent;
    }
}
