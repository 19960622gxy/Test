package com.jf.studentjfmusic.fragment.found;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jf.studentjfmusic.R;
import com.jf.studentjfmusic.adapter.PlayListAdapter;
import com.jf.studentjfmusic.bean.PlayListResponse;
import com.jf.studentjfmusic.utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 推荐
 * Created by weidong on 2017/6/12.
 */

public class PlayListFragment extends Fragment {
    @BindView(R.id.rl)
    RecyclerView rl;
    public static final String TAG = "PlayListFragment";

    ArrayList<PlayListResponse.ResultsBean> mResultsBeen;
    private PlayListAdapter mPlayListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mResultsBeen = new ArrayList<>();
        mPlayListAdapter = new PlayListAdapter(mResultsBeen);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        rl.setLayoutManager(gridLayoutManager);
//        rl.setLayoutManager(new LinearLayoutManager(getActivity()));

//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                Log.e(TAG, "getSpanSize: " + position);
//                int itemType = mPlayListAdapter.getItemViewType(position);
//                if (mPlayListAdapter.getHeadView() != null && itemType == PlayListAdapter.TYPE_HEAD) {
//                    return gridLayoutManager.getSpanCount();
//                }
//                return 1;//所占用的位置
//            }
//        });
//        gridLayoutManager.setSpanCount(gridLayoutManager.getChildCount());


        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_playlist_head,rl,false);
        mPlayListAdapter.setHeadView(headView);


        rl.setAdapter(mPlayListAdapter);
        getData();
    }

    public void getData(){
        OkHttpClient client = new OkHttpClient();
        String url = "https://leancloud.cn:443/1.1/classes/PlayList";

        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        builder.addQueryParameter("limit","10");

        final Request request = HttpUtils.requestGET(builder.build());
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, "onResponse: " + result);
                PlayListResponse listResponse = new Gson().fromJson(result,PlayListResponse.class);

                mResultsBeen.addAll(listResponse.getResults());



                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPlayListAdapter.notifyDataSetChanged();
                    }
                });

            }
        });

    }
}
