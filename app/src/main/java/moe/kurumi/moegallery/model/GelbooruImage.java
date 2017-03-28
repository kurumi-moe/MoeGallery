package moe.kurumi.moegallery.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.List;

import moe.kurumi.moegallery.utils.Utils;

/**
 * Created by kurumi on 15-5-31.
 */

@Root(name = "post")
public class GelbooruImage implements Image {

    @Attribute(name = "height")
    protected Short height;
    @Attribute(name = "score")
    protected Byte score;
    @Attribute(name = "file_url")
    protected String fileUrl;
    @Attribute(name = "parent_id")
    protected String parentId;
    @Attribute(name = "sample_url")
    protected String sampleUrl;
    @Attribute(name = "sample_width")
    protected Short sampleWidth;
    @Attribute(name = "sample_height")
    protected Short sampleHeight;
    @Attribute(name = "preview_url")
    protected String previewUrl;
    @Attribute(name = "rating")
    protected String rating;
    @Attribute(name = "tags")
    protected String tags;
    @Attribute(name = "id")
    protected Integer id;
    @Attribute(name = "width")
    protected Short width;
    @Attribute(name = "change")
    protected Integer change;
    @Attribute(name = "md5")
    protected String md5;
    @Attribute(name = "creator_id")
    protected Integer creatorId;
    @Attribute(name = "has_children")
    protected String hasChildren;
    @Attribute(name = "created_at")
    protected String createdAt;
    @Attribute(name = "status")
    protected String status;
    @Attribute(name = "source")
    protected String source;
    @Attribute(name = "has_notes")
    protected String hasNotes;
    @Attribute(name = "has_comments")
    protected String hasComments;
    @Attribute(name = "preview_width")
    protected Short previewWidth;
    @Attribute(name = "preview_height")
    protected Short previewHeight;

    /**
     * Gets the value of the height property.
     *
     * @return possible object is
     * {@link Short }
     */
    public Long getHeight() {
        return (long) height;
    }

    /**
     * Sets the value of the height property.
     *
     * @param value allowed object is
     * {@link Short }
     */
    public void setHeight(Short value) {
        this.height = value;
    }

    /**
     * Gets the value of the score property.
     *
     * @return possible object is
     * {@link Byte }
     */
    public Byte getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     *
     * @param value allowed object is
     * {@link Byte }
     */
    public void setScore(Byte value) {
        this.score = value;
    }

    /**
     * Gets the value of the fileUrl property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFileUrl() {
        if (fileUrl.startsWith("http")) {
            return fileUrl;
        }
        return "https:" + fileUrl;
    }

    /**
     * Sets the value of the fileUrl property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setFileUrl(String value) {
        this.fileUrl = value;
    }

    /**
     * Gets the value of the parentId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets the value of the parentId property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setParentId(String value) {
        this.parentId = value;
    }

    /**
     * Gets the value of the sampleUrl property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSampleUrl() {
        if (sampleUrl.startsWith("http")) {
            return sampleUrl;
        }
        return "https:" + sampleUrl;
    }

    /**
     * Sets the value of the sampleUrl property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setSampleUrl(String value) {
        this.sampleUrl = value;
    }

    @Override
    public String getName() {

        String tags = getTags();
        if (tags.length() > 50) {
            tags = tags.substring(0, 50);
        }
        String ext = getFileUrl().substring(getFileUrl().lastIndexOf('.'));

        return Utils.getProviderName(getPreviewUrl()) + " - " + getId() + " " + tags + ext;
    }

    /**
     * Gets the value of the sampleWidth property.
     *
     * @return possible object is
     * {@link Short }
     */
    public Short getSampleWidth() {
        return sampleWidth;
    }

    /**
     * Sets the value of the sampleWidth property.
     *
     * @param value allowed object is
     * {@link Short }
     */
    public void setSampleWidth(Short value) {
        this.sampleWidth = value;
    }

    /**
     * Gets the value of the sampleHeight property.
     *
     * @return possible object is
     * {@link Short }
     */
    public Short getSampleHeight() {
        return sampleHeight;
    }

    /**
     * Sets the value of the sampleHeight property.
     *
     * @param value allowed object is
     * {@link Short }
     */
    public void setSampleHeight(Short value) {
        this.sampleHeight = value;
    }

    /**
     * Gets the value of the previewUrl property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPreviewUrl() {
        if (previewUrl.startsWith("http")) {
            return previewUrl;
        }
        return "https:" + previewUrl;
    }

    /**
     * Sets the value of the previewUrl property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setPreviewUrl(String value) {
        this.previewUrl = value;
    }

    /**
     * Gets the value of the rating property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRating() {
        return rating;
    }

    /**
     * Sets the value of the rating property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setRating(String value) {
        this.rating = value;
    }

    /**
     * Gets the value of the tags property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTags() {
        return tags;
    }

    /**
     * Sets the value of the tags property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setTags(String value) {
        this.tags = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Long getId() {
        return (long) id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     * {@link Integer }
     */
    public void setId(Integer value) {
        this.id = value;
    }

    @Override
    public String getUser() {
        return "";
    }

    @Override
    public String getType() {
        return Utils.getMimeType(getFileUrl());
    }

    @Override
    public Long getCount() {
        return (long) 0;
    }

    @Override
    public Long getSize() {
        return (long) 0;
    }

    @Override
    public List<String> getTagList() {
        return Utils.tags2List(getTags());
    }

    /**
     * Gets the value of the width property.
     *
     * @return possible object is
     * {@link Short }
     */
    public Long getWidth() {
        return (long) width;
    }

    /**
     * Sets the value of the width property.
     *
     * @param value allowed object is
     * {@link Short }
     */
    public void setWidth(Short value) {
        this.width = value;
    }

    /**
     * Gets the value of the change property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getChange() {
        return change;
    }

    /**
     * Sets the value of the change property.
     *
     * @param value allowed object is
     * {@link Integer }
     */
    public void setChange(Integer value) {
        this.change = value;
    }

    /**
     * Gets the value of the md5 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMd5() {
        return md5;
    }

    /**
     * Sets the value of the md5 property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setMd5(String value) {
        this.md5 = value;
    }

    /**
     * Gets the value of the creatorId property.
     *
     * @return possible object is
     * {@link Integer }
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * Sets the value of the creatorId property.
     *
     * @param value allowed object is
     * {@link Integer }
     */
    public void setCreatorId(Integer value) {
        this.creatorId = value;
    }

    /**
     * Gets the value of the hasChildren property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getHasChildren() {
        return hasChildren;
    }

    /**
     * Sets the value of the hasChildren property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setHasChildren(String value) {
        this.hasChildren = value;
    }

    /**
     * Gets the value of the createdAt property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the value of the createdAt property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setCreatedAt(String value) {
        this.createdAt = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the source property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the hasNotes property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getHasNotes() {
        return hasNotes;
    }

    /**
     * Sets the value of the hasNotes property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setHasNotes(String value) {
        this.hasNotes = value;
    }

    /**
     * Gets the value of the hasComments property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getHasComments() {
        return hasComments;
    }

    /**
     * Sets the value of the hasComments property.
     *
     * @param value allowed object is
     * {@link String }
     */
    public void setHasComments(String value) {
        this.hasComments = value;
    }

    /**
     * Gets the value of the previewWidth property.
     *
     * @return possible object is
     * {@link Short }
     */
    public Short getPreviewWidth() {
        return previewWidth;
    }

    /**
     * Sets the value of the previewWidth property.
     *
     * @param value allowed object is
     * {@link Short }
     */
    public void setPreviewWidth(Short value) {
        this.previewWidth = value;
    }

    /**
     * Gets the value of the previewHeight property.
     *
     * @return possible object is
     * {@link Short }
     */
    public Short getPreviewHeight() {
        return previewHeight;
    }

    /**
     * Sets the value of the previewHeight property.
     *
     * @param value allowed object is
     * {@link Short }
     */
    public void setPreviewHeight(Short value) {
        this.previewHeight = value;
    }

}