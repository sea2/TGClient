package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.util.IntentUtil;

public class SesameCreditActivity extends BaseActivity {

    private android.widget.ImageView tvzhimatop;
    private android.widget.TextView tvzhimascore;
    private android.widget.TextView tvzhimaremark;
    private android.widget.Button btnconfirmnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesame_credit);

        initTitle();
        initView();
        initListener();
        initData();
    }


    private void initView() {
        this.btnconfirmnext = (Button) findViewById(R.id.btn_confirm_next);
        this.tvzhimaremark = (TextView) findViewById(R.id.tv_zhima_remark);
        this.tvzhimascore = (TextView) findViewById(R.id.tv_zhima_score);
        this.tvzhimatop = (ImageView) findViewById(R.id.tv_zhima_top);
    }

    private void initTitle() {
        setTitle("芝麻信用");
    }

    private void initListener() {
        btnconfirmnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mBundle = new Bundle();
                mBundle.putInt(MyConstants.CERTIFICATION_STEP, 1);
                IntentUtil.backPageActivity(SesameCreditActivity.this, CertificationCenterActivity.class, mBundle);
                finish();
            }
        });
    }

    private void initData() {
        String scoreStr = "780<small><small><small>分</small></small></small>";
        tvzhimascore.setText(Html.fromHtml(scoreStr));
        tvzhimaremark.setText("您的信用不错哦");
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.activity_translate_down_out);
    }



}
