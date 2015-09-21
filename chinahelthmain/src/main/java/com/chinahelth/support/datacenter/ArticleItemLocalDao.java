package com.chinahelth.support.datacenter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chinahelth.support.bean.ArticleItemBean;
import com.chinahelth.support.bean.ServerParam;
import com.chinahelth.support.datacenter.table.ArticleItemTable;
import com.chinahelth.support.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caihanyuan on 15-8-31.
 */
public class ArticleItemLocalDao {

    private final static String TAG = ArticleItemLocalDao.class.getSimpleName();

    /**
     * get data's in database that base on pushtime
     *
     * @param lastItemBean
     * @return
     */
    List<ArticleItemBean> getItemDatas(ArticleItemBean lastItemBean, int groupType, int oneLoadNum, String dataStatus) {
        String selectSQL = createSelectSQL(lastItemBean, groupType, oneLoadNum, dataStatus);
        List<ArticleItemBean> articleItemBeanList = new ArrayList();
        Cursor cursor = getRead().rawQuery(selectSQL, null);
        int idIndex = cursor.getColumnIndex(ArticleItemTable.UID);
        int groupTypeIndex = cursor.getColumnIndex(ArticleItemTable.GROUP_TYPE);
        int typeIndex = cursor.getColumnIndex(ArticleItemTable.TYPE);
        int titleIndex = cursor.getColumnIndex(ArticleItemTable.TITLE);
        int fromIndex = cursor.getColumnIndex(ArticleItemTable.SOURCE);
        int commentNumIndex = cursor.getColumnIndex(ArticleItemTable.COMMENT_NUMS);
        int publishTimeIndex = cursor.getColumnIndex(ArticleItemTable.PUBLISH_TIME);
        int thumbnailIndex = cursor.getColumnIndex(ArticleItemTable.THUMBNAIL_URIS);
        int isReadedIndex = cursor.getColumnIndex(ArticleItemTable.IS_READED);
        while (cursor.moveToNext()) {
            ArticleItemBean itemBean = new ArticleItemBean();
            itemBean.articleId = cursor.getString(idIndex);
            itemBean.groupType = cursor.getInt(groupTypeIndex);
            itemBean.itemType = cursor.getInt(typeIndex);
            itemBean.title = cursor.getString(titleIndex);
            itemBean.from = cursor.getString(fromIndex);
            itemBean.commentNums = cursor.getInt(commentNumIndex);
            itemBean.publishTime = cursor.getLong(publishTimeIndex);
            itemBean.isReaded = cursor.getInt(isReadedIndex) == 0 ? false : true;
            String uriText = cursor.getString(thumbnailIndex);
            try {
                if (uriText != null) {
                    JSONArray jsonArray = new JSONArray(uriText);
                    int length = jsonArray.length();
                    String[] thumbnailUris = new String[length];
                    for (int i = 0; i < length; i++) {
                        thumbnailUris[i] = jsonArray.getString(i);
                    }
                    itemBean.thumbnailUris = thumbnailUris;
                }
            } catch (JSONException e) {
                LogUtils.e(TAG, "json parser error:", e);
            }
            articleItemBeanList.add(itemBean);
        }
        cursor.close();
        return articleItemBeanList;
    }

    ArticleItemBean getNewestData(int groupType) {
        ArticleItemBean itemBean = null;
        List<ArticleItemBean> itemBeanList = getItemDatas(null, groupType, 1, ServerParam.VALUES.DATA_STATUS_NEWER);
        itemBean = itemBeanList.size() != 0 ? itemBeanList.get(0) : itemBean;
        return itemBean;
    }

    ArticleItemBean getOldestData(int groupType) {
        ArticleItemBean itemBean = null;
        List<ArticleItemBean> itemBeanList = getItemDatas(null, groupType, 1, ServerParam.VALUES.DATA_STATUS_OLDER);
        itemBean = itemBeanList.size() != 0 ? itemBeanList.get(0) : itemBean;
        return itemBean;
    }

    /**
     * if there's more items in database that publish_time before last item in the list view
     *
     * @param lastItemBean
     * @return
     */
    boolean hasMoreItemData(ArticleItemBean lastItemBean, int groupType, int onceLoadNum, String dataStatus) {
        String selectSQL = createCountSelectSQL(lastItemBean, groupType, dataStatus);
        Cursor cursor = getRead().rawQuery(selectSQL, null);
        cursor.moveToFirst();
        int itemCount = cursor.getInt(0);
        cursor.close();
        return itemCount >= onceLoadNum;
    }

    boolean saveOrUpdateItemDatas(List<ArticleItemBean> itemDatas) {
        boolean success = true;
        for (ArticleItemBean itemData : itemDatas) {
            if (isItemDataExist(itemData)) {
                ContentValues values = new ContentValues();
                values.put(ArticleItemTable.GROUP_TYPE, itemData.groupType);
                values.put(ArticleItemTable.TYPE, itemData.itemType);
                values.put(ArticleItemTable.TITLE, itemData.title);
                values.put(ArticleItemTable.SOURCE, itemData.from);
                values.put(ArticleItemTable.COMMENT_NUMS, itemData.commentNums);
                values.put(ArticleItemTable.PUBLISH_TIME, itemData.publishTime);
                values.put(ArticleItemTable.THUMBNAIL_URIS, itemData.getThumbnailJson());
                values.put(ArticleItemTable.IS_READED, itemData.isReaded);
                String[] args = {itemData.articleId};
                int affectedNum = getWrite().update(ArticleItemTable.TABLE_NAME, values, ArticleItemTable.UID + "=?", args);
                if (success)
                    success = success && (affectedNum > 0);
            } else {
                ContentValues values = new ContentValues();
                values.put(ArticleItemTable.UID, itemData.articleId);
                values.put(ArticleItemTable.GROUP_TYPE, itemData.groupType);
                values.put(ArticleItemTable.TYPE, itemData.itemType);
                values.put(ArticleItemTable.TITLE, itemData.title);
                values.put(ArticleItemTable.SOURCE, itemData.from);
                values.put(ArticleItemTable.COMMENT_NUMS, itemData.commentNums);
                values.put(ArticleItemTable.PUBLISH_TIME, itemData.publishTime);
                values.put(ArticleItemTable.THUMBNAIL_URIS, itemData.getThumbnailJson());
                values.put(ArticleItemTable.IS_READED, itemData.isReaded);
                long rowId = getWrite().insert(ArticleItemTable.TABLE_NAME, null, values);
                if (success)
                    success = success && (rowId != -1);
            }
        }
        return success;
    }

    private boolean isItemDataExist(ArticleItemBean itemData) {
        boolean exist = false;
        String selectSQL = "SELECT COUNT(1) FROM " + ArticleItemTable.TABLE_NAME + " WHERE " + ArticleItemTable.UID + "='" + itemData.articleId + "'";
        Cursor cursor = getRead().rawQuery(selectSQL, null);
        cursor.moveToFirst();
        int itemCount = cursor.getInt(0);
        cursor.close();
        exist = itemCount != 0;
        return exist;
    }

    private String createSelectSQL(ArticleItemBean lastItemBean, int groupType, int oneLoadNum, String dataStatus) {
        int offset = 0;
        String selectSQL = "";
        String whereCondition = "";
        if (lastItemBean != null) {
            if (dataStatus.equals(ServerParam.VALUES.DATA_STATUS_OLDER)) {
                whereCondition = ArticleItemTable.PUBLISH_TIME + "<=" + lastItemBean.publishTime + " AND " + ArticleItemTable.UID + "<>'" + lastItemBean.articleId + "' ";
            } else {
                whereCondition = ArticleItemTable.PUBLISH_TIME + ">=" + lastItemBean.publishTime + " AND " + ArticleItemTable.UID + "<>'" + lastItemBean.articleId + "' ";
            }
        }
        if (groupType == 0) {
            whereCondition = whereCondition == "" ? whereCondition : ("WHERE " + whereCondition);
            selectSQL = "SELECT * FROM " + ArticleItemTable.TABLE_NAME + " " +
                    whereCondition +
                    "ORDER BY " + ArticleItemTable.PUBLISH_TIME + " DESC " +
                    "LIMIT " + oneLoadNum + " OFFSET " + offset;
        } else {
            whereCondition = whereCondition == "" ? ("WHERE " + ArticleItemTable.GROUP_TYPE + "=" + groupType + " ") : ("WHERE " + ArticleItemTable.GROUP_TYPE + "=" + groupType + " AND " + whereCondition);
            selectSQL = "SELECT * FROM " + ArticleItemTable.TABLE_NAME + " " +
                    whereCondition +
                    "ORDER BY " + ArticleItemTable.PUBLISH_TIME + " DESC " +
                    "LIMIT " + oneLoadNum + " OFFSET " + offset;
        }
        return selectSQL;
    }

    private String createCountSelectSQL(ArticleItemBean lastItemBean, int groupType, String dataStatus) {
        String selectSQL = "";
        String whereCondition = "";
        if (lastItemBean != null) {
            if (dataStatus.equals(ServerParam.VALUES.DATA_STATUS_OLDER)) {
                whereCondition = ArticleItemTable.PUBLISH_TIME + "<=" + lastItemBean.publishTime + " AND " + ArticleItemTable.UID + "<>'" + lastItemBean.articleId + "' ";
            } else {
                whereCondition = ArticleItemTable.PUBLISH_TIME + ">=" + lastItemBean.publishTime + " AND " + ArticleItemTable.UID + "<>'" + lastItemBean.articleId + "' ";
            }
        }
        if (groupType == 0) {
            whereCondition = whereCondition == "" ? whereCondition : ("WHERE " + whereCondition);
            selectSQL = "SELECT COUNT(1) FROM " + ArticleItemTable.TABLE_NAME + " " + whereCondition;
        } else {
            whereCondition = whereCondition == "" ? ("WHERE " + ArticleItemTable.GROUP_TYPE + "=" + groupType + " ") : ("WHERE " + ArticleItemTable.GROUP_TYPE + "=" + groupType + " AND " + whereCondition);
            selectSQL = "SELECT COUNT(1) FROM " + ArticleItemTable.TABLE_NAME + " " + whereCondition;
        }
        return selectSQL;
    }

    private SQLiteDatabase getRead() {
        return DatabaseManager.getInstance().getReadableDatabase();
    }

    private SQLiteDatabase getWrite() {
        return DatabaseManager.getInstance().getWritableDatabase();
    }
}
