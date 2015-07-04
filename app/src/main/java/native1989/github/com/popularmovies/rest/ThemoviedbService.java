package native1989.github.com.popularmovies.rest;

import native1989.github.com.popularmovies.model.MoviePager;
import native1989.github.com.popularmovies.model.ReviewPager;
import native1989.github.com.popularmovies.model.TrailerPager;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Anton on 6/7/2015.
 */
public interface ThemoviedbService {

    String ACCESS_TOKEN = "1109a0924442c1508b4b67bd8d32c0e8";

    String POPULARITY = "popularity.desc";
    String VOTE = "vote_count.desc";

    @GET("/3/discover/movie?api_key=" + ACCESS_TOKEN)
    void listMovies(@Query("sort_by") String sort, Callback<MoviePager> response);

    @GET("/3/movie/{id}/videos?api_key=" + ACCESS_TOKEN)
    void movieTrailers(@Path("id") Integer id, Callback<TrailerPager> response);

    @GET("/3/movie/{id}/reviews?api_key=" + ACCESS_TOKEN)
    void movieReviews(@Path("id") Integer id, Callback<ReviewPager> response);
}
