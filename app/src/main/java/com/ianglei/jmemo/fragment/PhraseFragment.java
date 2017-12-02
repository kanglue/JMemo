package com.ianglei.jmemo.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ianglei.jmemo.JMemoApplication;
import com.ianglei.jmemo.R;
import com.ianglei.jmemo.adapter.JRVBaseAdapter;
import com.ianglei.jmemo.bean.Phrase;
import com.ianglei.jmemo.mini.MyWindowManager;
import com.ianglei.jmemo.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j00255628 on 2017/1/28.
 */

public class PhraseFragment extends BaseXRecyclerViewFragment {
    public static final String TAG = PhraseFragment.class.getSimpleName();
    private JRVBaseAdapter jrvBaseAdapter;
    List<Phrase> list = new ArrayList<Phrase>();

    @Override
    public void loadData() {
        list = (List<Phrase>) JMemoApplication.getPhraseHelper().getPhraseList();
        L.i("Phrase list size: " + list.size());
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
        jrvBaseAdapter = new JRVBaseAdapter<Phrase>(new ArrayList<Phrase>(), getMyContext()) {

            @Override
            protected void onBindData(JRVBaseAdapter.CommomViewHolder holder, Phrase bean) {
                holder.setText(R.id.phrase, bean.getPhrase());
                holder.setText(R.id.symbol, bean.getSymbol());
                holder.setText(R.id.translation, bean.getTranslation());
                holder.setText(R.id.sample, bean.getSample());
            }

            @Override
            public int getItemLayoutID(int viewType) {
                return R.layout.phrase_item;
            }

            @Override
            protected void onItemClick(int position) {
                MyWindowManager.openBigWindow(getContext(), list.get(position - 1));
            }


        };

        return jrvBaseAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getMyContext());
    }
}
