package in.yogesh.searchx.app.viewmodel;

import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.view.View;

import in.yogesh.searchx.BR;
import in.yogesh.searchx.R;
import in.yogesh.searchx.app.model.rvdata.ImageRvData;
import in.yogesh.searchx.library.recyclerview.ItemViewModel;
import in.yogesh.searchx.library.utility.ResourceUtils;
import in.yogesh.searchx.library.utility.Utils;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class ImageViewModel extends ItemViewModel<ImageRvData> {

    ImageRvData data;
    View.OnClickListener onClickListener;
    String transitionName;
    int cardColor;

    @Override
    public ImageRvData setItem(ImageRvData item) {
        this.data = item;
        setTransitionName(String.valueOf(this.data.getPosition()));
        setUpCardColor();
        notifyChange();
        return data;
    }

    private void setUpCardColor() {
        Palette palette = Utils.getPaletteFromBitmap(data.getBitmap());
        int color;
        int defaultColor = ResourceUtils.getColor(R.color.dark_grey);
        if (palette.getDarkMutedColor(defaultColor) > 0) {
            color = palette.getDarkMutedColor(defaultColor);
        } else if (palette.getDarkVibrantColor(defaultColor) > 0) {
            color = palette.getDarkVibrantColor(defaultColor);
        } else if (palette.getDominantColor(defaultColor) > 0) {
            color = palette.getDominantColor(defaultColor);
        } else {
            color = defaultColor;
        }
        setCardColor(color);
    }

    @Bindable
    public Bitmap getBitmap() {
        return data.getBitmap();
    }

    @Bindable
    public int getWidth() {
        return ResourceUtils.getDimensionPixelSize(200);
    }

    @Bindable
    public int getHeight() {
        return ResourceUtils.getDimensionPixelSize(200);
    }

    @Bindable
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        notifyPropertyChanged(BR.onClickListener);
    }

    @Bindable
    public ImageRvData getData() {
        return data;
    }

    @Bindable
    public String getTransitionName() {
        return transitionName;
    }

    public void setTransitionName(String transitionName) {
        this.transitionName = transitionName;
        notifyPropertyChanged(BR.transitionName);
    }

    @Bindable
    public int getCardColor() {
        return cardColor;
    }

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
        notifyPropertyChanged(BR.cardColor);
    }
}
