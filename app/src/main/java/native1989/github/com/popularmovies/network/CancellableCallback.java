package native1989.github.com.popularmovies.network;

import retrofit.Callback;

/*
    We extends default retrofit callback to support request cancellation
 */
public interface CancellableCallback<T> extends Callback<T> {
    void cancel();
    boolean isCanceled();
}