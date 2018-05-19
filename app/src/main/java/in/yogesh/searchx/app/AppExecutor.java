package in.yogesh.searchx.app;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Yogesh Kumar
 */
public class AppExecutor {

    public final Executor diskIO;


    public AppExecutor(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public AppExecutor() {
        this(Executors.newSingleThreadExecutor());
    }

    public Executor getDiskIO() {
        return diskIO;
    }

}
