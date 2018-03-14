package com.tangguo.tangguoxianjin.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;

import org.json.JSONObject;

public class BorrowHelpActivity extends BaseActivity {

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_help);
        setTitle("借款攻略");
        basePageView.setRight(123);
    }

}
