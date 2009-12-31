package com.dazhi.newsdemo2.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.dazhi.newsdemo2.Utils.LogUtils;

/**
 * Created by Administrator on 2016/5/31.
 */
public class MyBaseActivity extends Activity {
    protected int screen_w;
    protected int screen_h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screen_w = getWindowManager().getDefaultDisplay().getWidth();
        screen_h = getWindowManager().getDefaultDisplay().getHeight();

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d(getClass().getSimpleName() + "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d(getClass().getSimpleName() + "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(getClass().getSimpleName() + "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(getClass().getSimpleName() + "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(getClass().getSimpleName() + "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d(getClass().getSimpleName() + "onDestroy");
    }

    //跳转页面
    public void openActivity(Class<?> pClass, Bundle bundle, Uri uri) {

        Intent intent = new Intent(this, pClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);

    }

    public void openActivity(Class<?> pClass, Bundle bundle) {
        openActivity(pClass, bundle, null);
    }

    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, null);
    }

    //通过action字符串进行页面跳转，并带Bundle和Uri数据
    public void openActivity(String action, Bundle bundle, Uri uri) {
        Intent intent = new Intent(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (uri != null) {
            intent.setData(uri);
        }
        startActivity(intent);
    }

    public void openActivity(String action) {
        openActivity(action, null, null);
    }

    public void openActivity(String action, Bundle bundle) {
        openActivity(action, bundle, null);
    }


    /**
     * 公共功能封装
     */
    private Toast toast;

    public void showToast(int resId) {
        showToast(getString(resId));
    }

    private void showToast(String msg) {
        if (msg == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(msg);
            toast.show();
        }
    }

}
