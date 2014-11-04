android-crash
=============

android crash 是我写的一个Android程序崩溃信息处理框架。通过它，可以在程序崩溃时收集崩溃信息并用你指定的方式发送出来。

在本框架中，我实现了邮件及HTTP POST请求的发送方式。如果要采用其他方式，可以继承AbstractCrashReportHandler类并实现其抽象方法。

使用本框架的方法只需要两个步骤。
1、添加依赖：
在repository中添加jcenter。
```groovy
repository {
   jcenter() // or mavenCentral()
}
```
在dependencies中添加如下依赖。
```groovy

compile(group: 'com.githang', name: 'android-crash', version: '0.2.2')
```

2、写一个类，继承自Application，并在AndroidManifest.xml中指定。然后在onCreate方法中添加如下代码：

使用E-mail方式，需要添加activation.jar, additionnal.jar, mail.jar 这三个jar包，可以从本项目的libs文件夹中获取。使用HTTP方式则不需要其他依赖库。

```java
    @Override
    public void onCreate() {
        super.onCreate();

        initHttpReporter();
    }

    /**
     * 使用EMAIL发送日志
     */
    private void initEmailReporter() {
        CrashEmailReporter reporter = new CrashEmailReporter(this);
        reporter.setReceiver("admin@githang.com");
        reporter.setSender("irain_log@163.com");
        reporter.setSendPassword("xxxxxxxx");
        reporter.setSMTPHost("smtp.163.com");
        reporter.setPort("465");
        AndroidCrash.getInstance().setCrashReporter(reporter).init(this);
    }

    /**
     * 使用HTTP发送日志
     */
    private void initHttpReporter() {
        CrashHttpReporter reporter = new CrashHttpReporter(this) {
            /**
             * 重写此方法，可以弹出自定义的崩溃提示对话框，而不使用系统的崩溃处理。
             * @param thread
             * @param ex
             */
            @Override
            public void closeApp(Thread thread, Throwable ex) {
                final Activity activity = AppManager.currentActivity();
                Toast.makeText(activity, "发生异常，正在退出", Toast.LENGTH_SHORT).show();
                // 自定义弹出对话框
                new AlertDialog.Builder(activity).
                        setMessage("程序发生异常，现在退出").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppManager.AppExit(activity);
                    }
                }).create().show();
                Log.d("MyApplication", "thead:" + Thread.currentThread().getName());
            }
        };
        reporter.setUrl("http://crashreport.jd-app.com/ReportFile").setFileParam("fileName")
                .setToParam("to").setTo("admin@githang.com")
                .setTitleParam("subject").setBodyParam("message");
        reporter.setCallback(new CrashHttpReporter.HttpReportCallback() {
            @Override
            public boolean isSuccess(int i, String s) {
                return s.endsWith("ok");
            }
        });
        AndroidCrash.getInstance().setCrashReporter(reporter).init(this);
    }

```
