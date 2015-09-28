package com.chinahelth.ui.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.chinahelth.R;

/**
 * this customImageView will resize Height default
 * Created by caihanyuan on 9/25/15.
 */
public class CustomImageView extends ImageView {

    private final static String TAG = CustomImageView.class.getSimpleName();

    private int mMaxWidth = Integer.MAX_VALUE;

    private int mMaxHeight = Integer.MAX_VALUE;

    private int mWidthPercent = -1;

    private int mHeightPercent = -1;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        mWidthPercent = a.getInteger(R.styleable.CustomImageView_weightPercent, -1);
        mHeightPercent = a.getInteger(R.styleable.CustomImageView_heightPercent, -1);
        a.recycle();
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
        super.setMaxWidth(maxWidth);
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
        super.setMaxHeight(maxHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = 0;
        int h = 0;
        Drawable drawable = getDrawable();

        float desiredAspect = 0.0f;

        if (drawable == null) {
            // If no drawable, its intrinsic size is 0.
            w = h = 0;
        } else {
            w = drawable.getIntrinsicWidth();
            h = drawable.getIntrinsicHeight();
            if (w <= 0) w = 1;
            if (h <= 0) h = 1;

            desiredAspect = (float) w / (float) h;
        }

        int pleft = getPaddingLeft();
        int pright = getPaddingRight();
        int ptop = getPaddingTop();
        int pbottom = getPaddingBottom();

        int widthSize;
        int heightSize;
        int desiredWidth;
        int desiredHeight;

        if (mWidthPercent != -1) {
            int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
            desiredWidth = parentWidth * mWidthPercent / 100 + pleft + pright;
        } else {
            desiredWidth = w + pleft + pright;
        }
        if (mHeightPercent != -1) {
            int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
            desiredHeight = parentHeight * mHeightPercent / 100 + ptop + pbottom;
        } else {
            desiredHeight = h + ptop + pbottom;
        }

        // Get the max possible width given our constraints
        widthSize = resolveAdjustedSize(desiredWidth, mMaxWidth, widthMeasureSpec, mWidthPercent);

        // Get the max possible height given our constraints
        heightSize = resolveAdjustedSize(desiredHeight, mMaxHeight, heightMeasureSpec, mHeightPercent);

        if (desiredAspect != 0.0f) {
            // See what our actual aspect ratio is
            float actualAspect = (float) (widthSize - pleft - pright) /
                    (heightSize - ptop - pbottom);

            if (Math.abs(actualAspect - desiredAspect) > 0.0000001) {
                // Try adjusting height to be proportional to width
                int newHeight = (int) ((widthSize - pleft - pright) / desiredAspect) +
                        ptop + pbottom;
                heightSize = newHeight <= mMaxHeight ? newHeight : heightSize;
            }
        } else {
            /* We are either don't want to preserve the drawables aspect ratio,
               or we are not allowed to change view dimensions. Just measure in
               the normal way.
            */
            w += pleft + pright;
            h += ptop + pbottom;

            w = Math.max(w, getSuggestedMinimumWidth());
            h = Math.max(h, getSuggestedMinimumHeight());

            widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
            heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    private int resolveAdjustedSize(int desiredSize, int maxSize,
                                    int measureSpec, int percentValue) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /* Parent says we can be as big as we want. Just don't be larger
                   than max size imposed on ourselves.
                */
                result = Math.min(desiredSize, maxSize);
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                if (percentValue != -1) {
                    result = Math.min(desiredSize, specSize);
                } else {
                    result = specSize;
                }
                break;
        }
        return result;
    }
}
