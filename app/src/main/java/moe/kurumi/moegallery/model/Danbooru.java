package moe.kurumi.moegallery.model;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by kurumi on 15-5-30.
 */
public interface Danbooru {

    @GET("/posts.json")
    List<DanbooruImage> list(
            @Query("limit") int limit,
            @Query("page") int page,
            @Query("tags") String tags
    );

    @GET("/tags.json?commit=Search&search[order]=count&utf8=✓&search[hide_empty]=yes")
    List<DanbooruTag> tag(
            @Query("search[name_matches]") String name
    );

}
