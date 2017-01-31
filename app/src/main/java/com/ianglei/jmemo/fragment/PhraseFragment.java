package com.ianglei.jmemo.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ianglei.jmemo.R;
import com.ianglei.jmemo.activity.DetailActivity;
import com.ianglei.jmemo.adapter.JRVBaseAdapter;
import com.ianglei.jmemo.bean.Listening;
import com.ianglei.jmemo.bean.Phrase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j00255628 on 2017/1/28.
 */

public class PhraseFragment extends BaseXRecyclerViewFragment {

    private JRVBaseAdapter jrvBaseAdapter;

    @Override
    public void loadData() {

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

            protected void onBindData(JRVBaseAdapter.CommomViewHolder holder, Phrase bean) {
                holder.setText(R.id.phrase, bean.getPhrase());
                holder.setText(R.id.category, bean.getCategory() == 0 ? "word" : "phrase");
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

            }
        };

        return jrvBaseAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }
}
