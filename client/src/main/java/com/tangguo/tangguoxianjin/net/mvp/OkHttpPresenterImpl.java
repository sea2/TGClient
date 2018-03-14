package com.tangguo.tangguoxianjin.net.mvp;

import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.net.OkHttpListener;
import com.tangguo.tangguoxianjin.net.OkHttpManager;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by lhy on 2018/3/14.
 */

public class OkHttpPresenterImpl implements OkHttpPresenter, OkHttpListener {

    OkHttpListener listener;
    OkHttpManager mOkHttpManager;
    HashSet<Integer> listOkHttp = new HashSet<Integer>();

    public OkHttpPresenterImpl(OkHttpListener listener) {
        this.listener = listener;
    }


    @Override
    public void login(String name, String password) {
        int requestCode = UrlConstans.LOGIN_OPEN_CODE;
        String url = UrlConstans.LOGIN_OPEN;
        HashMap<String, String> map = new HashMap<>();
        map.put("phone", name);
        new OkHttpManager.Builder(url).setResquestCode(requestCode).setResquestParam(map).Builder(this);
        listOkHttp.add(requestCode);
    }

    @Override
    public void getBaseInfo(String uid) {

    }

    @Override
    public void onDestroy() {
        listener = null;
        if (listOkHttp != null) {
            for (Integer requestCode : listOkHttp) {
                OkHttpUtils.getInstance().cancelTag(requestCode);
            }
        }
    }

    @Override
    public void OnSuccess(int requestCode, JSONObject json) {
        listOkHttp.remove(requestCode);
    }

    @Override
    public void OnError(int requestCode, String json) {
        listOkHttp.remove(requestCode);
    }
}
