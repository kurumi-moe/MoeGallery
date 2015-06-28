package moe.kurumi.moegallery.model;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by kurumi on 15-5-28.
 */
public interface Moebooru {

    @GET("/post.json")
    List<MoebooruImage> list(
            @Query("limit") int limit,
            @Query("page") int page,
            @Query("tags") String tags
    );

    @GET("/tag.json")
    List<MoebooruTag> tag(
            @Query("name") String name
    );

}
