package com.chinahelth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import com.chinahelth.ui.homepages.HomepagesContainerFragment;
import com.chinahelth.ui.slidingmenu.MenuContainerFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by caihanyuan on 6/27/15.
 */
public class HealthMainActivity extends SlidingFragmentActivity {
    private final static String TAG = HealthMainActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        configSlidingMenu();
        setBehindContentView(R.layout.slidingmenu_container_layout);
        setContentView(R.layout.health_main_container_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.sliding_menu_container, getSlidingMenuContainerFragment(), MenuContainerFragment.TAG);
        Fragment homepagesContainerFragment = getHomepagesContainerFragment();
        if (!homepagesContainerFragment.isAdded()) {
            fragmentTransaction.add(R.id.chinahealth_main_container, homepagesContainerFragment, HomepagesContainerFragment.TAG);
        }
        fragmentTransaction.commit();
    }

    private void configSlidingMenu() {
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setBehindWidth(getSlidingMenuSuitableWidth());
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    }

    private int getSlidingMenuSuitableWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windowWidth = displayMetrics.widthPixels;
        int suitableWidth = (int) (windowWidth * 0.66);
        return suitableWidth;
    }

    private Fragment getSlidingMenuContainerFragment() {
        Fragment slidingMenuContainer = getSupportFragmentManager().findFragmentByTag(MenuContainerFragment.TAG);
        if (slidingMenuContainer == null) {
            slidingMenuContainer = new MenuContainerFragment();
        }
        return slidingMenuContainer;
    }

    private Fragment getHomepagesContainerFragment() {
        Fragment homepagesContainerFragment = getSupportFragmentManager().findFragmentByTag(HomepagesContainerFragment.TAG);
        if (homepagesContainerFragment == null) {
            homepagesContainerFragment = new HomepagesContainerFragment();
        }
        return homepagesContainerFragment;
    }
}
