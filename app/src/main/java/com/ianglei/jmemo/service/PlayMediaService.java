package com.ianglei.jmemo.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

import com.ianglei.jmemo.activity.DetailActivity;
import com.ianglei.jmemo.utils.L;
import com.ianglei.jmemo.utils.NotificationUtil;

public class PlayMediaService extends Service implements
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
		OnInfoListener, OnErrorListener, MediaPlayer.OnSeekCompleteListener {

	private MediaPlayer player;
	public DetailActivity detailActivity;   //播放界面activity对象，用于调用实现更新UI
	private boolean isPlay;   //判断是否在播放中的标记

	private UpdateSeekBar updateSeekBar;  //更新seekbar的一个线程对象
	//private static updateSingWords updateWords;  //更新歌词的一个线程
	private long wordTime;   //当前播放时间，更新歌词用
	private int flag = 0;
	public boolean isPause;   //是否暂停标志
	//private static PlayMusicService playMusicService;  //当前类对象，用于播放界面调用
	private Handler handler2 = new Handler();  //更新歌词的handler
	public int playMusicId;   //音乐播放id
	private String playTitle;
	private String playUrl;   //音乐的播放url
	private String type;    //是本地还是在线音乐标记
    private boolean isRandom;   //是否随机播放

	public void onStopToBack()
	{
		L.d("onStopToBack");
//		int gap = (int) ((30 * 1000f) / player.getDuration() * 100);
//		detailActivity.backSeekBar(gap);
	}

	@Override
	public IBinder onBind(Intent intent) {
		L.d("onBind called");
		return new PlayerBinder();
	}

	@Override
	public void onSeekComplete(MediaPlayer mp) {
		mp.start();
	}

	public class PlayerBinder extends Binder {
		public PlayMediaService getPlayMediaService()
		{
			L.d("getPlayMediaService called");
			return PlayMediaService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		L.d("onCreate called");
		player = new MediaPlayer();   //初始化MediaPlayer对象
		player.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
		player.setOnBufferingUpdateListener(this); //监听缓冲数据
		player.setOnPreparedListener(this);  //监听初始化
		player.setOnCompletionListener(this); //监听是否播放完了
		player.setOnErrorListener(this);   //监听出错信息
		player.setOnInfoListener(this);   //监听播放过程中，返回的信息
		player.setOnSeekCompleteListener(this);
//			player.setOnStopToBackListener(playMusicService);
		updateSeekBar = new UpdateSeekBar();   //初始化更新seekbar的线程
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		L.d("onStartCommand called");
		return super.onStartCommand(intent, flags, startId);
	}
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		if(null != intent) {
//			init(intent);  //初始化一些数据
//		}
//		startPlay();  //开始播放视频
//		return super.onStartCommand(intent, flags, startId);
//	}
	
//
//	private void init(Intent intent) { //初始化数据，从intent获取播放界面传过来的数据
//
//
//
//	}
//	private void startPlay() { //开始播放音乐
//
//	}
	Handler seekbarHandler = new Handler() {    //更新seekbar和播放时间的handler
		public void handleMessage(android.os.Message msg) {
			if (detailActivity != null && player != null && isPlay) { 
				detailActivity.updateSeekBar(player.getCurrentPosition());
				detailActivity.updateTime(player.getCurrentPosition());
			}
		};
	};

	class UpdateSeekBar implements Runnable {  //更新seekbar和时间的线程
		@Override
		public void run() {
			seekbarHandler.sendEmptyMessage(1);
			seekbarHandler.postDelayed(updateSeekBar, 1000);
		}
	}

	public void setActivity(DetailActivity activity)
	{
		detailActivity = activity;
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {  //播放过程中的一些返回信息
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:  //进入缓冲
			detailActivity.showBar();  //显示转圈圈
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:  //结束缓冲
			detailActivity.hideBar();  //隐藏圈圈
			break;
		default:
			break;
		}
		return true;
	}
	@Override
	public void onPrepared(MediaPlayer mp) {  //初始化播放回调方法
		player.start();  //初始化完成，调用start，开始播放音乐
		isPlay = true;
		isPause = false;
		refreshUI();   //更新PlayMusicActivity界面
		new Thread(updateSeekBar).start();  //启动线程，更新seekbar
		showNotifi();  //在通知栏显示
	}
	@Override
	public void onCompletion(MediaPlayer mp) { //一首播放完毕，继续下首
		playNextMusic();
	}
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {  //获取缓冲进度
		if (detailActivity != null && player != null) {
			detailActivity.updateSeekCach(mp.getDuration() * percent / 100);  //seekbar第2进度条
		}
	}
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) { //出错时候，会回调这里
		switch (what) {
			case MediaPlayer.MEDIA_ERROR_UNKNOWN :
				break;
			default:
				break;
		}
		return false;
	}


	public void refreshUI() {
		if (player != null && detailActivity != null) {
			detailActivity.refreshUI(player.getDuration());
			detailActivity.setIsplay(true);
		}
	}

	public void play(String title, String url) {
		playTitle = title;
		playUrl = url;  //获取传过来的type数据，用于判断是在线播放，还是本地本地播放。2这获取歌词不一样

		try {
			player.reset();   //恢复原始状态
			player.setDataSource(playUrl);   //把播放地址丢给播放器
			player.prepareAsync();   //异步初始化
			isPlay = true;
		} catch (Exception e) {
			L.e("PlayMediaserver play error: "+e.toString());
		}
	}

	public void seekTo(String title, String url) {
		playTitle = title;
		playUrl = url;  //获取传过来的type数据，用于判断是在线播放，还是本地本地播放。2这获取歌词不一样

		try {
			player.reset();   //恢复原始状态
			player.setDataSource(playUrl);   //把播放地址丢给播放器
			player.prepareAsync();   //异步初始化
			isPlay = true;
		} catch (Exception e) {
			L.e("PlayMediaserver play error: "+e.toString());
		}
	}

	public void play() {  //播放
		if (player != null) {
			if (isPlay) { //如果在播放，则暂停
				player.pause();
				isPlay = false;
				isPause = true;
//				detailActivity.setIsplay(false);
				hidenNotifi(); //应藏通知栏
             
			} else {  //播放
				if (player != null && !player.isPlaying()) {
					player.start();
//					detailActivity.setIsplay(true);
					isPlay = true;
					isPause = false;
					showNotifi();
				}
			}
		}
	}

	//	public void start() { //开始播放
//
//		if (player != null && !player.isPlaying()) {
//			player.start();
//			isPlay = true;
//			isPause = false;
//			if (detailActivity != null) {
//				detailActivity.setIsplay(true);
//			}
//			showNotifi();
//		}
//	}

	public void pause() { //暂停播放
		if (player != null) {
			player.pause();
			isPlay = false;
			isPause = true;
			if (detailActivity != null) {
				detailActivity.setIsplay(false);
			}
			hidenNotifi();
		}
	}



	public boolean isPlaying()
	{
		return isPlay;
	}

	public void seekTo(int size) {  //拖动时候，快进，快退

		if (player != null && isPlay) {
			L.d("SeekTo: " + size);
			player.seekTo(size);
		}
	}

	public void start()
	{
		if(null != player)
		{
			player.start();
		}
	}



	public void playNextMusic() { //播放下一曲音乐
		if (isPlay) {
			player.stop();
		}
		reset();
		//startPlay();
		showNotifi();
		//detailActivity.resetDate();
	}

	public void playFontMusic() { //上一曲歌曲
		
		if (isPlay) {
			player.stop();
		}
		reset();
		//startPlay();
		showNotifi();
		//detailActivity.resetDate();
	}

	private void reset() { //移除相应线程
		isPlay = false;
		//handler2.removeCallbacks(updateWords);
		seekbarHandler.removeCallbacks(updateSeekBar);
//		flag = 0;
	}

	private void showNotifi() {  //通知栏显示
		if (playUrl != null) {
			NotificationUtil.showNotification(this,playTitle);
		} else {
			NotificationUtil.showNotification(this, playTitle);
		}
	}

	private void hidenNotifi() {
		NotificationUtil.clearNotification(this);
	}

	@Override
	public void onDestroy() {  //销毁时候的一些数据清理
		super.onDestroy();

		if (isPause || !isPlay) {
			if (player != null) {
				player.stop();
				player.release();
				player = null;
				playMusicId = 0;
				seekbarHandler.removeCallbacks(updateSeekBar);
				this.stopSelf();
				flag = 0;
				wordTime = 0;
				playMusicId = 0;
			}
		}
	}

	public void stop() { //停止了，移除线程，恢复一些初始数据
		if (player != null) {
			player.stop();
			player.release();
			player = null;
			seekbarHandler.removeCallbacks(updateSeekBar);
			flag = 0;
			wordTime = 0;
			stopSelf();
		}
	}

}
