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
    protected Byte offset;

    public List<GelbooruImage> getPost() {
        if (post == null) {
            post = new ArrayList<>();
        }
        return this.post;
    }

    /**
     * Gets the value of the count property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setCount(Integer value) {
        this.count = value;
    }

    /**
     * Gets the value of the offset property.
     *
     * @return
     *     possible object is
     *     {@link Byte }
     *
     */
    public Byte getOffset() {
        return offset;
    }

    /**
     * Sets the value of the offset property.
     *
     * @param value
     *     allowed object is
     *     {@link Byte }
     *
     */
    public void setOffset(Byte value) {
        this.offset = value;
    }

}