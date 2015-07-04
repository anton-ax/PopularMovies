package native1989.github.com.popularmovies;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import native1989.github.com.popularmovies.adapter.MovieHolder;
import native1989.github.com.popularmovies.adapter.OnItemClickListener;
import native1989.github.com.popularmovies.model.Movie;
import native1989.github.com.popularmovies.rest.ThemoviedbService;
import native1989.github.com.popularmovies.ui.ColumnSpaceDecoration;

/**
 * A main fragment
 */
public class MainActivityFragment extends Fragment implements
        AdapterView.OnItemSelectedListener, AdapterManagerCallback, OnItemClickListener, View.OnLayoutChangeListener {

    private static final int LANDSCAPE_COLUMN_COUNT = 5;//5
    private static final int PORTRAIT_COLUMN_COUNT = 3;
    private static final int COLUMN_SPACING = 2;

    private RecyclerView grid;
    private ProgressBar loader;

    private GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        grid = (RecyclerView) view.findViewById(R.id.grid);
        grid.setHasFixedSize(true);

        grid.addItemDecoration(new ColumnSpaceDecoration(COLUMN_SPACING));

        loader = (ProgressBar) view.findViewById(R.id.loader);

        Spinner filter = (Spinner) view.findViewById(R.id.filter);
        filter.setOnItemSelectedListener(this);

        view.addOnLayoutChangeListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int currentOrientation = getResources().getConfiguration().orientation;

        if (Util.isTablet(getActivity())
                && getActivity().getSupportFragmentManager().getBackStackEntryCount() != 0) {
            Fragment movieList = getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.movie_list);
            ViewGroup.LayoutParams params = movieList.getView().getLayoutParams();
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            params.width = getActivity().getResources()
                    .getDimensionPixelSize(R.dimen.titles_size);
            movieList.getView().setLayoutParams(params);
            gridLayoutManager = new GridLayoutManager(getActivity(), PORTRAIT_COLUMN_COUNT);
            grid.setLayoutManager(gridLayoutManager);
        } else {
            adjustColumnCount(currentOrientation);
        }
    }

    private void adjustColumnCount(int currentOrientation) {
        int columnCount;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            columnCount = LANDSCAPE_COLUMN_COUNT;
        } else {
            columnCount = PORTRAIT_COLUMN_COUNT;
        }

        gridLayoutManager = new GridLayoutManager(getActivity(), columnCount);
        grid.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                AdapterManager adapterManager = new NetworkAdapterManager((AppCompatActivity) getActivity(),
                        grid, this);
                adapterManager.addCallbackListener(this);
                adapterManager.fillData(ThemoviedbService.POPULARITY);
                break;
            case 1:
                adapterManager = new NetworkAdapterManager((AppCompatActivity) getActivity(),
                        grid, this);
                adapterManager.addCallbackListener(this);
                adapterManager.fillData(ThemoviedbService.VOTE);
                break;
            case 2:
                adapterManager = new ContentAdapterManager(getActivity(),
                        grid, this);
                adapterManager.addCallbackListener(this);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

        fragmentTransaction.add(R.id.fragment_container, fragment);
        if (!Util.isTablet(getActivity())) {
            fragmentTransaction.hide(this);
        } else {
            if (getActivity().getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                if (this.getView() != null) {
                    ViewGroup.LayoutParams params = this.getView().getLayoutParams();
                    params.width = getActivity().getResources()
                            .getDimensionPixelSize(R.dimen.titles_size);
                    this.getView().setLayoutParams(params);
                }
            }
        }

        fragmentTransaction.addToBackStack("movie");
        fragmentTransaction.addSharedElement(movieHolder.poster, transitionName)
                .commit();
    }

    /*
        Tablet specific code.
        If second fragment has been opened in landscape mode, we need
        shrink RecyclerView
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                               int oldTop, int oldRight, int oldBottom) {

        if (oldRight != right && oldRight != 0 &&
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (oldRight < right) {
                gridLayoutManager.setSpanCount(LANDSCAPE_COLUMN_COUNT);
            } else {
                gridLayoutManager.setSpanCount(PORTRAIT_COLUMN_COUNT);
            }
            grid.invalidateItemDecorations();
        }
    }
}
