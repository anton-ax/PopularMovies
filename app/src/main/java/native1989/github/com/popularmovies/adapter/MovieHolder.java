package native1989.github.com.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import native1989.github.com.popularmovies.R;
import native1989.github.com.popularmovies.model.Movie;
import native1989.github.com.popularmovies.ui.PosterImageView;

/**
 * Created by Anton on 7/4/2015.
 */
public class MovieHolder extends RecyclerView.ViewHolder {

    public PosterImageView poster;
    public Movie movie;
    public View container;

    public MovieHolder(View itemView) {
        super(itemView);
        container = itemView;
        poster = (PosterImageView) itemView.findViewById(R.id.poster);
    }
}
