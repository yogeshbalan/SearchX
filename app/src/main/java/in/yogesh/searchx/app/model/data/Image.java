package in.yogesh.searchx.app.model.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Yogesh Kumar on 8/5/18
 */
public class Image implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
    private String title;
    private String url;
    private Bitmap bitmap;

    public Image(String title, String url, Bitmap bitmap) {
        this.title = title;
        this.url = url;
        this.bitmap = bitmap;
    }

    protected Image(Parcel in) {
        title = in.readString();
        url = in.readString();
        bitmap = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(url);
        dest.writeValue(bitmap);
    }
}
