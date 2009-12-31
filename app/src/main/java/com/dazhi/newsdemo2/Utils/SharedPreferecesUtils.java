package com.dazhi.newsdemo2.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/5/30.
 */
public class SharedPreferecesUtils {
    public static void saveSP(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveMessage", Context.MODE_PRIVATE);
        ;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSP(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("saveMessage", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }


}
