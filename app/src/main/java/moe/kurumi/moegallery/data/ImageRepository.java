package moe.kurumi.moegallery.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.UiThread;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import moe.kurumi.moegallery.application.Application;
import moe.kurumi.moegallery.model.AnimePictures;
import moe.kurumi.moegallery.model.AnimePicturesList;
import moe.kurumi.moegallery.model.Behoimi;
import moe.kurumi.moegallery.model.Config;
import moe.kurumi.moegallery.model.Danbooru;
import moe.kurumi.moegallery.model.DanbooruTag;
import moe.kurumi.moegallery.model.Gelbooru;
import moe.kurumi.moegallery.model.GelbooruList;
import moe.kurumi.moegallery.model.Github;
import moe.kurumi.moegallery.model.GithubRelease;
import moe.kurumi.moegallery.model.Image;
import moe.kurumi.moegallery.model.Moebooru;
import moe.kurumi.moegallery.model.Tag;
import moe.kurumi.moegallery.model.Version;
import moe.kurumi.moegallery.model.database.FavoriteImage;
import moe.kurumi.moegallery.model.database.FavoriteImage$Table;
import moe.kurumi.moegallery.model.database.HistoryImage;
import moe.kurumi.moegallery.model.database.HistoryImage$Table;
import moe.kurumi.moegallery.model.setting.Setting;
import moe.kurumi.moegallery.utils.Utils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ImageRepository implements ImageDataSource {

    public static final int LIMIT = 20;

    @Inject
    Setting mSetting;

    @Inject
    Context context;
    @Inject
    Retrofit.Builder mBuilder;
    @Inject
    @Named("xml")
    Retrofit.Builder mXmlBuilder;
    private List<Image> mImageList = new ArrayList<>();
    private Map<String, Image> mDetailImageMap = new HashMap<>();
    private Map<String, Uri> mImageUri = new HashMap<>();
    private OnListUpdateListener listUpdateListener;
    private int page = 0;

    private ImageRepository() {
        Application.getAppComponent().inject(this);
    }

    public static ImageRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public Observable<List<? extends Image>> loadList(final String tags) {

        return Observable.create(new Observable.OnSubscribe<List<? extends Image>>() {
            @Override
            public void call(Subscriber<? super List<? extends Image>> subscriber) {
                try {
                    page++;

                    String apiUri = mSetting.provider();

                    Retrofit restAdapter = mBuilder
                            .baseUrl(apiUri)
                            .build();

                    List<? extends Image> images;

                    switch (apiUri) {
                        case Providers.DANBOORU_URI:
                            Danbooru danbooru = restAdapter.create(Danbooru.class);
                            images = danbooru.list(LIMIT, page, tags).execute().body();
                            break;
                        case Providers.KONACHAN_URI:
                        case Providers.YANDERE_URI:
                            Moebooru moebooru = restAdapter.create(Moebooru.class);
                            images = moebooru.list(LIMIT, page, tags.isEmpty() ? "*" : tags)
                                    .execute()
                                    .body();
                            break;
                        case Providers.BEHOIMI_URI:
                            Behoimi behoimi = restAdapter.create(Behoimi.class);
                            images = behoimi.list(LIMIT, page, tags).execute().body();
                            break;
                        case Providers.ANIME_PICTURES_URI:
                            AnimePictures animePictures = restAdapter.create(AnimePictures.class);
                            AnimePicturesList animePicturesList;

                            if (tags.isEmpty()) {
                                animePicturesList = animePictures.list(page - 1, "json", "en")
                                        .execute()
                                        .body();
                            } else {
                                animePicturesList = animePictures.search(page - 1, tags, "date", 0,
                                        "json",
                                        "en").execute().body();
                            }

                            images = animePicturesList.getPreviews();
                            break;
                        case Providers.GELBOORU_URI:

                            restAdapter = mXmlBuilder
                                    .baseUrl(apiUri)
                                    .build();
                            Gelbooru gelbooru = restAdapter.create(Gelbooru.class);
                            GelbooruList gelbooruList = gelbooru.list(LIMIT, page - 1, tags)
                                    .execute()
                                    .body();

                            images = gelbooruList.getPost();

                            break;
                        default:
                            images = new ArrayList<>();
                    }

                    for (Image image : images) {
                        mImageList.add(image);
                    }

                    subscriber.onNext(images);

                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    page--;
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<? extends Tag>> listTag(final String tag) {

        return Observable.create(new Observable.OnSubscribe<List<? extends Tag>>() {
            @Override
            public void call(Subscriber<? super List<? extends Tag>> subscriber) {
                try {
                    String apiUri = mSetting.provider();

                    List<? extends Tag> tags;

                    Retrofit restAdapter = mBuilder
                            .baseUrl(apiUri)
                            //.setLogLevel(RestAdapter.LogLevel.FULL)
                            .build();

                    switch (apiUri) {
                        case Providers.KONACHAN_URI:
                        case Providers.YANDERE_URI:
                            Moebooru moebooru = restAdapter.create(Moebooru.class);
                            tags = moebooru.tag(tag.trim().replace(' ', '_'))
                                    .execute()
                                    .body();
                            break;
                        case Providers.DANBOORU_URI:
                            Danbooru danbooru = restAdapter.create(Danbooru.class);
                            List<DanbooruTag> tagsStart = danbooru.tag("*" + tag.trim())
                                    .execute()
                                    .body();
                            List<DanbooruTag> tagsEnd = danbooru.tag(tag.trim() + "*")
                                    .execute()
                                    .body();
                            tagsStart.addAll(tagsEnd);
                            tags = tagsStart;
                            break;
                        case Providers.BEHOIMI_URI:
                            Behoimi behoimi = restAdapter.create(Behoimi.class);
                            tags = behoimi.tag("*" + tag.trim().replace(' ', '_') + "*")
                                    .execute()
                                    .body();
                            break;
                        case Providers.ANIME_PICTURES_URI:
                            AnimePictures animePictures = restAdapter.create(
                                    AnimePictures.class);
                            tags = animePictures.tag(
                                    RequestBody.create(MediaType.parse("text/plain"), tag))
                                    .execute()
                                    .body()
                                    .getTagsList();
                            break;
                        case Providers.GELBOORU_URI:
                            restAdapter = mBuilder
                                    .baseUrl(apiUri)
                                    //.setLogLevel(RestAdapter.LogLevel.FULL)
                                    .build();

                            Gelbooru gelbooru = restAdapter.create(Gelbooru.class);
                            tags = gelbooru.tag(200, 0, tag.trim().replace(' ', '_'))
                                    .execute()
                                    .body()
                                    .getTag();
                            break;
                        default:
                            tags = new ArrayList<>();

                    }
                    subscriber.onNext(tags);
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Image get(int position) {
        return mImageList.get(position);
    }

    @Override
    public int size() {
        return mImageList.size();
    }

    @Override
    public int getCount() {

        int count;

        String apiUri = mSetting.provider();
        switch (apiUri) {
            case Providers.ANIME_PICTURES_URI:
                count = 80 * page;
                break;
            default:
                count = LIMIT * page;
        }
        return count;
    }

    @Override
    public void clear() {
        mImageList.clear();
        page = 0;
    }

    @Override
    public void cacheDetail(String key, Image image) {
        mDetailImageMap.put(key, image);
    }

    @Override
    public Image getCachedDetail(String key) {
        return mDetailImageMap.get(key);
    }

    @Override
    public void cacheImageUri(String key, Uri uri) {
        mImageUri.put(key, uri);
    }

    @Override
    public Uri getImageUri(String key) {
        return mImageUri.get(key);
    }

    @Override
    public Observable<List<? extends Image>> loadListFromHistory() {
        return Observable.create(new Observable.OnSubscribe<List<? extends Image>>() {
            @Override
            public void call(Subscriber<? super List<? extends Image>> subscriber) {
                try {
                    String providerUri = mSetting.provider().
                            replace(Providers.SCHEME_HTTPS, "").
                            replace(Providers.SCHEME_HTTP, "");

                    List<HistoryImage> historyImages = new Select().from(HistoryImage.class).where(
                            Condition.column(HistoryImage$Table.PREVIEWURL)
                                    .like("%" + providerUri + "%")).
                            orderBy(false, HistoryImage$Table.LAST).queryList();
                    for (Image image : historyImages) {
                        mImageList.add(image);
                    }
                    subscriber.onNext(mImageList);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<? extends Image>> loadListFromFavorite() {
        return Observable.create(new Observable.OnSubscribe<List<? extends Image>>() {
            @Override
            public void call(Subscriber<? super List<? extends Image>> subscriber) {
                try {
                    String providerUri = mSetting.provider().
                            replace(Providers.SCHEME_HTTPS, "").
                            replace(Providers.SCHEME_HTTP, "");

                    List<FavoriteImage> favoriteImages = new Select().from(FavoriteImage.class)
                            .where(
                                    Condition.column(FavoriteImage$Table.PREVIEWURL)
                                            .like("%" + providerUri + "%"))
                            .
                                    orderBy(false, FavoriteImage$Table.LAST)
                            .queryList();
                    for (Image image : favoriteImages) {
                        mImageList.add(image);
                    }
                    subscriber.onNext(mImageList);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<GithubRelease.Asset> checkUpdate(final String versionString) {
        return Observable.create(new Observable.OnSubscribe<GithubRelease.Asset>() {
            @Override
            public void call(Subscriber<? super GithubRelease.Asset> subscriber) {
                long lastUpdate = mSetting.lastUpdate();
                if (System.currentTimeMillis() - lastUpdate > Config.UPDATE_DURATION) {
                    try {
                        Retrofit restAdapter = mBuilder.baseUrl(Providers.GITHUB_API_URI).build();
                        Github github = restAdapter.create(Github.class);
                        GithubRelease latest = github.latest().execute().body();

                        Version currentVersion = new Version(versionString);
                        Version latestVersion = new Version(latest.getTagName().substring(1));

                        if (latestVersion.compareTo(currentVersion) > 0 &&
                                !latest.getPrerelease() &&
                                latest.getAuthor().getLogin().equals(Config.GITHUB_UPDATE_AUTHOR)) {

                            for (GithubRelease.Asset asset : latest.getAssets()) {

                                if (asset.getContentType().equals(Config.GITHUB_UPDATE_CONTENT_TYPE)
                                        && asset.getState().equals(Config.GITHUB_UPDATE_STATUS)) {
                                    subscriber.onNext(asset);
                                }
                            }
                        }
                        mSetting.setLastUpdate(System.currentTimeMillis());
                    } catch (Exception e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Uri> downloadUpdate(final File dir, final String name, final String url) {
        return Observable.create(new Observable.OnSubscribe<Uri>() {
            @Override
            public void call(Subscriber<? super Uri> subscriber) {
                try {
                    File downloadedFile = new File(dir, name);
                    OkHttpClient client = new OkHttpClient();
                    okhttp3.Request request = new okhttp3.Request.Builder().url(
                            Utils.fixURL(url)).build();
                    okhttp3.Response response = client.newCall(request).execute();
                    BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
                    sink.writeAll(response.body().source());
                    sink.close();
                    subscriber.onNext(Uri.fromFile(downloadedFile));
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @UiThread
    void notifyDataSetChanged() {
        if (listUpdateListener != null) {
            listUpdateListener.OnListUpdate();
        }
    }

    @UiThread
    void onError(String message) {
        if (listUpdateListener != null) {
            listUpdateListener.OnError(context, message);
        }
    }

    private final static class SingletonHelper {
        private final static ImageRepository INSTANCE = new ImageRepository();
    }
}
