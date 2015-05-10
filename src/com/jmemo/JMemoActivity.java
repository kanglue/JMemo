package com.jmemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.crittercism.app.Crittercism;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jmemo.db.DatabaseHelper;
import com.jmemo.db.Word;
import com.jmemo.fragment.GridWordFragment;
import com.jmemo.fragment.MenuFragment;
import com.jmemo.fragment.NewPhraseFragment;
import com.jmemo.fragment.OnMenuItemClickListener;
import com.jmemo.mini.FloatWindowService;
import com.jmemo.util.CommonLog;
import com.jmemo.util.ToastUtil;

public class JMemoActivity extends SlidingFragmentActivity implements
		OnMenuItemClickListener, ActionBar.TabListener, OnPageChangeListener {

	private static final String TAG = "JMemoActivity";

	private CommonLog log = new CommonLog();

	private Map<String, Word> wordMap = new HashMap<String, Word>();
	private ProgressBar loadingBar;
	private TextView progressText;
	
	private String[] tabTitles;
	
	private ViewPager viewPager;
	
	private List<Fragment> fragmentList;
	
	private ActionBar actionBar;

	private Toast toast;
	private boolean isBackExit;

	SlidingMenu sm;
		

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Crittercism.initialize(getApplicationContext(),
				"5523b8c78172e25e679067e8");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jmemo);
		setBehindContentView(R.layout.frame_menu);
		
		DatabaseHelper.init(this);
		
		toast = new ToastUtil().showToast(this);
		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		
		MenuFragment menuFragment = new MenuFragment();
		menuFragment.setOnMenuItemClickListener(this);
		fragmentTransaction.replace(R.id.menu, menuFragment);
		fragmentTransaction.commit();
		
		sm = getSlidingMenu();
		sm.setShadowWidth(50);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(100);
		sm.setFadeDegree(0.35f);
		
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		tabTitles = getResources().getStringArray(R.array.tab_title);
		fragmentList = new ArrayList<Fragment>();
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		actionBar = getSupportActionBar();

		actionBar.setDisplayShowTitleEnabled(true);

		actionBar.setDisplayShowHomeEnabled(true);

		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (int i = 0; i < tabTitles.length; i++)
		{
			ActionBar.Tab tab = actionBar.newTab();
			tab.setText(tabTitles[i]);
			tab.setTabListener(this);
			actionBar.addTab(tab, i);
		}

//		for (int i = 0; i < tabTitles.length; i++)
//		{
			Fragment fragment = new GridWordFragment();
//			Bundle args = new Bundle();
//			args.putString("arg", tabTitles[i]);
//			fragment.setArguments(args);

			fragmentList.add(fragment);
			
			Fragment fragment2 = new NewPhraseFragment();
			fragmentList.add(fragment2);
//		}

		viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager(),
				fragmentList));

		viewPager.setOnPageChangeListener(this);

//		Button startFloatWindow = (Button) findViewById(R.id.start_float_window);
//		startFloatWindow.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(JMemoActivity.this,
//						FloatWindowService.class);
//				startService(intent);
//				finish();
//			}
//		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.main_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId())
		{
		case android.R.id.home:
			// handle clicking the app icon/logo
			if (sm.isMenuShowing())
			{
				sm.showContent(true);
			} else
			{
				sm.showMenu(true);
			}
			return true;
		case R.id.menu_settings:
			Intent intent = new Intent(JMemoActivity.this,
					FloatWindowService.class);
			startService(intent);
			//finish();
			
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{

	}

	@Override
	public void onPageSelected(int arg0)
	{
		actionBar.setSelectedNavigationItem(arg0);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft)
	{

	}

	@Override
	public void onChange(int id)
	{

	}

	@Override
	public void onBackPressed()
	{
		if (isBackExit)
		{
			finish();
		} else
		{
			isBackExit = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run()
				{
					isBackExit = false;
				}
			}, 2000);
			toast.setText(getResources().getString(R.string.back_again_to_exit));
			toast.show();
		}
	}
}
