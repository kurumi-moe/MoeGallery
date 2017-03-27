package moe.kurumi.moegallery.model.setting;

public interface Setting {

    String provider();

    void setProvider(String provider);

    String animePicturesServer();

    void setAnimePicturesServer(String server);

    String animePicturesToken();

    void setAnimePicturesToken(String token);

    boolean autoDownload();

    void setAutoDownload(boolean enable);

    boolean floatSearch();

    void setFloatSearch(boolean enable);

    boolean floatFavorite();

    void setFloatFavorite(boolean enable);

    long lastUpdate();

    void setLastUpdate(long time);

    boolean autoRotate();

    void setAutoRotate(boolean enable);
}
