package com.ianglei.jmemo.service;

import android.os.AsyncTask;

import com.ianglei.jmemo.JMemoApplication;
import com.ianglei.jmemo.bean.Listening;
import com.ianglei.jmemo.utils.L;

import java.util.List;

public class SaveNewsListTask extends AsyncTask<Void, Void, Void> {

    public static String TAG = SaveNewsListTask.class.getSimpleName();

    private List<Listening> newsList;

    public SaveNewsListTask(List<Listening> newsList) {
        this.newsList = newsList;
    }

    @Override
    protected Void doInBackground(Void... params) {
        saveNewsList(newsList);
        return null;
    }

    private void saveNewsList(List<Listening> newsList) {
//        JMemoApplication.getDataSource().insertOrUpdateNewsList(
//                new GsonBuilder().create().toJson(newsList, Constants.Type.newsListType));
        for(Listening item : newsList)
        {
            L.i(TAG, "Save: " + item.getTitle());
            JMemoApplication.getDataSource().insertOrUpdateNewsList(item);

            new BBCGetPictureTask().execute(item);
        }
    }
}