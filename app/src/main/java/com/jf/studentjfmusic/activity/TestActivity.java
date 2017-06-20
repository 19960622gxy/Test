package com.jf.studentjfmusic.activity;

import android.os.Bundle;

import com.jf.studentjfmusic.R;
import com.jf.studentjfmusic.activity.base.BaseBottomNavActivity;

public class TestActivity extends BaseBottomNavActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setContentView(R.layout.activity_test);
//        ButterKnife.bind(this);

//        bindMusicService();
    }
//    public void bindMusicService() {
//        Intent intent = new Intent(this, MusicService.class);
//        bindService(intent, connection, BIND_AUTO_CREATE);
//    }
//
//    MusicService.MusicBinder mMusicBinder;
//
//    ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            mMusicBinder = (MusicService.MusicBinder) service;
//            Log.e(TAG, "onServiceConnected: " + "MainActivity 服务连接啦");
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };
//
//    @BindView(R.id.iv_playstatu)
//    ImageView iv_playstatu;
//
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
}
