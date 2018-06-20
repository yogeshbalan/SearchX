package in.yogesh.searchx.app.model.repository.interfaces;

import java.util.List;

import in.yogesh.searchx.app.model.rvdata.ImageRvData;

/**
 * @author Yogesh Kumar
 */
public interface SearchCacheResultAsyncDelegate {

    void onDataFetchFromCacheStarted();

    void onFetchImagesFromCacheComplete(List<ImageRvData> imageRvData);

    void onFetchQueryListFromCacheComplete(List<String> queryList);

    void onDataFetchFailed();

}
