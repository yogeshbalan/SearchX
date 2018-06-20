package in.yogesh.searchx.app.model.repository;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.bumptech.glide.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.yogesh.searchx.app.model.data.Image;
import in.yogesh.searchx.app.model.data.ImageEntity;
import in.yogesh.searchx.app.model.database.SearchDataBase;
import in.yogesh.searchx.app.model.repository.async.AddDataAsync;
import in.yogesh.searchx.app.model.repository.async.CacheAsync;
import in.yogesh.searchx.app.model.repository.async.SearchAsync;
import in.yogesh.searchx.app.model.repository.async.SuggestionListAsync;
import in.yogesh.searchx.app.model.repository.interfaces.LoadMoreListener;
import in.yogesh.searchx.app.model.repository.interfaces.SearchCacheResultAsyncDelegate;
import in.yogesh.searchx.app.model.repository.interfaces.SearchDataFetchListener;
import in.yogesh.searchx.app.model.repository.interfaces.SearchResultAsyncDelegate;
import in.yogesh.searchx.app.model.rvdata.ImageRvData;
import in.yogesh.searchx.library.repository.Repository;
import in.yogesh.searchx.library.utility.Utils;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class SearchRepository extends Repository implements SearchResultAsyncDelegate, SearchCacheResultAsyncDelegate {

    private SearchDataFetchListener searchDataFetchListener;
    private LoadMoreListener loadMoreListener;
    private List<Image> imageList;
    private List<ImageRvData> imageRvData;
    private List<String> distinctQueryList;
    private SearchAsync searchAsync;
    private SearchDataBase searchDataBase;
    private String url;
    private String query;

    private boolean loadMore;
    private long firstItemID = 1;

    public SearchRepository(String url, SearchDataFetchListener searchDataFetchListener,
                            LoadMoreListener loadMoreListener, SearchDataBase searchDataBase) {
        super(url, searchDataFetchListener);
        this.url = url;
        this.searchDataFetchListener = searchDataFetchListener;
        this.loadMoreListener = loadMoreListener;
        this.searchDataBase = searchDataBase;
        imageList = new ArrayList<>();
        imageRvData = new ArrayList<>();
        distinctQueryList = new ArrayList<>();
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
            if (loadMoreListener != null)
                loadMoreListener.loadMoreFinished(start);

        } else {
            if (searchDataFetchListener != null)
                searchDataFetchListener.onDataFetchedFromNetwork();
                addDataInDb();

        }

    }

    @Override
    public void onDataFetchFromCacheStarted() {
        if (searchDataFetchListener != null) {
            searchDataFetchListener.onDataFetchStarted();
        }
    }

    @Override
    public void onFetchImagesFromCacheComplete(List<ImageRvData> imageRvData) {
        if (searchDataFetchListener != null) {
            setImageRvData(imageRvData);
            searchDataFetchListener.onDataFetchedFromCache();
        }
    }

    @Override
    public void onFetchQueryListFromCacheComplete(List<String> queryList) {
        if (searchDataFetchListener != null) {
            setDistinctQueryList(queryList);
            searchDataFetchListener.onLocalQueryResultFetched();
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

    public void setDistinctQueryList(List<String> distinctQueryList) {
        this.distinctQueryList = distinctQueryList;
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

    public List<ImageRvData> getImageRvDataFromCache() {
        if (!Utils.isNullOrEmpty(imageRvData)) {
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

    @SuppressLint("StaticFieldLeak")
    public List<String> getQuerySuggestionDataFromCache() {
        SuggestionListAsync listAsync = new SuggestionListAsync(searchDataBase, this);
        listAsync.execute();
        return distinctQueryList;
    }

    @SuppressLint("StaticFieldLeak")
    private void addDataInDb() {
        if (isDataValid()) {
            AddDataAsync addDataAsync = new AddDataAsync(searchDataBase, getImageListForSavingInDatabase());
            addDataAsync.execute();
        }
    }

    private boolean isDataValid() {
        return !Utils.isNullOrEmpty(getImageList()) && getQuery() != null  && searchDataBase.getDatabaseCreated().getValue() != null;
    }

    private ImageEntity[] getImageListForSavingInDatabase() {
        ImageEntity[] imageEntityArray = new ImageEntity[imageList.size()];
        for (Image image : imageList) {
            addImageEntityToList(imageEntityArray, image);
        }
        return imageEntityArray;
    }

    private void addImageEntityToList(ImageEntity[] imageEntityArray, Image image) {
        ImageEntity imageEntity = new ImageEntity(-1, getQuery(), Utils.getBytArrayFromBitmap(image.getBitmap()));
        imageEntityArray[imageList.indexOf(image)] = imageEntity;
    }

    public void getDataFromCache(String query) {
        CacheAsync cacheAsync = new CacheAsync(query, searchDataBase, this);
        cacheAsync.execute(getQuery());
    }
}
