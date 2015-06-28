package moe.kurumi.moegallery.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kurumi on 15-5-28.
 */
public class ViewWrapper<V extends View> extends RecyclerView.ViewHolder {

    private V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }
}
