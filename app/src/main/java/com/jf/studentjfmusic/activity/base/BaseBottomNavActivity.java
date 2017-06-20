package com.jf.studentjfmusic.activity.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jf.studentjfmusic.Constant;
import com.jf.studentjfmusic.R;
import com.jf.studentjfmusic.bean.PlayList;
import com.jf.studentjfmusic.service.MusicService;

import java.util.ArrayList;

/**
 * 在这个类里面添加底部播放条，应该限制子Activity的布局大小
 * 该类里面处理：
 * 底部播放条所有的逻辑
 * 1. 播放按钮的状态
 *
 *
 * Created by weidong on 2017/6/16.
 */

public abstract class BaseBottomNavActivity extends AppCompatActivity {
    public static String TAG = "BaseBottomNavActivity";


    //方式一
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base_bottom_nav);
//        FrameLayout root = (FrameLayout) findViewById(R.id.root);
//
//        //获取子类的布局文件
//        int resid = layoutResId();
//
//        //加载子类布局
//        View childView = LayoutInflater.from(this).inflate(resid,root,false);
//
//        //将子类对应的View添加到root view中
//        root.addView(childView);
//
//    }

//    /**
//     * 返回想要显示的布局
//     * @return 布局id
//     */
//    public abstract int layoutResId();


    //方式二

    protected MusicService.MusicBinder mMusicBinder;

    //播放按钮
    protected ImageView iv_playstatu;

    //监听播放状态
    private MusicChangeBroadcastReceiver mPlayBroadcastReceiver;


    @Override
    public final void setContentView(@LayoutRes int layoutResID) {
        //加载父类的布局
        View view = LayoutInflater.from(this).inflate(R.layout.activity_base_bottom_nav,null);



        initBottomView(view);

        FrameLayout root = (FrameLayout) view.findViewById(R.id.root);
        //加载子类布局
        View childView = LayoutInflater.from(this).inflate(layoutResID,root,false);
        root.addView(childView);

        setContentView(view);
        //获取类型的名字
        TAG = getClass().getName();

        bindMusicService();

        registerBroadcast();
    }

    /**
     * 方式一：通过Activity生命周期去控制
     * 在 Activity 可见时去判断当前音乐播放状态
     */
    @Override
    protected void onResume() {
        super.onResume();
//        if(mMusicBinder!=null){
//            if (mMusicBinder.isPlaying()) {
//                iv_playstatu.setImageResource(R.mipmap.play_rdi_btn_pause);
//            } else {
//                iv_playstatu.setImageResource(R.mipmap.a2s);
//            }
//        }
    }

    public void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicBinder = (MusicService.MusicBinder) service;
            Log.e(TAG, "onServiceConnected: " + "MainActivity 服务连接啦");

            //进入到界面，应该判断当前音乐的播放状态
            if (mMusicBinder.isPlaying()) {
                iv_playstatu.setImageResource(R.mipmap.play_rdi_btn_pause);
            } else {
                iv_playstatu.setImageResource(R.mipmap.a2s);
            }


//            PlayList playList = mMusicBinder.getCurrPlayList();
//            int currIndex = mMusicBinder.getCurrPlayIndex();
//
//            updatePlayListStatus(playList,currIndex);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * 更新播放列表的状态
     */
    public void updatePlayListStatus(PlayList playList, int currIndex){};



    /**
     * 初始化底部按钮的点击事件
     * @param bottomView
     */
    private void initBottomView(View bottomView){
        iv_playstatu = (ImageView) bottomView.findViewById(R.id.iv_playstatu);
        iv_playstatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMusicBinder!=null) {
                    if (mMusicBinder.isPlaying()) {
                        mMusicBinder.pause();
                        iv_playstatu.setImageResource(R.mipmap.a2s);
                    } else {
                        mMusicBinder.play();
                        iv_playstatu.setImageResource(R.mipmap.play_rdi_btn_pause);
                    }
                }
            }
        });

        LinearLayout ll_playlist = (LinearLayout) bottomView.findViewById(R.id.ll_playlist);
        ImageView iv_showlist = (ImageView) bottomView.findViewById(R.id.iv_showlist);
        RecyclerView rl = (RecyclerView) bottomView.findViewById(R.id.rl);



        final BottomSheetBehavior behavior = BottomSheetBehavior.from(ll_playlist);
        iv_showlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        initPlaylist(rl);
    }

    private void initPlaylist(RecyclerView rl){
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            strings.add(i+" ");
        }

        rl.setLayoutManager(new LinearLayoutManager(this));
        PlayListAdapter playListAdapter = new PlayListAdapter(strings);
        rl.setAdapter(playListAdapter);

    }

    class PlayListAdapter extends RecyclerView.Adapter{
        ArrayList<String> strings;

        public PlayListAdapter(ArrayList<String> strings) {
            this.strings = strings;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,null);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView textView;
            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(android.R.id.text1);
            }
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textView.setText(strings.get(position) +" ");
        }

        @Override
        public int getItemCount() {
            return strings.size();
        }
    }



    private void registerBroadcast() {
        mPlayBroadcastReceiver = new MusicChangeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Action.PLAY);
        intentFilter.addAction(Constant.Action.PAUSE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPlayBroadcastReceiver,intentFilter);
    }

    class MusicChangeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果监听两个广播，那么需要判断接收到的广播类型
            switch (intent.getAction()){
                case Constant.Action.PLAY:
                    iv_playstatu.setImageResource(R.mipmap.play_rdi_btn_pause);
                    break;
                case Constant.Action.PAUSE:
                    iv_playstatu.setImageResource(R.mipmap.a2s);
                    break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //界面销毁，需要解绑服务和广播
        //unregisterReceiver(mPlayBroadcastReceiver);//本地广播不能使用这种方式解绑
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayBroadcastReceiver);
        unbindService(mConnection);
    }
}
