package com.jf.studentjfmusic;

import android.support.v7.app.AppCompatActivity;

public class MainActivity1 extends AppCompatActivity {


//    private static final String TAG = "MainActivity";
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//
//    @BindView(R.id.dl)
//    DrawerLayout dl;
//
//    @BindView(R.id.rl_home)
//    RecyclerView rl_home;
//
//    ArrayList<Result> results = new ArrayList<>();
//
//    ArrayList<Home> homes = new ArrayList<Home>();
//
//    //    ArrayList<SmartImageView> smartImageViews = new ArrayList<>();
//    private BannerAdapter bannerAdapter;
//    private HomeHeadFooterAdapter homeAdapter;
//
//    //侧边栏布局
//    //内容布局
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.mipmap.ic_menu);
//        setTitle("");
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "onClick: 点一下");
//                dl.openDrawer(Gravity.LEFT);
//            }
//        });
//
//        getBanner();
//
//
//        rl_home.setLayoutManager(new LinearLayoutManager(this));
//        homeAdapter = new HomeHeadFooterAdapter(homes);
//        View headView = LayoutInflater.from(this).inflate(R.layout.layout_home_header,null);
//
//
//        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_home_footer,null);
//
//
//        homeAdapter.setHeadView(headView);
//        homeAdapter.setFooterView(footerView);
//        rl_home.setAdapter(homeAdapter);
//
//
//
//
//
//
//
//
//        Button bt = (Button) footerView.findViewById(R.id.bt);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(MainActivity1.this,AdjusColumnActivity.class);
//                intent.putParcelableArrayListExtra("homes",homes);
//                startActivityForResult(intent,111);
//            }
//        });
//
//
//        ViewPager vp = (ViewPager) headView.findViewById(R.id.vp);
//
//
//
//
//
//        bannerAdapter = new BannerAdapter(results);
//        vp.setAdapter(bannerAdapter);
//
//        getHome();
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 111){
//            ArrayList<Home> results = data.getParcelableArrayListExtra("homes");
//            homes.clear();
//            homes.addAll(results);
//            homeAdapter.notifyDataSetChanged();
//            rl_home.scrollToPosition(0);
//        }
//    }
//
//    /**
//     * 获取广告数据
//     */
//    private void getBanner() {
//
//        //https://leancloud.cn:443/1.1/classes/Banner?limit=10&&&&
//        String url = "https://leancloud.cn:443/1.1/classes/Banner?limit=6";
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .addHeader("X-LC-Id", "kCFRDdr9tqej8FRLoqopkuXl-gzGzoHsz")
//                .addHeader("X-LC-Key", "bmEeEjcgvKIq0FRaPl8jV2Um")
//                .addHeader("Content-Type", "application/json")
//                .url(url)
//                .get()
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                parJson(result);
//                Log.i(TAG, "onResponse: " + result);
//
//                try {
//                    JSONObject jsonObject = new JSONObject(result);
//                    JSONArray jsonArray = jsonObject.getJSONArray("results");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject object = jsonArray.getJSONObject(i);
//                        String picurl = object.getString("picurl");
//                        String desc = object.getString("desc");
//                        String createdAt = object.getString("createdAt");
//                        String updatedAt = object.getString("updatedAt");
//                        String objectId = object.getString("objectId");
//                        Result resu = new Result(picurl, desc, createdAt, updatedAt, objectId);
//
//
//                        SmartImageView smartImageView = new SmartImageView(MainActivity1.this);
//                        //注意：使用xml的布局可以直接使用 imageView.getLayoutParams()
//                        //如果是通过代码new出来的View，不能使用该方法，必须主动创建LayoutParams对象
//                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                        //设置View的参数
//                        smartImageView.setLayoutParams(layoutParams);
//                        //设置默认图片
//                        smartImageView.setImageResource(R.mipmap.ic_launcher);
////                        smartImageViews.add(smartImageView);
//
//                        resu.setSmartImageView(smartImageView);
//
//
//                        results.add(resu);
//
//                    }
//
////                    for (int i = 0; i < results.size(); i++) {
////                        SmartImageView smartImageView = new SmartImageView(MainActivity.this);
////                        //注意：使用xml的布局可以直接使用 imageView.getLayoutParams()
////                        //如果是通过代码new出来的View，不能使用该方法，必须主动创建LayoutParams对象
////                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
////                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
////                        //设置View的参数
////                        smartImageView.setLayoutParams(layoutParams);
////                        //设置默认图片
////                        smartImageView.setImageResource(R.mipmap.ic_launcher);
////                        smartImageViews.add(smartImageView);
//
////                    }
//
//                    //更新ViewPager数据
//                    //不能在子线程更新UI   Only the original thread that created a view hierarchy can touch its views.
//                    //需要切换回主线程
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            bannerAdapter.notifyDataSetChanged();
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private void parJson(String json){
//        Gson gson = new Gson();
//
//        //json --- 》 object  反序列化
//        Banner banner = gson.fromJson(json, Banner.class);
//
//        //object   ---》  json 序列化
//        //将banner 对象序列化为Json
//        String bannerJson = gson.toJson(banner);
//
//
//
//        Log.i(TAG, "parJson: " + banner.toString());
//        Log.i(TAG, "parJson: " + bannerJson);
//
//
//
//
//    }
//
//    public void getHome(){
//
//        String url = Constant.URL.HOME + "?include=playList%2CplayList.author&";
//        OkHttpClient client = new OkHttpClient();
////        Request request = HttpUtils.getBuilder().url(url).get().build();
//        Request request = HttpUtils.requestGET(url);
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//
//                Gson gson = new Gson();
//                HomeResponse homeResponse = gson.fromJson(result, HomeResponse.class);
//                Log.e(TAG, "onResponse: "+homeResponse);
//                //存取有序吗？
//                HashMap<String,ArrayList<HomeResponse.ResultsBean.PlayListBean>> hashMap = new HashMap<>();
//                //
//
//                //最新音乐
//                //11111111111
//                //22222222222
//                //推荐音乐
//                //11111111111
//                //22222222222
//
//                for (int i = 0; i < homeResponse.getResults().size(); i++) {
//
//                    HomeResponse.ResultsBean resultsBean = homeResponse.getResults().get(i);
//                    HomeResponse.ResultsBean.PlayListBean playListBean = homeResponse.getResults().get(i).getPlayList();
////
////                    Home home = new Home();
////                    home.setName(homeResponse.getResults().get(i).getItem());
////                    home.setType(homeResponse.getResults().get(i).getType());
////                    home.getPlayListBeen().add(playListBean);
////                    homes.add(home);
//
//
//
//
//                    //获取【最新音乐】【推荐音乐】
//                    String Item = resultsBean.getItem();
////
////                    for (int j = 0; j < homes.size(); j++) {
////                        Home jHome = homes.get(j);
////                        if(jHome.getName().equals(Item)){
////                            jHome.getPlayListBeen().add(playListBean);
////                        }else{
////                            ArrayList<HomeResponse.ResultsBean.PlayListBean> resultsBeens = new ArrayList<HomeResponse.ResultsBean.PlayListBean>();
////                            resultsBeens.add(playListBean);
////                            jHome.setPlayListBeen(resultsBeens);
////                        }
////                    }
//
//
//                    if(hashMap.containsKey(Item)){
//                        //包含
//                        ArrayList<HomeResponse.ResultsBean.PlayListBean> resultsBeens = hashMap.get(Item);
//                        resultsBeens.add(playListBean);
//                    }else{
//                        //不包含 Home.ResultsBean.PlayListBean 使用类中的内部类中的内部类
//                        ArrayList<HomeResponse.ResultsBean.PlayListBean> resultsBeens = new ArrayList<HomeResponse.ResultsBean.PlayListBean>();
//                        resultsBeens.add(playListBean);
//                        hashMap.put(Item,resultsBeens);
//                    }
//                }
//
//                Log.i(TAG, "onResponse: " + hashMap);
//                Set<Map.Entry<String,ArrayList<HomeResponse.ResultsBean.PlayListBean>>> entrySet = hashMap.entrySet();
//                for (Map.Entry<String,ArrayList<HomeResponse.ResultsBean.PlayListBean>> entry:entrySet) {
//                    String name = entry.getKey();
//                    ArrayList<HomeResponse.ResultsBean.PlayListBean> playList = entry.getValue();
//                    Home home = new Home();
//                    home.setName(name);
//                    home.setPlayListBeen(playList);
//                    homes.add(home);
//                }
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        homeAdapter.notifyDataSetChanged();
//                    }
//                });
//                Log.e(TAG, "onResponse: "+homes);
//
//            }
//        });
//    }



}
