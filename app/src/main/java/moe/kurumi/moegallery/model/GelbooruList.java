package moe.kurumi.moegallery.model;

/**
 * Created by kurumi on 15-5-31.
 */

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "posts")
public class GelbooruList {

    @ElementList(name = "posts", inline=true)
    protected List<GelbooruImage> post;
    @Attribute(name = "count")
    protected Integer count;
    @Attribute(name = "offset")
    protected Integer offset;

    public List<GelbooruImage> getPost() {
        if (post == null) {
            post = new ArrayList<>();
        }
        return this.post;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer value) {
        this.count = value;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer value) {
        this.offset = value;
    }

}