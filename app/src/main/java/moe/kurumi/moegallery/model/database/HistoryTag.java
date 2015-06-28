package moe.kurumi.moegallery.model.database;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by kurumi on 15-6-14.
 */

@Table(databaseName = GalleryDatabase.NAME)
public class HistoryTag extends BaseModel implements Comparable {

    @Column
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public String tag;

    @Column
    public long count;

    @Column
    public long last;

    public HistoryTag() {

    }

    public HistoryTag(String tag, long count, long last) {
        this.tag = tag;
        this.count = count;
        this.last = last;
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
}
