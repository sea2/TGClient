package com.tangguo.tangguoxianjin.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.activity.BorrowMoneyActivity;
import com.tangguo.tangguoxianjin.activity.MainActivity;
import com.tangguo.tangguoxianjin.activity.MyMessageActivity;
import com.tangguo.tangguoxianjin.activity.RepaymentActivity;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.common.BaseFragment;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.model.AccountBaseInfo;
import com.tangguo.tangguoxianjin.model.AdListBean;
import com.tangguo.tangguoxianjin.model.HomeAdvertInfo;
import com.tangguo.tangguoxianjin.model.NoticeInfo;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.IntentUtil;
import com.tangguo.tangguoxianjin.util.LogManager;
import com.tangguo.tangguoxianjin.util.ScreenUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.view.AnimationNumberView;
import com.tangguo.tangguoxianjin.view.CustomImageButton;
import com.tangguo.tangguoxianjin.view.banner.Banner;
import com.tangguo.tangguoxianjin.view.banner.BannerConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.tangguo.tangguoxianjin.R.id.ll_broad_cast;

/**
 * @author lhy
 */
public class HomePageFragment extends BaseFragment {
    // 标志fragment是否初始化完成
    private boolean isPrepared;
    private View view;
    private Banner banner;
    private ImageView tv_broadcast;
    private TextView tv_broad_info;
    private TextView tv_item_left_name;
    private LinearLayout llbroad_cast;
    private ImageView tv_home_information;
    private int iconInt = 0;
    private CustomImageButton btn_add_lines;
    private AnimationNumberView tv_home_lines_money;
    private ImageView tv_broadcast_close;
    private LinearLayout ll_left_loan;
    private LinearLayout ll_right_repayment;
    private SwipeRefreshLayout swipe_ly_home;
    private RelativeLayout rl_need_repay;
    private NoticeInfo mNoticeInfo;
    private Subscription subscription;
    private NoticeInfo.NoticeListBean mNoticeListBean;
    private int noticeSize = 0;
    private List<AdListBean> adList;
    private AccountBaseInfo mAccountBaseInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home_page_layout, container, false);
            LogManager.i(TAG, "oneFragment--onCreateView");
            isPrepared = true;
            initView(view);
            lazyLoad();
        }
        return view;
    }


    private void initView(View view) {

        banner = (Banner) view.findViewById(R.id.banner1);
        tv_broadcast = (ImageView) view.findViewById(R.id.tv_broadcast);
        swipe_ly_home = (SwipeRefreshLayout) view.findViewById(R.id.swipe_ly_home);
        swipe_ly_home.setColorSchemeResources(R.color.color_theme);
        tv_home_information = (ImageView) view.findViewById(R.id.tv_home_information);

        tv_broadcast_close = (ImageView) view.findViewById(R.id.tv_broadcast_close);
        tv_broad_info = (TextView) view.findViewById(R.id.tv_broad_info);
        tv_item_left_name = (TextView) view.findViewById(R.id.tv_item_left_name);
        tv_home_lines_money = (AnimationNumberView) view.findViewById(R.id.tv_home_lines_money);
        btn_add_lines = (CustomImageButton) view.findViewById(R.id.btn_add_lines);
        llbroad_cast = (LinearLayout) view.findViewById(ll_broad_cast);
        ll_left_loan = (LinearLayout) view.findViewById(R.id.ll_left_loan);
        ll_right_repayment = (LinearLayout) view.findViewById(R.id.ll_right_repayment);
        rl_need_repay = (RelativeLayout) view.findViewById(R.id.rl_useful_record);
        //虚线
        View view_dash_line = view.findViewById(R.id.view_dash_line);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view_dash_line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //广播
        AnimationDrawable anim = (AnimationDrawable) tv_broadcast.getBackground();
        anim.start();


    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }

        requestData();
    }

    private void requestData() {

        //广播公告
        requestNotices();
        //轮播图
        requestAdvert();
        requestMessage();
        requestAccount();
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
                            tv_home_lines_money.showNumberWithAnimation("0", mAccountBaseInfo.getBalance(), false);
                            String repayMoney = mAccountBaseInfo.getBorrowMoney();
                            String newNeedRepay = "当前待还&nbsp;&nbsp;&nbsp;<font color='#ffaf30'>" + repayMoney + "</font>元";
                            tv_item_left_name.setText(Html.fromHtml(newNeedRepay));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                swipe_ly_home.setRefreshing(false);
            }
        });
    }

    /**
     * 广播公告
     */
    private void requestNotices() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        requestNetData(UrlConstans.ACCOUNT_NOTICE_ADV, map, false, MyConstants.HttpMethod.HTTP_POST, UrlConstans.ACCOUNT_NOTICE_ADV_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {

                    try {
                        Gson gson = new Gson();
                        mNoticeInfo = gson.fromJson(json, new TypeToken<NoticeInfo>() {
                        }.getType());
                        setNoticeView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 轮播图
     */
    private void requestAdvert() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(context));
        requestNetData(UrlConstans.ADVERT_HOME, map, false, MyConstants.HttpMethod.HTTP_POST, UrlConstans.ADVERT_HOME_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        HomeAdvertInfo mHomeAdvertInfo = gson.fromJson(json, new TypeToken<HomeAdvertInfo>() {
                        }.getType());
                        if (mHomeAdvertInfo != null && mHomeAdvertInfo.getAdList() != null) {
                            adList = mHomeAdvertInfo.getAdList();
                            if (adList != null) {
                                //设置指示器居中（CIRCLE_INDICATOR或者CIRCLE_INDICATOR_TITLE模式下）
                                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                                banner.setIndicatorGravity(BannerConfig.RIGHT);
                                banner.setImages(adList);//可以选择设置图片网址，或者资源文件，默认用Glide加载
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
     * 消息数量
     */
    private void requestMessage() {
        if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) return;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("pageNo", "1");
        map.put("uid", AccountInfoUtils.getUid(context));
        requestNetData(UrlConstans.ACCOUNT_MESSAGE, map, false, MyConstants.HttpMethod.HTTP_POST, UrlConstans.ACCOUNT_MESSAGE_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        JSONObject jb = new JSONObject(json);
                        if (jb.has("unread")) {
                            String unread = jb.getString("unread");
                            int messageCount = StringUtils.toInt(unread);
                            if (messageCount > 0) {
                                tv_home_information.setImageResource(R.drawable.home_message_icon);
                            } else tv_home_information.setImageResource(R.color.transparent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 广播公告滚动实现
     */
    private void setNoticeView() {
        if (mNoticeInfo != null && mNoticeInfo.getNoticeList() != null && mNoticeInfo.getNoticeList().size() > 0) {
            llbroad_cast.setVisibility(View.VISIBLE);
            noticeSize = mNoticeInfo.getNoticeList().size();
            if (noticeSize == 1) {
                mNoticeListBean = mNoticeInfo.getNoticeList().get(0);
                if (tv_broad_info != null) {
                    tv_broad_info.setText(mNoticeListBean.getTitle());
                    if (StringUtils.isEquals(mNoticeListBean.getLevel(), "1"))
                        tv_broad_info.setTextColor(ContextCompat.getColor(context, R.color.tv_color_808080));
                    else tv_broad_info.setTextColor(ContextCompat.getColor(context, R.color.stand_common));
                }

            } else {
                if (subscription != null) subscription.unsubscribe();
                // 第一个是延时开始，第二个参数是间隔
                subscription = Observable.interval(2, 5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (noticeSize != 0) {
                            int position = (int) (aLong % noticeSize);
                            if (noticeSize >= (position + 1)) {
                                mNoticeListBean = mNoticeInfo.getNoticeList().get(position);
                                if (tv_broad_info != null) {
                                    tv_broad_info.setText(mNoticeListBean.getTitle());
                                    if (StringUtils.isEquals(mNoticeListBean.getLevel(), "1"))
                                        tv_broad_info.setTextColor(ContextCompat.getColor(context, R.color.tv_color_808080));
                                    else tv_broad_info.setTextColor(ContextCompat.getColor(context, R.color.stand_common));
                                }
                            }
                        }
                    }
                });
            }
        } else llbroad_cast.setVisibility(View.GONE);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initListener();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int heightStuta = ScreenUtil.getStatusBarHeight(context);
        lp.setMargins(10, heightStuta + 10, 10, 10);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tv_home_information.setLayoutParams(lp);

    }


    private void initListener() {
        swipe_ly_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });

        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                if (adList != null) {
                    if (adList.size() >= position) {
                        int positionNew = position - 1;
                        IntentUtil.startWebActivity((BaseActivity) getActivity(), MainActivity.class, adList.get(positionNew).getLinkUrl());
                    }
                }
            }
        });

        tv_broadcast_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llbroad_cast.setVisibility(View.GONE);
            }
        });
        //广播公告
        tv_broad_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoticeListBean != null) {
                    IntentUtil.startWebActivity((BaseActivity) getActivity(), MainActivity.class, mNoticeListBean.getUrl());
                }
            }
        });
        //借款
        ll_left_loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                startAc(BorrowMoneyActivity.class);
            }
        });
        //还款
        ll_right_repayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                startAc(RepaymentActivity.class);
            }
        });
        rl_need_repay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                startAc(RepaymentActivity.class);
            }
        });
        btn_add_lines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tv_home_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(AccountInfoUtils.getUid(context))) {
                    IntentUtil.startLogin((BaseActivity) getActivity(), MainActivity.class);
                    return;
                }
                startAc(MyMessageActivity.class);
            }
        });
    }


    public void setBannerStatue(boolean flag) {
        if (banner != null) {
            banner.isAutoPlay(flag);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) requestData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null) subscription.unsubscribe();
    }

}
