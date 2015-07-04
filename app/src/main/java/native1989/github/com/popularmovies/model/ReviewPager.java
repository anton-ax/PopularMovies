package native1989.github.com.popularmovies.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Anton on 7/4/2015.
 */
public class ReviewPager {

    @Expose
    private Integer page;

    @Expose
    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
