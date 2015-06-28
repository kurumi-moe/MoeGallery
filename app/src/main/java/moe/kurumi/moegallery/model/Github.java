package moe.kurumi.moegallery.model;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by kurumi on 15-6-28.
 */
public interface Github {

    @GET("/repos/kurumi-moe/MoeGallery/releases")
    List<GithubRelease> releases();

    @GET("/repos/kurumi-moe/MoeGallery/releases/latest")
    GithubRelease latest();

}
