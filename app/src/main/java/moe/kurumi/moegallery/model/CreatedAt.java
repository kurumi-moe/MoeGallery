package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kurumi on 15-5-31.
 */
public class CreatedAt {

    @Expose
    private Long n;
    @Expose
    private Long s;
    @SerializedName("json_class")
    @Expose
    private String jsonClass;

    /**
     *
     * @return
     *     The n
     */
    public Long getN() {
        return n;
    }

    /**
     *
     * @param n
     *     The n
     */
    public void setN(Long n) {
        this.n = n;
    }

    /**
     *
     * @return
     *     The s
     */
    public Long getS() {
        return s;
    }

    /**
     *
     * @param s
     *     The s
     */
    public void setS(Long s) {
        this.s = s;
    }

    /**
     *
     * @return
     *     The jsonClass
     */
    public String getJsonClass() {
        return jsonClass;
    }

    /**
     *
     * @param jsonClass
     *     The json_class
     */
    public void setJsonClass(String jsonClass) {
        this.jsonClass = jsonClass;
    }

}
