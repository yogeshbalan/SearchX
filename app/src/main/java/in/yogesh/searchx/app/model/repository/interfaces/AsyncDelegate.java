package in.yogesh.searchx.app.model.repository.interfaces;

import java.util.List;

import in.yogesh.searchx.app.model.data.Image;

public interface AsyncDelegate {
    void onDataFetchComplete(List<Image> imageList);

    void onDataFetchFailed();
}