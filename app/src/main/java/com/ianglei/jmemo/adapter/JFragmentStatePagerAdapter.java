package com.ianglei.jmemo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ianglei.jmemo.fragment.BBC6MinFragment;
import com.ianglei.jmemo.fragment.PhraseFragment;
import com.ianglei.jmemo.utils.FragmentManagerUtil;

/**
 * Created by j00255628 on 2016/12/7.
 */

//Page页的数据集
public class JFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    public static final String TYPE_KEY = "type";
    private static String[] mTitles;

    public JFragmentStatePagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        //首页BBC
        if(position == 0) {
            Fragment fragment = FragmentManagerUtil.createFragment(BBC6MinFragment.class, false);
            Bundle bundle = new Bundle();
            bundle.putString(TYPE_KEY, mTitles[position]);
            fragment.setArguments(bundle);
            return fragment;
        }
        else if(position == 1) {
                Fragment fragment = FragmentManagerUtil.createFragment(PhraseFragment.class, false);
                Bundle bundle = new Bundle();
                bundle.putString(TYPE_KEY, mTitles[position]);
                fragment.setArguments(bundle);
                return fragment;
        }
        else
        {
            return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }


}
