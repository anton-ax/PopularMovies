package native1989.github.com.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import native1989.github.com.popularmovies.R;
import native1989.github.com.popularmovies.model.Movie;

/**
 * Created by Anton on 6/15/2015.
 */
public class FavoritesAdapter extends CursorRecyclerAdapter<MovieHolder> {

    private final Context context;

    private OnItemClickListener mItemClickListener;

    public FavoritesAdapter(Cursor cursor, Context context) {
        super(cursor);
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolderCursor(final MovieHolder holder, Cursor cursor) {
        Movie movie = new Movie();
        movie.setId(cursor.getInt(1));
        movie.setPoster_path(cursor.getString(2));
        movie.setTitle(cursor.getString(3));
        movie.setVote_average(cursor.getString(4));
        movie.setOverview(cursor.getString(5));
        movie.setBackdrop_path(cursor.getString(6));
        movie.setRelease_date(cursor.getString(7));
        holder.movie = movie;

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path())
                .placeholder(R.drawable.preloader)
                .into(holder.poster);

        holder.container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(view, holder);
                }
            }
        });
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_poster, parent, false);

        return new MovieHolder(v);
    }
}
