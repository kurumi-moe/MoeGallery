package moe.kurumi.moegallery.di.modules;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;

import java.io.InputStream;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import moe.kurumi.moegallery.activity.MainActivity;
import moe.kurumi.moegallery.glide.GifDrawableBytesTranscoder;
import moe.kurumi.moegallery.glide.OkHttpUrlLoader;
import moe.kurumi.moegallery.glide.StreamByteArrayResourceDecoder;
import okhttp3.OkHttpClient;
import pl.droidsonroids.gif.GifDrawable;

@Module
public class MainModule {

    private MainActivity mView;

    public MainModule(MainActivity view) {
        mView = view;
    }

    @Singleton
    @Provides
    RequestManager provideGlide(OkHttpClient okHttpClient) {
        OkHttpUrlLoader.Factory factory = new OkHttpUrlLoader.Factory(okHttpClient);
        Glide.get(mView).register(GlideUrl.class, InputStream.class, factory);
        return Glide.with(mView);
    }

    @Singleton
    @Provides
    GenericRequestBuilder<GlideUrl, InputStream, byte[], GifDrawable> provideGifRequestBuilder(
            RequestManager requestManager, OkHttpClient okHttpClient) {

        return requestManager.using(new OkHttpUrlLoader(okHttpClient), InputStream.class)
                .from(GlideUrl.class)
                .as(byte[].class)
                .transcode(new GifDrawableBytesTranscoder(), GifDrawable.class)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .decoder(new StreamByteArrayResourceDecoder())
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<>(new StreamByteArrayResourceDecoder()));
    }
}
