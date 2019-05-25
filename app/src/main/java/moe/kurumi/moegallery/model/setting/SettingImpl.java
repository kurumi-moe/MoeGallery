package moe.kurumi.moegallery.model.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import moe.kurumi.moegallery.R;

public class SettingImpl implements Setting {

    private static Context sContext;
    private SharedPreferences mPrefs;

    public SettingImpl() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static Setting getInstance() {
        if (sContext == null) {
            throw new IllegalStateException("Setting is not initialized!");
        }
        return SingletonHelper.INSTANCE;
    }

    private long getLong(int key, int def) {
        return mPrefs.getLong(sContext.getString(key), def);
    }

    private String getString(int key, int def) {
        return mPrefs.getString(sContext.getString(key), sContext.getString(def));
    }

    private boolean getBoolean(int key, boolean def) {
        return mPrefs.getBoolean(sContext.getString(key), def);
    }

    @Override
    public String provider() {
        return getString(R.string.provider_key, R.string.yandere_uri);
    }

    @Override
    public void setProvider(String provider) {
        mPrefs.edit().putString(sContext.getString(R.string.provider_key), provider).apply();
    }

    @Override
    public String animePicturesServer() {
        return getString(R.string.anime_pictures_server_key,
                R.string.default_anime_pictures_cookie);
    }

    @Override
    public void setAnimePicturesServer(String server) {
        mPrefs.edit()
                .putString(sContext.getString(R.string.anime_pictures_server_key), server)
                .apply();
    }

    @Override
    public String animePicturesToken() {
        return getString(R.string.anime_pictures_token_key, R.string.default_anime_pictures_cookie);
    }

    @Override
    public void setAnimePicturesToken(String token) {
        mPrefs.edit()
                .putString(sContext.getString(R.string.anime_pictures_token_key), token)
                .apply();
    }

    @Override
    public long animePicturesExpireDate() {
        return getLong(R.string.anime_pictures_token_expire_date, 0);
    }

    @Override
    public void setAnimePicturesExpireDate(long time) {
        mPrefs.edit()
                .putLong(sContext.getString(R.string.anime_pictures_token_expire_date), time)
                .apply();
    }

    @Override
    public boolean autoDownload() {
        return getBoolean(R.string.auto_download_key, true);
    }

    @Override
    public void setAutoDownload(boolean enable) {
        mPrefs.edit().putBoolean(sContext.getString(R.string.auto_download_key), enable).apply();
    }

    @Override
    public boolean floatSearch() {
        return getBoolean(R.string.float_search_key, true);
    }

    @Override
    public void setFloatSearch(boolean enable) {
        mPrefs.edit().putBoolean(sContext.getString(R.string.float_search_key), enable).apply();
    }

    @Override
    public boolean floatFavorite() {
        return getBoolean(R.string.float_favorite_key, true);
    }

    @Override
    public void setFloatFavorite(boolean enable) {
        mPrefs.edit().putBoolean(sContext.getString(R.string.float_favorite_key), enable).apply();
    }

    @Override
    public long lastUpdate() {
        return mPrefs.getLong(sContext.getString(R.string.last_update_key), 0);
    }

    @Override
    public void setLastUpdate(long time) {
        mPrefs.edit().putLong(sContext.getString(R.string.last_update_key), time).apply();
    }

    @Override
    public boolean autoRotate() {
        return getBoolean(R.string.auto_rotate_key, true);
    }

    @Override
    public void setAutoRotate(boolean enable) {
        mPrefs.edit().putBoolean(sContext.getString(R.string.auto_rotate_key), enable).apply();
    }

    private static class SingletonHelper {
        private final static SettingImpl INSTANCE = new SettingImpl();
    }
}
