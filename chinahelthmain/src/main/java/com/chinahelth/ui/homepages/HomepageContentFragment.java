package com.chinahelth.ui.homepages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.chinahelth.HealthConfig;
import com.chinahelth.R;
import com.chinahelth.support.bean.ArticleItemBean;
import com.chinahelth.support.bean.ServerParam;
import com.chinahelth.support.datacenter.ArticleItemDataCenter;
import com.chinahelth.support.lib.MyAsyncTask;
import com.chinahelth.support.utils.LogUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by caihanyuan on 15-8-22.
 */
public class HomepageContentFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2 {

    private final static String TAG = HomepageContentFragment.class.getSimpleName();

    public int mPageType = 0;

    protected View mViewRoot;

    protected PullToRefreshListView mRefreshListView;

    protected HomePageContentAdapter mAdapter;

    protected PauseOnScrollListener mPauseOnScrollListener;

    private boolean mDataInit = false;

    private AtomicInteger mLocalTaskCount = new AtomicInteger(0);

    private AtomicInteger mRemoteTaskCount = new AtomicInteger(0);

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
        mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mRefreshListView.setOnRefreshListener(this);
        mRefreshListView.setAdapter(mAdapter);
        setPauseScrollListener(null);
        return mViewRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d(TAG, "view " + mPageType + " onDestroyView");
        mRefreshListView.setOnLastItemVisibleListener(null);
        mRefreshListView.setOnScrollListener(null);
        mRefreshListView.setAdapter(null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    public void onDestroy() {
        mAdapter.destory();
        super.onDestroy();
    }

    protected void initView() {
    }

    protected void initData() {
        if (!mDataInit) {
            LogUtils.d(TAG, "view " + mPageType + " initData");
            mRefreshListView.setRefreshing(false);
            mDataInit = true;
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        LogUtils.d(TAG, "onPullUpToRefresh");
        if (!isRemoteAccessTaskBusy()) {
            executeRemoteDataReadTask(ServerParam.VALUES.DATA_STATUS_OLDER);
        }
        if (!isLocalAccessTaskBusy()) {
            executeLocalDataReadTask(ServerParam.VALUES.DATA_STATUS_OLDER);
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        LogUtils.d(TAG, "onPullDownToRefresh");
        if (!isRemoteAccessTaskBusy()) {
            executeRemoteDataReadTask(ServerParam.VALUES.DATA_STATUS_NEWER);
        }
        if (!isLocalAccessTaskBusy()) {
            executeLocalDataReadTask(ServerParam.VALUES.DATA_STATUS_NEWER);
        }
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

    private boolean isLocalAccessTaskBusy() {
        int taskCount = mLocalTaskCount.get();
        return taskCount != 0;
    }

    private boolean isRemoteAccessTaskBusy() {
        int taskCount = mLocalTaskCount.get();
        return taskCount != 0;
    }

    private void executeLocalDataReadTask(String dataStatus) {
        mLocalTaskCount.getAndIncrement();
        new LocalDataReadTask().executeOnDatabase(dataStatus);
    }

    private void executeRemoteDataReadTask(String dataStatus) {
        mRemoteTaskCount.getAndIncrement();
        new RemoteDataReadTask().executeOnIO(dataStatus);
    }

    /**
     * Load data from database only which contains enough items to show<br>
     * <p/>
     * <br>params:ArticleItemBean  the last item bean show on view
     * <br>result:List<ArticleItemBean> items' data from database
     */
    private class LocalDataReadTask extends MyAsyncTask<String, Void, List<ArticleItemBean>> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<ArticleItemBean> doInBackground(String... params) {
            String dataStatus = params[0];
            ArticleItemBean articleItemBean = null;
            if (dataStatus.equals(ServerParam.VALUES.DATA_STATUS_NEWER)) {
                articleItemBean = mAdapter.getFirstItemBean();
            } else {
                articleItemBean = mAdapter.getLastItemBean();
            }
            return ArticleItemDataCenter.getInstance().getLocalItemDatas(articleItemBean, mPageType, HealthConfig.ARTICLE_ITEM_ONCE_LOAD_NUM, dataStatus);
        }

        @Override
        protected void onPostExecute(List<ArticleItemBean> itemBeans) {
            mLocalTaskCount.getAndDecrement();
            if (itemBeans.size() > 0) {
                mAdapter.addAll(itemBeans);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private class LocalDataWriteTask extends MyAsyncTask<List<ArticleItemBean>, Void, Boolean> {
        @Override
        protected Boolean doInBackground(List<ArticleItemBean>... params) {
            List<ArticleItemBean> syncDatas = params[0];
            boolean success = true;
            if (syncDatas != null) {
                success = ArticleItemDataCenter.getInstance().saveOrUpdateDatas(syncDatas);
            }
            LogUtils.d(TAG, "save remote data in database: " + success);
            return success;
        }
    }

    /**
     * load data from remote server
     */
    private class RemoteDataReadTask extends MyAsyncTask<String, Void, List<ArticleItemBean>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<ArticleItemBean> doInBackground(String... params) {
            String dataStatus = params[0];
            ArticleItemBean articleItemBean = null;
            if (dataStatus.equals(ServerParam.VALUES.DATA_STATUS_NEWER)) {
                articleItemBean = mAdapter.getFirstItemBean();
                if (articleItemBean == null) {
                    articleItemBean = ArticleItemDataCenter.getInstance().getNewestLocalItemData(mPageType);
                }
            } else {
                articleItemBean = mAdapter.getLastItemBean();
            }
            return ArticleItemDataCenter.getInstance().getRemoteItemDatas(articleItemBean, mPageType, HealthConfig.ARTICLE_ITEM_ONCE_LOAD_NUM, dataStatus);
        }

        @Override
        protected void onPostExecute(List<ArticleItemBean> itemBeans) {
            mRefreshListView.onRefreshComplete();
            mRemoteTaskCount.getAndDecrement();
            if (itemBeans != null && itemBeans.size() > 0) {
                mAdapter.addAll(itemBeans);
                mAdapter.notifyDataSetChanged();
                new LocalDataWriteTask().executeOnDatabase(itemBeans);
            }
        }
    }
}
