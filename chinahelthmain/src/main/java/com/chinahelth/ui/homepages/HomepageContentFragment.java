package com.chinahelth.ui.homepages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.chinahelth.R;
import com.chinahelth.support.lib.MyAsyncTask;
import com.chinahelth.support.utils.LogUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

/**
 * Created by caihanyuan on 15-8-22.
 */
public class HomepageContentFragment extends Fragment implements PullToRefreshBase.OnRefreshListener {

    private final static String TAG = HomepageContentFragment.class.getSimpleName();

    protected int mLastOffset = 0;

    protected View mViewRoot;

    protected PullToRefreshListView mRefreshListView;

    protected HomePageContentAdapter mAdapter;

    protected PauseOnScrollListener mPauseOnScrollListener;

    public int mPageType = -1;

    public boolean mDataInit = false;

    public static HomepageContentFragment newInstace(int pageType) {
        HomepageContentFragment fragment = new HomepageContentFragment();
        fragment.mPageType = pageType;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new HomePageContentAdapter(getActivity(), mPageType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.d(TAG, "view " + mPageType + " onCreateView");
        mViewRoot = inflater.inflate(R.layout.homepages_fragment_layout, container, false);
        mRefreshListView = (PullToRefreshListView) mViewRoot.findViewById(R.id.homepage_refreshview);
        mRefreshListView.setOnRefreshListener(this);
        mRefreshListView.setOnScrollListener(mPauseOnScrollListener);
        mRefreshListView.setAdapter(mAdapter);
        return mViewRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d(TAG, "view " + mPageType + " onDestroyView");
        mRefreshListView.setOnScrollListener(null);
        mRefreshListView.setAdapter(null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView();
        initData();
    }

    protected void initView() {
    }

    protected void initData() {
        if (!mDataInit) {
            LogUtils.d(TAG, "view " + mPageType + " initData");
            new LocalDataAccessTask().executeOnDatabase();
            mDataInit = true;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

    }

    private void setPauseScrollListener(AbsListView.OnScrollListener customListener) {
        if (mPauseOnScrollListener == null) {
            mPauseOnScrollListener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true, customListener);
        }
        if (mRefreshListView != null) {
            mRefreshListView.setOnScrollListener(mPauseOnScrollListener);
        }
    }

    private boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    private class LocalDataAccessTask extends MyAsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            if (isEmpty()) {

            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            mLastOffset = mAdapter.getLocalData(mLastOffset);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!isEmpty()) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
