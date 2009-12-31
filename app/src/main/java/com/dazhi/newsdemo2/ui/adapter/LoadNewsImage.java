package com.dazhi.newsdemo2.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dazhi.newsdemo2.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2010/1/1.
 */
public class LoadNewsImage {
    private ImageView mImageView;
    private String mUrlTag;

    private ListView mListView;
    private Set<NewsAsyncTask> mTask;


    //缓存机制
    private LruCache<String, Bitmap> mCaches;



    public LoadNewsImage(ListView listView) {
        mListView = listView;
        mTask = new HashSet<>();

        //设置缓存的大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        Log.d("maxMemory", String.valueOf(maxMemory));
        int cacheSize = maxMemory / 4;
        mCaches = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();    //返回图片的大小
            }
        };

    }

    public void LoadImages(int start, int end) {
        for (int i = start; i < end-1; i++) {
            mUrlTag = NewsAdapter.urls[i];
            //从缓存中取出对应的图片
            Bitmap bitmap = getBitmapFromCache(mUrlTag);
            if (bitmap == null) {          //没有去下载
                NewsAsyncTask task = new NewsAsyncTask(mUrlTag);
                task.execute(mUrlTag);
                mTask.add(task);
                Log.d("第一次","bitmap == null");
            } else {

                ImageView imageView = (ImageView) mListView.findViewWithTag(mUrlTag);
                Log.d("第一次",mUrlTag);
                if (imageView==null){
                    Log.d("第一次","imageView   为空的");
                }else {
                    imageView.setImageBitmap(bitmap);
                }

            }

        }
    }

    //加载Bitmap到Cache
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (mCaches.get(url) == null) {
            mCaches.put(url, bitmap);
        }
    }

    //从缓存中获取数据
    public Bitmap getBitmapFromCache(String url) {
        return mCaches.get(url);
    }

    //new Thread线程
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (mImageView.getTag().equals(mUrlTag)) {
//                mImageView.setImageBitmap((Bitmap) msg.obj);
//            }
//        }
//    };


//    public void showImageViewByThread(final ImageView imageView, final String url) {
//        mImageView = imageView;
//        mUrlTag = url;
//        new Thread() {
//            @Override
//            public void run() {
//                Bitmap bitmap = getBitmapFromURL(url);
//                Message message = new Message();
//                message.obj = bitmap;
//                mHandler.sendMessage(message);
//            }
//        }.start();
//
//    }

    public Bitmap getBitmapFromURL(String url) {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    //    使用AsyncTask**************************************************************
    public void showImageByAsyncTask(ImageView imageView, String url) {
        //从缓存中取出对应的图片
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap == null) {          //没有去下载
            imageView.setImageResource(R.drawable.defaultpic);
        } else {
            imageView.setImageBitmap(bitmap);
        }


    }

    //暂停异步任务
    public void cancelAllTasks() {
        if (mTask != null){
            for (NewsAsyncTask task : mTask){
                task.cancel(false);
            }
        }
    }

    public class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {
        //        private ImageView mImageView;
        private String mUrl;

        public NewsAsyncTask(String url) {
//            mImageView = imageView;
            this.mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
//            String url = params[0];    这里面的路径使用传进来的就行了，要不容易混乱啊！！！！！
            Bitmap bitmap = getBitmapFromURL(mUrl);
            if (bitmap != null) {
                addBitmapToCache(mUrl, bitmap);
            }

            return bitmap;  //下载图片
        }

        @Override  //更新UI线程
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            mTask.remove(this);  //下载完成就移除了
        }
    }


}
