package native1989.github.com.popularmovies;

import android.os.Parcelable;

import java.util.ArrayList;

import native1989.github.com.popularmovies.model.Movie;

/**
 * Created by Anton on 6/15/2015.
 */
public abstract class AdapterManager {
    protected AdapterManagerCallback callback;

    public abstract void fillData(String param);

    public void addCallbackListener(AdapterManagerCallback callBack) {
        this.callback = callBack;
    }

    public abstract ArrayList<Movie> movieList();

    public abstract void recreate(ArrayList<Movie> movies);
}
