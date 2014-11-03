/*
 * @(#)CrashHandler.java		       Project: crash
 * Date:2014-5-26
 *
 * Copyright (c) 2014 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.githang.androidcrash.log;

import android.util.Log;

import com.githang.androidcrash.util.AssertUtil;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 崩溃处理者。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class CrashCatcher implements UncaughtExceptionHandler {
    private static final String LOG_TAG = CrashCatcher.class.getSimpleName();

    private static final CrashCatcher sHandler = new CrashCatcher();

    private CrashListener mListener;
    private File mLogFile;

    public static CrashCatcher getInstance() {
        return sHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            LogWriter.writeLog(mLogFile, "CrashHandler", ex.getMessage(), ex);
        }catch (Exception e) {
            Log.w(LOG_TAG, e);
        }

        mListener.sendFile(mLogFile);
        mListener.closeApp(thread, ex);
    }

    /**
     * 初始化日志文件及CrashListener对象
     * 
     * @param logFile
     *            保存日志的文件
     * @param listener
     *            回调接口
     */
    public void init(File logFile, CrashListener listener) {
        AssertUtil.assertNotNull("logFile", logFile);
        AssertUtil.assertNotNull("crashListener", listener);
        mLogFile = logFile;
        mListener = listener;
    }
}
