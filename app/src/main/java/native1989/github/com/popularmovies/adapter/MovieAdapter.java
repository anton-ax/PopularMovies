package native1989.github.com.popularmovies.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import native1989.github.com.popularmovies.R;
import native1989.github.com.popularmovies.model.Movie;

/**
 * Created by Anton on 6/7/2015.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

    private final AppCompatActivity context;
    private ArrayList<Movie> items;
    private OnItemClickListener mItemClickListener;

    public MovieAdapter(AppCompatActivity context) {
        this.items = new ArrayList<>();
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_poster, parent, false);

        return new MovieHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieHolder viewHolder, final int position) {
        Movie movie = items.get(position);
        viewHolder.movie = movie;
        viewHolder.container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(view, viewHolder);
                }
            }
        });

        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path())
                .placeholder(R.drawable.preloader)
                .into(viewHolder.poster);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addAll(Collection<? extends Movie> movies) {
        items.addAll(movies);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getList() {
        return items;
    }
}
