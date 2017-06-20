package com.jf.studentjfmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jf.studentjfmusic.R;
import com.jf.studentjfmusic.activity.NearFriendActivity;

/**
 * Created by weidong on 2017/6/12.
 */

public class MyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        view.findViewById(R.id.startNearFriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NearFriendActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
