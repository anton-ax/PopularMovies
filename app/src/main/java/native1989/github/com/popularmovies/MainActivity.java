package native1989.github.com.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (Util.isTablet(getApplicationContext())) {
            resize();
        }
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            MainActivityFragment mainActivityFragment = new MainActivityFragment();

            mainActivityFragment.setArguments(getIntent().getExtras());

            fragmentTransaction.add(R.id.fragment_container, mainActivityFragment);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Util.isTablet(getApplicationContext())) {
            resize();
        }
    }

    /*
        Tablet UI specific code.
        If FragmentManager is empty, we need stretch RecyclerView
     */
    private void resize() {
        Fragment movieList = getSupportFragmentManager()
                .findFragmentById(R.id.movie_list);
        if (isFragmentManagerEmpty() && movieList != null && movieList.getView() != null) {
            ViewGroup.LayoutParams params = movieList.getView().getLayoutParams();
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            params.width = metrics.widthPixels;
            movieList.getView().setLayoutParams(params);
        }
    }

    private boolean isFragmentManagerEmpty() {
        return getSupportFragmentManager().getBackStackEntryCount() == 0;
    }
}
