package native1989.github.com.popularmovies;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import native1989.github.com.popularmovies.adapter.MovieHolder;
import native1989.github.com.popularmovies.adapter.OnItemClickListener;
import native1989.github.com.popularmovies.model.Movie;
import native1989.github.com.popularmovies.rest.ThemoviedbService;
import native1989.github.com.popularmovies.ui.ColumnSpaceDecoration;

/**
 * A main fragment
 */
public class MainActivityFragment extends Fragment implements
        AdapterManagerCallback, OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final int COLUMN_SPACING = 2;

    private RecyclerView grid;
    private ProgressBar loader;
    private int filterPosition;

    private AdapterManager adapterManager;
    private boolean firstTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        grid = (RecyclerView) view.findViewById(R.id.grid);
        grid.setHasFixedSize(true);
        firstTime = true;

        grid.addItemDecoration(new ColumnSpaceDecoration(COLUMN_SPACING));

        loader = (ProgressBar) view.findViewById(R.id.loader);

        Spinner filter = (Spinner) view.findViewById(R.id.filter);


        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey("filterPosition")) {
                filterPosition = savedInstanceState.getInt("filterPosition");
            }
            filter.setSelection(filterPosition);
            setAdapter(filterPosition, false);
            if (savedInstanceState.containsKey("movies")) {
                ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList("movies");
                adapterManager.recreate(movies);
            } else {
                loader.setVisibility(View.INVISIBLE);
            }
        } else {
            setAdapter(0, true);
        }
        filter.setOnItemSelectedListener(this);

        return view;
    }

    private void setAdapter(int position, boolean fill) {
        String filter = null;
        switch (position) {
            case 0:
                adapterManager = new NetworkAdapterManager((AppCompatActivity) getActivity(),
                        grid, this);
                filter = ThemoviedbService.POPULARITY;
                break;
            case 1:
                adapterManager = new NetworkAdapterManager((AppCompatActivity) getActivity(),
                        grid, this);
                filter = ThemoviedbService.VOTE;
                break;
            case 2:
                adapterManager = new ContentAdapterManager(getActivity(),
                        grid, this);
                filter = "stub";
                break;
        }
        adapterManager.addCallbackListener(this);
        if (fill && filter != null) {
            adapterManager.fillData(filter);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (!firstTime) {
            if (filterPosition != position) {
                filterPosition = position;
                setAdapter(position, true);
            }
        }
        firstTime = false;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adjustColumnCount();
    }

    private void adjustColumnCount() {
        int columnCount = getResources().getInteger(R.integer.column_count);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), columnCount);
        grid.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onPreExecute() {
        loader.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostExecute() {
        loader.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFail() {
        loader.setVisibility(View.INVISIBLE);
        Toast.makeText(getActivity(), "Cannot connect to server :(", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onItemClick(View view, MovieHolder movieHolder) {
        Fragment fragment = new MovieFragment();

        Bundle b = new Bundle();
        Movie movie = movieHolder.movie;
        b.putParcelable("movie", movie);
        fragment.setArguments(b);

        String transitionName = String.valueOf(movie.getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                !Util.isTablet(getActivity())) {
            setSharedElementReturnTransition(TransitionInflater.from(getActivity().getApplicationContext())
                    .inflateTransition(R.transition.move));
            setReturnTransition(TransitionInflater.from(getActivity())
                    .inflateTransition(android.R.transition.explode));
            setAllowReturnTransitionOverlap(true);
            movieHolder.poster.setTransitionName(transitionName);
            fragment.setSharedElementEnterTransition(TransitionInflater
                    .from(getActivity().getApplicationContext())
                    .inflateTransition(R.transition.move));
            fragment.setEnterTransition(TransitionInflater
                    .from(getActivity().getApplicationContext())
                    .inflateTransition(android.R.transition.slide_right));
        }

        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager
                .beginTransaction();

        if (getActivity().findViewById(R.id.dual_pane) != null) {
            fragmentTransaction.add(R.id.detail_dual, fragment, MainActivity.DETAIL_FRAGMENT);
        } else {
            fragmentTransaction.hide(this);
            fragmentTransaction.add(R.id.single_pane, fragment, MainActivity.DETAIL_FRAGMENT);
        }
        fragmentTransaction.addToBackStack(MainActivity.DETAIL_FRAGMENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                !Util.isTablet(getActivity())) {
            fragmentTransaction.addSharedElement(movieHolder.poster, transitionName);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("filterPosition", filterPosition);
        if (adapterManager != null && adapterManager.movieList() != null) {
            outState.putParcelableArrayList("movies", adapterManager.movieList());
        }
    }
}
