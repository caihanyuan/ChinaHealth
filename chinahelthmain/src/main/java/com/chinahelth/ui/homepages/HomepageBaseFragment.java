package com.chinahelth.ui.homepages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.chinahelth.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * Created by caihanyuan on 15-8-22.
 */
public class HomepageBaseFragment extends Fragment implements PullToRefreshBase.OnRefreshListener {

    private AbsListView.OnScrollListener mOnScrollListener;

    protected View mViewRoot;

    protected ListView mListView;

    protected PullToRefreshListView mRefreshListView;

    protected HomePageContentAdapter mAdapter;

    protected PauseOnScrollListener mPauseOnScrollListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewRoot = inflater.inflate(R.layout.homepages_fragment_layout, container, false);
        mRefreshListView = (PullToRefreshListView) mViewRoot.findViewById(R.id.homepage_refreshview);
        mListView = mRefreshListView.getRefreshableView();
        mRefreshListView.setOnRefreshListener(this);
        return mViewRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView();
        initData();
        setPauseScrollListener();
    }

    protected void initView() {
    }

    protected void initData() {

    }

    public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

    }

    private void setPauseScrollListener() {
        if (mPauseOnScrollListener == null) {
            mPauseOnScrollListener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true, mOnScrollListener);
        }
        mRefreshListView.setOnScrollListener(mPauseOnScrollListener);
    }
}
