package com.jf.studentjfmusic.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jf.studentjfmusic.R;
import com.jf.studentjfmusic.bean.PlayListResponse;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by weidong on 2017/6/14.
 */

public class PlayListAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEAD = 0;//头布局
    public static final int TYPE_FOOTER = 1;//尾布局
    public static final int TYPE_NORMAL = 2;//默认的布局
    private static final String TAG = "PlayListAdapter";
    private View mHeadView;
    private View mFooterView;



    ArrayList<PlayListResponse.ResultsBean> mResultsBeen;

    public PlayListAdapter(ArrayList<PlayListResponse.ResultsBean> mResultsBeen) {
        this.mResultsBeen = mResultsBeen;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.e(TAG, "onAttachedToRecyclerView: " + recyclerView.toString() );

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    Log.e(TAG, "getSpanSize: " + position);
                    int itemType = getItemViewType(position);
                    if(mHeadView != null && itemType == TYPE_HEAD){
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;//所占用的位置
                }
            });

        }
    }

    /**
     * 设置头部的View
     *
     * @param view 想要显示的View
     */
    public void setHeadView(View view) {
        mHeadView = view;
    }


    public View getHeadView() {
        return mHeadView;
    }

    public void setmHeadView(View mHeadView) {
        this.mHeadView = mHeadView;
    }

    /**
     * 设置尾部的View
     *
     * @param view 想要显示的View
     */
    public void setFooterView(View view) {
        mFooterView = view;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeadView != null && position == 0) {
            return TYPE_HEAD;
        }
        if (mFooterView != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //判断当前是否应该设置头布局
        if (viewType == TYPE_HEAD) {
            return new PlayListAdapter.HeadViewHolder(mHeadView);
        }
        if (viewType == TYPE_FOOTER) {
            return new PlayListAdapter.FooterViewHolder(mFooterView);
        }



        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent,false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }


    class HeadViewHolder extends RecyclerView.ViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }





    class ItemViewHolder extends RecyclerView.ViewHolder {
        SmartImageView siv;
        TextView tv_name;

        public ItemViewHolder(View itemView) {
            super(itemView);
            siv = (SmartImageView) itemView.findViewById(R.id.siv);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);

        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //判断当前是否是头布局，如果是头布局，那么直接return
        if (isHead(position)) {
            return;
        }

        if (isFooter(position)) {
            return;
        }

        //这里需要注意，取值应该是从position-1 开始，因为position ==0 已经被mHeadView占用了
        if (mHeadView != null) {
            position = position - 1;
        }



        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        PlayListResponse.ResultsBean bean = mResultsBeen.get(position);
        viewHolder.siv.setImageUrl(bean.getPicUrl().getUrl());
        viewHolder.tv_name.setText(bean.getName());
    }

    private boolean isHead(int position) {
        return mHeadView != null && position == 0;
    }

    private boolean isFooter(int position) {
        //getItemCount 获取item的个数
        return mFooterView != null && position == getItemCount() - 1;
    }

    @Override
    public int getItemCount() {
        return mResultsBeen.size() + (mHeadView != null ? 1 : 0) + (mFooterView != null ? 1 : 0);
    }
}
