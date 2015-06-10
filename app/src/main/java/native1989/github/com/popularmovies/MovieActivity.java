package native1989.github.com.popularmovies;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import native1989.github.com.popularmovies.model.Movie;

/**
 * Created by Anton on 6/9/2015.
 */
public class MovieActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity);

        ImageView backdrop = (ImageView) findViewById(R.id.backdrop);
        ImageView poster = (ImageView) findViewById(R.id.poster);

        TextView title = (TextView) findViewById(R.id.title);
        TextView overview = (TextView) findViewById(R.id.overview);
        TextView rate = (TextView) findViewById(R.id.rate);
        TextView date = (TextView) findViewById(R.id.date);

        Movie movie = getIntent().getParcelableExtra("movie");

        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        rate.setText(movie.getVote_average() + "/10");

        DateFormat dateFormat = DateFormat.getDateInstance();
        String releaseDate = movie.getRelease_date();
        releaseDate = releaseDate.replaceAll("-", "/");
        String formattedDate = dateFormat.format(new Date(releaseDate));
        date.setText(formattedDate);

        Picasso.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w780/" + movie.getBackdrop_path())
                .into(backdrop);

        Picasso.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path())
                .placeholder(R.drawable.preloader)
                .into(poster);
    }
}
