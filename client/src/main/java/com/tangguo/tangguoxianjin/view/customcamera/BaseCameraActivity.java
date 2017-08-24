package com.tangguo.tangguoxianjin.view.customcamera;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;


public abstract class BaseCameraActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
        preInitData();
    }

    protected abstract int getContentViewResId();

    protected abstract void preInitData();



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

