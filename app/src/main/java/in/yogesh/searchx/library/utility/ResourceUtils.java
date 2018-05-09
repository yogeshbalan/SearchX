package in.yogesh.searchx.library.utility;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.support.v4.graphics.ColorUtils;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;

import in.yogesh.searchx.R;

/**
 * @author Yogesh Kumar on 7/5/18
 */
public class ResourceUtils {

    private static Context mContext;

    public static void initialize(Context context) {
        mContext = context;
    }


    public static String getString(int id) {
        return mContext.getResources().getString(id);
    }

    public static Drawable getDrawable(int id) {
        return mContext.getResources().getDrawable(id);
    }

    public static String getString(int id, int number) {
        return mContext.getResources().getString(id, number);
    }

    public static String getString(int id, Object... formatArgs) {
        return mContext.getResources().getString(id, formatArgs);
    }

    public static boolean getBoolean(int id) {
        return mContext.getResources().getBoolean(id);
    }

    public static int getColor(int color) {
        return mContext.getResources().getColor(color);
    }

    public static int getColor(String color) {
        if (TextUtils.isEmpty(color) || mContext == null) return 0;
        switch (color.trim().toLowerCase()) {
            case "white":
                return mContext.getResources().getColor(R.color.color_white);
            default:
                return mContext.getResources().getColor(R.color.text_color_accent);

        }
    }

    public static int getColorWithAlpha(int color, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        return ColorUtils.setAlphaComponent(mContext.getResources().getColor(color), (int) (alpha * 255f));
    }

    public static int getDimensionPixelOffset(int dimen) {
        return mContext.getResources().getDimensionPixelOffset(dimen);
    }

    public static int getDimensionPixelSize(int dimen) {
        return mContext.getResources().getDimensionPixelSize(dimen);
    }

    public static DisplayMetrics getDisplayMetrics() {
        return mContext.getResources().getDisplayMetrics();
    }


    public static float getDimension(int dimen) {
        return mContext.getResources().getDimension(dimen);
    }

    public static int getIdentifier(String name, String defType, String defPackage) {
        return mContext.getResources().getIdentifier(name, defType, defPackage);
    }

    public static Resources getResource() {
        return mContext.getResources();
    }

    public static final AssetManager getAssetManager() {
        return mContext.getAssets();
    }

    public static final ImageSpan getImageSpan(Bitmap bitmap) {
        return new ImageSpan(mContext, bitmap);
    }

    public static final Configuration getConfiguration() {
        return mContext.getResources().getConfiguration();
    }
}
