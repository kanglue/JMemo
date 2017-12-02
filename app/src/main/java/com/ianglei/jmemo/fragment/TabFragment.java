package com.ianglei.jmemo.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.ianglei.jmemo.R;
import com.ianglei.jmemo.adapter.JFragmentStatePagerAdapter;

/**
 * Created by j00255628 on 2016/12/7.
 */

public class TabFragment extends BaseFragment {
    private String[] mTitles;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private JFragmentStatePagerAdapter JFragmentStatePagerAdapter;

    @Override
    protected int getLayoutResourceID() {
        return R.layout.fragment_tab;
    }


    @Override
    protected void initView() {
        mTitles = new String[]{"Phrase", "Word", "BBC 6 Minutes"};
        mTabLayout = IFindViewById(R.id.tl_study);
        mViewPager = IFindViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        JFragmentStatePagerAdapter = new JFragmentStatePagerAdapter(getChildFragmentManager(), mTitles);

        for (int i = 0; i < mTitles.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[i]));
        }
    }

    @Override
    protected void initData() {
        //super.initData();
        if (JFragmentStatePagerAdapter != null) {
            mViewPager.setAdapter(JFragmentStatePagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }
}
