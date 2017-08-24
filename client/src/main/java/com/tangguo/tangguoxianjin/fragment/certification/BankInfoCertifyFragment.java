package com.tangguo.tangguoxianjin.fragment.certification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.activity.CertificationCenterActivity;
import com.tangguo.tangguoxianjin.common.BaseFragment;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.eventbus.CertificationMessageEvent;
import com.tangguo.tangguoxianjin.model.BankSupportInfo;
import com.tangguo.tangguoxianjin.model.ListDialogInfo;
import com.tangguo.tangguoxianjin.model.MyBankInfo;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.DisplayImageOptionsUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.util.TextViewUtil;
import com.tangguo.tangguoxianjin.view.dialog.CommonDialog;
import com.tangguo.tangguoxianjin.view.dialog.ListSelectDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BankInfoCertifyFragment extends BaseFragment {
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private Button btn_confirm;
    private boolean onlyPart = false;
    private EditText ed_bank_num, ed_phone_num;
    private TextView tv_bank_select, tv_bank_name;
    private TextView tv_bank_type, tv_remark;
    private TextView tv_number;
    private LinearLayout ll_add_bank_info;
    private RelativeLayout rl_bank_info;
    private ImageView iv_logo;
    private ImageButton btn_bank_delete;
    private List<BankSupportInfo.BankListBean> sBankList;
    private BankSupportInfo.BankListBean mBankBean;
    private MyBankInfo mMyBankInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_bank_info_certify, container, false);
            btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
            btn_bank_delete = (ImageButton) view.findViewById(R.id.btn_bank_delete);
            ed_bank_num = (EditText) view.findViewById(R.id.ed_bank_num);
            ed_phone_num = (EditText) view.findViewById(R.id.ed_phone_num);
            tv_bank_select = (TextView) view.findViewById(R.id.tv_bank_select);
            tv_bank_name = (TextView) view.findViewById(R.id.tv_bank_name);
            tv_number = (TextView) view.findViewById(R.id.tv_number);
            tv_bank_type = (TextView) view.findViewById(R.id.tv_bank_type);
            tv_remark = (TextView) view.findViewById(R.id.tv_remark);
            ll_add_bank_info = (LinearLayout) view.findViewById(R.id.ll_add_bank_info);
            rl_bank_info = (RelativeLayout) view.findViewById(R.id.rl_bank_info);
            iv_logo = (ImageView) view.findViewById(R.id.iv_logo);
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
        Log.e("TAG", "threeFragment--lazyLoad");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CertificationCenterActivity activty = (CertificationCenterActivity) getActivity();
        if (activty.getShowTopInt() == 0) {
            onlyPart = true;
        } else onlyPart = false;
        initView();
        initListener();
        initData();
    }


    private void initView() {
        String remarkStr = "糖果现金提示：<br/><font color='#ef7030'>*</font>请填写您的真实有效个人信息，认证成功后不可修改<br/><font color='#ef7030'>*</font>您的信息仅供借款审核使用，不会再任何地方泄露您的信息";
        tv_remark.setVisibility(View.VISIBLE);
        tv_remark.setText(Html.fromHtml(remarkStr));
    }

    private void initListener() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bank_num = TextViewUtil.getText(ed_bank_num);
                String phone_num = TextViewUtil.getText(ed_phone_num);
                if (StringUtils.isEmpty(bank_num)) {
                    showToast("请输入银行卡号");
                    return;
                }
                if (StringUtils.isEmpty(phone_num)) {
                    showToast("请输入银行预留手机号");
                    return;
                }
                if (mBankBean == null) {
                    showToast("请选择发卡行");
                    return;
                }
                requestAddBank(true, bank_num, phone_num);

            }
        });
        //选择支持银行
        tv_bank_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSupportBankList(true);
            }
        });
        //删除银行卡
        btn_bank_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDelBank();
            }
        });
    }


    private void initData() {
        requestMyBankInfo(true);
    }


    public void setOnlyPart(int showTopInt) {
        if (showTopInt == 0) onlyPart = true;
        else onlyPart = false;
    }


    /**
     * 我的银行信息
     */
    private void requestMyBankInfo(boolean flag) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        requestNetData(UrlConstans.MY_BANKCARD_INFO, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.MY_BANKCARD_INFO_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        mMyBankInfo = gson.fromJson(json, new TypeToken<MyBankInfo>() {
                        }.getType());
                        if (mMyBankInfo != null) {
                            if (!StringUtils.isEmpty(mMyBankInfo.getCardNo())) {
                                rl_bank_info.setVisibility(View.VISIBLE);
                                ll_add_bank_info.setVisibility(View.GONE);
                                ImageLoader.getInstance().displayImage(mMyBankInfo.getIconUrl(), iv_logo, DisplayImageOptionsUtil.getDisplayImageOptions(-1, true, true));
                                tv_bank_type.setText("");
                                tv_bank_name.setText(mMyBankInfo.getBankName());
                                tv_number.setText(StringUtils.setIdCardMask(mMyBankInfo.getCardNo()));
                                if (StringUtils.isEquals(mMyBankInfo.getCanDelete(), "1")) btn_bank_delete.setVisibility(View.VISIBLE);
                                else btn_bank_delete.setVisibility(View.GONE);
                            } else {
                                ll_add_bank_info.setVisibility(View.VISIBLE);
                                rl_bank_info.setVisibility(View.GONE);
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
     * 支持银行列表
     */
    private void requestSupportBankList(boolean flag) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        requestNetData(UrlConstans.BANK_CARD_SUPPORT, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.BANK_CARD_SUPPORT_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        BankSupportInfo mBankSupportInfo = gson.fromJson(json, new TypeToken<BankSupportInfo>() {
                        }.getType());
                        if (mBankSupportInfo != null) {
                            if (mBankSupportInfo.getBankList() != null) {
                                sBankList = mBankSupportInfo.getBankList();
                                List<ListDialogInfo> mListDialogInfoList = new ArrayList<>();
                                for (BankSupportInfo.BankListBean mBankListBean : sBankList) {
                                    ListDialogInfo mListDialogInfo = new ListDialogInfo(mBankListBean.getBankId(), mBankListBean.getBankName(), "", mBankListBean.getIconUrl());
                                    mListDialogInfoList.add(mListDialogInfo);
                                }
                                listSelectBanks(mListDialogInfoList);
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
     * 添加银行
     */
    private void requestAddBank(boolean flag, String bank_num, String phone_num) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        map.put("bankCode", mBankBean.getBankCode());
        map.put("bankId", mBankBean.getBankId());
        map.put("bankName", mBankBean.getBankName());
        map.put("cardNo", bank_num);
        map.put("phone_num", phone_num);
        requestNetData(UrlConstans.BANK_CARD_ADD, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.BANK_CARD_ADD_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        showToast("银行卡认证完成");
                        if (onlyPart) getActivity().finish();
                        else {
                            EventBus.getDefault().post(new CertificationMessageEvent(MyConstants.CERTIFICATION_STEP_SUCCESS));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 删除银行卡dialog
     */
    private void showDialogDelBank() {
        CommonDialog mDialog = new CommonDialog(context);
        mDialog.setTitle("温馨提示");
        mDialog.setContent("确定删除银行卡？");
        mDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDeleteBank(true);
            }
        });
        mDialog.setNegativeButton("取消");
        mDialog.show();
    }

    /**
     * 删除银行
     */
    private void requestDeleteBank(boolean flag) {
        if (mMyBankInfo == null) {
            showToast("我的银行卡信息为空");
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        map.put("id", mMyBankInfo.getId());
        requestNetData(UrlConstans.BANK_CARD_DEL, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.BANK_CARD_DEL_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        showToast("银行删除成功");
                        ll_add_bank_info.setVisibility(View.VISIBLE);
                        rl_bank_info.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * 银行选择
     */
    private void listSelectBanks(List<ListDialogInfo> mListDialogInfoList) {
        String titleDialog = "支持银行列表";
        ListSelectDialog mListSelectDialog = new ListSelectDialog(context);
        mListSelectDialog.setTitle(titleDialog);
        mListSelectDialog.setList(mListDialogInfoList, 1);
        mListSelectDialog.setSelectListener(new ListSelectDialog.SelectListener() {
            @Override
            public void OnSelectOne(ListDialogInfo mListDialogInfo) {
                if (sBankList != null) {
                    for (BankSupportInfo.BankListBean mBankListBean : sBankList) {
                        if (StringUtils.isEquals(mBankListBean.getBankId(), mListDialogInfo.getId())) {
                            mBankBean = mBankListBean;
                            tv_bank_select.setText(mBankListBean.getBankName());
                            break;
                        }
                    }
                }
            }
        });
        mListSelectDialog.show();
    }


}

