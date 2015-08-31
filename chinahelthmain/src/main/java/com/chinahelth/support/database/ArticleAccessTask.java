package com.chinahelth.support.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chinahelth.support.bean.ArticleItemBean;
import com.chinahelth.support.database.table.ArticleItemTable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caihanyuan on 15-8-31.
 */
public class ArticleAccessTask {

    private int mOnceLoadCount = 20;

    public ArticleAccessTask() {
    }

    public ArticleAccessTask(int mOnceLoadCount) {
        this.mOnceLoadCount = mOnceLoadCount;
    }

    public void setLoadCount(int onceLoadCount) {
        mOnceLoadCount = onceLoadCount;
    }

    public List<ArticleItemBean> loadAriticleItems(int offset) {
        String selectSQL = "SELECT * FROM " + ArticleItemTable.TABLE_NAME + " " +
                "ORDER BY " + ArticleItemTable.PUBLISH_TIME + " DESC " +
                "LIMIT " + mOnceLoadCount + " OFFSET " + offset;
        List<ArticleItemBean> articleItemBeanList = new ArrayList();
        Cursor cursor = getRead().rawQuery(selectSQL, null);
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex(ArticleItemTable.UID);
            int typeIndex = cursor.getColumnIndex(ArticleItemTable.TYPE);
            int titleIndex = cursor.getColumnIndex(ArticleItemTable.TITLE);
            int fromIndex = cursor.getColumnIndex(ArticleItemTable.FROM);
            int commentNumIndex = cursor.getColumnIndex(ArticleItemTable.COMMENT_NUMS);
            int publishTimeIndex = cursor.getColumnIndex(ArticleItemTable.PUBLISH_TIME);
            int thumbnailIndex = cursor.getColumnIndex(ArticleItemTable.THUMBNAIL_URIS);
            int isReadedIndex = cursor.getColumnIndex(ArticleItemTable.IS_READED);

            ArticleItemBean itemBean = new ArticleItemBean();
            itemBean.artileId = cursor.getString(idIndex);
            itemBean.itemType = cursor.getInt(typeIndex);
            itemBean.title = cursor.getString(titleIndex);
            itemBean.from = cursor.getString(fromIndex);
            itemBean.commentNums = cursor.getInt(commentNumIndex);
            itemBean.publishTime = cursor.getLong(publishTimeIndex);
            itemBean.isReaded = cursor.getInt(isReadedIndex) == 0 ? false : true;
            String uriText = cursor.getString(thumbnailIndex);
            try {
                JSONArray jsonArray = new JSONArray(uriText);
                int length = jsonArray.length();
                String[] thumbnailUris = new String[length];
                for(int i = 0; i < length; i++){
                    thumbnailUris[i] = jsonArray.getString(i);
                }
                itemBean.thumbnailUris = thumbnailUris;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            articleItemBeanList.add(itemBean);
        }
        cursor.close();
        return articleItemBeanList;
    }

    private SQLiteDatabase getRead() {
        return DatabaseHelper.getInstance().getReadableDatabase();
    }

    private SQLiteDatabase getWrite() {
        return DatabaseHelper.getInstance().getWritableDatabase();
    }
}
