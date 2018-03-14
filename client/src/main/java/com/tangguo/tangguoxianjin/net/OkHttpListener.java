package com.tangguo.tangguoxianjin.net;

import org.json.JSONObject;

/**
 * Created by lhy on 2018/3/13.
 */

public interface OkHttpListener {

    void OnSuccess(int requestCode,JSONObject json);
    void OnError(int requestCode,String json);
}
