package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kurumi on 15-6-1.
 */
public class DanbooruTag implements Tag {

    @Expose
    private Long id;
    @Expose
    private String name;
    @SerializedName("post_count")
    @Expose
    private Long postCount;
    @SerializedName("related_tags")
    @Expose
    private String relatedTags;
    @SerializedName("related_tags_updated_at")
    @Expose
    private String relatedTagsUpdatedAt;
    @Expose
    private Long category;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("is_locked")
    @Expose
    private Boolean isLocked;

    /**
     *
     * @return
     * The id
     */
    public Long getId() {
        return id;
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
     * The name
     */
    public String getName() {
        return name;
    }

    @Override
    public Long getCount() {
        return getPostCount();
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The postCount
     */
    public Long getPostCount() {
        return postCount;
    }

    /**
     *
     * @param postCount
     * The post_count
     */
    public void setPostCount(Long postCount) {
        this.postCount = postCount;
    }

    /**
     *
     * @return
     * The relatedTags
     */
    public String getRelatedTags() {
        return relatedTags;
    }

    /**
     *
     * @param relatedTags
     * The related_tags
     */
    public void setRelatedTags(String relatedTags) {
        this.relatedTags = relatedTags;
    }

    /**
     *
     * @return
     * The relatedTagsUpdatedAt
     */
    public String getRelatedTagsUpdatedAt() {
        return relatedTagsUpdatedAt;
    }

    /**
     *
     * @param relatedTagsUpdatedAt
     * The related_tags_updated_at
     */
    public void setRelatedTagsUpdatedAt(String relatedTagsUpdatedAt) {
        this.relatedTagsUpdatedAt = relatedTagsUpdatedAt;
    }

    /**
     *
     * @return
     * The category
     */
    public Long getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The category
     */
    public void setCategory(Long category) {
        this.category = category;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The isLocked
     */
    public Boolean getIsLocked() {
        return isLocked;
    }

    /**
     *
     * @param isLocked
     * The is_locked
     */
    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * @return The name
     */
    public String toString() {
        return getName().replace('_', ' ') + " (" + getCount() + ")";
    }


}