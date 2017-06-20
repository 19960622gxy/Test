package com.jf.studentjfmusic.login.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;

/**
 * 所有Fragment的父类
 * Created by weidong on 2017/6/7.
 */

public class BaseFragment extends Fragment {
    ProgressDialog mProgressDialog;

    /**
     * 显示提示框
     * @param msg 提示的文本
     */
    public void showProgressDialog(String msg){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(getActivity());
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    /**
     * 关闭提示框
     */
    public void closeProgressDialog(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }
}
