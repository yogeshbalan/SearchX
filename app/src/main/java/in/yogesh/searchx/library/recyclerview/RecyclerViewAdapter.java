package in.yogesh.searchx.library.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yogesh Kumar on 1/5/18
 */

public abstract class RecyclerViewAdapter<T extends RecyclerViewDataInterface> extends RecyclerView.Adapter<ItemViewHolder> {
    protected final ArrayList<T> items;

    public RecyclerViewAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolderByType(parent, viewType);
    }

    protected abstract ItemViewHolder getViewHolderByType(ViewGroup parent, int viewType);

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override #onBindViewHolder(ViewHolder, int) instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.setItem(items.get(position));
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    public final ArrayList<T> getItems() {
        return items;
    }

    public void setItems(ArrayList<? extends T> newItems) {
        setData(newItems);
    }

    /**
     * Remove the item at the specified position in recyclerViewData
     */
    public void removeItem(int position) {
        if (items == null) {
            return;
        }
        items.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Remove the specified item in recyclerViewData
     */
    public void removeItem(T data) {
        if (items == null) {
            return;
        }
        int position = items.indexOf(data);
        if (position >= 0) {
            removeItem(position);
        }
    }

    public void clearData() {
        if (items == null) {
            return;
        }
        int size = items.size();
        items.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addSingleData(T data) {
        if (items == null) {
            return;
        }
        items.add(data);
        notifyItemInserted(items.size() - 1);
    }

    /**
     * Add a CustomRecyclerViewData in items in the specified position
     */
    public void addSingleData(int position, T data) {
        if (items == null) {
            return;
        }
        items.add(position, data);
        notifyItemInserted(position);
    }

    public void setData(List<? extends T> data) {
        if (items == null) {
            return;
        }
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void addDataList(List<? extends T> data) {
        if (items == null || data == null) {
            return;
        }
        int initialSize = items.size();
        items.addAll(data);
        notifyItemRangeInserted(initialSize, data.size());
    }

    public void showLoadMore(@LoaderPosition int position) {

    }

    public void hideLoadMore(@LoaderPosition int position) {
    }

    public @interface LoaderPosition {
        int TOP = 1;
        int BOTTOM = 2;
    }
}
