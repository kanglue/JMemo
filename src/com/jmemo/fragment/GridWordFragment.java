package com.jmemo.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.jmemo.R;
import com.jmemo.db.Sample;
import com.jmemo.db.VocabularyHelper;
import com.jmemo.db.Word;
import com.jmemo.player.PlayMusicService;
import com.jmemo.util.CommonUtil;

public class GridWordFragment extends Fragment implements OnClickListener{
	
	public static final String TAG = "GridWordFragment";
	private GridView gridView;
	private TextView textWord;
	private TextView textPronounce;
	private TextView textTense;
	private TextView textTranslation;
	private TextView textSentence1;
	private TextView textSentence2;
	private TextView textSentence3;
	private TextView textSentence4;
	private TextView textSentence5;
	private Button fontButton, nextButton;   //上一曲，下一曲，按钮
	private Button playButton;  //播放，暂停按钮
	private boolean isPlay;  //是否在播放
	private Intent intent;
	private List<Word> wordlist = null;
	private HashMap<String, String[]> sentenceMap;
	private int index;  
	private WordListAdapter wordListAdapter;
	
	Handler mHandler;
	
	final String MEDIA_BROCASE_ACTION = "com.jmemo.JMemoActivity";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View contextView = inflater.inflate(R.layout.grid_word_fragment, container,
				false);
		init(contextView);
		
		PlayMusicService.gridFragment = this;

		listererTelephony(); //来电话监听
		
		final Handler myHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0x01)
				{
					index = msg.getData().getInt("index");
					Word word = wordlist.get(index);
					textWord.setText(word.getWord());
					textPronounce.setText(word.getPronounce());
					textTranslation.setText(word.getTranslation());
					textTense.setText(word.getTense());
					
					List<Sample> samples = word.getSamples();
					if(samples.size() > 0)
					{
						Sample sample = samples.get(0);
						textSentence1.setText(sample.getSentence());
					}
					if(samples.size() > 0)
					{
						Sample sample = samples.get(1);
						textSentence2.setText(sample.getSentence());
					}
					if(samples.size() > 0)
					{
						Sample sample = samples.get(2);
						textSentence3.setText(sample.getSentence());
					}
					if(samples.size() > 0)
					{
						Sample sample = samples.get(3);
						textSentence4.setText(sample.getSentence());
					}
					if(samples.size() > 0)
					{
						Sample sample = samples.get(4);
						textSentence5.setText(sample.getSentence());
					}
				}				
			}
		};

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Word word = wordlist.get(position);
				Log.i(TAG, word.getWord());
				wordListAdapter.setSeclection(position);
				wordListAdapter.notifyDataSetChanged();
				
				Message msg = new Message();
				msg.what = 0x01;
				Bundle bundle = new Bundle();
				bundle.putInt("index", position);
				msg.setData(bundle);
				myHandler.sendMessage(msg);
				
				//播放列表
//				ArrayList<String> playlist = new ArrayList<String>();
//				playlist.add(CommonUtil.getRootFilePath() + word.getVoicepath());
//				
//				for(int i = 0; i < word.getSamples().size(); i++)
//				{
//					playlist.add(CommonUtil.getRootFilePath() + word.getSamples().get(i).getMp3Path());
//				}
				
				startPlay(position);

			}
		});
		
		return contextView;		
	}
	
	private void init(View contextView)
	{
		gridView = (GridView)contextView.findViewById(R.id.gridword);
		gridView.setSelector(R.drawable.item_shape);
		textWord = (TextView)contextView.findViewById(R.id.text_word);
		textPronounce = (TextView)contextView.findViewById(R.id.text_pronounce);		
		textTense = (TextView)contextView.findViewById(R.id.text_tense);
		textTranslation = (TextView)contextView.findViewById(R.id.text_translation);
		textSentence1 = (TextView)contextView.findViewById(R.id.text_sentence1);
		textSentence2 = (TextView)contextView.findViewById(R.id.text_sentence2);
		textSentence3 = (TextView)contextView.findViewById(R.id.text_sentence3);
		textSentence4 = (TextView)contextView.findViewById(R.id.text_sentence4);
		textSentence5 = (TextView)contextView.findViewById(R.id.text_sentence5);
		fontButton = (Button) contextView.findViewById(R.id.font);
		fontButton.setOnClickListener(this);
		nextButton = (Button) contextView.findViewById(R.id.next);
		nextButton.setOnClickListener(this);
		playButton = (Button) contextView.findViewById(R.id.play);
		playButton.setOnClickListener(this);
		
		wordlist = getData();
		
		wordListAdapter = new WordListAdapter(getActivity().getApplicationContext(), wordlist);

		gridView.setAdapter(wordListAdapter);
	}
	
	public void setIsplay(boolean isPlay) {  //改变当前播放状态数据标志
		if (isPlay) {
			this.isPlay = true;
		} else {
			this.isPlay = false;
		}
	}
	
	public void refreshUI() {     //音乐初始化 后，调用这里

		playButton.setBackgroundResource(R.drawable.player_pause_highlight);//设置播放按钮的背景

	}

	
	//监听来电
	private void listererTelephony() {
		TelephonyManager telManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		telManager.listen(new MobliePhoneStateListener(),
				PhoneStateListener.LISTEN_CALL_STATE);
	}
	private class MobliePhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				PlayMusicService.start();
				isPlay = true;
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				PlayMusicService.pause();
				isPlay = false;
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				PlayMusicService.pause();
				isPlay = false;
				break;
			default:
				break;
			}
		}
	}
	
	
	private void startPlay(int index) {
		PlayMusicService.stop();//播放之前，先停止播放，不管有没有在播放，都先停止
		intent = new Intent();
		intent.putExtra("index", index);
		intent.putParcelableArrayListExtra("mp3", (ArrayList<? extends Parcelable>) wordlist);
		intent.setClass(getActivity(), PlayMusicService.class); //启动service，播放音乐
		getActivity().startService(intent);
	}
	

	
	
	
	////下面是取数据
	
	private ArrayList<Word> getData() {
		return VocabularyHelper.getWordListFirst();
	}
	
	class WordListAdapter extends BaseAdapter {
		Context context;
		List<Word> wordlist;
		int clickTemp = -1;

		public WordListAdapter(Context context, List<Word> wordlist) {
			this.context = context;
			this.wordlist = wordlist;
		}
		
		public void setSeclection(int position) {
			clickTemp = position;
			}
		
		//http://www.tuicool.com/articles/Zv2Mjy

		public View getView(int position, View convertView, ViewGroup parent) {
			final TextView view;
			if (convertView == null) {
				view = new TextView(context);
				view.setLayoutParams(new GridView.LayoutParams(CommonUtil.getScreenWidth(context) / 4, 50));
				//view.setBackgroundColor(Color.YELLOW);
				view.setText(wordlist.get(position).getWord());
			} else {
				view = (TextView) convertView;
			}
			
			if (clickTemp == position) {
				view.setBackgroundResource(R.drawable.item_shape);
				} else {
				view.setBackgroundColor(Color.TRANSPARENT);
				}
			

			return view;
		}

		public int getCount() {
			return wordlist.size();
		}

		public Object getItem(int position) {
			return wordlist.get(position);
		}

		public long getItemId(int position) {
			return position;
		}
        
        
	}



	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.play) {     //播放按钮
			play();
		} else if (v.getId() == R.id.next) { //下一去
			PlayMusicService.playNextMusic();
			index += 1;
			if (index >= wordlist.size()) {
				index = 0;
			}
		} else if (v.getId() == R.id.font) {//上一曲
			index -= 1;
			PlayMusicService.playFontMusic();
			if (index < 0) {
				index = wordlist.size() - 1;
			}
		}
	}
	
	private void play() {
		if (isPlay) {
			playButton.setBackgroundResource(R.drawable.player_play_highlight);
		} else {
			playButton.setBackgroundResource(R.drawable.player_pause_highlight);
		}
		PlayMusicService.play();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (!isPlay && intent != null) {
			getActivity().stopService(intent);
		}
		if (PlayMusicService.isPause) {
			PlayMusicService.stop();
		}
	}
}