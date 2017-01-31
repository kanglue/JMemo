package com.ianglei.jmemo.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.ianglei.jmemo.JMemoApplication;
import com.ianglei.jmemo.bean.Listening;
import com.ianglei.jmemo.fragment.BBC6MinFragment;
import com.ianglei.jmemo.utils.L;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by j00255628 on 2016/12/17.
 */

public class BBC6MinParser implements Runnable{

    private Handler handler;

    public BBC6MinParser(Handler handler)
    {
        this.handler = handler;
    }

    public void run()
    {
        L.i("Start to get bbc web site");
        List<Listening> resultNewsList = new ArrayList<>();

        String httpurl = "http://www.bbc.co.uk/learningenglish/english/features/6-minute-english";

        Document doc;

        try
        {
            long begin = System.currentTimeMillis();
            //.userAgent("Chrome/36.0.1985.143")
            doc = Jsoup.connect(httpurl).timeout(45000).get();

            //概要描述
            int itemcount = doc.select("div.details > p").size();
            L.d("抓取" + itemcount + "条数据中");

            for(int k = 0; k < itemcount; k++)
            {
                //图片
                Element jpgElement;
                if(k == 0)
                {
                    jpgElement = doc.select("img[data-pid][width=976]").get(k);
                    /*
                    //第一条相当于最后更新时间
                    Element dateElement = doc.select("div.details > h3 > b").get(k);
                    String dateText = dateElement.text();

                    SharedPreferences pref = JMemoApplication.getApplication().getSharedPreferences("jmemo", Context.MODE_PRIVATE);
                    String updatedDate = pref.getString("update_date?", "");
                    L.d("本地记录最后更新"+updatedDate);

                    if(dateText.equalsIgnoreCase(updatedDate))
                    {
                        L.i("自上次刷新后未更新");
                        break;
                    }
                    else
                    {
                        L.i("网站最后更新"+dateText);
                        SharedPreferences.Editor editor = JMemoApplication.getApplication().getSharedPreferences("jmemo", Context.MODE_PRIVATE).edit();
                        editor.putString("update_date?", dateText);
                        editor.commit();
                    }*/
                }
                else
                {
                    jpgElement = doc.select("img[data-pid][width=624]").get(k-1);
                }
                String jpgURL = jpgElement.attr("src");
                //Log.i(TAG, "JPG: " + jpgURL);

                //图片pid作为唯一标识
                String pid = jpgElement.attr("data-pid");
                //Log.i(TAG, "Pid: " + pid);

                if(!JMemoApplication.getDataSource().isListeningExist(pid))
                {
                    //Episode 150212
                    Element dateElement = doc.select("div.details > h3 > b").get(k);
                    String dateText = dateElement.text();
                    //Log.i(TAG, "Date: " + dateText);

                    //主题
                    Element ahrefElement = doc.select("h2 > a[href*=/6-minute-english/]").get(k);
                    String titleText = ahrefElement.text();
                    //Log.i(TAG, "Title: " + titleText);

                    //拼接绝对地址 learningenglish/english/features/6-minute-english/ep-150205
                    String urlText = "http://www.bbc.co.uk" + ahrefElement.attr("href");
                    //Log.i(TAG, "URL: " + urlText);

                    //概要描述
                    Element descElement = doc.select("div.details > p").get(k);
                    String descText = descElement.text();
                    //Log.i(TAG, "Desc: " + descText);

                    Listening item = new Listening();
                    item.setId(pid);
                    item.setCoverpath(jpgURL);
                    item.setCategory(6);
                    item.setDescribe(descText);
                    item.setLink(urlText);
                    item.setLearnedtimes(0);
                    item.setTitle(titleText);
                    item.setUpdated(dateText);

                    resultNewsList.add(item);
                }
            }
            long end = System.currentTimeMillis();
            L.d("HTML解析耗时" + (end - begin) + "ms");

        } catch (IOException e)
        {
            L.e("BBC请求出错");
            // TODO Auto-generated catch block
            e.printStackTrace();
            handler.sendEmptyMessage(1);
        }

        Message msg = Message.obtain();
        msg.obj = resultNewsList;
        handler.sendMessage(msg);
    }
}
