package com.chinahelth.ui.homepush;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinahelth.HealthConfig;
import com.chinahelth.R;
import com.chinahelth.support.bean.HomePushItemBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by caihanyuan on 7/2/15.
 */
public class HomePushItem extends FrameLayout implements ImageLoadingListener, View.OnClickListener {
    private final static String TAG = HomePushItem.class.getName();

    private ViewHolder viewHolder;

    private HomePushItemBean itemData;

    private DisplayImageOptions displayImageOptions;

    public HomePushItem(Context context) {
        super(context);
        initView(context);
    }

    public HomePushItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomePushItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public void onClick(View v) {
        if (HealthConfig.isDebug) {
            Log.d(TAG, "pushviewItem: " + itemData.toString() + " clicked");
        }
    }

    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        if (HealthConfig.isDebug) {
            Log.d(ImageLoader.TAG, "image:" + imageUri + " load failed");
        }
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        if (HealthConfig.isDebug) {
            Log.d(ImageLoader.TAG, "image:" + imageUri + " loading cancelled");
        }
    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.homepushitem_layout, this, true);
        viewHolder = new ViewHolder();
        viewHolder.backgroundImageView = (ImageView) findViewById(R.id.homepush_item_backImg);
        viewHolder.titleTextView = (TextView) findViewById(R.id.homepush_item_title);
        displayImageOptions = HealthConfig.getDefaultDisplayImageOptions();
        setOnClickListener(this);
    }

    void setItemData(HomePushItemBean itemData) {
        this.itemData = itemData;
        viewHolder.titleTextView.setText(itemData.articleTitle);
        ImageLoader.getInstance().displayImage(itemData.imageUri, viewHolder.backgroundImageView, displayImageOptions, this);
    }

    HomePushItemBean getItemData() {
        return itemData;
    }

    private class ViewHolder {
        ImageView backgroundImageView;

        TextView titleTextView;
    }
}
