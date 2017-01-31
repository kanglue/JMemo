package com.ianglei.jmemo.utils;

import com.ianglei.jmemo.fragment.BaseFragment;

import java.util.HashMap;
import java.util.Map;


/**
 * 创建Fragment工具
 */
public class FragmentManagerUtil {
    private static Map<String, BaseFragment> fragmentlist = new HashMap<>();

    public static BaseFragment createFragment(Class<?> clazz, boolean isAddList) {
        BaseFragment targetFrament = null;
        String className = clazz.getName();
        if (fragmentlist.containsKey(className)) {
            targetFrament = fragmentlist.get(className);
        } else {
            try {
                targetFrament = (BaseFragment) Class.forName(className).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isAddList) {
            fragmentlist.put(className, targetFrament);
        }
        return targetFrament;
    }

    public static BaseFragment createFragment(Class<?> clazz) {
        return createFragment(clazz, true);
    }

    public static void clearFragmentList() {
        fragmentlist.clear();
    }
}
