package com.chinahelth.support.bean;

import com.chinahelth.support.utils.TimeUtility;

import java.io.Serializable;

/**
 * Created by caihanyuan on 7/2/15.
 */
public class HomePushItemBean implements Serializable {

    public String articleId;

    public String imageUri;

    public String articleTitle;

    public long publishTime;

    @Override
    public String toString() {
        String timeStamp = TimeUtility.getTimestamp(publishTime);
        return "articleId:" + articleId + " imageUri:" + imageUri + " articleTitle:" + articleId + " publishTime:" + timeStamp;
    }
}
