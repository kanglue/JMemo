package com.ianglei.jmemo.fragment;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ianglei.jmemo.JMemoApplication;
import com.ianglei.jmemo.R;
import com.ianglei.jmemo.adapter.JRVBaseAdapter;
import com.ianglei.jmemo.utils.FileUtils;
import com.ianglei.jmemo.utils.HttpUtil;
import com.ianglei.jmemo.utils.L;
import com.ianglei.jmemo.utils.LogUtil;
import com.ianglei.jmemo.utils.Tools;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.File;
import java.util.List;


/**
 * Created by _SOLID
 * Date:2016/4/18
 * Time:17:36
 * <p>
 * common fragment for list data display ,and you can extends this fragment for everywhere you want to display list data
 */
public abstract class BaseXRecyclerViewFragment<T> extends BaseFragment {
    public static final String TAG = BaseXRecyclerViewFragment.class.getSimpleName();

    public static final int ACTION_REFRESH = 1;
    public static final int ACTION_LOAD_MORE = 2;

    public XRecyclerView mXRecyclerView;
    public LinearLayout errorReload;
    public Button mBtnReload;
    public JRVBaseAdapter<T> mAdapter;

    public int mCurrentAction = ACTION_REFRESH;
    public int mCurrentPageIndex = 1;

    @Override
    protected int getLayoutResourceID() {
        return R.layout.fragment_base_recycler_view;
    }

    /**
     *
     */
    @Override
    protected void initView() {
        //网络异常
        errorReload = IFindViewById(R.id.error_reload);
        mBtnReload = IFindViewById(R.id.btn_reload);

        mXRecyclerView = IFindViewById(R.id.xrecyclerview);
        //mRecyclerView.hasFixedSize();
        mXRecyclerView.setLayoutManager(getLayoutManager());
        //下拉加载进度图标
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        mXRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);

        //不同子Fragment获取不同的Adapter，然后设置给XRV列表适配器
        mAdapter = getAdapter();
        mXRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

        //下拉刷新监听
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                swiActionLoadData(ACTION_REFRESH);
            }

            @Override
            public void onLoadMore() {
                swiActionLoadData(ACTION_LOAD_MORE);

            }
        });
        //重新加载按钮
        mBtnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorReload.setVisibility(View.GONE);
                mXRecyclerView.setRefreshing(true);
            }
        });

        //设置recycleView为可以下拉刷新,放置位置靠后会自动刷新一下
        mXRecyclerView.setRefreshing(true);
    }


    /**
     * 刷新或者加载更多
     *
     * @param action
     */
    private void swiActionLoadData(int action) {
        mCurrentAction = action;
        switch (mCurrentAction) {
            case ACTION_REFRESH:
                // mAdapter.clear();
                mCurrentPageIndex = 0;

                break;
            case ACTION_LOAD_MORE:
                mCurrentPageIndex++;
                break;
        }

        //加载数据
        loadData();
    }

    /**
     * 加载数据
     */
    public abstract void loadData();

    /**
     * 存储离线数据
     *
     * @param result
     */
    public abstract void storeOfflineData(String result);
//        try {
//            FileUtils.writeFile(getOfflineDir(url), result, "UTF-8", true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    /**
     * 成功接收消息
     *
     */
    public void onDataSuccessReceived(List<T> list) {
        if (null != list) {
//            List<T> list = parseData(result);
            mAdapter.addAll(list);
            loadComplete();
            errorReload.setVisibility(View.GONE);
        } else {
            onDataErrorReceived();
        }
    }

    /**
     * 解析处理数据
     *
     * @param result
     * @return
     */
    protected abstract List<T> parseData(String result);

    /**
     * 通知数据加载完毕
     */
    private void loadComplete() {
        if (mCurrentAction == ACTION_REFRESH) {
            mXRecyclerView.refreshComplete();
        }
        if (mCurrentAction == ACTION_LOAD_MORE)
            mXRecyclerView.loadMoreComplete();
    }

    /**
     * 设置加载错误的布局
     */
    public void onDataErrorReceived() {
        // LogUtil.d("onDataErrorReceived");
        errorReload.setVisibility(View.VISIBLE);
        loadComplete();
    }

    /**
     * 获取离线json数据
     *
     * @param url
     * @return
     */
    public abstract String obtainOfflineData(String url);
//        String result = null;
//        try {
//            result = FileUtils.readFile(getOfflineDir(url), "");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;


    /**
     * 获取文件离线数据目录
     *
     * @param url
     * @return
     */
    private String getOfflineDir(String url) {
        L.i(FileUtils.getCacheDir(getMyContext()) + File.separator + "offline_gan_huo_cache" + File.separator + Tools.md5(url));
        return FileUtils.getCacheDir(getMyContext()) + File.separator + "offline_gan_huo_cache" + File.separator + Tools.md5(url);

    }

    /**
     * 对外提供
     *
     * @return
     */
    protected abstract String getUrl();


    /**
     * 对外提供设置adapter方法
     *
     * @return
     */
    protected abstract JRVBaseAdapter<T> getAdapter();


    /**
     * 对外提供layoutManager
     *
     * @return
     */
    protected abstract RecyclerView.LayoutManager getLayoutManager();
}
