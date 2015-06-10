package native1989.github.com.popularmovies.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Anton on 6/7/2015.
 */
public class MoviePager {

    @Expose
    private Integer page;

    @Expose
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }


}
