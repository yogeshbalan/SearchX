package in.yogesh.searchx.app.view.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import in.yogesh.searchx.R;
import in.yogesh.searchx.app.model.database.SearchResultDbHelper;
import in.yogesh.searchx.app.viewmodel.HomeViewModel;
import in.yogesh.searchx.app.viewmodel.interfaces.ViewInteractionListener;
import in.yogesh.searchx.app.viewmodel.interfaces.ViewModelToActivityCommunicator;
import in.yogesh.searchx.databinding.ActivityHomeBinding;
import in.yogesh.searchx.library.activity.ViewModelActivity;
import in.yogesh.searchx.library.utility.Utils;


public class HomeActivity extends ViewModelActivity<ActivityHomeBinding, HomeViewModel> implements ViewInteractionListener {

    public static final int DEFAULT_SPAN = 2;
    Toolbar toolbar;
    RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private ActivityHomeBinding activityHomeBinding;
    private ViewModelToActivityCommunicator viewModelToActivityCommunicator;
    private int gridSpan = DEFAULT_SPAN;
    private SearchResultDbHelper searchResultDbHelper;

    @NonNull
    @Override
    protected HomeViewModel createViewModel(Bundle savedInstanceState) {
        setViewModelToActivityCommunicator();
        searchResultDbHelper = new SearchResultDbHelper(HomeActivity.this);
        homeViewModel = new HomeViewModel(this, getViewModelToActivityCommunicator(), searchResultDbHelper);
        return homeViewModel;
    }

    @NonNull
    @Override
    protected ActivityHomeBinding createBindingClass() {
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        return activityHomeBinding;
    }

    @NonNull
    @Override
    protected void setViewModelToBinding() {
        activityHomeBinding.setViewmodel(homeViewModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpViews();
        if (savedInstanceState == null) {
            animateToolbar();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        switch (id) {
            case R.id.action_2_column:
                item.setChecked(true);
                changeGridLayoutColumn(2);
                return true;
            case R.id.action_3_column:
                item.setChecked(true);
                changeGridLayoutColumn(3);
                return true;
            case R.id.action_4_column:
                item.setChecked(true);
                changeGridLayoutColumn(4);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void hideKeyboard() {
        Utils.hideKeyboard(this);
    }

    @Override
    public void showKeyboard() {
        Utils.showKeyboard(this);
    }

    public ViewModelToActivityCommunicator getViewModelToActivityCommunicator() {
        return viewModelToActivityCommunicator;
    }

    public void setViewModelToActivityCommunicator() {
        this.viewModelToActivityCommunicator = new ViewModelToActivityCommunicator() {
            @Override
            public boolean isNetworkAvailable() {
                return Utils.isNetworkAvailable(HomeActivity.this);
            }

            @Override
            public Activity getContext() {
                return HomeActivity.this;
            }

            @Override
            public FragmentManager getFragmentManager() {
                return HomeActivity.this.getSupportFragmentManager();
            }
        };
    }

    private void setUpViews() {
        toolbar = activityHomeBinding.toolbar;
        recyclerView = activityHomeBinding.getRoot().findViewById(R.id.recyclerview);
        setSupportActionBar(toolbar);

    }

    private void animateToolbar() {
        // this is gross but toolbar doesn't expose it's children to animate them :(
        View t = toolbar.getChildAt(0);
        if (t != null && t instanceof TextView) {
            TextView title = (TextView) t;

            // fade in and space out the title.  Animating the letterSpacing performs horribly so
            // fake it by setting the desired letterSpacing then animating the scaleX ¯\_(ツ)_/¯
            title.setAlpha(0f);
            title.setScaleX(0.8f);

            title.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .setStartDelay(300)
                    .setDuration(900)
                    .setInterpolator(new FastOutSlowInInterpolator());
        }
    }

    private void changeGridLayoutColumn(int span) {
        gridSpan = span;
        if (homeViewModel != null) {
            homeViewModel.setGridSpan(span);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, span);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
}
