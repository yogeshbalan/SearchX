package in.yogesh.searchx.app.model.repository.async;

import android.os.AsyncTask;

import in.yogesh.searchx.app.model.data.ImageEntity;
import in.yogesh.searchx.app.model.database.SearchDataBase;

public class AddDataAsync extends AsyncTask {

    private SearchDataBase searchDataBase;
    private ImageEntity[] imageEntities;

    public AddDataAsync(SearchDataBase searchDataBase, ImageEntity[] imageEntities) {
        this.searchDataBase = searchDataBase;
        this.imageEntities = imageEntities;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        if (searchDataBase.getDatabaseCreated().getValue() != null) {
            searchDataBase.imageEntityDao().insertAll(imageEntities);
        }
        return null;
    }
}
