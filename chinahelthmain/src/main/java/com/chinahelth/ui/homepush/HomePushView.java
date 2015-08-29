package com.chinahelth.ui.homepush;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.chinahelth.HealthConfig;
import com.chinahelth.R;
import com.chinahelth.viewpagerIndicator.PageIndicator;

import java.util.ArrayList;

/**
 * View that push prority imformation in homepage
 * Created by caihanyuan on 7/2/15.
 */
public class HomePushView extends FrameLayout {
    private final static String TAG = HomePushView.class.getName();

    /**
     * ratio height/width
     */
    private final static float ASPECT_RATIO = 0.66f;

    private HomePushViewPager viewPager;

    private PageIndicator pageIndicator;

    private final ArrayList<View> mMatchParentChildren = new ArrayList<View>(1);

    public HomePushView(Context context) {
        super(context);
        initView(context);
    }

    public HomePushView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomePushView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        final boolean measureMatchParentChildren =
                MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY ||
                        MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
        mMatchParentChildren.clear();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth,
                        child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                childState = combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren) {
                    if (lp.width == LayoutParams.MATCH_PARENT ||
                            lp.height == LayoutParams.MATCH_PARENT) {
                        mMatchParentChildren.add(child);
                    }
                }
            }
        }

        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        // Check against our foreground's minimum height and width
        final Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }

        int widthMeasured = resolveSizeAndState(maxWidth, widthMeasureSpec, childState);
        int heightMeasured = (int) (widthMeasured * ASPECT_RATIO);
        if (HealthConfig.isDebug) {
            Log.d(TAG, "onMeasure excute, widthMeasured:" + widthMeasured + " heightMeasured:" + heightMeasured);
        }
        setMeasuredDimension(widthMeasured, heightMeasured);

        count = mMatchParentChildren.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final View child = mMatchParentChildren.get(i);

                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int childWidthMeasureSpec;
                int childHeightMeasureSpec;

                if (lp.width == LayoutParams.MATCH_PARENT) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() -
                                    getPaddingLeft() - getPaddingRight() -
                                    lp.leftMargin - lp.rightMargin,
                            MeasureSpec.EXACTLY);
                } else {
                    childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                            getPaddingLeft() + getPaddingRight() +
                                    lp.leftMargin + lp.rightMargin,
                            lp.width);
                }

                if (lp.height == LayoutParams.MATCH_PARENT) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() -
                                    getPaddingTop() - getPaddingBottom() -
                                    lp.topMargin - lp.bottomMargin,
                            MeasureSpec.EXACTLY);
                } else {
                    childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom() +
                                    lp.topMargin + lp.bottomMargin,
                            lp.height);
                }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.homepushview_layout, this, true);

        viewPager = (HomePushViewPager) findViewById(R.id.homepush_viewpager);
        pageIndicator = (PageIndicator) findViewById(R.id.homepush_indicator);

        HomePushViewAdapter viewpagerAdapter = new HomePushViewAdapter(context, viewPager);
        viewPager.setAdapter(viewpagerAdapter);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.home_push_view_page_margin));
        viewPager.setOffscreenPageLimit(getResources().getInteger(R.integer.home_push_view_cache_size));
        pageIndicator.setViewPager(viewPager);
        pageIndicator.setOnPageChangeListener(viewPager.getPageChangeListener());

    }
}
