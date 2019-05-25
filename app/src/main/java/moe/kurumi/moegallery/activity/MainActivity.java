package moe.kurumi.moegallery.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.apmem.tools.layouts.FlowLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import moe.kurumi.moegallery.R;
import moe.kurumi.moegallery.application.Application;
import moe.kurumi.moegallery.data.ImageDataSource;
import moe.kurumi.moegallery.data.Providers;
import moe.kurumi.moegallery.di.DaggerMainComponent;
import moe.kurumi.moegallery.di.modules.AppModule;
import moe.kurumi.moegallery.di.modules.MainModule;
import moe.kurumi.moegallery.model.AnimePictures;
import moe.kurumi.moegallery.model.AnimePicturesList;
import moe.kurumi.moegallery.model.AnimePicturesUser;
import moe.kurumi.moegallery.model.GithubRelease;
import moe.kurumi.moegallery.model.Image;
import moe.kurumi.moegallery.model.Tag;
import moe.kurumi.moegallery.model.database.FavoriteImage;
import moe.kurumi.moegallery.model.database.FavoriteImage$Table;
import moe.kurumi.moegallery.model.database.HistoryTag;
import moe.kurumi.moegallery.model.database.HistoryTag$Table;
import moe.kurumi.moegallery.model.setting.Setting;
import moe.kurumi.moegallery.utils.Utils;
import moe.kurumi.moegallery.view.ViewPager;
import moe.kurumi.moegallery.view.adapter.GalleryAdapter;
import moe.kurumi.moegallery.view.adapter.PagerAdapter;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity
        implements android.support.v4.view.ViewPager.OnPageChangeListener,
        GalleryAdapter.OnRecyclerListener {

    public final static String TAG = MainActivity.class.getSimpleName();

    private static final int WRITE_STORAGE = 0x1;
    private static final int READ_STORAGE = 0x2;

    private final static int ANIMATION_INT = 100;
    private final static int ANIMATION_DURATION = 300;
    DrawerLayout drawerLayout;
    ListView leftDrawerList;
    LinearLayout rightDrawer;
    ListView rightDrawerList;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ViewPager viewPager;
    FrameLayout imageContainer;
    ImageButton home;
    TextView history;
    TextView favorite;
    ButtonFloat floatSearch;
    ButtonFloat floatFavorite;
    String[] providerNames;
    String[] providerValues;
    GalleryAdapter mGalleryAdapter;
    @Inject
    Setting mSetting;
    @Inject
    ImageDataSource mDataSource;
    MenuItem menuSearch;
    MenuItem menuInfo;
    MenuItem menuWallpaper;
    MenuItem menuDownload;
    MenuItem menuFavorite;
    MenuItem menuShare;
    AlertDialog dialog;
    int transparent;
    int black;
    File downloadDir;
    File updateDir;
    ReloadImageCallback reloadImageCallback;
    float favoriteShowPosition;
    float favoriteHidePosition;
    int favoriteRightMargin;
    @Inject
    Retrofit.Builder mBuilder;
    @Inject
    RequestManager mRequestManager;
    private ArrayAdapter providerAdapter;
    private ArrayAdapter historyTagsAdapter;
    private String providerUri;
    private Handler handler = new Handler();
    private Runnable hideSystemUIRunnable;
    private String tags = "";
    private String title = "";
    private ArrayList<String> historyTags = new ArrayList<>();
    private int lastAnimatedValue = -1;
    private boolean turn = false;
    private float turnProgress;
    private Image currentImage;
    private Uri currentImageUri;
    private MaterialDialog progressDialog;
    private MaterialDialog messageDialog;
    private MaterialDialog updateDialog;
    private boolean isInHistoryMode = false;
    private boolean isInFavoriteMode = false;
    private String updateUrl = "";
    private String updateFileName = "";
    private PagerAdapter mPagerAdapter;
    private TouchEventListener mTouchEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMainComponent.builder()
                .appModule(new AppModule(Application.getApplication()))
                .mainModule(new MainModule(this))
                .build()
                .inject(this);

        setContentView(R.layout.activity_main);
        bindViews();
        bindAdapter();
    }

    void bindViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftDrawerList = (ListView) findViewById(R.id.left_drawer_list);
        rightDrawer = (LinearLayout) findViewById(R.id.right_drawer);
        rightDrawerList = (ListView) findViewById(R.id.right_drawer_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        viewPager = (ViewPager) findViewById(R.id.full_image_container);
        imageContainer = (FrameLayout) findViewById(R.id.image_container);
        home = (ImageButton) findViewById(R.id.home);
        history = (TextView) findViewById(R.id.history);
        favorite = (TextView) findViewById(R.id.favorite);
        floatSearch = (ButtonFloat) findViewById(R.id.float_search);
        floatFavorite = (ButtonFloat) findViewById(R.id.float_favorite);

        providerNames = getResources().getStringArray(R.array.provider_names);
        providerValues = getResources().getStringArray(R.array.provider_values);

        transparent = getResources().getColor(R.color.transparent);
        black = getResources().getColor(R.color.black);
    }

    void bindAdapter() {

        setSupportActionBar(toolbar);

        updateDir = new File(Environment.getExternalStorageDirectory().getPath(), "Downloads");

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

        updateDialog = new MaterialDialog.Builder(this)
                .positiveText(android.R.string.ok)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        downloadUpdate();
                    }
                })
                .negativeText(android.R.string.cancel)
                .title(R.string.new_version)
                .cancelable(true)
                .theme(Theme.DARK)
                .build();

        showProgressDialog();

        drawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.actionbar_background));

        providerAdapter = new ArrayAdapter<>(this, R.layout.drawer_list_item, providerNames);

        leftDrawerList.setAdapter(providerAdapter);

        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                providerUri = providerValues[position];
                mSetting.setProvider(providerUri);
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

        providerUri = mSetting.provider();

        mGalleryAdapter = new GalleryAdapter(this);
        mGalleryAdapter.setRecyclerListener(this);

        recyclerView.setAdapter(mGalleryAdapter);
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
                    if (lastPosition
                            == ((GalleryAdapter) recyclerView.getAdapter()).getNextCount() - 1) {
                        ((GalleryAdapter) recyclerView.getAdapter()).loadNextPage(tags);
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
                    providerUri = mSetting.provider();
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
                    providerUri = mSetting.provider();
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

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    private void clearModes() {
        isInHistoryMode = false;
        isInFavoriteMode = false;
        history.setTextColor(getResources().getColor(android.R.color.white));
        favorite.setTextColor(getResources().getColor(android.R.color.white));
    }

    @UiThread
    public void clearHistoryMode() {
        providerUri = mSetting.provider();
        setDefaultTitle();
        isInHistoryMode = false;
        history.setTextColor(getResources().getColor(android.R.color.white));
        reload();
    }

    @UiThread
    public void clearFavoriteMode() {
        providerUri = mSetting.provider();
        setDefaultTitle();
        isInFavoriteMode = false;
        favorite.setTextColor(getResources().getColor(android.R.color.white));
        reload();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getVisibility() != View.GONE) {
            hideImage();
            hideFavorite();

            if (!floatSearch.isShow) {
                floatSearch.show();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void hideFavorite() {
        if (mSetting.floatFavorite()) {
            if (floatFavorite.isShow()) {
                floatFavorite.hide();
            }
        }
    }

    public void showFavorite() {
        if (mSetting.floatFavorite()) {
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
        runExitAnimation(viewPager, view, orientation);
        if (floatSearch.getVisibility() == View.VISIBLE) {
            floatSearch.show();
        }
        //fullImage.setVisibility(View.GONE);
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(Image image) {
        currentImage = image;
    }

    public void setMenu(boolean isMain) {
        menuSearch.setVisible(isMain && !mSetting.floatSearch());
        menuInfo.setVisible(!isMain);
        menuWallpaper.setVisible(!isMain);
        menuShare.setVisible(!isMain);
        menuDownload.setVisible(!isMain && !mSetting.autoDownload());

        menuFavorite.setVisible(!isMain && !mSetting.floatFavorite());

        if (!isMain) {
            List<FavoriteImage> favoriteImages = new Select().from(FavoriteImage.class).where(
                    Condition.column(FavoriteImage$Table.PREVIEWURL)
                            .eq(currentImage.getPreviewUrl())).queryList();

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

        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.cancel();
        }

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuSearch = menu.findItem(R.id.search);
        menuInfo = menu.findItem(R.id.info);
        menuWallpaper = menu.findItem(R.id.set_wallpaper);
        menuDownload = menu.findItem(R.id.download);
        menuFavorite = menu.findItem(R.id.favorite);
        menuShare = menu.findItem(R.id.share);

        boolean isFloatSearch = mSetting.floatSearch();
        floatSearch.setVisibility(isFloatSearch ? View.VISIBLE : View.INVISIBLE);
        if (menuSearch != null) {
            menuSearch.setVisible(!isFloatSearch);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (providerUri != null && !providerUri.equals(mSetting.provider())) {

            providerUri = mSetting.provider();

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

        if (viewPager.getVisibility() == View.GONE) {
            setDefaultTitle();
            boolean isFloatSearch = mSetting.floatSearch();
            floatSearch.setVisibility(isFloatSearch ? View.VISIBLE : View.INVISIBLE);
            if (menuSearch != null) {
                menuSearch.setVisible(!isFloatSearch);
            }
        } else {
            boolean isFloatFavorite = mSetting.floatFavorite();
            floatFavorite.setVisibility(isFloatFavorite ? View.VISIBLE : View.INVISIBLE);
            if (menuFavorite != null) {
                menuFavorite.setVisible(!isFloatFavorite);
            }
        }

        checkUpdate();
    }

    void checkUpdate() {
        try {
            PackageManager packageManager = getPackageManager();
            String versionString = packageManager.getPackageInfo(getPackageName(), 0).versionName;

            mDataSource.checkUpdate(versionString).subscribe(new Action1<GithubRelease.Asset>() {
                @Override
                public void call(GithubRelease.Asset asset) {
                    showUpdateDialog(asset.getName(), asset.getSize(),
                            asset.getBrowserDownloadUrl());
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    onError(throwable.getMessage());
                }
            });

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
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

        //        Log.e(TAG, "drawerLayout.getRootView().getHeight(): "+drawerLayout.getRootView().getHeight());
        //        Log.e(TAG, "drawerLayout.getRootView().getWidth(): "+drawerLayout.getRootView().getWidth());
        //        Log.e(TAG, "metrics.heightPixels: "+metrics.heightPixels);
        //        Log.e(TAG, "metrics.widthPixels: "+metrics.widthPixels);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            right = drawerLayout.getRootView().getHeight() - metrics.widthPixels;
        }

        params.setMargins(0, top, right, 0);
        toolbar.requestLayout();

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            RelativeLayout.LayoutParams favoriteParams =
                    (RelativeLayout.LayoutParams) floatFavorite.getLayoutParams();
            if (favoriteRightMargin == 0) {
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

            RelativeLayout.LayoutParams favoriteParams =
                    (RelativeLayout.LayoutParams) floatFavorite.getLayoutParams();
            if (favoriteRightMargin == 0) {
                favoriteRightMargin = favoriteParams.rightMargin;
            }
            favoriteParams.rightMargin =
                    favoriteRightMargin + Utils.getNavigationBarHeight(this, newConfig.orientation);

            floatFavorite.setShowPosition(metrics.heightPixels - favoriteBottomMargin);
            floatFavorite.setHidePosition(
                    metrics.heightPixels - favoriteBottomMargin - (favoriteShowPosition
                            - favoriteHidePosition));
        }
    }

    void launchSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    void setWallpaper() {
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(currentImageUri, "image/*");
        intent.putExtra("mimeType", "image/*");
        this.startActivity(Intent.createChooser(intent, getResources().getString(R.string.set_as)));
    }

    void share() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, currentImageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.share)));
    }

    void showDetails() {
        Image image = currentImage;

        if (mDataSource.getCachedDetail(image.getFileUrl()) != null) {
            image = mDataSource.getCachedDetail(image.getFileUrl());
        }

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

    void downloadImage() {
        if (!hasStoragePermission()) {
            return;
        }
        Image image = currentImage;

        if (mDataSource.getCachedDetail(image.getFileUrl()) != null) {
            image = mDataSource.getCachedDetail(image.getFileUrl());
        }

        download(image);
    }

    private boolean hasStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_STORAGE);
            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE);
            return false;
        }
        return true;
    }

    void switchFavorite() {
        Image image = currentImage;
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

        mGalleryAdapter.reload(tags);
    }

    @UiThread
    void reloadFromHistory() {
        isInFavoriteMode = false;
        favorite.setTextColor(getResources().getColor(android.R.color.white));
        mGalleryAdapter.reloadFromHistory();
    }

    @UiThread
    void reloadFromFavorite() {
        isInHistoryMode = false;
        history.setTextColor(getResources().getColor(android.R.color.white));
        mGalleryAdapter.reloadFromFavorite();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                search();
                break;
            case R.id.favorite:
                switchFavorite();
                break;
            case R.id.download:
                downloadImage();
                break;
            case R.id.share:
                share();
                break;
            case R.id.set_wallpaper:
                setWallpaper();
                break;
            case R.id.info:
                showDetails();
                break;
            case R.id.settings:
                launchSettings();
                break;
        }

        return true;
    }

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
                                mDataSource.listTag(tags)
                                        .subscribe(new Action1<List<? extends Tag>>() {
                                            @Override
                                            public void call(List<? extends Tag> tagList) {
                                                listTagDialog(tags, tagList);
                                            }
                                        }, new Action1<Throwable>() {
                                            @Override
                                            public void call(Throwable throwable) {
                                                showErrorDialog(throwable.getMessage());
                                            }
                                        });
                            }
                        });

        dialog = builder.create();
        dialog.show();
    }

    public void listTagDialog(String tag, final List<? extends Tag> tagList) {

        hideProgressDialog();

        if (tag.contains("&&") || tag.contains("||")) {
            clearModes();
            reload();
        } else if (tagList.size() == 1) {
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

    @Override
    protected void onResume() {
        super.onResume();

        if (mSetting.autoDownload()) {
            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_STORAGE);
            askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE);
        }
    }

    public void login(String username, String password) {

        String apiUri = mSetting.provider();

        Retrofit.Builder restAdapter = mBuilder.baseUrl(apiUri);

        switch (apiUri) {
            case Providers.ANIME_PICTURES_URI:
                AnimePictures animePictures = restAdapter.build().create(AnimePictures.class);
                animePictures.login(RequestBody.create(MediaType.parse("text/plain"), username),
                        RequestBody.create(MediaType.parse("text/plain"), password),
                        RequestBody.create(MediaType.parse("text/plain"),
                                TimeZone.getDefault().getID()))
                        .enqueue(new Callback<AnimePicturesUser>() {
                            @Override
                            public void onResponse(retrofit2.Call<AnimePicturesUser> call,
                                    Response<AnimePicturesUser> response) {
                                try {
                                    AnimePicturesUser user = response.body();

                                    if (user.getSuccess()) {
                                        makeToast(R.string.login_success);
                                        mSetting.setAnimePicturesToken(user.getToken());
                                        mSetting.setAnimePicturesExpireDate(
                                                System.currentTimeMillis()
                                                        + 3600 * 24 * 360 * 1000L);
                                    } else {
                                        makeToast(R.string.login_failed);
                                        mSetting.setAnimePicturesToken("");
                                    }

                                    Headers headers = response.headers();

                                    for (String cookie : headers.values("set-cookie")) {
                                        if (cookie.contains("_server")) {
                                            processAnimeCookie(cookie);
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<AnimePicturesUser> call,
                                    Throwable t) {

                            }
                        });

                break;
            default:
                // do nothing
        }
        hideProgressDialog();
    }

    private void processAnimeCookie(String cookie) throws Exception {
        String[] values = cookie.split(";");
        String server = values[0].split("=")[0];
        mSetting.setAnimePicturesServer(server);

        String expired = values[1].split("=")[1];

        Log.d(TAG, "server: " + server + "," + "expired:"
                + expired);

        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        Date mDate = sdf.parse(expired);
        long date = mDate.getTime();

        mSetting.setAnimePicturesExpireDate(date);
        Log.d(TAG, "expired date: " + date);
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

                    if (viewPager.getVisibility() == View.VISIBLE) {
                        hideSystemUI();
                    }
                }
            }, 300);
        }
    }

    public void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.getWindow().getAttributes().windowAnimations = android.R.anim.fade_in;
            progressDialog.show();
        }
    }

    @UiThread
    public void runExitAnimation(final ViewPager target, final View to, final int orientation) {

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

        //photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                if (orientation == getRequestedOrientation()
                        || orientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
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
                                        (int) ((thumbnailLeft - fullLeft) * animatedProgress
                                                / progress) -
                                                parent.getPaddingLeft();

                            } else if (!turn) {
                                //photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
                                //photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

        //photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);

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

                    if (orientation == getRequestedOrientation()
                            || orientation == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
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
                                //photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
                                //photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
                        //photoViewAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    }
                }
            }
        });
        valueAnimator.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //photoViewAttacher.setMinimumScale(1f);
                turn = false;
            }
        }, duration + 100);
    }

    @UiThread
    public void showErrorDialog(String message) {
        messageDialog.setContent(getString(R.string.connect_error) + " (" + message + ")");
        messageDialog.show();
    }

    @UiThread
    public void showUpdateDialog(String name, long size, String uri) {

        updateFileName = name;
        updateUrl = uri;

        updateDialog.setContent(getString(
                R.string.new_version_available) + " \n" + name + " ("
                + Utils.humanReadableByteCount(
                size, true) + ")");
        updateDialog.show();
    }

    public File getDownloadDir() {
        return downloadDir;
    }

    public void download(final Image image) {
        mRequestManager.load(new GlideUrl(image.getFileUrl()))
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        showErrorDialog(e.getMessage());
                    }

                    @Override
                    public void onResourceReady(File resource,
                            GlideAnimation<? super File> glideAnimation) {
                        File dir = new File(
                                Environment.getExternalStorageDirectory().getPath(), "MoeGallery");
                        String fileName = image.getName().replace('/', '-');
                        Utils.copy(resource, dir, fileName);
                        mDataSource.cacheImageUri(currentImage.getFileUrl(),
                                Uri.fromFile(new File(dir, fileName)));

                        makeToast(R.string.download_complete);

                        if (reloadImageCallback != null) {
                            reloadImageCallback.onReloadImage(image);
                        }
                    }
                });
    }

    void downloadUpdate() {

        if (!hasStoragePermission()) {
            return;
        }

        if (!updateDir.exists()) {
            updateDir.mkdir();
        }

        showProgressDialog();

        mDataSource.downloadUpdate(updateDir, updateFileName, updateUrl).subscribe(
                new Action1<Uri>() {
                    @Override
                    public void call(Uri uri) {
                        hideProgressDialog();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onError(throwable.getMessage());
                    }
                });
    }

    public void setReloadImageCallback(ReloadImageCallback callback) {
        reloadImageCallback = callback;
    }

    public void updateOrientation(int width, int height) {
        if (width == height) {
            return;
        }

        if (mPagerAdapter.getCount() == 1) {
            return;
        }

        int orientation = getRequestedOrientation();
        if (width <= height && orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (width > height && orientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (currentImage != null) {

            Image image = mDataSource.getCachedDetail(currentImage.getFileUrl());
            if (image != null) {
                String fileName = image.getName().replace('/', '-');
                File file = new File(Utils.getDir(), fileName);
                if (file.exists()) {
                    mDataSource.cacheImageUri(currentImage.getFileUrl(), Uri.fromFile(file));
                }
            }

            currentImageUri = mDataSource.getImageUri(currentImage.getFileUrl());
        }

        if (currentImageUri != null && viewPager.getVisibility() == View.VISIBLE) {
            menuWallpaper.setVisible(true);
            menuShare.setVisible(true);
            menuDownload.setVisible(false);
        } else {
            menuWallpaper.setVisible(false);
            menuShare.setVisible(false);
        }

        if (currentImageUri == null && viewPager.getVisibility() == View.VISIBLE &&
                !mSetting.autoDownload()) {
            menuDownload.setVisible(true);
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onPageSelected(int position) {
        currentImage = mDataSource.get(position);
        if (currentImage != null) {
            String title = currentImage.getName();
            Image image = mDataSource.getCachedDetail(currentImage.getFileUrl());
            if (image != null) {
                title = image.getName();
            }
            setTitle(title);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setPagingEnabled(boolean enabled) {
        viewPager.setPagingEnabled(enabled);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mTouchEventListener != null) {
            mTouchEventListener.onDispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setTouchEventListener(TouchEventListener listener) {
        mTouchEventListener = listener;
    }

    @Override
    public void onListUpdate() {
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        hideProgressDialog();

        showLoginSnakeBar();
    }

    @Override
    public void onError(String message) {
        hideProgressDialog();
        showErrorDialog(message);
    }

    @Override
    public void onItemClick(View view, int position) {

        final Image image = mDataSource.get(position);

        if (floatSearch.isShow()) {
            floatSearch.hide();
        }

        viewPager.setCurrentItem(position, false);
        viewPager.setVisibility(View.VISIBLE);

        setCurrentImage(image);
        setMenu(false);
        hideSystemUIDelayed(0);
    }

    private void askForPermission(String permission, int requestCode) {
        if (ActivityCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission },
                    requestCode);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission },
                    requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && ActivityCompat.checkSelfPermission(this, permissions[0])
                != PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case READ_STORAGE:
                case WRITE_STORAGE:
                    mSetting.setAutoDownload(false);
                    break;
            }
        }
    }

    private void showLoginSnakeBar() {

        if (mDataSource.getCount() == 0) {
            return;
        }

        final Image image = mDataSource.get(0);

        if (image instanceof AnimePicturesList.AnimePicturesPreview) {
            if (mSetting.animePicturesToken().isEmpty()
                    || mSetting.animePicturesExpireDate() < System.currentTimeMillis()) {

                Snackbar snackbar = Snackbar.make(recyclerView, R.string.login_text, 5000);
                snackbar.setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login();
                    }
                });
                snackbar.show();
            }
        }
    }

    public interface ReloadImageCallback {
        void onReloadImage(Image image);
    }

    public interface TouchEventListener {
        boolean onDispatchTouchEvent(MotionEvent motionEvent);
    }
}