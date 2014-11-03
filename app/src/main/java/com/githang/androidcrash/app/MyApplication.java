package com.githang.androidcrash.app;

import android.app.Application;

import com.githang.androidcrash.AndroidCrash;
import com.githang.androidcrash.reporter.httpreporter.CrashHttpReporter;

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-03
 * Time: 23:26
 * FIXME
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHttpReporter reporter = new CrashHttpReporter(this);
        AndroidCrash.getInstance().setCrashReporter(reporter).init(this);
    }
}
