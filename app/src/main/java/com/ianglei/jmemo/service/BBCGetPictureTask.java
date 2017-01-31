package com.ianglei.jmemo.service;

import android.os.AsyncTask;

import com.ianglei.jmemo.JMemoApplication;
import com.ianglei.jmemo.bean.Listening;
import com.ianglei.jmemo.utils.HttpAgent;

/**
 * Created by j00255628 on 2015/11/19.
 */
public class BBCGetPictureTask extends AsyncTask<Listening, Integer, String> {

    String result;
    Listening item;

    @Override
    protected String doInBackground(Listening... params) {
        item = params[0];
        result = HttpAgent.downloadResource(params[0].getCoverpath());

        return result;
    }

    @Override
    protected void onPostExecute(String result)
    {
        item.setCoverpath(result);
        JMemoApplication.getDataSource().updateCoverImgPath(item);
    }
}
