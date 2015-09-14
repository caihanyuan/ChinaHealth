package com.chinahelth.ui.homepages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chinahelth.support.bean.ArticleItemBean;
import com.chinahelth.support.bean.ArticleItemType;
import com.chinahelth.support.database.ArticleItemLocalData;
import com.chinahelth.support.remoteserver.ArticleItemRemoteData;
import com.chinahelth.support.utils.LogUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by caihanyuan on 15-8-8.
 */
public class HomePageContentAdapter extends BaseAdapter {

    private final static String TAG = HomePageContentAdapter.class.getSimpleName();

    private Context mContext;

    private int mPageType;

    private LayoutInflater mLayoutInflater;

    private ArticleItemLocalData mLocalData;

    private ArticleItemRemoteData mRemtoeData;

    private LinkedList<ArticleItemBean> mItemsDataList = new LinkedList();

    public HomePageContentAdapter(Context context, int pageType) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mPageType = pageType;
        mLocalData = new ArticleItemLocalData(mPageType);
        mRemtoeData = new ArticleItemRemoteData(mPageType);
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
        ArticleItemBean articleItemBean = mItemsDataList.get(position);
        HomepageBaseItem pageItem = null;
        if (convertView == null) {
            pageItem = createItemByType(articleItemBean);
            convertView = pageItem.getItemView();
            convertView.setTag(pageItem);
            pageItem.setHomepageItemData(articleItemBean);
        } else {
            pageItem = (HomepageBaseItem) convertView.getTag();
            if (pageItem.getClass() != getItemClassType(articleItemBean)) {
                LogUtils.d(TAG, "reCreate HompageBaseItem because item type different");
                pageItem = createItemByType(articleItemBean);
                convertView = pageItem.getItemView();
                convertView.setTag(pageItem);
            }
            pageItem.setHomepageItemData(articleItemBean);
        }
        return convertView;
    }

    public List<ArticleItemBean> getLocalData() {
        ArticleItemBean lastItemBean = getLastItemBean();
        List<ArticleItemBean> articleItemBeans = mLocalData.getItemDatas(lastItemBean);
        mItemsDataList.addAll(articleItemBeans);
        return articleItemBeans;
    }

    public List<ArticleItemBean> getRemoteData(String dataStatus) {
        ArticleItemBean lastItemBean = getFirstItemBean();
        List<ArticleItemBean> articleItemBeans = mRemtoeData.getItemDatas(lastItemBean, dataStatus);
        mItemsDataList.addAll(0, articleItemBeans);
        return articleItemBeans;
    }

    public boolean hasMoreLocalData() {
        ArticleItemBean lastItemBean = getLastItemBean();
        return mLocalData.hasMoreItemData(lastItemBean);
    }

    private ArticleItemBean getLastItemBean() {
        ArticleItemBean articleItemBean = null;
        articleItemBean = mItemsDataList.size() == 0 ? articleItemBean : mItemsDataList.getLast();
        return articleItemBean;
    }

    private ArticleItemBean getFirstItemBean() {
        ArticleItemBean articleItemBean = null;
        articleItemBean = mItemsDataList.size() == 0 ? articleItemBean : mItemsDataList.getFirst();
        return articleItemBean;
    }

    private HomepageBaseItem createItemByType(ArticleItemBean articleItemBean) {
        HomepageBaseItem item = null;
        switch (articleItemBean.itemType) {
            case ArticleItemType.NORMAL_TEXT:
            case ArticleItemType.NORMAL_VIDEO:
                item = new HomePageNormalItem(mContext);
                break;
            case ArticleItemType.PROMOTION:
                item = new HomepageBigpicItem(mContext);
                break;
            case ArticleItemType.GALLERY:
                item = new HomePageGalleryItem(mContext);
                break;
            default:
                LogUtils.e(TAG, "create item error, item type: " + articleItemBean.itemType + " not found");
                break;
        }
        return item;
    }

    private Class getItemClassType(ArticleItemBean articleItemBean) {
        Class itemClass = null;

        switch (articleItemBean.itemType) {
            case ArticleItemType.NORMAL_TEXT:
            case ArticleItemType.NORMAL_VIDEO:
                itemClass = HomePageNormalItem.class;
                break;
            case ArticleItemType.PROMOTION:
                itemClass = HomepageBigpicItem.class;
                break;
            case ArticleItemType.GALLERY:
                itemClass = HomePageGalleryItem.class;
                break;
        }
        return itemClass;
    }
}
