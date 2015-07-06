package native1989.github.com.popularmovies.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 7/4/2015.
 */
public class ReviewPager {

    @Expose
    private Integer page;

    @Expose
    private ArrayList<Review> results;

    public ArrayList<Review> getResults() {
        return results;
    }

    public void setResults(ArrayList<Review> results) {
        this.results = results;
    }
}
