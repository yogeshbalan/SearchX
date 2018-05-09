package in.yogesh.searchx.app.model.repository.async;

import android.os.AsyncTask;

import java.util.List;

import in.yogesh.searchx.app.model.data.Image;
import in.yogesh.searchx.app.model.database.SearchResultDbHelper;

/**
 * @author Yogesh Kumar on 9/5/18
 */
public class SaveDataInDBAsync extends AsyncTask {


    private List<Image> imageList;
    private String query;
    private SearchResultDbHelper searchResultDbHelper;

    public SaveDataInDBAsync(List<Image> imageList, String query, SearchResultDbHelper searchResultDbHelper) {
        this.imageList = imageList;
        this.query = query;
        this.searchResultDbHelper = searchResultDbHelper;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        searchResultDbHelper.insertSearchResult(imageList, query);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }
}
