package in.yogesh.searchx.library.recyclerview;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import in.yogesh.searchx.library.ViewModel;

/**
 * @author Yogesh Kumar on 1/5/18
 */

public abstract class RecyclerViewViewModel<T extends RecyclerViewAdapter> extends ViewModel implements RecyclerViewViewModelInterface {

    RecyclerView.LayoutManager layoutManager;
    private Parcelable savedLayoutManagerState;

    public RecyclerViewViewModel() {
    }

    public RecyclerViewViewModel(@Nullable State savedInstanceState) {
        super(savedInstanceState);
        if (savedInstanceState instanceof RecyclerViewViewModelState) {
            savedLayoutManagerState =
                    ((RecyclerViewViewModelState) savedInstanceState).layoutManagerState;
        }
    }

    public abstract T getAdapter();

    protected abstract <LM extends RecyclerView.LayoutManager> LM createLayoutManager(Context context);

    public abstract RecyclerView.OnScrollListener getScrollListenerForLoadMore();

    @Override
    public RecyclerViewViewModelState getInstanceState() {
        return new RecyclerViewViewModelState(this);
    }

    @CallSuper
    @Override
    public void setupRecyclerView(RecyclerView recyclerView) {
        layoutManager = createLayoutManager(recyclerView.getContext());
        if (savedLayoutManagerState != null) {
            layoutManager.onRestoreInstanceState(savedLayoutManagerState);
            savedLayoutManagerState = null;
        }
        recyclerView.setAdapter(getAdapter());
        recyclerView.setLayoutManager(layoutManager);
        if (getScrollListenerForLoadMore() != null) {
            recyclerView.addOnScrollListener(getScrollListenerForLoadMore());
        }
    }

    protected static class RecyclerViewViewModelState extends State {

        public static Parcelable.Creator<RecyclerViewViewModelState> CREATOR =
                new Parcelable.Creator<RecyclerViewViewModelState>() {
                    @Override
                    public RecyclerViewViewModelState createFromParcel(Parcel source) {
                        return new RecyclerViewViewModelState(source);
                    }

                    @Override
                    public RecyclerViewViewModelState[] newArray(int size) {
                        return new RecyclerViewViewModelState[size];
                    }
                };
        private Parcelable layoutManagerState;

        public RecyclerViewViewModelState(RecyclerViewViewModel viewModel) {
            super(viewModel);
            if (viewModel.layoutManager == null) return;
            layoutManagerState = viewModel.layoutManager.onSaveInstanceState();
        }

        public RecyclerViewViewModelState(Parcel in) {
            super(in);
            layoutManagerState = in.readParcelable(
                    RecyclerView.LayoutManager.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(layoutManagerState, flags);
        }
    }
}
