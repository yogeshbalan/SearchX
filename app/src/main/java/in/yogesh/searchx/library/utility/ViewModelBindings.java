package in.yogesh.searchx.library.utility;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import in.yogesh.searchx.library.recyclerview.RecyclerViewViewModel;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class ViewModelBindings {

    @BindingAdapter("setSearchTextWatcher")
    public static void setTWOnEditText(final AppCompatEditText appCompatEditText,
                                       TextWatcher textWatcher) {
        appCompatEditText.addTextChangedListener(textWatcher);
    }

    @BindingAdapter("setSearchTextWatcher")
    public static void setTWOnAutoCompleteTextView(final AutoCompleteTextView autoCompleteTextView,
                                                   TextWatcher textWatcher) {
        autoCompleteTextView.addTextChangedListener(textWatcher);
    }

    @BindingAdapter("visibility")
    public static void setVisibility(final View view,
                                     boolean visibility) {
        view.setVisibility((visibility) ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("setClearText")
    public static void setClearText(final AppCompatEditText appCompatEditText,
                                    boolean clear) {
        appCompatEditText.setText((clear) ? "" : "");
    }

    @BindingAdapter("showDropDown")
    public static void showDropDown(final AutoCompleteTextView autoCompleteTextView,
                                    boolean showDropDown) {
        if (showDropDown) {
            autoCompleteTextView.showDropDown();
        }
    }

    @BindingAdapter("setOnItemClickListener")
    public static void setOnItemClickListener(final AutoCompleteTextView autoCompleteTextView,
                                              AdapterView.OnItemClickListener onItemClickListener) {
        autoCompleteTextView.setOnItemClickListener(onItemClickListener);
    }

    @BindingAdapter("setClearText")
    public static void setClearText(final AutoCompleteTextView autoCompleteTextView,
                                    boolean clear) {
        autoCompleteTextView.setText((clear) ? "" : "");
    }

    @BindingAdapter("setAdapter")
    public static void setArrayAdapter(final AutoCompleteTextView autoCompleteTextView,
                                       ArrayAdapter<String> adapter) {
        autoCompleteTextView.setAdapter(adapter);
    }

    @BindingAdapter("setBitmap")
    public static void setBitmap(final AppCompatImageView appCompatImageView,
                                 Bitmap bitmap) {
        if (bitmap != null) {
            appCompatImageView.setImageBitmap(bitmap);
        }
    }

    @BindingAdapter({"setHeight"})
    public static void setViewHeight(View view, int height) {
        Utils.setLayoutHeight(view, height);
    }

    @BindingAdapter({"setWidth"})
    public static void setViewWidth(View view, int width) {
        Utils.setWidth(view, width);
    }

    @BindingAdapter("recyclerViewViewModel")
    public static void setRecyclerViewViewModel(RecyclerView recyclerView,
                                                RecyclerViewViewModel viewModel) {
        viewModel.setupRecyclerView(recyclerView);
    }


}
