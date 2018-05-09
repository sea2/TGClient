package com.tangguo.tangguoxianjin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.activity.BorrowHelpActivity;
import com.tangguo.tangguoxianjin.activity.BorrowProgressActivity;
import com.tangguo.tangguoxianjin.activity.BorrowRecordActivity;
import com.tangguo.tangguoxianjin.activity.HelpCenterActivity;
import com.tangguo.tangguoxianjin.activity.MainActivity;
import com.tangguo.tangguoxianjin.activity.SettingActivity;
import com.tangguo.tangguoxianjin.common.AppInfoHelper;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.common.BaseFragment;
import com.tangguo.tangguoxianjin.eventbus.MessageEvent;
import com.tangguo.tangguoxianjin.model.AuthStatusInfo;
import com.tangguo.tangguoxianjin.net.HttpRequestService;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.IntentUtil;
import com.tangguo.tangguoxianjin.util.ScreenUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.util.TextViewUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * @author lhy
 */
public class AccountPageFragment extends BaseFragment implements View.OnClickListener, ObservableScrollViewCallbacks {
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private RelativeLayout rl_account_page_title, ll_account_top_image;
    private ImageView iv_certify_complete_flag;
    private TextView tv_account_phone;
    private TextView tv_app_version;
    private LinearLayout ll_borrow_progress;
    private LinearLayout ll_account_borrow_record;
    private LinearLayout ll_account_setting;
    private LinearLayout ll_account_help_center;
    private LinearLayout ll_account_servicer;
    private TextView tv_account_servicer;
    private LinearLayout ll_borrow_help;
    private ObservableScrollView mScrollView;
    private TextView tv_account_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_account_layout, container, false);
            Log.i("TAG", "AccountPageFragment--onCreateView");
            rl_account_page_title = (RelativeLayout) view.findViewById(R.id.rl_account_page_title);
            ll_account_top_image = (RelativeLayout) view.findViewById(R.id.ll_account_top_image);
            iv_certify_complete_flag = (ImageView) view.findViewById(R.id.iv_certify_complete_flag);
            tv_account_phone = (TextView) view.findViewById(R.id.tv_account_phone);
            tv_account_servicer = (TextView) view.findViewById(R.id.tv_account_servicer);
            tv_account_title = (TextView) view.findViewById(R.id.tv_account_title);
            tv_app_version = (TextView) view.findViewById(R.id.tv_app_version);
            ll_borrow_progress = (LinearLayout) view.findViewById(R.id.ll_borrow_progress);
            ll_account_borrow_record = (LinearLayout) view.findViewById(R.id.ll_account_borrow_record);
            ll_account_setting = (LinearLayout) view.findViewById(R.id.ll_account_setting);
            ll_account_help_center = (LinearLayout) view.findViewById(R.id.ll_account_help_center);
            ll_account_servicer = (LinearLayout) view.findViewById(R.id.ll_account_servicer);
            ll_borrow_help = (LinearLayout) view.findViewById(R.id.ll_borrow_help);
            mScrollView = (ObservableScrollView) view.findViewById(R.id.fragment_account_scroll);
            mScrollView.setScrollViewCallbacks(this);
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
        Log.i("TAG", "AccountPageFragment--lazyLoad");
        requestData();
    }

    private void requestData() {

        requestCertifyStatus();

        String phoneNum = AccountInfoUtils.getAccountPhone(context);
        if (!StringUtils.isEmpty(phoneNum)) tv_account_phone.setText(StringUtils.setPhoneNumMask(phoneNum));
        else tv_account_phone.setText("未登录");

        String servicePhone = "400-8639-6985";
        tv_account_servicer.setText(servicePhone);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int heightStuta = ScreenUtil.getStatusBarHeight(context);
        lp.setMargins(0, heightStuta, 0, 0);
        rl_account_page_title.setLayoutParams(lp);
        tv_account_title.setPadding(0, heightStuta, 0, 0);
        initView();
        initListener();
    }

    private void initListener() {
        ll_borrow_progress.setOnClickListener(this);
        ll_account_borrow_record.setOnClickListener(this);
        ll_account_setting.setOnClickListener(this);
        ll_account_help_center.setOnClickListener(this);
        ll_account_servicer.setOnClickListener(this);
        ll_borrow_help.setOnClickListener(this);
        ll_account_top_image.setOnClickListener(this);

    }

    private void initView() {

        tv_app_version.setText("当前版本V" + AppInfoHelper.getInstance().getAppVersionNumber());
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_borrow_progress:
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                startAc(BorrowProgressActivity.class);
                break;
            case R.id.ll_account_top_image:
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                break;
            case R.id.ll_account_borrow_record:
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                startAc(BorrowRecordActivity.class);
                break;
            case R.id.ll_account_setting:
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                startAc(SettingActivity.class);
                break;
            case R.id.ll_account_help_center:
                startAc(HelpCenterActivity.class);
                break;
            case R.id.ll_account_servicer:
                EventBus.getDefault().post(new MessageEvent(1, TextViewUtil.getText(tv_account_servicer).replace("-", "")));
                break;
            case R.id.ll_borrow_help:
                startAc(BorrowHelpActivity.class);
                break;
        }

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
                        AuthStatusInfo mAuthStatusInfo = gson.fromJson(json, new TypeToken<AuthStatusInfo>() {
                        }.getType());
                        if (StringUtils.isEquals(mAuthStatusInfo.getBankCardVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getIdentityVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getContactVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getPhoneVtStatus(), "1") && StringUtils.isEquals(mAuthStatusInfo.getSesameVtStatus(), "1")) {
                            iv_certify_complete_flag.setImageResource(R.drawable.account_certified_icon);
                        } else iv_certify_complete_flag.setImageResource(R.drawable.account_not_certified_icon);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    iv_certify_complete_flag.setImageResource(R.drawable.account_not_certified_icon);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) requestData();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = ContextCompat.getColor(context, R.color.color_theme);
        float alpha = Math.min(1, (float) scrollY / 200);
        tv_account_title.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


    @Override
    public void onDestroyView() {        //移除当前视图，防止重复加载相同视图使得程序闪退
        ((ViewGroup) view.getParent()).removeView(view);
        super.onDestroyView();
    }

}
