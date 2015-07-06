package native1989.github.com.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/*
    Master-Detail Flow layout
 */
public class MainActivity extends AppCompatActivity {

    public static final String MASTER_FRAGMENT = "MASTER_FRAGMENT";
    public static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager
                    .beginTransaction();
            Fragment fragment = new MainActivityFragment();

            if (findViewById(R.id.dual_pane) != null) {
                fragmentTransaction.add(R.id.master_dual, fragment, MainActivity.MASTER_FRAGMENT);
            } else {
                fragmentTransaction.add(R.id.single_pane, fragment, MainActivity.MASTER_FRAGMENT);
            }
            fragmentTransaction.commit();
        }
    }
}
