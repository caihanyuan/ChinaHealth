package com.chinahelth.ui.homepages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.chinahelth.R;
import com.chinahelth.support.bean.ArticleItemBean;
import com.chinahelth.support.bean.ServerParam;
import com.chinahelth.support.lib.MyAsyncTask;
import com.chinahelth.support.utils.LogUtils;
import com.chinahelth.support.utils.Utility;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by caihanyuan on 15-8-22.
 */
public class HomepageContentFragment extends Fragment implements PullToRefreshBase.OnRefreshListener, PullToRefreshBase.OnLastItemVisibleListener {

    private final static String TAG = HomepageContentFragment.class.getSimpleName();

    public int mPageType = -1;

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
        mRefreshListView.setOnRefreshListener(this);
        mRefreshListView.setOnLastItemVisibleListener(this);
        mRefreshListView.setOnScrollListener(mPauseOnScrollListener);
        mRefreshListView.setAdapter(mAdapter);
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

    protected void initView() {
    }

    protected void initData() {
        if (!mDataInit) {
            LogUtils.d(TAG, "view " + mPageType + " initData");
            if (Utility.isNetConnected(getActivity())) {
                //execute remote data access task
                if (!isRemoteAccessTaskBusy()) {
                    executeRemoteDataReadTask(ServerParam.VALUES.DATA_STATUS_NEWER);
                }
            } else {
                //let users know that net work isn't connected
                Toast.makeText(getActivity(), R.string.internet_disconnect_error, Toast.LENGTH_LONG).show();
            }
            if (!isLocalAccessTaskBusy()) {
                executeLocalDataReadTask();
            }
            mDataInit = true;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        if (Utility.isNetConnected(getActivity())) {
            //execute remote data access task
            if (!isRemoteAccessTaskBusy()) {
                executeRemoteDataReadTask(ServerParam.VALUES.DATA_STATUS_NEWER);
            }
        } else {
            //let users know that net work isn't connected
            Toast.makeText(getActivity(), R.string.internet_disconnect_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLastItemVisible() {
        if (mAdapter.hasMoreLocalData()) {
            if (!isLocalAccessTaskBusy()) {
                LogUtils.d(TAG, "get older data from database, when scroll to last item");
                executeLocalDataReadTask();
            }
        } else {
            //load data from remote server
            if (!isRemoteAccessTaskBusy()) {
                LogUtils.d(TAG, "get older data from remote server, when scroll to last item");
                mRefreshListView.setRefreshing();
                executeRemoteDataReadTask(ServerParam.VALUES.DATA_STATUS_OLDER);
            }
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

    private void executeLocalDataReadTask() {
        mLocalTaskCount.getAndIncrement();
        new LocalDataReadTask().executeOnDatabase();
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
    private class LocalDataReadTask extends MyAsyncTask<Void, Void, List<ArticleItemBean>> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<ArticleItemBean> doInBackground(Void... params) {
            return mAdapter.getLocalData();
        }

        @Override
        protected void onPostExecute(List<ArticleItemBean> itemBeans) {
            mLocalTaskCount.getAndDecrement();
            if (itemBeans.size() > 0) {
                mAdapter.notifyDataSetChanged();
            }
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
            if (dataStatus == ServerParam.VALUES.DATA_STATUS_NEWER || dataStatus == ServerParam.VALUES.DATA_STATUS_OLDER)
                return mAdapter.getRemoteData(dataStatus);
            else {
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ArticleItemBean> itemBeans) {
            mRefreshListView.onRefreshComplete();
            mRemoteTaskCount.getAndDecrement();
            if (itemBeans != null && itemBeans.size() > 0) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}