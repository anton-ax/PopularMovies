package native1989.github.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class Movie implements Parcelable {

    @Expose
    private Integer id;

    @Expose
    private String poster_path;

    @Expose
    private String title;

    @Expose
    private String vote_average;

    @Expose
    private String overview;

    @Expose
    private String backdrop_path;

    @Expose String release_date;

    private Movie(Parcel in) {
        id = in.readInt();
        poster_path = in.readString();
        title = in.readString();
        vote_average = in.readString();
        overview = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(poster_path);
        out.writeString(title);
        out.writeString(vote_average);
        out.writeString(overview);
        out.writeString(backdrop_path);
        out.writeString(release_date);
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}