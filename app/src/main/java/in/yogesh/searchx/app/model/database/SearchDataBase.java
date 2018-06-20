package in.yogesh.searchx.app.model.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import in.yogesh.searchx.app.AppExecutor;
import in.yogesh.searchx.app.SearchConstants;
import in.yogesh.searchx.app.model.data.ImageEntity;

/**
 * @author Yogesh Kumar
 */
@Database(entities = {ImageEntity.class}, version = 1)
public abstract class SearchDataBase extends RoomDatabase {

    public abstract ImageEntityDao imageEntityDao();

    private static final String DATABASE_NAME = SearchConstants.DATABASE_NAME;
    private static SearchDataBase instance;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static SearchDataBase getInstance(final Context context, final AppExecutor executors) {
        if (instance == null) {
            synchronized (SearchDataBase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext(), executors);
                    instance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static SearchDataBase buildDatabase(final Context appContext,
                                                final AppExecutor executors) {
        return Room.databaseBuilder(appContext, SearchDataBase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.getDiskIO().execute(() -> {
                            // Add a delay to simulate a long-running operation
                            addDelay();
                            // Generate the data for pre-population
                            SearchDataBase database = SearchDataBase.getInstance(appContext, executors);

                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                }).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
