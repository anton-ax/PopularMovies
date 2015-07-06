package native1989.github.com.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import native1989.github.com.popularmovies.adapter.MovieAdapter;
import native1989.github.com.popularmovies.adapter.OnItemClickListener;
import native1989.github.com.popularmovies.model.Movie;
import native1989.github.com.popularmovies.model.MoviePager;
import native1989.github.com.popularmovies.network.CancellableCallback;
import native1989.github.com.popularmovies.rest.ThemoviedbService;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Retrieve movies from network
 * Created by Anton on 6/15/2015.
 */
public class NetworkAdapterManager extends AdapterManager {

    private final ThemoviedbService service;
    private MovieAdapter adapter;

    private CancellableCallback<MoviePager> requestCallback;

    public NetworkAdapterManager(AppCompatActivity context, RecyclerView grid,
                                 OnItemClickListener onItemClickListener) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org")
                .build();

        adapter = new MovieAdapter(context);
        adapter.setOnItemClickListener(onItemClickListener);
        grid.setAdapter(adapter);

        service = restAdapter.create(ThemoviedbService.class);
    }

    @Override
    public void fillData(String sort) {
        adapter.clear();
        callback.onPreExecute();

        // if queue isn't empty - cancel previous request
        if (requestCallback != null) {
            requestCallback.cancel();
        }

        requestCallback = new CancellableCallback<MoviePager>() {

            private boolean cancel;

            @Override
            public void cancel() {
                cancel = true;
            }

            @Override
            public boolean isCanceled() {
                return cancel;
            }

            @Override
            public void success(MoviePager pager, Response response) {
                if (!isCanceled()) {
                    List<Movie> results = pager.getResults();
                    adapter.addAll(results);
                }
                callback.onPostExecute();
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onFail();
            }
        };

        service.listMovies(sort, requestCallback);
    }

    @Override
    public ArrayList<Movie> movieList() {
        return adapter.getList();
    }

    @Override
    public void recreate(ArrayList<Movie> movies) {
        adapter.addAll(movies);
        callback.onPostExecute();
    }
}
