package com.ianglei.jmemo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by ianglei on 2017/12/2.
 */

public class MusicService extends Service {

    MediaPlayer mediaPlayer;
    int pos = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //play();
        return new MyBind();
    }

    class MyBind extends Binder
    {
        public MusicService getService()
        {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(null == mediaPlayer)
        {
            //mediaPlayer = mediaPlayer.create(MusicService.this,null);
        }
    }
}
