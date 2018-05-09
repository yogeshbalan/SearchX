package in.yogesh.searchx.library.recyclerview;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Yogesh Kumar on 1/5/18
 */

public class ItemViewHolder<T, VT extends ItemViewModelInterface<T>>
        extends RecyclerView.ViewHolder {

    protected final VT viewModel;
    protected ViewDataBinding binding;

    public ItemViewHolder(View view, ViewDataBinding binding, VT viewModel) {
        super(view);
        this.binding = binding;
        this.viewModel = viewModel;
    }

    public ItemViewHolder(ViewDataBinding binding, VT viewModel) {
        this(binding.getRoot(), binding, viewModel);
    }

    public ItemViewHolder(View rootView, VT viewModel) {
        super(rootView);
        this.viewModel = viewModel;
    }

    public void setItem(T item) {
        if (viewModel != null) {
            viewModel.setItem(item);
        }

        if (binding != null) {
            binding.executePendingBindings();
        }
    }

    protected ViewDataBinding getBinding() {
        return binding;
    }

    protected VT getViewModel() {
        return viewModel;
    }

}
