package moe.kurumi.moegallery.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import javax.inject.Inject;

import moe.kurumi.moegallery.application.Application;
import moe.kurumi.moegallery.data.ImageDataSource;
import moe.kurumi.moegallery.fragment.ImageFragment;
import moe.kurumi.moegallery.model.Image;

public class PagerAdapter extends FragmentStatePagerAdapter {

    @Inject
    ImageDataSource mSource;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        Application.getAppComponent().inject(this);
    }

    @Override
    public Fragment getItem(int position) {
        Image image = mSource.get(position);
        return ImageFragment.newInstance(image);
    }

    @Override
    public int getCount() {
        return mSource.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mSource.get(position).getName();
    }
}
