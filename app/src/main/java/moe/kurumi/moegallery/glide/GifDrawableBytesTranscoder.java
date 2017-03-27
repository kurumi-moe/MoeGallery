package moe.kurumi.moegallery.glide;

import android.util.Log;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

public class GifDrawableBytesTranscoder implements ResourceTranscoder<byte[], GifDrawable> {
    @Override
    public Resource<GifDrawable> transcode(Resource<byte[]> toTranscode) {
        try {
            return new GifDrawableResource(new GifDrawable(toTranscode.get()));
        } catch (IOException ex) {
            Log.e("GifDrawable", "Cannot decode bytes", ex);
            return null;
        }
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    private static class GifDrawableResource implements Resource<GifDrawable> {
        GifDrawable gifDrawable;

        GifDrawableResource(GifDrawable drawable) {
            gifDrawable = drawable;
        }

        @Override
        public GifDrawable get() {
            return gifDrawable;
        }

        @Override
        public int getSize() {
            return (int) gifDrawable.getInputSourceByteCount();
        }

        @Override
        public void recycle() {
            gifDrawable.stop();
            gifDrawable.recycle();
        }
    }
}