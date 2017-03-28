package moe.kurumi.moegallery.fragment;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.almeros.android.multitouch.RotateGestureDetector;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

import moe.kurumi.moegallery.R;
import moe.kurumi.moegallery.activity.MainActivity;
import moe.kurumi.moegallery.data.ImageDataSource;
import moe.kurumi.moegallery.data.Providers;
import moe.kurumi.moegallery.di.DaggerMainComponent;
import moe.kurumi.moegallery.di.modules.AppModule;
import moe.kurumi.moegallery.di.modules.MainModule;
import moe.kurumi.moegallery.model.AnimePictures;
import moe.kurumi.moegallery.model.AnimePicturesImage;
import moe.kurumi.moegallery.model.Image;
import moe.kurumi.moegallery.model.database.HistoryImage;
import moe.kurumi.moegallery.model.database.HistoryImage$Table;
import moe.kurumi.moegallery.model.setting.Setting;
import moe.kurumi.moegallery.utils.OkHttp;
import moe.kurumi.moegallery.utils.Utils;
import moe.kurumi.moegallery.view.DraggableRelativeLayout;
import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFragment extends Fragment implements MainActivity.TouchEventListener {

    private static final String TAG = ImageFragment.class.getSimpleName();

    @Inject
    RequestManager mRequestManager;

    @Inject
    Setting mSetting;

    @Inject
    GenericRequestBuilder<GlideUrl, InputStream, byte[], GifDrawable> mGifRequestBuilder;

    @Inject
    Retrofit.Builder mBuilder;

    @Inject
    ImageDataSource mImageDataSource;

    private boolean isVisibleToUser = false;
    private int width = -1;
    private int height = -1;
    private DraggableRelativeLayout mDraggableLayout;
    private PhotoView mPhotoView;
    private ProgressBar mProgressBar;

    private String mUri;
    private String mName;

    private Handler mHandler;

    private RotateGestureDetector mRotationDetector;

    private Runnable mUpdateGifRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPhotoView != null && mPhotoView.getDrawable() instanceof Animatable) {
                Animatable animatable = (Animatable) mPhotoView.getDrawable();
                if (isVisibleToUser) {
                    animatable.start();
                } else {
                    animatable.stop();
                }
            }

        }
    };

    private Runnable mUpdateOrientationRunnable = new Runnable() {
        @Override
        public void run() {
            if (getActivity() != null && mSetting.autoRotate()) {
                ((MainActivity) getActivity()).updateOrientation(width, height);
            }
        }
    };

    private Runnable mUpdateTransitionRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPhotoView != null) {
                ViewCompat.setTransitionName(mPhotoView, isVisibleToUser ? mName : null);
            }
        }
    };

    private DraggableRelativeLayout.DragListener mDragListener =
            new DraggableRelativeLayout.DragListener() {
                final float threshold = 0.85f;
                float releaseScale = 1f;
                float scale = 1f;

                @Override
                public void onDraggedVertical(int top, int height) {
                    scale = (height - Math.abs(top)) / (float) height;
                    if (scale >= threshold) {
                        mPhotoView.setMinimumScale(threshold);
                        mPhotoView.setScale(scale);
                        mPhotoView.setAlpha(scale);
                    }
                }

                @Override
                public void onViewReleased(float xvel, float yvel) {
                    mPhotoView.setMinimumScale(1f);
                    releaseScale = scale;
                }

                @Override
                public void onViewDragFinished() {
                    if (getActivity() != null && releaseScale < threshold) {
                        getActivity().onBackPressed();
                    }
                    releaseScale = 1f;
                }
            };
    private RotateGestureDetector.OnRotateGestureListener mOnRotateGestureListener =
            new RotateGestureDetector.OnRotateGestureListener() {
                int rotate = 0;

                @Override
                public boolean onRotate(RotateGestureDetector detector) {

                    if (mPhotoView != null) {
                        float degree = detector.getRotationDegreesDelta();
                        mPhotoView.setRotation(-degree + rotate);
                    }
                    return false;
                }

                @Override
                public boolean onRotateBegin(RotateGestureDetector detector) {
                    mDraggableLayout.setDraggable(false);
                    if (getActivity() != null) {
                        ((MainActivity) getActivity()).setPagingEnabled(false);
                    }
                    //rotate = mDataSource.getRotate(mUri);
                    return true;
                }

                @Override
                public void onRotateEnd(RotateGestureDetector detector) {
                    if (mPhotoView != null) {
                        // set rotation and save state
                        float degree = -detector.getRotationDegreesDelta() + rotate;

                        int n = Math.round(degree / 90);
                        n = n % 4;
                        if (n < 0) {
                            n += 4;
                        }
                        switch (n) {
                            case 0:
                                mPhotoView.setRotation(0);
                                break;
                            case 1:
                                mPhotoView.setRotation(90);
                                break;
                            case 2:
                                mPhotoView.setRotation(180);
                                break;
                            case 3:
                                mPhotoView.setRotation(270);
                                break;
                        }
                        //mDataSource.setRotate(mUri, (int) mPhotoView.getRotation());
                        rotate = 0;
                    }

                    mDraggableLayout.setDraggable(true);

                    if (getActivity() != null) {
                        ((MainActivity) getActivity()).setPagingEnabled(true);
                    }
                }
            };
    private Image mImage;

    public ImageFragment() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static ImageFragment newInstance(Image image) {
        ImageFragment fragment = new ImageFragment();
        fragment.setImage(image);
        return fragment;
    }

    private static LazyHeaders.Builder header() {
        return new LazyHeaders.Builder().addHeader("User-Agent", OkHttp.USER_AGENT);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        DaggerMainComponent.builder()
                .appModule(new AppModule(getActivity().getApplication()))
                .mainModule(new MainModule((MainActivity) getActivity()))
                .build().inject(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        this.isVisibleToUser = isVisibleToUser;

        if (isVisibleToUser) {
            updateOrientation();
            if (getActivity() != null) {
                ((MainActivity) getActivity()).setTouchEventListener(this);
            }
        }
        mHandler.removeCallbacks(mUpdateGifRunnable);
        mHandler.postDelayed(mUpdateGifRunnable, 10);

        updateTransition();

    }

    private void updateTransition() {
        mHandler.removeCallbacks(mUpdateTransitionRunnable);
        mHandler.postDelayed(mUpdateTransitionRunnable, 10);
    }

    private void updateOrientation() {
        mHandler.removeCallbacks(mUpdateOrientationRunnable);
        mHandler.postDelayed(mUpdateOrientationRunnable, 10);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mUpdateGifRunnable);
        mHandler.removeCallbacks(mUpdateOrientationRunnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.full_image, container, false);

        mRotationDetector = new RotateGestureDetector(getActivity(), mOnRotateGestureListener);

        mDraggableLayout = (DraggableRelativeLayout) view.findViewById(R.id.layout);
        mDraggableLayout.setDragListener(mDragListener);
        mDraggableLayout.setThreshold(0.15f);

        mPhotoView = (PhotoView) view.findViewById(R.id.image);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);

        ViewCompat.setTransitionName(mPhotoView, mName);

        updateTransition();

        String apiUri = mSetting.provider();
        Image image = mImage;
        final LazyHeaders.Builder header = header();

        mRequestManager.load(new GlideUrl(mImage.getPreviewUrl(), header.build()))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mPhotoView);

        switch (apiUri) {
            case Providers.BEHOIMI_URI:
                // add refer header
                header.addHeader("Referer", Providers.BEHOIMI_URI + "/");

                cacheImage(image, header);
                break;
            case Providers.ANIME_PICTURES_URI:
                // check if the image have been visited
                List<HistoryImage> historyImages = new Select().from(HistoryImage.class)
                        .where(Condition.column(HistoryImage$Table.PREVIEWURL)
                                .eq(image.getPreviewUrl())).queryList();

                if (historyImages.size() == 0) {
                    Retrofit restAdapter = mBuilder
                            .baseUrl(apiUri)
                            .build();
                    AnimePictures animePictures = restAdapter.create(AnimePictures.class);

                    String cookie = mSetting.animePicturesServer() + "=" +
                            mSetting.animePicturesToken();

                    animePictures.post(image.getId(), "json", "en", cookie).enqueue(
                            new Callback<AnimePicturesImage>() {
                                @Override
                                public void onResponse(Call<AnimePicturesImage> call,
                                        Response<AnimePicturesImage> response) {
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    Image i = response.body();
                                    cacheImage(i, header);
                                }

                                @Override
                                public void onFailure(Call<AnimePicturesImage> call, Throwable t) {

                                }
                            });
                } else {
                    image = historyImages.get(0);
                    mImageDataSource.cacheDetail(mImage.getFileUrl(), image);
                    cacheImage(image, header);
                }
                break;
            default:
                cacheImage(image, header);
                break;
        }

        mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                MainActivity out = (MainActivity) getActivity();
                if (!out.isSystemUIVisible()) {
                    out.setTitle(mName);
                    out.showSystemUI();
                    out.hideSystemUIDelayed(3000);
                    out.showFavorite();
                } else {
                    out.hideSystemUIDelayed(0);
                }
            }
        });

        //mPhotoView.setRotation(mDataSource.getRotate(mUri));
        return view;
    }

    private void cacheImage(final Image image, final LazyHeaders.Builder header) {
        mName = image.getName();
        mUri = image.getFileUrl();

        saveImage(image);

        mRequestManager.load(new GlideUrl(image.getFileUrl(), header.build()))
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource,
                            GlideAnimation<? super File> glideAnimation) {
                        if (mSetting.autoDownload()) {
                            File dir = new File(
                                    Environment.getExternalStorageDirectory().getPath(),
                                    "MoeGallery");
                            String fileName = image.getName().replace('/', '-');
                            Utils.copy(resource, dir, fileName);
                            mImageDataSource.cacheImageUri(mImage.getFileUrl(),
                                    Uri.fromFile(new File(dir, fileName)));
                        }
                        loadImage(image, header);
                    }
                });
        mImageDataSource.cacheDetail(mImage.getFileUrl(), image);
    }

    // save to database
    private void saveImage(Image image) {
        List<HistoryImage> historyImages = new Select().from(HistoryImage.class)
                .where(
                        Condition.column(HistoryImage$Table.PREVIEWURL).eq(image.getPreviewUrl()))
                .queryList();

        if (historyImages.size() == 1) {
            historyImages.get(0).updateLast();
            historyImages.get(0).save();
        } else {
            HistoryImage historyImage = new HistoryImage(image);
            historyImage.save();
        }
    }

    private void loadImage(Image image, LazyHeaders.Builder header) {

        if (mUri != null && mUri.toLowerCase().endsWith("gif")) {
            mGifRequestBuilder.load(new GlideUrl(image.getFileUrl(), header.build()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new MediaRequestListener<GlideUrl, GifDrawable>())
                    .into(mPhotoView);
        } else {
            mRequestManager.load(new GlideUrl(image.getFileUrl(), header.build()))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .listener(new MediaRequestListener<GlideUrl, GlideDrawable>())
                    .into(mPhotoView);
        }
    }

    @Override
    public boolean onDispatchTouchEvent(MotionEvent motionEvent) {
        return mRotationDetector.onTouchEvent(motionEvent);
    }

    public void setImage(Image image) {
        mImage = image;
    }

    private class MediaRequestListener<T, V extends Drawable> implements RequestListener<T, V> {

        @Override
        public boolean onException(Exception e, T model, Target<V> target,
                boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(final V resource, T model, Target<V> target,
                boolean isFromMemoryCache,
                boolean isFirstResource) {
            width = resource.getIntrinsicWidth();
            height = resource.getIntrinsicHeight();

            if (isVisibleToUser) {
                updateOrientation();

                if (getActivity() != null) {
                    ((MainActivity) getActivity()).setTouchEventListener(ImageFragment.this);
                }
            }

            if (resource instanceof Animatable) {
                mHandler.removeCallbacks(mUpdateGifRunnable);
                mHandler.postDelayed(mUpdateGifRunnable, 10);
            }
            mProgressBar.setVisibility(View.GONE);
            return false;
        }
    }
}