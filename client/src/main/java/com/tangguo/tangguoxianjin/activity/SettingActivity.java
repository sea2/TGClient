package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.IntentUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;

public class SettingActivity extends BaseActivity {

    private android.widget.TextView tvsettingphone;
    private android.widget.LinearLayout llsettingresetpassword;
    private android.widget.Button btnout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        this.btnout = (Button) findViewById(R.id.btn_out);
        this.llsettingresetpassword = (LinearLayout) findViewById(R.id.ll_setting_reset_password);
        this.tvsettingphone = (TextView) findViewById(R.id.tv_setting_phone);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        setTitle("安全设置");
    }

    private void initListener() {
        llsettingresetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mb = new Bundle();
                mb.putInt("type", 1);
                startAc(ResetLoginPasswordActivity.class, mb);
            }
        });
        btnout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountInfoUtils.logoutClearLoginInfo();
                IntentUtil.backPageActivity(SettingActivity.this, MainActivity.class);
            }
        });


    }

    private void initData() {
        String phoneNum = AccountInfoUtils.getAccountPhone(SettingActivity.this);
        tvsettingphone.setText(StringUtils.setPhoneNumMask(phoneNum));

    }


}
