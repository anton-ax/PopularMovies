package native1989.github.com.popularmovies.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Anton on 7/4/2015.
 */
public class Trailer {

    @Expose
    private String id;

    @Expose
    private String key;

    @Expose
    private String name;

    @Expose
    private String site;

    @Expose
    private Integer size;

    @Expose
    private String type;

    public Trailer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
