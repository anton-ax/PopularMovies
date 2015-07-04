package native1989.github.com.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import native1989.github.com.popularmovies.model.Movie;
import native1989.github.com.popularmovies.model.Review;
import native1989.github.com.popularmovies.model.ReviewPager;
import native1989.github.com.popularmovies.model.Trailer;
import native1989.github.com.popularmovies.model.TrailerPager;
import native1989.github.com.popularmovies.provider.FavoriteMovieProvider;
import native1989.github.com.popularmovies.rest.ThemoviedbService;
import native1989.github.com.popularmovies.ui.PosterImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Anton on 7/4/2015.
 */
public class MovieFragment extends Fragment implements
        View.OnClickListener {

    private Movie movie;
    private boolean favorited;
    private FloatingActionButton favorite;
    private ThemoviedbService service;
    private LinearLayout trailersView;
    private LinearLayout reviewsView;
    private String firstTrailer;
    private MenuItem item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_fragment, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ImageView backdrop = (ImageView) view.findViewById(R.id.backdrop);
        PosterImageView poster = (PosterImageView) view.findViewById(R.id.poster);


        trailersView = (LinearLayout) view.findViewById(R.id.trailers);
        reviewsView = (LinearLayout) view.findViewById(R.id.reviews);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView overview = (TextView) view.findViewById(R.id.overview);
        TextView rate = (TextView) view.findViewById(R.id.rate);
        TextView date = (TextView) view.findViewById(R.id.date);
        favorite = (FloatingActionButton) view.findViewById(R.id.favorite);
        favorite.setOnClickListener(this);
        if (getArguments() != null && getArguments().containsKey("movie")) {
            movie = getArguments().getParcelable("movie");
        }
        if (movie != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                poster.setTransitionName(String.valueOf(movie.getId()));
            }
            title.setText(movie.getTitle());
            overview.setText(movie.getOverview());
            rate.setText(movie.getVote_average() + "/10");

            DateFormat dateFormat = DateFormat.getDateInstance();
            String releaseDate = movie.getRelease_date();
            releaseDate = releaseDate.replaceAll("-", "/");
            String formattedDate = dateFormat.format(new Date(releaseDate));
            date.setText(formattedDate);

            Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w780/" + movie.getBackdrop_path())
                    .into(backdrop);

            Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path())
                    .placeholder(R.drawable.preloader)
                    .into(poster);

            Cursor uri = getActivity().getContentResolver().query(
                    FavoriteMovieProvider.CONTENT_URI, null, "id=?",
                    new String[]{String.valueOf(movie.getId())}, FavoriteMovieProvider._ID);
            favorited = uri != null && uri.getCount() > 0;
            if (uri != null) {
                uri.close();
            }
            updateActionButton();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.themoviedb.org")
                    .build();

            service = restAdapter.create(ThemoviedbService.class);

            retrieveTrailers();
            retrieveReviews();
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_menu, menu);
        item = menu.findItem(R.id.share);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void retrieveReviews() {
        Callback<ReviewPager> requestCallback = new Callback<ReviewPager>() {

            @Override
            public void success(ReviewPager pager, Response response) {
                List<Review> results = pager.getResults();
                if (results.size() > 0) {
                    reviewsView.setVisibility(View.VISIBLE);
                    for (Review review : results) {
                        displayReview(review);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };

        service.movieReviews(movie.getId(), requestCallback);
    }

    private void displayReview(Review review) {
        if (getActivity() != null) {
            LinearLayout reviewLayout = (LinearLayout) getActivity().getLayoutInflater()
                    .inflate(R.layout.review_item, trailersView, false);

            TextView author = (TextView) reviewLayout.findViewById(R.id.author);
            author.setText(review.getAuthor());

            TextView content = (TextView) reviewLayout.findViewById(R.id.content);
            content.setText(review.getContent());
            reviewsView.addView(reviewLayout);
        }
    }

    private void retrieveTrailers() {
        Callback<TrailerPager> requestCallback = new Callback<TrailerPager>() {

            @Override
            public void success(TrailerPager pager, Response response) {
                List<Trailer> results = pager.getResults();
                if (results.size() > 0) {
                    trailersView.setVisibility(View.VISIBLE);
                    boolean first = true;
                    for (Trailer trailer : results) {
                        if (first) {
                            firstTrailer = trailer.getKey();
                            item.setVisible(true);
                            first = false;
                        }
                        displayTrailer(trailer);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };

        service.movieTrailers(movie.getId(), requestCallback);
    }

    private void displayTrailer(final Trailer trailer) {
        if (getActivity() != null) {
            TextView trailerText = (TextView) getActivity().getLayoutInflater()
                    .inflate(R.layout.trailer_item, trailersView, false);

            trailerText.setText(trailer.getName());
            trailerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open(trailer.getKey());
                }
            });
            trailersView.addView(trailerText);
        }
    }

    private void updateActionButton() {
        if (favorited) {
            favorite.setImageResource(R.drawable.ic_favorite);
        } else {
            favorite.setImageResource(R.drawable.ic_nonfavorite);
        }
    }

    @Override
    public void onClick(View v) {

        if (favorited) {
            getActivity().getContentResolver()
                    .delete(FavoriteMovieProvider.CONTENT_URI, "id=?",
                            new String[]{String.valueOf(movie.getId())});
        } else {
            ContentValues values = new ContentValues();
            values.put("id", movie.getId());
            values.put("poster_path", movie.getPoster_path());
            values.put("title", movie.getTitle());
            values.put("vote_average", movie.getVote_average());
            values.put("overview", movie.getOverview());
            values.put("backdrop_path", movie.getBackdrop_path());
            values.put("release_date", movie.getRelease_date());
            getActivity().getContentResolver()
                    .insert(FavoriteMovieProvider.CONTENT_URI, values);
        }
        favorited = !favorited;
        updateActionButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (item.getItemId() == R.id.share) {
            share(firstTrailer);
        }
        return true;
    }

    private void open(String key) {
        String urlToShare = "https://www.youtube.com/watch?v=" + key;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToShare));
        startActivity(intent);
    }

    private void share(String key) {
        String urlToShare = "https://www.youtube.com/watch?v=" + key;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
