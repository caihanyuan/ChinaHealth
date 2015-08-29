package com.chinahelth.ui.homepages;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinahelth.R;
import com.chinahelth.support.bean.HomepageItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by caihanyuan on 15-8-22.
 */
public class HomepageBigpicItem extends HomepageBaseItem {

    private ImageView mItemImageView;

    public HomepageBigpicItem(Context context) {
        super(context);
    }

    @Override
    void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        mItemRoot = layoutInflater.inflate(R.layout.homepages_item_normal, null, false);
        mTitile = (TextView) mItemRoot.findViewById(R.id.homepage_item_title_text);
        mFromText = (TextView) mItemRoot.findViewById(R.id.homepage_item_from_text);
        mCommentText = (TextView) mItemRoot.findViewById(R.id.homepage_item_comment_text);
        mPublishTimeText = (TextView) mItemRoot.findViewById(R.id.homepage_item_time_text);
        mItemImageView = (ImageView) mItemRoot.findViewById(R.id.homepage_bigpicitem_imageview);
    }

    @Override
    protected void setHomepageItemData(HomepageItemBean homepageItemBean) {
        super.setHomepageItemData(homepageItemBean);
        if(homepageItemBean.imgUris.length > 0){
            String imgeUri = homepageItemBean.imgUris[0];
            ImageLoader.getInstance().displayImage(imgeUri, mItemImageView, mDisplayImageOptions, this);
        }
    }
}
