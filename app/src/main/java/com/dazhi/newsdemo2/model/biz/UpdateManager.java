package com.dazhi.newsdemo2.model.biz;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.webkit.DownloadListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/31.
 */
public class UpdateManager {
    public static void downLoad(Context context,String url){
        //初始化下载管理器
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //创建请求
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //设置允许的网络类型  wifi
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //在通知栏显示下载详情
        request.setShowRunningNotification(true);
        //显示下载界面
        request.setVisibleInDownloadsUi(true);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh-mm-ss");
        String date = dateFormat.format(new Date());

        //设置下载后文件的存放位置
        request.setDestinationInExternalFilesDir(context,null,date+".apk");

        manager.enqueue(request);  //将下载请求放入列队

    }



    public static void judgeUpdate(){}



}
