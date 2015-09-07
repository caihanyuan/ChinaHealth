package com.chinahelth.ui.homepages;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinahelth.HealthConfig;
import com.chinahelth.R;
import com.chinahelth.viewpagerIndicator.TabPageIndicator;

/**
 * Created by caihanyuan on 15-7-10.
 */
public class HomepagesContainerFragment extends Fragment {

    public final static String TAG = HomepagesContainerFragment.class.getName();

    private ViewPager mViewPager;

    private TabPageIndicator mPageIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homepages_container_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView(View container) {
        if (HealthConfig.isDebug) {
            Log.d(TAG, "initVeiw");
        }
        mViewPager = (ViewPager) container.findViewById(R.id.homepages_viewpager);
        mPageIndicator = (TabPageIndicator) container.findViewById(R.id.homepages_tabindicator);
        mViewPager.setAdapter(new HomepagesAdapter(getFragmentManager()));
        mPageIndicator.setViewPager(mViewPager);
    }
}
