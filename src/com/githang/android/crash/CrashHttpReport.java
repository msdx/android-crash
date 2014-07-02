/*
 * @(#)CrashHttpReport.java		       Project: CrashHandler
 * Date: 2014-7-1
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
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;

/**
 * HTTP的post请求方式发送。
 * 
 * @author Geek_Soledad <a target="_blank" href=
 *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
 *         style="text-decoration:none;"><img src=
 *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
 *         /></a>
 */
public class CrashHttpReport extends AbstractCrashReportHandler {
    HttpClient httpclient = new DefaultHttpClient();
    private String url;
    private Map<String, String> otherParams;
    private String titleParam;
    private String bodyParam;
    private String fileParam;
    private String to;
    private String toParam;
    private HttpReportCallback callback;

    public CrashHttpReport(Context context, String logName) {
        super(context, logName);
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
    }

    public CrashHttpReport(Context context) {
        super(context);
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
    }

    @Override
    protected void sendReport(String title, String body, File file) {
        SimpleMultipartEntity entity = new SimpleMultipartEntity();
        entity.addPart(titleParam, title);
        entity.addPart(bodyParam, body);
        entity.addPart(toParam, to);
        if (otherParams != null) {
            for (Map.Entry<String, String> param : otherParams.entrySet()) {
                entity.addPart(param.getKey(), param.getValue());
            }
        }
        entity.addPart(fileParam, file, true);

        try {
            HttpPost req = new HttpPost(url);
            req.setEntity(entity);
            HttpResponse resp = httpclient.execute(req);
            if (callback != null) {
                if (callback.isSuccess(resp.getStatusLine().getStatusCode(),
                        EntityUtils.toString(resp.getEntity()))) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getUrl() {
        return url;
    }

    /**
     * 发送请求的地址。
     * 
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitleParam() {
        return titleParam;
    }

    /**
     * 标题的参数名
     * 
     * @param titleParam
     */
    public void setTitleParam(String titleParam) {
        this.titleParam = titleParam;
    }

    public String getBodyParam() {
        return bodyParam;
    }

    /**
     * 内容的参数名
     * 
     * @param bodyParam
     */
    public void setBodyParam(String bodyParam) {
        this.bodyParam = bodyParam;
    }

    public String getFileParam() {
        return fileParam;
    }

    /**
     * 文件的参数名
     * 
     * @param fileParam
     */
    public void setFileParam(String fileParam) {
        this.fileParam = fileParam;
    }

    public Map<String, String> getOtherParams() {
        return otherParams;
    }

    /**
     * 其他自定义的参数对（可不设置）。
     * 
     * @param otherParams
     */
    public void setOtherParams(Map<String, String> otherParams) {
        this.otherParams = otherParams;
    }

    public String getTo() {
        return to;
    }

    /**
     * 收件人
     * 
     * @param to
     */
    public void setTo(String to) {
        this.to = to;
    }

    public HttpReportCallback getCallback() {
        return callback;
    }

    /**
     * 设置发送请求之后的回调接口。
     * 
     * @param callback
     */
    public void setCallback(HttpReportCallback callback) {
        this.callback = callback;
    }

    public String getToParam() {
        return toParam;
    }

    /**
     * 收件人参数名。
     * 
     * @param toParam
     */
    public void setToParam(String toParam) {
        this.toParam = toParam;
    }

    /**
     * 发送请求之后的回调接口。
     * 
     * @author Geek_Soledad <a target="_blank" href=
     *         "http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=XTAuOSVzPDM5LzI0OR0sLHM_MjA"
     *         style="text-decoration:none;"><img src=
     *         "http://rescdn.qqmail.com/zh_CN/htmledition/images/function/qm_open/ico_mailme_01.png"
     *         /></a>
     */
    public static interface HttpReportCallback {
        /**
         * 判断是否发送成功。它在发送日志的方法中被调用，如果成功，则日志文件会被删除。
         * 
         * @param status
         *            状态码
         * @param content
         *            返回的内容。
         * @return
         */
        public boolean isSuccess(int status, String content);
    }
}
