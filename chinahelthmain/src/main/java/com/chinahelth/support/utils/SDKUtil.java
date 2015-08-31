package com.chinahelth.support.utils;

import android.os.Build;

/**
 * Created by caihanyuan on 15-7-7.
 */
public class SDKUtil {
    /**
     * version after 1.5
     */
    public static boolean isAfterCUPCAKE() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE;
    }

    /**
     * version after 1.6
     */
    public static boolean isAfterDONUT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT;
    }

    /**
     * version after 2.0
     */
    public static boolean isAfterECLAIR() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
    }

    /**
     * version after 2.0.1
     */
    public static boolean isAfterECLAIR_0_1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1;
    }

    /**
     * version after 2.1
     */
    public static boolean isAfterECLAIR_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1;
    }

    /**
     * version after 2.2
     */
    public static boolean isAfterFROYO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * version after 2.3
     */
    public static boolean isAfterGINGERBREAD() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * version after 2.3.3
     */
    public static boolean isAfterGINGERBREAD_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
    }

    /**
     * version after 3.0
     */
    public static boolean isAfterHONEYCOMB() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * version after 3.1
     */
    public static boolean isAfterHONEYCOMB_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * version after 3.2
     */
    public static boolean isAfterHONEYCOMB_MR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    /**
     * version after 4.0
     */
    public static boolean isAfterICE_CREAM_SANDWICH() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * version after 4.0.3
     */
    public static boolean isAfterICE_CREAM_SANDWICH_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    /**
     * version after 4.1
     */
    public static boolean isAfterJELLY_BEAN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * version after 4.2
     */
    public static boolean isAfterJELLY_BEAN_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * version after 4.3
     */
    public static boolean isAfterJELLY_BEAN_MR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * version after 5.0
     */
    public static boolean isAfterLOLLIPOP() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * version after 5.1
     */
    public static boolean isAfterLOLLIPOP_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }
}
