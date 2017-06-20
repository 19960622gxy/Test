package com.jf.studentjfmusic.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.jf.studentjfmusic.bean.Result;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by weidong on 2017/6/8.
 */

public class BannerAdapter extends PagerAdapter {
    ArrayList<Result> results;

    public BannerAdapter(ArrayList<Result> results) {
        this.results = results;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //这里需要获取到图片的链接


        SmartImageView smartImageView = results.get(position).getSmartImageView();
        //设置图片的链接，开始加载图片
        smartImageView.setImageUrl(results.get(position).getPicurl());

        container.addView(smartImageView);
        return smartImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(results.get(position).getSmartImageView());
    }
}
