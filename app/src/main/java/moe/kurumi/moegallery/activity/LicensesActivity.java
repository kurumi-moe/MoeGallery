package moe.kurumi.moegallery.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.webkit.WebView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import moe.kurumi.moegallery.R;

/**
 * Created by kurumi on 15-6-28.
 */

@EActivity(R.layout.activity_licenses)
public class LicensesActivity extends Activity {

    @ViewById
    WebView webView;

    @AfterViews
    void setView() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        webView.loadUrl("file:///android_res/raw/licenses.html");
    }

    @OptionsItem(android.R.id.home)
    void close() {
        finish();
    }
}
