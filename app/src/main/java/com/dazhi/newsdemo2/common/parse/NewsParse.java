package com.dazhi.newsdemo2.common.parse;

import com.dazhi.newsdemo2.common.bean.NewsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/1.
 */
public class NewsParse {
    static String jsonData = null;
    private static ArrayList<NewsBean.DataBean>  jsonList = new ArrayList<>();

    public  static ArrayList<NewsBean.DataBean> parseNewsJson(String json){

        jsonData = json;
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                NewsBean.DataBean dataBean = new NewsBean.DataBean();
                JSONObject object = dataArray.getJSONObject(i);
                dataBean.setSummary(object.getString("summary"));
                dataBean.setIcon(object.getString("icon"));
                dataBean.setStamp(object.getString("stamp"));
                dataBean.setTitle(object.getString("title"));
                dataBean.setNid(object.getInt("nid"));
                dataBean.setLink(object.getString("link"));
                dataBean.setType(object.getInt("type"));

                jsonList.add(dataBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonList;
    }
}
