package com.chinahelth.ui.homepush;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.chinahelth.HealthConfig;
import com.chinahelth.support.utils.LogUtils;

import java.lang.reflect.Field;

/**
 * Created by caihanyuan on 15-7-8.
 */
public class HomePushViewPager extends ViewPager {

    private final static String TAG = HomePushViewAdapter.class.getSimpleName();

    private final static int SCROLL = 1;
    int firstPage = 0;
    int endPage;
    private Scroller defaultScroller;
    private Scroller myScroller;
    private OnPageChangeListener pageChangeListener;
    private Handler scrollHandler;
    private Thread timeThread;
    private int scrollState = ViewPager.SCROLL_STATE_IDLE;
    private boolean timeThreadRun = true;
    private long lastScrollTime = 0;

    public HomePushViewPager(Context context) {
        super(context);
        initView();
    }

    public HomePushViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        goToFirtPage();
    }

    @Override
    protected void onDetachedFromWindow() {
        LogUtils.d(TAG, "HomePUshViewPager onDetachedFromWindow");
        timeThreadRun = false;
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getScrooler() != defaultScroller) {
                setScroller(defaultScroller);
                LogUtils.d(TAG, "set default scroller");
            }
            resetScrollTime();
        }
        return super.onInterceptTouchEvent(event);
    }

    OnPageChangeListener getPageChangeListener() {
        return pageChangeListener;
    }

    void goToFirtPage() {
        setCurrentItem(firstPage, false);
    }

    void goToLastPage() {
        setCurrentItem(endPage, false);
    }

    private void initView() {
        timeThread = new TimeThread();
        scrollHandler = new ScrollHandler(Looper.getMainLooper());
        pageChangeListener = new HomePushViewPageChangeListener();
        if (HealthConfig.isAutoRun) {
            defaultScroller = getScrooler();
            Interpolator sInterpolator = new AccelerateDecelerateInterpolator();
            myScroller = new HomePushViewScroller(getContext(), sInterpolator);
            setScroller(myScroller);
            timeThread.start();
        }
    }

    private void setScroller(Scroller scroller) {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException e) {
            LogUtils.e(TAG, e.toString());
        } catch (IllegalArgumentException e) {
            LogUtils.e(TAG, e.toString());
        } catch (IllegalAccessException e) {
            LogUtils.e(TAG, e.toString());
        }
    }

    private Scroller getScrooler() {
        Scroller currentScroll = null;
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            currentScroll = (Scroller) mScroller.get(this);
        } catch (NoSuchFieldException e) {
            LogUtils.e(TAG, e.toString());
        } catch (IllegalArgumentException e) {
            LogUtils.e(TAG, e.toString());
        } catch (IllegalAccessException e) {
            LogUtils.e(TAG, e.toString());
        } finally {
            return currentScroll;
        }
    }

    private void scrollToNextPage() {
        if (getAdapter() != null && getAdapter().getCount() > 1) {
            setCurrentItem(getCurrentItem() + 1, true);
        }
    }

    private synchronized boolean isTimeToScroll() {
        boolean scroll = System.currentTimeMillis() - lastScrollTime > HealthConfig.AUTO_SCROLL_DELAY;
        lastScrollTime = scroll ? System.currentTimeMillis() : lastScrollTime;
        return scroll;
    }

    private synchronized void resetScrollTime() {
        lastScrollTime = System.currentTimeMillis();
    }

    private class HomePushViewPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            scrollState = state;
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (HealthConfig.isLoop) {
                    int position = getCurrentItem();
                    if (position == 0) {
                        goToLastPage();
                    } else if (position == getAdapter().getCount() - 1) {
                        goToFirtPage();
                    }
                }
                if (getScrooler() != myScroller) {
                    setScroller(myScroller);
                    LogUtils.d(TAG, "set own scroller");
                }
                resetScrollTime();
            }
        }
    }

    private class ScrollHandler extends Handler {
        private ScrollHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SCROLL) {
                scrollToNextPage();
            }
        }
    }

    private class TimeThread extends Thread {
        @Override
        public void run() {
            while (timeThreadRun) {
                if (isTimeToScroll() && scrollState == ViewPager.SCROLL_STATE_IDLE) {
                    Message message = new Message();
                    message.what = SCROLL;
                    scrollHandler.sendMessage(message);
                }
            }
        }
    }
}
