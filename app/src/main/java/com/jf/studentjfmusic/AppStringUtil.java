package com.jf.studentjfmusic;

/**
 * 项目中String 处理类
 * Created by weidong on 2017/6/5.
 */

public class AppStringUtil {

    /**
     * 检查用户名是否正确
     *
     * @return true 正确， false 错误
     */
    public static boolean checkUserName(String userName) {
        if (userName == null) {
            return false;
        }
        if ("".equals(userName)) {
            return false;
        }

        if (userName.length() < 3 || userName.length() > 10) {
            return false;
        }
        return true;
    }

    /**
     * 判断密码是否合法
     *
     * @return  true 正确， false 错误
     */
    public static boolean checkPassword(String password) {
        if (password == null) {
            return false;
        }
        if ("".equals(password)) {
            return false;
        }

        if (password.length() < 3 || password.length() > 10) {
            return false;
        }
        return true;
    }

}
