package moe.kurumi.moegallery.provider;

import android.content.Context;

import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;
import java.util.List;

import moe.kurumi.moegallery.R;
import moe.kurumi.moegallery.activity.MainActivity;
import moe.kurumi.moegallery.model.AnimePictures;
import moe.kurumi.moegallery.model.AnimePicturesList;
import moe.kurumi.moegallery.model.Behoimi;
import moe.kurumi.moegallery.model.Danbooru;
import moe.kurumi.moegallery.model.Gelbooru;
import moe.kurumi.moegallery.model.GelbooruList;
import moe.kurumi.moegallery.model.Image;
import moe.kurumi.moegallery.model.Moebooru;
import moe.kurumi.moegallery.model.Preferences_;
import moe.kurumi.moegallery.model.database.FavoriteImage;
import moe.kurumi.moegallery.model.database.FavoriteImage$Table;
import moe.kurumi.moegallery.model.database.HistoryImage;
import moe.kurumi.moegallery.model.database.HistoryImage$Table;
import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

/**
 * Created by kurumi on 15-5-28.
 */

@EBean
public class ImageProvider implements ImageProviderBase<Image> {

    public static final int LIMIT = 20;
    @Pref
    Preferences_ preferences;
    private List<Image> images;
    private OnListUpdateListener listUpdateListener;
    private int page = 0;

    @RootContext
    Context context;

    @Override
    public void bindList(List<Image> list, OnListUpdateListener listener) {
        images = list;
        listUpdateListener = listener;
        loadList("");
    }

    @Background
    public void loadList(String tags) {

        try {
            page++;

            String apiUri = preferences.provider().get();

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(apiUri)
                    //.setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();

            List<? extends Image> images;

            switch (apiUri) {
                case Providers.DANBOORU_URI:
                    Danbooru danbooru = restAdapter.create(Danbooru.class);
                    images = danbooru.list(LIMIT, page, tags);
                    break;
                case Providers.KONACHAN_URI:
                case Providers.YANDERE_URI:
                    Moebooru moebooru = restAdapter.create(Moebooru.class);
                    images = moebooru.list(LIMIT, page, tags);
                    break;
                case Providers.BEHOIMI_URI:
                    Behoimi behoimi = restAdapter.create(Behoimi.class);
                    images = behoimi.list(LIMIT, page, tags);
                    break;
                case Providers.ANIME_PICTURES_URI:
                    AnimePictures animePictures = restAdapter.create(AnimePictures.class);
                    AnimePicturesList animePicturesList;

                    if (tags.isEmpty()) {
                        animePicturesList = animePictures.list(page - 1, "json", "en");
                    } else {
                        animePicturesList = animePictures.search(page - 1, tags, "date", 0,"json", "en");
                    }

                    images = animePicturesList.getPreviews();
                    break;
                case Providers.GELBOORU_URI:

                    restAdapter = new RestAdapter.Builder()
                            .setEndpoint(apiUri)
                            //.setLogLevel(RestAdapter.LogLevel.FULL)
                            .setConverter(new SimpleXMLConverter())
                            .build();
                    Gelbooru gelbooru = restAdapter.create(Gelbooru.class);
                    GelbooruList gelbooruList = gelbooru.list(LIMIT, page - 1, tags);

                    images = gelbooruList.getPost();

                    break;
                default:
                    images = new ArrayList<>();
            }

            for (Image image : images) {
                this.images.add(image);
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            page--;
            onError(e.getMessage());
        }
    }

    @Override
    public int getCount() {

        int count;

        String apiUri = preferences.provider().get();
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
        images.clear();
        page = 0;
    }

    @Override
    @Background
    public void loadListFromHistory() {
        try {
            String providerUri = preferences.provider().get().
                    replace(Providers.SCHEME_HTTPS, "").
                    replace(Providers.SCHEME_HTTP, "");

            List<HistoryImage> historyImages = new Select().from(HistoryImage.class).where(
                    Condition.column(HistoryImage$Table.PREVIEWURL).like("%" + providerUri + "%")).
                    orderBy(false, HistoryImage$Table.LAST).queryList();
            for (Image image : historyImages) {
                this.images.add(image);
            }
            notifyDataSetChanged();
            if (this.images.size()==0 && context!=null) {
                ((MainActivity) context).makeToast(R.string.no_history);
                ((MainActivity) context).clearHistoryMode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e.getMessage());
        }
    }

    @Override
    @Background
    public void loadListFromFavorite() {
        try {
            String providerUri = preferences.provider().get().
                    replace(Providers.SCHEME_HTTPS, "").
                    replace(Providers.SCHEME_HTTP, "");

            List<FavoriteImage> favoriteImages = new Select().from(FavoriteImage.class).where(
                    Condition.column(FavoriteImage$Table.PREVIEWURL).like("%" + providerUri + "%")).
                    orderBy(false, FavoriteImage$Table.LAST).queryList();
            for (Image image : favoriteImages) {
                this.images.add(image);
            }
            notifyDataSetChanged();
            if (this.images.size()==0 && context!=null) {
                ((MainActivity) context).makeToast(R.string.no_favorite);
                ((MainActivity) context).clearFavoriteMode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e.getMessage());
        }
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
}
