package com.tangguo.tangguoxianjin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.activity.CertificationCenterActivity;
import com.tangguo.tangguoxianjin.activity.MainActivity;
import com.tangguo.tangguoxianjin.activity.SesameCreditActivity;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.common.BaseFragment;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.model.AccountBaseInfo;
import com.tangguo.tangguoxianjin.model.AuthStatusInfo;
import com.tangguo.tangguoxianjin.net.HttpRequestService;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.IntentUtil;
import com.tangguo.tangguoxianjin.util.LogManager;
import com.tangguo.tangguoxianjin.util.ScreenUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.view.AnimationNumberView;

import java.util.HashMap;

/**
 * @author lhy
 */
public class CertifyPageFragment extends BaseFragment {
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private RelativeLayout rl_add_lines_title;
    private AnimationNumberView tv_lines_new;
    private TextView tv_lines_new_sign;
    private TextView tv_cerfity_id, tv_cerfity_phone, tv_cerfity_contact, tv_cerfity_bank, tv_cerfity_sesame;
    private LinearLayout ll_certify_id, ll_certify_phone;
    private LinearLayout ll_certify_contact;
    private LinearLayout ll_certify_bank;
    private LinearLayout ll_certify_sesame;
    private AccountBaseInfo mAccountBaseInfo;
    private AuthStatusInfo mAuthStatusInfo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_certify_main_layout, container, false);
            Log.i("TAG", "twoFragment--onCreateView");
            rl_add_lines_title = (RelativeLayout) view.findViewById(R.id.rl_add_lines_title);
            tv_lines_new = (AnimationNumberView) view.findViewById(R.id.tv_lines_new);
            tv_lines_new_sign = (TextView) view.findViewById(R.id.tv_lines_new_sign);
            tv_cerfity_id = (TextView) view.findViewById(R.id.tv_cerfity_id);
            tv_cerfity_phone = (TextView) view.findViewById(R.id.tv_cerfity_phone);
            tv_cerfity_bank = (TextView) view.findViewById(R.id.tv_cerfity_bank);
            tv_cerfity_contact = (TextView) view.findViewById(R.id.tv_cerfity_contact);
            tv_cerfity_sesame = (TextView) view.findViewById(R.id.tv_cerfity_sesame);
            ll_certify_id = (LinearLayout) view.findViewById(R.id.ll_certify_id);
            ll_certify_phone = (LinearLayout) view.findViewById(R.id.ll_certify_phone);
            ll_certify_contact = (LinearLayout) view.findViewById(R.id.ll_certify_contact);
            ll_certify_bank = (LinearLayout) view.findViewById(R.id.ll_certify_bank);
            ll_certify_sesame = (LinearLayout) view.findViewById(R.id.ll_certify_sesame);
            isPrepared = true;
            lazyLoad();

        }
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int heightStuta = ScreenUtil.getStatusBarHeight(context);
        lp.setMargins(0, heightStuta, 0, 0);
        rl_add_lines_title.setLayoutParams(lp);
        initListener();
    }

    private void initListener() {
        //身份认证
        ll_certify_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                Bundle mBundle = new Bundle();
                mBundle.putInt(MyConstants.CERTIFICATION_STEP, MyConstants.CERTIFICATION_STEP_ID);
                mBundle.putInt(MyConstants.CERTIFICATION_SHOW_TOP, 0);
                startAc(CertificationCenterActivity.class, mBundle);
            }
        });
        //芝麻信用
        ll_certify_sesame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                if (getStepStatus(2)) {
                    startAc(SesameCreditActivity.class);
                    getActivity().overridePendingTransition(R.anim.activity_translate_up_into, R.anim.activity_alpha_out);
                } else showToast("请先完成身份认证");
            }
        });
        //手机认证
        ll_certify_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                if (getStepStatus(2)) {
                    Bundle mBundle = new Bundle();
                    mBundle.putInt(MyConstants.CERTIFICATION_STEP, MyConstants.CERTIFICATION_STEP_PHONE);
                    mBundle.putInt(MyConstants.CERTIFICATION_STEP_ONLY_PART, MyConstants.CERTIFICATION_STEP_ONLY_PART_PHONE);
                    mBundle.putInt(MyConstants.CERTIFICATION_SHOW_TOP, 0);
                    startAc(CertificationCenterActivity.class, mBundle);
                }
            }
        });
        //联系人
        ll_certify_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                if (getStepStatus(2)) {
                    Bundle mBundle = new Bundle();
                    mBundle.putInt(MyConstants.CERTIFICATION_STEP, MyConstants.CERTIFICATION_STEP_PHONE);
                    mBundle.putInt(MyConstants.CERTIFICATION_STEP_ONLY_PART, MyConstants.CERTIFICATION_STEP_ONLY_PART_CONTACT);
                    mBundle.putInt(MyConstants.CERTIFICATION_SHOW_TOP, 0);
                    startAc(CertificationCenterActivity.class, mBundle);
                }
            }
        });
        //银行卡认证
        ll_certify_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                if (getStepStatus(2)) {
                    Bundle mBundle = new Bundle();
                    mBundle.putInt(MyConstants.CERTIFICATION_STEP, MyConstants.CERTIFICATION_STEP_BANK);
                    //  mBundle.putInt(MyConstants.CERTIFICATION_STEP_ONLY_PART, MyConstants.CERTIFICATION_STEP_ONLY_PART_CONTACT);
                    mBundle.putInt(MyConstants.CERTIFICATION_SHOW_TOP, 0);
                    startAc(CertificationCenterActivity.class, mBundle);
                }
            }
        });

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        Log.i("TAG", "twoFragment--lazyLoad");
        requestData();
    }

    private void requestData() {


        requestAccount();
        requestCertifyStatus();


    }


    /**
     * 认证状态
     *
     * @param mTv
     * @param type
     */
    private void setColorText(TextView mTv, int type) {
        if (type == 1) {
            mTv.setTextColor(ContextCompat.getColor(context, R.color.tv_color_b3b3b3));
            mTv.setText("已完成");
        } else {
            mTv.setTextColor(ContextCompat.getColor(context, R.color.color_00c2cb));
            mTv.setText("待完成");
        }
    }

    private void setColorSesameText(TextView mTv, int type) {
        if (type == 1) {
            mTv.setTextColor(ContextCompat.getColor(context, R.color.tv_color_b3b3b3));
            mTv.setText("已授权");
        } else {
            mTv.setTextColor(ContextCompat.getColor(context, R.color.color_00c2cb));
            mTv.setText("待授权");
        }
    }


    /**
     * 个人信息
     */
    private void requestAccount() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        requestNetData(UrlConstans.ACCOUNT_BASE, map, false, MyConstants.HttpMethod.HTTP_POST, UrlConstans.ACCOUNT_BASE_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        mAccountBaseInfo = gson.fromJson(json, new TypeToken<AccountBaseInfo>() {
                        }.getType());
                        if (mAccountBaseInfo != null) {
                            AccountInfoUtils.setAccountPhone(context, mAccountBaseInfo.getPhone());
                            String linesMoney = mAccountBaseInfo.getBalance();
                            try {
                                LogManager.i(Double.parseDouble(linesMoney) + "");
                            } catch (Exception e) {
                                tv_lines_new_sign.setVisibility(View.GONE);
                            }
                            tv_lines_new.showNumberWithAnimation("0", linesMoney, false);
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

        HttpRequestService.requestCertifyStatus(context, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        mAuthStatusInfo = gson.fromJson(json, new TypeToken<AuthStatusInfo>() {
                        }.getType());
                        //认证状态
                        setColorText(tv_cerfity_id, StringUtils.toInt(mAuthStatusInfo.getIdentityVtStatus(), 0));
                        setColorText(tv_cerfity_phone, StringUtils.toInt(mAuthStatusInfo.getPhoneVtStatus(), 0));
                        setColorText(tv_cerfity_contact, StringUtils.toInt(mAuthStatusInfo.getContactVtStatus(), 0));
                        setColorText(tv_cerfity_bank, StringUtils.toInt(mAuthStatusInfo.getBankCardVtStatus(), 0));
                        setColorSesameText(tv_cerfity_sesame, StringUtils.toInt(mAuthStatusInfo.getSesameVtStatus(), 0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    setColorText(tv_cerfity_id, 0);
                    setColorText(tv_cerfity_phone, 0);
                    setColorText(tv_cerfity_contact, 0);
                    setColorText(tv_cerfity_bank, 0);
                    setColorSesameText(tv_cerfity_sesame, 0);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) requestData();
    }


    private boolean getStepStatus(int step) {
        boolean status = false;
        if (mAuthStatusInfo != null) {
            switch (step) {
                case 2:
                    if (StringUtils.isEquals(mAuthStatusInfo.getIdentityVtStatus(), "1")) status = true;
                    else {
                        showToast("请先完成身份认证");
                    }
                    break;
                case 3:
                    if (StringUtils.isEquals(mAuthStatusInfo.getIdentityVtStatus(), "1")) {
                        if (StringUtils.isEquals(mAuthStatusInfo.getSesameVtStatus(), "1")) status = true;
                        else showToast("请先认证芝麻信用");
                    } else {
                        showToast("请先完成身份认证");
                    }
                    break;
                case 4:
                    if (StringUtils.isEquals(mAuthStatusInfo.getIdentityVtStatus(), "1")) {
                        if (StringUtils.isEquals(mAuthStatusInfo.getSesameVtStatus(), "1")) {
                            if (StringUtils.isEquals(mAuthStatusInfo.getPhoneVtStatus(), "1")) status = true;
                            else showToast("请先完成手机认证");
                        } else showToast("请先认证芝麻信用");
                    } else {
                        showToast("请先完成身份认证");
                    }
                    break;
                case 5:
                    if (StringUtils.isEquals(mAuthStatusInfo.getIdentityVtStatus(), "1")) {
                        if (StringUtils.isEquals(mAuthStatusInfo.getSesameVtStatus(), "1")) {
                            if (StringUtils.isEquals(mAuthStatusInfo.getPhoneVtStatus(), "1")) {
                                if (StringUtils.isEquals(mAuthStatusInfo.getContactVtStatus(), "1")) status = true;
                                else showToast("请先完成紧急联系人认证");
                            } else showToast("请先完成手机认证");
                        } else showToast("请先认证芝麻信用");
                    } else {
                        showToast("请先完成身份认证");
                    }
                    break;
            }
        }
        return status;
    }
}
