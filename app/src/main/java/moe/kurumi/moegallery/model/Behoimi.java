package moe.kurumi.moegallery.model;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by kurumi on 15-5-31.
 */
public interface Behoimi {

    @GET("/post/index.json")
    List<BehoimiImage> list(
            @Query("limit") int limit,
            @Query("page") int page,
            @Query("tags") String tags
    );

    @GET("/tag/index.json")
    List<BehoimiTag> tag(
            @Query("name") String name
    );

}
