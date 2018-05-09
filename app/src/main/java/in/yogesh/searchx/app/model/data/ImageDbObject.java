package in.yogesh.searchx.app.model.data;

/**
 * @author Yogesh Kumar on 9/5/18
 */
public class ImageDbObject {

    private String queryName;

    private byte[] imageBitmap;

    public ImageDbObject(String queryName, byte[] imageBitmapy) {
        this.queryName = queryName;
        this.imageBitmap = imageBitmapy;
    }

    public ImageDbObject() {
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public byte[] getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(byte[] imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
