package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import moe.kurumi.moegallery.data.Providers;
import moe.kurumi.moegallery.utils.Utils;

import java.util.List;

/**
 * Created by kurumi on 15-5-30.
 */
public class DanbooruImage implements Image {

    @Expose
    private Long id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("uploader_id")
    @Expose
    private Long uploaderId;
    @Expose
    private Long score;
    @Expose
    private String source;
    @Expose
    private String md5;
    @SerializedName("last_comment_bumped_at")
    @Expose
    private Object lastCommentBumpedAt;
    @Expose
    private String rating;
    @SerializedName("image_width")
    @Expose
    private Long imageWidth;
    @SerializedName("image_height")
    @Expose
    private Long imageHeight;
    @SerializedName("tag_string")
    @Expose
    private String tagString;
    @SerializedName("is_note_locked")
    @Expose
    private Boolean isNoteLocked;
    @SerializedName("fav_count")
    @Expose
    private Long favCount;
    @SerializedName("file_ext")
    @Expose
    private String fileExt;
    @SerializedName("last_noted_at")
    @Expose
    private Object lastNotedAt;
    @SerializedName("is_rating_locked")
    @Expose
    private Boolean isRatingLocked;
    @SerializedName("parent_id")
    @Expose
    private Object parentId;
    @SerializedName("has_children")
    @Expose
    private Boolean hasChildren;
    @SerializedName("approver_id")
    @Expose
    private Object approverId;
    @SerializedName("tag_count_general")
    @Expose
    private Long tagCountGeneral;
    @SerializedName("tag_count_artist")
    @Expose
    private Long tagCountArtist;
    @SerializedName("tag_count_character")
    @Expose
    private Long tagCountCharacter;
    @SerializedName("tag_count_copyright")
    @Expose
    private Long tagCountCopyright;
    @SerializedName("file_size")
    @Expose
    private Long fileSize;
    @SerializedName("is_status_locked")
    @Expose
    private Boolean isStatusLocked;
    @SerializedName("fav_string")
    @Expose
    private String favString;
    @SerializedName("pool_string")
    @Expose
    private String poolString;
    @SerializedName("up_score")
    @Expose
    private Long upScore;
    @SerializedName("down_score")
    @Expose
    private Long downScore;
    @SerializedName("is_pending")
    @Expose
    private Boolean isPending;
    @SerializedName("is_flagged")
    @Expose
    private Boolean isFlagged;
    @SerializedName("is_deleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("tag_count")
    @Expose
    private Long tagCount;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("is_banned")
    @Expose
    private Boolean isBanned;
    @SerializedName("pixiv_id")
    @Expose
    private Long pixivId;
    @SerializedName("last_commented_at")
    @Expose
    private Object lastCommentedAt;
    @SerializedName("has_active_children")
    @Expose
    private Boolean hasActiveChildren;
    @SerializedName("bit_flags")
    @Expose
    private Long bitFlags;
    @SerializedName("uploader_name")
    @Expose
    private String uploaderName;
    @SerializedName("has_large")
    @Expose
    private Boolean hasLarge;
    @SerializedName("tag_string_artist")
    @Expose
    private String tagStringArtist;
    @SerializedName("tag_string_character")
    @Expose
    private String tagStringCharacter;
    @SerializedName("tag_string_copyright")
    @Expose
    private String tagStringCopyright;
    @SerializedName("tag_string_general")
    @Expose
    private String tagStringGeneral;
    @SerializedName("has_visible_children")
    @Expose
    private Boolean hasVisibleChildren;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("large_file_url")
    @Expose
    private String largeFileUrl;
    @SerializedName("preview_file_url")
    @Expose
    private String previewFileUrl;

    /**
     *
     * @return
     *     The id
     */
    public Long getId() {
        return id;
    }

    @Override
    public String getUser() {
        return ""+getUploaderId();
    }

    @Override
    public String getType() {
        return Utils.getMimeType(getFileExt());
    }

    @Override
    public Long getCount() {
        return (long)0;
    }

    @Override
    public Long getSize() {
        return getFileSize();
    }

    @Override
    public List<String> getTagList() {
        return Utils.tags2List(getTagString());
    }

    /**
     *
     * @param id
     *     The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     *     The uploaderId
     */
    public Long getUploaderId() {
        return uploaderId;
    }

    /**
     *
     * @param uploaderId
     *     The uploader_id
     */
    public void setUploaderId(Long uploaderId) {
        this.uploaderId = uploaderId;
    }

    /**
     *
     * @return
     *     The score
     */
    public Long getScore() {
        return score;
    }

    /**
     *
     * @param score
     *     The score
     */
    public void setScore(Long score) {
        this.score = score;
    }

    /**
     *
     * @return
     *     The source
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     *     The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     *     The md5
     */
    public String getMd5() {
        return md5;
    }

    @Override
    public String getSampleUrl() {
        return Providers.DANBOORU_URI+getLargeFileUrl();
    }

    @Override
    public String getName() {

        String tags = tagString;

        if (tags.length()>50) {
            tags = tags.substring(0, 50);
        }

        return Utils.getProviderName(getPreviewUrl())+" - "+getId()+" "+tags+"."+getFileExt();
    }

    /**
     *
     * @param md5
     *     The md5
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    /**
     *
     * @return
     *     The lastCommentBumpedAt
     */
    public Object getLastCommentBumpedAt() {
        return lastCommentBumpedAt;
    }

    /**
     *
     * @param lastCommentBumpedAt
     *     The last_comment_bumped_at
     */
    public void setLastCommentBumpedAt(Object lastCommentBumpedAt) {
        this.lastCommentBumpedAt = lastCommentBumpedAt;
    }

    /**
     *
     * @return
     *     The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     *     The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     *     The imageWidth
     */
    public Long getImageWidth() {
        return imageWidth;
    }

    /**
     *
     * @param imageWidth
     *     The image_width
     */
    public void setImageWidth(Long imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     *
     * @return
     *     The imageHeight
     */
    public Long getImageHeight() {
        return imageHeight;
    }

    /**
     *
     * @param imageHeight
     *     The image_height
     */
    public void setImageHeight(Long imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     *
     * @return
     *     The tagString
     */
    public String getTagString() {
        return tagString;
    }

    /**
     *
     * @param tagString
     *     The tag_string
     */
    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    /**
     *
     * @return
     *     The isNoteLocked
     */
    public Boolean getIsNoteLocked() {
        return isNoteLocked;
    }

    /**
     *
     * @param isNoteLocked
     *     The is_note_locked
     */
    public void setIsNoteLocked(Boolean isNoteLocked) {
        this.isNoteLocked = isNoteLocked;
    }

    /**
     *
     * @return
     *     The favCount
     */
    public Long getFavCount() {
        return favCount;
    }

    /**
     *
     * @param favCount
     *     The fav_count
     */
    public void setFavCount(Long favCount) {
        this.favCount = favCount;
    }

    /**
     *
     * @return
     *     The fileExt
     */
    public String getFileExt() {
        return fileExt;
    }

    /**
     *
     * @param fileExt
     *     The file_ext
     */
    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    /**
     *
     * @return
     *     The lastNotedAt
     */
    public Object getLastNotedAt() {
        return lastNotedAt;
    }

    /**
     *
     * @param lastNotedAt
     *     The last_noted_at
     */
    public void setLastNotedAt(Object lastNotedAt) {
        this.lastNotedAt = lastNotedAt;
    }

    /**
     *
     * @return
     *     The isRatingLocked
     */
    public Boolean getIsRatingLocked() {
        return isRatingLocked;
    }

    /**
     *
     * @param isRatingLocked
     *     The is_rating_locked
     */
    public void setIsRatingLocked(Boolean isRatingLocked) {
        this.isRatingLocked = isRatingLocked;
    }

    /**
     *
     * @return
     *     The parentId
     */
    public Object getParentId() {
        return parentId;
    }

    /**
     *
     * @param parentId
     *     The parent_id
     */
    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    /**
     *
     * @return
     *     The hasChildren
     */
    public Boolean getHasChildren() {
        return hasChildren;
    }

    /**
     *
     * @param hasChildren
     *     The has_children
     */
    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
     *
     * @return
     *     The approverId
     */
    public Object getApproverId() {
        return approverId;
    }

    /**
     *
     * @param approverId
     *     The approver_id
     */
    public void setApproverId(Object approverId) {
        this.approverId = approverId;
    }

    /**
     *
     * @return
     *     The tagCountGeneral
     */
    public Long getTagCountGeneral() {
        return tagCountGeneral;
    }

    /**
     *
     * @param tagCountGeneral
     *     The tag_count_general
     */
    public void setTagCountGeneral(Long tagCountGeneral) {
        this.tagCountGeneral = tagCountGeneral;
    }

    /**
     *
     * @return
     *     The tagCountArtist
     */
    public Long getTagCountArtist() {
        return tagCountArtist;
    }

    /**
     *
     * @param tagCountArtist
     *     The tag_count_artist
     */
    public void setTagCountArtist(Long tagCountArtist) {
        this.tagCountArtist = tagCountArtist;
    }

    /**
     *
     * @return
     *     The tagCountCharacter
     */
    public Long getTagCountCharacter() {
        return tagCountCharacter;
    }

    /**
     *
     * @param tagCountCharacter
     *     The tag_count_character
     */
    public void setTagCountCharacter(Long tagCountCharacter) {
        this.tagCountCharacter = tagCountCharacter;
    }

    /**
     *
     * @return
     *     The tagCountCopyright
     */
    public Long getTagCountCopyright() {
        return tagCountCopyright;
    }

    /**
     *
     * @param tagCountCopyright
     *     The tag_count_copyright
     */
    public void setTagCountCopyright(Long tagCountCopyright) {
        this.tagCountCopyright = tagCountCopyright;
    }

    /**
     *
     * @return
     *     The fileSize
     */
    public Long getFileSize() {
        return fileSize;
    }

    /**
     *
     * @param fileSize
     *     The file_size
     */
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     *
     * @return
     *     The isStatusLocked
     */
    public Boolean getIsStatusLocked() {
        return isStatusLocked;
    }

    /**
     *
     * @param isStatusLocked
     *     The is_status_locked
     */
    public void setIsStatusLocked(Boolean isStatusLocked) {
        this.isStatusLocked = isStatusLocked;
    }

    /**
     *
     * @return
     *     The favString
     */
    public String getFavString() {
        return favString;
    }

    /**
     *
     * @param favString
     *     The fav_string
     */
    public void setFavString(String favString) {
        this.favString = favString;
    }

    /**
     *
     * @return
     *     The poolString
     */
    public String getPoolString() {
        return poolString;
    }

    /**
     *
     * @param poolString
     *     The pool_string
     */
    public void setPoolString(String poolString) {
        this.poolString = poolString;
    }

    /**
     *
     * @return
     *     The upScore
     */
    public Long getUpScore() {
        return upScore;
    }

    /**
     *
     * @param upScore
     *     The up_score
     */
    public void setUpScore(Long upScore) {
        this.upScore = upScore;
    }

    /**
     *
     * @return
     *     The downScore
     */
    public Long getDownScore() {
        return downScore;
    }

    /**
     *
     * @param downScore
     *     The down_score
     */
    public void setDownScore(Long downScore) {
        this.downScore = downScore;
    }

    /**
     *
     * @return
     *     The isPending
     */
    public Boolean getIsPending() {
        return isPending;
    }

    /**
     *
     * @param isPending
     *     The is_pending
     */
    public void setIsPending(Boolean isPending) {
        this.isPending = isPending;
    }

    /**
     *
     * @return
     *     The isFlagged
     */
    public Boolean getIsFlagged() {
        return isFlagged;
    }

    /**
     *
     * @param isFlagged
     *     The is_flagged
     */
    public void setIsFlagged(Boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    /**
     *
     * @return
     *     The isDeleted
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     *
     * @param isDeleted
     *     The is_deleted
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     *
     * @return
     *     The tagCount
     */
    public Long getTagCount() {
        return tagCount;
    }

    /**
     *
     * @param tagCount
     *     The tag_count
     */
    public void setTagCount(Long tagCount) {
        this.tagCount = tagCount;
    }

    /**
     *
     * @return
     *     The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     *     The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     *     The isBanned
     */
    public Boolean getIsBanned() {
        return isBanned;
    }

    /**
     *
     * @param isBanned
     *     The is_banned
     */
    public void setIsBanned(Boolean isBanned) {
        this.isBanned = isBanned;
    }

    /**
     *
     * @return
     *     The pixivId
     */
    public Long getPixivId() {
        return pixivId;
    }

    /**
     *
     * @param pixivId
     *     The pixiv_id
     */
    public void setPixivId(Long pixivId) {
        this.pixivId = pixivId;
    }

    /**
     *
     * @return
     *     The lastCommentedAt
     */
    public Object getLastCommentedAt() {
        return lastCommentedAt;
    }

    /**
     *
     * @param lastCommentedAt
     *     The last_commented_at
     */
    public void setLastCommentedAt(Object lastCommentedAt) {
        this.lastCommentedAt = lastCommentedAt;
    }

    /**
     *
     * @return
     *     The hasActiveChildren
     */
    public Boolean getHasActiveChildren() {
        return hasActiveChildren;
    }

    /**
     *
     * @param hasActiveChildren
     *     The has_active_children
     */
    public void setHasActiveChildren(Boolean hasActiveChildren) {
        this.hasActiveChildren = hasActiveChildren;
    }

    /**
     *
     * @return
     *     The bitFlags
     */
    public Long getBitFlags() {
        return bitFlags;
    }

    /**
     *
     * @param bitFlags
     *     The bit_flags
     */
    public void setBitFlags(Long bitFlags) {
        this.bitFlags = bitFlags;
    }

    /**
     *
     * @return
     *     The uploaderName
     */
    public String getUploaderName() {
        return uploaderName;
    }

    /**
     *
     * @param uploaderName
     *     The uploader_name
     */
    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    /**
     *
     * @return
     *     The hasLarge
     */
    public Boolean getHasLarge() {
        return hasLarge;
    }

    /**
     *
     * @param hasLarge
     *     The has_large
     */
    public void setHasLarge(Boolean hasLarge) {
        this.hasLarge = hasLarge;
    }

    /**
     *
     * @return
     *     The tagStringArtist
     */
    public String getTagStringArtist() {
        return tagStringArtist;
    }

    /**
     *
     * @param tagStringArtist
     *     The tag_string_artist
     */
    public void setTagStringArtist(String tagStringArtist) {
        this.tagStringArtist = tagStringArtist;
    }

    /**
     *
     * @return
     *     The tagStringCharacter
     */
    public String getTagStringCharacter() {
        return tagStringCharacter;
    }

    /**
     *
     * @param tagStringCharacter
     *     The tag_string_character
     */
    public void setTagStringCharacter(String tagStringCharacter) {
        this.tagStringCharacter = tagStringCharacter;
    }

    /**
     *
     * @return
     *     The tagStringCopyright
     */
    public String getTagStringCopyright() {
        return tagStringCopyright;
    }

    /**
     *
     * @param tagStringCopyright
     *     The tag_string_copyright
     */
    public void setTagStringCopyright(String tagStringCopyright) {
        this.tagStringCopyright = tagStringCopyright;
    }

    /**
     *
     * @return
     *     The tagStringGeneral
     */
    public String getTagStringGeneral() {
        return tagStringGeneral;
    }

    /**
     *
     * @param tagStringGeneral
     *     The tag_string_general
     */
    public void setTagStringGeneral(String tagStringGeneral) {
        this.tagStringGeneral = tagStringGeneral;
    }

    /**
     *
     * @return
     *     The hasVisibleChildren
     */
    public Boolean getHasVisibleChildren() {
        return hasVisibleChildren;
    }

    /**
     *
     * @param hasVisibleChildren
     *     The has_visible_children
     */
    public void setHasVisibleChildren(Boolean hasVisibleChildren) {
        this.hasVisibleChildren = hasVisibleChildren;
    }

    @Override
    public String getPreviewUrl() {
        return Providers.DANBOORU_URI+getPreviewFileUrl();
    }

    @Override
    public Long getWidth() {
        return getImageWidth();
    }

    @Override
    public Long getHeight() {
        return getImageHeight();
    }

    /**
     *
     * @return
     *     The fileUrl
     */
    @Override
    public String getFileUrl() {
        return Providers.DANBOORU_URI+fileUrl;
    }

    /**
     *
     * @param fileUrl
     *     The file_url
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     *
     * @return
     *     The largeFileUrl
     */
    public String getLargeFileUrl() {
        return largeFileUrl;
    }

    /**
     *
     * @param largeFileUrl
     *     The large_file_url
     */
    public void setLargeFileUrl(String largeFileUrl) {
        this.largeFileUrl = largeFileUrl;
    }

    /**
     *
     * @return
     *     The previewFileUrl
     */
    public String getPreviewFileUrl() {
        return previewFileUrl;
    }

    /**
     *
     * @param previewFileUrl
     *     The preview_file_url
     */
    public void setPreviewFileUrl(String previewFileUrl) {
        this.previewFileUrl = previewFileUrl;
    }

}