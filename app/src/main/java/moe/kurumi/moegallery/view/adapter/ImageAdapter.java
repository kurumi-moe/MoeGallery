package moe.kurumi.moegallery.view.adapter;

import android.content.Context;
import android.view.ViewGroup;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import moe.kurumi.moegallery.model.Image;
import moe.kurumi.moegallery.view.ImageItemView;
import moe.kurumi.moegallery.view.ImageItemView_;
import moe.kurumi.moegallery.view.ViewWrapper;

/**
 * Created by kurumi on 15-5-28.
 */

@EBean
public class ImageAdapter extends RecyclerViewAdapterBase<Image, ImageItemView> {

    @RootContext
    Context context;

    @Override
    protected ImageItemView onCreateItemView(ViewGroup parent, int viewType) {
        return ImageItemView_.build(context);
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ImageItemView> holder, int position) {

        ImageItemView view = holder.getView();
        Image image = items.get(position);

        view.bind(image, position);

    }
}
