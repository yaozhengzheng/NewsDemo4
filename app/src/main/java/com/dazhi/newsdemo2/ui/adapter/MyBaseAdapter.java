package com.dazhi.newsdemo2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    public Context context;
    public LayoutInflater inflater;

    public List<T> myList = new ArrayList<>();

    public MyBaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    //清除所有数据
    public void clear() {
        myList.clear();
    }

    //查找所有数据
    public List<T> getAdapterData() {
        return myList;
    }

    //自己写的方法
    public void appendData(T t, boolean isClearOld) {
        if (t == null) {
            return;
        }
        if (isClearOld) {
            myList.clear();
        }
        myList.add(t);

    }

    //添加多条数据
    public void appendData(ArrayList<T> alist, boolean isClearOld) {
        if (alist == null) {
            return;
        }
        if (isClearOld) {
            myList.clear();
        }
        myList.addAll(alist);
    }

    //添加一条记录到第一条
    public void appendDataTop(T t, boolean isClearOld) {
        if (t == null) {
            return;
        }
        if (isClearOld) {
            myList.clear();
        }
        myList.add(0, t);

    }

    //添加多条数据到顶部
    public void appendDataTop(ArrayList<T> alist, boolean isClearOld) {
        if (alist == null) {
            return;
        }
        if (isClearOld) {
            myList.clear();
        }
        myList.addAll(0, alist);
    }

    //刷新适配器
    public void update() {
        this.notifyDataSetChanged();
    }

    //重写baseAdapter的四个方法
    @Override
    public int getCount() {
        if (myList != null) {
            return myList.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getMyView(position, convertView, parent);
    }

    //作为预留方法，定义为抽象方法，要求子类继承该基础类时，必须重写该方法
    public abstract View getMyView(int position, View convertView, ViewGroup parent);
}
