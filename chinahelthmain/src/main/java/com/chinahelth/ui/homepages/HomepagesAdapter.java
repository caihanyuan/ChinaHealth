package com.chinahelth.ui.homepages;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.chinahelth.HealthApplication;
import com.chinahelth.R;
import com.chinahelth.support.bean.HomePageType;

import java.util.ArrayList;

/**
 * Created by caihanyuan on 15-7-14.
 */
public class HomepagesAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mHomepageFragments = new ArrayList();
    private ArrayList<String> mHomepageTitles = new ArrayList<>();

    public HomepagesAdapter(FragmentManager fm) {
        super(fm);
        initFragments();
    }

    @Override
    public int getCount() {
        return mHomepageFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mHomepageFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mHomepageTitles.get(position);
    }

    private void initFragments() {
        Fragment fragment = null;
        String title = "";
        Resources resources = HealthApplication.getInstance().getResources();

        title = resources.getString(R.string.homepage_title_food);
        fragment = HomepageContentFragment.newInstace(HomePageType.FOOD);
        mHomepageTitles.add(title);
        mHomepageFragments.add(fragment);


        title = resources.getString(R.string.homepage_title_tea);
        fragment = HomepageContentFragment.newInstace(HomePageType.TEA);
        mHomepageTitles.add(title);
        mHomepageFragments.add(fragment);

        title = resources.getString(R.string.homepage_title_kongfu);
        fragment = HomepageContentFragment.newInstace(HomePageType.KONGFU);
        mHomepageTitles.add(title);
        mHomepageFragments.add(fragment);

        title = resources.getString(R.string.homepage_title_buddhism);
        fragment = HomepageContentFragment.newInstace(HomePageType.BUDDHISM);
        mHomepageTitles.add(title);
        mHomepageFragments.add(fragment);
    }
}
