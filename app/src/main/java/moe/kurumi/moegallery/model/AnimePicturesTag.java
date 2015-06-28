package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;

public class AnimePicturesTag implements Tag{

    @Expose
    private Long c;
    @Expose
    private Object t2;
    @Expose
    private String t;
    @Expose
    private Long id;

    /**
     * @return The c
     */
    public Long getC() {
        return c;
    }

    /**
     * @param c The c
     */
    public void setC(Long c) {
        this.c = c;
    }

    /**
     * @return The t2
     */
    public Object getT2() {
        return t2;
    }

    /**
     * @param t2 The t2
     */
    public void setT2(Object t2) {
        this.t2 = t2;
    }

    /**
     * @return The t
     */
    public String getT() {
        return t;
    }

    /**
     * @param t The t
     */
    public void setT(String t) {
        this.t = t;
    }

    /**
     * @return The id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return getT().replace("<b>", "").replace("</b>", "");
    }

    @Override
    public Long getCount() {
        return -getC();
    }

    /**
     * @return The name
     */
    public String toString() {
        return getName();
    }

}