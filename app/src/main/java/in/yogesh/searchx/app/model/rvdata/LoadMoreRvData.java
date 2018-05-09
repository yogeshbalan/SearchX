package in.yogesh.searchx.app.model.rvdata;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class LoadMoreRvData implements SearchRvData {
    @Override
    public int getType() {
        return SearchRvData.TYPE_LOADER;
    }
}
