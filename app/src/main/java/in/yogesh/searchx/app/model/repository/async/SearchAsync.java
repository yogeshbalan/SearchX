package in.yogesh.searchx.app.model.repository.async;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.yogesh.searchx.app.SearchConstants;
import in.yogesh.searchx.app.model.data.Image;
import in.yogesh.searchx.app.model.repository.interfaces.SearchResultAsyncDelegate;
import in.yogesh.searchx.library.utility.Utils;

/**
 * @author Yogesh Kumar on 9/5/18
 */
public class SearchAsync extends AsyncTask<String, Void, List<Image>> {

    private Customsearch.Builder customSearch;
    private SearchResultAsyncDelegate searchResultAsyncDelegate;
    private long itemId;
    private String query;

    public SearchAsync(SearchResultAsyncDelegate searchResultAsyncDelegate, long itemId, String query) {
        this.searchResultAsyncDelegate = searchResultAsyncDelegate;
        this.itemId = itemId;
        this.query = query;
        this.customSearch = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null);
        this.customSearch.setApplicationName(SearchConstants.APP_NAME);
    }

    @Override
    protected List<Image> doInBackground(String... strings) {

        if (!TextUtils.isEmpty(query)) {
            return fetchImages();
        } else {
            return null;
        }
    }

    private List<Image> fetchImages() {
        try {
            Search results = generateSearchEngine().execute();

            if (results.getItems() != null) {
                return curateImageList(results);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void notifyFail() {
        if (searchResultAsyncDelegate != null)
            searchResultAsyncDelegate.onDataFetchFailed();
    }

    private List<Image> curateImageList(Search results) throws MalformedURLException {
        List<Image> imageList = new ArrayList<>();
        for (Result res : results.getItems()) {
            if (res != null) {
                imageList.add(convertToImage(res));
            }
        }
        return imageList;
    }

    private Image convertToImage(Result res) throws MalformedURLException {
        Rect rect = new Rect();
        rect.contains(100, 100, 100, 100);
        Bitmap bitmap = Utils.decodeSampledBitmapFromResource(new URL(res.getImage().getThumbnailLink()), rect, 100, 100);
        return new Image(res.getTitle(), res.getImage().getThumbnailLink(), bitmap);
    }

    @NonNull
    private Customsearch.Cse.List generateSearchEngine() throws IOException {
        Customsearch.Cse.List list = customSearch.build().cse().list(query);
        list.setKey(SearchConstants.CUSTOM_SEARCH_API_KEY);
        list.setCx(SearchConstants.CUSTOM_SEARCH_ENGINE_KEY);
        list.setStart(itemId);
        list.setSearchType(SearchConstants.CUSTOM_SEARCH_TYPE);
        return list;
    }

    @Override
    protected void onPostExecute(List<Image> images) {
        super.onPostExecute(images);
        if (Utils.isNullOrEmpty(images)) {
            notifyFail();
        } else {
            searchResultAsyncDelegate.onDataFetchComplete(images);
        }
    }
}