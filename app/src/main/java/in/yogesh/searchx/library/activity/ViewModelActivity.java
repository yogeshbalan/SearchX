package in.yogesh.searchx.library.activity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import in.yogesh.searchx.library.ViewModel;

/**
 * @author Yogesh Kumar on 7/5/18
 */
public abstract class ViewModelActivity<B extends ViewDataBinding, V extends ViewModel> extends AppCompatActivity {

    private static final String EXTRA_VIEW_MODEL_STATE = "viewModelState";
    private B viewDataBinding;
    private V viewModel;

    /**
     * Create view model and no need to keep a reference. Parent will do it for you.
     *
     * @param savedInstanceState
     */
    @NonNull
    protected abstract V createViewModel(Bundle savedInstanceState);

    /**
     * Inflate and get ViewBindingObject and return it.
     */
    @NonNull
    protected abstract B createBindingClass();


    /**
     * reflection dependant
     */
    @NonNull
    protected abstract void setViewModelToBinding();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        /**
         * Kindly DO NOT alter the sequence of the following commands.
         */
        super.onCreate(savedInstanceState);
        viewDataBinding = createBindingClass();
        viewModel = createViewModel(savedInstanceState);
        setViewModelToBinding();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (viewModel != null) {
            viewModel.onStart();
        }
    }

    @Override
    public void onStop() {
        if (viewModel != null) {
            viewModel.onStop();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        if (viewModel != null) {
            viewModel.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (viewModel != null) {
            viewModel.onDestroy();
        }
        super.onDestroy();
    }

    public V getViewModel() {
        return viewModel;
    }

    public void setViewModel(V viewModel) {
        this.viewModel = viewModel;
    }

    public B getViewDataBinding() {
        return viewDataBinding;
    }

    public void setViewDataBinding(B viewDataBinding) {
        this.viewDataBinding = viewDataBinding;
    }

}
