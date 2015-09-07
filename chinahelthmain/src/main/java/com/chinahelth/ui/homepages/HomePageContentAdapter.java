package com.chinahelth.ui.homepages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chinahelth.support.bean.ArticleItemBean;
import com.chinahelth.support.database.ArticleItemLocalData;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by caihanyuan on 15-8-8.
 */
public class HomePageContentAdapter extends BaseAdapter {

    private Context mContext;

    private int mPageType;

    private LayoutInflater mLayoutInflater;

    private ArticleItemLocalData mLocalData;

    private LinkedList<ArticleItemBean> mItemsDataList = new LinkedList();

    public HomePageContentAdapter(Context context, int pageType) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mPageType = pageType;
        mLocalData = new ArticleItemLocalData(mPageType);
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

    public int getLocalData(int offset) {
        List<ArticleItemBean> articleItemBeans = mLocalData.getAriticleItems(offset);
        mItemsDataList.addAll(articleItemBeans);
        return offset + articleItemBeans.size();
    }
}
