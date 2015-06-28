package moe.kurumi.moegallery.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import moe.kurumi.moegallery.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kurumi on 15-5-31.
 */
public class AnimePicturesList {

    @SerializedName("max_pages")
    @Expose
    private Long maxPages;
    @SerializedName("response_posts_count")
    @Expose
    private Long responsePostsCount;
    @SerializedName("posts_per_page")
    @Expose
    private Long postsPerPage;
    @Expose
    private List<AnimePicturesPreview> posts = new ArrayList<AnimePicturesPreview>();
    @SerializedName("page_number")
    @Expose
    private Long pageNumber;
    @SerializedName("posts_count")
    @Expose
    private Long postsCount;

    /**
     *
     * @return
     *     The maxPages
     */
    public Long getMaxPages() {
        return maxPages;
    }

    /**
     *
     * @param maxPages
     *     The max_pages
     */
    public void setMaxPages(Long maxPages) {
        this.maxPages = maxPages;
    }

    /**
     *
     * @return
     *     The responsePostsCount
     */
    public Long getResponsePostsCount() {
        return responsePostsCount;
    }

    /**
     *
     * @param responsePostsCount
     *     The response_posts_count
     */
    public void setResponsePostsCount(Long responsePostsCount) {
        this.responsePostsCount = responsePostsCount;
    }

    /**
     *
     * @return
     *     The postsPerPage
     */
    public Long getPostsPerPage() {
        return postsPerPage;
    }

    /**
     *
     * @param postsPerPage
     *     The posts_per_page
     */
    public void setPostsPerPage(Long postsPerPage) {
        this.postsPerPage = postsPerPage;
    }

    /**
     *
     * @return
     *     The posts
     */
    public List<AnimePicturesPreview> getPreviews() {
        return posts;
    }

    /**
     *
     * @param posts
     *     The posts
     */
    public void setPreviews(List<AnimePicturesPreview> posts) {
        this.posts = posts;
    }

    /**
     *
     * @return
     *     The pageNumber
     */
    public Long getPageNumber() {
        return pageNumber;
    }

    /**
     *
     * @param pageNumber
     *     The page_number
     */
    public void setPageNumber(Long pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     *
     * @return
     *     The postsCount
     */
    public Long getPostsCount() {
        return postsCount;
    }

    /**
     *
     * @param postsCount
     *     The posts_count
     */
    public void setPostsCount(Long postsCount) {
        this.postsCount = postsCount;
    }

    public class AnimePicturesPreview implements Image {

        @SerializedName("medium_preview")
        @Expose
        private String mediumPreview;
        @Expose
        private Long erotics;
        @SerializedName("small_preview")
        @Expose
        private String smallPreview;
        @SerializedName("big_preview")
        @Expose
        private String bigPreview;
        @Expose
        private String ext;
        @SerializedName("score_number")
        @Expose
        private Long scoreNumber;
        @Expose
        private Long height;
        @Expose
        private Long width;
        @SerializedName("md5_pixels")
        @Expose
        private String md5Pixels;
        @Expose
        private Long score;
        @Expose
        private String pubtime;
        @SerializedName("download_count")
        @Expose
        private Long downloadCount;
        @Expose
        private Long size;
        @Expose
        private Long id;
        @Expose
        private List<Long> color = new ArrayList<Long>();
        @Expose
        private String md5;

        /**
         *
         * @return
         *     The mediumPreview
         */
        public String getMediumPreview() {
            return mediumPreview;
        }

        /**
         *
         * @param mediumPreview
         *     The medium_preview
         */
        public void setMediumPreview(String mediumPreview) {
            this.mediumPreview = mediumPreview;
        }

        /**
         *
         * @return
         *     The erotics
         */
        public Long getErotics() {
            return erotics;
        }

        /**
         *
         * @param erotics
         *     The erotics
         */
        public void setErotics(Long erotics) {
            this.erotics = erotics;
        }

        /**
         *
         * @return
         *     The smallPreview
         */
        public String getSmallPreview() {
            return smallPreview;
        }

        /**
         *
         * @param smallPreview
         *     The small_preview
         */
        public void setSmallPreview(String smallPreview) {
            this.smallPreview = smallPreview;
        }

        /**
         *
         * @return
         *     The bigPreview
         */
        public String getBigPreview() {
            return bigPreview;
        }

        /**
         *
         * @param bigPreview
         *     The big_preview
         */
        public void setBigPreview(String bigPreview) {
            this.bigPreview = bigPreview;
        }

        /**
         *
         * @return
         *     The ext
         */
        public String getExt() {
            return ext;
        }

        /**
         *
         * @param ext
         *     The ext
         */
        public void setExt(String ext) {
            this.ext = ext;
        }

        /**
         *
         * @return
         *     The scoreNumber
         */
        public Long getScoreNumber() {
            return scoreNumber;
        }

        /**
         *
         * @param scoreNumber
         *     The score_number
         */
        public void setScoreNumber(Long scoreNumber) {
            this.scoreNumber = scoreNumber;
        }

        /**
         *
         * @return
         *     The height
         */
        public Long getHeight() {
            return height;
        }

        @Override
        public String getFileUrl() {
            return getBigPreview();
        }

        /**
         *
         * @param height
         *     The height
         */
        public void setHeight(Long height) {
            this.height = height;
        }

        @Override
        public String getPreviewUrl() {
            return getBigPreview();
        }

        /**
         *
         * @return
         *     The width
         */
        public Long getWidth() {
            return width;
        }

        /**
         *
         * @param width
         *     The width
         */
        public void setWidth(Long width) {
            this.width = width;
        }

        /**
         *
         * @return
         *     The md5Pixels
         */
        public String getMd5Pixels() {
            return md5Pixels;
        }

        /**
         *
         * @param md5Pixels
         *     The md5_pixels
         */
        public void setMd5Pixels(String md5Pixels) {
            this.md5Pixels = md5Pixels;
        }

        /**
         *
         * @return
         *     The score
         */
        public Long getScore() {
            return score;
        }

        /**
         *
         * @param score
         *     The score
         */
        public void setScore(Long score) {
            this.score = score;
        }

        /**
         *
         * @return
         *     The pubtime
         */
        public String getPubtime() {
            return pubtime;
        }

        /**
         *
         * @param pubtime
         *     The pubtime
         */
        public void setPubtime(String pubtime) {
            this.pubtime = pubtime;
        }

        /**
         *
         * @return
         *     The downloadCount
         */
        public Long getDownloadCount() {
            return downloadCount;
        }

        /**
         *
         * @param downloadCount
         *     The download_count
         */
        public void setDownloadCount(Long downloadCount) {
            this.downloadCount = downloadCount;
        }

        /**
         *
         * @return
         *     The size
         */
        public Long getSize() {
            return size;
        }

        @Override
        public List<String> getTagList() {
            return Utils.tags2List("");
        }

        /**
         *
         * @param size
         *     The size
         */
        public void setSize(Long size) {
            this.size = size;
        }

        /**
         *
         * @return
         *     The id
         */
        public Long getId() {
            return id;
        }

        @Override
        public String getUser() {
            return "";
        }

        @Override
        public String getType() {
            return Utils.getMimeType(getExt());
        }

        @Override
        public Long getCount() {
            return getDownloadCount();
        }

        /**
         *
         * @param id
         *     The id
         */
        public void setId(Long id) {
            this.id = id;
        }

        /**
         *
         * @return
         *     The color
         */
        public List<Long> getColor() {
            return color;
        }

        /**
         *
         * @param color
         *     The color
         */
        public void setColor(List<Long> color) {
            this.color = color;
        }

        /**
         *
         * @return
         *     The md5
         */
        public String getMd5() {
            return md5;
        }

        @Override
        public String getSampleUrl() {
            return getBigPreview();
        }

        @Override
        public String getName() {
            return "no_name"+getExt();
        }

        /**
         *
         * @param md5
         *     The md5
         */
        public void setMd5(String md5) {
            this.md5 = md5;
        }

    }
}