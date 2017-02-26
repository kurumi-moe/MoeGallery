package moe.kurumi.moegallery.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kurumi on 15-5-31.
 */
public interface Behoimi {

    @GET("/post/index.json")
    Call<List<BehoimiImage>> list(
            @Query("limit") int limit,
            @Query("page") int page,
            @Query("tags") String tags
    );

    @GET("/tag/index.json")
    Call<List<BehoimiTag>> tag(
            @Query("name") String name
    );

}
