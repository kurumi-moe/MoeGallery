package moe.kurumi.moegallery.provider;

import android.content.Context;

import java.util.List;

/**
 * Created by kurumi on 15-5-28.
 */

public interface ImageProviderBase<V> {
    void bindList(List<V> list, OnListUpdateListener listener);

    void loadList(String tags);

    int getCount();

    void clear();

    void loadListFromHistory();

    void loadListFromFavorite();

    interface OnListUpdateListener {
        void OnListUpdate();
        void OnError(Context context, String message);
    }
}
