package com.jf.studentjfmusic.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.jf.studentjfmusic.Constant;
import com.jf.studentjfmusic.bean.PlayList;

import java.io.IOException;

/**
 * 将
 * 1. 歌单id
 * 2. 歌曲列表
 * Created by weidong on 2017/6/12.
 */

public class MusicService extends Service {
    private static final String TAG = "MusicService";

    private static MediaPlayer mMediaPlayer = new MediaPlayer();


    public static PlayList mPlayList;

    public static int mCurrIndex = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    public class MusicBinder extends Binder {

        public void play(){
            mMediaPlayer.start();
            Intent intent = new Intent(Constant.Action.PLAY);
            LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(intent);
        }

        /**
         * 播放
         */
        public void play(PlayList playList) {
            Log.e(TAG, "play: 开始播放啦");
            mPlayList = playList;
            String url = "";

            //获取当前播放的url

            for (int i = 0; i < playList.getMusics().size(); i++) {
                PlayList.Music music = playList.getMusics().get(i);
                if(music.isPlayStatus()){
                    url = music.getMusicUrl();
                    mCurrIndex = i;
                }
            }

            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaPlayer.start();

            Intent intent = new Intent(Constant.Action.PLAY);
            LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(intent);

        }

        /**
         * 暂停
         */
        public void pause() {
            mMediaPlayer.pause();

            Intent intent = new Intent(Constant.Action.PAUSE);
            LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(intent);
        }

        /**
         * 获取播放状态
         * @return
         */
        public boolean isPlaying() {
            if (mMediaPlayer != null) {
                return mMediaPlayer.isPlaying();
            } else {
                return false;
            }
        }
    }



    /**
     * 获取当前播放的歌单
     * @return
     */
    public static PlayList getCurrPlayList(){
        return mPlayList;
    }

    /**
     * 获取当前播放歌曲的下标
     * @return
     */
    public static int getCurrPlayIndex(){
        return mCurrIndex;
    }

}
