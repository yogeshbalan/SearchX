package in.yogesh.searchx.app;

import android.app.Application;

import in.yogesh.searchx.library.utility.ResourceUtils;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class SearchXApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ResourceUtils.initialize(getApplicationContext());
    }
}
