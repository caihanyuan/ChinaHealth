package com.chinahelth.support.datacenter;

import com.chinahelth.HealthConfig;
import com.chinahelth.support.bean.ArticleItemBean;
import com.chinahelth.support.bean.ServerParam;
import com.chinahelth.support.datacenter.table.ArticleItemTable;
import com.chinahelth.support.http.HttpMethod;
import com.chinahelth.support.http.HttpUtility;
import com.chinahelth.support.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caihanyuan on 15-9-14.
 */
public class ArticleItemRemoteDao {

    private final static String TAG = ArticleItemRemoteDao.class.getSimpleName();
    private int mGroupType = 0;

    public ArticleItemRemoteDao(int groupType) {
        mGroupType = groupType;
    }

    public List<ArticleItemBean> getItemDatas(ArticleItemBean itemBean, String dataStutus) {
        Map<String, String> params = new HashMap();
        params.put(ServerParam.DATA_STATUS, dataStutus);
        JSONObject paramJson = new JSONObject();
        try {
            paramJson.put(ArticleItemTable.GROUP_TYPE, mGroupType);
            if (itemBean != null) {
                paramJson.put(ArticleItemTable.UID, itemBean.articleId);
                paramJson.put(ArticleItemTable.PUBLISH_TIME, itemBean.publishTime);
            }
            params.put(ServerParam.REQUEST_JSON, paramJson.toString());
        } catch (JSONException e) {
            LogUtils.e(TAG, "json object create error", e);
        }

        List<ArticleItemBean> itemBeans = new ArrayList();
        String url = HealthConfig.SERVER_HOST_ROOT_URL + ServerParam.ARTICLE_ITEM_URL;
        String itemDatasJson = HttpUtility.getInstance().executeNormalTask(HttpMethod.Post, url, params);
        LogUtils.d(TAG, "item datas from server: " + itemDatasJson);
        itemBeans = parserJsonResult(itemDatasJson);
        return itemBeans;
    }

    private List<ArticleItemBean> parserJsonResult(String jsonResult) {
        if(jsonResult == null || jsonResult.equals(""))
            return null;
        List<ArticleItemBean> itemBeans = new ArrayList();
        try {
            JSONArray jsonArray = new JSONArray(jsonResult);
            int arrLen = jsonArray.length();
            for (int i = 0; i < arrLen; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ArticleItemBean articleItemBean = new ArticleItemBean();
                articleItemBean.articleId = jsonObject.getString(ArticleItemTable.UID);
                articleItemBean.groupType = jsonObject.getInt(ArticleItemTable.GROUP_TYPE);
                articleItemBean.itemType = jsonObject.getInt(ArticleItemTable.TYPE);
                articleItemBean.title = jsonObject.getString(ArticleItemTable.TITLE);
                articleItemBean.from = jsonObject.getString(ArticleItemTable.SOURCE);
                articleItemBean.commentNums = jsonObject.getInt(ArticleItemTable.COMMENT_NUMS);
                articleItemBean.publishTime = jsonObject.getLong(ArticleItemTable.PUBLISH_TIME);
                JSONArray thumbnailArr = jsonObject.getJSONArray(ArticleItemTable.THUMBNAIL_URIS);
                int thumbnailArrLen = thumbnailArr.length();
                String[] thumbnailUrls = new String[thumbnailArrLen];
                for (int j = 0; j < thumbnailArrLen; j++) {
                    thumbnailUrls[j] = thumbnailArr.getString(j);
                }
                articleItemBean.thumbnailUris = thumbnailUrls;
                itemBeans.add(articleItemBean);
            }
        } catch (JSONException e) {
            LogUtils.e(TAG, "parser remote json result error", e);
        }
        return itemBeans;
    }
}
