package moe.kurumi.moegallery.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kurumi on 15-5-28.
 */
public interface Moebooru {

    @GET("/post.json")
    Call<List<MoebooruImage>> list(
            @Query("limit") int limit,
            @Query("page") int page,
            @Query("tags") String tags
    );

    @GET("/tag.json")
    Call<List<MoebooruTag>> tag(
            @Query("name") String name
    );

}
