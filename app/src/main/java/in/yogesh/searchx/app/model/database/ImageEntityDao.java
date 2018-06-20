package in.yogesh.searchx.app.model.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import in.yogesh.searchx.app.model.data.ImageEntity;

/**
 * @author Yogesh Kumar
 */
@Dao
public interface ImageEntityDao {

    @Insert
    void insertAll(ImageEntity... imageEntities);

    @Delete
    void delete(ImageEntity imageEntity);

    @Query("Select * from ImageEntity Where `Query` in (:query)")
    List<ImageEntity> findImageResultByQuery(String query);

    @Query("Select DISTINCT `Query` from ImageEntity")
    List<String> getAllDistinctQueryFromDb();

}
