package native1989.github.com.popularmovies;

/**
 * Created by Anton on 6/15/2015.
 */
public abstract class AdapterManager {
    protected AdapterManagerCallback callback;

    public abstract void fillData(String param);

    public void addCallbackListener(AdapterManagerCallback callBack) {
        this.callback = callBack;
    }

}
