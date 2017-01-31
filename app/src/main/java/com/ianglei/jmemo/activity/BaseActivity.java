package com.ianglei.jmemo.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ianglei.jmemo.R;
import com.ianglei.jmemo.mini.FloatWindowService;
import com.ianglei.jmemo.utils.AppManager;
import com.ianglei.jmemo.utils.L;
import com.ianglei.jmemo.utils.LogUtil;


public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected Toolbar mToolbar;
    protected Context mContext;
    protected String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(getLayoutResourceId());
        init();
        initView();
        initData();
        //管理activity
        AppManager.getAppManager().addActivity(this);
        //Logger.d("目前Actvity 栈中有：" + AppManager.getAppManager().getActivityCount() + "个Actvity");
        L.i("目前Actvity 栈中有：" + AppManager.getAppManager().getActivityCount() + "个Actvity");
        //  Log.d("BaseActivity", "当前Actvity 栈中有：" + AppManager.getAppManager().getActivityCount() + "个Actvity");

    }

    /**
     * 该方法是用来加载菜单布局的
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 设置布局文件
     *
     * @return
     */
    protected abstract int getLayoutResourceId();

    /**
     * 初始化View之前做的事
     */
    protected void init() {
    }



    /**
     * 初始化空控件
     */
    protected abstract void initView();

    /**
     * 处理数据
     */
    protected void initData() {}




    /**
     * 分装findviewById
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T IfindViewById(int id) {
        return (T) super.findViewById(id);
    }

    /**
     * 启动不带参数的Aactvity
     *
     * @param clazz
     */
    protected void launchActvity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 启动带参数的Activity
     *
     * @param clazz
     * @param extras
     */
    protected void launchActvityWithBundle(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(extras);
        startActivity(intent);
    }

    protected void initToolBar() {
        mToolbar = IfindViewById(R.id.toolbar);
        if (mToolbar != null) {
//            mToolbar.setLogo(R.mipmap.ic_top);
            mToolbar.setBackgroundColor(getResources().getColor(R.color.theme_color));
            // mToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.theme_color));
            mToolbar.setTitleTextAppearance(this, R.style.ToolBarTitleTextApperance);
            setSupportActionBar(mToolbar);
        }
    }

    public void setToolbarTitle(String title) {

        if (mToolbar != null) {
            this.title = title;
            //  mToolbar.setTitle(title);
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * 显示ToolBar
     */
    public void showToolBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
    }

    /**
     * 隐藏ToolBar
     */
    public void hideToolBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    /**
     * 完全退出应用
     */
    protected void closeApp() {
        AppManager.getAppManager().AppExit(mContext);
    }

    @Override
    public void onClick(View v) {

    }

}
