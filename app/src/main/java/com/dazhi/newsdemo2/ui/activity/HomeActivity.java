package com.dazhi.newsdemo2.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dazhi.newsdemo2.R;
import com.dazhi.newsdemo2.Utils.HttpURLConnectionUtil;
import com.dazhi.newsdemo2.common.bean.NewsBean;
import com.dazhi.newsdemo2.common.parse.NewsParse;
import com.dazhi.newsdemo2.ui.adapter.NewsAdapter;
import com.dazhi.newsdemo2.ui.base.MyBaseActivity;
import com.dazhi.newsdemo2.views.SlidingMenu;

import java.util.ArrayList;

public class HomeActivity extends MyBaseActivity implements ReFlashListView.IReflashListener,
        View.OnClickListener, AdapterView.OnItemClickListener {
    private WebView mWebView;

    private ImageView iv_set;
    private ImageView iv_user;
    private ReFlashListView listview;
//    private ListView listview;

    /**
     * 左侧菜单
     */
    private int menu_item_background = 868767061;     //菜单栏被点动是的背景颜色
    private int menu_item_none_background = 16777215;     //菜单栏被点动设置无颜色

    private RelativeLayout rlNews;
    private RelativeLayout rlReading;
    private RelativeLayout rlLocal;
    private RelativeLayout rlCommnet;
    private RelativeLayout rlPhoto;

    //将上面的菜单栏的布局加入数组
    RelativeLayout[] memuLayoutArray = {rlNews, rlReading, rlLocal, rlCommnet, rlPhoto};

    //中间资讯的布局，里面有三个layout
    private RelativeLayout mLayout;

    //左侧菜单栏的每个layout对应的view,替换layout的时候用
    private View mViewNews;
    private View mViewReading;
    private View mViewLocal;
    private View mViewComnnet;
    private View mViewPhotos;

    //实例化SlidingMenu的对象
    private SlidingMenu mSlidingMenu;

    private NewsAdapter mAdapter;   //新闻的适配器

    private static int mWhat = 1;


    private TextView textView_title;
    //news的新闻集合
    private ArrayList<NewsBean.DataBean> jsonList = new ArrayList<>();

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mAdapter = new NewsAdapter(HomeActivity.this, jsonList, listview);

                    listview.setAdapter(mAdapter);

                    break;

            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_news);

        initID();  //初始化，关联ID

        new Thread() {
            @Override
            public void run() {
                String json11 = "http://118.244.212.82:9092/newsClient/news_list?" +
                        "ver=4&subid=1&dir=1&nid=2&stamp=20150601&cnt=20";
                String json = HttpURLConnectionUtil.getHttpJson(json11);  //网上获取Json
                jsonList = NewsParse.parseNewsJson(json);               //解析Json

                handler.sendEmptyMessage(mWhat);
            }
        }.start();


    }

    private void initID() {
        //主页面（资讯）关联ID
        iv_set = (ImageView) findViewById(R.id.imageView_set);
        iv_user = (ImageView) findViewById(R.id.imageView_user);
//        listview = (ListView) findViewById(R.id.lv_news_listview);
        listview = (ReFlashListView) findViewById(R.id.lv_news_listview);

        listview.setInterface(this);   //顶部下拉接口
//        listview.setFooterReflash(this);//底部上拉监听

        //菜单栏的布局的关联ID
        memuLayoutArray[0] = (RelativeLayout) findViewById(R.id.rl_news);
        memuLayoutArray[1] = (RelativeLayout) findViewById(R.id.rl_reading);
        memuLayoutArray[2] = (RelativeLayout) findViewById(R.id.rl_local);
        memuLayoutArray[3] = (RelativeLayout) findViewById(R.id.rl_commnet);
        memuLayoutArray[4] = (RelativeLayout) findViewById(R.id.rl_photo);
        //中间资讯布局
        mLayout = (RelativeLayout) findViewById(R.id.ll_linearLayout);
        //自定义HongzontalScroll
        mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);

        //主页面（资讯）监听
        iv_set.setOnClickListener(this);
        iv_user.setOnClickListener(this);
        //listView监听
        listview.setOnItemClickListener(this);
        //菜单栏的布局监听
        memuLayoutArray[0].setOnClickListener(this);
        memuLayoutArray[1].setOnClickListener(this);
        memuLayoutArray[2].setOnClickListener(this);
        memuLayoutArray[3].setOnClickListener(this);
        memuLayoutArray[4].setOnClickListener(this);

        //自己定义的点击菜单后主页面显示的内容
        mViewNews = View.inflate(this, R.layout.activity_activity_main, null);
        mViewReading = View.inflate(this, R.layout.click_menu_to_layout, null);
        mViewLocal = View.inflate(this, R.layout.click_menu_to_layout, null);
        mViewComnnet = View.inflate(this, R.layout.click_menu_to_layout, null);
        mViewPhotos = View.inflate(this, R.layout.click_menu_to_layout, null);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_set:   // 左菜单
                mSlidingMenu.toggleLeft();

                break;
            case R.id.imageView_user:  //右边设置
                mSlidingMenu.toggleRight();
                break;

            //菜单栏的布局监听
            case R.id.rl_news:

                setMenuClickAcion(mViewNews, memuLayoutArray[0]);
                initID();

                listview.setAdapter(mAdapter);
//                try {
//                    Thread.sleep(1000);
//                    mAdapter = new NewsAdapter(HomeActivity.this, jsonList, listview);
//                    listview.setAdapter(mAdapter);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


                break;
            case R.id.rl_reading:


                setMenuClickAcion(mViewReading, memuLayoutArray[1]);

                break;
            case R.id.rl_local:

                setMenuClickAcion(mViewLocal, memuLayoutArray[2]);
                break;
            case R.id.rl_commnet:

                setMenuClickAcion(mViewComnnet, memuLayoutArray[3]);
                break;
            case R.id.rl_photo:

                setMenuClickAcion(mViewPhotos, memuLayoutArray[4]);
                break;

        }

    }

    //当左侧菜单栏点击后的反应
    public void setMenuClickAcion(View view, RelativeLayout rlMenu) {

        //先移除原有的view
        mLayout.removeAllViews();
//        mLayout.setVisibility(View.GONE);
        //加载view
        mLayout.addView(view);
        //吐司
        Toast.makeText(HomeActivity.this, "功能待完善", Toast.LENGTH_SHORT).show();
        //关闭左侧菜单栏
        mSlidingMenu.toggleLeft();

        //设置菜单的背景颜色
        for (int i = 0; i < 5; i++) {
            if (rlMenu == memuLayoutArray[i]) {
                memuLayoutArray[i].setBackgroundColor(menu_item_background);
            } else {
                memuLayoutArray[i].setBackgroundColor(menu_item_none_background);
            }
        }


    }


    //listview的监听事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //注意这个get(position-1)里面的-1,因为加了一个下拉，下拉业占一个item
        String newsLink = jsonList.get(position-1).getLink();
        Bundle bundle = new Bundle();
        bundle.putString("link", newsLink);
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    //下拉刷新的是实现的接口
    @Override
    public void onReflash() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                mAdapter = new NewsAdapter(HomeActivity.this, jsonList, listview);
                listview.setAdapter(mAdapter);
                listview.reflashComplete();

            }
        }, 2000);
    }

    //底部上拉刷新的接口
//    @Override
//    public void footerReflash() {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                listview.footerLoadCompleted();
//            }
//        }, 2000);
//
//
//    }
}
