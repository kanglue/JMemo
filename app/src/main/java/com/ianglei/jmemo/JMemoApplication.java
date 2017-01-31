package com.ianglei.jmemo;

import android.app.Application;
import android.database.SQLException;

//import com.antfortune.freeline.FreelineCore;
import com.ianglei.jmemo.db.ListeningDataSource;
import com.ianglei.jmemo.utils.StorageUtil;
import com.ianglei.jmemo.utils.ToastUtil;
import com.orhanobut.logger.Logger;

/**
 * Created by j00255628 on 2016/12/6.
 */

public class JMemoApplication extends Application {
    public static JMemoApplication application;

    private static ListeningDataSource dataSource;

    public static JMemoApplication getApplication() {
        return application;
    }

    public static ListeningDataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //初始化logger系统
        Logger
                .init("JMemo2")                 // default PRETTYLOGGER or use just init()
                .methodCount(2)                 // default 2
                .hideThreadInfo()               // default shown
                .methodOffset(2);                // default 0

        dataSource = new ListeningDataSource(this);

        try
        {
            dataSource.open();
        }
        catch (SQLException e)
        {
            ToastUtil.show(application, "DB is error.", 3000);
        }

        StorageUtil.printAppPathInfo(application);
    }
}
