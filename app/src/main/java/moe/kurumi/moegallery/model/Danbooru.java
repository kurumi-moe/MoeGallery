package moe.kurumi.moegallery.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kurumi on 15-5-30.
 */
public interface Danbooru {

    @GET("/posts.json")
    Call<List<DanbooruImage>> list(
            @Query("limit") int limit,
            @Query("page") int page,
            @Query("tags") String tags
    );

    @GET("/tags.json?commit=Search&search[order]=count&utf8=✓&search[hide_empty]=yes")
    Call<List<DanbooruTag>> tag(
            @Query("search[name_matches]") String name
    );

}
