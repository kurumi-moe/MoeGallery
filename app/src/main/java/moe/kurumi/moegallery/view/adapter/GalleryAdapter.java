package moe.kurumi.moegallery.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import javax.inject.Inject;

import moe.kurumi.moegallery.R;
import moe.kurumi.moegallery.activity.MainActivity;
import moe.kurumi.moegallery.application.Application;
import moe.kurumi.moegallery.data.ImageDataSource;
import moe.kurumi.moegallery.model.Image;
import moe.kurumi.moegallery.utils.Utils;
import moe.kurumi.moegallery.view.SquareImageView;
import rx.functions.Action1;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    @Inject
    ImageDataSource mImageDataSource;
    Context mContext;
    private RequestManager mRequestManager;
    private OnRecyclerListener mListener;

    public GalleryAdapter(Context context) {
        Application.getAppComponent().inject(this);
        mRequestManager = Glide.with(context);

        mContext = context;

        loadImages("");
    }

    private void loadImages(final String tags) {
        mImageDataSource.loadList(tags).subscribe(new Action1<List<? extends Image>>() {
            @Override
            public void call(List<? extends Image> images) {
                mListener.onListUpdate();
                notifyDataSetChanged();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mListener.onError(throwable.getMessage());
            }
        });
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mImageDataSource.size();
    }

    public void loadNextPage(String tags) {
        loadImages(tags);
    }

    public void reload(String tags) {
        mImageDataSource.clear();
        loadImages(tags);
    }

    public void reloadFromHistory() {
        mImageDataSource.clear();
        mImageDataSource.loadListFromHistory().subscribe(new Action1<List<? extends Image>>() {
            @Override
            public void call(List<? extends Image> images) {
                mListener.onListUpdate();
                notifyDataSetChanged();

                if (images.size() == 0 && mContext != null) {
                    ((MainActivity) mContext).makeToast(R.string.no_history);
                    ((MainActivity) mContext).clearHistoryMode();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mListener.onError(throwable.getMessage());
            }
        });
    }

    public void reloadFromFavorite() {
        mImageDataSource.clear();
        mImageDataSource.loadListFromFavorite().subscribe(new Action1<List<? extends Image>>() {
            @Override
            public void call(List<? extends Image> images) {
                mListener.onListUpdate();
                notifyDataSetChanged();

                if (images.size() == 0 && mContext != null) {
                    ((MainActivity) mContext).makeToast(R.string.no_favorite);
                    ((MainActivity) mContext).clearFavoriteMode();
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                mListener.onError(throwable.getMessage());
            }
        });
    }

    public int getNextCount() {
        return mImageDataSource.getCount();
    }

    public void setRecyclerListener(OnRecyclerListener listener) {
        mListener = listener;
    }

    public interface OnRecyclerListener {
        void onItemClick(View view, int position);

        void onListUpdate();

        void onError(String message);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        SquareImageView imageView;
        ImageView gifTag;
        TextView resolution;
        TextView size;
        TextView type;

        Image mImage;
        int mPosition;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (SquareImageView) itemView.findViewById(R.id.imageView);
            gifTag = (ImageView) itemView.findViewById(R.id.gif_tag);
            resolution = (TextView) itemView.findViewById(R.id.resolution);
            size = (TextView) itemView.findViewById(R.id.size);
            type = (TextView) itemView.findViewById(R.id.type);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(imageView, mPosition);
                }
            });
        }

        void bind(int position) {
            mPosition = position;
            mImage = mImageDataSource.get(position);

            if (Utils.isGif(mImage.getFileUrl())) {
                gifTag.setVisibility(VISIBLE);
            } else {
                gifTag.setVisibility(GONE);
            }

            mRequestManager.load(mImage.getPreviewUrl())
                    .asBitmap()
                    .format(DecodeFormat.PREFER_RGB_565)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.color.gray_overlay)
                    .fitCenter().centerCrop().into(imageView);
        }

        public View getView() {
            return itemView;
        }
    }
}
