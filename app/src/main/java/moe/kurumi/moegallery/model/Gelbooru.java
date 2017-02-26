package moe.kurumi.moegallery.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kurumi on 15-5-31.
 */
public interface Gelbooru {
    @GET("/index.php?page=dapi&s=post&q=index")
    Call<GelbooruList> list(
            @Query("limit") int limit,
            @Query("pid") int page,
            @Query("tags") String tags
    );

    @GET("/index.php?page=dapi&s=tag&q=index&order=count")
    Call<GelbooruTagList> tag(
            @Query("limit") int limit,
            @Query("pid") int page,
            @Query("name_pattern") String tags
    );
}
