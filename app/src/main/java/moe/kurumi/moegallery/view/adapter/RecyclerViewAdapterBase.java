package moe.kurumi.moegallery.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import moe.kurumi.moegallery.activity.MainActivity;
import moe.kurumi.moegallery.provider.ImageProvider;
import moe.kurumi.moegallery.provider.ImageProviderBase;
import moe.kurumi.moegallery.view.ViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kurumi on 15-5-28.
 */

@EBean
public abstract class RecyclerViewAdapterBase<T, V extends View>
        extends RecyclerView.Adapter<ViewWrapper<V>> {

    protected List<T> items = new ArrayList<>();

    @Bean(ImageProvider.class)
    ImageProviderBase imageProviderBase;

    ViewGroup parent;

    @Override
    public ViewWrapper<V> onCreateViewHolder(ViewGroup parent,
            int viewType) {

        this.parent = parent;
        ((MainActivity) parent.getContext()).hideProgressDialog();

        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    // additional methods to manipulate the items

    @AfterInject
    void initAdapter() {
        imageProviderBase.bindList(items, new ImageProviderBase.OnListUpdateListener() {
            @Override
            public void OnListUpdate() {
                notifyDataSetChanged();
                if (parent != null) {
                    ((MainActivity) parent.getContext()).hideProgressDialog();
                }
            }

            @Override
            public void OnError(Context context, String message) {
                if (context != null) {
                    ((MainActivity) context).hideProgressDialog();
                    ((MainActivity) context).showErrorDialog(message);
                }
            }
        });
    }

    public void loadNextPage(String tags) {
        imageProviderBase.loadList(tags);
    }

    public void reload(String tags) {
        imageProviderBase.clear();
        imageProviderBase.loadList(tags);
    }

    public void reloadFromHistory() {
        imageProviderBase.clear();
        imageProviderBase.loadListFromHistory();
    }

    public void reloadFromFavorite() {
        imageProviderBase.clear();
        imageProviderBase.loadListFromFavorite();
    }

    public int getNextCount() {
        return imageProviderBase.getCount();
    }

}
