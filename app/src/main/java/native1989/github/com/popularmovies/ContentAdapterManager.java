package native1989.github.com.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import native1989.github.com.popularmovies.adapter.FavoritesAdapter;
import native1989.github.com.popularmovies.adapter.OnItemClickListener;
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

    }
}
