package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseVerifyActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.model.AuthStatusInfo;
import com.tangguo.tangguoxianjin.net.HttpRequestService;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.view.CustomImageButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BorrowMoneyActivity extends BaseVerifyActivity {

    private com.tangguo.tangguoxianjin.view.CustomImageButton btnaddlines;
    private android.widget.RadioButton rbmoneyone;
    private android.widget.RadioButton rbmoneytwo;
    private android.widget.RadioButton rbmoneythree;
    private android.widget.RadioGroup rgmoney;
    private android.widget.RadioButton rbtimelimitone;
    private android.widget.RadioButton rbtimelimittwo;
    private android.widget.RadioButton rbtimelimitthree;
    private android.widget.RadioGroup rgtimelimit;
    private android.widget.TextView tvcost;
    private com.tangguo.tangguoxianjin.view.CustomImageButton btncostdetail;
    private android.widget.TextView tvcomemoney;
    private android.widget.EditText edphonenum;
    private android.widget.EditText etsecuritycode;
    private android.widget.Button btngetcode;
    private android.widget.LinearLayout llphoneinfo;
    private android.widget.CheckBox cbprotocol;
    private android.widget.LinearLayout protocolcontainer;
    private android.widget.Button btnconfirm;
    private android.widget.ScrollView svborrowmoneymain;
    private TextView tvprotocol;
    private String protocolUrl;
    private String borrowDay = "";
    private String borrowAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_money);


        initView();
        initTitle();
        initListener();
        initData();
    }

    private void initView() {
        this.btnconfirm = (Button) findViewById(R.id.btn_confirm);
        this.protocolcontainer = (LinearLayout) findViewById(R.id.protocol_container);
        this.tvprotocol = (TextView) findViewById(R.id.tv_protocol);
        this.cbprotocol = (CheckBox) findViewById(R.id.cb_protocol);
        this.svborrowmoneymain = (ScrollView) findViewById(R.id.sv_borrow_money_main);
        this.llphoneinfo = (LinearLayout) findViewById(R.id.ll_phone_info);
        this.btngetcode = (Button) findViewById(R.id.btn_get_code);
        this.etsecuritycode = (EditText) findViewById(R.id.et_security_code);
        this.edphonenum = (EditText) findViewById(R.id.ed_phone_num);
        this.tvcomemoney = (TextView) findViewById(R.id.tv_come_money);
        this.btncostdetail = (CustomImageButton) findViewById(R.id.btn_cost_detail);
        this.tvcost = (TextView) findViewById(R.id.tv_cost);
        this.rgtimelimit = (RadioGroup) findViewById(R.id.rg_time_limit);
        this.rbtimelimitthree = (RadioButton) findViewById(R.id.rb_time_limit_three);
        this.rbtimelimittwo = (RadioButton) findViewById(R.id.rb_time_limit_two);
        this.rbtimelimitone = (RadioButton) findViewById(R.id.rb_time_limit_one);
        this.rgmoney = (RadioGroup) findViewById(R.id.rg_money);
        this.rbmoneythree = (RadioButton) findViewById(R.id.rb_money_three);
        this.rbmoneytwo = (RadioButton) findViewById(R.id.rb_money_two);
        this.rbmoneyone = (RadioButton) findViewById(R.id.rb_money_one);
        this.btnaddlines = (CustomImageButton) findViewById(R.id.btn_add_lines);
    }

    private void initTitle() {
        if (basePageView != null) {
            basePageView.setTitle("借款");
        }
    }

    private void initListener() {

        rgmoney.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_money_one:
                        totleEndMoney(true);
                        break;
                    case R.id.rb_money_two:
                        totleEndMoney(true);
                        break;
                    default:
                        break;
                }
            }
        });
        rgtimelimit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                totleEndMoney(true);
            }
        });
        //借款按钮
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCertifyStatus();
            }
        });
        //协议
        tvprotocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmpty(protocolUrl)) {
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(MyConstants.CLASS_FROM_ACTIVITY, BorrowMoneyActivity.class);
                    mBundle.putString("web_view_url", protocolUrl);
                    startAc(WebActivity.class, mBundle);
                }
            }
        });

        edphonenum.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    if (svborrowmoneymain != null) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                svborrowmoneymain.smoothScrollTo(0, svborrowmoneymain.getChildAt(0).getHeight());
                            }
                        }, 500);
                    }
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });


        cbprotocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnconfirm.setClickable(true);
                    btnconfirm.setBackgroundResource(R.drawable.btn_style_theme_selector);

                } else {
                    btnconfirm.setClickable(false);
                    btnconfirm.setBackgroundResource(R.color.color_theme_press);
                }
            }
        });

    }

    private void initData() {
        btnconfirm.setText("申请借款");
        totleEndMoney(true);
        requestConfig();
    }

    /**
     * 判断选择项，计算费用
     */
    private void totleEndMoney(boolean flag) {
        borrowDay = "7";
        borrowAmount = "500";
        if (rgmoney != null) {
            switch (rgmoney.getCheckedRadioButtonId()) {
                case R.id.rb_money_one:
                    borrowAmount = "500";
                    break;
                case R.id.rb_money_two:
                    borrowAmount = "1000";
                    break;
                default:
                    break;
            }
        }

        if (rgtimelimit != null) {
            switch (rgtimelimit.getCheckedRadioButtonId()) {
                case R.id.rb_time_limit_one:
                    borrowDay = "7";
                    break;
                case R.id.rb_time_limit_two:
                    borrowDay = "14";
                    break;
                case R.id.rb_time_limit_three:
                    borrowDay = "28";
                    break;
                default:
                    break;
            }
        }

        requestTotleMoney(flag);
    }


    /**
     * 计算选项金额
     */
    private void requestTotleMoney(boolean flag) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("borrowDay", borrowDay);
        map.put("borrowAmount", borrowAmount);
        map.put("uid", AccountInfoUtils.getUid(BorrowMoneyActivity.this));
        requestNetData(UrlConstans.BORROW_CALCMONEY, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.BORROW_CALCMONEY_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        JSONObject jb = new JSONObject(json);
                        if (jb.has("realMoney")) {
                            String realMoney = jb.getString("realMoney");
                            tvcomemoney.setText(realMoney);
                        }
                        if (jb.has("cost")) {
                            String cost = jb.getString("cost");
                            tvcost.setText(cost);
                        }
                        if (jb.has("repayMoney")) {
                            String repayMoney = jb.getString("repayMoney");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 配置信息
     */
    private void requestConfig() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(BorrowMoneyActivity.this));
        requestNetData(UrlConstans.BORROW_CONFIG, map, true, MyConstants.HttpMethod.HTTP_POST, UrlConstans.BORROW_CONFIG_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        JSONObject jb = new JSONObject(json);
                        if (jb.has("borrowButtonText")) {
                            String borrowButtonText = jb.getString("borrowButtonText");
                            if (!StringUtils.isEmpty(borrowButtonText)) btnconfirm.setText(Html.fromHtml(borrowButtonText));
                        }
                        if (jb.has("protocolTitle")) {
                            String protocolTitle = jb.getString("protocolTitle");
                            if (!StringUtils.isEmpty(protocolTitle)) tvprotocol.setText(Html.fromHtml(protocolTitle));
                        }
                        if (jb.has("protocolUrl")) {
                            protocolUrl = jb.getString("protocolUrl");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 认证状态
     */
    private void requestCertifyStatus() {
        showProgressDialog();
        HttpRequestService.requestCertifyStatus(BorrowMoneyActivity.this, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        AuthStatusInfo mAuthStatusInfo = gson.fromJson(json, new TypeToken<AuthStatusInfo>() {
                        }.getType());
                        if (StringUtils.isEquals(mAuthStatusInfo.getBankCardVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getIdentityVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getContactVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getPhoneVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getSesameVtStatus(), "1")) {
                            requestBorrowMoney();
                        } else {
                            startAc(CertificationCenterActivity.class);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dismissProgressDialog();
            }
        });
    }

    /**
     * 发起借款
     */
    private void requestBorrowMoney() {
        if (isInRequestQueue(UrlConstans.BORROW_SUBMIT_CODE)) {
            return;
        }
        totleEndMoney(false);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("borrowDay", borrowDay);
        map.put("borrowAmount", borrowAmount);
        map.put("uid", AccountInfoUtils.getUid(BorrowMoneyActivity.this));
        requestNetData(UrlConstans.BORROW_SUBMIT, map, true, MyConstants.HttpMethod.HTTP_POST, UrlConstans.BORROW_SUBMIT_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        JSONObject jb = new JSONObject(json);
                        String recordId = "";
                        if (jb.has("recordId")) {
                            recordId = jb.getString("recordId");
                        }
                        if (jb.has("status")) {
                            String status = jb.getString("status");
                        }
                        if (!StringUtils.isEmpty(recordId)) {
                            Bundle mb = new Bundle();
                            mb.putString("recordId", recordId);
                            startAc(BorrowMoneyEndActivity.class, mb);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected Button getBtnGetCodeView() {
        return btngetcode;
    }
}
