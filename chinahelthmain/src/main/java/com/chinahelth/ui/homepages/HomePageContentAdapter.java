package com.chinahelth.ui.homepages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chinahelth.support.bean.HomepageItemBean;
import com.chinahelth.ui.homepages.base.RemoteSyncInterface;

import java.util.LinkedList;

/**
 * Created by caihanyuan on 15-8-8.
 */
public class HomePageContentAdapter extends BaseAdapter implements RemoteSyncInterface {

    private Context mContext;

    private String mPageType;

    private LayoutInflater mLayoutInflater;

    private LinkedList<HomepageItemBean> mItemsDataList = new LinkedList();

    public HomePageContentAdapter(Context context, String pageType) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mPageType = pageType;
    }

    @Override
    public int getCount() {
        return mItemsDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemsDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
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
