package moe.kurumi.moegallery.model;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.DefaultRes;
import org.androidannotations.annotations.sharedpreferences.SharedPref;
import moe.kurumi.moegallery.R;

/**
 * Created by kurumi on 15-5-30.
 */

@SharedPref(value = SharedPref.Scope.APPLICATION_DEFAULT)
public interface Preferences {

    @DefaultRes(R.string.yandere_uri)
    String provider();

    @DefaultRes(R.string.default_anime_pictures_cookie)
    String animePicturesServer();

    @DefaultRes(R.string.default_anime_pictures_cookie)
    String animePicturesToken();

    @DefaultBoolean(value=true, keyRes=R.string.auto_download_key)
    boolean autoDownload();

    @DefaultBoolean(value=true, keyRes=R.string.float_search_key)
    boolean floatSearch();

    @DefaultBoolean(value=true, keyRes=R.string.float_favorite_key)
    boolean floatFavorite();

    @DefaultLong(value=0)
    long lastUpdate();

}
