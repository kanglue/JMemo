package com.jmemo.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

import com.jmemo.db.VocabularyHelper;
import com.jmemo.db.Word;

public class JGridView extends GridView implements OnScrollListener
{
	public final static String TAG = "JGridView";
	private static int EACH_STEP = 100;
	private boolean isLastRow = false;
	private int totalCount = 100;
	private ArrayList<Word> moreList = null;
	
	public JGridView(Context context) {
		super(context);
	}
	
    public JGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	
	class LoadMoreWordAsync extends AsyncTask<String, String, ArrayList<Word>> {

		@Override
		protected ArrayList<Word> doInBackground(String... params) {
			moreList = VocabularyHelper.getWordListMore(EACH_STEP, totalCount);
			return moreList;
		}
		
		@Override
		protected void onPostExecute(ArrayList<Word> result) {
			super.onPostExecute(result);
			
			((JGridViewAdapter)getAdapter()).setMoreData(moreList);
			((JGridViewAdapter)getAdapter()).notifyDataSetChanged();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
			//加载元素
			LoadMoreWordAsync task = new LoadMoreWordAsync();
			task.execute("");
			totalCount += EACH_STEP;
			isLastRow = false;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		Log.i(TAG, "firstVisibleItem: " + firstVisibleItem + ", visibleItemCount: " 
			+ visibleItemCount + ", LastVisiblePosition: " + getLastVisiblePosition()
			+ " totalItenCount: " + totalItemCount);
		if(firstVisibleItem + visibleItemCount == totalCount && totalItemCount > 0)
		{
			isLastRow = true;
		}
	}

}
