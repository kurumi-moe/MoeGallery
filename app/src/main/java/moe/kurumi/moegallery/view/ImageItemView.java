package moe.kurumi.moegallery.view;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import moe.kurumi.moegallery.R;
import moe.kurumi.moegallery.activity.MainActivity;
import moe.kurumi.moegallery.model.AnimePictures;
import moe.kurumi.moegallery.model.AnimePicturesList;
import moe.kurumi.moegallery.model.Image;
import moe.kurumi.moegallery.model.Preferences_;
import moe.kurumi.moegallery.model.database.HistoryImage;
import moe.kurumi.moegallery.model.database.HistoryImage$Table;
import moe.kurumi.moegallery.provider.Providers;
import moe.kurumi.moegallery.utils.MD5;
import moe.kurumi.moegallery.utils.Utils;
import pl.droidsonroids.gif.GifDrawable;
import retrofit.RestAdapter;

/**
 * Created by kurumi on 15-5-28.
 */

@EViewGroup(R.layout.image_item)
public class ImageItemView extends FrameLayout implements MainActivity.ReloadImageCallback {

    public final static String TAG = "ImageItemView";

    @ViewById
    SquareImageView imageView;

    @ViewById
    ImageView gifTag;

    @ViewById
    TextView resolution;

    @ViewById
    TextView size;

    @ViewById
    TextView type;

    MainActivity activity;

    ImageView image;

    File downloadDir;

    int position;

    @Pref
    Preferences_ preferences;

    public ImageItemView(Context context) {
        super(context);
        activity = (MainActivity) context;
        image = (ImageView) activity.findViewById(R.id.image);

        downloadDir = activity.getDownloadDir();
    }

    public void bind(Image image, int position) {
        this.position = position;
        imageView.setTag(image);

        if (Utils.isGif(image.getFileUrl())) {
            gifTag.setVisibility(VISIBLE);
        } else {
            gifTag.setVisibility(GONE);
        }

        Picasso.with(imageView.getContext()).load(image.getPreviewUrl()).into(imageView);
    }

    @Click
    void imageViewClicked(final View view) {
        final Image image = (Image) view.getTag();

        if (image instanceof AnimePicturesList.AnimePicturesPreview) {
            if (((AnimePicturesList.AnimePicturesPreview) image).getErotics() > 1) {
                if (preferences.animePicturesToken().get().isEmpty()) {
                    // need login
                    activity.login();
                    return;
                }
            }
        }

        this.image.setTag(image);

        activity.hideSystemUIDelayed(0);

        activity.getPhotoViewAttacher().setZoomable(false);
        Picasso.with(view.getContext()).load(image.getPreviewUrl()).into(this.image);
        activity.getPhotoViewAttacher().setZoomable(true);

        //activity.showProgressDialog();
        activity.setMenu(false);

        int orientation = activity.getRequestedOrientation();

        if (image.getWidth() <= image.getHeight() &&
                orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (image.getWidth() > image.getHeight() &&
                orientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        activity.setCurrentImage(image);
        activity.runEnterAnimation(this.image, view, orientation);
        activity.setImageUri(null);

        if (!loadFromLocal(image)) {
            loadHighQualityImage(view, image);
        }

        //this.image.setVisibility(VISIBLE);
    }

    @Background
    void loadHighQualityImage(View view, Image imageSource) {

        try {
            final Picasso picasso;
            final Image image;

            String apiUri = preferences.provider().get();
            OkHttpClient picassoClient = new OkHttpClient();

            switch (apiUri) {
                case Providers.BEHOIMI_URI:
                    // add refer header
                    picassoClient.interceptors().add(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Referer", Providers.BEHOIMI_URI + "/")
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    });

                    image = imageSource;
                    break;
                case Providers.ANIME_PICTURES_URI:

                    // check if the image have been visited
                    List<HistoryImage> historyImages = new Select().from(HistoryImage.class).where(
                            Condition.column(HistoryImage$Table.PREVIEWURL).eq(imageSource.getPreviewUrl())).queryList();

                    if (historyImages.size() == 0) {
                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(apiUri)
                                //.setLogLevel(RestAdapter.LogLevel.FULL)
                                .build();
                        AnimePictures animePictures = restAdapter.create(AnimePictures.class);

                        String cookie = preferences.animePicturesServer().get() + "=" +
                                preferences.animePicturesToken().get();

                        image = animePictures.post(imageSource.getId(), "json", "en", cookie);
                    } else {
                        image = historyImages.get(0);
                    }
                    break;
                default:
                    image = imageSource;
            }

            // save to database
            saveImage(image);

            this.image.setTag(image);
            picasso = new Picasso.Builder(getContext()).downloader(
                    new OkHttpDownloader(picassoClient)).build();

            loadImage(picasso, image);
        } catch (Exception e) {
            e.printStackTrace();
            activity.showErrorDialog(e.getMessage());
        }

    }

    private void saveImage(Image image) {
        List<HistoryImage> historyImages = new Select().from(HistoryImage.class).where(
                Condition.column(HistoryImage$Table.PREVIEWURL).eq(image.getPreviewUrl())).queryList();

        if (historyImages.size() == 1) {
            historyImages.get(0).updateLast();
            historyImages.get(0).save();
        } else {
            HistoryImage historyImage = new HistoryImage(image);
            historyImage.save();
        }
    }

    @UiThread
    void loadImage(final Picasso picasso, final Image image) {
        if (!loadFromLocal(image)) {
            activity.showProgressDialog();
            if (Utils.isGif(image.getFileUrl())) {
                download(image);
            } else {
                picasso.load(image.getSampleUrl()).fetch(
                        new Callback() {
                            @Override
                            public void onSuccess() {
                                activity.getPhotoViewAttacher().setZoomable(false);
                                picasso.load(image.getSampleUrl()).into(
                                        ImageItemView.this.image);
                                activity.getPhotoViewAttacher().setZoomable(true);
                                activity.hideProgressDialog();
                                if (preferences.autoDownload().get()) {
                                    download(image);
                                } else {
                                    activity.setReloadImageCallback(ImageItemView.this);
                                }
                            }

                            @Override
                            public void onError() {
                                Log.d(TAG, "image fetch error.");
                            }
                        });
            }
            setTitle(image.getName());
        }
    }

    boolean loadFromLocal(final Image image) {
        final File downloadedFile = new File(downloadDir, image.getName().replace('/', '-'));
        boolean isExist = downloadedFile.exists() && MD5.checkMD5(image.getMd5(), downloadedFile);
        if (isExist && ((Image) this.image.getTag()).getFileUrl().equals(image.getFileUrl())) {

            if (Utils.isGif(image.getName())) {
                loadGif(downloadedFile);
            } else {
                final RequestCreator creator;

                if (image.getHeight() > 2048 || image.getWidth() > 2048) {
                    if (image.getHeight() > image.getWidth()) {
                        creator = Picasso.with(activity).load(downloadedFile).resize(0, 2048);
                    } else {
                        creator = Picasso.with(activity).load(downloadedFile).resize(2048, 0);
                    }
                } else {
                    creator = Picasso.with(activity).load(downloadedFile);
                }

                creator.fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        loadImage(creator);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
            setTitle(image.getName());
            activity.setImageUri(Uri.fromFile(downloadedFile));
        }
        return isExist;
    }

    @UiThread
    void loadImage(RequestCreator creator) {
        activity.getPhotoViewAttacher().setZoomable(false);
        creator.into(ImageItemView.this.image);
        activity.getPhotoViewAttacher().setZoomable(true);
        activity.hideProgressDialog();
    }

    @UiThread
    void setTitle(String title) {
        if (image.getVisibility() == VISIBLE && ((Image) image.getTag()).getName().equals(title)) {
            activity.setTitle(title);
        }
    }

    @Background
    void loadGif(File gifFile) {
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(gifFile),
                    (int) gifFile.length());
            GifDrawable gifFromStream = new GifDrawable(bis);
            setGifDrawable(gifFromStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void setGifDrawable(GifDrawable gifFromStream) {
        activity.getPhotoViewAttacher().setZoomable(false);
        ImageItemView.this.image.setImageDrawable(gifFromStream);
        activity.getPhotoViewAttacher().setZoomable(true);
        activity.hideProgressDialog();
    }

    @Background
    void download(Image image) {

        activity.download(image, new MainActivity.DownloadCallback() {
            @Override
            public void onSucceed(Image image, File file) {
                loadFromLocal(image);
            }

            @Override
            public void onFailed(Image image, String message) {
                activity.hideProgressDialog();
                activity.showErrorDialog(message);
            }
        });
    }

    @Override
    public void onReloadImage(Image image) {
        loadFromLocal(image);
    }
}
