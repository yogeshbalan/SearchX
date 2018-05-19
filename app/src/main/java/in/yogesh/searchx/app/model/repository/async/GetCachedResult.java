package in.yogesh.searchx.app.model.repository.async;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import in.yogesh.searchx.app.model.data.Image;
import in.yogesh.searchx.app.model.data.ImageResult;
import in.yogesh.searchx.app.model.database.SearchDataBase;
import in.yogesh.searchx.app.model.repository.interfaces.SearchCacheResultAsyncDelegate;
import in.yogesh.searchx.app.model.rvdata.ImageRvData;
import in.yogesh.searchx.library.utility.Utils;

/**
 * @author Yogesh Kumar on 9/5/18
 */
public class GetCachedResult extends AsyncTask<String, Void, List<ImageResult>> {

    private String query;
    private SearchDataBase searchDataBase;
    private SearchCacheResultAsyncDelegate asyncDelegate;

    public GetCachedResult(String query,
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
    protected List<ImageResult> doInBackground(String... strings) {
        List<ImageResult> imageResultList = searchDataBase.imageResultDao().findImageResultByQuery(query);
        return imageResultList;
    }

    @Override
    protected void onPostExecute(List<ImageResult> imageResults) {
        super.onPostExecute(imageResults);
        List<ImageRvData> imageRvData = new ArrayList<>();
        if (!Utils.isNullOrEmpty(imageResults)) {
            for (ImageResult imageResult : imageResults) {
                Bitmap bitmap = Utils.getBitmapFromByteArray(imageResult.getImage());
                if (bitmap != null) {
                    imageRvData.add(new ImageRvData(bitmap, imageResults.indexOf(imageResult)));
                }
            }
            if (asyncDelegate != null) {
                asyncDelegate.onDataFetchFromCacheComplete(imageRvData);
            }
        } else {
            if (asyncDelegate != null) {
                asyncDelegate.onDataFetchFailed();
            }
        }
    }
}
