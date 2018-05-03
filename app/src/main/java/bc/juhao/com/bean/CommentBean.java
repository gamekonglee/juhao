package bc.juhao.com.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gamekonglee on 2018/4/2.
 */

public class CommentBean implements Serializable{
    public Author author;
    public String updated_at;
    public String content;
    public List<String >path;
    public List<String> movie;

    public List<String> getMovies() {
        return movie;
    }

    public void setMovies(List<String> movies) {
        this.movie = movies;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }
}
