package in.yogesh.searchx.app;

import android.app.Application;

import in.yogesh.searchx.app.model.database.SearchDataBase;
import in.yogesh.searchx.library.utility.ResourceUtils;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class SearchXApp extends Application {

    private AppExecutor appExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        appExecutor = new AppExecutor();
        ResourceUtils.initialize(getApplicationContext());
    }

    public SearchDataBase getDatabase() {
        return SearchDataBase.getInstance(this, appExecutor);
    }

}
