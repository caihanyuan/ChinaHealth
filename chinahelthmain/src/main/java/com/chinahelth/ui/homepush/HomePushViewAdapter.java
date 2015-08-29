package com.chinahelth.ui.homepush;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chinahelth.HealthApplication;
import com.chinahelth.HealthConfig;
import com.chinahelth.support.bean.HomePushItemBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by caihanyuan on 7/2/15.
 */
public class HomePushViewAdapter extends PagerAdapter {
    private final static String TAG = HomePushViewAdapter.class.getSimpleName();

    private Context context;

    private ArrayList<HomePushItemBean> pushItemDatas = new ArrayList();

    private HashMap<Integer, HomePushItem> pushItems = new HashMap();

    private HomePushViewPager viewPager;

    private HomePushItem currentPushItem = null;

    public HomePushViewAdapter(Context context, HomePushViewPager viewPager) {
        this.context = context;
        this.viewPager = viewPager;
        initData();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HomePushItem homePushItem;
        if (HealthConfig.isLoop) {
            homePushItem = instantiateItemForLoop(container, position);
        } else {
            homePushItem = instantiateItemNormal(container, position, position);
        }
        container.addView(homePushItem);
        if (HealthConfig.isDebug) {
            Log.d(TAG, "new pushitem " + position + ": " + homePushItem.getItemData().toString());
        }
        return homePushItem;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

        if (HealthConfig.isDebug) {
            Log.d(TAG, "destory item " + position + ": " + object.toString());
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        HomePushItem homePushItem = (HomePushItem) object;
        if (currentPushItem == null)
            currentPushItem = homePushItem;
        if (currentPushItem != homePushItem) {
            currentPushItem = homePushItem;

            if (HealthConfig.isDebug) {
                Log.d(TAG, "current pushitem " + position + ": " + homePushItem.getItemData().toString());
            }
        }
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }


    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }


    @Override
    public int getItemPosition(Object object) {
        int position = 0;
        Set<Integer> positions = pushItems.keySet();
        Iterator<Integer> positionIterator = positions.iterator();
        while (positionIterator.hasNext()) {
            int positionkey = positionIterator.next();
            if (pushItems.get(positionkey) == object) {
                position = positionkey;
                break;
            }
        }
        if (HealthConfig.isDebug) {
            Log.d(TAG, "get item position:" + position);
        }

        return position;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        if (HealthConfig.isLoop)
            return pushItemDatas.size() + 2;
        else
            return pushItemDatas.size();
    }

    private void initData() {
        try {
            String[] fileNames = HealthApplication.getInstance().getAssets().list("screen_img");
            int fileLen = fileNames.length;
            for (int i = 0; i < fileLen; i++) {
                HomePushItemBean bean = new HomePushItemBean();
                bean.articleId = "article" + i;
                bean.articleTitle = "this is a new title" + i;
                bean.imageUri = "assets://screen_img/" + fileNames[i];
                pushItemDatas.add(bean);
            }
            initPageIndex();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private HomePushItem instantiateItemForLoop(ViewGroup container, int position) {
        int dataPosition = 0;
        if (position == 0)
            dataPosition = pushItemDatas.size() - 1;
        else if (position == getCount() - 1)
            dataPosition = 0;
        else
            dataPosition = position - 1;
        return instantiateItemNormal(container, position, dataPosition);
    }

    private HomePushItem instantiateItemNormal(ViewGroup container, int position, int dataPosition) {
        HomePushItem homePushItem = pushItems.get(position);
        if (homePushItem == null) {
            homePushItem = new HomePushItem(context);
            pushItems.put(position, homePushItem);
            HomePushItemBean pushItemBean = pushItemDatas.get(dataPosition);
            homePushItem.setItemData(pushItemBean);
        }
        return homePushItem;
    }

    private void initPageIndex() {
        if (HealthConfig.isLoop) {
            viewPager.firstPage = 1;
            viewPager.endPage = getCount() - 2;
        } else {
            viewPager.firstPage = 0;
            viewPager.endPage = getCount() - 1;
        }
        viewPager.goToFirtPage();
    }
}
