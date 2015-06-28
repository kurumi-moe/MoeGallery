package moe.kurumi.moegallery.model.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by kurumi on 15-6-14.
 */

@Database(name = GalleryDatabase.NAME, version = GalleryDatabase.VERSION)
public class GalleryDatabase {

    public static final String NAME = "Gallery";

    public static final int VERSION = 4;

}
