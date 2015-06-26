package com.jmemo.fragment;

import java.util.List;

import com.jmemo.R;
import com.jmemo.db.Word;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JGridViewAdapter extends BaseAdapter
{
	Context context;
	List<Word> wordlist;
	int clickTemp = -1;

	public JGridViewAdapter(Context context, List<Word> wordlist) {
		this.context = context;
		this.wordlist = wordlist;
	}
	
	public void setMoreData(List<Word> moreWordlist)
	{
		if(null != moreWordlist)
		{
			wordlist.addAll(moreWordlist);
		}
	}
	
	public void setSeclection(int position) {
		clickTemp = position;
		}
	
	//http://www.tuicool.com/articles/Zv2Mjy

	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.i(TAG, "getView " + position);
		ViewHolder holder = null;
		final TextView view;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, null);
			holder.textView = (TextView)convertView.findViewById(R.id.grid_word);  
			convertView.setTag(holder); 
		} else {
			holder=(ViewHolder)convertView.getTag();
		}
		holder.textView.setText(wordlist.get(position).getWord()); 
		
		if (clickTemp == position) {
			convertView.setBackgroundResource(R.drawable.item_shape);
			} else {
				convertView.setBackgroundColor(Color.TRANSPARENT);
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
    
	class ViewHolder  
    {
        public TextView textView;	  
    }
}
