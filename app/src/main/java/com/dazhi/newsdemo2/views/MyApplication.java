package com.dazhi.newsdemo2.views;

import android.app.Application;
import android.content.res.Configuration;

import com.dazhi.newsdemo2.Utils.LogUtils;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/5/31.
 */
public class MyApplication extends Application {

    private HashMap<String, Object> allData = new HashMap<>();

    public void addAllData(String key, Object value) {
        allData.put(key, value);
    }

    public Object getAllData(String key) {
        return allData.get(key);
    }

    public void delAllDataByKey(String key) {
        if (allData.containsKey(key)) {
            allData.remove(key);
        }
    }

    public void delAllData() {
        allData.clear();
    }

    private static MyApplication application;

    //获取实例
    public static MyApplication getInstance() {
        LogUtils.d("MyApplication oncreate");
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        LogUtils.d("MyApplication oncreate");
    }
    //内存不足的时候


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtils.d("MyApplication onLowMemory");
    }

    //结束的时候
    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtils.d("MyApplication onTerminate");
    }

    //配置改变的时候
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.d("MyApplication onConfigurationChanged");
    }
}
