package com.chinahelth.support.datacenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chinahelth.support.bean.ArticleItemBean;
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

    private int mGroupType = 0;

    private int mOnceLoadCount = 10;

    public ArticleItemLocalDao() {
    }

    public ArticleItemLocalDao(int groupType) {
        mGroupType = groupType;
    }

    public void setOnceLoadCount(int onceLoadCount) {
        mOnceLoadCount = onceLoadCount;
    }

    /**
     * get data's in database that pusblis_time before last item in the list view
     *
     * @param lastItemBean
     * @return
     */
    public List<ArticleItemBean> getItemDatas(ArticleItemBean lastItemBean) {
        String selectSQL = createTimeBeforeSelectSQL(lastItemBean);
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

    /**
     * if there's more items in database that publish_time before last item in the list view
     *
     * @param lastItemBean
     * @return
     */
    public boolean hasMoreItemData(ArticleItemBean lastItemBean) {
        String selectSQL = createTimeBeforeCountSelectSQL(lastItemBean);
        Cursor cursor = getRead().rawQuery(selectSQL, null);
        cursor.moveToFirst();
        int itemCount = cursor.getInt(0);
        return itemCount != 0;
    }

    private String createTimeBeforeSelectSQL(ArticleItemBean lastItemBean) {
        int offset = 0;
        String selectSQL = "";
        String whereCondition = "";
        if (lastItemBean != null) {
            whereCondition = ArticleItemTable.PUBLISH_TIME + "<=" + lastItemBean.publishTime + " AND " + ArticleItemTable.UID + "<>'" + lastItemBean.articleId + "' ";
        }
        if (mGroupType == 0) {
            whereCondition = whereCondition == "" ? whereCondition : ("WHERE " + whereCondition);
            selectSQL = "SELECT * FROM " + ArticleItemTable.TABLE_NAME + " " +
                    whereCondition +
                    "ORDER BY " + ArticleItemTable.PUBLISH_TIME + " DESC " +
                    "LIMIT " + mOnceLoadCount + " OFFSET " + offset;
        } else {
            whereCondition = whereCondition == "" ? ("WHERE " + ArticleItemTable.GROUP_TYPE + "=" + mGroupType + " ") : ("WHERE " + ArticleItemTable.GROUP_TYPE + "=" + mGroupType + " AND " + whereCondition);
            selectSQL = "SELECT * FROM " + ArticleItemTable.TABLE_NAME + " " +
                    whereCondition +
                    "ORDER BY " + ArticleItemTable.PUBLISH_TIME + " DESC " +
                    "LIMIT " + mOnceLoadCount + " OFFSET " + offset;
        }
        return selectSQL;
    }

    private String createTimeBeforeCountSelectSQL(ArticleItemBean lastItemBean) {
        String selectSQL = "";
        String whereCondition = "";
        if (lastItemBean != null) {
            whereCondition = ArticleItemTable.PUBLISH_TIME + "<=" + lastItemBean.publishTime + " AND " + ArticleItemTable.UID + "<>'" + lastItemBean.articleId + "' ";
        }
        if (mGroupType == 0) {
            whereCondition = whereCondition == "" ? whereCondition : ("WHERE " + whereCondition);
            selectSQL = "SELECT COUNT(1) FROM " + ArticleItemTable.TABLE_NAME + " " + whereCondition;
        } else {
            whereCondition = whereCondition == "" ? ("WHERE " + ArticleItemTable.GROUP_TYPE + "=" + mGroupType + " ") : ("WHERE " + ArticleItemTable.GROUP_TYPE + "=" + mGroupType + " AND " + whereCondition);
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
