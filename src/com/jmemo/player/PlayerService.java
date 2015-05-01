package com.jmemo.player;

import java.io.IOException;
import java.util.LinkedList;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PlayerService extends Service implements Runnable, MediaPlayer.OnCompletionListener
{
	public static MediaPlayer mMediaPlayer = null;  
    // 是否单曲循环  
    private static boolean isLoop = false;  
    // 用户操作  
    private int MSG;  
	

    @Override  
    public void onCreate() {  
        super.onCreate();  
        if (mMediaPlayer != null) {  
            mMediaPlayer.reset();  
            mMediaPlayer.release();  
            mMediaPlayer = null;  
        }  
        mMediaPlayer = new MediaPlayer();  
        /* 监听播放是否完成 */  
        mMediaPlayer.setOnCompletionListener(this);  
    }  
  
    @Override  
    public void onDestroy() {  
        super.onDestroy();  
  
        if (mMediaPlayer != null) {  
            mMediaPlayer.stop();  
            mMediaPlayer.release();  
            mMediaPlayer = null;  
        }  
  
        System.out.println("service onDestroy");  
    }  
    /*启动service时执行的方法*/  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        /*得到从startService传来的动作，后是默认参数，这里是我自定义的常量*/  
        MSG = intent.getIntExtra("MSG", PlayerConstants.PLAY);
        if (MSG == PlayerConstants.PLAY) {  
            playMusic();  
        }  
        if (MSG == PlayerConstants.PAUSE) {  
            if (mMediaPlayer.isPlaying()) {// 正在播放  
                mMediaPlayer.pause();// 暂停  
            } else {// 没有播放  
                mMediaPlayer.start();  
            }  
        }  
  
        return super.onStartCommand(intent, flags, startId);  
    }  
  
    @SuppressWarnings("static-access")  
    public void playMusic(LinkedList<String> playlist) {  
        try {  
            /* 重置多媒体 */  
            mMediaPlayer.reset();  
            /* 读取mp3文件 */  
            mMediaPlayer.setDataSource("");  
//          Uri uri = Uri.parse(MUSIC_PATH+TestMediaPlayer.mMusicList.get(TestMediaPlayer.currentListItme));  

//          mMediaPlayer.create(PlayerService.this,uri);  
            /* 准备播放 */  
            mMediaPlayer.prepare();  
            /* 开始播放 */  
            mMediaPlayer.start();  
            /* 是否单曲循环 */  
            mMediaPlayer.setLooping(isLoop);  

            new Thread(this).start();  
        } catch (IOException e) {  
        }  
  
    }  
  
    // 刷新进度条  
    @Override  
    public void run() {  
  
    }  

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{
		// TODO Auto-generated method stub
		
	}

}
