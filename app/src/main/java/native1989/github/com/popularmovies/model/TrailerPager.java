package native1989.github.com.popularmovies.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Anton on 7/4/2015.
 */
public class TrailerPager {

    @Expose
    private Integer page;

    @Expose
    private List<Trailer> results;

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
