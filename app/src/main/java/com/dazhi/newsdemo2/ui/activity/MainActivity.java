package com.dazhi.newsdemo2.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.dazhi.newsdemo2.R;
import com.dazhi.newsdemo2.Utils.SharedPreferecesUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager mViewPager;
    private Button mButton;

    //viewPager的图片ID
    int[] picIds = {R.drawable.bd, R.drawable.small, R.drawable.welcome, R.drawable.wy};
    private ArrayList<View> viewPagerList = new ArrayList<>();
    //viewpager下面的四个小点
    private ImageView mImageView1 = null;
    private ImageView mImageView2 = null;
    private ImageView mImageView3 = null;
    private ImageView mImageView4 = null;
    ImageView[] mImageViews = {mImageView1, mImageView2, mImageView3, mImageView4};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //判断是否是第一次登陆
        if (TextUtils.isEmpty(SharedPreferecesUtils.getSP(this, "FirstLand"))) {
            initViewPager(); //初次登陆时的图片翻页效果
            SharedPreferecesUtils.saveSP(this, "FirstLand", "true");
        } else {
            Intent intent = new Intent(this, AnimatinActivity.class);
            startActivity(intent);
            finish();

        }

        initViewPager();  //初始化viewPager

    }

    private void initViewPager() {
        mButton = (Button) findViewById(R.id.btn_jump);
        mButton.setOnClickListener(this);



        mImageViews[0] = (ImageView) findViewById(R.id.iv_show01);
        mImageViews[1] = (ImageView) findViewById(R.id.iv_show02);
        mImageViews[2] = (ImageView) findViewById(R.id.iv_show03);
        mImageViews[3] = (ImageView) findViewById(R.id.iv_show04);
        mImageViews[0].setImageResource(R.drawable.adware_style_selected);  //设置第一个图片为选中

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        for (int i = 0; i < picIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(picIds[i]);
            viewPagerList.add(imageView);
        }
        mViewPager.setAdapter(new MyAdapter(viewPagerList)); //为viewPaager设置适配器
        mViewPager.addOnPageChangeListener(this);     //设置监听
        mViewPager.setPageTransformer(true, new RotateDownPageTransformer());//设置动画效果

    }

    //ViewPager的监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //ViewPager的监听
    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mImageViews.length; i++) {
            if (i == position) {
                mImageViews[i].setImageResource(R.drawable.adware_style_selected);
            } else {
                mImageViews[i].setImageResource(R.drawable.adware_style_default);
            }
        }
        /**
         * 设置viewPage的图片翻到最后一张时，如果不自动点击“直接跳转”按钮，
         * 2秒后自动跳转到第二个页面
         */

        if (position == 3) {




            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            Intent intent = new Intent(MainActivity.this, AnimatinActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }
            };

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        handler.sendEmptyMessage(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }

//        有一个Bug,翻到最后一页后，再返回前几页，还是自动跳转
//        if (position != 3 && thread != null) {
//            thread.stop();
//
//            Log.d("哈哈", "dkjkfjkdjkfj");
//        }


    }

    //ViewPager的监听
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //button按键的监听
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AnimatinActivity.class);
        startActivity(intent);
        finish();
    }

    //ViewPager的adapter自定义类
    class MyAdapter extends PagerAdapter {
        ArrayList<View> list = new ArrayList<>();

        public MyAdapter(ArrayList<View> list) {
            this.list = list;
        }

        @Override    //缓存当前图片两边的图片
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override  //销毁图片
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    //viewPager动画效果
    private class RotateDownPageTransformer implements ViewPager.PageTransformer {
        private static final float ROT_MAX = 25.0f;
        private float mRot;

        @Override
        public void transformPage(View view, float position) {
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setRotation(0);

            } else if (position <= 1) // a页滑动至b页 ； a页从 0.0 ~ -1 ；b页从1 ~ 0.0
            { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                if (position < 0) {

                    mRot = (ROT_MAX * position);
                    view.setPivotX(view.getMeasuredWidth() * 0.5f);
                    view.setPivotY(view.getMeasuredHeight());
                    view.setRotation(mRot);
                } else {

                    mRot = (ROT_MAX * position);
                    view.setPivotX(view.getMeasuredWidth() * 0.5f);
                    view.setPivotY(view.getMeasuredHeight());
                    view.setRotation(mRot);
                }

                // Scale the page down (between MIN_SCALE and 1)

                // Fade the page relative to its size.

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setRotation(0);
            }

        }
    }

}
