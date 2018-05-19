package in.yogesh.searchx.app.model.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * @author Yogesh Kumar
 */
@Entity
public class ImageResult {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    int id;

    @ColumnInfo(name = "Query")
    String query;

    @ColumnInfo(name = "Image", typeAffinity = ColumnInfo.BLOB)
    byte[] image;

    public static Date now;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ImageResult(int id, String query, byte[] image) {
        this.query = query;
        this.image = image;
    }

    public ImageResult() {
    }

}
