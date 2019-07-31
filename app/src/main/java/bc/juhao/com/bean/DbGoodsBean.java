package bc.juhao.com.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by gamekonglee on 2018/4/9.
 */

@Entity
public class DbGoodsBean {
    String name;
    String price;
    String original_img;
    String current_price;
    String create_time;
    int id;
    @Id
    Long g_id;
    public Long getG_id() {
        return this.g_id;
    }
    public void setG_id(Long g_id) {
        this.g_id = g_id;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCreate_time() {
        return this.create_time;
    }
    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
    public String getCurrent_price() {
        return this.current_price;
    }
    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }
    public String getOriginal_img() {
        return this.original_img;
    }
    public void setOriginal_img(String original_img) {
        this.original_img = original_img;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Generated(hash = 1895214603)
    public DbGoodsBean(String name, String price, String original_img,
            String current_price, String create_time, int id, Long g_id) {
        this.name = name;
        this.price = price;
        this.original_img = original_img;
        this.current_price = current_price;
        this.create_time = create_time;
        this.id = id;
        this.g_id = g_id;
    }
    @Generated(hash = 1153896551)
    public DbGoodsBean() {
    }

}
