package com.jmemo.fragment;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jmemo.R;
import com.jmemo.db.VocabularyHelper;
import com.jmemo.db.Word;

public class JGridViewAdapter extends BaseAdapter {
	private enum WORD_COLOR {
		First(0x563624, 1), Second(0x1b315e, 2), Third(0x8552a1, 3), Forth(
				0x5c7a29, 4), Fifth(0xc37e00, 5);

		// 成员变量
		private int name;
		private int index;

		// 构造方法
		private WORD_COLOR(int name, int index) {
			this.name = name;
			this.index = index;
		}

		public int getIndex() {
			return index;
		}

		public static int getColor(int index) {
			for (WORD_COLOR c : WORD_COLOR.values()) {
				if (c.getIndex() == index) {
					return c.name;
				}
			}
			return 0;
		}
	}

	Context context;
	List<Word> wordlist;
	int clickTemp = -1;

	public JGridViewAdapter(Context context, List<Word> wordlist) {
		this.context = context;
		this.wordlist = wordlist;
	}

	public void setMoreData(List<Word> moreWordlist) {
		if (null != moreWordlist) {
			wordlist.addAll(moreWordlist);
		}
	}

	public void setSeclection(int position) {
		clickTemp = position;
	}

	// http://www.tuicool.com/articles/Zv2Mjy

	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.i(TAG, "getView " + position);
		ViewHolder holder = null;
		final TextView view;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.grid_item, null);
			holder.textView = (TextView) convertView
					.findViewById(R.id.grid_word);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String word = wordlist.get(position).getWord();
		
		int time = VocabularyHelper.queryLearnedTime(word);
		holder.textView.setBackgroundColor(WORD_COLOR.getColor(time));
		holder.textView.setText(word);

		if (clickTemp == position) {
			VocabularyHelper.updateLearnedTime(word, time + 1);
			//粗体
//			convertView.setBackgroundResource(R.drawable.item_shape);
//			TextPaint tp = ((ViewHolder) convertView.getTag()).textView
//					.getPaint();
//			tp.setFakeBoldText(true);
		} else {
			//convertView.setBackgroundColor(Color.TRANSPARENT);
		}

		return convertView;
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

	class ViewHolder {
		public TextView textView;
	}
}
