package com.dazhi.newsdemo2.Utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/5/30.
 */
public class LogUtils {
    public static final String TAG = "新闻随意看";
    public static boolean isDebug = true;

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(LogUtils.TAG, msg);
        }
    }
}



