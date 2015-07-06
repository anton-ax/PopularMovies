package native1989.github.com.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import native1989.github.com.popularmovies.adapter.FavoritesAdapter;
import native1989.github.com.popularmovies.adapter.OnItemClickListener;
import native1989.github.com.popularmovies.model.Movie;
import native1989.github.com.popularmovies.provider.FavoriteMovieProvider;

/**
 * Retrieve movies from local data
 * Created by Anton on 6/15/2015.
 */
public class ContentAdapterManager extends AdapterManager {

    public ContentAdapterManager(Context context, RecyclerView grid,
                                 OnItemClickListener onItemClickListener) {
        Cursor cursor = context.getContentResolver().query(
                FavoriteMovieProvider.CONTENT_URI, null, null, null, FavoriteMovieProvider._ID);

        FavoritesAdapter adapter = new FavoritesAdapter(cursor, context);
        adapter.setOnItemClickListener(onItemClickListener);
        grid.setAdapter(adapter);
    }

    @Override
    public void fillData(String param) {
        // we doesn't have any delay so we can call onPostExecute immediately;
        callback.onPostExecute();
    }

    @Override
    public ArrayList<Movie> movieList() {
        return null;
    }

    @Override
    public void recreate(ArrayList<Movie> movies) {
        // it will never call, cause we don't save adapter state
        callback.onPostExecute();
    }
}
