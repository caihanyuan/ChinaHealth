package com.chinahelth.ui.homepages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinahelth.R;
import com.chinahelth.support.bean.HomepageItemBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by caihanyuan on 15-8-8.
 */
public class HomePageGalleryItem extends HomepageBaseItem {

    private ArrayList<ImageView> mImageViews = new ArrayList();

    private ViewGroup mImagesContainer;

    public HomePageGalleryItem(Context context) {
        super(context);
    }

    @Override
    void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
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
    protected void setHomepageItemData(HomepageItemBean homepageItemBean) {
        super.setHomepageItemData(homepageItemBean);
        if(homepageItemBean.imgUris.length > 0){
            int length = Math.min(homepageItemBean.imgUris.length, mImageViews.size());
            ImageView imageView = null;
            for(int i = 0; i < mImageViews.size(); i++){
                imageView = mImageViews.get(i);
                if(i < length){
                    String url = homepageItemBean.imgUris[i];
                    imageView.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(url, imageView, mDisplayImageOptions, this);
                }else{
                    imageView.setVisibility(View.GONE);
                }
            }
        }
    }
}
