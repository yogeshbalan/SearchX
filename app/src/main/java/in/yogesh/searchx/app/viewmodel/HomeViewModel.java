package in.yogesh.searchx.app.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.List;

import in.yogesh.searchx.BR;
import in.yogesh.searchx.R;
import in.yogesh.searchx.app.model.database.SearchDataBase;
import in.yogesh.searchx.app.model.repository.SearchRepository;
import in.yogesh.searchx.app.model.repository.interfaces.LoadMoreListener;
import in.yogesh.searchx.app.model.repository.interfaces.SearchDataFetchListener;
import in.yogesh.searchx.app.model.rvdata.ImageRvData;
import in.yogesh.searchx.app.view.activity.ImageDetailActivity;
import in.yogesh.searchx.app.view.adapter.SearchResultRvAdapter;
import in.yogesh.searchx.app.viewmodel.interfaces.ViewInteractionListener;
import in.yogesh.searchx.app.viewmodel.interfaces.ViewModelToActivityCommunicator;
import in.yogesh.searchx.library.recyclerview.RecyclerViewViewModel;
import in.yogesh.searchx.library.utility.Utils;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class HomeViewModel extends RecyclerViewViewModel<SearchResultRvAdapter> implements SearchResultRvAdapter.ItemInteractionListener {

    private SearchRepository repository;
    private SearchDataFetchListener searchDataFetchListener;
    private LoadMoreListener loadMoreListener;
    private ViewInteractionListener viewInteractionListener;
    private ViewModelToActivityCommunicator activityCommunicator;
    private SearchResultRvAdapter resultRvAdapter;

    private TextWatcher textWatcher;
    private ArrayAdapter<String> stringArrayAdapter;
    private View.OnClickListener doneButtonClickListener;
    private View.OnClickListener cancelButtonClickListener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private boolean crossButtonVisibility;
    private boolean doneButtonVisibility;
    private boolean clearEditTextText;
    private boolean showProgress;
    private boolean showLoadMoreProgress;
    private boolean error;
    private boolean showDropDown;
    private boolean loadMoreAllowed;
    private String query;
    private int gridSpan;

    public HomeViewModel(final ViewInteractionListener viewInteractionListener,
                         ViewModelToActivityCommunicator viewModelToActivityCommunicator,
                         SearchDataBase searchDataBase) {

        setSearchDataFetchListener();
        setLoadMoreListener();
        setViewInteractionListener(viewInteractionListener);
        setActivityCommunicator(viewModelToActivityCommunicator);
        setSearchRepository(searchDataBase);
        setStringArrayAdapter(getAdapterDataFromRepository());
        setTextWatcher(getEditTextTextWatcher());
        setError(true);
        setShowLoadMoreProgress(false);
        setShowDropDown(false);
        setLoadMoreAllowed(true);
        setGridSpan(2);
        setDoneButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateRv();
                setLoadMoreAllowed(true);
                viewInteractionListener.hideKeyboard();
            }
        });
        setCancelButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClearEditTextText(true);
                setCrossButtonVisibility(false);
                setDoneButtonVisibility(false);
                setShowLoadMoreProgress(false);
                setShowProgress(false);
                setError(true);
                //setShowDropDown(true);

                if (getAdapter() != null) {
                    getAdapter().clearData();
                    repository.clearData();
                }


            }
        });
        setOnItemClickListener((parent, view, position, id) -> {
            repository.getDataFromCache(((AppCompatTextView) view).getText().toString());
            viewInteractionListener.hideKeyboard();
            setLoadMoreAllowed(false);

        });
    }

    private ArrayAdapter<String> getAdapterDataFromRepository() {
        return new ArrayAdapter<>(activityCommunicator.getContext(),
                R.layout.item_text,
                repository.getQuerySuggestionDataFromCache());
    }

    private void populateRv() {
        if (getActivityCommunicator().isNetworkAvailable()) {
            repository.setFirstItemID(1);
            repository.setQuery(query);
            repository.provideData();
        } else {
            setError(true);
        }
    }

    private void populateRvOnLoadMore(int itemId) {
        repository.setFirstItemID(itemId);
        repository.FetchDataOnLoadMore(resultRvAdapter.getItemCount() + 1);
    }


    private void setViewInteractionListener(ViewInteractionListener viewInteractionListener) {
        this.viewInteractionListener = viewInteractionListener;
    }

    private void setSearchRepository(SearchDataBase searchDataBase) {
        if (searchDataFetchListener != null)
            this.repository = new SearchRepository("", getSearchDataFetchListener(), getLoadMoreListener(), searchDataBase);
    }

    private SearchDataFetchListener getSearchDataFetchListener() {
        return searchDataFetchListener;
    }

    private LoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    private void setLoadMoreListener() {
        this.loadMoreListener = new LoadMoreListener() {
            @Override
            public void loadMoreStarted() {
                setError(false);
                setShowLoadMoreProgress(true);
            }

            @Override
            public void loadMoreFinished(int startedFrom) {
                setShowLoadMoreProgress(false);
                setError(false);
                if (!Utils.isNullOrEmpty(repository.getImageRvData())) {
                    getAdapter().addDataList(repository.getImageRvData());
                } else {
                    setError(true);
                }


            }

            @Override
            public void loadMoreFailed() {
                setShowLoadMoreProgress(false);
                setError(true);
            }
        };
    }

    private void setSearchDataFetchListener() {
        this.searchDataFetchListener = new SearchDataFetchListener() {

            @Override
            public void onLocalQueryResultFetched() {
                setStringArrayAdapter(getAdapterDataFromRepository());
            }

            @Override
            public void onDataFetchedFromNetwork() {
                setShowProgress(false);
                setError(false);
                if (!Utils.isNullOrEmpty(repository.getImageRvData())) {
                    getAdapter().addDataList(repository.getImageRvData());
                    setStringArrayAdapter(getAdapterDataFromRepository());
                    setShowDropDown(false);
                } else {
                    setError(true);
                }

            }

            @Override
            public void onDataFetchedFromCache() {
                setShowProgress(false);
                setError(false);
                if (!Utils.isNullOrEmpty(repository.getImageRvDataFromCache())) {
                    getAdapter().addDataList(repository.getImageRvDataFromCache());
                    setShowDropDown(false);
                } else {
                    setError(true);
                }
            }

            @Override
            public void onDataFetchStarted() {
                setError(false);
                setShowProgress(true);
            }

            @Override
            public void onDataFetchFailed() {
                setShowProgress(false);
                setError(true);
            }
        };
    }

    @Bindable
    public ArrayAdapter<String> getStringArrayAdapter() {
        return stringArrayAdapter;
    }

    public void setStringArrayAdapter(ArrayAdapter<String> stringArrayAdapter) {
        this.stringArrayAdapter = stringArrayAdapter;
        notifyPropertyChanged(BR.stringArrayAdapter);
    }

    @Bindable
    public TextWatcher getTextWatcher() {
        return textWatcher;
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
        notifyPropertyChanged(BR.textWatcher);
    }


    private TextWatcher getEditTextTextWatcher() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                query = s.toString();
                setRightActionVisibilityAccordingQueryLength(s.toString());
            }
        };
    }

    private void setRightActionVisibilityAccordingQueryLength(String s) {
        setCrossButtonVisibility(s.length() > 0);
        setDoneButtonVisibility(s.length() > 0);
    }

    @Bindable
    public boolean isCrossButtonVisibility() {
        return crossButtonVisibility;
    }

    public void setCrossButtonVisibility(boolean crossButtonVisibility) {
        this.crossButtonVisibility = crossButtonVisibility;
        notifyPropertyChanged(BR.crossButtonVisibility);
    }

    @Bindable
    public boolean isDoneButtonVisibility() {
        return doneButtonVisibility;
    }

    public void setDoneButtonVisibility(boolean doneButtonVisibility) {
        this.doneButtonVisibility = doneButtonVisibility;
        notifyPropertyChanged(BR.doneButtonVisibility);
    }

    @Bindable
    public View.OnClickListener getDoneButtonClickListener() {
        return doneButtonClickListener;
    }

    public void setDoneButtonClickListener(View.OnClickListener doneButtonClickListener) {
        this.doneButtonClickListener = doneButtonClickListener;
        notifyPropertyChanged(BR.doneButtonClickListener);
    }

    @Bindable
    public View.OnClickListener getCancelButtonClickListener() {
        return cancelButtonClickListener;
    }

    public void setCancelButtonClickListener(View.OnClickListener cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
        notifyPropertyChanged(BR.cancelButtonClickListener);
    }

    @Bindable
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyPropertyChanged(BR.onItemClickListener);
    }

    @Bindable
    public boolean isClearEditTextText() {
        return clearEditTextText;
    }

    public void setClearEditTextText(boolean clearEditTextText) {
        this.clearEditTextText = clearEditTextText;
        notifyPropertyChanged(BR.clearEditTextText);
    }

    @Bindable
    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        notifyPropertyChanged(BR.showProgress);
    }

    @Bindable
    public boolean isShowLoadMoreProgress() {
        return showLoadMoreProgress;
    }

    public void setShowLoadMoreProgress(boolean showLoadMoreProgress) {
        this.showLoadMoreProgress = showLoadMoreProgress;
        notifyPropertyChanged(BR.showLoadMoreProgress);
    }

    @Bindable
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
        notifyPropertyChanged(BR.error);
    }

    @Bindable
    public boolean isShowDropDown() {
        return showDropDown;
    }

    public void setShowDropDown(boolean showDropDown) {
        this.showDropDown = showDropDown;
        notifyPropertyChanged(BR.showDropDown);
    }

    public boolean isLoadMoreAllowed() {
        return loadMoreAllowed;
    }

    public void setLoadMoreAllowed(boolean loadMoreAllowed) {
        this.loadMoreAllowed = loadMoreAllowed;
    }

    private ViewModelToActivityCommunicator getActivityCommunicator() {
        return activityCommunicator;
    }

    private void setActivityCommunicator(ViewModelToActivityCommunicator activityCommunicator) {
        this.activityCommunicator = activityCommunicator;
    }

    private int getGridSpan() {
        return gridSpan;
    }

    public void setGridSpan(int gridSpan) {
        this.gridSpan = gridSpan;
    }

    @Override
    public SearchResultRvAdapter getAdapter() {
        if (resultRvAdapter == null) {
            resultRvAdapter = new SearchResultRvAdapter(this);
        }
        return resultRvAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager(Context context) {
        return new GridLayoutManager(context, getGridSpan());
    }

    @Override
    public RecyclerView.OnScrollListener getScrollListenerForLoadMore() {
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isError() && isLoadMoreAllowed() && totalItemCount <= (lastVisibleItem + 1) && getActivityCommunicator().isNetworkAvailable()) {
                    /**
                     * loadMore should not be called in the same frame as it tries to add item to rv and change its state, and Scroll callbacks might be run during a measure &
                     * layout pass where you cannot change the RecyclerView data. Any method call that might change the structure of the RecyclerView or the adapter contents
                     * should be postponed to the next frame.
                     */
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            populateRvOnLoadMore(resultRvAdapter.getItemCount() + 1);
                        }
                    });
                }
            }
        };
        return scrollListener;
    }

    @Override
    public void onImageClicked(Bitmap bitmap, View view, int adapterPosition) {
        AppCompatImageView imageView = view.findViewById(R.id.card_image);
        String transitionName = ViewCompat.getTransitionName(imageView);
        ImageDetailActivity.start(activityCommunicator.getContext(), imageView, bitmap, transitionName);
    }
}
