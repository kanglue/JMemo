package com.jmemo.player;

import java.util.List;
import java.util.Random;

import android.R.integer;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.jmemo.db.Word;
import com.jmemo.fragment.GridWordFragment;
import com.jmemo.util.CommonUtil;
import com.jmemo.util.NotiFicationUtil;
/**
 * 播放音乐的service
 * @author hck
 *
 */
public class PlayMusicService extends Service implements
		OnCompletionListener, OnPreparedListener,OnErrorListener {
	public static GridWordFragment gridFragment;   //播放界面activity对象，用于调用实现更新UI
	private static boolean isPlay;   //判断是否在播放中的标记
	private static MediaPlayer player;   //MediaPlayer对象，播放音乐
	private static int flag = 0; 
	public static boolean isPause;   //是否暂停标志
	private static PlayMusicService playMusicService;  //当前类对象，用于播放界面调用
	public static List<Word> wordList;  //存放歌曲的集合
	public static int index;   //音乐播放id
	private static String playUrl;   //音乐的播放url
    private boolean isRandom;   //是否随机播放
    private static String[] sentences;
    private static int indexOf;
    
    private static final String TAG = "PlayMusicService";
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		player = new MediaPlayer();   //初始化MediaPlayer对象
		playMusicService = this;  //初始化playMusicServer对象
	}
	@Override
	public void onStart(Intent intent, int startId) {
		init(intent);  //初始化一些数据
		
		beginToPlay(index);		
	}
	
	private static void beginToPlay(int index)
	{
		Word word = wordList.get(index);
		int isamples = word.getSamples().size();
		sentences = new String[isamples + 1];
		sentences[0] = CommonUtil.getRootFilePath() + word.getVoicepath();
		for(int i = 1; i <= isamples; i++)
		{
			sentences[i] = CommonUtil.getRootFilePath() + word.getSample(i - 1).getMp3Path();			
		}

		startPlay(sentences[0]);  //开始播放视频

	}
	
	private void init(Intent intent) { //初始化数据，从intent获取播放界面传过来的数据
		
		index = intent.getIntExtra("index", -1);
		wordList = intent.getParcelableArrayListExtra("mp3");
		isRandom=intent.getBooleanExtra("isRandom", false);   //获取Playmusicactivity传过来的isRandom，用于是否随机播放
		indexOf = 0;
	}
	private static void startPlay(String url) { //开始播放音乐
		
		//Log.i(TAG, "index: " + index);
		//Log.i(TAG, "indexOf: " + indexOf);
		
		try {
			player.reset();   //恢复原始状态
			playUrl = url; //获取当前播放的歌曲的url
			player.setDataSource(playUrl);   //把播放地址丢给播放器
			player.prepareAsync();   //异步初始化
			player.setOnPreparedListener(playMusicService);  //监听初始化
			player.setOnCompletionListener(playMusicService); //监听是否播放完了
			player.setOnErrorListener(playMusicService);   //监听出错信息
		} catch (Exception e) {
             Toast.makeText(playMusicService, "错误: "+e, Toast.LENGTH_LONG).show(); //出错时候，提示
             Log.e("hck", "PlayMusicserver startPlay: "+e.toString());
		}
	}


	@Override
	public void onPrepared(MediaPlayer mp) {  //初始化播放回调方法
		player.start();  //初始化完成，调用start，开始播放音乐
		isPlay = true;
		isPause = false;
		refreshUI();   //更新PlayMusicActivity界面

		showNotifi();  //在通知栏显示
	}
	public static void refreshUI() { 
		if (player != null && gridFragment != null) {
			gridFragment.refreshUI();
			gridFragment.setIsplay(true);
		}
	}
	@Override
	public void onCompletion(MediaPlayer mp) { //一首播放完毕，继续下手
		if (isRandom) { //是否为随机播放
			index = new Random().nextInt(wordList.size() - 1);
		}
		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		//playNextMusic();
		playRepeat();
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

	public static void play() {  //播放
		if (player != null) {
			if (isPlay) { //如果在播放，则暂停
				player.pause();
				isPlay = false;
				isPause = true;
				gridFragment.setIsplay(false);
				hidenNotifi(); //应藏通知栏
             
			} else {  //播放
				if (player != null && !player.isPlaying()) {
					player.start();
					gridFragment.setIsplay(true);
					isPlay = true;
					isPause = false;
					showNotifi();
				}
			}
		}
	}

	public static void pause() { //暂停播放
		if (player != null) {
			player.pause();
			isPlay = false;
			isPause = true;
			if (gridFragment != null) {
				gridFragment.setIsplay(false);
			}
			hidenNotifi();
		}
	}

	public static void start() { //开始播放

		if (player != null && !player.isPlaying()) {
			player.start();
			isPlay = true;
			isPause = false;
			if (gridFragment != null) {
				gridFragment.setIsplay(true);
			}
			showNotifi();
		}
	}

	public static void seekTo(int size) {  //拖动时候，快进，快退

		if (player != null && isPlay) {
			player.seekTo(size);
		}
	}

	public static void playRepeat() { //播放下一曲音乐
		if (isPlay) {
			player.stop();
		}
		reset();
		startPlay(sentences[indexOf]);
	}

	public static void playNextMusic() { //播放下一曲音乐
		if (isPlay) {
			player.stop();
		}
		reset();
		indexOf++; //音乐id+1
		if (indexOf >= sentences.length) {
			indexOf = 0;
		}
		startPlay(sentences[indexOf]);
		showNotifi();
	}

	public static void playFontMusic() { //上一曲歌曲
		
		if (isPlay) {
			player.stop();
		}
		reset();
		indexOf -= 1;
		if (indexOf < 0) {
			indexOf = sentences.length - 1;
		}
		startPlay(sentences[indexOf]);
		showNotifi();
	}

	private static void reset() { //移除相应线程
		isPlay = false;
		flag = 0;
	}

	private static void showNotifi() {  //通知栏显示
		if (playUrl != null) {
			NotiFicationUtil.showNotification(playMusicService,
					wordList.get(index).getWord());
		} else {
			NotiFicationUtil.showNotification(playMusicService,
					wordList.get(index).getWord());
		}
	}

	private static void hidenNotifi() {
		NotiFicationUtil.clearNotification(playMusicService);
	}

	@Override
	public void onDestroy() {  //销毁时候的一些数据清理
		super.onDestroy();

		if (isPause || !isPlay) {
			if (player != null) {
				player.stop();
				player.release();
				player = null;
				index = 0;
				this.stopSelf();
				flag = 0;
				wordList = null;
				index = 0;
			}
		}
	}

	public static void stop() { //停止了，移除线程，恢复一些初始数据
		if (player != null) {
			player.stop();
			player.release();
			player = null;
			flag = 0;
			playMusicService.stopSelf();
		}
	}


}
