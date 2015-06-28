package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kurumi on 15-6-28.
 */
public class GithubRelease {

    @Expose
    private String url;
    @SerializedName("assets_url")
    @Expose
    private String assetsUrl;
    @SerializedName("upload_url")
    @Expose
    private String uploadUrl;
    @SerializedName("html_url")
    @Expose
    private String htmlUrl;
    @Expose
    private Long id;
    @SerializedName("tag_name")
    @Expose
    private String tagName;
    @SerializedName("target_commitish")
    @Expose
    private String targetCommitish;
    @Expose
    private String name;
    @Expose
    private Boolean draft;
    @Expose
    private Author author;
    @Expose
    private Boolean prerelease;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @Expose
    private List<Asset> assets = new ArrayList<Asset>();
    @SerializedName("tarball_url")
    @Expose
    private String tarballUrl;
    @SerializedName("zipball_url")
    @Expose
    private String zipballUrl;
    @Expose
    private String body;

    /**
     * @return The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return The assetsUrl
     */
    public String getAssetsUrl() {
        return assetsUrl;
    }

    /**
     * @param assetsUrl The assets_url
     */
    public void setAssetsUrl(String assetsUrl) {
        this.assetsUrl = assetsUrl;
    }

    /**
     * @return The uploadUrl
     */
    public String getUploadUrl() {
        return uploadUrl;
    }

    /**
     * @param uploadUrl The upload_url
     */
    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    /**
     * @return The htmlUrl
     */
    public String getHtmlUrl() {
        return htmlUrl;
    }

    /**
     * @param htmlUrl The html_url
     */
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    /**
     * @return The id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return The tagName
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * @param tagName The tag_name
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * @return The targetCommitish
     */
    public String getTargetCommitish() {
        return targetCommitish;
    }

    /**
     * @param targetCommitish The target_commitish
     */
    public void setTargetCommitish(String targetCommitish) {
        this.targetCommitish = targetCommitish;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The draft
     */
    public Boolean getDraft() {
        return draft;
    }

    /**
     * @param draft The draft
     */
    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    /**
     * @return The author
     */
    public Author getAuthor() {
        return author;
    }

    /**
     * @param author The author
     */
    public void setAuthor(Author author) {
        this.author = author;
    }

    /**
     * @return The prerelease
     */
    public Boolean getPrerelease() {
        return prerelease;
    }

    /**
     * @param prerelease The prerelease
     */
    public void setPrerelease(Boolean prerelease) {
        this.prerelease = prerelease;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The publishedAt
     */
    public String getPublishedAt() {
        return publishedAt;
    }

    /**
     * @param publishedAt The published_at
     */
    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    /**
     * @return The assets
     */
    public List<Asset> getAssets() {
        return assets;
    }

    /**
     * @param assets The assets
     */
    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    /**
     * @return The tarballUrl
     */
    public String getTarballUrl() {
        return tarballUrl;
    }

    /**
     * @param tarballUrl The tarball_url
     */
    public void setTarballUrl(String tarballUrl) {
        this.tarballUrl = tarballUrl;
    }

    /**
     * @return The zipballUrl
     */
    public String getZipballUrl() {
        return zipballUrl;
    }

    /**
     * @param zipballUrl The zipball_url
     */
    public void setZipballUrl(String zipballUrl) {
        this.zipballUrl = zipballUrl;
    }

    /**
     * @return The body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body The body
     */
    public void setBody(String body) {
        this.body = body;
    }

    public class Asset {

        @Expose
        private String url;
        @Expose
        private Long id;
        @Expose
        private String name;
        @Expose
        private Object label;
        @Expose
        private Uploader uploader;
        @SerializedName("content_type")
        @Expose
        private String contentType;
        @Expose
        private String state;
        @Expose
        private Long size;
        @SerializedName("download_count")
        @Expose
        private Long downloadCount;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("browser_download_url")
        @Expose
        private String browserDownloadUrl;

        /**
         * @return The url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url The url
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return The id
         */
        public Long getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(Long id) {
            this.id = id;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The label
         */
        public Object getLabel() {
            return label;
        }

        /**
         * @param label The label
         */
        public void setLabel(Object label) {
            this.label = label;
        }

        /**
         * @return The uploader
         */
        public Uploader getUploader() {
            return uploader;
        }

        /**
         * @param uploader The uploader
         */
        public void setUploader(Uploader uploader) {
            this.uploader = uploader;
        }

        /**
         * @return The contentType
         */
        public String getContentType() {
            return contentType;
        }

        /**
         * @param contentType The content_type
         */
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        /**
         * @return The state
         */
        public String getState() {
            return state;
        }

        /**
         * @param state The state
         */
        public void setState(String state) {
            this.state = state;
        }

        /**
         * @return The size
         */
        public Long getSize() {
            return size;
        }

        /**
         * @param size The size
         */
        public void setSize(Long size) {
            this.size = size;
        }

        /**
         * @return The downloadCount
         */
        public Long getDownloadCount() {
            return downloadCount;
        }

        /**
         * @param downloadCount The download_count
         */
        public void setDownloadCount(Long downloadCount) {
            this.downloadCount = downloadCount;
        }

        /**
         * @return The createdAt
         */
        public String getCreatedAt() {
            return createdAt;
        }

        /**
         * @param createdAt The created_at
         */
        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        /**
         * @return The updatedAt
         */
        public String getUpdatedAt() {
            return updatedAt;
        }

        /**
         * @param updatedAt The updated_at
         */
        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        /**
         * @return The browserDownloadUrl
         */
        public String getBrowserDownloadUrl() {
            return browserDownloadUrl;
        }

        /**
         * @param browserDownloadUrl The browser_download_url
         */
        public void setBrowserDownloadUrl(String browserDownloadUrl) {
            this.browserDownloadUrl = browserDownloadUrl;
        }

    }

    public class Author {

        @Expose
        private String login;
        @Expose
        private Long id;
        @SerializedName("avatar_url")
        @Expose
        private String avatarUrl;
        @SerializedName("gravatar_id")
        @Expose
        private String gravatarId;
        @Expose
        private String url;
        @SerializedName("html_url")
        @Expose
        private String htmlUrl;
        @SerializedName("followers_url")
        @Expose
        private String followersUrl;
        @SerializedName("following_url")
        @Expose
        private String followingUrl;
        @SerializedName("gists_url")
        @Expose
        private String gistsUrl;
        @SerializedName("starred_url")
        @Expose
        private String starredUrl;
        @SerializedName("subscriptions_url")
        @Expose
        private String subscriptionsUrl;
        @SerializedName("organizations_url")
        @Expose
        private String organizationsUrl;
        @SerializedName("repos_url")
        @Expose
        private String reposUrl;
        @SerializedName("events_url")
        @Expose
        private String eventsUrl;
        @SerializedName("received_events_url")
        @Expose
        private String receivedEventsUrl;
        @Expose
        private String type;
        @SerializedName("site_admin")
        @Expose
        private Boolean siteAdmin;

        /**
         * @return The login
         */
        public String getLogin() {
            return login;
        }

        /**
         * @param login The login
         */
        public void setLogin(String login) {
            this.login = login;
        }

        /**
         * @return The id
         */
        public Long getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(Long id) {
            this.id = id;
        }

        /**
         * @return The avatarUrl
         */
        public String getAvatarUrl() {
            return avatarUrl;
        }

        /**
         * @param avatarUrl The avatar_url
         */
        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        /**
         * @return The gravatarId
         */
        public String getGravatarId() {
            return gravatarId;
        }

        /**
         * @param gravatarId The gravatar_id
         */
        public void setGravatarId(String gravatarId) {
            this.gravatarId = gravatarId;
        }

        /**
         * @return The url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url The url
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return The htmlUrl
         */
        public String getHtmlUrl() {
            return htmlUrl;
        }

        /**
         * @param htmlUrl The html_url
         */
        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        /**
         * @return The followersUrl
         */
        public String getFollowersUrl() {
            return followersUrl;
        }

        /**
         * @param followersUrl The followers_url
         */
        public void setFollowersUrl(String followersUrl) {
            this.followersUrl = followersUrl;
        }

        /**
         * @return The followingUrl
         */
        public String getFollowingUrl() {
            return followingUrl;
        }

        /**
         * @param followingUrl The following_url
         */
        public void setFollowingUrl(String followingUrl) {
            this.followingUrl = followingUrl;
        }

        /**
         * @return The gistsUrl
         */
        public String getGistsUrl() {
            return gistsUrl;
        }

        /**
         * @param gistsUrl The gists_url
         */
        public void setGistsUrl(String gistsUrl) {
            this.gistsUrl = gistsUrl;
        }

        /**
         * @return The starredUrl
         */
        public String getStarredUrl() {
            return starredUrl;
        }

        /**
         * @param starredUrl The starred_url
         */
        public void setStarredUrl(String starredUrl) {
            this.starredUrl = starredUrl;
        }

        /**
         * @return The subscriptionsUrl
         */
        public String getSubscriptionsUrl() {
            return subscriptionsUrl;
        }

        /**
         * @param subscriptionsUrl The subscriptions_url
         */
        public void setSubscriptionsUrl(String subscriptionsUrl) {
            this.subscriptionsUrl = subscriptionsUrl;
        }

        /**
         * @return The organizationsUrl
         */
        public String getOrganizationsUrl() {
            return organizationsUrl;
        }

        /**
         * @param organizationsUrl The organizations_url
         */
        public void setOrganizationsUrl(String organizationsUrl) {
            this.organizationsUrl = organizationsUrl;
        }

        /**
         * @return The reposUrl
         */
        public String getReposUrl() {
            return reposUrl;
        }

        /**
         * @param reposUrl The repos_url
         */
        public void setReposUrl(String reposUrl) {
            this.reposUrl = reposUrl;
        }

        /**
         * @return The eventsUrl
         */
        public String getEventsUrl() {
            return eventsUrl;
        }

        /**
         * @param eventsUrl The events_url
         */
        public void setEventsUrl(String eventsUrl) {
            this.eventsUrl = eventsUrl;
        }

        /**
         * @return The receivedEventsUrl
         */
        public String getReceivedEventsUrl() {
            return receivedEventsUrl;
        }

        /**
         * @param receivedEventsUrl The received_events_url
         */
        public void setReceivedEventsUrl(String receivedEventsUrl) {
            this.receivedEventsUrl = receivedEventsUrl;
        }

        /**
         * @return The type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type The type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return The siteAdmin
         */
        public Boolean getSiteAdmin() {
            return siteAdmin;
        }

        /**
         * @param siteAdmin The site_admin
         */
        public void setSiteAdmin(Boolean siteAdmin) {
            this.siteAdmin = siteAdmin;
        }

    }

    public class Uploader {

        @Expose
        private String login;
        @Expose
        private Long id;
        @SerializedName("avatar_url")
        @Expose
        private String avatarUrl;
        @SerializedName("gravatar_id")
        @Expose
        private String gravatarId;
        @Expose
        private String url;
        @SerializedName("html_url")
        @Expose
        private String htmlUrl;
        @SerializedName("followers_url")
        @Expose
        private String followersUrl;
        @SerializedName("following_url")
        @Expose
        private String followingUrl;
        @SerializedName("gists_url")
        @Expose
        private String gistsUrl;
        @SerializedName("starred_url")
        @Expose
        private String starredUrl;
        @SerializedName("subscriptions_url")
        @Expose
        private String subscriptionsUrl;
        @SerializedName("organizations_url")
        @Expose
        private String organizationsUrl;
        @SerializedName("repos_url")
        @Expose
        private String reposUrl;
        @SerializedName("events_url")
        @Expose
        private String eventsUrl;
        @SerializedName("received_events_url")
        @Expose
        private String receivedEventsUrl;
        @Expose
        private String type;
        @SerializedName("site_admin")
        @Expose
        private Boolean siteAdmin;

        /**
         * @return The login
         */
        public String getLogin() {
            return login;
        }

        /**
         * @param login The login
         */
        public void setLogin(String login) {
            this.login = login;
        }

        /**
         * @return The id
         */
        public Long getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(Long id) {
            this.id = id;
        }

        /**
         * @return The avatarUrl
         */
        public String getAvatarUrl() {
            return avatarUrl;
        }

        /**
         * @param avatarUrl The avatar_url
         */
        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        /**
         * @return The gravatarId
         */
        public String getGravatarId() {
            return gravatarId;
        }

        /**
         * @param gravatarId The gravatar_id
         */
        public void setGravatarId(String gravatarId) {
            this.gravatarId = gravatarId;
        }

        /**
         * @return The url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url The url
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return The htmlUrl
         */
        public String getHtmlUrl() {
            return htmlUrl;
        }

        /**
         * @param htmlUrl The html_url
         */
        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        /**
         * @return The followersUrl
         */
        public String getFollowersUrl() {
            return followersUrl;
        }

        /**
         * @param followersUrl The followers_url
         */
        public void setFollowersUrl(String followersUrl) {
            this.followersUrl = followersUrl;
        }

        /**
         * @return The followingUrl
         */
        public String getFollowingUrl() {
            return followingUrl;
        }

        /**
         * @param followingUrl The following_url
         */
        public void setFollowingUrl(String followingUrl) {
            this.followingUrl = followingUrl;
        }

        /**
         * @return The gistsUrl
         */
        public String getGistsUrl() {
            return gistsUrl;
        }

        /**
         * @param gistsUrl The gists_url
         */
        public void setGistsUrl(String gistsUrl) {
            this.gistsUrl = gistsUrl;
        }

        /**
         * @return The starredUrl
         */
        public String getStarredUrl() {
            return starredUrl;
        }

        /**
         * @param starredUrl The starred_url
         */
        public void setStarredUrl(String starredUrl) {
            this.starredUrl = starredUrl;
        }

        /**
         * @return The subscriptionsUrl
         */
        public String getSubscriptionsUrl() {
            return subscriptionsUrl;
        }

        /**
         * @param subscriptionsUrl The subscriptions_url
         */
        public void setSubscriptionsUrl(String subscriptionsUrl) {
            this.subscriptionsUrl = subscriptionsUrl;
        }

        /**
         * @return The organizationsUrl
         */
        public String getOrganizationsUrl() {
            return organizationsUrl;
        }

        /**
         * @param organizationsUrl The organizations_url
         */
        public void setOrganizationsUrl(String organizationsUrl) {
            this.organizationsUrl = organizationsUrl;
        }

        /**
         * @return The reposUrl
         */
        public String getReposUrl() {
            return reposUrl;
        }

        /**
         * @param reposUrl The repos_url
         */
        public void setReposUrl(String reposUrl) {
            this.reposUrl = reposUrl;
        }

        /**
         * @return The eventsUrl
         */
        public String getEventsUrl() {
            return eventsUrl;
        }

        /**
         * @param eventsUrl The events_url
         */
        public void setEventsUrl(String eventsUrl) {
            this.eventsUrl = eventsUrl;
        }

        /**
         * @return The receivedEventsUrl
         */
        public String getReceivedEventsUrl() {
            return receivedEventsUrl;
        }

        /**
         * @param receivedEventsUrl The received_events_url
         */
        public void setReceivedEventsUrl(String receivedEventsUrl) {
            this.receivedEventsUrl = receivedEventsUrl;
        }

        /**
         * @return The type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type The type
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return The siteAdmin
         */
        public Boolean getSiteAdmin() {
            return siteAdmin;
        }

        /**
         * @param siteAdmin The site_admin
         */
        public void setSiteAdmin(Boolean siteAdmin) {
            this.siteAdmin = siteAdmin;
        }

    }

}
