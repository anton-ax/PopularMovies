package native1989.github.com.popularmovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import native1989.github.com.popularmovies.model.Movie;

/**
 * Created by Anton on 7/3/2015.
 */
public class FavoriteData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_NAME = "favorite";
    private final static String COLUMN_ID = "_id";
    private final static String ID = "id";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + " id integer not null, "
            + " poster_path text not null, "
            + " title text not null, "
            + " vote_average text not null, "
            + " overview text not null, "
            + " backdrop_path text not null, "
            + " release_date integer not null);";

    public FavoriteData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(FavoriteData.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void add(Movie movie) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", movie.getId());
        values.put("poster_path", movie.getPoster_path());
        values.put("title", movie.getTitle());
        values.put("vote_average", movie.getVote_average());
        values.put("overview", movie.getOverview());
        values.put("backdrop_path", movie.getBackdrop_path());
        values.put("release_date", movie.getRelease_date());
        sqLiteDatabase.insert(TABLE_NAME, null, values);
    }

    public void remove(Movie movie) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(movie.getId())});
    }

    public Cursor list() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT _id, id, poster_path, title, vote_average," +
                "overview, backdrop_path, release_date FROM favorite ORDER BY _id";
        return sqLiteDatabase.rawQuery(query, null);
    }

    public boolean exists(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        long num = DatabaseUtils.queryNumEntries(db, TABLE_NAME, "id=?",
                new String[]{String.valueOf(id)});
        return num > 0;
    }
}
