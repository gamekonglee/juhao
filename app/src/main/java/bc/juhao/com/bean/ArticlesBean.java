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
    String link;
    String file_url;
    int display;

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public ArticlesBean(int id, String title, String url, String link, String file_url,int display) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.link = link;
        this.file_url=file_url;
        this.display=display;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArticlesBean(int id, String title, String url){
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
