package com.chinahelth.ui.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinahelth.R;

/**
 * Created by caihanyuan on 15-7-9.
 */
public class MenuItemBase extends FrameLayout {
    final static String HOME = "HOME";

    final static String ACCOUNT = "ACCOUNT";

    final static String NEWACTIVE = "NEWACTIVE";

    final static String COLLECT = "COLLECT";

    final static String FRIENDS = "FRIENDS";

    final static String ADVISE = "ADVISE";

    final static String LOGOUT = "LOGOUT";

    protected Context mContext;

    protected ViewHolder mViewHolder;

    private Drawable mTitleDrawable;

    private int layoutResId;

    private String mTitleName;

    protected String mItemTYpe;

    private boolean attrsInit = false;

    public MenuItemBase(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItemBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenuNormalBase, defStyleAttr, 0);
        mTitleDrawable = attributes.getDrawable(R.styleable.SlidingMenuNormalBase_titleImg);
        mTitleName = attributes.getString(R.styleable.SlidingMenuNormalBase_titleName);
        mItemTYpe = attributes.getString(R.styleable.SlidingMenuNormalBase_normalType);
        layoutResId = attributes.getResourceId(R.styleable.SlidingMenuNormalBase_layoutRes, -1);
        attrsInit = true;
        attributes.recycle();

        initView();
    }

    protected void initView() {
        if (layoutResId != -1) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            layoutInflater.inflate(layoutResId, this, true);
            mViewHolder = new ViewHolder();
            mViewHolder.itemImg = (ImageView) findViewById(R.id.slidingmenu_item_img);
            mViewHolder.itemName = (TextView) findViewById(R.id.slidingmenu_item_name);
            if (attrsInit) {
                mViewHolder.itemImg.setImageDrawable(mTitleDrawable);
                mViewHolder.itemName.setText(mTitleName);
            }
        }
    }

    private class ViewHolder {
        ImageView itemImg;

        TextView itemName;
    }
}
