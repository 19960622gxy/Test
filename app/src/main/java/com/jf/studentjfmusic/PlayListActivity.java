package com.jf.studentjfmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jf.studentjfmusic.activity.base.BaseBottomNavActivity;
import com.jf.studentjfmusic.adapter.RecommendedAdapter;
import com.jf.studentjfmusic.bean.HomeResponse;
import com.jf.studentjfmusic.bean.NewPlayListResponse;
import com.jf.studentjfmusic.bean.NewPlayListResultsBean;
import com.jf.studentjfmusic.bean.PlayList;
import com.jf.studentjfmusic.service.MusicService;
import com.jf.studentjfmusic.utils.HttpUtils;
import com.jf.studentjfmusic.utils.JFMusicUrlJoint;
import com.loopj.android.image.SmartImageView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 匹配正在播放的歌单
 * 1. 需要获取到正在播放的歌单的id
 * 2. 如果播放的歌单id 和 进入界面的歌单id一致 ，需要获取正在播放的歌曲下标
 */
public class PlayListActivity extends BaseBottomNavActivity {

    @BindView(R.id.rl)
    RecyclerView rl;

    //如果当前布局没有这个View，使用这个注解可以使得没有使用这个View的时候不会奔溃，
    // 但是使用这个View会出现空指针@Nullable
    @BindView(R.id.ll_actionbar)
    RelativeLayout ll_actionbar;

    @BindView(R.id.iv_bg)
    ImageView iv_bg;


    @BindView(R.id.iv_back)
    ImageView iv_back;


    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_name)
    TextView tv_name;

//    @BindView(R.id.iv_playstatu)
//    ImageView iv_playstatu;

    //歌单对象
    PlayList mPlayList = new PlayList();


    public static final String OBJECTID_KEY = "objectId";
    public static final String PLAYLISTBEAN_KEY = "PlayListBean";
    public static final String AUTHOR_KEY = "AUTHOR";
    private static final String TAG = "PlayListActivity";

    ArrayList<NewPlayListResultsBean> mResultsBeens;
    private RecommendedAdapter mPlayListAdapter;
    private HomeResponse.ResultsBean.PlayListBean mPlayListBean;
    private PlayBroadcastReceiver mPlayBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        ButterKnife.bind(this);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ll_actionbar.getLayoutParams();
        layoutParams.height = layoutParams.height + getStatusBarHeight(this);
        ll_actionbar.setLayoutParams(layoutParams);

        mPlayListBean = getIntent().getParcelableExtra(PLAYLISTBEAN_KEY);
        String author = getIntent().getStringExtra(AUTHOR_KEY);


        mResultsBeens = new ArrayList<>();

        Log.e(TAG, "onCreate: " + mPlayListBean.getObjectId());

        getNewPlayList(mPlayListBean.getObjectId());

        rl.setLayoutManager(new LinearLayoutManager(this));
        mPlayListAdapter = new RecommendedAdapter(mPlayList);

        View headView = LayoutInflater.from(this).inflate(R.layout.layout_songlist_head, rl, false);
        ImageView head_iv_bg = (ImageView) headView.findViewById(R.id.iv_bg);

        //模糊背景

        Glide.with(this)
                .load(mPlayListBean.getPicUrl())
                //模糊图片, this   10 模糊度   5 将图片缩放到5倍后进行模糊
                .bitmapTransform(new BlurTransformation(this, 10, 5) {
                })
                .into(head_iv_bg);


        ImageView iv_play = (ImageView) headView.findViewById(R.id.iv_play);
        iv_play.setColorFilter(Color.BLACK);

        //设置歌单封面图
        SmartImageView siv_pic = (SmartImageView) headView.findViewById(R.id.siv_pic);
        siv_pic.setImageUrl(mPlayListBean.getPicUrl());
//        Palette

        //设置歌单和作者名字
        TextView tv_playListName = (TextView) headView.findViewById(R.id.tv_playListName);
        TextView tv_author = (TextView) headView.findViewById(R.id.tv_author);
        tv_playListName.setText(mPlayListBean.getPlayListName());
        tv_author.setText(author);

        RelativeLayout ll = (RelativeLayout) headView.findViewById(R.id.ll);
        RelativeLayout.LayoutParams llParams = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        llParams.topMargin = layoutParams.height + getStatusBarHeight(this);
        ll.setLayoutParams(llParams);


        mPlayListAdapter.setHeadView(headView);

        rl.setAdapter(mPlayListAdapter);


        //监听RecyclerView的滑动，实现标题View的透明度变化
        rl.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e(TAG, "onScrollStateChanged: " + newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e(TAG, "onScrolled:  dx" + dx + "   dy" + dy);
                View headView = null;

                //获取第一个Item
                //获取布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int findFirstVisibleItemPosition = manager.findFirstVisibleItemPosition();//获取屏幕显示的第一个条目的下标
                if (findFirstVisibleItemPosition == 0) {
                    headView = recyclerView.getChildAt(findFirstVisibleItemPosition);
                }

                float alpha = 0;
                //如果headView 不是空的，那么标题View不透明
                if (headView == null) {
                    alpha = 1;
                } else {
                    alpha = Math.abs(headView.getTop()) * 1.0f / headView.getHeight();
                }

                if (alpha > 0.5) {
                    tv_title.setText(mPlayListBean.getPlayListName());
                } else {
                    tv_title.setText("歌单");
                }
                iv_bg.setAlpha(alpha);
            }
        });
        registerBroadcast();
        //bindMusicService();
    }


    public void registerBroadcast() {
        mPlayBroadcastReceiver = new PlayBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.Action.ACTION_PLAY);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPlayBroadcastReceiver, intentFilter);
    }


    /**
     * Activity 关闭
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPlayBroadcastReceiver);

    }
//    public void bindMusicService() {
//        Intent intent = new Intent(this, MusicService.class);
//        bindService(intent, connection, BIND_AUTO_CREATE);
//    }

//    MusicService.MusicBinder mMusicBinder;
//
//    ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mMusicBinder = (MusicService.MusicBinder) service;
//            Log.e(TAG, "onServiceConnected: " + "PlayListActivity  服务连接啦");
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };

//    @OnClick(R.id.iv_playstatu)
//    void palyStatus(View view) {
//        if (mMusicBinder.isPlaying()) {
//            mMusicBinder.pause();
//            iv_playstatu.setImageResource(R.mipmap.a2s);
//        } else {
//            mMusicBinder.play();
//            iv_playstatu.setImageResource(R.mipmap.play_rdi_btn_pause);
//        }
//    }

    @OnClick(R.id.iv_back)
    void onBack() {
        finish();
    }

    class PlayBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "我接收到播放的广播啦", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onReceive: 我接收到播放的广播啦");

            PlayList bean = intent.getParcelableExtra(RecommendedAdapter.PLAYDATA_KEY);

//            tv_name.setText(bean.getTitle());
//
//            Log.e(TAG, "onReceive: " + bean.toString());

            mMusicBinder.play(bean);
            iv_playstatu.setImageResource(R.mipmap.play_rdi_btn_pause);

        }
    }


    public void getNewPlayList(String objectId) {
        OkHttpClient client = new OkHttpClient();
        String url = Constant.URL.NEWPLAYLIST + JFMusicUrlJoint.getNewPlayListUrl(objectId);
        Request request = HttpUtils.requestGET(url);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

//                //从程序的角度出发，让一个线程去等待另一个线程，使用睡眠的方式是不合理的
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                    String result = response.body().string();//F11 添加书签   Shift + F11 查看书签
                    Log.e(TAG, "onResponse: " + result);
                    NewPlayListResponse newPlayListResponse = new Gson().fromJson(result, NewPlayListResponse.class);

                    Log.e(TAG, "onResponse: " + newPlayListResponse.toString());

                    //将所有数据添加到 adapter 持有的集合里面
                    mResultsBeens.addAll(newPlayListResponse.getResults());

                    mPlayList.setObjectId(mPlayListBean.getObjectId());

                    ArrayList<PlayList.Music> musics = new ArrayList<PlayList.Music>();
                    //歌曲列表
                    for (int i = 0; i < newPlayListResponse.getResults().size(); i++) {
                        NewPlayListResultsBean bean = newPlayListResponse.getResults().get(i);
                        //bean.getAlbumPic()  == null
                        //null.getUrl()   NullPointerException
                        String alubmPic = bean.getAlbumPic() == null ? "" : bean.getAlbumPic().getUrl();

                        PlayList.Music music = new PlayList.Music(
                                bean.getObjectId(),
                                bean.getTitle(),
                                bean.getArtist(),
                                bean.getFileUrl().getUrl(),
                                alubmPic,
                                bean.getAlbum()
                        );


                        //模拟第一个正在播放

                        //需要判断正在播放的歌单是否和当前界面歌单的id一致，如果一致，那么直接取正在播放的下标
//                    if(currIndex != -1 && mCurrPlayList!= null && mCurrPlayList.getObjectId().equals(mPlayListBean.getObjectId())) {
//                        if (i == currIndex) {
//                            music.setPlayStatus(true);
//                        }
//                    }

                        //直接使用服务的静态方法
                        int currIndex = MusicService.getCurrPlayIndex();
                        PlayList playList = MusicService.getCurrPlayList();
                        if (currIndex != -1 && playList != null && playList.getObjectId().equals(mPlayListBean.getObjectId())) {
                            if (i == currIndex) {
                                music.setPlayStatus(true);
                            }
                        }


                        musics.add(music);
                    }

                    mPlayList.setMusics(musics);

                    Log.e(TAG, "onResponse: " + musics.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPlayListAdapter.notifyDataSetChanged();
                        }
                    });


            }
        });

    }


//    int currIndex = -1;
//    PlayList mCurrPlayList;
//    @Override
//    public void updatePlayListStatus(PlayList playList, int currIndex) {
//        this.currIndex = currIndex;
//        this.mCurrPlayList = playList;
//    }

    //获取状态栏高度
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

}
