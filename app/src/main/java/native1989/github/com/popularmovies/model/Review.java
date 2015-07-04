package native1989.github.com.popularmovies.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Anton on 7/4/2015.
 */
public class Review {

    @Expose
    private String id;

    @Expose
    private String author;

    @Expose
    private String content;

    @Expose
    private String url;

    public Review() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
