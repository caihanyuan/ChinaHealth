package com.chinahelth.ui.homepages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.chinahelth.ui.homepages.base.RemoteSyncInterface;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by caihanyuan on 15-8-22.
 */
public class HomepageBaseFragment extends Fragment implements PullToRefreshBase.OnRefreshListener<ScrollView>, RemoteSyncInterface {

    protected View mViewRoot;

    protected ListView mListView;

    protected ScrollView mScrollView;

    protected HomePageContentAdapter mAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView();
        initData();
        setScrollListener();
    }

    protected void initView() {

    }

    protected void initData() {

    }

    protected void setScrollListener() {
        if (mScrollView != null) {
        }
        if (mListView != null) {
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

    }

    @Override
    public void setRemoteSyncListner(RemoteSyncListenr remoteSyncListner) {

    }

    @Override
    public void loadDataInDatabase() {

    }

    @Override
    public void syncDataFromRemote() {

    }
}
