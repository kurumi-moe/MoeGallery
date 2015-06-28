package moe.kurumi.moegallery.model.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import moe.kurumi.moegallery.model.Image;

/**
 * Created by kurumi on 15-6-21.
 */

@Table(databaseName = GalleryDatabase.NAME)
public class HistoryImage extends BaseModel implements Image, Comparable {

    @Column
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public String previewUrl;

    @Column
    public long width;

    @Column
    public long height;

    @Column
    public String fileUrl;

    @Column
    public String md5;

    @Column
    public String sampleUrl;

    @Column
    public String name;

    @Column
    public long imageId;

    @Column
    public String user;

    @Column
    public String type;

    @Column
    public long count;

    @Column
    public long size;

    @Column
    public String tagList;

    @Column
    public long last;

    public HistoryImage() {

    }

    public HistoryImage(Image image) {

        previewUrl = image.getPreviewUrl();
        width = image.getWidth();
        height = image.getHeight();
        fileUrl = image.getFileUrl();
        md5 = image.getMd5();
        sampleUrl = image.getSampleUrl();
        name = image.getName();
        imageId = image.getId();
        user = image.getUser();
        type = image.getType();
        count = image.getCount();
        size = image.getSize();

        JSONArray jsArray = new JSONArray(image.getTagList());
        tagList = jsArray.toString();

        last = System.currentTimeMillis();
    }

    @Override
    public String getPreviewUrl() {
        return previewUrl;
    }

    @Override
    public Long getWidth() {
        return width;
    }

    @Override
    public Long getHeight() {
        return height;
    }

    @Override
    public String getFileUrl() {
        return fileUrl;
    }

    @Override
    public String getMd5() {
        return md5;
    }

    @Override
    public String getSampleUrl() {
        return sampleUrl;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getId() {
        return imageId;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public Long getSize() {
        return size;
    }

    @Override
    public List<String> getTagList() {
        ArrayList<String> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(tagList);
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int compareTo(Object another) {
        if (last > ((HistoryTag) another).last) {
            return -1;
        } else if (last < ((HistoryTag) another).last) {
            return 1;
        } else {
            return 0;
        }
    }

    public void updateLast() {
        last = System.currentTimeMillis();
    }
}
