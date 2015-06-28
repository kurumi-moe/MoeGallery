package moe.kurumi.moegallery.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by kurumi on 15-6-2.
 */
@Root(name = "tag")
public class GelbooruTag implements Tag {

    @Attribute(name = "type")
    protected Long type;
    @Attribute(name = "count")
    protected Long count;
    @Attribute(name = "name")
    protected String name;
    @Attribute(name = "ambiguous")
    protected String ambiguous;
    @Attribute(name = "id")
    protected Long id;

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     * {@link Byte }
     */
    public Long getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link Byte }
     */
    public void setType(Long value) {
        this.type = value;
    }

    /**
     * Gets the value of the count property.
     *
     * @return possible object is
     * {@link Byte }
     */
    public Long getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     *
     * @param value allowed object is
     *              {@link Byte }
     */
    public void setCount(Long value) {
        this.count = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the ambiguous property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAmbiguous() {
        return ambiguous;
    }

    /**
     * Sets the value of the ambiguous property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAmbiguous(String value) {
        this.ambiguous = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link Short }
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link Short }
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * @return The name
     */
    public String toString() {
        return getName().replace('_', ' ') + " (" + getCount() + ")";
    }

}