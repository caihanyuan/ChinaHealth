package com.chinahelth.ui.homepages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.chinahelth.HealthConfig;
import com.chinahelth.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

/**
 * Created by caihanyuan on 15-7-14.
 */
public class HomepageMainFragment extends HomepageBaseFragment {

    private final static String TAG = HomepageMainFragment.class.getName();

    private PullToRefreshScrollView mPullRefreshScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.homepages_main_layout, container, false);
        return mViewRoot;
    }


    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
        if (HealthConfig.isDebug) {
            Log.d(TAG, "onRefresh execute");
        }
        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initView() {
        mListView = (ListView) mViewRoot.findViewById(R.id.homepage_listview);
        mPullRefreshScrollView = (PullToRefreshScrollView) mViewRoot.findViewById(R.id.homepages_main_pullrefresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(this);
        mScrollView = mPullRefreshScrollView.getRefreshableView();
    }
}
