package bc.juhao.com.bean;

/**
 * Created by bocang on 18-2-7.
 */

/**
 * Copyright 2018 bejson.com 
 */

import java.util.List;

/**
 * Auto-generated: 2018-02-07 9:48:15
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class FangAnBean {

    private int id;
    private String name;
    private int scene_id;
    private String goods_id;
    private String content;
    private String sort;
    private String style;
    private String space;
    private String path;
    private int user_id;
    private int parent_id;
    private String date;
    private List<GoodsInfo> goodsInfo;
    private String username;
    private String avatar;
    private String parent_name;
    private String nickname;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isIs_privite() {
        return is_privite;
    }

    public void setIs_privite(boolean is_privite) {
        this.is_privite = is_privite;
    }

    private boolean is_privite;
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setScene_id(int scene_id) {
        this.scene_id = scene_id;
    }
    public int getScene_id() {
        return scene_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }
    public String getGoods_id() {
        return goods_id;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getSort() {
        return sort;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    public String getStyle() {
        return style;
    }

    public void setSpace(String space) {
        this.space = space;
    }
    public String getSpace() {
        return space;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public int getUser_id() {
        return user_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }
    public int getParent_id() {
        return parent_id;
    }

    public void setString(String date) {
        this.date = date;
    }
    public String getString() {
        return date;
    }


    public void setGoodsInfo(List<GoodsInfo> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }
    public List<GoodsInfo> getGoodsInfo() {
        return goodsInfo;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }
    public String getParent_name() {
        return parent_name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

}
