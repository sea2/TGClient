package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.model.BorrowProgressInfo;
import com.tangguo.tangguoxianjin.net.ResponseNewListener;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.IntentUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.view.TitleLayout;

import java.util.HashMap;
import java.util.List;

public class BorrowProgressActivity extends BaseActivity {


    private ImageView ivprogressstep1;
    private ImageView ivprogressline1step;
    private ImageView ivprogressstep2;
    private ImageView ivprogressline2step;
    private ImageView ivprogressstep3;
    private ImageView ivprogressline3step;
    private ImageView ivprogressstep4;
    private TextView progressstep1;
    private TextView progressstep1detail;
    private TextView progressstep2;
    private TextView progressstep2detail;
    private TextView progressstep3;
    private TextView progressstep3detail;
    private TextView progressstep4;
    private TextView progressstep4detail;
    private Button btnconfirmrepay;
    private SwipeRefreshLayout swipelyborrowprogress;
    private String recordId = "";
    private Class<?> fromClass = null;
    boolean backMainPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_progress);
        this.swipelyborrowprogress = (SwipeRefreshLayout) findViewById(R.id.swipe_ly_borrow_progress);
        swipelyborrowprogress.setColorSchemeResources(R.color.color_theme);
        this.btnconfirmrepay = (Button) findViewById(R.id.btn_confirm_repay);
        this.progressstep4detail = (TextView) findViewById(R.id.progress_step4_detail);
        this.progressstep4 = (TextView) findViewById(R.id.progress_step4);
        this.progressstep3detail = (TextView) findViewById(R.id.progress_step3_detail);
        this.progressstep3 = (TextView) findViewById(R.id.progress_step3);
        this.progressstep2detail = (TextView) findViewById(R.id.progress_step2_detail);
        this.progressstep2 = (TextView) findViewById(R.id.progress_step2);
        this.progressstep1detail = (TextView) findViewById(R.id.progress_step1_detail);
        this.progressstep1 = (TextView) findViewById(R.id.progress_step1);
        this.ivprogressstep4 = (ImageView) findViewById(R.id.iv_progress_step4);
        this.ivprogressline3step = (ImageView) findViewById(R.id.iv_progress_line3_step);
        this.ivprogressstep3 = (ImageView) findViewById(R.id.iv_progress_step3);
        this.ivprogressline2step = (ImageView) findViewById(R.id.iv_progress_line2_step);
        this.ivprogressstep2 = (ImageView) findViewById(R.id.iv_progress_step2);
        this.ivprogressline1step = (ImageView) findViewById(R.id.iv_progress_line1_step);
        this.ivprogressstep1 = (ImageView) findViewById(R.id.iv_progress_step1);
        recordId = getIntent().getStringExtra("recordId");
        fromClass = (Class) getIntent().getSerializableExtra(MyConstants.CLASS_FROM_ACTIVITY);
        if (fromClass != null) {
            if (StringUtils.isEquals(fromClass.getSimpleName(), BorrowMoneyEndActivity.class.getSimpleName())) {
                backMainPage = true;
            }
        }

        initView();
        initListener();
        initData();
    }


    private void initView() {
        setTitle("借款进度");
        basePageView.setRightText("历史记录", new TitleLayout.OnRightListener() {
            @Override
            public void onClick() {
                startAc(BorrowRecordActivity.class);
            }
        });
        basePageView.setBack(new TitleLayout.OnBackListener() {
            @Override
            public void onBack() {
                backPage();
            }
        });
    }


    private void initListener() {
        //刷新
        swipelyborrowprogress.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        //立即还款
        btnconfirmrepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StringUtils.isEmpty(recordId)) {
                    Bundle mb = new Bundle();
                    mb.putString("recordId", recordId);
                    startAc(RepaymentActivity.class, mb);
                } else startAc(RepaymentActivity.class);
            }
        });

    }


    private void initData() {
        requestBorrowProgress();
    }


    /**
     * 进度
     */
    private void setProgressStep(int stepProgress) {
        switch (stepProgress) {
            case 0:

                progressstep1.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.tv_color_808080));
                progressstep2.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.tv_color_808080));
                progressstep3.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.tv_color_808080));
                progressstep4.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.tv_color_808080));
                ivprogressstep1.setBackgroundResource(R.drawable.icon_borrow_progress_wait);
                ivprogressstep2.setBackgroundResource(R.drawable.icon_borrow_progress_wait);
                ivprogressstep3.setBackgroundResource(R.drawable.icon_borrow_progress_wait);
                ivprogressstep4.setBackgroundResource(R.drawable.icon_borrow_progress_wait);
                ivprogressline1step.setBackgroundResource(R.drawable.shape_borrow_progress_wait);
                ivprogressline2step.setBackgroundResource(R.drawable.shape_borrow_progress_wait);
                ivprogressline3step.setBackgroundResource(R.drawable.shape_borrow_progress_wait);
                break;
            case 1:
                progressstep1.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.color_00c2cb));
                progressstep2.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.tv_color_808080));
                progressstep3.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.tv_color_808080));
                progressstep4.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.tv_color_808080));
                ivprogressstep1.setBackgroundResource(R.drawable.icon_borrow_progress_success);
                ivprogressstep2.setBackgroundResource(R.drawable.icon_borrow_progress_wait);
                ivprogressstep3.setBackgroundResource(R.drawable.icon_borrow_progress_wait);
                ivprogressstep4.setBackgroundResource(R.drawable.icon_borrow_progress_wait);
                ivprogressline1step.setBackgroundResource(R.drawable.shape_borrow_progress_wait);
                ivprogressline2step.setBackgroundResource(R.drawable.shape_borrow_progress_wait);
                ivprogressline3step.setBackgroundResource(R.drawable.shape_borrow_progress_wait);
                break;
            case 2:
                progressstep1.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.color_00c2cb));
                progressstep2.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.color_00c2cb));
                progressstep3.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.tv_color_808080));
                progressstep4.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.tv_color_808080));
                ivprogressstep1.setBackgroundResource(R.drawable.icon_borrow_progress_success);
                ivprogressstep2.setBackgroundResource(R.drawable.icon_borrow_progress_success);
                ivprogressstep3.setBackgroundResource(R.drawable.icon_borrow_progress_wait);
                ivprogressstep4.setBackgroundResource(R.drawable.icon_borrow_progress_wait);
                ivprogressline1step.setBackgroundResource(R.drawable.shape_borrow_progress_success);
                ivprogressline2step.setBackgroundResource(R.drawable.shape_borrow_progress_wait);
                ivprogressline3step.setBackgroundResource(R.drawable.shape_borrow_progress_wait);
                break;
            case 3:
                progressstep1.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.color_00c2cb));
                progressstep2.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.color_00c2cb));
                progressstep3.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.color_00c2cb));
                progressstep4.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.tv_color_808080));
                ivprogressstep1.setBackgroundResource(R.drawable.icon_borrow_progress_success);
                ivprogressstep2.setBackgroundResource(R.drawable.icon_borrow_progress_success);
                ivprogressstep3.setBackgroundResource(R.drawable.icon_borrow_progress_success);
                ivprogressstep4.setBackgroundResource(R.drawable.icon_borrow_progress_wait);
                ivprogressline1step.setBackgroundResource(R.drawable.shape_borrow_progress_success);
                ivprogressline2step.setBackgroundResource(R.drawable.shape_borrow_progress_success);
                ivprogressline3step.setBackgroundResource(R.drawable.shape_borrow_progress_wait);
                break;
            case 4:
                progressstep1.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.color_00c2cb));
                progressstep2.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.color_00c2cb));
                progressstep3.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.color_00c2cb));
                progressstep4.setTextColor(ContextCompat.getColor(BorrowProgressActivity.this, R.color.color_00c2cb));
                ivprogressstep1.setBackgroundResource(R.drawable.icon_borrow_progress_success);
                ivprogressstep2.setBackgroundResource(R.drawable.icon_borrow_progress_success);
                ivprogressstep3.setBackgroundResource(R.drawable.icon_borrow_progress_success);
                ivprogressstep4.setBackgroundResource(R.drawable.icon_borrow_progress_success);
                ivprogressline1step.setBackgroundResource(R.drawable.shape_borrow_progress_success);
                ivprogressline2step.setBackgroundResource(R.drawable.shape_borrow_progress_success);
                ivprogressline3step.setBackgroundResource(R.drawable.shape_borrow_progress_success);
                break;

        }
    }

    /**
     * 借款进度
     */
    private void requestBorrowProgress() {
        HashMap<String, String> map = new HashMap<String, String>();
        if (!StringUtils.isEmpty(recordId)) map.put("recordId", recordId);
        map.put("uid", AccountInfoUtils.getUid(BorrowProgressActivity.this));
        requestNetData(UrlConstans.BORROW_PROGRESS, map, true, MyConstants.HttpMethod.HTTP_POST, UrlConstans.BORROW_PROGRESS_CODE, new ResponseNewListener() {
            @Override
            public void OnResponse(String json, boolean successorfail) {
                if (successorfail && (!StringUtils.isEmpty(json))) {
                    try {
                        Gson gson = new Gson();
                        BorrowProgressInfo mBorrowProgressInfo = gson.fromJson(json, new TypeToken<BorrowProgressInfo>() {
                        }.getType());
                        if (mBorrowProgressInfo != null) {
                            if (StringUtils.isEquals(mBorrowProgressInfo.getCanRepay(), "1")) {
                                btnconfirmrepay.setVisibility(View.VISIBLE);
                            } else btnconfirmrepay.setVisibility(View.GONE);
                            List<BorrowProgressInfo.RecordListBean> mRecordList = mBorrowProgressInfo.getRecordList();
                            int step = 0;
                            if (mRecordList != null) {
                                for (int i = 0; i < mRecordList.size(); i++) {
                                    BorrowProgressInfo.RecordListBean mRecordListBean = mRecordList.get(i);
                                    if (mRecordListBean != null) {
                                        switch (i) {
                                            case 0:
                                                progressstep1.setText(mRecordListBean.getTitle());
                                                progressstep1detail.setText(mRecordListBean.getText());
                                                if (StringUtils.isEquals(mRecordListBean.getIsSelected(), "1")) step = 1;
                                                break;
                                            case 1:
                                                progressstep2.setText(mRecordListBean.getTitle());
                                                progressstep2detail.setText(mRecordListBean.getText());
                                                if (StringUtils.isEquals(mRecordListBean.getIsSelected(), "1")) step = 2;
                                                break;
                                            case 2:
                                                progressstep3.setText(mRecordListBean.getTitle());
                                                progressstep3detail.setText(mRecordListBean.getText());
                                                if (StringUtils.isEquals(mRecordListBean.getIsSelected(), "1")) step = 3;
                                                break;
                                            case 3:
                                                progressstep4.setText(mRecordListBean.getTitle());
                                                progressstep4detail.setText(mRecordListBean.getText());
                                                if (StringUtils.isEquals(mRecordListBean.getIsSelected(), "1")) step = 4;
                                                break;
                                        }
                                    }
                                }
                                setProgressStep(step);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                swipelyborrowprogress.setRefreshing(false);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_BACK == keyCode) {
            backPage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backPage() {
        if (backMainPage) {
            IntentUtil.backPageActivity(BorrowProgressActivity.this, MainActivity.class);
        } else finish();
    }

}
