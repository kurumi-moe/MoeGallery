package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kurumi on 15-6-1.
 */
public class AnimePicturesTagList {

    @SerializedName("tags_list")
    @Expose
    private List<AnimePicturesTag> tagsList = new ArrayList<AnimePicturesTag>();

    /**
     * @return The tagsList
     */
    public List<AnimePicturesTag> getTagsList() {
        return tagsList;
    }

    /**
     * @param tagsList The tags_list
     */
    public void setTagsList(List<AnimePicturesTag> tagsList) {
        this.tagsList = tagsList;
    }

}
