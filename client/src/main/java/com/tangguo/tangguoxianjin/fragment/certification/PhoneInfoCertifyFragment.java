package com.tangguo.tangguoxianjin.fragment.certification;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.activity.CertificationCenterActivity;
import com.tangguo.tangguoxianjin.common.BaseVerifyFragment;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.eventbus.CertificationMessageEvent;
import com.tangguo.tangguoxianjin.model.AuthStatusInfo;
import com.tangguo.tangguoxianjin.model.ContactUrgentInfo;
import com.tangguo.tangguoxianjin.model.ListDialogInfo;
import com.tangguo.tangguoxianjin.model.PhoneServicePasswordInfo;
import com.tangguo.tangguoxianjin.net.HttpRequestService;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.ScreenUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.util.TextViewUtil;
import com.tangguo.tangguoxianjin.view.dialog.ListSelectDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PhoneInfoCertifyFragment extends BaseVerifyFragment {
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private Button btn_confirm;
    private Button btn_confirm_code;
    private Button btn_confirm_code_end;
    private LinearLayout ll_code;
    private LinearLayout ll_code_end;
    private TextView tv_code_end;
    private RelativeLayout rl_relate_top;
    private TextView tv_item_reletion_select;
    private TextView tv_item_right2_raletion;
    private EditText ed_phone_raletion, ed_phone_social;
    private LinearLayout ll_phone_serciry_num;
    private TextView tv_item_social_select;
    private RelativeLayout rl_social_top;
    private TextView tv_social_item_right2name;
    private Button btn_contact_submit;
    private LinearLayout ll_phone_contact;
    //是否是单个流程
    private boolean onlyPart = false;
    private int onlyPartInt = 0;
    private TextView tv_remark, tv_remark_serciry_num;
    //运行商密码
    private EditText ed_phone_serciry_num;
    private PhoneServicePasswordInfo mPhoneServicePasswordInfo;
    //发送验证码
    private Button btn_get_code;
    private LinearLayout ll_phone_serciry_code, ll_phone_serciry_code_image;
    private EditText ed_phone_serciry_code_image;
    private ImageView iv_phone_serciry_code_image;
    private EditText ed_phone_serciry_code;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_phone_info_certify, container, false);
            btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
            btn_confirm_code = (Button) view.findViewById(R.id.btn_confirm_code);
            btn_confirm_code_end = (Button) view.findViewById(R.id.btn_confirm_code_end);
            btn_contact_submit = (Button) view.findViewById(R.id.btn_contact_submit);
            btn_get_code = (Button) view.findViewById(R.id.btn_get_code);
            tv_code_end = (TextView) view.findViewById(R.id.tv_code_end);
            ed_phone_raletion = (EditText) view.findViewById(R.id.ed_phone_raletion);
            ed_phone_social = (EditText) view.findViewById(R.id.ed_phone_social);
            ed_phone_serciry_code_image = (EditText) view.findViewById(R.id.ed_phone_serciry_code_image);
            ed_phone_serciry_num = (EditText) view.findViewById(R.id.ed_phone_serciry_num);
            ed_phone_serciry_code = (EditText) view.findViewById(R.id.ed_phone_serciry_code);
            iv_phone_serciry_code_image = (ImageView) view.findViewById(R.id.iv_phone_serciry_code_image);
            tv_item_reletion_select = (TextView) view.findViewById(R.id.tv_item_reletion_select);
            tv_item_social_select = (TextView) view.findViewById(R.id.tv_item_social_select);
            ll_code = (LinearLayout) view.findViewById(R.id.ll_code);
            ll_phone_serciry_num = (LinearLayout) view.findViewById(R.id.ll_phone_serciry_num);
            ll_phone_contact = (LinearLayout) view.findViewById(R.id.ll_phone_contact);
            ll_code_end = (LinearLayout) view.findViewById(R.id.ll_code_end);
            ll_phone_serciry_code_image = (LinearLayout) view.findViewById(R.id.ll_phone_serciry_code_image);
            ll_phone_serciry_code = (LinearLayout) view.findViewById(R.id.ll_phone_serciry_code);
            rl_relate_top = (RelativeLayout) view.findViewById(R.id.rl_relate_top);
            rl_social_top = (RelativeLayout) view.findViewById(R.id.rl_social_top);
            tv_item_right2_raletion = (TextView) view.findViewById(R.id.tv_item_right2_raletion);
            tv_social_item_right2name = (TextView) view.findViewById(R.id.tv_social_item_right2name);
            tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            tv_remark_serciry_num = (TextView) view.findViewById(R.id.tv_remark_serciry_num);
            isPrepared = true;

            lazyLoad();
        }
        return view;
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        Log.i("TAG", "threeFragment--lazyLoad");

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CertificationCenterActivity activty = (CertificationCenterActivity) getActivity();
        if (activty.getShowTopInt() == 0) {
            onlyPart = true;
        } else onlyPart = false;
        this.onlyPartInt = activty.getDetialPart();

        initCountdown(PHONE_CERTIFY_VERIFY_CODE);
        initListener();
        requestData();
    }

    private void requestData() {

        String remarkStr = "糖果现金提示：<br/><font color='#ef7030'>*</font>请填写您的真实有效个人信息，认证成功后不可修改<br/><font color='#ef7030'>*</font>您的信息仅供借款审核使用，不会再任何地方泄露您的信息";
        tv_remark.setVisibility(View.VISIBLE);
        tv_remark.setText(Html.fromHtml(remarkStr));
        tv_remark_serciry_num.setText(Html.fromHtml(remarkStr));
//判断是流程还是单个进程
        if (onlyPart) {
            if (onlyPartInt == MyConstants.CERTIFICATION_STEP_ONLY_PART_CONTACT) {
                requestContactInfo(true);
            } else {
                ll_phone_serciry_num.setVisibility(View.VISIBLE);
                ll_phone_contact.setVisibility(View.GONE);
                requestCertifyPhoneStatus();
            }
        } else {//极速流程
            if (onlyPartInt == MyConstants.CERTIFICATION_STEP_ONLY_PART_CONTACT) {
                ll_phone_contact.setVisibility(View.VISIBLE);
                ll_phone_serciry_num.setVisibility(View.GONE);
            } else {
                ll_phone_contact.setVisibility(View.GONE);
                ll_phone_serciry_num.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initListener() {
        //提交运行商服务密码
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(TextViewUtil.getText(ed_phone_serciry_num))) {
                    showToast("请输入运行商密码");
                    return;
                }
                requestPhonePassword(true, TextViewUtil.getText(ed_phone_serciry_num));
            }
        });
        //提交验证码或图片验证码
        btn_confirm_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhoneServicePasswordInfo != null) {
                    requestPhonePasswordCode(true);
                } else showToast("验证码发送错误");

            }
        });

        //手机验证结束
        btn_confirm_code_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onlyPart) {
                    showToast("手机认证完成");
                    getActivity().finish();
                } else requestCertifyStatus();
            }
        });
        //重新发送验证码
        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCanRequestCode()) {
                    return;
                }
                if (mPhoneServicePasswordInfo != null) {
                    requestRetryCodeData(mPhoneServicePasswordInfo.getTaskId(), true);
                } else showToast("验证码发送错误");

            }
        });
        //重新发送验证码
        iv_phone_serciry_code_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCanRequestCode()) {
                    return;
                }
                if (mPhoneServicePasswordInfo != null) {
                    requestRetryCodeData(mPhoneServicePasswordInfo.getTaskId(), true);
                } else showToast("验证码发送错误");

            }
        });


        //亲属关系
        rl_relate_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listSelectRelatives(0);
            }
        });
        //社会关系
        rl_social_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listSelectRelatives(1);
            }
        });

        //亲属关系手机号
        tv_item_right2_raletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_RALETION));

            }
        });
        //社会关系手机号
        tv_social_item_right2name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_RALETION2));
            }
        });
        //紧急联系人
        btn_contact_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String relation = TextViewUtil.getText(tv_item_reletion_select);
                String relation_phone = TextViewUtil.getText(ed_phone_raletion);
                String relation2 = TextViewUtil.getText(tv_item_social_select);
                String relation_social_phone = TextViewUtil.getText(ed_phone_social);
                if (StringUtils.isEmpty(relation)) showToast("请选择亲属关系");
                else if (StringUtils.isEmpty(relation_phone)) showToast("请填写亲属关系联系方式");
                else if (StringUtils.isEmpty(relation2)) showToast("请选择社会关系");
                else if (StringUtils.isEmpty(relation_social_phone)) {
                    showToast("请填写社会关系联系方式");
                } else {
                    saveContact(true);
                }
            }
        });


    }


    /**
     * 显示紧急联系人
     */
    private void setViewStepContact() {

        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_phone_contact, "translationX", new ScreenUtil(context).getWidth(), 0f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll_phone_contact.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ll_code_end.setVisibility(View.GONE);
                ll_code.setVisibility(View.GONE);
                ll_phone_serciry_num.setVisibility(View.GONE);
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


    /**
     * 关系选择
     *
     * @param type
     */
    private void listSelectRelatives(final int type) {
        String titleDialog = "";
        List<String> listStr = new ArrayList<>();
        if (type == 0) {
            titleDialog = "亲属关系";
            listStr.add("父母");
            listStr.add("配偶");
            listStr.add("兄弟姐妹");
        } else {
            titleDialog = "社会关系";
            listStr.add("同事");
            listStr.add("同学");
            listStr.add("朋友");
        }
        ListSelectDialog mListSelectDialog = new ListSelectDialog(context);
        mListSelectDialog.setTitle(titleDialog);
        mListSelectDialog.setStringList(listStr);
        mListSelectDialog.setSelectListener(new ListSelectDialog.SelectListener() {
            @Override
            public void OnSelectOne(ListDialogInfo mListDialogInfo) {
                if (mListDialogInfo == null) return;
                if (type == 0) {
                    tv_item_reletion_select.setText(mListDialogInfo.getTitle());
                } else {
                    tv_item_social_select.setText(mListDialogInfo.getTitle());
                }
            }
        });
        mListSelectDialog.show();
    }


    public void setSelectPhone(String phoneStr, int requestCode) {
        if (!StringUtils.isEmpty(phoneStr)) {
            if (requestCode == MyConstants.CERTIFICATION_RALETION) {
                ed_phone_raletion.setText(phoneStr);
            } else ed_phone_social.setText(phoneStr);
        }
    }

    public void setOnlyPart(int showTopInt, int onlyPartInt) {
        if (showTopInt == 0) {
            onlyPart = true;
        } else onlyPart = false;
        this.onlyPartInt = onlyPartInt;
    }


    /**
     * 判断紧急联系人是否认证
     */
    private void requestCertifyStatus() {
        showProgressDialog();
        HttpRequestService.requestCertifyStatus(context, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        AuthStatusInfo mAuthStatusInfo = gson.fromJson(json, new TypeToken<AuthStatusInfo>() {
                        }.getType());
                        if (StringUtils.isEquals(mAuthStatusInfo.getBankCardVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getIdentityVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getContactVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getPhoneVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getSesameVtStatus(), "1")) {
                            EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_STEP_SUCCESS));
                        } else if (StringUtils.isEquals(mAuthStatusInfo.getContactVtStatus(), "0")) {
                            setViewStepContact();
                        } else EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_STEP_BANK));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dismissProgressDialog();
            }
        });
    }

    /**
     * 判断银行卡是否认证
     */
    private void requestCertifyBankStatus() {
        showProgressDialog();
        HttpRequestService.requestCertifyStatus(context, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        AuthStatusInfo mAuthStatusInfo = gson.fromJson(json, new TypeToken<AuthStatusInfo>() {
                        }.getType());
                        if (StringUtils.isEquals(mAuthStatusInfo.getBankCardVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getIdentityVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getContactVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getPhoneVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getSesameVtStatus(), "1")) {
                            EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_STEP_SUCCESS));
                        } else EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_STEP_BANK));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dismissProgressDialog();
            }
        });
    }

    /**
     * 判断手机是否认证
     */
    private void requestCertifyPhoneStatus() {
        showProgressDialog();
        HttpRequestService.requestCertifyStatus(context, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        AuthStatusInfo mAuthStatusInfo = gson.fromJson(json, new TypeToken<AuthStatusInfo>() {
                        }.getType());
                        if (StringUtils.isEquals(mAuthStatusInfo.getPhoneVtStatus(), "1")) {
                            openPhonePasswordSuccess();
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
     * 重新获取验证码
     */
    private void requestRetryCodeData(String taskId, boolean flag) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        map.put("taskId", taskId);
        requestNetData(UrlConstans.PHONE_RETRY_VALID_PASSWORD, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.PHONE_RETRY_VALID_PASSWORD_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        mPhoneServicePasswordInfo = gson.fromJson(json, new TypeToken<PhoneServicePasswordInfo>() {
                        }.getType());
                        if (mPhoneServicePasswordInfo != null) {
                            showToast("短信已下发至您手机,请注意查收!");
                            if (!btn_get_code.isSelected()) {
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
     * 提交服务商密码
     */
    private void requestPhonePassword(boolean flag, String password) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        map.put("password", password);
        requestNetData(UrlConstans.PHONE_VALID_PASSWORD, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.PHONE_VALID_PASSWORD_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        mPhoneServicePasswordInfo = gson.fromJson(json, new TypeToken<PhoneServicePasswordInfo>() {
                        }.getType());
                        if (mPhoneServicePasswordInfo != null) {
                            if (StringUtils.isEquals(mPhoneServicePasswordInfo.getStatus(), "1")) openPhoneCode();
                            else openPhonePasswordSuccess();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 获取紧急联系人
     */
    private void requestContactInfo(boolean flag) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        requestNetData(UrlConstans.CONTACT_FIND_INFO, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.CONTACT_FIND_INFO_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    ll_phone_contact.setVisibility(View.VISIBLE);
                    ll_phone_serciry_num.setVisibility(View.GONE);
                    try {
                        Gson gson = new Gson();
                        ContactUrgentInfo mContactUrgentInfo = gson.fromJson(json, new TypeToken<ContactUrgentInfo>() {
                        }.getType());
                        if (mContactUrgentInfo != null) {
                            if (StringUtils.isEquals(mContactUrgentInfo.getStatus(), "1")) {
                                btn_contact_submit.setVisibility(View.GONE);
                                //亲属关系
                                rl_relate_top.setClickable(false);
                                //社会关系
                                rl_social_top.setClickable(false);

                                //亲属关系手机号
                                tv_item_right2_raletion.setVisibility(View.GONE);
                                //社会关系手机号
                                tv_social_item_right2name.setVisibility(View.GONE);
                                tv_item_reletion_select.setText(getRelatonTypeString(mContactUrgentInfo.getKinRelationType()));
                                ed_phone_raletion.setText(StringUtils.setPhoneNumMask(mContactUrgentInfo.getKinPhone()));
                                tv_item_social_select.setText(getRelatonTypeString(mContactUrgentInfo.getSocietyRelationType()));
                                ed_phone_social.setText(StringUtils.setPhoneNumMask(mContactUrgentInfo.getSocietyPhone()));
                                ed_phone_social.setEnabled(false);
                                ed_phone_raletion.setEnabled(false);
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
     * 提交服务商验证码
     */
    private void requestPhonePasswordCode(boolean flag) {
        if (!StringUtils.isEmpty(mPhoneServicePasswordInfo.getSmsLabel())) {
            if (StringUtils.isEmpty(TextViewUtil.getText(ed_phone_serciry_code))) {
                showToast("请输入验证码");
                return;
            }
        }
        if (!StringUtils.isEmpty(mPhoneServicePasswordInfo.getAuthImg())) {
            if (StringUtils.isEmpty(TextViewUtil.getText(ed_phone_serciry_code_image))) {
                showToast("请输入图形验证码");
                return;
            }
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("authCode", TextViewUtil.getText(ed_phone_serciry_code_image));
        map.put("smsCode", TextViewUtil.getText(ed_phone_serciry_code));
        map.put("taskId", mPhoneServicePasswordInfo.getTaskId());
        map.put("taskStage", mPhoneServicePasswordInfo.getTaskStage());
        map.put("uid", AccountInfoUtils.getUid(context));
        requestNetData(UrlConstans.PHONE_VALIDOPERATOR, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.PHONE_VALIDOPERATOR_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        openPhonePasswordSuccess();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 打开验证码
     */
    private void openPhoneCode() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_code, "translationX", new ScreenUtil(context).getWidth(), 0f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll_code.setVisibility(View.VISIBLE);
                if (mPhoneServicePasswordInfo != null) {
                    if (!StringUtils.isEmpty(mPhoneServicePasswordInfo.getSmsLabel())) ll_phone_serciry_code.setVisibility(View.VISIBLE);
                    if (!StringUtils.isEmpty(mPhoneServicePasswordInfo.getAuthImg())) {
                        ll_phone_serciry_code_image.setVisibility(View.VISIBLE);
                        try {
                            Bitmap mBitmap = StringUtils.stringtoBitmap(mPhoneServicePasswordInfo.getAuthImg());
                            iv_phone_serciry_code_image.setImageBitmap(mBitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ll_phone_serciry_num.setVisibility(View.GONE);
                showToast("短信已下发至您手机,请注意查收!");
                if (!btn_get_code.isSelected()) {
                    openCountDown(false);
                }
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

    /**
     * 打开服务商验证成功
     */
    private void openPhonePasswordSuccess() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_code_end, "translationX", new ScreenUtil(context).getWidth(), 0f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll_code_end.setVisibility(View.VISIBLE);
                finishCountdown();
                String phoneStr = StringUtils.setPhoneNumMask(AccountInfoUtils.getAccountPhone(context));
                SpannableStringBuilder spannableString = new SpannableStringBuilder();
                spannableString.append("恭喜你,");
                if (!StringUtils.isEmpty(phoneStr)) spannableString.append(phoneStr);
                spannableString.append("验证成功!");
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#009ad6"));
                spannableString.setSpan(colorSpan, 4, phoneStr.length() + 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                tv_code_end.setText(spannableString);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ll_code.setVisibility(View.GONE);
                ll_phone_serciry_num.setVisibility(View.GONE);
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


    /**
     * 保存紧急联系人
     */
    private void saveContact(boolean flag) {
        String relation = TextViewUtil.getText(tv_item_reletion_select);
        String relation_phone = TextViewUtil.getText(ed_phone_raletion);
        String relation2 = TextViewUtil.getText(tv_item_social_select);
        String relation_social_phone = TextViewUtil.getText(ed_phone_social);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        map.put("kinPhone", relation_phone);
        map.put("kinRelationType", String.valueOf(getRelatonType(relation)));
        map.put("societyPhone", relation_social_phone);
        map.put("societyRelationType", String.valueOf(getRelatonType(relation2)));
        requestNetData(UrlConstans.CONTACT_SAVE_INFO, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.CONTACT_SAVE_INFO_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        showToast("紧急联系人认证完成");
                        if (onlyPart) getActivity().finish();
                        else requestCertifyBankStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    protected Button getBtnGetCodeView() {
        return (Button) view.findViewById(R.id.btn_get_code);
    }


    private int getRelatonType(String str) {
        int type = 0;
        switch (str) {
            case "父母":
                type = 1;
                break;
            case "配偶":
                type = 2;
                break;
            case "兄弟姐妹":
                type = 3;
                break;
            case "同事":
                type = 4;
                break;
            case "同学":
                type = 5;
                break;
            case "朋友":
                type = 6;
                break;
        }
        return type;
    }

    private String getRelatonTypeString(String str) {
        String strRelation = "";
        switch (str) {
            case "1":
                strRelation = "父母";
                break;
            case "2":
                strRelation = "配偶";
                break;
            case "3":
                strRelation = "兄弟姐妹";
                break;
            case "4":
                strRelation = "同事";
                break;
            case "5":
                strRelation = "同学";
                break;
            case "6":
                strRelation = "朋友";
                break;
        }
        return strRelation;
    }


    /**
     * @return 是否正在输入服务商验证码
     */
    public boolean getPhoneCodeShow() {
        if (ll_code != null && ll_code.getVisibility() == View.VISIBLE) {
            return true;
        } else return false;
    }

    /**
     * @return 从验证码返回到输入服务商密码
     */
    public void getPhoneCodeBackInputShow() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_code, "translationX", 0f, new ScreenUtil(context).getWidth());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ll_phone_serciry_num.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ll_code.setVisibility(View.GONE);
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

