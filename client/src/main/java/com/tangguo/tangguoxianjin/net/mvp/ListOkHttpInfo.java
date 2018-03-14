package com.tangguo.tangguoxianjin.net.mvp;

import com.tangguo.tangguoxianjin.net.OkHttpManager;

/**
 * Created by lhy on 2018/3/14.
 */

public class ListOkHttpInfo {

    OkHttpManager mOkHttpManager;
    int requestCode;


    public ListOkHttpInfo(OkHttpManager mOkHttpManager, int requestCode) {
        this.mOkHttpManager = mOkHttpManager;
        this.requestCode = requestCode;
    }

    public OkHttpManager getmOkHttpManager() {
        return mOkHttpManager;
    }

    public void setmOkHttpManager(OkHttpManager mOkHttpManager) {
        this.mOkHttpManager = mOkHttpManager;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }
}
