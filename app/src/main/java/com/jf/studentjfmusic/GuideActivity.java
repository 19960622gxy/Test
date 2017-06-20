package com.jf.studentjfmusic;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.jf.studentjfmusic.login.LoginActivity;
import com.jf.studentjfmusic.utils.AppConfigUtils;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

//1920  x  1080
public class GuideActivity extends AppCompatActivity {

    private static final String TAG = "GuideActivity";
    private float ratio;

    private int currentIndex;


    String[] titles = new String[]{
            "个 性 推 荐",
            "精 彩 评 论",
            "海 量 资 讯"
    };
    String[] descs = new String[]{
            "每 天 为 你 量 身 推 荐 最 合 口 味 的 好 音 乐",
            "4 亿 多 条 有 趣 的 故 事，听 歌 再 不 孤 单",
            "明 星 动 态、音 乐 热 点 尽 收 眼 底"
    };
    private TextSwitcher ts_title;
    private TextSwitcher ts_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initView();
    }

    ArrayList<View> views = new ArrayList<>();

    private void initView() {

        //获取图片资源和屏幕的比例
        ratio = 1F * (getResources().getDisplayMetrics().heightPixels / 1920.0F);

        ts_title = (TextSwitcher) findViewById(R.id.ts_title);
        ts_title.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(GuideActivity.this);
                tv.setTextSize(24);
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(getResources().getColor(android.R.color.white));
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                return tv;
            }
        });

        ts_desc = (TextSwitcher) findViewById(R.id.ts_desc);
        ts_desc.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(GuideActivity.this);
                tv.setTextSize(13);
                tv.setGravity(Gravity.CENTER);
                tv.setTextColor(getResources().getColor(android.R.color.white));
                tv.setTypeface(Typeface.DEFAULT_BOLD);
                return tv;
            }
        });

        ts_title.setText(titles[0]);
        ts_desc.setText(descs[0]);

        final View view1 = LayoutInflater.from(this).inflate(R.layout.layout_guide1, null);
        setViewSize(view1, 0);


        View view2 = LayoutInflater.from(this).inflate(R.layout.layout_guide2, null);
        setViewSize(view2, 1);


        View view3 = LayoutInflater.from(this).inflate(R.layout.layout_guide3, null);
        setViewSize(view3, 2);


        views.add(view1);
        views.add(view2);
        views.add(view3);

        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new MyPagerAdapter());
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                View view = views.get(position);
                if (position == 1) {
                    ivAesAnimator(view.findViewById(R.id.iv_aes), (517F * ratio));
                    ivAekAnimator(view.findViewById(R.id.iv_aek));
                    ivAekAnimator(view.findViewById(R.id.iv_aet));
                }
                if (position == 2) {
                    ivAesAnimator(view.findViewById(R.id.iv_aet), (333F * ratio));
                    ivAekAnimator(view.findViewById(R.id.iv_aek));
                }

                if (position > currentIndex) {
                    ts_title.setInAnimation(GuideActivity.this, R.anim.right_in);
                    ts_title.setOutAnimation(GuideActivity.this, R.anim.right_out);

                    ts_desc.setInAnimation(GuideActivity.this, R.anim.right_in);
                    ts_desc.setOutAnimation(GuideActivity.this, R.anim.right_out);
                } else {
                    ts_title.setInAnimation(GuideActivity.this, R.anim.left_in);
                    ts_title.setOutAnimation(GuideActivity.this, R.anim.left_out);

                    ts_desc.setInAnimation(GuideActivity.this, R.anim.left_in);
                    ts_desc.setOutAnimation(GuideActivity.this, R.anim.left_out);
                }


                ts_title.setText(titles[position]);
                ts_desc.setText(descs[position]);

                currentIndex = position;
            }

            private void ivAesAnimator(View view, float size) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", size, 0);
                animator.setDuration(800);
                animator.start();
            }

            private void ivAekAnimator(View view) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", new float[]{0F, 1F});
                animator.setDuration(1000);
                animator.start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        vp.setPageTransformer(true, new ViewPager.PageTransformer() {

            /**
             *
             * @param page  当前滑动的View
             * @param position View所占屏幕的空间
             *                 值为-1 page 滑出屏幕了
             *                 值为 0 在屏幕中间
             */
            @Override
            public void transformPage(View page, float position) {
                //1  LinearLayout   2 RelativeLayout
                Log.i(TAG, "transformPage: " + page.getClass() + "   " + position);
                //
                int pageWidth = page.getWidth();
                if (position < -1) {
                    page.setAlpha(0);
                } else if (position <= 1) {
//                    if (position < 0) {
//                        page.setTranslationX(-pageWidth * position);
//                    } else {
//                        page.setTranslationX(pageWidth);
                    //抵消移动的距离
                    page.setTranslationX(-pageWidth * position);
//                    }
                    page.setAlpha(Math.max(0, 1 - Math.abs(position)));
                } else {
                    page.setAlpha(0);
                }
            }
        });
        //TextSwitcher
        PageIndicatorView pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(vp);
        pageIndicatorView.setRadius(15F * ratio);
        pageIndicatorView.setPadding((int) (5 * ratio), 0, (int) (5 * ratio), 0);


        View bg_red = findViewById(R.id.bg_red);
        bg_red.getLayoutParams().height = (int) (1185F * ratio);


    }


    private void setViewSize(View view, int index) {
        //设置中间View的大小
        view.findViewById(R.id.cv_content).getLayoutParams().width = (int) (803F * ratio);
        view.findViewById(R.id.cv_content).getLayoutParams().height = (int) (1218F * ratio);


        int ivAesSzie = (int) (237F * ratio); //获取图片的宽高
        int marginLeft = (int) (40F * ratio);

        if (index == 0) {
            ImageView iv_aes = (ImageView) view.findViewById(R.id.iv_aes);
            FrameLayout.LayoutParams ivAesParams = (FrameLayout.LayoutParams) iv_aes.getLayoutParams();


            ivAesParams.height = ivAesSzie;
            ivAesParams.width = ivAesSzie;
            //获取图片顶部距离
            int marginTop = (int) (552F * ratio); //获取图片的宽高
            //int marginLeft = (int) (40F * ratio); //获取图片的宽高
            ivAesParams.topMargin = marginTop;
            ivAesParams.leftMargin = marginLeft;
        }

        if (index == 1) {
            FrameLayout.LayoutParams view2_iv_aes_Params = (FrameLayout.LayoutParams) view.findViewById(R.id.iv_aes).getLayoutParams();
            view2_iv_aes_Params.leftMargin = marginLeft;
            view2_iv_aes_Params.topMargin = marginLeft;
            view2_iv_aes_Params.height = ivAesSzie;
            view2_iv_aes_Params.width = ivAesSzie;

            ImageView iv_aet = (ImageView) view.findViewById(R.id.iv_aet);
            FrameLayout.LayoutParams iv_aetParams = (FrameLayout.LayoutParams) iv_aet.getLayoutParams();
            iv_aetParams.topMargin = (int) (333F * ratio);
            iv_aetParams.leftMargin = marginLeft;
        }

        if (index == 2) {
            ImageView iv_aet = (ImageView) view.findViewById(R.id.iv_aet);
            FrameLayout.LayoutParams iv_aetParams = (FrameLayout.LayoutParams) iv_aet.getLayoutParams();
            iv_aetParams.leftMargin = marginLeft;
            iv_aetParams.topMargin = marginLeft;
        }

    }

    class MyPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }




    @OnClick(R.id.bt_login)
    public void bt_login(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.bt_experience)
    public void bt_experience(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
        //存储第一次数据数据，修改为false

        AppConfigUtils.getInstance().setGuide(this,false);


    }





}
