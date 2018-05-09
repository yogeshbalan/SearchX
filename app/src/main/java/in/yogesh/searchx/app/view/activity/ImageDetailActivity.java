package in.yogesh.searchx.app.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;

import java.lang.ref.WeakReference;

import in.yogesh.searchx.R;
import in.yogesh.searchx.library.utility.ResourceUtils;

public class ImageDetailActivity extends AppCompatActivity {


    public static final String BUNDLE_KEY_TRANSITION_NAME = "transitionName";

    private static WeakReference<Bitmap> bitmapWeakReference;
    private AppCompatImageView appCompatImageView;


    public static void start(Activity context, AppCompatImageView appCompatImageView, Bitmap bitmap, String tranistionName) {
        Intent starter = new Intent(context, ImageDetailActivity.class);
        bitmapWeakReference = bitmap != null ? new WeakReference<>(bitmap) : null;
        starter.putExtra(BUNDLE_KEY_TRANSITION_NAME, ViewCompat.getTransitionName(appCompatImageView));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context,
                appCompatImageView,
                ViewCompat.getTransitionName(appCompatImageView));

        context.startActivity(starter, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        postponeEnterTransition();
        setUpViews();
    }

    private void setUpViews() {
        Bitmap bitmap = bitmapWeakReference == null ? null : bitmapWeakReference.get();
        if (bitmap == null) {
            finish();
            return;
        }
        getWindow().setStatusBarColor(ResourceUtils.getColor(R.color.color_black));
        final String transitionName = getIntent().getStringExtra(BUNDLE_KEY_TRANSITION_NAME);
        appCompatImageView = findViewById(R.id.card_image);
        appCompatImageView.setTransitionName(transitionName);
        appCompatImageView.setImageBitmap(bitmap);
        startPostponedEnterTransition();
    }

}
