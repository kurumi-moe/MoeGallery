package moe.kurumi.moegallery.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kurumi on 15-5-31.
 */
public interface AnimePictures {

    @GET("/pictures/view_posts/{page}")
    Call<AnimePicturesList> list(
            @Path("page") int page,
            @Query("type") String type,
            @Query("lang") String lang
    );

    @GET("/pictures/view_post/{post}")
    Call<AnimePicturesImage> post(
            @Path("post") long post,
            @Query("type") String type,
            @Query("lang") String lang,
            @Header("cookie") String cookie
    );

    @GET("/pictures/view_post/{post}")
    Call<AnimePicturesImage> post(
            @Path("post") long post,
            @Query("type") String type,
            @Query("lang") String lang
    );

    @GET("/pictures/view_posts/{page}")
    Call<AnimePicturesList> search(
            @Path("page") int page,
            @Query("search_tag") String tags,
            @Query("order_by") String order,
            @Query("ldate") int date,
            @Query("type") String type,
            @Query("lang") String lang
    );

    @Multipart
    @POST("/pictures/autocomplete_tag")
    Call<AnimePicturesTagList> tag(
            @Part("tag") String tags
    );

    @Multipart
    @POST("/login/submit")
    Call<AnimePicturesUser> login(
            @Part("login") String username,
            @Part("password") String password,
            @Part("time_zone") String timezone
    );

}
