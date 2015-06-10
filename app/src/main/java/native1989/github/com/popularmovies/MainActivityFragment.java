package native1989.github.com.popularmovies;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import native1989.github.com.popularmovies.adapter.MovieAdapter;
import native1989.github.com.popularmovies.model.Movie;
import native1989.github.com.popularmovies.model.MoviePager;
import native1989.github.com.popularmovies.network.CancellableCallback;
import native1989.github.com.popularmovies.rest.ThemoviedbService;
import native1989.github.com.popularmovies.ui.ColumnSpaceDecoration;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A main fragment
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final int LANDSCAPE_COLUMN_COUNT = 5;
    private static final int PORTRAIT_COLUMN_COUNT = 3;
    private static final int COLUMN_SPACING = 2;

    private CancellableCallback<MoviePager> requestCallback;
    private RecyclerView grid;
    private MovieAdapter adapter;
    private ThemoviedbService service;
    private ProgressBar loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        grid = (RecyclerView) view.findViewById(R.id.grid);
        grid.setHasFixedSize(true);

        int currentOrientation = getResources().getConfiguration().orientation;
        adjustColumnCount(currentOrientation);

        grid.addItemDecoration(new ColumnSpaceDecoration(COLUMN_SPACING));
        adapter = new MovieAdapter(getActivity());
        grid.setAdapter(adapter);

        loader = (ProgressBar) view.findViewById(R.id.loader);

        Spinner filter = (Spinner) view.findViewById(R.id.filter);
        filter.setOnItemSelectedListener(this);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org")
                .build();

        service = restAdapter.create(ThemoviedbService.class);

        loadData(ThemoviedbService.VOTE);

        return view;
    }

    private void adjustColumnCount(int currentOrientation) {
        int columnCount;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            columnCount = LANDSCAPE_COLUMN_COUNT;
        } else {
            columnCount = PORTRAIT_COLUMN_COUNT;
        }
        grid.setLayoutManager(new GridLayoutManager(getActivity(), columnCount));
    }

    /*
        Load json from themoviedb.com
     */
    private void loadData(String sort) {
        loader.setVisibility(View.VISIBLE);
        adapter.clear();

        // if queue isn't empty - cancel previous request
        if (requestCallback != null) {
            requestCallback.cancel();
        }

        requestCallback = new CancellableCallback<MoviePager>() {

            // Don't have any idea how simplify request cancellation
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
                    adapter.notifyDataSetChanged();
                    loader.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                loader.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Cannot connect to server :(", Toast.LENGTH_SHORT)
                        .show();
            }
        };

        service.listMovies(sort, requestCallback);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        adjustColumnCount(newConfig.orientation);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                loadData(ThemoviedbService.POPULARITY);
                break;
            case 1:
                loadData(ThemoviedbService.VOTE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
