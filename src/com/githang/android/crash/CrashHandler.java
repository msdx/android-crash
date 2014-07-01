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
package com.githang.android.crash;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 崩溃处理者。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private static final CrashHandler sHandler = new CrashHandler();
    private static final UncaughtExceptionHandler sDefaultHandler = Thread
            .getDefaultUncaughtExceptionHandler();
    private static final ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor();
    private Future<?> future;
    private CrashListener mListener;
    private File mLogFile;
    /**
     * 发送报告的超时时间。
     */
    protected int timeout = 5;

    public static CrashHandler getInstance() {
        return sHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        CrashLogUtil.writeLog(mLogFile, "CrashHandler", ex.getMessage(), ex);
        future = THREAD_POOL.submit(new Runnable() {
            public void run() {
                if (mListener != null) {
                    mListener.afterSaveCrash(mLogFile);
                }
            };
        });
        if (!future.isDone()) {
            try {
                future.get(timeout, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sDefaultHandler.uncaughtException(thread, ex);
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
        mLogFile = logFile;
        mListener = listener;
    }

}
