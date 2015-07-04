package native1989.github.com.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Empty fragment for Tablet UI
 *
 * Created by Anton on 7/4/2015.
 */
public class EmptyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.empty, container, false);
    }
}
