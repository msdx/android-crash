package com.githang.androidcrash.util;

/**
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-03
 * Time: 22:11
 * FIXME
 */
public class AssertUtil {
    /**
     * 断言参数非空。
     * @param name 参数名
     * @param object 参数值
     */
    public static final void assertNotNull(String name, Object object) {
        if(object == null) {
            throw new IllegalArgumentException("The argument '" + name + "' is null");
        }
    }
}
