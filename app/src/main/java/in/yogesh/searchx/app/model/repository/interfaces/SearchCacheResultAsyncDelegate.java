package in.yogesh.searchx.app.model.repository.interfaces;

import java.util.List;

import in.yogesh.searchx.app.model.rvdata.ImageRvData;

/**
 * @author Yogesh Kumar
 */
public interface SearchCacheResultAsyncDelegate {

    void onDataFetchFromCacheStarted();

    void onDataFetchFromCacheComplete(List<ImageRvData> imageRvData);

    void onDataFetchFailed();

}
