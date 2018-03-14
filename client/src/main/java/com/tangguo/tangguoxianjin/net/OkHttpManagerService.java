package com.tangguo.tangguoxianjin.net;

import com.tangguo.tangguoxianjin.config.MyConstants;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by lhy on 2018/3/13.
 */

public class OkHttpManagerService {

    OkHttpManager mOkHttpManager;
    OkHttpListener mListener;



    public OkHttpManagerService(String urlStr, int requestCode, HashMap<String, String> map, MyConstants.HttpMethod mHttpMethod, boolean isShow, final OkHttpListener listener) {
        mListener = listener;
        mOkHttpManager = new OkHttpManager.Builder(urlStr).setResquestCode(requestCode).setResquestParam(map).Builder(new OkHttpListener() {
            @Override
            public void OnSuccess(int requestCode, JSONObject json) {
                if (mListener != null) mListener.OnSuccess(requestCode, json);
            }

            @Override
            public void OnError(int requestCode, String json) {
                if (mListener != null) mListener.OnError(requestCode, json);
            }
        });
    }


    /**
     * 取消网络
     */
    public void cancelRequest() {
        if (mOkHttpManager != null)
            mOkHttpManager.cancelRequest();
        if (mListener != null) mListener = null;
    }


}
