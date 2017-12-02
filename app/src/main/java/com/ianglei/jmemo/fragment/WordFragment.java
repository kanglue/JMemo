package com.ianglei.jmemo.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.ianglei.jmemo.JMemoApplication;
import com.ianglei.jmemo.R;
import com.ianglei.jmemo.activity.PopupWord;
import com.ianglei.jmemo.adapter.JRVBaseAdapter;
import com.ianglei.jmemo.bean.Phrase;
import com.ianglei.jmemo.bean.Word;
import com.ianglei.jmemo.db.VocabularyHelper;
import com.ianglei.jmemo.mini.MyWindowManager;
import com.ianglei.jmemo.utils.L;
import com.ianglei.jmemo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ianglei on 2017/11/18.
 */

public class WordFragment extends BaseXRecyclerViewFragment {
    public static final String TAG = WordFragment.class.getSimpleName();
    private JRVBaseAdapter jrvBaseAdapter;
    List<Word> list = new ArrayList<Word>();

    @Override
    public void loadData() {
        List<Word> loadList = (List<Word>) JMemoApplication.getVocabularyHelper().getWordList(mCurrentPageIndex * 80 + 1);
        if(list.size() == 0) {
            list = loadList;
        }else{
            list.addAll(loadList);
        }

        L.i("Word list size: " + list.size());
        onDataSuccessReceived(list);
    }

    @Override
    public void storeOfflineData(String result) {

    }

    @Override
    protected List parseData(String result) {
        return null;
    }

    @Override
    public String obtainOfflineData(String url) {
        return null;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected JRVBaseAdapter getAdapter() {
        jrvBaseAdapter = new JRVBaseAdapter<Word>(new ArrayList<Word>(), getMyContext()) {

            @Override
            protected void onBindData(JRVBaseAdapter.CommomViewHolder holder, Word bean) {
                holder.setText(R.id.word, bean.getWord());
            }

            @Override
            public int getItemLayoutID(int viewType) {
                return R.layout.word_item;
            }

            @Override
            protected void onItemClick(int position) {
                //ToastUtil.show(getContext(), list.get(position - 1).getWord(), 100);
                PopupWord.launchActivity(getActivity(), list.get(position - 1));
            }


        };

        return jrvBaseAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new StaggeredGridLayoutManager(4, 1);
    }


}
