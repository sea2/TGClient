package com.tangguo.tangguoxianjin.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.model.BorrowRecordInfo;
import com.tangguo.tangguoxianjin.model.ListDialogInfo;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.IntentUtil;
import com.tangguo.tangguoxianjin.util.QRCodeUtil;
import com.tangguo.tangguoxianjin.util.ScreenUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.util.TextViewUtil;
import com.tangguo.tangguoxianjin.view.TitleLayout;
import com.tangguo.tangguoxianjin.view.dialog.CommonDialog;
import com.tangguo.tangguoxianjin.view.dialog.ListSelectDialog;
import com.tangguo.tangguoxianjin.view.dialog.WeiXinRepayDialog;
import com.tangguo.tangguoxianjin.view.progress.CustomProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tangguo.tangguoxianjin.R.id.btn_return_money;

public class RepaymentActivity extends BaseActivity {

    private com.tangguo.tangguoxianjin.view.progress.CustomProgressBar progressbar;
    private android.widget.TextView tvlinedayremark;
    private android.widget.TextView tvlineday;
    private android.widget.RelativeLayout rltopprogress;
    private TextView tvaddremark;
    private android.support.v4.widget.SwipeRefreshLayout idswipely;
    private TextView tvborrowmoney;
    private TextView tvpaymoneydate;
    private TextView tvreturnmoneydate;
    private TextView tvreturnmoney;
    private android.widget.Button btnreturnmoney;
    private TextView tvrepaymoney;
    private TextView tvrepaymoneyway;
    private Button btnrepaymoney;
    private android.widget.LinearLayout llrepaystepway;
    private Button btnrepayend;
    private LinearLayout llrepayend;
    private String recordId = "";
    private BorrowRecordInfo mBorrowRecordInfo;
    private LinearLayout llrepaynothing;
    private String repayId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repayment);
        this.llrepaynothing = (LinearLayout) findViewById(R.id.ll_repay_nothing);
        this.llrepayend = (LinearLayout) findViewById(R.id.ll_repay_end);
        this.btnrepayend = (Button) findViewById(R.id.btn_repay_end);
        this.llrepaystepway = (LinearLayout) findViewById(R.id.ll_repay_step_way);
        this.btnrepaymoney = (Button) findViewById(R.id.btn_repay_money);
        this.tvrepaymoneyway = (TextView) findViewById(R.id.tv_repay_money_way);
        this.tvrepaymoney = (TextView) findViewById(R.id.tv_repay_money);
        this.btnreturnmoney = (Button) findViewById(btn_return_money);
        this.tvreturnmoney = (TextView) findViewById(R.id.tv_return_money);
        this.tvreturnmoneydate = (TextView) findViewById(R.id.tv_return_money_date);
        this.tvpaymoneydate = (TextView) findViewById(R.id.tv_pay_money_date);
        this.tvborrowmoney = (TextView) findViewById(R.id.tv_borrow_money);
        this.idswipely = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        this.tvaddremark = (TextView) findViewById(R.id.tv_add_remark);
        this.rltopprogress = (RelativeLayout) findViewById(R.id.rl_top_progress);
        this.tvlineday = (TextView) findViewById(R.id.tv_line_day);
        this.tvlinedayremark = (TextView) findViewById(R.id.tv_line_day_remark);
        this.progressbar = (CustomProgressBar) findViewById(R.id.progress_bar);
        idswipely.setColorSchemeColors(ContextCompat.getColor(RepaymentActivity.this, R.color.color_theme));
        initIntent();
        initView();
        initTitle();
        initListener();
        initData();
    }

    private void initIntent() {
        recordId = getIntent().getStringExtra("recordId");
    }


    private void initView() {
    }

    private void initTitle() {
        setTitle("还款");
        if (basePageView != null) {
            basePageView.setBack(new TitleLayout.OnBackListener() {
                @Override
                public void onBack() {
                    backControl();
                }
            });
        }
    }


    private void initListener() {

        idswipely.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestRepayData(false);
            }
        });

        btnreturnmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRepayWay();
            }
        });
        tvrepaymoneyway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listSelectRepayWay();
            }
        });
        btnrepaymoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(TextViewUtil.getText(tvrepaymoneyway))) {
                    showToast("请选择支付方式");
                    return;
                }

                int type = 1;
                if (StringUtils.isEquals(TextViewUtil.getText(tvrepaymoneyway), "支付宝")) type = 1;
                else if (StringUtils.isEquals(TextViewUtil.getText(tvrepaymoneyway), "微信")) type = 2;
                requestRepay(true, type);

            }
        });
        btnrepayend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initData() {
        requestRepayData(true);
    }


    /**
     * 还款详情
     */
    private void requestRepayData(boolean flag) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(RepaymentActivity.this));
        if (!StringUtils.isEmpty(recordId)) map.put("recordId", recordId);
        requestNetData(UrlConstans.REPAY_MONEY_DETAIL, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.REPAY_MONEY_DETAIL_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        mBorrowRecordInfo = gson.fromJson(json, new TypeToken<BorrowRecordInfo>() {
                        }.getType());
                        recordId = mBorrowRecordInfo.getRecordId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                idswipely.setRefreshing(false);
                setViewData();
            }
        });
    }

    /**
     * 还款
     */
    private void requestRepay(boolean flag, final int type) {
        if (StringUtils.isEmpty(recordId)) {
            showToast("还款id为空");
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(RepaymentActivity.this));
        map.put("recordId", recordId);
        map.put("payChannel", String.valueOf(type));
        requestNetData(UrlConstans.REPAY_MONEY_SUBMIT, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.REPAY_MONEY_SUBMIT_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        JSONObject jb = new JSONObject(json);
                        if (jb.has("repayId")) {
                            repayId = jb.getString("repayId");
                        }
                        if (jb.has("url")) {
                            String url = jb.getString("url");
                            if (type == 1) {//支付宝
                                try {
                                    if (StringUtils.isNotEmpty(url)) {
                                        IntentUtil.openUri(RepaymentActivity.this, url);
                                        showZhiFuBaoDialog();
                                    } else showToast("还款链接为空");
                                } catch (Exception e) {
                                    showToastLong("请先安装支付宝，在进行还款");
                                }
                            } else if (type == 2) {
                                showWeixinDialog(url);
                            }
                        }
                        //回调Schema: tangguoxianjin://repayStatus
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    /**
     * @param codeImage 微信支付
     */
    private void showWeixinDialog(String codeImage) {
        WeiXinRepayDialog mDialog = new WeiXinRepayDialog(RepaymentActivity.this);
        mDialog.setTitle("微信还款");
        mDialog.setContent("手机屏幕截图后，打开微信扫一扫，选择相册中的屏幕截图，识别二维码进行还款~");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        mDialog.setImage(QRCodeUtil.addLogo(QRCodeUtil.CreateQRCodeImage(codeImage, 2 * new ScreenUtil(RepaymentActivity.this).getWidth() / 3, 2 * new ScreenUtil(RepaymentActivity.this).getWidth() / 3), bitmap));
        mDialog.setPositiveButton("还款完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRepayEndStatus(true);
            }
        });
        mDialog.setNegativeButton("取消");
        mDialog.show();
    }

    /**
     * 支付宝支付
     */
    private void showZhiFuBaoDialog() {
        CommonDialog mDialog = new CommonDialog(RepaymentActivity.this);
        mDialog.setTitle("支付宝还款结果查询");
        mDialog.setPositiveButton("还款结果查询", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRepayEndStatus(true);
            }
        });
        mDialog.show();
    }

    /**
     * 还款结果
     */
    private void requestRepayEndStatus(boolean flag) {
        if (StringUtils.isEmpty(repayId)) {
            showToast("还款记录id为空");
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(RepaymentActivity.this));
        map.put("repayId", repayId);
        requestNetData(UrlConstans.REPAY_MONEY_SUBMIT_STATUS, map, flag, MyConstants.HttpMethod.HTTP_POST, UrlConstans.REPAY_MONEY_SUBMIT_STATUS_CODE, new ResponseNewListener() {

            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        JSONObject jb = new JSONObject(json);
                        if (jb.has("status")) {
                            String status = jb.getString("status");
                            if (StringUtils.isEquals(status, "1")) {
                                showToast("支付成功");
                                setRepayEnd();
                            } else showToast("未完成支付");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void setViewData() {
        if (mBorrowRecordInfo == null) {
            idswipely.setVisibility(View.GONE);
            llrepaynothing.setVisibility(View.VISIBLE);
        } else {
            if (StringUtils.isEmpty(mBorrowRecordInfo.getRecordId())) llrepaynothing.setVisibility(View.VISIBLE);
            else {
                idswipely.setVisibility(View.VISIBLE);
                llrepaynothing.setVisibility(View.GONE);
                float progressValue = 0;


                if (StringUtils.isEquals(mBorrowRecordInfo.getRecordBorrowStatus(), "3") || StringUtils.isEquals(mBorrowRecordInfo.getRecordBorrowStatus(), "5"))
                    btnreturnmoney.setVisibility(View.VISIBLE);
                else btnreturnmoney.setVisibility(View.GONE);

                if (StringUtils.isEquals(mBorrowRecordInfo.getRecordBorrowStatus(), "5")) {
                    tvlinedayremark.setText("逾期天数");
                    progressValue = 0;
                    int daysStatus = -StringUtils.toInt(mBorrowRecordInfo.getRepayDay());
                    tvlineday.setText(daysStatus + "天");
                } else {
                    tvlinedayremark.setText("距离还款还有");
                    tvlineday.setText(mBorrowRecordInfo.getRepayDay() + "天");
                    try {
                        progressValue = StringUtils.toInt(mBorrowRecordInfo.getRepayDay()) * 360f / (float) StringUtils.toInt(mBorrowRecordInfo.getRecordBorrowLines());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                progressbar.setCurrentValues(progressValue);
                tvreturnmoney.setText("应还金额       " + mBorrowRecordInfo.getRepayMoney() + "元");
                tvreturnmoneydate.setText("应还日期       " + mBorrowRecordInfo.getRepayTime());
                tvpaymoneydate.setText("放款日期       " + mBorrowRecordInfo.getOpenMoneyTime());
                tvborrowmoney.setText("借款金额       " + mBorrowRecordInfo.getRecordBorrowMoney() + "元");
                //选择支付方式的还款金额
                tvrepaymoney.setText(mBorrowRecordInfo.getRepayMoney() + "元");
            }
        }
    }


    /**
     * 支付方式显示
     */
    private void setRepayWay() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(llrepaystepway, "translationX", new ScreenUtil(RepaymentActivity.this).getWidth(), 0f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                llrepaystepway.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                idswipely.setVisibility(View.GONE);
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
     * 支付结果
     */
    private void setRepayEnd() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(llrepayend, "translationX", new ScreenUtil(RepaymentActivity.this).getWidth(), 0f);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                llrepayend.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                llrepaystepway.setVisibility(View.GONE);
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
     * 支付方式选择
     */
    private void listSelectRepayWay() {
        String titleDialog = "请选择支付方式";
        List<String> listStr = new ArrayList<>();
        listStr.add("支付宝");
        listStr.add("微信");
        ListSelectDialog mListSelectDialog = new ListSelectDialog(this);
        mListSelectDialog.setTitle(titleDialog);
        mListSelectDialog.setStringList(listStr);
        mListSelectDialog.setSelectListener(new ListSelectDialog.SelectListener() {
            @Override
            public void OnSelectOne(ListDialogInfo mListDialogInfo) {
                tvrepaymoneyway.setText(mListDialogInfo.getTitle());
            }
        });
        mListSelectDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_BACK == keyCode) {
            backControl();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //点击返回的操作
    private void backControl() {
        if (llrepaystepway != null && llrepaystepway.getVisibility() == View.VISIBLE) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(llrepaystepway, "translationX", 0f, new ScreenUtil(RepaymentActivity.this).getWidth());
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    idswipely.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    llrepaystepway.setVisibility(View.GONE);
                    llrepayend.setVisibility(View.GONE);
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
        } else finish();
    }


}
