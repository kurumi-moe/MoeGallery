package moe.kurumi.moegallery.model;

import java.util.List;

/**
 * Created by kurumi on 15-5-30.
 */

// Note: use http://www.jsonschema2pojo.org/ for json convert

public interface Image {

    /**
     * @return The previewUrl
     */
    String getPreviewUrl();

    /**
     * @return The width
     */
    Long getWidth();

    /**
     * @return The height
     */
    Long getHeight();

    /**
     * @return The fileUrl
     */
    String getFileUrl();

    /**
     * @return The md5
     */
    String getMd5();

    /**
     * @return The sampleUrl
     */
    String getSampleUrl();

    /**
     * @return The name
     */
    String getName();

    /**
     * @return The id
     */
    Long getId();

    /**
     * @return The user
     */
    String getUser();

    /**
     * @return The type
     */
    String getType();

    /**
     * @return The count
     */
    Long getCount();

    /**
     * @return The size
     */
    Long getSize();

    /**
     * @return The tags
     */
    List<String> getTagList();
}
