package com.chinahelth.ui.slidingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinahelth.R;

import java.util.HashMap;

/**
 * Created by caihanyuan on 15-7-10.
 */
public class MenuContainerFragment extends Fragment implements View.OnClickListener {

    public final static String TAG = MenuContainerFragment.class.getName();

    private HashMap<String, MenuItemBase> menuItems = new HashMap<>();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slidingmenu_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initMenuItems(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        MenuItemBase menuItemBase = (MenuItemBase) v;
    }

    private void initMenuItems(View container) {
        MenuItemBase menuItemBase = null;
        menuItemBase = (MenuItemBase) container.findViewById(R.id.sliding_menu_item_home);
        menuItemBase.setOnClickListener(this);
        menuItemBase = (MenuItemBase) container.findViewById(R.id.sliding_menu_item_account);
        menuItemBase.setOnClickListener(this);
        menuItems.put(menuItemBase.mItemTYpe, menuItemBase);
        menuItemBase = (MenuItemBase) container.findViewById(R.id.sliding_menu_item_collect);
        menuItemBase.setOnClickListener(this);
        menuItems.put(menuItemBase.mItemTYpe, menuItemBase);
        menuItemBase = (MenuItemBase) container.findViewById(R.id.sliding_menu_item_friends);
        menuItemBase.setOnClickListener(this);
        menuItems.put(menuItemBase.mItemTYpe, menuItemBase);
        menuItemBase = (MenuItemBase) container.findViewById(R.id.sliding_menu_item_advise);
        menuItemBase.setOnClickListener(this);
        menuItems.put(menuItemBase.mItemTYpe, menuItemBase);
        menuItemBase = (MenuItemBase) container.findViewById(R.id.sliding_menu_item_logout);
        menuItemBase.setOnClickListener(this);
        menuItems.put(menuItemBase.mItemTYpe, menuItemBase);
    }
}
