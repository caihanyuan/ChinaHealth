package com.chinahelth.ui.homepush;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.chinahelth.HealthConfig;

/**
 * Created by caihanyuan on 15-7-8.
 */
public class HomePushViewScroller extends Scroller {
    public HomePushViewScroller(Context context) {
        super(context);
    }

    public HomePushViewScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public HomePushViewScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, HealthConfig.SCROLL_DURATION);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, HealthConfig.SCROLL_DURATION);
    }
}
