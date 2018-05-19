package in.yogesh.searchx.app.model.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import in.yogesh.searchx.app.model.data.ImageResult;

/**
 * @author Yogesh Kumar
 */
@Dao
public interface ImageResultDao {

    @Insert
    void insertAll(ImageResult... imageResults);

    @Delete
    void delete(ImageResult imageResult);

    @Query("Select * from ImageResult Where `Query` in (:query)")
    List<ImageResult> findImageResultByQuery(String query);

    @Query("Select DISTINCT `Query` from ImageResult")
    List<String> getAllDistinctQueryFromDb();

}
