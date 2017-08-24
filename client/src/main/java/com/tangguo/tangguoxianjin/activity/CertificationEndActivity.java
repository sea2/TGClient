package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.view.TitleLayout;

public class CertificationEndActivity extends BaseActivity {

    private android.widget.TextView tvcertifyendinfo;
    private android.widget.TextView tvcertifyendremark;
    private android.widget.TextView tvcertifyendline;
    private android.widget.RadioButton rbmoneyone;
    private android.widget.RadioButton rbmoneytwo;
    private android.widget.RadioButton rbmoneythree;
    private android.widget.RadioGroup rgmoney;
    private android.widget.RadioButton rbtimelimitone;
    private android.widget.RadioButton rbtimelimittwo;
    private android.widget.RadioButton rbtimelimitthree;
    private android.widget.RadioGroup rgtimelimit;
    private TextView tvcost;
    private TextView tvcomemoney;
    private android.widget.ScrollView svborrowmoneymain;
    private android.widget.Button btnconfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_end);


        initTitle();
        initView();
        initListener();
        initData();
    }

    private void initTitle() {
        setTitle("认证完成");
        if (basePageView != null) {
            basePageView.setBack(new TitleLayout.OnBackListener() {
                @Override
                public void onBack() {

                }
            });
        }
        hideBack();
    }

    private void initView() {
        this.tvcertifyendline = (TextView) findViewById(R.id.tv_certify_end_line);
        this.tvcertifyendremark = (TextView) findViewById(R.id.tv_certify_end_remark);
        this.tvcertifyendinfo = (TextView) findViewById(R.id.tv_certify_end_info);
        this.btnconfirm = (Button) findViewById(R.id.btn_confirm);
        this.svborrowmoneymain = (ScrollView) findViewById(R.id.sv_borrow_money_main);
        this.tvcomemoney = (TextView) findViewById(R.id.tv_come_money);
        this.tvcost = (TextView) findViewById(R.id.tv_cost);
        this.rgtimelimit = (RadioGroup) findViewById(R.id.rg_time_limit);
        this.rbtimelimitthree = (RadioButton) findViewById(R.id.rb_time_limit_three);
        this.rbtimelimittwo = (RadioButton) findViewById(R.id.rb_time_limit_two);
        this.rbtimelimitone = (RadioButton) findViewById(R.id.rb_time_limit_one);
        this.rgmoney = (RadioGroup) findViewById(R.id.rg_money);
        this.rbmoneythree = (RadioButton) findViewById(R.id.rb_money_three);
        this.rbmoneytwo = (RadioButton) findViewById(R.id.rb_money_two);
        this.rbmoneyone = (RadioButton) findViewById(R.id.rb_money_one);
        this.tvcertifyendline = (TextView) findViewById(R.id.tv_certify_end_line);
        this.tvcertifyendremark = (TextView) findViewById(R.id.tv_certify_end_remark);
        this.tvcertifyendinfo = (TextView) findViewById(R.id.tv_certify_end_info);
    }

    private void initListener() {
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAc(BorrowMoneyEndActivity.class);
            }
        });
    }

    private void initData() {
        tvcertifyendinfo.setText("恭喜你，极速认证成功");
        tvcertifyendremark.setText("XX系统智能分析，您的授信额度");
        String lineStr = "500<font color='#4d4d4d'>元</font>";
        tvcertifyendline.setText(Html.fromHtml(lineStr));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_BACK == keyCode) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
