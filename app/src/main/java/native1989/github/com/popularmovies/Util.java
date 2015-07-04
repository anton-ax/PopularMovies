package native1989.github.com.popularmovies;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Anton on 7/5/2015.
 */
public class Util {

    /*
        Copied from
        http://stackoverflow.com/questions/9279111/determine-if-the-device-is-a-smartphone-or-tablet
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
