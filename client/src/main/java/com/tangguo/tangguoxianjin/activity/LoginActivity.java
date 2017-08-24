package com.tangguo.tangguoxianjin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseVerifyActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.model.LoginInfo;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.KeyBoardUtils;
import com.tangguo.tangguoxianjin.util.ScreenUtil;
import com.tangguo.tangguoxianjin.util.SharePreferenceUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.util.TextViewUtil;
import com.tangguo.tangguoxianjin.view.EditTextWithDel;

import org.json.JSONObject;

import java.util.HashMap;


public class LoginActivity extends BaseVerifyActivity {

    private String phoneNum = "";
    private Class<?> backClass = null;
    private EditTextWithDel etphonenum;
    private Button btnconfirm;
    private EditText etsecuritycode;
    private Button btngetcode;
    private EditText edphonepassword;
    private CheckBox cbshoushipassword;
    private LinearLayout llphoneinfo;
    private Button btnconfirmregister;
    private LinearLayout llinputtwoinfo;
    private EditText etloginpassword;
    private Button btnconfirmlogin;
    private LinearLayout llinputlogininfo;
    private CheckBox cbprotocol;
    private LinearLayout protocolcontainer;
    private android.widget.TextView tvprotocol;
    private Button btnforgetpassword;
    private LoginInfo mLoginInfo;
    private String verification_no;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);


        initIntent();
        initViews();
        setTitle("注册/登录");
        addListener();
        initData();

    }


    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            this.backClass = (Class) intent.getSerializableExtra(MyConstants.CLASS_FROM_ACTIVITY);
        }
    }


    protected void initViews() {
        this.btnforgetpassword = (Button) findViewById(R.id.btn_forget_password);
        this.tvprotocol = (TextView) findViewById(R.id.tv_protocol);
        this.llinputlogininfo = (LinearLayout) findViewById(R.id.ll_input_login_info);
        this.btnconfirmlogin = (Button) findViewById(R.id.btn_confirm_login);
        this.etloginpassword = (EditText) findViewById(R.id.et_login_password);
        this.llinputtwoinfo = (LinearLayout) findViewById(R.id.ll_input_two_info);
        this.btnconfirmregister = (Button) findViewById(R.id.btn_confirm_register);
        this.protocolcontainer = (LinearLayout) findViewById(R.id.protocol_container);
        this.cbprotocol = (CheckBox) findViewById(R.id.cb_protocol);
        this.llphoneinfo = (LinearLayout) findViewById(R.id.ll_phone_info);
        this.cbshoushipassword = (CheckBox) findViewById(R.id.cb_shoushi_password);
        this.edphonepassword = (EditText) findViewById(R.id.ed_phone_password);
        this.btngetcode = (Button) findViewById(R.id.btn_get_code);
        this.etsecuritycode = (EditText) findViewById(R.id.et_security_code);
        this.btnconfirm = (Button) findViewById(R.id.btn_confirm);
        this.etphonenum = (EditTextWithDel) findViewById(R.id.et_phone_num);
        initCountdown(VERIFY_CODE_REGISTER);

    }


    private void addListener() {
        //注册或登录
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNum = TextViewUtil.getText(etphonenum);
                SharePreferenceUtil.getInstance().save(MyConstants.LOGIN_INPUT_PHONE, phoneNum, LoginActivity.this);
                requestNumData(phoneNum);
            }
        });
        //注册验证码
        btngetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCanRequestCode()) {
                    return;
                }
                requestCodeData();

            }
        });
        //注册
        btnconfirmregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(TextViewUtil.getText(etsecuritycode))) {
                    showToast("请输入验证码");
                } else if (StringUtils.isEmpty(TextViewUtil.getText(edphonepassword))) {
                    showToast("请设置登录密码");
                } else if (TextViewUtil.getText(edphonepassword).length() < 6) {
                    showToast("请输6位以上登录密码");
                } else {
                    requestRegisterData(TextViewUtil.getText(etsecuritycode), TextViewUtil.getText(edphonepassword));
                }
            }
        });
        //忘记密码
        btnforgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mBundle = new Bundle();
                mBundle.putString("phoneNum", phoneNum);
                startAc(ResetLoginPasswordActivity.class, mBundle);
            }
        });
        //登录
        btnconfirmlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(TextViewUtil.getText(etloginpassword))) {
                    showToast("请输入登录密码");
                } else if (TextViewUtil.getText(etloginpassword).length() < 6) {
                    showToast("请输6位以上登录密码");
                } else {
                    requestLoginData(TextViewUtil.getText(etloginpassword));
                }
            }
        });
        //协议选择
        cbprotocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnconfirmregister.setClickable(true);
                    btnconfirmregister.setBackgroundResource(R.drawable.btn_style_theme_selector);

                } else {
                    btnconfirmregister.setClickable(false);
                    btnconfirmregister.setBackgroundResource(R.color.color_theme_press);
                }
            }
        });

        cbshoushipassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edphonepassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    edphonepassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });

        etphonenum.addTextChangedListener(new TextChangeListener(etphonenum));

        tvprotocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginInfo != null && (!StringUtils.isEmpty(mLoginInfo.getAgreement_url()))) {
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(MyConstants.CLASS_FROM_ACTIVITY, LoginActivity.class);
                    mBundle.putString("web_view_url", mLoginInfo.getAgreement_url());
                    startAc(WebActivity.class, mBundle);
                }
            }
        });
    }


    private void initData() {
        //输入曾今输入过的手机号
        String input_phone = SharePreferenceUtil.getInstance().getValue(MyConstants.LOGIN_INPUT_PHONE, LoginActivity.this);
        if (!StringUtils.isEmpty(input_phone)) {
            etphonenum.setText(input_phone);
            etphonenum.setSelection(etphonenum.getText().length());
        }
    }

    /**
     * 发送验证码
     */
    private void requestCodeData() {
        if (StringUtils.isEmpty(phoneNum)) {
            showToast("手机号为空");
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", phoneNum);
        map.put("type", "0");
        map.put("sms_type", "1");
        requestNetData(UrlConstans.VERIFICATION_SENDSMS, map, true, MyConstants.HttpMethod.HTTP_POST, UrlConstans.VERIFICATION_SENDSMS_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        JSONObject jb = new JSONObject(json);
                        if (jb.has("verification_no")) verification_no = jb.getString("verification_no");
                        if (!StringUtils.isEmpty(verification_no)) {
                            showToast("短信已下发至您手机,请注意查收!");
                            SharePreferenceUtil.getInstance().save(MyConstants.VERIFICATION_NO.concat(phoneNum), verification_no, LoginActivity.this);
                            if (!btngetcode.isSelected()) {
                                openCountDown(false);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 注册
     */
    private void requestRegisterData(String code, String password) {
        if (StringUtils.isEmpty(verification_no)) {
            String verification_no_str = SharePreferenceUtil.getInstance().getValue(MyConstants.VERIFICATION_NO.concat(phoneNum), LoginActivity.this);
            if (!StringUtils.isEmpty(verification_no_str)) verification_no = verification_no_str;
            else {
                showToast("请发送验证码");
                return;
            }
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("password", password);
        map.put("phone", phoneNum);
        map.put("verification_code", code);
        map.put("verification_no", verification_no);
        requestNetData(UrlConstans.ACCOUNT_REGIST, map, true, MyConstants.HttpMethod.HTTP_POST, UrlConstans.ACCOUNT_REGIST_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        JSONObject jb = new JSONObject(json);
                        if (jb.has("uid")) {
                            String uid = jb.getString("uid");
                            AccountInfoUtils.setUid(LoginActivity.this, uid);
                        }
                        if (jb.has("phone")) {
                            String phone = jb.getString("phone");
                            AccountInfoUtils.setAccountPhone(LoginActivity.this, phone);
                        }
                        if (!StringUtils.isEmpty(AccountInfoUtils.getUid(LoginActivity.this))) {
                            showToast("注册成功");
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 登录
     */
    private void requestLoginData(String password) {
        AccountInfoUtils.setUid(LoginActivity.this, "123456");
        finish();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("password", password);
        map.put("phone", phoneNum);
        requestNetData(UrlConstans.ACCOUNT_LOGIN, map, true, MyConstants.HttpMethod.HTTP_POST, UrlConstans.ACCOUNT_LOGIN_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {

                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        JSONObject jb = new JSONObject(json);
                        if (jb.has("uid")) {
                            String uid = jb.getString("uid");
                            AccountInfoUtils.setUid(LoginActivity.this, uid);
                        }
                        if (jb.has("phone")) {
                            String phone = jb.getString("phone");
                            AccountInfoUtils.setAccountPhone(LoginActivity.this, phone);
                        }
                        if (!StringUtils.isEmpty(AccountInfoUtils.getUid(LoginActivity.this))) {
                            showToast("登录成功");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 检测手机号是注册还是登录
     *
     * @param phoneNum
     */
    private void requestNumData(String phoneNum) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", phoneNum);
        requestNetData(UrlConstans.LOGIN_OPEN, map, true, MyConstants.HttpMethod.HTTP_GET, UrlConstans.LOGIN_OPEN_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        mLoginInfo = gson.fromJson(json, new TypeToken<LoginInfo>() {
                        }.getType());
                        if (mLoginInfo != null) {
                            if (StringUtils.isEquals(mLoginInfo.getStatus(), "0")) selectLoginRegister(true);
                            else if (StringUtils.isEquals(mLoginInfo.getStatus(), "2")) selectLoginRegister(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void selectLoginRegister(boolean registerFlog) {
        if (registerFlog) {
            setTitle("注册");
            if (mLoginInfo != null) {
                tvprotocol.setText(Html.fromHtml(mLoginInfo.getAgreement_title()));
            }
            ObjectAnimator animator = ObjectAnimator.ofFloat(llinputtwoinfo, "translationX", new ScreenUtil(LoginActivity.this).getWidth(), 0f);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    btnconfirm.setClickable(false);
                    llinputtwoinfo.setVisibility(View.VISIBLE);
                    etsecuritycode.requestFocus();//get the focus
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.setDuration(400);
            animator.start();
        } else {
            setTitle("登录");
            ObjectAnimator animator = ObjectAnimator.ofFloat(llinputlogininfo, "translationX", new ScreenUtil(LoginActivity.this).getWidth(), 0f);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    btnconfirm.setClickable(false);
                    llinputlogininfo.setVisibility(View.VISIBLE);
                    etloginpassword.requestFocus();//get the focus
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    KeyBoardUtils.showSoftInput(etloginpassword, LoginActivity.this);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.setDuration(400);
            animator.start();
        }
    }


    private class TextChangeListener implements TextWatcher {
        private EditTextWithDel et_phone_num;

        public TextChangeListener(EditTextWithDel et_phone_num) {
            this.et_phone_num = et_phone_num;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = et_phone_num.getText().toString().trim();
            if (et_phone_num != null && text.length() >= 11) {
                btnconfirm.setBackgroundResource(R.drawable.btn_style_theme_selector);
                btnconfirm.setEnabled(true);
            } else {
                btnconfirm.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.color.color_theme_press));
                btnconfirm.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    protected Button getBtnGetCodeView() {
        return (Button) findViewById(R.id.btn_get_code);
    }

    @Override
    public void finish() {
        KeyBoardUtils.hideInputMethod(LoginActivity.this);
        super.finish();
        overridePendingTransition(0, R.anim.activity_translate_down_out);
    }

}
