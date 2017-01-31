package com.ianglei.jmemo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.ianglei.jmemo.R;
import com.ianglei.jmemo.fragment.AboutFragment;
import com.ianglei.jmemo.fragment.TabFragment;
import com.ianglei.jmemo.mini.FloatWindowService;
import com.ianglei.jmemo.utils.AppManager;
import com.ianglei.jmemo.utils.FragmentManagerUtil;
import com.ianglei.jmemo.utils.L;
import com.ianglei.jmemo.utils.DoubleClickExitHelper;


public class MainActivity extends BaseActivity {
    private static String TAG = "MainActvity";
    private static String KEY = "currentSelectMenuIndex";
    private static int mSelectMenuIndex = 0;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private FragmentManager mFragmentManager;
    private Fragment mDefaultFragment;
    private DoubleClickExitHelper DoubleClickExit;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSelectMenuIndex = savedInstanceState.getInt(KEY, 0);
        }
        mToolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        initToolBar();
        mFragmentManager = getSupportFragmentManager();
        DoubleClickExit = new DoubleClickExitHelper(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, mSelectMenuIndex);
    }

    @Override
    protected void initView() {

        setToolbarTitle(getString(R.string.app_name));
        mDrawerLayout = IfindViewById(R.id.drawer_layout);
        mNavigationView = IfindViewById(R.id.navigation_view);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mActionBarDrawerToggle.syncState();//该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        handleNavigationItemClick();
        initDefalueFragment();
    }

    /**
     * 第一个默认加载的fragment
     */
    private void initDefalueFragment() {
        mDefaultFragment = FragmentManagerUtil.createFragment(TabFragment.class);
        mFragmentManager.beginTransaction().add(R.id.frame_content, mDefaultFragment).commit();
        mNavigationView.getMenu().getItem(mSelectMenuIndex).setChecked(true);

    }

    /**
     * 处理侧滑栏
     */
    private void handleNavigationItemClick() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nvItem_main:
                        // setToobarTitle(getString(R.string.navigation_main));
                        setToolbarTitle(getString(R.string.app_name));
                        switchFragment(TabFragment.class);
                        //ToastUtil.showWithImg("点到我啦", Toast.LENGTH_SHORT);
                        break;
//                    case R.id.nvItem_news:
//                        //  setToobarTitle("新闻");
//                        setToobarTitle("搞笑图片");
//                        switchFragment(ImageFragment.class);
//                        break;
                    case R.id.nvsub_exit:
                        AppManager.getAppManager().AppExit(MainActivity.this);
                        break;
                    case R.id.sub_about:
                        switchFragment(AboutFragment.class);

                        break;

                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return false;//返回值表示该Item是否处于选中状态
            }
        });
    }

    //防止被重复实例化
    private void switchFragment(Class<?> clazz) {
        Fragment switchTo = FragmentManagerUtil.createFragment(clazz);
        if (switchTo.isAdded()) {
            L.i("already add");
            mFragmentManager.beginTransaction().hide(mDefaultFragment).show(switchTo).commitAllowingStateLoss();
        } else {
            L.i("not add");
            mFragmentManager.beginTransaction().hide(mDefaultFragment).add(R.id.frame_content, switchTo).commitAllowingStateLoss();
        }
        mDefaultFragment = switchTo;


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
                return true;
            }

            return DoubleClickExit.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_search:
                    askForPermission();
                    break;
//                case R.id.action_scan:
//                    msg += "Click share";
//                    break;
//                case R.id.action_settings:
//                    msg += "Click setting";
//                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };


    /**
     * 请求用户给予悬浮窗的权限
     */
    public void askForPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(MainActivity.this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        } else {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,
                    FloatWindowService.class);
            startService(intent);
        }
    }
}

