package in.yogesh.searchx.app.model.repository.interfaces;

public interface LoadMoreListener {

    void loadMoreStarted();

    void loadMoreFinished(int startedFrom);

    void loadMoreFailed();
}