package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import moe.kurumi.moegallery.utils.Utils;

import java.util.List;

/**
 * Created by kurumi on 15-5-31.
 */
public class BehoimiImage implements Image {
    private String status;
    @SerializedName("creator_id")
    @Expose
    private Long creatorId;
    @SerializedName("preview_width")
    @Expose
    private Long previewWidth;
    @Expose
    private String source;
    @Expose
    private String author;
    @Expose
    private Long width;
    @Expose
    private Long score;
    @SerializedName("preview_height")
    @Expose
    private Long previewHeight;
    @SerializedName("has_comments")
    @Expose
    private Boolean hasComments;
    @SerializedName("sample_width")
    @Expose
    private Long sampleWidth;
    @SerializedName("has_children")
    @Expose
    private Boolean hasChildren;
    @SerializedName("sample_url")
    @Expose
    private String sampleUrl;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("parent_id")
    @Expose
    private Object parentId;
    @SerializedName("sample_height")
    @Expose
    private Long sampleHeight;
    @Expose
    private String md5;
    @Expose
    private String tags;
    @Expose
    private Long change;
    @SerializedName("has_notes")
    @Expose
    private Boolean hasNotes;
    @Expose
    private String rating;
    @Expose
    private Long id;
    @Expose
    private Long height;
    @SerializedName("preview_url")
    @Expose
    private String previewUrl;
    @SerializedName("file_size")
    @Expose
    private Long fileSize;
    @SerializedName("created_at")
    @Expose
    private CreatedAt createdAt;

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The creatorId
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId The creator_id
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * @return The previewWidth
     */
    public Long getPreviewWidth() {
        return previewWidth;
    }

    /**
     * @param previewWidth The preview_width
     */
    public void setPreviewWidth(Long previewWidth) {
        this.previewWidth = previewWidth;
    }

    /**
     * @return The source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author The author
     */
    public void setAuthor(String author) {
        this.author = author;
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
     * @return The previewHeight
     */
    public Long getPreviewHeight() {
        return previewHeight;
    }

    /**
     * @param previewHeight The preview_height
     */
    public void setPreviewHeight(Long previewHeight) {
        this.previewHeight = previewHeight;
    }

    /**
     * @return The hasComments
     */
    public Boolean getHasComments() {
        return hasComments;
    }

    /**
     * @param hasComments The has_comments
     */
    public void setHasComments(Boolean hasComments) {
        this.hasComments = hasComments;
    }

    /**
     * @return The sampleWidth
     */
    public Long getSampleWidth() {
        return sampleWidth;
    }

    /**
     * @param sampleWidth The sample_width
     */
    public void setSampleWidth(Long sampleWidth) {
        this.sampleWidth = sampleWidth;
    }

    /**
     * @return The hasChildren
     */
    public Boolean getHasChildren() {
        return hasChildren;
    }

    /**
     * @param hasChildren The has_children
     */
    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    /**
     * @return The sampleUrl
     */
    public String getSampleUrl() {
        return sampleUrl;
    }

    /**
     * @param sampleUrl The sample_url
     */
    public void setSampleUrl(String sampleUrl) {
        this.sampleUrl = sampleUrl;
    }

    @Override
    public String getName() {
        return Utils.getProviderName(getPreviewUrl()) + " - " + getId() + " " +
                getTags() + "." + getFileUrl().substring(getFileUrl().lastIndexOf('.'));
    }

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
     * @return The parentId
     */
    public Object getParentId() {
        return parentId;
    }

    /**
     * @param parentId The parent_id
     */
    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    /**
     * @return The sampleHeight
     */
    public Long getSampleHeight() {
        return sampleHeight;
    }

    /**
     * @param sampleHeight The sample_height
     */
    public void setSampleHeight(Long sampleHeight) {
        this.sampleHeight = sampleHeight;
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

    /**
     * @return The tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags The tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return The change
     */
    public Long getChange() {
        return change;
    }

    /**
     * @param change The change
     */
    public void setChange(Long change) {
        this.change = change;
    }

    /**
     * @return The hasNotes
     */
    public Boolean getHasNotes() {
        return hasNotes;
    }

    /**
     * @param hasNotes The has_notes
     */
    public void setHasNotes(Boolean hasNotes) {
        this.hasNotes = hasNotes;
    }

    /**
     * @return The rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * @param rating The rating
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * @return The id
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
     * @param id The id
     */
    public void setId(Long id) {
        this.id = id;
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
     * @return The previewUrl
     */
    public String getPreviewUrl() {
        return previewUrl;
    }

    /**
     * @param previewUrl The preview_url
     */
    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    /**
     * @return The fileSize
     */
    public Long getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize The file_size
     */
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return The createdAt
     */
    public CreatedAt getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(CreatedAt createdAt) {
        this.createdAt = createdAt;
    }

}