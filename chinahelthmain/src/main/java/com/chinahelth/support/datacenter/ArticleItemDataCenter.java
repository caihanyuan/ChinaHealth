package com.chinahelth.support.datacenter;

import com.chinahelth.HealthConfig;
import com.chinahelth.support.bean.ArticleItemBean;
import com.chinahelth.support.bean.ServerParam;

import java.util.List;

/**
 * Created by caihanyuan on 15-9-17.
 */
public class ArticleItemDataCenter {

    private static ArticleItemDataCenter mInstace = null;

    private ArticleItemLocalDao mArticleItemLocalDao;

    private ArticleItemRemoteDao mArticleItemRemoteDao;

    private ArticleItemDataCenter() {
        mArticleItemLocalDao = new ArticleItemLocalDao();
        mArticleItemRemoteDao = new ArticleItemRemoteDao();
    }

    public static ArticleItemDataCenter getInstance() {
        if (mInstace == null) {
            mInstace = new ArticleItemDataCenter();
        }
        return mInstace;
    }

    /**
     * get article item data from database which publish time older than oldest item in memory
     *
     * @param itemBean  oldest item's data
     * @param groupType
     * @return
     */
    public List<ArticleItemBean> getOlderLocalItemDatas(ArticleItemBean itemBean, int groupType) {
        return getLocalItemDatas(itemBean, groupType, HealthConfig.ARTICLE_ITEM_ONCE_LOAD_NUM, ServerParam.VALUES.DATA_STATUS_OLDER);
    }

    public ArticleItemBean getNewestLocalItemData(int groupType) {
        ArticleItemBean itemBean = null;
        List<ArticleItemBean> itemDatas = getLocalItemDatas(null, groupType, 1, ServerParam.VALUES.DATA_STATUS_NEWER);
        itemBean = itemDatas.size() != 0 ? itemDatas.get(0) : itemBean;
        return itemBean;
    }

    public List<ArticleItemBean> getOlderRemoteItemDatas(ArticleItemBean itemBean, int groupType) {
        return getRemoteItemDatas(itemBean, groupType, HealthConfig.ARTICLE_ITEM_ONCE_LOAD_NUM, ServerParam.VALUES.DATA_STATUS_OLDER);
    }

    public List<ArticleItemBean> getNewerRemoteItemDatas(ArticleItemBean itemBean, int groupType) {
        return getRemoteItemDatas(itemBean, groupType, HealthConfig.ARTICLE_ITEM_ONCE_LOAD_NUM, ServerParam.VALUES.DATA_STATUS_NEWER);
    }

    /**
     * get article item data from database which publish time older or newer than last item in memory
     *
     * @param itemBean    oldest or newest item's data
     * @param groupType
     * @param onceLoadNum
     * @param dataStatus
     * @return
     */
    public List<ArticleItemBean> getLocalItemDatas(ArticleItemBean itemBean, int groupType, int onceLoadNum, String dataStatus) {
        List<ArticleItemBean> itemBeans = mArticleItemLocalDao.getItemDatas(itemBean, groupType, onceLoadNum, dataStatus);
        return itemBeans;
    }

    public List<ArticleItemBean> getRemoteItemDatas(ArticleItemBean itemBean, int groupType, int onceLoadNum, String dataStatus) {
        List<ArticleItemBean> itemBeans = mArticleItemRemoteDao.getItemDatas(itemBean, groupType, onceLoadNum, dataStatus);
        return itemBeans;
    }

    public boolean hasMoreOlderLocalData(ArticleItemBean itemBean, int groupType) {
        return hasMoreLocalData(itemBean, groupType, HealthConfig.ARTICLE_ITEM_ONCE_LOAD_NUM, ServerParam.VALUES.DATA_STATUS_OLDER);
    }

    public boolean hasMoreNewerLocalData(ArticleItemBean itemBean, int groupType) {
        return hasMoreLocalData(itemBean, groupType, HealthConfig.ARTICLE_ITEM_ONCE_LOAD_NUM, ServerParam.VALUES.DATA_STATUS_NEWER);
    }

    public boolean hasMoreLocalData(ArticleItemBean itemBean, int groupType, int onceLoadNum, String dataStatus) {
        boolean hasMore = mArticleItemLocalDao.hasMoreItemData(itemBean, groupType, onceLoadNum, dataStatus);
        return hasMore;
    }

    public boolean saveOrUpdateDatas(List<ArticleItemBean> itemDatas) {
        boolean success = mArticleItemLocalDao.saveOrUpdateItemDatas(itemDatas);
        return success;
    }
}
