package in.yogesh.searchx.library;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

/**
 * @author Yogesh Kumar on 1/5/18
 */
public abstract class ViewModel extends BaseObservable {

    protected ViewModel(@Nullable State savedInstanceState) {
    }

    protected ViewModel() {
    }

    @CallSuper
    public void onStart() {

    }

    public State getInstanceState() {
        return new State(this);
    }

    @CallSuper
    public void onStop() {

    }

    @CallSuper
    public void onDestroy() {

    }

    @CallSuper
    public void onResume() {

    }

    public static class State implements Parcelable {

        public static final Creator<State> CREATOR = new Creator<State>() {
            @Override
            public State createFromParcel(Parcel in) {
                return new State(in);
            }

            @Override
            public State[] newArray(int size) {
                return new State[size];
            }
        };

        protected State(Parcel in) {
        }

        protected State(ViewModel viewModel) {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    }
}
