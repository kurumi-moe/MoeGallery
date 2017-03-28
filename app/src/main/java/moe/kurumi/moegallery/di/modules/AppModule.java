package moe.kurumi.moegallery.di.modules;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import moe.kurumi.moegallery.data.ImageDataSource;
import moe.kurumi.moegallery.data.ImageRepository;
import moe.kurumi.moegallery.model.setting.Setting;
import moe.kurumi.moegallery.model.setting.SettingImpl;
import moe.kurumi.moegallery.utils.OkHttp;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return mApplication;
    }

    @Singleton
    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @Singleton
    @Provides
    Retrofit.Builder provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson));
    }

    @Singleton
    @Provides
    @Named("xml")
    Retrofit.Builder provideXmlRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create());
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        return OkHttp.getInstance().client();
    }

    @Singleton
    @Provides
    Setting provideSetting(Context context) {
        SettingImpl.init(context);
        return SettingImpl.getInstance();
    }

    @Singleton
    @Provides
    ImageDataSource provideImageDataSource() {
        return ImageRepository.getInstance();
    }
}
