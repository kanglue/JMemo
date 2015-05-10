package com.jmemo.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jmemo.R;
import com.jmemo.db.Phrase;

public class NewPhraseAdapter extends BaseAdapter
{
	private LayoutInflater mInflater;
	private ArrayList<Phrase> lists = new ArrayList<Phrase>();
	private ListView listView;
	private static final String TAG = "ListAdapter";
	private Context context;
	private Handler handler;
	
	public NewPhraseAdapter(Context context, ListView listView, Handler handler) {
		mInflater = LayoutInflater.from(context);
		this.listView = listView;
		this.context = context;
		this.handler = handler;
		
	}
	
	public void addItem(String phrase, int category, String translation)
	{
		Phrase item = new Phrase();
		item.setPhrase(phrase);
		item.setCategory(category);
		item.setTranslation(translation);
		lists.add(item);
		notifyDataSetChanged();
	}
	
	public void addItem(ArrayList<Phrase> list)
	{
		this.lists.addAll(list);
		notifyDataSetChanged();
	}
	
    public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return lists.size();
	}
	@Override
	public Object getItem(int position)
	{
		if (position >= getCount()) {
			return null;
		}
		return lists.get(position);
	}
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.phrase_item_adapter, null);
			viewHolder = new ViewHolder();
			viewHolder.phraseTextView = (TextView) convertView
					.findViewById(R.id.phraseStr);
			viewHolder.translationTextView = (TextView) convertView.findViewById(R.id.translationStr);
			viewHolder.listitem = (RelativeLayout)convertView.findViewById(R.id.listitem);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
        viewHolder.listitem.setBackgroundColor(position % 2 == 0 ? convertView.getContext()
                .getResources()
                .getColor(R.color.white) : convertView.getContext().getResources().getColor(R.color.item_bg));
		
		Phrase item = lists.get(position);
		viewHolder.phraseTextView.setText(item.getPhrase());
		viewHolder.translationTextView.setText(item.getTranslation());
		
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView phraseTextView;
		TextView translationTextView;
		RelativeLayout listitem;
	}
}
