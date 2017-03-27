package moe.kurumi.moegallery.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.executor.FifoPriorityThreadPoolExecutor;
import com.bumptech.glide.module.GlideModule;

public class GlideSetup implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //builder.setMemoryCache(new LruResourceCache(64 * 1024 * 1024));
        //builder.setBitmapPool(new LruBitmapPool(32 * 1024 * 1024));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 2147483647));
        builder.setResizeService(new FifoPriorityThreadPoolExecutor(2));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //glide.register(Image.class, InputStream.class, new ImageLoader.Factory());
    }
}
