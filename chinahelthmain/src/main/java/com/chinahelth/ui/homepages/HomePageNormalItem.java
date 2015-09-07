package com.chinahelth.ui.homepages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinahelth.R;
import com.chinahelth.support.bean.ArticleItemBean;
import com.chinahelth.support.bean.ArticleItemType;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by caihanyuan on 15-7-20.
 */
public class HomePageNormalItem extends HomepageBaseItem {

    private ImageView mItemImageView;

    private ImageView mVideoPlayIcon;

    public HomePageNormalItem(Context context) {
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
        mItemImageView = (ImageView) mItemRoot.findViewById(R.id.homepage_normalitem_imageview);
        mVideoPlayIcon = (ImageView) mItemRoot.findViewById(R.id.homepage_normalitem_videoplay_img);
    }

    @Override
    protected void setHomepageItemData(ArticleItemBean homepageItemBean) {
        super.setHomepageItemData(homepageItemBean);
        if (homepageItemBean.thumbnailUris.length > 0) {
            String imgeUri = homepageItemBean.thumbnailUris[0];
            ImageLoader.getInstance().displayImage(imgeUri, mItemImageView, mDisplayImageOptions, this);
        }
        if (homepageItemBean.itemType == ArticleItemType.NORMAL_VIDEO) {
            mVideoPlayIcon.setVisibility(View.VISIBLE);
        } else {
            mVideoPlayIcon.setVisibility(View.GONE);
        }
    }
}
