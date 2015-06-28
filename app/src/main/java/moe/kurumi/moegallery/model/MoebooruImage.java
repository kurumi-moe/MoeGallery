package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import moe.kurumi.moegallery.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kurumi on 15-5-28.
 */
public class MoebooruImage implements Image {

    @Expose
    private Long id;
    @Expose
    private String tags;
    @SerializedName("created_at")
    @Expose
    private Long createdAt;
    @SerializedName("creator_id")
    @Expose
    private Long creatorId;
    @Expose
    private String author;
    @Expose
    private Long change;
    @Expose
    private String source;
    @Expose
    private Long score;
    @Expose
    private String md5;
    @SerializedName("file_size")
    @Expose
    private Long fileSize;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("is_shown_in_index")
    @Expose
    private Boolean isShownInIndex;
    @SerializedName("preview_url")
    @Expose
    private String previewUrl;
    @SerializedName("preview_width")
    @Expose
    private Long previewWidth;
    @SerializedName("preview_height")
    @Expose
    private Long previewHeight;
    @SerializedName("actual_preview_width")
    @Expose
    private Long actualPreviewWidth;
    @SerializedName("actual_preview_height")
    @Expose
    private Long actualPreviewHeight;
    @SerializedName("sample_url")
    @Expose
    private String sampleUrl;
    @SerializedName("sample_width")
    @Expose
    private Long sampleWidth;
    @SerializedName("sample_height")
    @Expose
    private Long sampleHeight;
    @SerializedName("sample_file_size")
    @Expose
    private Long sampleFileSize;
    @SerializedName("jpeg_url")
    @Expose
    private String jpegUrl;
    @SerializedName("jpeg_width")
    @Expose
    private Long jpegWidth;
    @SerializedName("jpeg_height")
    @Expose
    private Long jpegHeight;
    @SerializedName("jpeg_file_size")
    @Expose
    private Long jpegFileSize;
    @Expose
    private String rating;
    @SerializedName("has_children")
    @Expose
    private Boolean hasChildren;
    @SerializedName("parent_id")
    @Expose
    private Object parentId;
    @Expose
    private String status;
    @Expose
    private Long width;
    @Expose
    private Long height;
    @SerializedName("is_held")
    @Expose
    private Boolean isHeld;
    @SerializedName("frames_pending_string")
    @Expose
    private String framesPendingString;
    @SerializedName("frames_pending")
    @Expose
    private List<Object> framesPending = new ArrayList<Object>();
    @SerializedName("frames_string")
    @Expose
    private String framesString;
    @Expose
    private List<Object> frames = new ArrayList<Object>();

    /**
     *
     * @return
     * The id
     */
    public Long getId() {
        return id;
    }

    @Override
    public String getUser() {
        return getAuthor();
    }

    @Override
    public String getType() {
        return Utils.getMimeType(getFileUrl());
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
        return Utils.tags2List(getTags());
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The tags
     */
    public String getTags() {
        return tags;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public Long getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The creatorId
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     *
     * @param creatorId
     * The creator_id
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     *
     * @return
     * The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     * The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     * The change
     */
    public Long getChange() {
        return change;
    }

    /**
     *
     * @param change
     * The change
     */
    public void setChange(Long change) {
        this.change = change;
    }

    /**
     *
     * @return
     * The source
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     * The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return
     * The score
     */
    public Long getScore() {
        return score;
    }

    /**
     *
     * @param score
     * The score
     */
    public void setScore(Long score) {
        this.score = score;
    }

    /**
     *
     * @return
     * The md5
     */
    public String getMd5() {
        return md5;
    }

    /**
     *
     * @param md5
     * The md5
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }

    /**
     *
     * @return
     * The fileSize
     */
    public Long getFileSize() {
        return fileSize;
    }

    /**
     *
     * @param fileSize
     * The file_size
     */
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     *
     * @return
     * The fileUrl
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     *
     * @param fileUrl
     * The file_url
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     *
     * @return
     * The isShownInIndex
     */
    public Boolean getIsShownInIndex() {
        return isShownInIndex;
    }

    /**
     *
     * @param isShownInIndex
     * The is_shown_in_index
     */
    public void setIsShownInIndex(Boolean isShownInIndex) {
        this.isShownInIndex = isShownInIndex;
    }

    /**
     *
     * @return
     * The previewUrl
     */
    public String getPreviewUrl() {
        return previewUrl;
    }

    /**
     *
     * @param previewUrl
     * The preview_url
     */
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    /**
     *
     * @return
     * The previewWidth
     */
    public Long getPreviewWidth() {
        return previewWidth;
    }

    /**
     *
     * @param previewWidth
     * The preview_width
     */
    public void setPreviewWidth(Long previewWidth) {
        this.previewWidth = previewWidth;
    }

    /**
     *
     * @return
     * The previewHeight
     */
    public Long getPreviewHeight() {
        return previewHeight;
    }

    /**
     *
     * @param previewHeight
     * The preview_height
     */
    public void setPreviewHeight(Long previewHeight) {
        this.previewHeight = previewHeight;
    }

    /**
     *
     * @return
     * The actualPreviewWidth
     */
    public Long getActualPreviewWidth() {
        return actualPreviewWidth;
    }

    /**
     *
     * @param actualPreviewWidth
     * The actual_preview_width
     */
    public void setActualPreviewWidth(Long actualPreviewWidth) {
        this.actualPreviewWidth = actualPreviewWidth;
    }

    /**
     *
     * @return
     * The actualPreviewHeight
     */
    public Long getActualPreviewHeight() {
        return actualPreviewHeight;
    }

    /**
     *
     * @param actualPreviewHeight
     * The actual_preview_height
     */
    public void setActualPreviewHeight(Long actualPreviewHeight) {
        this.actualPreviewHeight = actualPreviewHeight;
    }

    /**
     *
     * @return
     * The sampleUrl
     */
    public String getSampleUrl() {
        return sampleUrl;
    }

    @Override
    public String getName() {
        return Utils.getFileNameFromUrl(getFileUrl());
    }

    /**
     *
     * @param sampleUrl
     * The sample_url
     */
    public void setSampleUrl(String sampleUrl) {
        this.sampleUrl = sampleUrl;
    }

    /**
     *
     * @return
     * The sampleWidth
     */
    public Long getSampleWidth() {
        return sampleWidth;
    }

    /**
     *
     * @param sampleWidth
     * The sample_width
     */
    public void setSampleWidth(Long sampleWidth) {
        this.sampleWidth = sampleWidth;
    }

    /**
     *
     * @return
     * The sampleHeight
     */
    public Long getSampleHeight() {
        return sampleHeight;
    }

    /**
     *
     * @param sampleHeight
     * The sample_height
     */
    public void setSampleHeight(Long sampleHeight) {
        this.sampleHeight = sampleHeight;
    }

    /**
     *
     * @return
     * The sampleFileSize
     */
    public Long getSampleFileSize() {
        return sampleFileSize;
    }

    /**
     *
     * @param sampleFileSize
     * The sample_file_size
     */
    public void setSampleFileSize(Long sampleFileSize) {
        this.sampleFileSize = sampleFileSize;
    }

    /**
     *
     * @return
     * The jpegUrl
     */
    public String getJpegUrl() {
        return jpegUrl;
    }

    /**
     *
     * @param jpegUrl
     * The jpeg_url
     */
    public void setJpegUrl(String jpegUrl) {
        this.jpegUrl = jpegUrl;
    }

    /**
     *
     * @return
     * The jpegWidth
     */
    public Long getJpegWidth() {
        return jpegWidth;
    }

    /**
     *
     * @param jpegWidth
     * The jpeg_width
     */
    public void setJpegWidth(Long jpegWidth) {
        this.jpegWidth = jpegWidth;
    }

    /**
     *
     * @return
     * The jpegHeight
     */
    public Long getJpegHeight() {
        return jpegHeight;
    }

    /**
     *
     * @param jpegHeight
     * The jpeg_height
     */
    public void setJpegHeight(Long jpegHeight) {
        this.jpegHeight = jpegHeight;
    }

    /**
     *
     * @return
     * The jpegFileSize
     */
    public Long getJpegFileSize() {
        return jpegFileSize;
    }

    /**
     *
     * @param jpegFileSize
     * The jpeg_file_size
     */
    public void setJpegFileSize(Long jpegFileSize) {
        this.jpegFileSize = jpegFileSize;
    }

    /**
     *
     * @return
     * The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     * The hasChildren
     */
    public Boolean getHasChildren() {
        return hasChildren;
    }

    /**
     *
     * @param hasChildren
     * The has_children
     */
    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
     *
     * @return
     * The parentId
     */
    public Object getParentId() {
        return parentId;
    }

    /**
     *
     * @param parentId
     * The parent_id
     */
    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The width
     */
    public Long getWidth() {
        return width;
    }

    /**
     *
     * @param width
     * The width
     */
    public void setWidth(Long width) {
        this.width = width;
    }

    /**
     *
     * @return
     * The height
     */
    public Long getHeight() {
        return height;
    }

    /**
     *
     * @param height
     * The height
     */
    public void setHeight(Long height) {
        this.height = height;
    }

    /**
     *
     * @return
     * The isHeld
     */
    public Boolean getIsHeld() {
        return isHeld;
    }

    /**
     *
     * @param isHeld
     * The is_held
     */
    public void setIsHeld(Boolean isHeld) {
        this.isHeld = isHeld;
    }

    /**
     *
     * @return
     * The framesPendingString
     */
    public String getFramesPendingString() {
        return framesPendingString;
    }

    /**
     *
     * @param framesPendingString
     * The frames_pending_string
     */
    public void setFramesPendingString(String framesPendingString) {
        this.framesPendingString = framesPendingString;
    }

    /**
     *
     * @return
     * The framesPending
     */
    public List<Object> getFramesPending() {
        return framesPending;
    }

    /**
     *
     * @param framesPending
     * The frames_pending
     */
    public void setFramesPending(List<Object> framesPending) {
        this.framesPending = framesPending;
    }

    /**
     *
     * @return
     * The framesString
     */
    public String getFramesString() {
        return framesString;
    }

    /**
     *
     * @param framesString
     * The frames_string
     */
    public void setFramesString(String framesString) {
        this.framesString = framesString;
    }

    /**
     *
     * @return
     * The frames
     */
    public List<Object> getFrames() {
        return frames;
    }

    /**
     *
     * @param frames
     * The frames
     */
    public void setFrames(List<Object> frames) {
        this.frames = frames;
    }

}

