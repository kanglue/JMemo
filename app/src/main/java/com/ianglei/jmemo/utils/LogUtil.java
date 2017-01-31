package com.ianglei.jmemo.utils;

import android.util.Log;

import com.ianglei.jmemo.BuildConfig;
import com.orhanobut.logger.Logger;

/**
 * Author: xsf
 * Time: created at 2016/4/22.
 * Email: xsf_uestc_ncl@163.com
 */
public class LogUtil {
    public static final boolean isDebugMode = BuildConfig.DEBUG;

    //public static final boolean isDebugMode = false;
    public static void d(String message, Object... args) {
        if (isDebugMode) {
            Logger.d(message, args);
        }/*else  Log.d("xsf","shibai");*/
    }

    public static void d(String tag, String msg) {
        if (isDebugMode) {
            Log.d(tag, msg);
        }
    }

}
