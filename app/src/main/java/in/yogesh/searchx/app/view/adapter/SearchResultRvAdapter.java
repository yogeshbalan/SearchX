package in.yogesh.searchx.app.view.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.yogesh.searchx.R;
import in.yogesh.searchx.app.model.rvdata.LoadMoreRvData;
import in.yogesh.searchx.app.model.rvdata.SearchRvData;
import in.yogesh.searchx.app.viewmodel.ImageViewModel;
import in.yogesh.searchx.databinding.ItemImageBinding;
import in.yogesh.searchx.library.recyclerview.ItemViewHolder;
import in.yogesh.searchx.library.recyclerview.RecyclerViewAdapter;
import in.yogesh.searchx.library.utility.Utils;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class SearchResultRvAdapter extends RecyclerViewAdapter<SearchRvData> {


    private final ItemInteractionListener itemInteractionListener;

    public SearchResultRvAdapter(ItemInteractionListener itemInteractionListener) {
        this.itemInteractionListener = itemInteractionListener;
    }

    /**
     * return appropriate ViewHolders for view types
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType can be one of {@link SearchRvData}
     *                 {@link SearchRvData#TYPE_IMAGE} for search image result
     *                 {@link SearchRvData#TYPE_LOADER} for load more view
     * @return view holder for corresponding view type
     */
    @Override
    protected ItemViewHolder getViewHolderByType(ViewGroup parent, int viewType) {
        switch (viewType) {
            case SearchRvData.TYPE_IMAGE:
                return getImageViewHolder(parent);
            case SearchRvData.TYPE_LOADER:
                return getLoadMoreViewHolder(parent);
        }
        return null;
    }

    @Override
    public void showLoadMore(int position) {
        if (!Utils.isNullOrEmpty(items)) {
            if (position == LoaderPosition.BOTTOM) {
                items.add(new LoadMoreRvData());
                notifyItemInserted(items.size() - 1);
            } else {
                items.add(1, new LoadMoreRvData());
                notifyItemInserted(1);
            }

        }
    }

    @Override
    public void hideLoadMore(int position) {
        if (!Utils.isNullOrEmpty(items)) {
            if (position == LoaderPosition.BOTTOM) {
                int pos = items.size() - 1;
                items.remove(pos);
                notifyItemRemoved(pos);
            } else {
                items.remove(1);
                notifyItemRemoved(1);
            }
        }
    }

    /**
     * @param parent
     * @return ViewHolder for load more item
     */
    private ItemViewHolder getLoadMoreViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loader, parent, false);
        return new ItemViewHolder(itemView, null, null);

    }

    /**
     * @param parent
     * @return ViewHolder for image Item
     */
    private ItemViewHolder getImageViewHolder(ViewGroup parent) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        final ImageViewModel imageViewModel = new ImageViewModel();
        imageViewModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemInteractionListener.onImageClicked(imageViewModel.getBitmap(), itemView, imageViewModel.getData().getPosition());
            }
        });
        ItemImageBinding itemImageBinding = ItemImageBinding.bind(itemView);
        itemImageBinding.setViewModel(imageViewModel);
        return new ItemViewHolder(itemView, itemImageBinding, imageViewModel);
    }

    public interface ItemInteractionListener {
        void onImageClicked(Bitmap bitmap, View view, int adapterPosition);
    }

}
