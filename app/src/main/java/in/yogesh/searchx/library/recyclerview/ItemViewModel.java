package in.yogesh.searchx.library.recyclerview;


import in.yogesh.searchx.library.ViewModel;

/**
 * @author Yogesh Kumar on 1/5/18
 */

public abstract class ItemViewModel<T> extends ViewModel implements ItemViewModelInterface<T> {
    public ItemViewModel() {
        super(null);
    }
}
