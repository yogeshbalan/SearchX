package in.yogesh.searchx.library.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * @author Yogesh Kumar on 7/5/18
 */
public class Utils {

    /**
     * Checks whether a list is empty and null or not
     *
     * @param list
     * @return
     */
    public static boolean isNullOrEmpty(List list) {
        if (list != null && !list.isEmpty())
            return false;
        else
            return true;
    }

    /**
     * Checks internet connectivity which is used before making any network call
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null) && activeNetworkInfo.isConnected();
    }

    /**
     * Utility method for hiding keyboard
     *
     * @param act
     */
    public static void hideKeyboard(Activity act) {
        if (act != null && act.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isAcceptingText()) {
                inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    /**
     * Utility method for showing keyboard
     *
     * @param act
     */
    public static void showKeyboard(Activity act) {
        if (act != null && act.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, 0);
        }
    }

    /**
     * Used to generate a bitmap by fetching it from
     *
     * @param url       and the compressing it to save memory space
     * @param url
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    @Nullable
    public static Bitmap decodeSampledBitmapFromResource(URL url, Rect resId, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(url.openConnection().getInputStream(), resId, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            options.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream(), resId, options);
            return bmp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Helper method to calculate sample bitmap size
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Binder method helper to set view height
     *
     * @param view
     * @param height
     */
    public static void setLayoutHeight(View view, int height) {
        if (view == null)
            return;

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null && layoutParams.height != height) {
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    /**
     * Binder method helper to set view width
     *
     * @param view
     * @param width
     */
    public static void setWidth(View view, float width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams((int) width, view.getHeight());
        } else {
            params.width = (int) width;
        }
        view.setLayoutParams(params);
    }

    /**
     * Returns palette from given bitmap
     *
     * @param bitmap
     * @return
     */
    public static Palette getPaletteFromBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            Palette palette = Palette.from(bitmap).generate();
            return palette;
        }
        return null;
    }

}
