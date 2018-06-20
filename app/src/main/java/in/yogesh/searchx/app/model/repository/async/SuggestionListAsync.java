package in.yogesh.searchx.app.model.repository.async;

import android.os.AsyncTask;

import java.util.List;

import in.yogesh.searchx.app.model.database.SearchDataBase;
import in.yogesh.searchx.app.model.repository.interfaces.SearchCacheResultAsyncDelegate;

public class SuggestionListAsync extends AsyncTask<String, Void, List<String>> {

    private SearchDataBase searchDataBase;
    private SearchCacheResultAsyncDelegate asyncDelegate;
    private List<String> distinctQueryList;

    public SuggestionListAsync(SearchDataBase searchDataBase, SearchCacheResultAsyncDelegate asyncDelegate) {
        this.searchDataBase = searchDataBase;
        this.asyncDelegate = asyncDelegate;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        return searchDataBase.imageEntityDao().getAllDistinctQueryFromDb();
    }

    protected void onPostExecute(List<String> strings) {
        super.onPostExecute(strings);
        distinctQueryList = strings;
        if (asyncDelegate != null) {
            asyncDelegate.onFetchQueryListFromCacheComplete(distinctQueryList);
        }
        return;
    }

}
