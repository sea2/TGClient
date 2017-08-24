package com.tangguo.tangguoxianjin.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by lhy on 2017/5/24.
 */

public class GsonUtils<T> {

    public T fromJson(String json) {
        Gson gson = new Gson();
        T mT = gson.fromJson(json, new TypeToken<T>() {
        }.getType());
        return mT;
    }


    public String toJson(T mT) {
        Gson gson = new Gson();
        String json = gson.toJson(mT);
        return json;
    }


}
