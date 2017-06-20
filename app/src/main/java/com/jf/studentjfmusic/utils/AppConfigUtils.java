package com.jf.studentjfmusic.utils;

import android.content.Context;

/**
 * 单例- 只存在一个对象
 * 1. 私有化构造方法
 * 2. 持有本类的应用
 * 3. 提供一个静态的方法，获取本类对象
 *
 * 这个app在运行的过程中，应该只存在一个AppConfigUtils对象
 * app　配置信息的类
 * 调用该类的方法可以很方便获取到
 * 1. 是否第一次使用应用
 * 2. 是否登录
 * 3.
 * Created by weidong on 2017/6/7.
 */

public class AppConfigUtils {
    static String GUIDE = "guide";

    private AppConfigUtils(){}

    private static AppConfigUtils mConfigUtils;

    public static final AppConfigUtils getInstance(){
        if(mConfigUtils == null){
            mConfigUtils = new AppConfigUtils();
        }
        return mConfigUtils;
    }



    /**
     * 获取是否第一次使用
     *
     * @param context 上下文
     * @return 是否第一次使用， true 是   false 否
     */
    public boolean getGuide(Context context) {
        return SpUtils.getBoolean(context, GUIDE);
    }

    /**
     * 设置是否第一次使用
     *
     * @param context 上下文
     * @param guide   是否第一次使用， true 是   false 否
     */
    public void setGuide(Context context, boolean guide) {
        SpUtils.putBoolean(context, GUIDE, guide);
    }


}
