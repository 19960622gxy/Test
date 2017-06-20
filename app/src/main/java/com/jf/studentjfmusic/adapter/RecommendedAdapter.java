package com.jf.studentjfmusic.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jf.studentjfmusic.Constant;
import com.jf.studentjfmusic.PlayDetailsActivity;
import com.jf.studentjfmusic.R;
import com.jf.studentjfmusic.bean.PlayList;

import static com.jf.studentjfmusic.PlayDetailsActivity.INDEX_KEY;
import static com.jf.studentjfmusic.PlayDetailsActivity.RESULTSBEEN_KEY;

/**
 * 固定头尾Adapter
 */
public class RecommendedAdapter extends RecyclerView.Adapter {
    private static final int TYPE_HEAD = 0;//头布局
    private static final int TYPE_FOOTER = 1;//尾布局
    private static final int TYPE_NORMAL = 2;//默认的布局


    public static final String PLAYDATA_KEY = "playData";

    PlayList mPlayList;

    private View mHeadView;
    private View mFooterView;

    public RecommendedAdapter(PlayList playList) {
        this.mPlayList = playList;

    }

    /**
     * 设置头部的View
     *
     * @param view 想要显示的View
     */
    public void setHeadView(View view) {
        mHeadView = view;
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
            return new HeadViewHolder(mHeadView);
        }
        if (viewType == TYPE_FOOTER) {
            return new FooterViewHolder(mFooterView);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended, parent, false);
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
        TextView tv_number;
        TextView tv_name;
        TextView tv_album;
        ImageView iv_palystatus;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_album = (TextView) itemView.findViewById(R.id.tv_album);
            iv_palystatus = (ImageView) itemView.findViewById(R.id.iv_palystatus);
        }
    }


    //记录上一次操作的bean，再次点击，修改该bean播放状态，并且重写赋值
    PlayList.Music mLastBean;

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
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
        final PlayList.Music bean = mPlayList.getMusics().get(position);


        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;


        itemViewHolder.tv_name.setText(bean.getTitle() + "");
        itemViewHolder.tv_album.setText(bean.getArtist() + " - " + bean.getAlbum() + "");

        if (bean.isPlayStatus()) {
            itemViewHolder.iv_palystatus.setVisibility(View.VISIBLE);
            itemViewHolder.tv_number.setVisibility(View.INVISIBLE);
        } else {
            itemViewHolder.iv_palystatus.setVisibility(View.INVISIBLE);

            itemViewHolder.tv_number.setVisibility(View.VISIBLE);
            itemViewHolder.tv_number.setText((position + 1) + "");
        }


        final int finalPosition = position;
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //判断当前条目和最后一次记录的对象是否相等
                if (bean.equals(mLastBean)) {
                    Intent intent = new Intent(holder.itemView.getContext(), PlayDetailsActivity.class);
                    //因为在播放详情界面，需要拿到所有的歌曲列表，所以需要将集合传递给PlayDetailsActivity
                    intent.putExtra(RESULTSBEEN_KEY,mPlayList);
                    intent.putExtra(INDEX_KEY, finalPosition);

                    intent.putExtra(PlayDetailsActivity.DETAILS_KEY,bean);
                    ((Activity)holder.itemView.getContext()).startActivity(intent);

                } else {

                    if (mLastBean != null) {
                        mLastBean.setPlayStatus(false);
                    }


                    //重置所有的状态
                    for (int i = 0; i < mPlayList.getMusics().size(); i++) {
                        mPlayList.getMusics().get(i).setPlayStatus(false);
                    }



                    bean.setPlayStatus(true);
                    notifyDataSetChanged();
                    mLastBean = bean;//重新赋值


                    //本地广播
                    Intent intent = new Intent(Constant.Action.ACTION_PLAY);
                    intent.putExtra(PLAYDATA_KEY, mPlayList);


                    //获取广播管理器
                    LocalBroadcastManager manager = LocalBroadcastManager.getInstance(holder.itemView.getContext());
                    manager.sendBroadcast(intent);
                }

            }
        });


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
        return mPlayList.getMusics().size() + (mHeadView != null ? 1 : 0) + (mFooterView != null ? 1 : 0);
    }
}