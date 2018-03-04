package bc.juhao.com.bean;

/**
 * @author: Jun
 * @date : 2017/9/15 11:45
 * @description :
 */
public class ArticlesBean {
    int id;
    String title;
    String url;

    public ArticlesBean(int id,String title,String url){
        this.id=id;
        this.title=title;
        this.url=url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
