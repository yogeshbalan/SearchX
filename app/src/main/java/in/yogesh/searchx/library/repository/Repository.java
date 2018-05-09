package in.yogesh.searchx.library.repository;

import android.os.Bundle;

import java.util.Map;

/**
 * @author Yogesh Kumar on 7/5/18
 */

public abstract class Repository<T extends Repository.OnDataFetched> {
    protected String url;
    protected T listener;
    protected Bundle bundle;
    Map<String, String> queryMap;
    private boolean isDestroyed = false;

    public Repository(String url, Map<String, String> queryMap) {
        this.url = url;
        this.queryMap = queryMap;
    }

    public Repository(Bundle bundle) {
        this.bundle = bundle;
    }

    public Repository(String url, T listener) {
        this.url = url;
        this.listener = listener;
    }

    public Repository(String url, T listener, Bundle bundle) {
        this.url = url;
        this.listener = listener;
        this.bundle = bundle;
    }

    public Repository(String url, Bundle bundle) {
        this.url = url;
        this.bundle = bundle;
    }

    public Repository(String url) {
        this.url = url;
    }

    public Repository() {

    }

    public void setListener(T listener) {
        this.listener = listener;
    }

    public abstract void provideData();

    protected abstract void fetchFromNetwork(String url, Map<String, String> queryMap);

    protected abstract void fetchFromCache(String url, Map<String, String> queryMap);

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void onDestroy() {
        isDestroyed = true;
    }

    public interface OnDataFetched {
        void onDataFetchedFromNetwork();

        void onDataFetchedFromCache();

        void onDataFetchStarted();

        /**
         * Called when fetching data from network fails
         */
        void onDataFetchFailed();
    }
}
