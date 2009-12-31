package com.dazhi.newsdemo2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2010/1/1.
 */

public class SlidingMenu extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private ViewGroup mSetting;
    private int mScreenWidth;

    private int mRightMenuPadding;
    private boolean isFirst;

    private boolean isOpenMenu ;


    private int mMenuWidth;
    private int mSettingWidth;



    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        //窗口管理类
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //为了获取DisplayMetrics 成员，首先初始化一个对象
        DisplayMetrics outMetrics = new DisplayMetrics();
        //getDefaultDisplay() 方法将取得的宽 高 维度 存放于DisplayMetrics 对象中
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;

        //将dp转换为px
        mRightMenuPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());



    }


    //决定自己定义的宽和高，以及自己的宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!isFirst) {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mSetting = (ViewGroup) mWapper.getChildAt(2);

            //设置左面的宽度
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mRightMenuPadding;
            mContent.getLayoutParams().width = mScreenWidth;  //设置主页面的宽度
            mSettingWidth = mSetting.getLayoutParams().width = mMenuWidth;  //设置右面的面的宽度

            isFirst = true;
        }

    }

    //决定子view的位置
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);  //super必须有，而且必须在前面
        if (changed) {

            this.scrollTo(mMenuWidth, 0);  //view迅速滚动到偏移的位置，（X,Y）x,为整数则向左滑动

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = ev.getAction();  //获取行为对象
        switch (action) {
            case MotionEvent.ACTION_UP:  //行为对象为抬起手指的时候
                int scrollX = getScrollX();  //隐藏的宽度
                if (scrollX <= mMenuWidth / 2) {
                    this.smoothScrollTo(0, 0);
                    isOpenMenu = true;
                } else if (scrollX <= mMenuWidth+mScreenWidth/2){
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpenMenu = false;
                }else {
                    this.smoothScrollTo(mMenuWidth+mScreenWidth,0);
                }
                return true;


        }

        return super.onTouchEvent(ev);
    }
//按键打开左菜单
    public void openMenu(){
        if (isOpenMenu){
            return;
        }
        this.smoothScrollTo(0,0);
        isOpenMenu = true;
    }
    //按键关闭左菜单
    public void closeMenu(){
        if (!isOpenMenu){
            return;
        }
     this.smoothScrollTo(mMenuWidth,0);
        isOpenMenu = false;
    }
//按键管理左菜单
    public  void toggleLeft(){
        if (isOpenMenu){
            closeMenu();
        }else {
            openMenu();
        }
    }


    //按键打开右设置界面
    public void openSettingRight(){
        if (isOpenMenu){
            return;
        }
        this.smoothScrollTo(mMenuWidth+mScreenWidth,0);
        isOpenMenu = true;
    }
    //按键关闭右设置界面
    public void closeSettingRight(){
        if (!isOpenMenu){
            return;
        }
        this.smoothScrollTo(mMenuWidth, 0);
        isOpenMenu = false;
    }
    //管理右边设置界面
    public  void toggleRight(){
        if (isOpenMenu){
            closeSettingRight();
        }else {
            openSettingRight();
        }
    }



}
