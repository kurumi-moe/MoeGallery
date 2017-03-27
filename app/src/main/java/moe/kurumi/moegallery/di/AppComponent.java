package moe.kurumi.moegallery.di;

import javax.inject.Singleton;

import dagger.Component;
import moe.kurumi.moegallery.application.Application;
import moe.kurumi.moegallery.data.ImageRepository;
import moe.kurumi.moegallery.di.modules.AppModule;
import moe.kurumi.moegallery.view.adapter.GalleryAdapter;
import moe.kurumi.moegallery.view.adapter.PagerAdapter;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(Application application);

    void inject(GalleryAdapter galleryAdapter);

    void inject(ImageRepository imageRepository);

    void inject(PagerAdapter pagerAdapter);
}
