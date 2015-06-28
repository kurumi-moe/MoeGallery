package moe.kurumi.moegallery.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.apmem.tools.layouts.FlowLayout;
import moe.kurumi.moegallery.R;
import moe.kurumi.moegallery.model.AnimePictures;
import moe.kurumi.moegallery.model.AnimePicturesUser;
import moe.kurumi.moegallery.model.Behoimi;
import moe.kurumi.moegallery.model.Danbooru;
import moe.kurumi.moegallery.model.DanbooruTag;
import moe.kurumi.moegallery.model.Gelbooru;
import moe.kurumi.moegallery.model.Image;
import moe.kurumi.moegallery.model.Moebooru;
import moe.kurumi.moegallery.model.Preferences_;
import moe.kurumi.moegallery.model.Tag;
import moe.kurumi.moegallery.model.database.FavoriteImage;
import moe.kurumi.moegallery.model.database.FavoriteImage$Table;
import moe.kurumi.moegallery.model.database.HistoryTag;
import moe.kurumi.moegallery.model.database.HistoryTag$Table;
import moe.kurumi.moegallery.provider.Providers;
import moe.kurumi.moegallery.utils.Utils;
import moe.kurumi.moegallery.view.adapter.ImageAdapter;
import moe.kurumi.moegallery.view.adapter.RecyclerViewAdapterBase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import okio.BufferedSink;
import okio.Okio;
import retrofit.RestAdapter;
import retrofit.client.Header;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by kurumi on 15-5-28.
 */
@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.menu_main)
public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity";
    private final static int ANIMATION_INT = 100;
    private final static int ANIMATION_DURATION = 300;

    @ViewById
    DrawerLayout drawerLayout;
    @ViewById
    ListView leftDrawerList;
    @ViewById
    LinearLayout rightDrawer;
    @ViewById
    ListView rightDrawerList;
    @ViewById
    Toolbar toolbar;
    @ViewById
    RecyclerView recyclerView;
    @ViewById(R.id.image)
    ImageView fullImage;
    @ViewById(R.id.image_container)
    FrameLayout imageContainer;
    @ViewById
    ImageButton home;
    @ViewById
    TextView history;
    @ViewById
    TextView favorite;

    @ViewById
    ButtonFloat floatSearch;

    @ViewById
    ButtonFloat floatFavorite;

    @StringArrayRes(R.array.provider_names)
    String[] providerNames;
    @StringArrayRes(R.array.provider_values)
    String[] providerValues;
    @Bean
    ImageAdapter imageAdapter;
    @Pref
    Preferences_ preferences;

    @OptionsMenuItem(R.id.search)
    MenuItem menuSearch;
    @OptionsMenuItem(R.id.info)
    MenuItem menuInfo;
    @OptionsMenuItem(R.id.set_wallpaper)
    MenuItem menuWallpaper;
    @OptionsMenuItem(R.id.download)
    MenuItem menuDownload;
    @OptionsMenuItem(R.id.favorite)
    MenuItem menuFavorite;

    @OptionsMenuItem(R.id.share)
    MenuItem menuShare;

    AlertDialog dialog;

    @ColorRes
    int transparent;
    @ColorRes
    int black;
    File downloadDir;
    ReloadImageCallback reloadImageCallback;
    float favoriteShowPosition;
    float favoriteHidePosition;
    int favoriteRightMargin;
    private ArrayAdapter providerAdapter;
    private ArrayAdapter historyTagsAdapter;
    private String providerUri;
    private Handler handler = new Handler();
    private Runnable hideSystemUIRunnable;
    private String tags = "";
    private String title = "";
    private ArrayList<String> historyTags = new ArrayList<>();
    private PhotoViewAttacher photoViewAttacher;
    private int lastAnimatedValue = -1;
    private boolean turn = false;
    private float turnProgress;
    private Image currentImage;
    private Uri currentImageUri;
    private MaterialDialog progressDialog;
    private MaterialDialog messageDialog;
    private boolean isInHistoryMode = false;
    private boolean isInFavoriteMode = false;

    @AfterViews
    void bindAdapter() {

        setSupportActionBar(toolbar);

        downloadDir = new File(Environment.getExternalStorageDirectory().getPath(), "MoeGallery");

        if (!downloadDir.exists()) {
            downloadDir.mkdir();
        }

        progressDialog = new MaterialDialog.Builder(this)
                .cancelable(false)
                .theme(Theme.LIGHT)
                .content(R.string.please_wait)
                .progress(true, 0).build();

        messageDialog = new MaterialDialog.Builder(this)
                .positiveText(android.R.string.ok)
                .title(R.string.network_error)
                .cancelable(true)
                .theme(Theme.DARK)
                .build();

        showProgressDialog();

        photoViewAttacher = new PhotoViewAttacher(fullImage);

        photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);

        photoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (!isSystemUIVisible()) {
                    showSystemUI();
                    hideSystemUIDelayed(3000);
                    showFavorite();
                } else {
                    hideSystemUIDelayed(0);
                }
            }
        });

        drawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.actionbar_background));

        providerAdapter = new ArrayAdapter<>(this, R.layout.drawer_list_item, providerNames);

        leftDrawerList.setAdapter(providerAdapter);

        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                providerUri = providerValues[position];
                preferences.edit().provider().put(providerUri).apply();
                drawerLayout.closeDrawers();
                setDefaultTitle();
                clearModes();
                reload();
            }
        });

        historyTags.clear();

        List<HistoryTag> historyTagList = new Select().from(HistoryTag.class).queryList();
        Collections.sort(historyTagList);

        for (HistoryTag tag : historyTagList) {
            historyTags.add(tag.tag);
        }

        historyTagsAdapter = new ArrayAdapter<>(this, R.layout.drawer_list_item, historyTags);

        rightDrawerList.setAdapter(historyTagsAdapter);

        rightDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tags = historyTags.get(position);
                clearModes();
                reload();
                drawerLayout.closeDrawers();
            }
        });

        providerUri = preferences.provider().get();

        recyclerView.setAdapter(imageAdapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = gridLayoutManager.findLastVisibleItemPosition();

                if (lastPosition == recyclerView.getAdapter().getItemCount() - 1) {
                    //Log.d(TAG, "" + lastPosition);

                    // load more
                    if (lastPosition == ((RecyclerViewAdapterBase) recyclerView.getAdapter()).getNextCount() - 1) {
                        ((RecyclerViewAdapterBase) recyclerView.getAdapter()).loadNextPage(tags);
                    }
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags = "";
                clearModes();
                reload();
                drawerLayout.closeDrawers();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawerLayout.closeDrawers();
                if (!isInHistoryMode) {
                    providerUri = Providers.HISTORY;
                    setTitle(
                            getString(R.string.app_name) + " (" + getString(
                                    R.string.history) + ")");
                    reloadFromHistory();
                } else {
                    providerUri = preferences.provider().get();
                    setDefaultTitle();
                    reload();

                }
                isInHistoryMode = !isInHistoryMode;
                history.setTextColor(getResources().getColor(
                        isInHistoryMode ? android.R.color.holo_green_light :
                                android.R.color.white));
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                if (!isInFavoriteMode) {
                    providerUri = Providers.FAVORITE;
                    setTitle(
                            getString(R.string.app_name) + " (" + getString(
                                    R.string.favorite) + ")");
                    reloadFromFavorite();
                } else {
                    providerUri = preferences.provider().get();
                    setDefaultTitle();
                    reload();

                }
                isInFavoriteMode = !isInFavoriteMode;
                favorite.setTextColor(getResources().getColor(
                        isInFavoriteMode ? android.R.color.holo_green_light :
                                android.R.color.white));
            }
        });

        floatSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        floatFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFavorite();
            }
        });
    }

    private void clearModes() {
        isInHistoryMode = false;
        isInFavoriteMode = false;
        history.setTextColor(getResources().getColor(android.R.color.white));
        favorite.setTextColor(getResources().getColor(android.R.color.white));
    }

    @UiThread
    public void clearHistoryMode() {
        providerUri = preferences.provider().get();
        setDefaultTitle();
        isInHistoryMode = false;
        history.setTextColor(getResources().getColor(android.R.color.white));
        reload();
    }

    @UiThread
    public void clearFavoriteMode() {
        providerUri = preferences.provider().get();
        setDefaultTitle();
        isInFavoriteMode = false;
        favorite.setTextColor(getResources().getColor(android.R.color.white));
        reload();
    }

    @Override
    public void onBackPressed() {
        if (fullImage.getVisibility() != View.GONE) {
            hideImage();
            hideFavorite();
        } else {
            super.onBackPressed();
        }
    }

    private void hideFavorite() {
        if (preferences.floatFavorite().get()) {
            if (floatFavorite.isShow()) {
                floatFavorite.hide();
            }
        }
    }

    private void showFavorite() {
        if (preferences.floatFavorite().get()) {
            floatFavorite.setVisibility(View.VISIBLE);

            if (!floatFavorite.isShow()) {
                floatFavorite.show();
            }
        }
    }

    private void hideImage() {
        //fullImage.setVisibility(View.GONE);
        //fullImage.setImageBitmap(null);

        setDefaultTitle();

        hideProgressDialog();
        showSystemUI();

        setMenu(true);

        int orientation = getRequestedOrientation();

        if (orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        View view = recyclerView.findViewWithTag(currentImage);
        runExitAnimation(fullImage, view, orientation);
        if (floatSearch.getVisibility() == View.VISIBLE) {
            floatSearch.show();
        }
        //fullImage.setVisibility(View.GONE);
    }

    public void setCurrentImage(Image image) {
        currentImage = image;
    }

    public void setMenu(boolean isMain) {
        menuSearch.setVisible(isMain && !preferences.floatSearch().get());
        menuInfo.setVisible(!isMain);
        menuWallpaper.setVisible(!isMain);
        menuShare.setVisible(!isMain);
        menuDownload.setVisible(!isMain && !preferences.autoDownload().get());

        menuFavorite.setVisible(!isMain && !preferences.floatFavorite().get());


        if (!isMain) {
            Image image = (Image) this.fullImage.getTag();
            List<FavoriteImage> favoriteImages = new Select().from(FavoriteImage.class).where(
                    Condition.column(FavoriteImage$Table.PREVIEWURL).eq(image.getPreviewUrl())).
                    queryList();

            if (favoriteImages.size() == 1) {
                menuFavorite.setTitle(R.string.remove_favorite);
                favoriteImages.get(0).updateLast();
                favoriteImages.get(0).save();
                floatFavorite.setDrawableIcon(getResources().getDrawable(
                        R.drawable.ic_favorite_white_48dp));
            } else {
                menuFavorite.setTitle(R.string.add_favorite);
                floatFavorite.setDrawableIcon(getResources().getDrawable(
                        R.drawable.ic_favorite_border_white_48dp));
            }
        }

    }

    public void setDefaultTitle() {
        title = getString(R.string.app_name);
        String hostname = Utils.getHostName(providerUri);
        setTitle(title + " (" + hostname + ")");
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.getWindow().getAttributes().windowAnimations = android.R.anim.fade_out;
            progressDialog.cancel();
        }

        if (messageDialog != null && messageDialog.isShowing()) {
            messageDialog.cancel();
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isFloatSearch = preferences.floatSearch().get();
        floatSearch.setVisibility(isFloatSearch ? View.VISIBLE : View.INVISIBLE);
        if (menuSearch != null) {
            menuSearch.setVisible(!isFloatSearch);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (providerUri != null && !providerUri.equals(preferences.provider().get())) {

            providerUri = preferences.provider().get();

            if (isInHistoryMode) {
                reloadFromHistory();
            } else if (isInFavoriteMode) {
                reloadFromFavorite();
            } else {
                reload();
            }
            history.setTextColor(getResources().getColor(
                    isInHistoryMode ? android.R.color.holo_green_light :
                            android.R.color.white));
            favorite.setTextColor(getResources().getColor(
                    isInFavoriteMode ? android.R.color.holo_green_light :
                            android.R.color.white));
        }

        if (fullImage.getVisibility() == View.GONE) {
            setDefaultTitle();
            boolean isFloatSearch = preferences.floatSearch().get();
            floatSearch.setVisibility(isFloatSearch ? View.VISIBLE : View.INVISIBLE);
            if (menuSearch != null) {
                menuSearch.setVisible(!isFloatSearch);
            }
        } else {
            boolean isFloatFavorite = preferences.floatFavorite().get();
            floatFavorite.setVisibility(isFloatFavorite ? View.VISIBLE : View.INVISIBLE);
            if (menuFavorite != null) {
                menuFavorite.setVisible(!isFloatFavorite);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        int top = (int) getResources().getDimension(R.dimen.toolbar_margin_top);
        int right = 0;

        Rect visibleFrame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(visibleFrame);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        Log.e(TAG, "drawerLayout.getRootView().getHeight(): "+drawerLayout.getRootView().getHeight());
        Log.e(TAG, "drawerLayout.getRootView().getWidth(): "+drawerLayout.getRootView().getWidth());
        Log.e(TAG, "metrics.heightPixels: "+metrics.heightPixels);
        Log.e(TAG, "metrics.widthPixels: "+metrics.widthPixels);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            right = drawerLayout.getRootView().getHeight() - metrics.widthPixels;
        }

        params.setMargins(0, top, right, 0);
        toolbar.requestLayout();

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            RelativeLayout.LayoutParams favoriteParams = (RelativeLayout.LayoutParams)floatFavorite.getLayoutParams();
            if (favoriteRightMargin==0) {
                favoriteRightMargin = favoriteParams.rightMargin;
            }
            favoriteParams.rightMargin = favoriteRightMargin;

            floatFavorite.setShowPosition(favoriteShowPosition);
            floatFavorite.setHidePosition(favoriteHidePosition);
            floatFavorite.setVisibility(View.INVISIBLE);
            floatFavorite.hide();
        } else {
            favoriteShowPosition = floatFavorite.getShowPosition();
            favoriteHidePosition = floatFavorite.getHidePosition();

            float favoriteBottomMargin = metrics.widthPixels - favoriteShowPosition;

            RelativeLayout.LayoutParams favoriteParams = (RelativeLayout.LayoutParams)floatFavorite.getLayoutParams();
            if (favoriteRightMargin==0) {
                favoriteRightMargin = favoriteParams.rightMargin;
            }
            favoriteParams.rightMargin = favoriteRightMargin + Utils.getNavigationBarHeight(this, newConfig.orientation);

            floatFavorite.setShowPosition(metrics.heightPixels - favoriteBottomMargin);
            floatFavorite.setHidePosition(
                    metrics.heightPixels - favoriteBottomMargin - (favoriteShowPosition - favoriteHidePosition));
        }
    }

    @OptionsItem(R.id.settings)
    void launchSettings() {
        SettingsActivity_.intent(this).start();
    }

    @UiThread
    public void setImageUri(Uri uri) {
        currentImageUri = uri;
        if (uri != null && fullImage.getVisibility() == View.VISIBLE) {
            menuWallpaper.setVisible(true);
            menuShare.setVisible(true);
            menuDownload.setVisible(false);
        } else {
            menuWallpaper.setVisible(false);
            menuShare.setVisible(false);
        }

        if (uri == null && fullImage.getVisibility() == View.VISIBLE &&
                !preferences.autoDownload().get()) {
            menuDownload.setVisible(true);
        }
    }

    @OptionsItem(R.id.set_wallpaper)
    void setWallpaper() {
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(currentImageUri, "image/*");
        intent.putExtra("mimeType", "image/*");
        this.startActivity(Intent.createChooser(intent, getResources().getString(R.string.set_as)));
    }

    @OptionsItem(R.id.share)
    void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, currentImageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share)));
    }

    @OptionsItem(R.id.info)
    void showDetails() {
        Image image = (Image) this.fullImage.getTag();

        View detailsView = getLayoutInflater().inflate(R.layout.details,
                (ViewGroup) findViewById(android.R.id.content), false);
        final TextView name = (TextView) detailsView.findViewById(R.id.name);
        final TextView resolution = (TextView) detailsView.findViewById(R.id.resolution);
        final TextView size = (TextView) detailsView.findViewById(R.id.size);
        final TextView type = (TextView) detailsView.findViewById(R.id.type);
        final TextView count = (TextView) detailsView.findViewById(R.id.count);
        final TextView user = (TextView) detailsView.findViewById(R.id.user);
        final FlowLayout tagsView = (FlowLayout) detailsView.findViewById(R.id.tags);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImage();
                hideFavorite();
                tags = (String) v.getTag();
                dialog.cancel();
                reload();
            }
        };

        if (image.getTagList().size() > 0) {
            for (String s : image.getTagList()) {

//            FlowLayout.LayoutParams params =
//                    new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
//                            FlowLayout.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, 0, 20, 20);
                ButtonRectangle button = (ButtonRectangle) getLayoutInflater().
                        inflate(R.layout.tag_button, tagsView, false);
                button.setText(s.replace('_', ' '));
                button.setTag(s);

                button.setOnClickListener(clickListener);
                tagsView.addView(button/*, params*/);
                tagsView.invalidate();

                button.setBackgroundColor(Utils.String2Color(s));
            }
        } else {
            ((View) tagsView.getParent()).setVisibility(View.GONE);
        }

        name.setText(image.getName());
        resolution.setText("" + image.getWidth() + "x" + image.getHeight());

        if (image.getSize() > 0) {
            size.setText(Utils.humanReadableByteCount(image.getSize(), true));
        } else {
            ((View) size.getParent()).setVisibility(View.GONE);
        }

        type.setText(image.getType());

        if (image.getCount() > 0) {
            count.setText("" + image.getCount());
        } else {
            ((View) count.getParent()).setVisibility(View.GONE);
        }

        if (!image.getUser().isEmpty()) {
            user.setText(image.getUser());
        } else {
            ((View) user.getParent()).setVisibility(View.GONE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.info)
                .setView(detailsView)
                .setPositiveButton(android.R.string.ok, null);

        dialog = builder.create();
        dialog.show();
    }

    @OptionsItem(R.id.download)
    void downloadImage() {
        showProgressDialog();
        Image image = (Image) this.fullImage.getTag();
        download(image, new DownloadCallback() {
            @Override
            public void onSucceed(Image image, File file) {
                hideProgressDialog();
                if (reloadImageCallback != null) {
                    reloadImageCallback.onReloadImage(image);
                }
            }

            @Override
            public void onFailed(Image image, String message) {
                hideProgressDialog();
                showErrorDialog(message);
            }
        });
    }

    @OptionsItem(R.id.favorite)
    void switchFavorite() {
        Image image = (Image) this.fullImage.getTag();
        List<FavoriteImage> favoriteImages = new Select().from(FavoriteImage.class).where(
                Condition.column(FavoriteImage$Table.PREVIEWURL).eq(image.getPreviewUrl())).
                queryList();

        if (favoriteImages.size() == 1) {
            favoriteImages.get(0).delete();
            menuFavorite.setTitle(R.string.add_favorite);
            floatFavorite.setDrawableIcon(getResources().getDrawable(
                    R.drawable.ic_favorite_border_white_48dp));
        } else {
            new FavoriteImage(image).save();
            menuFavorite.setTitle(R.string.remove_favorite);
            floatFavorite.setDrawableIcon(getResources().getDrawable(
                    R.drawable.ic_favorite_white_48dp));
        }
    }

    @UiThread
    void reload() {

        showProgressDialog();

        if (!tags.isEmpty()) {
            int index = historyTags.indexOf(tags.replace('_', ' '));
            if (index >= 0) {
                historyTags.remove(index);
            }
            historyTags.add(0, tags.replace('_', ' '));
            historyTagsAdapter.notifyDataSetChanged();

            List<HistoryTag> historyTags = new Select().from(HistoryTag.class).where(
                    Condition.column(HistoryTag$Table.TAG).eq(tags.replace('_', ' '))).queryList();

            if (historyTags.size() == 1) {
                historyTags.get(0).count++;
                historyTags.get(0).last = System.currentTimeMillis();
                historyTags.get(0).save();
            } else {
                HistoryTag historyTag =
                        new HistoryTag(tags.replace('_', ' '), 1, System.currentTimeMillis());
                historyTag.save();
            }

            switch (providerUri) {
                case Providers.KONACHAN_URI:
                case Providers.YANDERE_URI:
                case Providers.BEHOIMI_URI:
                case Providers.GELBOORU_URI:
                case Providers.DANBOORU_URI:
                    tags = tags.trim().replace(' ', '_');
                    break;
            }
        }

        imageAdapter.reload(tags);
    }

    @UiThread
    void reloadFromHistory() {
        isInFavoriteMode = false;
        favorite.setTextColor(getResources().getColor(android.R.color.white));
        imageAdapter.reloadFromHistory();
    }

    @UiThread
    void reloadFromFavorite() {
        isInHistoryMode = false;
        history.setTextColor(getResources().getColor(android.R.color.white));
        imageAdapter.reloadFromFavorite();
    }

    @OptionsItem(R.id.search)
    void search() {

        View searchView = getLayoutInflater().inflate(R.layout.search_box,
                (ViewGroup) findViewById(android.R.id.content), false);
        final EditText searchText = (EditText) searchView.findViewById(R.id.search);

        searchText.setText(tags.replace('_', ' '));
        searchText.setSelection(searchText.getText().length());

        searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                searchText.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager =
                                (InputMethodManager) MainActivity.this.getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(searchText,
                                InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        searchText.requestFocus();

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(android.R.string.search_go)
                .setView(searchView)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.search_go,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgressDialog();
                                tags = searchText.getText().toString();
                                listTag(tags);
                            }
                        });

        dialog = builder.create();
        dialog.show();
    }

    @Background
    public void listTag(String tag) {

        String apiUri = preferences.provider().get();

        List<? extends Tag> tags;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(apiUri)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        switch (apiUri) {
            case Providers.KONACHAN_URI:
            case Providers.YANDERE_URI:
                Moebooru moebooru = restAdapter.create(Moebooru.class);
                tags = moebooru.tag(tag.trim().replace(' ', '_'));
                break;
            case Providers.DANBOORU_URI:
                Danbooru danbooru = restAdapter.create(Danbooru.class);
                List<DanbooruTag> tagsStart = danbooru.tag("*" + tag.trim());
                List<DanbooruTag> tagsEnd = danbooru.tag(tag.trim() + "*");
                tagsStart.addAll(tagsEnd);
                tags = tagsStart;
                break;
            case Providers.BEHOIMI_URI:
                Behoimi behoimi = restAdapter.create(Behoimi.class);
                tags = behoimi.tag("*" + tag.trim().replace(' ', '_') + "*");
                break;
            case Providers.ANIME_PICTURES_URI:
                AnimePictures animePictures = restAdapter.create(AnimePictures.class);
                tags = animePictures.tag(tag).getTagsList();
                break;
            case Providers.GELBOORU_URI:

                restAdapter = new RestAdapter.Builder()
                        .setEndpoint(apiUri)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setConverter(new SimpleXMLConverter())
                        .build();

                Gelbooru gelbooru = restAdapter.create(Gelbooru.class);
                tags = gelbooru.tag(200, 0, tag.trim().replace(' ', '_')).getTag();
                break;
            default:
                tags = new ArrayList<>();

        }

        listTagDialog(tags);
    }

    @UiThread
    public void listTagDialog(final List<? extends Tag> tagList) {

        hideProgressDialog();

        if (tagList.size() == 1) {
            tags = tagList.get(0).getName();
            clearModes();
            reload();
        } else if (tagList.size() == 0) {
            makeToast(R.string.no_tag_found);
        } else {

            // sort by count
            Collections.sort(tagList, new Tag.TagComparator());

            View tagsView = getLayoutInflater().inflate(R.layout.tag_list,
                    (ViewGroup) findViewById(android.R.id.content), false);
            final ListView tagListView = (ListView) tagsView.findViewById(R.id.tag_list);
            tagListView.setAdapter(new ArrayAdapter<>(this, R.layout.tag_item, tagList));

            tagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tags = tagList.get(position).getName();
                    clearModes();
                    reload();
                    dialog.cancel();
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.search_suggest)
                    .setView(tagsView)
                    .setNegativeButton(android.R.string.cancel, null);

            dialog = builder.create();
            dialog.show();
        }
    }

    @UiThread
    public void makeToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @UiThread
    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @UiThread
    public void login() {
        View loginView = getLayoutInflater().inflate(R.layout.login,
                (ViewGroup) findViewById(android.R.id.content), false);
        final EditText usernameText = (EditText) loginView.findViewById(R.id.username);
        final EditText passwordText = (EditText) loginView.findViewById(R.id.password);
        final TextView providerText = (TextView) loginView.findViewById(R.id.provider);

        providerText.setText(providerUri);

        usernameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                usernameText.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager =
                                (InputMethodManager) MainActivity.this.getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(usernameText,
                                InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        usernameText.requestFocus();

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.login_required)
                .setView(loginView)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showProgressDialog();
                                String username = usernameText.getText().toString();
                                String password = passwordText.getText().toString();
                                login(username, password);
                            }
                        });

        dialog = builder.create();
        dialog.show();
    }

    @Background
    public void login(String username, String password) {

        String apiUri = preferences.provider().get();

        RestAdapter.Builder restAdapter = new RestAdapter.Builder()
                .setEndpoint(apiUri)
                .setLogLevel(RestAdapter.LogLevel.FULL);

        switch (apiUri) {
            case Providers.ANIME_PICTURES_URI:
                restAdapter.setClient(new OkClient() {

                    @Override
                    public Response execute(Request request) throws IOException {
                        Response response = super.execute(request);

                        for (Header header : response.getHeaders()) {
                            if (header.getValue().contains("_server=")) {
                                String server = header.getValue().substring(0,
                                        header.getValue().indexOf('='));
                                preferences.edit().animePicturesServer().put(server).apply();
                            }
                        }

                        return response;
                    }
                });
                AnimePictures animePictures = restAdapter.build().create(AnimePictures.class);
                AnimePicturesUser user = animePictures.login(username, password,
                        TimeZone.getDefault().getID());

                if (user.getSuccess()) {
                    makeToast(R.string.login_success);
                    preferences.edit().animePicturesToken().put(user.getToken()).apply();
                } else {
                    makeToast(R.string.login_failed);
                    preferences.edit().animePicturesToken().put("").apply();
                }

                break;
            default:
                // do nothing
        }
        hideProgressDialog();
    }

    @UiThread
    public void showSystemUI() {

        cancelHideSystemUIDelayed();

        toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();

        int flags = 0;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            flags = View.SYSTEM_UI_FLAG_VISIBLE;

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
        getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    @UiThread
    public void hideSystemUIDelayed(int timeout) {

        if (hideSystemUIRunnable == null) {
            hideSystemUIRunnable = new Runnable() {
                @Override
                public void run() {
                    hideSystemUI();
                }
            };
        }

        handler.removeCallbacks(hideSystemUIRunnable);
        handler.postDelayed(hideSystemUIRunnable, timeout);
    }

    @UiThread
    public void cancelHideSystemUIDelayed() {
        if (hideSystemUIRunnable != null) {
            handler.removeCallbacks(hideSystemUIRunnable);
        }
    }

    public boolean isSystemUIVisible() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            return (getWindow().getDecorView().getSystemUiVisibility() &
                    View.SYSTEM_UI_FLAG_LOW_PROFILE) == 0;
        } else {
            return (getWindow().getDecorView().getSystemUiVisibility() &
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
        }
    }

    private void hideSystemUI() {

        int flags = 0;

        toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(
                new AccelerateInterpolator()).start();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            flags = View.SYSTEM_UI_FLAG_LOW_PROFILE;

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags = flags | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(flags);

        hideFavorite();
    }

    @UiThread
    public void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.getWindow().getAttributes().windowAnimations =
                            android.R.anim.fade_out;
                    progressDialog.dismiss();

                    if (fullImage.getVisibility() == View.VISIBLE) {
                        hideSystemUI();
                    }
                }
            }, 300);
        }
    }

    @UiThread
    public void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.getWindow().getAttributes().windowAnimations = android.R.anim.fade_in;
            progressDialog.show();
        }
    }

    @UiThread
    public void showImage(final int thumbnailLeft, final int thumbnailTop,
            final int thumbnailWidth, final int thumbnailHeight) {

//        mOriginalOrientation = getResources().getConfiguration().orientation;
//
//        imageContainer.setClickable(false);
//        imageContainer.setVisibility(View.VISIBLE);
//
//        ViewTreeObserver observer = imageContainer.getViewTreeObserver();
//        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//
//            @Override
//            public boolean onPreDraw() {
//                imageContainer.getViewTreeObserver().removeOnPreDrawListener(this);
//
//                // Figure out where the thumbnail and full size versions are, relative
//                // to the screen and each other
//                mLeftDelta = thumbnailLeft;
//                mTopDelta = thumbnailTop;
//
//                // Scale factors to make the large version the same size as the thumbnail
//
//
//                mWidthScale = (float) thumbnailWidth / imageContainer.getWidth();
//                mHeightScale = (float) thumbnailHeight / imageContainer.getHeight();
//
//                runEnterAnimation();
//
//                return true;
//            }
//        });
    }

    private void runEnterAnimation() {
//        final long duration = (long) (ANIM_DURATION * mAnimatorScale);
//
//        YoYo.with(new BaseViewAnimator() {
//            @Override
//            protected void prepare(View view) {
//                this.getAnimatorAgent().playTogether(
//                        ObjectAnimator.ofFloat(imageContainer, "pivotX", 0, 0),
//                        ObjectAnimator.ofFloat(imageContainer, "pivotY", 0, 0),
//                        ObjectAnimator.ofFloat(imageContainer, "scaleX", mWidthScale, 1.0F),
//                        ObjectAnimator.ofFloat(imageContainer, "scaleY", mHeightScale, 1.0F),
//                        ObjectAnimator.ofFloat(imageContainer, "translationX", mLeftDelta, 0),
//                        ObjectAnimator.ofFloat(imageContainer, "translationY", mTopDelta, 0));
//            }
//        }).duration(duration).playOn(imageContainer);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                imageContainer.setClickable(true);
//                imageContainer.setBackgroundColor(black);
//            }
//        }, ANIM_DURATION);
    }

    private void runExitAnimation() {
//        final long duration = (long) (ANIM_DURATION * mAnimatorScale);
//
//        final boolean fadeOut;
//        if (getResources().getConfiguration().orientation != mOriginalOrientation) {
//            imageContainer.setPivotX(imageContainer.getWidth() / 2);
//            imageContainer.setPivotY(imageContainer.getHeight() / 2);
//            mLeftDelta = 0;
//            mTopDelta = 0;
//            fadeOut = false;
//        } else {
//            fadeOut = true;
//        }
//
//        imageContainer.setBackgroundColor(transparent);
//
//        if (fadeOut) {
//            // Animate fullImage back to thumbnail size/location
//
//            YoYo.with(new BaseViewAnimator() {
//                @Override
//                protected void prepare(View view) {
//                    this.getAnimatorAgent().playTogether(
//                            ObjectAnimator.ofFloat(imageContainer, "pivotX", 0, 0),
//                            ObjectAnimator.ofFloat(imageContainer, "pivotY", 0, 0),
//                            ObjectAnimator.ofFloat(imageContainer, "scaleX", mWidthScale),
//                            ObjectAnimator.ofFloat(imageContainer, "scaleY", mHeightScale),
//                            ObjectAnimator.ofFloat(imageContainer, "translationX", mLeftDelta),
//                            ObjectAnimator.ofFloat(imageContainer, "translationY", mTopDelta));
//                }
//            }).duration(duration).playOn(imageContainer);
//
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    imageContainer.setClickable(false);
//                    imageContainer.setVisibility(View.GONE);
//                }
//            }, ANIM_DURATION);
//        } else {
//            imageContainer.setClickable(false);
//            imageContainer.setVisibility(View.GONE);
//        }
    }

    @UiThread
    public void runExitAnimation(final ImageView target, final View to, final int orientation) {

        final int duration = ANIMATION_DURATION;

        // background
        ObjectAnimator backgroundAnimator = ObjectAnimator.ofFloat(imageContainer,
                "alpha", 1f, 0f);
        backgroundAnimator.setDuration(duration);
        backgroundAnimator.start();

        if (to == null) {
            target.setVisibility(View.GONE);
            return;
        }

        int[] location = new int[2];
        to.getLocationInWindow(location);

        final int thumbnailLeft = location[0];
        final int thumbnailTop = location[1];
        final int thumbnailWidth = to.getWidth();
        final int thumbnailHeight = to.getHeight();

//        int height = target.getDrawable().getIntrinsicHeight();
//        int width = target.getDrawable().getIntrinsicWidth();
        int height = ((Image) target.getTag()).getHeight().intValue();
        int width = ((Image) target.getTag()).getWidth().intValue();

        //Log.d(TAG, "height: " + height + ", width: " + width);

        final float ratioImage = (float) height / width;
        final float ratioView = (float) thumbnailHeight / thumbnailWidth;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, ANIMATION_INT);
        valueAnimator.setDuration(duration);

        photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                if (orientation == getRequestedOrientation() || orientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                    int animatedValue = (int) animation.getAnimatedValue();

                    if (lastAnimatedValue != animatedValue) {
                        lastAnimatedValue = animatedValue;

                        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                                target.getLayoutParams());

                        float animatedProgress = (float) animatedValue / ANIMATION_INT;

                        View parent = target.getRootView();

                        int fullLeft = parent.getLeft();
                        int fullTop = parent.getTop();

                        int fullWidth = parent.getWidth();
                        int fullHeight = parent.getHeight();

                        int left = (int) ((thumbnailLeft - fullLeft) * animatedProgress) -
                                parent.getPaddingLeft();
                        int top = (int) ((thumbnailTop - fullTop) * animatedProgress) -
                                parent.getPaddingTop();

                        layoutParams.setMargins(left, top, 0, 0);

                        layoutParams.width = (int) ((fullWidth - thumbnailWidth) *
                                (1 - animatedProgress)) + thumbnailWidth;
                        layoutParams.height = (int) ((fullHeight - thumbnailHeight) *
                                (1 - animatedProgress)) + thumbnailHeight;

                        if (ratioImage >= ratioView) {
                            // has left/right spacing

                            float progress =
                                    1 - (0.5f * (thumbnailWidth * ratioImage - thumbnailHeight)) /
                                            (thumbnailTop - fullTop - parent.getPaddingTop());

                            if (animatedProgress < progress) {
                                layoutParams.width = (int) ((fullWidth - thumbnailWidth) *
                                        (progress - animatedProgress) / progress) +
                                        thumbnailWidth;
                                left =
                                        (int) ((thumbnailLeft - fullLeft) * animatedProgress / progress) -
                                                parent.getPaddingLeft();

                            } else if (!turn) {
                                photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                turn = true;
                                turnProgress = animatedProgress;
                            }

                            if (turn) {
                                layoutParams.width = thumbnailWidth;
                                left = ((thumbnailLeft - fullLeft)) - parent.getPaddingLeft();
                            }

                        } else {
                            // TODO
                            // has top/bottom spacing

                            if (layoutParams.width < ratioImage * thumbnailHeight) {
                                layoutParams.height = thumbnailHeight;
                                top = ((thumbnailTop - fullTop)) - parent.getPaddingTop();
                            } else if (!turn) {
                                photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                turn = true;
                                turnProgress = animatedProgress;
                            }

                            if (turn) {
                                layoutParams.height = (int) ((fullHeight - thumbnailHeight) *
                                        (animatedProgress - turnProgress) /
                                        (1 - turnProgress)) +
                                        thumbnailHeight;
                                top = (int) ((thumbnailTop - fullTop) *
                                        (1 - (animatedProgress - turnProgress) /
                                                (1 - turnProgress))) -
                                        parent.getPaddingTop();
                            }
                        }

                        layoutParams.setMargins(left, top, 0, 0);
                        target.setLayoutParams(layoutParams);
                    }
                } else {
                    target.setVisibility(View.GONE);
                }
            }
        });
        valueAnimator.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                target.setVisibility(View.GONE);
                turn = false;
            }
        }, duration + 100);
    }

    // Step 1: open the view
    // Step 2: zoom the view
    @UiThread
    public void runEnterAnimation(final ImageView target, final View from, final int orientation) {

        if (floatSearch.getVisibility() == View.VISIBLE) {
            floatSearch.hide();
        }

        final int duration = ANIMATION_DURATION;

        // background
        ObjectAnimator backgroundAnimator = ObjectAnimator.ofFloat(imageContainer,
                "alpha", 0f, 1f);
        backgroundAnimator.setDuration(duration);
        backgroundAnimator.start();

        ViewGroup.LayoutParams layoutParams = from.getLayoutParams();

        int[] location = new int[2];
        from.getLocationInWindow(location);

        final int thumbnailLeft = location[0];
        final int thumbnailTop = location[1];
        final int thumbnailWidth = from.getWidth();
        final int thumbnailHeight = from.getHeight();

        target.setTop(thumbnailTop);
        target.setLeft(thumbnailLeft);
        target.setLayoutParams(layoutParams);

//        int height = target.getDrawable().getIntrinsicHeight();
//        int width = target.getDrawable().getIntrinsicWidth();
        int height = ((Image) target.getTag()).getHeight().intValue();
        int width = ((Image) target.getTag()).getWidth().intValue();

        //Log.d(TAG, "height: " + height + ", width: " + width);

        final float ratioImage = (float) height / width;
        final float ratioView = (float) thumbnailHeight / thumbnailWidth;

        photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);

        target.setVisibility(View.VISIBLE);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, ANIMATION_INT);
        valueAnimator.setDuration(duration);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();

                if (lastAnimatedValue != animatedValue) {
                    lastAnimatedValue = animatedValue;

                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                            target.getLayoutParams());

                    View parent = target.getRootView();

                    int fullLeft = parent.getLeft();
                    int fullTop = parent.getTop();

                    int fullWidth = parent.getWidth();
                    int fullHeight = parent.getHeight();

                    if (orientation == getRequestedOrientation() || orientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                        float animatedProgress = (float) animatedValue / ANIMATION_INT;

                        int left = (int) ((thumbnailLeft - fullLeft) * (1 - animatedProgress)) -
                                parent.getPaddingLeft();
                        int top = (int) ((thumbnailTop - fullTop) * (1 - animatedProgress)) -
                                parent.getPaddingTop();

                        layoutParams.width =
                                (int) ((fullWidth - thumbnailWidth) * animatedProgress) +
                                        thumbnailWidth;
                        layoutParams.height = (int) ((fullHeight - thumbnailHeight) *
                                animatedProgress) + thumbnailHeight;

                        if (ratioImage > ratioView) {
                            // has left/right spacing
                            if (layoutParams.height < ratioImage * thumbnailWidth) {
                                layoutParams.width = thumbnailWidth;
                                left = ((thumbnailLeft - fullLeft)) - parent.getPaddingLeft();
                            } else if (!turn) {
                                photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                turn = true;
                                turnProgress = animatedProgress;
                            }

                            if (turn) {
                                layoutParams.width = (int) ((fullWidth - thumbnailWidth) *
                                        (animatedProgress - turnProgress) /
                                        (1 - turnProgress)) +
                                        thumbnailWidth;
                                left = (int) ((thumbnailLeft - fullLeft) *
                                        (1 - (animatedProgress - turnProgress) /
                                                (1 - turnProgress))) -
                                        parent.getPaddingLeft();
                            }

                        } else {
                            // has top/bottom spacing

                            if (layoutParams.width < ratioImage * thumbnailHeight) {
                                layoutParams.height = thumbnailHeight;
                                top = ((thumbnailTop - fullTop)) - parent.getPaddingTop();
                            } else if (!turn) {
                                photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                turn = true;
                                turnProgress = animatedProgress;
                            }

                            if (turn) {
                                layoutParams.height = (int) ((fullHeight - thumbnailHeight) *
                                        (animatedProgress - turnProgress) /
                                        (1 - turnProgress)) +
                                        thumbnailHeight;
                                top = (int) ((thumbnailTop - fullTop) *
                                        (1 - (animatedProgress - turnProgress) /
                                                (1 - turnProgress))) -
                                        parent.getPaddingTop();
                            }
                        }

                        //Log.e(TAG, "width:" + layoutParams.width + ", left:" + left);

                        layoutParams.setMargins(left, top, 0, 0);
                        target.setLayoutParams(layoutParams);
                    } else {
                        layoutParams.width = parent.getWidth();
                        layoutParams.height = parent.getHeight();

                        layoutParams.setMargins(-parent.getPaddingLeft(), -parent.getPaddingTop(),
                                0, 0);
                        target.setLayoutParams(layoutParams);
                        photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                }
            }
        });
        valueAnimator.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                photoViewAttacher.setMinimumScale(1f);
                turn = false;
            }
        }, duration + 100);
    }

    public PhotoViewAttacher getPhotoViewAttacher() {
        return photoViewAttacher;
    }

    @UiThread
    public void showErrorDialog(String message) {
        messageDialog.setContent(getString(R.string.connect_error) + " (" + message + ")");
        messageDialog.show();
    }

    public File getDownloadDir() {
        return downloadDir;
    }

    @Background
    public void download(Image image, DownloadCallback callback) {
        try {
            File downloadedFile = new File(downloadDir, image.getName().replace('/', '-'));
            OkHttpClient client = new OkHttpClient();

            String apiUri = preferences.provider().get();
            if (apiUri.equals(Providers.BEHOIMI_URI)) {
                client.interceptors().add(new Interceptor() {
                    @Override
                    public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                        com.squareup.okhttp.Request newRequest = chain.request().newBuilder()
                                .addHeader("Referer", Providers.BEHOIMI_URI + "/")
                                .build();
                        return chain.proceed(newRequest);
                    }
                });
            }

            com.squareup.okhttp.Request request =
                    new com.squareup.okhttp.Request.Builder().url(
                            Utils.fixURL(image.getFileUrl())).build();
            com.squareup.okhttp.Response response = client.newCall(request).execute();
            BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
            sink.writeAll(response.body().source());
            sink.close();

            if (callback != null) {
                callback.onSucceed(image, downloadedFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onFailed(image, e.getMessage());
            }
        }
    }

    public void setReloadImageCallback(ReloadImageCallback callback) {
        reloadImageCallback = callback;
    }

    public interface ReloadImageCallback {
        void onReloadImage(Image image);
    }

    public interface DownloadCallback {
        void onSucceed(Image image, File file);

        void onFailed(Image image, String message);
    }
}