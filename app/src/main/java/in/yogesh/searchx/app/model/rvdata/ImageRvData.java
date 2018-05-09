package in.yogesh.searchx.app.model.rvdata;

import android.graphics.Bitmap;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class ImageRvData implements SearchRvData {

    Bitmap bitmap;
    int position;

    public ImageRvData(Bitmap bitmap, int position) {
        setBitmap(bitmap);
        setPosition(position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int getType() {
        return SearchRvData.TYPE_IMAGE;
    }
}
