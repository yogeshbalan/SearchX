package in.yogesh.searchx.app.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import in.yogesh.searchx.app.model.data.Image;
import in.yogesh.searchx.app.model.data.ImageDbObject;

/**
 * @author Yogesh Kumar on 9/5/18
 */
public class SearchResultDbHelper extends SQLiteOpenHelper {

    private static final int DATA_BASE_VERSION = 1;
    private static final String DATA_BASE_NAME = "SearchResultDb";
    private static final String TABLE_NAME = "SearchResultTable";
    // Search Result Table Columns names
    private static final String COL_ID = "col_id";
    private static final String QUERY = "query";
    private static final String IMAGE_BITMAPS = "image_bitmaps";
    private Context context;

    public SearchResultDbHelper(Context context) {
        super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY ,"
                + QUERY + " TEXT,"
                + IMAGE_BITMAPS + " TEXT )";
        db.execSQL(CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertSearchResult(List<Image> images, String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (Image image : images) {
            values.put(QUERY, query);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = image.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            values.put(IMAGE_BITMAPS, stream.toByteArray());
            db.insert(TABLE_NAME, null, values);
        }
        db.close();
    }

    public ImageDbObject retrieveSearchResult(String query) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor2 = db.query(TABLE_NAME,
                new String[]{COL_ID, QUERY, IMAGE_BITMAPS}, QUERY
                        + " LIKE '" + query + "%'", null, null, null, null);

        ImageDbObject imageDbObject = new ImageDbObject();

        if (cursor2.moveToFirst()) {
            do {
                imageDbObject.setQueryName(cursor2.getString(1));
                imageDbObject.setImageBitmap(cursor2.getBlob(2));
            } while (cursor2.moveToNext());
        }

        cursor2.close();
        db.close();
        return imageDbObject;
    }

    public List<ImageDbObject> getImageDbObjectListFromDb(String query) {
        List<ImageDbObject> imageDbObjectList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor2 = db.query(TABLE_NAME,
                new String[]{COL_ID, QUERY, IMAGE_BITMAPS},
                QUERY
                        + " LIKE '" + query + "%'",
                null,
                null,
                null,
                null);

        ImageDbObject imageDbObject = new ImageDbObject();

        if (cursor2.moveToFirst()) {
            while (!cursor2.isAfterLast()) {
                imageDbObject.setImageBitmap(cursor2.getBlob(cursor2.getColumnIndex(IMAGE_BITMAPS)));
                imageDbObjectList.add(imageDbObject);
                cursor2.moveToNext();
            }
        }

        cursor2.close();
        db.close();
        return imageDbObjectList;

    }

    public List<String> getAllQueryFromDb() {
        List<String> queryList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor1 = db.query(true, TABLE_NAME,
                new String[]{QUERY},
                null,
                null,
                QUERY,
                null,
                null,
                null);

        if (cursor1.moveToFirst()) {
            do {
                queryList.add(cursor1.getString(cursor1.getColumnIndex(QUERY)));
            } while (cursor1.moveToNext());
        }

        cursor1.close();
        db.close();
        return queryList;

    }

}
