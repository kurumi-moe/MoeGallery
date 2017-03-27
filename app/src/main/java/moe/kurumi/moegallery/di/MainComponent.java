package moe.kurumi.moegallery.di;

import javax.inject.Singleton;

import dagger.Component;
import moe.kurumi.moegallery.activity.MainActivity;
import moe.kurumi.moegallery.di.modules.AppModule;
import moe.kurumi.moegallery.di.modules.MainModule;
import moe.kurumi.moegallery.fragment.ImageFragment;

@Singleton
@Component(modules = { AppModule.class, MainModule.class })
public interface MainComponent {
    void inject(ImageFragment imageFragment);

    void inject(MainActivity mainActivity);
}
