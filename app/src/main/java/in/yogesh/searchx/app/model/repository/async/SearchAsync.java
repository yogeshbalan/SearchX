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
    private List<Image> imageList;

    public SearchAsync(SearchResultAsyncDelegate searchResultAsyncDelegate, long itemId, String query) {
        this.searchResultAsyncDelegate = searchResultAsyncDelegate;
        this.itemId = itemId;
        this.query = query;
        this.imageList = new ArrayList<>();
        this.customSearch = new Customsearch.Builder(new NetHttpTransport(), new JacksonFactory(), null);
        this.customSearch.setApplicationName(SearchConstants.APP_NAME);
    }

    @Override
    protected List<Image> doInBackground(String... strings) {

        if (!TextUtils.isEmpty(query)) {
            try {
                Customsearch.Cse.List list;
                list = setupCSE();

                Search results = list.execute();

                if (results.getItems() != null) {
                    curateImageList(results);
                    return imageList;
                } else {
                    if (searchResultAsyncDelegate != null)
                        searchResultAsyncDelegate.onDataFetchFailed();

                    return null;
                }

            } catch (IOException e) {
                e.printStackTrace();
                if (searchResultAsyncDelegate != null)
                    searchResultAsyncDelegate.onDataFetchFailed();

            }
        }
        return null;
    }

    private void curateImageList(Search results) throws MalformedURLException {
        for (Result res : results.getItems()) {
            if (res != null) {
                addBitmapToImageList(res);
            }
        }
    }

    private void addBitmapToImageList(Result res) throws MalformedURLException {
        Rect rect = new Rect();
        rect.contains(100, 100, 100, 100);
        Bitmap bitmap = Utils.decodeSampledBitmapFromResource(new URL(res.getImage().getThumbnailLink()), rect, 100, 100);
        imageList.add(new Image(res.getTitle(), res.getImage().getThumbnailLink(), bitmap));
    }

    @NonNull
    private Customsearch.Cse.List setupCSE() throws IOException {
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
        searchResultAsyncDelegate.onDataFetchComplete(images);
    }
}