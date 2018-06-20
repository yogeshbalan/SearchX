package in.yogesh.searchx.app.model.repository.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import in.yogesh.searchx.app.model.data.ImageEntity;
import in.yogesh.searchx.app.model.database.SearchDataBase;
import in.yogesh.searchx.app.model.repository.interfaces.SearchCacheResultAsyncDelegate;
import in.yogesh.searchx.app.model.rvdata.ImageRvData;
import in.yogesh.searchx.library.utility.Utils;

/**
 * @author Yogesh Kumar on 9/5/18
 */
public class CacheAsync extends AsyncTask<String, Void, List<ImageEntity>> {

    private String query;
    private SearchDataBase searchDataBase;
    private SearchCacheResultAsyncDelegate asyncDelegate;

    public CacheAsync(String query,
                      SearchDataBase searchDataBase,
                      SearchCacheResultAsyncDelegate asyncDelegate) {
        this.query = query;
        this.searchDataBase = searchDataBase;
        this.asyncDelegate = asyncDelegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (asyncDelegate != null) {
            asyncDelegate.onDataFetchFromCacheStarted();
        }
    }

    @Override
    protected List<ImageEntity> doInBackground(String... strings) {
        List<ImageEntity> imageEntityList = searchDataBase.imageEntityDao().findImageResultByQuery(query);
        return imageEntityList;
    }

    @Override
    protected void onPostExecute(List<ImageEntity> imageEntities) {
        super.onPostExecute(imageEntities);
        List<ImageRvData> imageRvData = new ArrayList<>();
        if (!Utils.isNullOrEmpty(imageEntities)) {
            curateImageRvData(imageEntities, imageRvData);
            if (asyncDelegate != null)
                asyncDelegate.onFetchImagesFromCacheComplete(imageRvData);

        } else {
            if (asyncDelegate != null)
                asyncDelegate.onDataFetchFailed();

        }
    }

    private void curateImageRvData(List<ImageEntity> imageEntities, List<ImageRvData> imageRvData) {
        for (ImageEntity imageEntity : imageEntities) {
            Bitmap bitmap = Utils.getBitmapFromByteArray(imageEntity.getImage());
            if (bitmap != null) {
                imageRvData.add(new ImageRvData(bitmap, imageEntities.indexOf(imageEntity)));
            }
        }
    }
}
