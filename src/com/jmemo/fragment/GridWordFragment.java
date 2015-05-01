package com.jmemo.fragment;


import java.util.LinkedList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.jmemo.R;
import com.jmemo.db.Sample;
import com.jmemo.db.VocabularyHelper;
import com.jmemo.db.Word;
import com.jmemo.util.CommonUtil;

public class GridWordFragment extends Fragment {
	
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
	private LinkedList<Word> wordlist = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View contextView = inflater.inflate(R.layout.grid_word_fragment, container,
				false);
		
		gridView = (GridView)contextView.findViewById(R.id.gridword);
		textWord = (TextView)contextView.findViewById(R.id.text_word);
		textPronounce = (TextView)contextView.findViewById(R.id.text_pronounce);
		textTense = (TextView)contextView.findViewById(R.id.text_tense);
		textTranslation = (TextView)contextView.findViewById(R.id.text_translation);
		textSentence1 = (TextView)contextView.findViewById(R.id.text_sentence1);
		textSentence2 = (TextView)contextView.findViewById(R.id.text_sentence2);
		textSentence3 = (TextView)contextView.findViewById(R.id.text_sentence3);
		textSentence4 = (TextView)contextView.findViewById(R.id.text_sentence4);
		textSentence5 = (TextView)contextView.findViewById(R.id.text_sentence5);
		
		wordlist = getData();
		
		WordListAdapter wordListAdapter = new WordListAdapter(getActivity().getApplicationContext(), wordlist);

		gridView.setAdapter(wordListAdapter);
		
		final Handler myHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0x01)
				{
					int index = msg.getData().getInt("index");
					Word word = wordlist.get(index);
					textWord.setText(word.getWord());
					textPronounce.setText(word.getPronounce());
					textTranslation.setText(word.getTranslation());
					textTense.setText(word.getTense());
					
					LinkedList<Sample> samples = word.getSamples();
					if(samples.size() > 0)
					{
						Sample sample = samples.removeFirst();
						textSentence1.setText(sample.getSentence());
					}
					if(samples.size() > 0)
					{
						Sample sample = samples.removeFirst();
						textSentence2.setText(sample.getSentence());
					}
					if(samples.size() > 0)
					{
						Sample sample = samples.removeFirst();
						textSentence3.setText(sample.getSentence());
					}
					if(samples.size() > 0)
					{
						Sample sample = samples.removeFirst();
						textSentence4.setText(sample.getSentence());
					}
					if(samples.size() > 0)
					{
						Sample sample = samples.removeFirst();
						textSentence5.setText(sample.getSentence());
					}
				}				
			}
		};

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(TAG, wordlist.get(position).getWord());
				
				Message msg = new Message();
				msg.what = 0x01;
				Bundle bundle = new Bundle();
				bundle.putInt("index", position);
				msg.setData(bundle);
				myHandler.sendMessage(msg);
			}
		});
		
		return contextView;		
	}

	
	
	private LinkedList<Word> getData() {
		return VocabularyHelper.getWordListFirst();
	}
	
	class WordListAdapter extends BaseAdapter {
		Context context;
		LinkedList<Word> wordlist;

		public WordListAdapter(Context context, LinkedList<Word> wordlist) {
			this.context = context;
			this.wordlist = wordlist;
		}
		
		//http://www.tuicool.com/articles/Zv2Mjy

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final TextView view;
			if (convertView == null) {
				view = new TextView(context);
				view.setLayoutParams(new GridView.LayoutParams(CommonUtil.getScreenWidth(context) / 4, 50));
				view.setBackgroundColor(Color.YELLOW);
				view.setText(wordlist.get(position).getWord());
			} else {
				view = (TextView) convertView;
			}

			return view;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return wordlist.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return wordlist.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
        
        
	}
}
