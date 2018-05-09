package in.yogesh.searchx.app.model.repository;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.yogesh.searchx.app.model.data.Image;
import in.yogesh.searchx.app.model.data.ImageDbObject;
import in.yogesh.searchx.app.model.database.SearchResultDbHelper;
import in.yogesh.searchx.app.model.repository.async.SaveDataInDBAsync;
import in.yogesh.searchx.app.model.repository.async.SearchAsync;
import in.yogesh.searchx.app.model.repository.interfaces.AsyncDelegate;
import in.yogesh.searchx.app.model.repository.interfaces.LoadMoreListener;
import in.yogesh.searchx.app.model.repository.interfaces.SearchDataFetchListener;
import in.yogesh.searchx.app.model.rvdata.ImageRvData;
import in.yogesh.searchx.library.repository.Repository;
import in.yogesh.searchx.library.utility.Utils;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class SearchRepository extends Repository implements AsyncDelegate {

    private SearchDataFetchListener searchDataFetchListener;
    private LoadMoreListener loadMoreListener;
    private SearchResultDbHelper searchResultDbHelper;
    private List<Image> imageList;
    private List<ImageRvData> imageRvData;
    private SearchAsync searchAsync;
    private SaveDataInDBAsync saveDataInDBAsync;
    private String url;
    private String query;

    private boolean loadMore;
    private long firstItemID = 1;

    public SearchRepository(String url, SearchDataFetchListener searchDataFetchListener, LoadMoreListener loadMoreListener, SearchResultDbHelper searchResultDbHelper) {
        super(url, searchDataFetchListener);
        this.url = url;
        this.searchDataFetchListener = searchDataFetchListener;
        this.loadMoreListener = loadMoreListener;
        this.searchResultDbHelper = searchResultDbHelper;
        imageList = new ArrayList<>();
        imageRvData = new ArrayList<>();
        setLoadMore(false);
    }

    @Override
    public void provideData() {
        if (listener != null) {
            listener.onDataFetchStarted();
            setLoadMore(false);
        }
        fetchFromNetwork(url, new HashMap(1));
    }

    @Override
    protected void fetchFromCache(String url, Map queryMap) {

    }

    @Override
    protected void fetchFromNetwork(String url, Map queryMap) {
        searchAsync = new SearchAsync(this, getFirstItemID(), getQuery());
        searchAsync.execute(getQuery());
    }

    @Override
    public void onDataFetchComplete(List<Image> imageList) {
        int start = (int) getFirstItemID();
        setImageList(imageList);
        if (isLoadMore()) {
            if (loadMoreListener != null) {
                loadMoreListener.loadMoreFinished(start);
            }
        } else {
            if (searchDataFetchListener != null) {
                searchDataFetchListener.onDataFetchedFromNetwork();
                addDataInDb();
            }
        }

    }

    @Override
    public void onDataFetchFailed() {
        if (isLoadMore()) {
            if (loadMoreListener != null) {
                loadMoreListener.loadMoreFailed();
            }
        } else {
            if (searchDataFetchListener != null) {
                searchDataFetchListener.onDataFetchFailed();
            }
        }

    }

    public void FetchDataOnLoadMore(int firstItemID) {
        setFirstItemID(firstItemID);
        setLoadMore(true);
        if (loadMoreListener != null) {
            loadMoreListener.loadMoreStarted();
        }
        fetchFromNetwork(url, new HashMap(1));
    }

    public long getFirstItemID() {
        return firstItemID;
    }

    public void setFirstItemID(long firstItemID) {
        this.firstItemID = firstItemID;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public List<ImageRvData> getImageRvData() {
        if (!Utils.isNullOrEmpty(imageList)) {
            for (Image image : imageList) {
                if (image.getBitmap() != null) {
                    imageRvData.add(new ImageRvData(image.getBitmap(), imageList.indexOf(image)));
                }
            }
            return imageRvData;
        }
        return null;
    }

    public void setImageRvData(List<ImageRvData> imageRvData) {
        this.imageRvData = imageRvData;
    }

    public boolean isLoadMore() {
        return loadMore;
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    public void clearData() {
        if (!Utils.isNullOrEmpty(imageRvData)) imageRvData.clear();
        if (!Utils.isNullOrEmpty(imageList)) imageList.clear();
        setFirstItemID(1);
        setQuery(null);
        if (searchAsync != null) {
            searchAsync.cancel(true);
        }
    }

    public List<String> getQuerySuggestionDataFromCache() {
        List<String> querySuggestionList = searchResultDbHelper.getAllQueryFromDb();
        return (!Utils.isNullOrEmpty(querySuggestionList)) ? querySuggestionList : new ArrayList<String>();
    }


    private void addDataInDb() {
        saveDataInDBAsync = new SaveDataInDBAsync(getImageList(), getQuery(), searchResultDbHelper);
        saveDataInDBAsync.execute();
    }

    public List<ImageRvData> getDataFromCache(String query) {
        List<ImageDbObject> imageDbObjectList = searchResultDbHelper.getImageDbObjectListFromDb(query);
        if (!Utils.isNullOrEmpty(imageDbObjectList)) {
            for (ImageDbObject imageDbObject : imageDbObjectList) {
                if (imageDbObject.getImageBitmap() != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageDbObject.getImageBitmap(), 0, imageDbObject.getImageBitmap().length);
                    imageRvData.add(new ImageRvData(bitmap, imageDbObjectList.indexOf(imageDbObject)));
                }
            }
        }
        return imageRvData;
    }
}
