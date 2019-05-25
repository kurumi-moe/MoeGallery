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
    protected Integer height;
    @Attribute(name = "score")
    protected Integer score;
    @Attribute(name = "file_url")
    protected String fileUrl;
    @Attribute(name = "parent_id")
    protected String parentId;
    @Attribute(name = "sample_url")
    protected String sampleUrl;
    @Attribute(name = "sample_width")
    protected Integer sampleWidth;
    @Attribute(name = "sample_height")
    protected Integer sampleHeight;
    @Attribute(name = "preview_url")
    protected String previewUrl;
    @Attribute(name = "rating")
    protected String rating;
    @Attribute(name = "tags")
    protected String tags;
    @Attribute(name = "id")
    protected Integer id;
    @Attribute(name = "width")
    protected Integer width;
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
    protected Integer previewWidth;
    @Attribute(name = "preview_height")
    protected Integer previewHeight;


    public Long getHeight() {
        return (long) height;
    }


    public void setHeight(Integer value) {
        this.height = value;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer value) {
        this.score = value;
    }

    public String getFileUrl() {
        if (fileUrl.startsWith("http")) {
            return fileUrl;
        }
        return "https:" + fileUrl;
    }

    public void setFileUrl(String value) {
        this.fileUrl = value;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String value) {
        this.parentId = value;
    }

    public String getSampleUrl() {
        if (sampleUrl.startsWith("http")) {
            return sampleUrl;
        }
        return "https:" + sampleUrl;
    }

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

    public Integer getSampleWidth() {
        return sampleWidth;
    }

    public void setSampleWidth(Integer value) {
        this.sampleWidth = value;
    }

    public Integer getSampleHeight() {
        return sampleHeight;
    }

    public void setSampleHeight(Integer value) {
        this.sampleHeight = value;
    }

    public String getPreviewUrl() {
        if (previewUrl.startsWith("http")) {
            return previewUrl;
        }
        return "https:" + previewUrl;
    }

    public void setPreviewUrl(String value) {
        this.previewUrl = value;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String value) {
        this.rating = value;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String value) {
        this.tags = value;
    }

    public Long getId() {
        return (long) id;
    }

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

    public Long getWidth() {
        return (long) width;
    }

    public void setWidth(Integer value) {
        this.width = value;
    }

    public Integer getChange() {
        return change;
    }

    public void setChange(Integer value) {
        this.change = value;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String value) {
        this.md5 = value;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer value) {
        this.creatorId = value;
    }

    public String getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(String value) {
        this.hasChildren = value;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String value) {
        this.createdAt = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public String getHasNotes() {
        return hasNotes;
    }

    public void setHasNotes(String value) {
        this.hasNotes = value;
    }

    public String getHasComments() {
        return hasComments;
    }

    public void setHasComments(String value) {
        this.hasComments = value;
    }

    public Integer getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(Integer value) {
        this.previewWidth = value;
    }

    public Integer getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(Integer value) {
        this.previewHeight = value;
    }

}