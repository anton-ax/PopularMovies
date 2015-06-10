package native1989.github.com.popularmovies.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;

import native1989.github.com.popularmovies.MovieActivity;
import native1989.github.com.popularmovies.R;
import native1989.github.com.popularmovies.model.Movie;
import native1989.github.com.popularmovies.ui.PosterImageView;

/**
 * Created by Anton on 6/7/2015.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Movie> items;

    public MovieAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_poster, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Movie movie = items.get(position);
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
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public PosterImageView poster;

        public ViewHolder(View itemView) {
            super(itemView);
            poster = (PosterImageView) itemView.findViewById(R.id.poster);
            poster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MovieActivity.class);
            intent.putExtra("movie", items.get(getAdapterPosition()));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String transitionName = context.getString(R.string.movie_poster);
                ActivityOptions transitionActivityOptions = ActivityOptions
                        .makeSceneTransitionAnimation((Activity) context, poster, transitionName);
                context.startActivity(intent, transitionActivityOptions.toBundle());
            } else {
                context.startActivity(intent);
            }
        }
    }
}
