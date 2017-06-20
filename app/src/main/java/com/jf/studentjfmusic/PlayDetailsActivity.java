package com.jf.studentjfmusic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jf.studentjfmusic.bean.PlayList;
import com.jf.studentjfmusic.widget.DiscView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

///**
// * 播放详情
// * 1. 唱针
// *      状态：
// *          1. 按下
// *          2. 抬起
// * 2. 唱盘
//         状态
//            1. 旋转
// *          2. 静止
// */
public class PlayDetailsActivity extends AppCompatActivity {
    public static final String DETAILS_KEY = "details";
    public static final String RESULTSBEEN_KEY = "mResultsBeen";
    public static final String INDEX_KEY = "position";


    private static final String TAG = "PlayDetailsActivity";

    @BindView(R.id.iv_bg)
    ImageView iv_bg;

    @BindView(R.id.iv_play)
    ImageView iv_play;


    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_artist)
    TextView tv_artist;

    @BindView(R.id.dv)
    DiscView dv;

    //歌单的歌曲列表
    PlayList mPlayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_details);
        ButterKnife.bind(this);

        mPlayList = getIntent().getParcelableExtra(RESULTSBEEN_KEY);
        Log.e(TAG, "onCreate: " + mPlayList);

        //获取下标
        int position = getIntent().getIntExtra(INDEX_KEY, 0);
        PlayList.Music music = mPlayList.getMusics().get(position);


//        NewPlayListResultsBean bean = getIntent().getParcelableExtra(DETAILS_KEY);
        String url = "http://ac-kCFRDdr9.clouddn.com/e3e80803c73a099d96a5.jpg";
        if (music.getAlbumPicUrl() != null) {
            url = music.getAlbumPicUrl();
        }
        tv_name.setText(music.getTitle());
        tv_artist.setText(music.getArtist());


        Glide.with(this)
                .load(url)
                //模糊图片, this   10 模糊度   5 将图片缩放到5倍后进行模糊
                .bitmapTransform(new BlurTransformation(this, 10, 3) {
                })
                .into(iv_bg);

        //这里想要拿到更新标题的通知
        dv.setDiscChangListener(discChangListener);
        dv.setMusicData(mPlayList, position);

    }

    DiscView.DiscChangListener discChangListener = new DiscView.DiscChangListener() {
        @Override
        public void onActionbarChanged(PlayList.Music bean) {
            tv_name.setText(bean.getTitle());
            tv_artist.setText(bean.getArtist());
        }
    };


    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }


    @OnClick({R.id.iv_last, R.id.iv_play, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_last:
                dv.playLast();


                break;
            case R.id.iv_play:
                iv_play.setSelected(!iv_play.isSelected());

                if(iv_play.isSelected()) {
                    iv_play.setImageResource(R.mipmap.play_rdi_btn_pause);
                }else{
                    iv_play.setImageResource(R.mipmap.play_rdi_btn_play);
                }

                dv.pause();


                break;
            case R.id.iv_next:

                dv.playNext();


                break;
        }
    }
}
