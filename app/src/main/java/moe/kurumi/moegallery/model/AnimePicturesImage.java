package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import moe.kurumi.moegallery.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kurumi on 15-5-31.
 */
public class AnimePicturesImage implements Image {
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("medium_preview")
    @Expose
    private String mediumPreview;
    @SerializedName("user_avatar")
    @Expose
    private String userAvatar;
    @Expose
    private List<String> tags = new ArrayList<String>();
    @Expose
    private Long erotics;
    @SerializedName("small_preview")
    @Expose
    private String smallPreview;
    @SerializedName("big_preview")
    @Expose
    private String bigPreview;
    @Expose
    private String ext;
    @SerializedName("score_number")
    @Expose
    private Long scoreNumber;
    @Expose
    private Long height;
    @SerializedName("tags_full")
    @Expose
    private List<TagsFull> tagsFull = new ArrayList<TagsFull>();
    @Expose
    private Long width;
    @SerializedName("md5_pixels")
    @Expose
    private String md5Pixels;
    @Expose
    private Long score;
    @Expose
    private String pubtime;
    @SerializedName("download_count")
    @Expose
    private Long downloadCount;
    @Expose
    private Long size;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @Expose
    private Long id;
    @Expose
    private List<Long> color = new ArrayList<Long>();
    @Expose
    private String md5;

    /**
     * @return The fileUrl
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * @param fileUrl The file_url
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * @return The mediumPreview
     */
    public String getMediumPreview() {
        return mediumPreview;
    }

    /**
     * @param mediumPreview The medium_preview
     */
    public void setMediumPreview(String mediumPreview) {
        this.mediumPreview = mediumPreview;
    }

    /**
     * @return The userAvatar
     */
    public String getUserAvatar() {
        return userAvatar;
    }

    /**
     * @param userAvatar The user_avatar
     */
    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    /**
     * @return The tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @param tags The tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * @return The erotics
     */
    public Long getErotics() {
        return erotics;
    }

    /**
     * @param erotics The erotics
     */
    public void setErotics(Long erotics) {
        this.erotics = erotics;
    }

    /**
     * @return The smallPreview
     */
    public String getSmallPreview() {
        return smallPreview;
    }

    /**
     * @param smallPreview The small_preview
     */
    public void setSmallPreview(String smallPreview) {
        this.smallPreview = smallPreview;
    }

    /**
     * @return The bigPreview
     */
    public String getBigPreview() {
        return bigPreview;
    }

    /**
     * @param bigPreview The big_preview
     */
    public void setBigPreview(String bigPreview) {
        this.bigPreview = bigPreview;
    }

    /**
     * @return The ext
     */
    public String getExt() {
        return ext;
    }

    /**
     * @param ext The ext
     */
    public void setExt(String ext) {
        this.ext = ext;
    }

    /**
     * @return The scoreNumber
     */
    public Long getScoreNumber() {
        return scoreNumber;
    }

    /**
     * @param scoreNumber The score_number
     */
    public void setScoreNumber(Long scoreNumber) {
        this.scoreNumber = scoreNumber;
    }

    /**
     * @return The height
     */
    public Long getHeight() {
        return height;
    }

    /**
     * @param height The height
     */
    public void setHeight(Long height) {
        this.height = height;
    }

    /**
     * @return The tagsFull
     */
    public List<TagsFull> getTagsFull() {
        return tagsFull;
    }

    /**
     * @param tagsFull The tags_full
     */
    public void setTagsFull(List<TagsFull> tagsFull) {
        this.tagsFull = tagsFull;
    }

    @Override
    public String getPreviewUrl() {
        return getBigPreview();
    }

    /**
     * @return The width
     */
    public Long getWidth() {
        return width;
    }

    /**
     * @param width The width
     */
    public void setWidth(Long width) {
        this.width = width;
    }

    /**
     * @return The md5Pixels
     */
    public String getMd5Pixels() {
        return md5Pixels;
    }

    /**
     * @param md5Pixels The md5_pixels
     */
    public void setMd5Pixels(String md5Pixels) {
        this.md5Pixels = md5Pixels;
    }

    /**
     * @return The score
     */
    public Long getScore() {
        return score;
    }

    /**
     * @param score The score
     */
    public void setScore(Long score) {
        this.score = score;
    }

    /**
     * @return The pubtime
     */
    public String getPubtime() {
        return pubtime;
    }

    /**
     * @param pubtime The pubtime
     */
    public void setPubtime(String pubtime) {
        this.pubtime = pubtime;
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
     * @return The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return The id
     */
    public Long getId() {
        return id;
    }

    @Override
    public String getUser() {
        return getUserName();
    }

    @Override
    public String getType() {
        return Utils.getMimeType(getExt());
    }

    @Override
    public Long getCount() {
        return getDownloadCount();
    }

    @Override
    public List<String> getTagList() {
        return getTags();
    }

    /**
     * @param id The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return The color
     */
    public List<Long> getColor() {
        return color;
    }

    /**
     * @param color The color
     */
    public void setColor(List<Long> color) {
        this.color = color;
    }

    /**
     * @return The md5
     */
    public String getMd5() {
        return md5;
    }

    /**
     * @param md5 The md5
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    @Override
    public String getSampleUrl() {
        return getBigPreview();
    }

    @Override
    public String getName() {
        return Utils.getProviderName(getPreviewUrl()) + " - " +
                getId() + "" + getTagString() + getExt();
    }

    private String getTagString() {

        String tags = "";

        for (String s : getTags()) {
            tags += " " + s;
        }

        if (tags.length()>50) {
            tags = tags.substring(0, 50);
        }

        return tags;
    }

    public class TagsFull {

        @Expose
        private Long type;
        @Expose
        private String name;

        /**
         * @return The type
         */
        public Long getType() {
            return type;
        }

        /**
         * @param type The type
         */
        public void setType(Long type) {
            this.type = type;
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

    }

}