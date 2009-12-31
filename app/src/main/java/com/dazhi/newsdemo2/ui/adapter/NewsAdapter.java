package com.dazhi.newsdemo2.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dazhi.newsdemo2.R;
import com.dazhi.newsdemo2.common.bean.NewsBean;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2016/6/1.
 */
public class NewsAdapter extends MyBaseAdapter<NewsBean.DataBean> implements AbsListView.OnScrollListener {
    private LoadNewsImage mLoadNewsImage;

    private int start;
    private int end;
    private boolean isFistIn;

    public static String[] urls;

    //构造方法
    public NewsAdapter(Context context, List<NewsBean.DataBean> jsonList, ListView listView) {
        super(context);
        myList = null;
        myList = jsonList;
        Log.d("第二次", "NewsAdapter");
        Log.d("第二次", String.valueOf(myList.size()));
        mLoadNewsImage = new LoadNewsImage(listView);  //初始化对象

        urls = new String[myList.size()];

        for (int i = 0; i < myList.size(); i++) {
            NewsBean.DataBean bean = (NewsBean.DataBean) myList.get(i);
            urls[i] = bean.getIcon();
        }
        isFistIn = true;
        listView.setOnScrollListener(this);

    }


    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.news_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_news);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_titile);
            viewHolder.summary = (TextView) convertView.findViewById(R.id.tv_summary);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //当前title和summary的对象，由于添加了下拉，webview和Item就错位了
        NewsBean.DataBean bean = (NewsBean.DataBean) myList.get(position);
        //图片对应的Bean对象
//        NewsBean.DataBean beanIcon = (NewsBean.DataBean) myList.get(position);

        //开启线程加载图片
        String urlTag = bean.getIcon();
        viewHolder.imageView.setTag(urlTag);//给图片设置一个标记
        //这是用新建一个线程的方法
//        new LoadNewsImage().showImageViewByThread(viewHolder.imageView,bean.getIcon());
        //这是用AsyncTask，这里不能使用new  LoadNewsImage(),  因为每次创建一个对象，缓存就重新加载
//        new LoadNewsImage().showImageByAsyncTask(viewHolder.imageView,bean.getIcon());
        mLoadNewsImage.showImageByAsyncTask(viewHolder.imageView, urlTag);

        viewHolder.title.setText(bean.getTitle());
        viewHolder.summary.setText(bean.getSummary());

        return convertView;
    }


    class ViewHolder {
        ImageView imageView;
        TextView title;
        TextView summary;
    }

    //屏幕变化的时候调用监听
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {    //SCROLL_STATE_IDLE   滚动的闲置状态
            mLoadNewsImage.LoadImages(start, end);
            Log.d("第一次", "SCROLL_STATE_IDLE ");
        } else {
            mLoadNewsImage.cancelAllTasks();
            Log.d("第一次", "cancelAllTasks ");
        }
    }

    //屏幕正在滚动的时候调用
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        start = firstVisibleItem;
        end = firstVisibleItem + visibleItemCount;
        //第一次显示的时候调用
        if (isFistIn && visibleItemCount > 0) {
            mLoadNewsImage.LoadImages(start, end);
            Log.d("第一次", "jiazai ");
            isFistIn = false;
        }
    }


}
