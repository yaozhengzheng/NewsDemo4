package com.dazhi.newsdemo2.model.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/31.
 */
public class News implements Serializable {
    private int nid = 0;  //新闻id
    private String title = null;
    private String summary = null; //摘要
    private String icon = null;  //图标
    private String link = null; //网络链接
    private int type; //新闻类型

    public News(int nid,String title,String summary,String icon,String url,int type){

        this.nid = nid;
        this.title = title;
        this.summary = summary;
        this.icon = icon;
        this.link = link;
        this.type = type;
    }

    public int getNid() {
        return nid;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public String getLink() {
        return link;
    }

    public int getType() {
        return type;
    }

    public String toString(){
        return "News [ nid ="+nid+",title = "+title+", summary = "+summary+", icon = "+
                icon+", link = "+link+", type = "+type+"]";
    }




}
