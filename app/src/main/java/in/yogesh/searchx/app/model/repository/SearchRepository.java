package in.yogesh.searchx.app.model.repository;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.yogesh.searchx.app.model.data.Image;
import in.yogesh.searchx.app.model.data.ImageResult;
import in.yogesh.searchx.app.model.database.SearchDataBase;
import in.yogesh.searchx.app.model.repository.async.GetCachedResult;
import in.yogesh.searchx.app.model.repository.async.SearchAsync;
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
    private List<String> distinctQueryResultList;
    private SearchAsync searchAsync;
    private String url;
    private String query;
    private SearchDataBase searchDataBase;

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
        distinctQueryResultList = new ArrayList<>();
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
    public void onDataFetchFromCacheStarted() {
        if (searchDataFetchListener != null) {
            searchDataFetchListener.onDataFetchStarted();
        }
    }

    @Override
    public void onDataFetchFromCacheComplete(List<ImageRvData> imageRvData) {
        if (searchDataFetchListener != null) {
            setImageRvData(imageRvData);
            searchDataFetchListener.onDataFetchedFromCache();
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
        new AsyncTask<String, Void, List<String>>() {

            @Override
            protected List<String> doInBackground(String... strings) {
                return searchDataBase.imageResultDao().getAllDistinctQueryFromDb();
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                distinctQueryResultList = strings;
                if (searchDataFetchListener != null) {
                    searchDataFetchListener.onLocalQueryResultFetched();
                }
                return;
            }
        }.execute();
        return distinctQueryResultList;
    }

    @SuppressLint("StaticFieldLeak")
    private void addDataInDb() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (!Utils.isNullOrEmpty(getImageList()) && getQuery() != null  && searchDataBase.getDatabaseCreated().getValue() != null) {
                    if (searchDataBase.getDatabaseCreated().getValue() != null) {
                        searchDataBase.imageResultDao().insertAll(getImageListForSavingInDatabase());
                    }
                }
                return null;
            }
        }.execute();

    }

    private ImageResult[] getImageListForSavingInDatabase() {
        ImageResult[] imageResultArray = new ImageResult[imageList.size()];
        for (Image image : imageList) {
            ImageResult imageResult = new ImageResult(-1, getQuery(), Utils.getBytArrayFromBitmap(image.getBitmap()));
            imageResultArray[imageList.indexOf(image)] = imageResult;
        }
        return imageResultArray;
    }

    public void getDataFromCache(String query) {
        GetCachedResult getCachedResult = new GetCachedResult(query, searchDataBase, this);
        getCachedResult.execute(getQuery());
    }
}
