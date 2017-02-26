package moe.kurumi.moegallery.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by kurumi on 15-6-28.
 */
public interface Github {

    @GET("/repos/kurumi-moe/MoeGallery/releases")
    Call<List<GithubRelease>> releases();

    @GET("/repos/kurumi-moe/MoeGallery/releases/latest")
    Call<GithubRelease> latest();

}
