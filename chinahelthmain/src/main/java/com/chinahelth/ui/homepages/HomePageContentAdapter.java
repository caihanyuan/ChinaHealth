package com.chinahelth.ui.homepages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chinahelth.support.bean.ArticleItemBean;

import java.util.LinkedList;

/**
 * Created by caihanyuan on 15-8-8.
 */
public class HomePageContentAdapter extends BaseAdapter{

    private Context mContext;

    private String mPageType;

    private LayoutInflater mLayoutInflater;

    private LinkedList<ArticleItemBean> mItemsDataList = new LinkedList();

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
}
