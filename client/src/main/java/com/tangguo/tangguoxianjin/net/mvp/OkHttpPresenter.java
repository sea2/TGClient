package com.tangguo.tangguoxianjin.net.mvp;

/**
 * Created by lhy on 2018/3/14.
 */

public interface OkHttpPresenter {




    void  login(String name,String password);
    void  getBaseInfo(String uid);
    void  onDestroy();


}
