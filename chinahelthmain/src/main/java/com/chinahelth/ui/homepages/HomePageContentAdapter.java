package com.chinahelth.ui.homepages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chinahelth.support.bean.ArticleItemBean;
import com.chinahelth.support.bean.ArticleItemType;
import com.chinahelth.support.bean.ServerParam;
import com.chinahelth.support.datacenter.ArticleItemLocalDao;
import com.chinahelth.support.datacenter.ArticleItemRemoteDao;
import com.chinahelth.support.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by caihanyuan on 15-8-8.
 */
public class HomePageContentAdapter extends BaseAdapter {

    private final static String TAG = HomePageContentAdapter.class.getSimpleName();

    private WeakReference<Context> mContext;

    private int mPageType;

    private LayoutInflater mLayoutInflater;

    private ArticleItemLocalDao mLocalData;

    private ArticleItemRemoteDao mRemoteData;

    private List<ArticleItemBean> mItemsDataList = new ArrayList<>();

    private Map<ArticleItemBean.ItemKey, ArticleItemBean> mItemsDataMap = Collections.synchronizedMap(new TreeMap<ArticleItemBean.ItemKey, ArticleItemBean>());

    public HomePageContentAdapter(Context context, int pageType) {
        mContext = new WeakReference<Context>(context);
        mLayoutInflater = LayoutInflater.from(mContext.get());
        mPageType = pageType;
        mLocalData = new ArticleItemLocalDao(mPageType);
        mRemoteData = new ArticleItemRemoteDao(mPageType);
    }

    public void destory() {
        mItemsDataList.clear();
        mItemsDataMap.clear();
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
        return articleItemBeans;
    }

    public List<ArticleItemBean> getRemoteData(String dataStatus) {
        ArticleItemBean itemBean = null;
        if (dataStatus.equals(ServerParam.VALUES.DATA_STATUS_NEWER)) {
            itemBean = getFirstItemBean();
        } else {
            itemBean = getLastItemBean();
        }
        List<ArticleItemBean> articleItemBeans = mRemoteData.getItemDatas(itemBean, dataStatus);
        return articleItemBeans;
    }

    public boolean hasMoreLocalData() {
        ArticleItemBean lastItemBean = getLastItemBean();
        return mLocalData.hasMoreItemData(lastItemBean);
    }

    void addAll(List<ArticleItemBean> articleItemBeans) {
        for (ArticleItemBean itemBean : articleItemBeans) {
            mItemsDataMap.put(itemBean.getKey(), itemBean);
        }
        mItemsDataList = new ArrayList<>(mItemsDataMap.values());
    }

    private ArticleItemBean getLastItemBean() {
        ArticleItemBean articleItemBean = null;
        articleItemBean = mItemsDataList.size() == 0 ? articleItemBean : mItemsDataList.get(0);
        return articleItemBean;
    }

    private ArticleItemBean getFirstItemBean() {
        ArticleItemBean articleItemBean = null;
        articleItemBean = mItemsDataList.size() == 0 ? articleItemBean : mItemsDataList.get(mItemsDataList.size() - 1);
        return articleItemBean;
    }

    private HomepageBaseItem createItemByType(ArticleItemBean articleItemBean) {
        HomepageBaseItem item = null;
        switch (articleItemBean.itemType) {
            case ArticleItemType.NORMAL_TEXT:
            case ArticleItemType.NORMAL_VIDEO:
                item = new HomePageNormalItem(mContext.get());
                break;
            case ArticleItemType.PROMOTION:
                item = new HomepageBigpicItem(mContext.get());
                break;
            case ArticleItemType.GALLERY:
                item = new HomePageGalleryItem(mContext.get());
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
