package com.jf.studentjfmusic.login.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jf.studentjfmusic.R;
import com.jf.studentjfmusic.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by weidong on 2017/6/5.
 */

public class RegisterFragment extends BaseFragment {
    private static final String TAG = "RegisterFragment";
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pass)
    EditText etPass;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_back)
    public void backClick(){
        //回退到上一个Fragment
        getFragmentManager().popBackStack();
    }



    @OnClick(R.id.bt_register)
    public void onViewClicked() {
        showProgressDialog("请等待。。。");

        String username = etName.getText().toString();
        String password = etPass.getText().toString();


        register(username, password);

    }

    private void register(String userName, String password) {
        OkHttpClient client = new OkHttpClient();
        //将两个参数封装为Json对象
        //{"password":"123456","username":"yyyyyy"}
        User user = new User(userName, password);
        //Gson
        //将User 转为 Json 格式的数据
        Gson gson = new Gson();
        String json = gson.toJson(user);//将user 转为 Json 格式的数据
        Log.i(TAG, "register: " + json);

        //告诉服务器，我们上传的格式是json格式
        MediaType JSONTYPE = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSONTYPE, json);

        String url = "https://leancloud.cn:443/1.1/users";
        final Request request = new Request.Builder()
                .addHeader("X-LC-Id", "kCFRDdr9tqej8FRLoqopkuXl-gzGzoHsz")
                .addHeader("X-LC-Key", "bmEeEjcgvKIq0FRaPl8jV2Um")
                .addHeader("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            /**
             * 只要访问成功，必定会回调改方法
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String result = response.body().string();
                Log.i(TAG, "run: " + result);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 201) {//
                            //{"sessionToken":"83yuqccnk9fqpa2lvkzonmpnl","updatedAt":"2017-06-07T02:21:26.032Z",
                            // "objectId":"59376326ac502e0068c9cefa","username":"aaa1","createdAt":"2017-06-07T02:21:26.032Z",
                            // "emailVerified":false,"mobilePhoneVerified":false}

                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String sessionToken = jsonObject.getString("sessionToken");
                                String updatedAt = jsonObject.getString("updatedAt");
                                String objectId = jsonObject.getString("objectId");
                                String username = jsonObject.getString("username");
                                String createdAt = jsonObject.getString("createdAt");
                                boolean emailVerified = jsonObject.getBoolean("emailVerified");
                                boolean mobilePhoneVerified = jsonObject.getBoolean("mobilePhoneVerified");
                                Log.e(TAG, "run: "+ sessionToken +
                                        updatedAt + username
                                );

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                        } else {
                            if(response.code() == 202){
                                // {"code":202,"error":"Username has already been taken"}
                                Toast.makeText(getActivity(), "用户名已经存在", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                        closeProgressDialog();
                    }
                });

            }
        });

    }

}
