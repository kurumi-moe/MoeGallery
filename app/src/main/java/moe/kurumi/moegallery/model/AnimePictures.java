package moe.kurumi.moegallery.model;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by kurumi on 15-5-31.
 */
public interface AnimePictures {

    @GET("/pictures/view_posts/{page}")
    AnimePicturesList list(
            @Path("page") int page,
            @Query("type") String type,
            @Query("lang") String lang
    );

    @GET("/pictures/view_post/{post}")
    AnimePicturesImage post(
            @Path("post") long post,
            @Query("type") String type,
            @Query("lang") String lang,
            @Header("cookie") String cookie
    );

    @GET("/pictures/view_post/{post}")
    AnimePicturesImage post(
            @Path("post") long post,
            @Query("type") String type,
            @Query("lang") String lang
    );

    @GET("/pictures/view_posts/{page}")
    AnimePicturesList search(
            @Path("page") int page,
            @Query("search_tag") String tags,
            @Query("order_by") String order,
            @Query("ldate") int date,
            @Query("type") String type,
            @Query("lang") String lang
    );

    @Multipart
    @POST("/pictures/autocomplete_tag")
    AnimePicturesTagList tag(
            @Part("tag") String tags
    );

    @Multipart
    @POST("/login/submit")
    AnimePicturesUser login(
            @Part("login") String username,
            @Part("password") String password,
            @Part("time_zone") String timezone
    );

}
