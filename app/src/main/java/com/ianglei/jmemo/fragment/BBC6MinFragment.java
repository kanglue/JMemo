package com.ianglei.jmemo.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ianglei.jmemo.JMemoApplication;
import com.ianglei.jmemo.R;
import com.ianglei.jmemo.activity.DetailActivity;
import com.ianglei.jmemo.adapter.JRVBaseAdapter;
import com.ianglei.jmemo.bean.Listening;
import com.ianglei.jmemo.service.BBC6MinParser;
import com.ianglei.jmemo.service.SaveNewsListTask;
import com.ianglei.jmemo.utils.L;
import com.ianglei.jmemo.utils.NetworkUtil;
import com.ianglei.jmemo.utils.ToastUtil;
import com.ianglei.jmemo.utils.Tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class BBC6MinFragment extends BaseXRecyclerViewFragment
{
    public static final String TAG = BBC6MinFragment.class.getSimpleName();

    private String mType;
    private JRVBaseAdapter jrvBaseAdapter;
    private ArrayList<Listening> alllist = new ArrayList<Listening>();


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            ArrayList<Listening> localList = JMemoApplication.getDataSource().getItemListByCategory(6);
            if(msg.what == 1)
            {
                onDataErrorReceived();
            }
            ArrayList<Listening> list = (ArrayList<Listening>)msg.obj;
            L.i("Up to date add more " + list.size());
            if(null != null) {
                alllist.addAll(list);
            }
            if(null != localList) {
                alllist.addAll(localList);
            }

            new SaveNewsListTask(list).execute();
            onDataSuccessReceived(localList);
        }
    };

    @Override
    protected List parseData(String result) {

        //LogUtil.d(result);
        List<Listening> list = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            Gson gson = new Gson();
            list = gson.fromJson(jsonObject.getString("results"), new TypeToken<List<Listening>>() {
            }.getType());

        } catch (JSONException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public String obtainOfflineData(String url) {
        return null;
    }

    @Override
    protected String getUrl() {
//        mType = getArguments().getString(JFragmentStatePagerAdapter.TYPE_KEY);
//        String url = Apis.GanHuo + "/" + mType + "/10/" + currentPageIndex;
        String url = "http://www.bbc.co.uk/learningenglish/chinese/features/6-minute-english";
        L.i(url);
        return url;
    }

    /**
     * 加载数据
     */
    public void loadData() {

//        List<T> list = (List<T>)JMemoApplication.getDataSource().getItemListByCategory(6, 10 * mCurrentPageIndex);
//        mAdapter.addAll(list);
//        loadComplete();
        L.i("loadData begin");

        //不同fragment去不同url获取
        final String reqUrl = getUrl();

        if (!Tools.isNetworkConnected(getMyContext())) {
            //无网络则获取缓存数据
            List<Listening> list = (List<Listening>)JMemoApplication.getDataSource().getItemListByCategory(6, 10 * mCurrentPageIndex);
            L.i("无网络");
            onDataSuccessReceived(list);
            ToastUtil.show(getContext(),"当前无网络连接", Toast.LENGTH_SHORT);
        } else {

            if(NetworkUtil.Constants.NETWORK_WIFI == NetworkUtil.getNetWorkStatus(getActivity())) {
                new Thread(new BBC6MinParser(handler)).start();
            }
        }
    }

    @Override
    public void storeOfflineData(String result) {

    }

    @Override
    protected JRVBaseAdapter getAdapter() {
        jrvBaseAdapter = new JRVBaseAdapter<Listening>(new ArrayList<Listening>(), getMyContext()) {

            protected void onBindData(JRVBaseAdapter.CommomViewHolder holder, Listening bean) {


                holder.getView(R.id.title).setVisibility(View.VISIBLE);
                holder.setText(R.id.title, bean.getTitle());
                holder.setText(R.id.desc, bean.getDescribe());
                holder.getView(R.id.poster).setVisibility(View.VISIBLE);
                ImageView imageView = (ImageView)holder.getView(R.id.poster);
                Bitmap bm = BitmapFactory.decodeFile(bean.getCoverpath());
                //将图片显示到ImageView中
                imageView.setImageBitmap(bm);
//              HttpUtil.getInstance().loadImage(bean.url, imageView, true);
                holder.setText(R.id.learned_time, bean.getLearnedtimes() + "");
//                holder.setText(R.id.tv_tag, bean.type);
            }

            @Override
            public int getItemLayoutID(int viewType) {
                return R.layout.listening_item;
            }

            @Override
            protected void onItemClick(int position) {
//                Intent intent = new Intent(getMyContext(), WebViewActivity.class);
//                intent.putExtra(WebViewActivity.URL, mBeans.get(position - 1).getLink());
                Intent intent = new Intent(getMyContext(), DetailActivity.class);
                intent.putExtra("currentpos", (Listening) alllist.get(position - 1));
                getMyContext().startActivity(intent);
                //ToastUtil.show("你点到我了", Toast.LENGTH_SHORT);
            }
        };

        return jrvBaseAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getMyContext());
    }
}
