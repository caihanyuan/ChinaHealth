package com.chinahelth.ui.homepages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinahelth.HealthConfig;
import com.chinahelth.R;
import com.chinahelth.support.bean.ArticleItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by caihanyuan on 15-8-8.
 */
public class HomePageGalleryItem extends HomepageBaseItem {

    private ArrayList<ImageView> mImageViews;

    private ViewGroup mImagesContainer;

    public HomePageGalleryItem(Context context) {
        super(context);
    }

    @Override
    void initView() {
        mImageViews = new ArrayList();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext.get());
        mItemRoot = layoutInflater.inflate(R.layout.homepages_item_gallery, null, false);
        mTitile = (TextView) mItemRoot.findViewById(R.id.homepage_item_title_text);
        mFromText = (TextView) mItemRoot.findViewById(R.id.homepage_item_from_text);
        mCommentText = (TextView) mItemRoot.findViewById(R.id.homepage_item_comment_text);
        mPublishTimeText = (TextView) mItemRoot.findViewById(R.id.homepage_item_time_text);
        mImagesContainer = (ViewGroup) mItemRoot.findViewById(R.id.homepage_galleryitem_img_container);

        int childCount = mImagesContainer.getChildCount();
        ImageView imageView = null;
        for (int i = 0; i < childCount; i++) {
            imageView = (ImageView) mImagesContainer.getChildAt(i);
            mImageViews.add(imageView);
        }
    }

    @Override
    protected void setHomepageItemData(ArticleItemBean homepageItemBean) {
        super.setHomepageItemData(homepageItemBean);
        if (homepageItemBean.thumbnailUris != null && homepageItemBean.thumbnailUris.length > 0) {
            int length = Math.min(homepageItemBean.thumbnailUris.length, mImageViews.size());
            ImageView imageView = null;
            for (int i = 0; i < mImageViews.size(); i++) {
                imageView = mImageViews.get(i);
                if (i < length) {
                    String url = homepageItemBean.thumbnailUris[i];
                    imageView.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(url, imageView, HealthConfig.getDefaultDisplayImageOptions(), this);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
