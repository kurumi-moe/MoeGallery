package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kurumi on 15-6-6.
 */
public class AnimePicturesUser {

    @Expose
    private Object redirect;
    @Expose
    private String username;
    @SerializedName("user_id")
    @Expose
    private Long userId;
    @Expose
    private Boolean success;
    @Expose
    private String token;
    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;

    /**
     *
     * @return
     * The redirect
     */
    public Object getRedirect() {
        return redirect;
    }

    /**
     *
     * @param redirect
     * The redirect
     */
    public void setRedirect(Object redirect) {
        this.redirect = redirect;
    }

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The token
     */
    public String getToken() {
        return token;
    }

    /**
     *
     * @param token
     * The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     *
     * @return
     * The avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     *
     * @param avatarUrl
     * The avatar_url
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

}