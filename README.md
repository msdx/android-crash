android-crash
=============

android crash 是我写的一个Android程序崩溃信息处理框架。通过它，可以在程序崩溃时收集崩溃信息并用你指定的方式发送出来。

在本框架中，我只实现了邮件的发送方式。如果要采用其他方式，可以继承AbstractCrashReportHandler类并实现其抽象方法。

使用本框架的方法很简单，写一个类，继承自Application，并在AndroidManifest.xml中指定。然后在onCreate方法中添加如下代码：

```java

    CrashEmailReport report = new CrashEmailReport(this);
    report.setReceiver("log@msdx.pw");
    report.setSender("irain_log@163.com");
    report.setPass("xxxxxxxx");
    report.setHost("smtp.163.com");
    report.setPort("465");
```

注意：发送邮件需依赖三个包，分别是：activation.jar, additionnal.jar, mail.jar。可以从项目的libs文件夹中获取。
