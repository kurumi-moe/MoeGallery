package moe.kurumi.moegallery.application;

import com.raizlabs.android.dbflow.config.FlowManager;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import moe.kurumi.moegallery.di.AppComponent;
import moe.kurumi.moegallery.di.DaggerAppComponent;
import moe.kurumi.moegallery.di.modules.AppModule;

public class Application extends android.app.Application {

    private static AppComponent sAppComponent;

    private static android.app.Application sApplication;

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

    public static android.app.Application getApplication() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

        sAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        sAppComponent.inject(this);

        CustomActivityOnCrash.install(this);

        FlowManager.init(this);
    }
}
