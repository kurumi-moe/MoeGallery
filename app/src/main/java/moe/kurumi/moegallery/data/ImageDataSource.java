package moe.kurumi.moegallery.data;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.util.List;

import moe.kurumi.moegallery.model.GithubRelease;
import moe.kurumi.moegallery.model.Image;
import moe.kurumi.moegallery.model.Tag;
import rx.Observable;

public interface ImageDataSource {
    Observable<List<? extends Image>> loadList(String tags);

    Observable<List<? extends Tag>> listTag(String tag);

    Image get(int position);

    int size();

    int getCount();

    void clear();

    void cacheDetail(String key, Image image);

    Image getCachedDetail(String key);

    void cacheImageUri(String key, Uri uri);

    Uri getImageUri(String key);

    Observable<List<? extends Image>> loadListFromHistory();

    Observable<List<? extends Image>> loadListFromFavorite();

    Observable<GithubRelease.Asset> checkUpdate(String versionString);

    Observable<Uri> downloadUpdate(File dir, String name, String url);

    interface OnListUpdateListener {
        void OnListUpdate();
        void OnError(Context context, String message);
    }
}
