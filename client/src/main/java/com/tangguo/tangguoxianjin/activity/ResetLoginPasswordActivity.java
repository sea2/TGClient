package com.tangguo.tangguoxianjin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseVerifyActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.ScreenUtil;
import com.tangguo.tangguoxianjin.util.SharePreferenceUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.util.TextViewUtil;
import com.tangguo.tangguoxianjin.view.dialog.CommonDialog;

import org.json.JSONObject;

import java.util.HashMap;

public class ResetLoginPasswordActivity extends BaseVerifyActivity {


    private String phoneNum = "";
    private android.widget.EditText edname;
    private android.widget.EditText etidentity;
    private android.widget.Button btnnext;
    private android.widget.EditText etsecuritycode;
    private android.widget.Button btngetcode;
    private android.widget.EditText edphonepassword;
    private android.widget.CheckBox cbshoushipassword;
    private android.widget.LinearLayout llphoneinfo;
    private android.widget.CheckBox cbprotocol;
    private android.widget.TextView tvprotocol;
    private android.widget.LinearLayout protocolcontainer;
    private LinearLayout llinputreset;
    private Button btnconfirmok;
    private LinearLayout llcheckaccountid;
    private String verification_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_login_password);
        this.llcheckaccountid = (LinearLayout) findViewById(R.id.ll_check_account_id);
        this.btnconfirmok = (Button) findViewById(R.id.btn_confirm_ok);
        this.llinputreset = (LinearLayout) findViewById(R.id.ll_input_reset);
        this.protocolcontainer = (LinearLayout) findViewById(R.id.protocol_container);
        this.tvprotocol = (TextView) findViewById(R.id.tv_protocol);
        this.cbprotocol = (CheckBox) findViewById(R.id.cb_protocol);
        this.llphoneinfo = (LinearLayout) findViewById(R.id.ll_phone_info);
        this.cbshoushipassword = (CheckBox) findViewById(R.id.cb_shoushi_password);
        this.edphonepassword = (EditText) findViewById(R.id.ed_phone_password);
        this.btngetcode = (Button) findViewById(R.id.btn_get_code);
        this.etsecuritycode = (EditText) findViewById(R.id.et_security_code);
        this.btnnext = (Button) findViewById(R.id.btn_next);
        this.etidentity = (EditText) findViewById(R.id.et_identity);
        this.edname = (EditText) findViewById(R.id.ed_name);
        initIntent();
        initTitle();
        initListener();
        initCountdown(VERIFY_CODE_RESET);
    }


    private void initIntent() {
        Intent it = getIntent();
        phoneNum = it.getStringExtra("phoneNum");
        if (StringUtils.isEmpty(phoneNum)) phoneNum = AccountInfoUtils.getAccountPhone(ResetLoginPasswordActivity.this);
    }


    private void initTitle() {
        if (!StringUtils.isEmpty(AccountInfoUtils.getUid(ResetLoginPasswordActivity.this))) setTitle("修改登录密码");
        else setTitle("重置登录密码");
    }

    private void initListener() {
        //验证身份
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(TextViewUtil.getText(edname))) showToast("请输入姓名");
                else if (StringUtils.isEmpty(TextViewUtil.getText(etidentity))) showToast("请输入身份证号");
                else if (TextViewUtil.getText(etidentity).length() < 15) showToast("请输入正确的身份证号");
                else {
                    getAccount();
                }
            }
        });
        //密码隐藏显示
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

        //重置
        btnconfirmok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(TextViewUtil.getText(etsecuritycode))) {
                    showToast("请输入验证码");
                } else if (StringUtils.isEmpty(TextViewUtil.getText(edphonepassword))) {
                    showToast("请设置登录密码");
                } else if (TextViewUtil.getText(edphonepassword).length() < 6) {
                    showToast("请输6位以上登录密码");
                } else {
                    requestResetPassword(TextViewUtil.getText(etsecuritycode), TextViewUtil.getText(edphonepassword));
                }
            }
        });
        //验证码
        btngetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCanRequestCode()) {
                    return;
                }
                requestCodeData();
            }
        });
    }


    /**
     * 发送验证码
     */
    private void requestCodeData() {
        int type = 2;
        HashMap<String, String> map = new HashMap<String, String>();
        if (StringUtils.isEmpty(phoneNum)) {
            showToast("手机号为空");
            return;
        } else {
            map.put("phone", phoneNum);
        }


        map.put("type", String.valueOf(type));
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
                            SharePreferenceUtil.getInstance().save(MyConstants.VERIFICATION_NO.concat(phoneNum), verification_no, ResetLoginPasswordActivity.this);
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
     * 重置登录密码
     */
    private void requestResetPassword(String code, String password) {
        if (StringUtils.isEmpty(verification_no)) {
            String verification_no_str = SharePreferenceUtil.getInstance().getValue(MyConstants.VERIFICATION_NO.concat(phoneNum), ResetLoginPasswordActivity.this);
            if (!StringUtils.isEmpty(verification_no_str)) verification_no = verification_no_str;
            else {
                showToast("请发送验证码");
                return;
            }
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("new_password", password);
        map.put("phone", phoneNum);
        map.put("verification_code", code);
        map.put("verification_no", verification_no);
        requestNetData(UrlConstans.ACCOUNT_RESET_PASSWORD, map, true, MyConstants.HttpMethod.HTTP_PUT, UrlConstans.ACCOUNT_RESET_PASSWORD_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    showDialogResetPassword();
                }
            }
        });

    }

    private void getAccount() {

        setTitle("重置登录密码");
        ObjectAnimator animator = ObjectAnimator.ofFloat(llinputreset, "translationX", new ScreenUtil(ResetLoginPasswordActivity.this).getWidth(), 0f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                btnnext.setClickable(false);
                llinputreset.setVisibility(View.VISIBLE);
                etsecuritycode.requestFocus();
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

    }

    @Override
    protected Button getBtnGetCodeView() {
        return (Button) findViewById(R.id.btn_get_code);
    }


    private void showDialogResetPassword() {
        CommonDialog mDialog = new CommonDialog(this);
        mDialog.setTitle("温馨提示");
        if (!StringUtils.isEmpty(AccountInfoUtils.getUid(ResetLoginPasswordActivity.this))) mDialog.setContent("修改登录密码成功");
        else mDialog.setContent("重置登录密码成功，请登录");
        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDialog.show();
    }

}
