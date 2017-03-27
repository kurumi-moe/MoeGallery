package moe.kurumi.moegallery.glide;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bytes.BytesResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamByteArrayResourceDecoder implements ResourceDecoder<InputStream, byte[]> {
    @Override
    public Resource<byte[]> decode(InputStream in, int width, int height) throws
            IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = in.read(buffer)) != -1) {
            bytes.write(buffer, 0, count);
        }
        return new BytesResource(bytes.toByteArray());
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}