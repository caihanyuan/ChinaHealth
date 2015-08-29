package com.chinahelth.ui.homepush;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.chinahelth.HealthConfig;
import com.chinahelth.viewpagerIndicator.CirclePageIndicator;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by caihanyuan on 15-7-9.
 */
public class HomePushViewCircleIndicator extends CirclePageIndicator {
    private final static String TAG = HomePushViewCircleIndicator.class.getSimpleName();

    public HomePushViewCircleIndicator(Context context) {
        super(context);
    }

    public HomePushViewCircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomePushViewCircleIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mViewPager == null) {
            return;
        }
        int count = mViewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        int longSize;
        int longPaddingBefore;
        int longPaddingAfter;
        int shortPaddingBefore;
        if (mOrientation == HORIZONTAL) {
            longSize = getWidth();
            longPaddingBefore = getPaddingLeft();
            longPaddingAfter = getPaddingRight();
            shortPaddingBefore = getPaddingTop();
        } else {
            longSize = getHeight();
            longPaddingBefore = getPaddingTop();
            longPaddingAfter = getPaddingBottom();
            shortPaddingBefore = getPaddingLeft();
        }

        final float threeRadius = mRadius * 3;
        final float shortOffset = shortPaddingBefore + mRadius;
        float longOffset = longPaddingBefore + mRadius;
        if (mCentered) {
            longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - ((count * threeRadius) / 2.0f);
        }

        float dX;
        float dY;

        float pageFillRadius = mRadius;
        if (mPaintStroke.getStrokeWidth() > 0) {
            pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
        }

        int iLoop = 0;
        if (HealthConfig.isLoop) {
            iLoop = 1;
            count = count - 1;
        }
        float firstCenter = iLoop;
        float lastCenter = count - 1;
        //Draw stroked circles
        for (; iLoop < count; iLoop++) {
            float drawLong = longOffset + (iLoop * threeRadius);
            if (mOrientation == HORIZONTAL) {
                dX = drawLong;
                dY = shortOffset;
            } else {
                dX = shortOffset;
                dY = drawLong;
            }
            //save first circle and last circle center postion
            if (firstCenter == iLoop) {
                firstCenter = drawLong;
            }
            if (lastCenter == iLoop) {
                lastCenter = drawLong;
            }

            // Only paint fill if not completely transparent
            if (mPaintPageFill.getAlpha() > 0) {
                canvas.drawCircle(dX, dY, pageFillRadius, mPaintPageFill);
            }

            // Only paint stroke if a stroke width was non-zero
            if (pageFillRadius != mRadius) {
                canvas.drawCircle(dX, dY, mRadius, mPaintStroke);
            }
        }

        //Draw the filled circle according to the current scroll
        float cx = (mSnap ? mSnapPage : mCurrentPage) * threeRadius;
        if (!mSnap) {
            cx += mPageOffset * threeRadius;
        }
        if (mOrientation == HORIZONTAL) {
            dX = longOffset + cx;
            dY = shortOffset;
            dX = dX < firstCenter ? firstCenter : dX;
            dX = dX > lastCenter ? lastCenter : dX;
        } else {
            dX = shortOffset;
            dY = longOffset + cx;
            dY = dY < firstCenter ? firstCenter : dY;
            dY = dY > lastCenter ? lastCenter : dY;
        }
        canvas.drawCircle(dX, dY, mRadius, mPaintFill);
    }
}
