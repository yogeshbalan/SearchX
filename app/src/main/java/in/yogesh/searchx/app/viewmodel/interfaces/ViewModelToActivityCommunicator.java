package in.yogesh.searchx.app.viewmodel.interfaces;

import android.app.Activity;
import android.support.v4.app.FragmentManager;

public interface ViewModelToActivityCommunicator {
    boolean isNetworkAvailable();

    Activity getContext();

    FragmentManager getFragmentManager();
}