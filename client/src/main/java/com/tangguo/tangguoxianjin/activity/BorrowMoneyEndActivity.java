package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.view.TitleLayout;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

public class BorrowMoneyEndActivity extends BaseActivity {

    private android.widget.ProgressBar idloadingprogressbar;
    private android.widget.TextView tvborrowendsecond;
    private android.widget.TextView tvborrowendsecondsuccess;
    private android.widget.TextView tvborrowendsecondremark;
    private android.widget.Button btnconfirm;
    private Subscription subscription = null;
    private android.widget.RelativeLayout rltopprogress;
    private String recordId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_money_end);
        this.rltopprogress = (RelativeLayout) findViewById(R.id.rl_top_progress);
        this.btnconfirm = (Button) findViewById(R.id.btn_confirm);
        this.tvborrowendsecondremark = (TextView) findViewById(R.id.tv_borrow_end_second_remark);
        this.tvborrowendsecondsuccess = (TextView) findViewById(R.id.tv_borrow_end_second_success);
        this.tvborrowendsecond = (TextView) findViewById(R.id.tv_borrow_end_second);
        this.idloadingprogressbar = (ProgressBar) findViewById(R.id.id_loading_progressbar);
        recordId = getIntent().getStringExtra("recordId");
        initListener();
        initTitle();
        initData();

    }


    private void initTitle() {
        setTitle("借款申请提交完成");
        if (basePageView != null) {
            basePageView.setBack(new TitleLayout.OnBackListener() {
                @Override
                public void onBack() {

                }
            });
        }
        hideBack();
    }

    private void initListener() {
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle mb = new Bundle();
                mb.putString("recordId", recordId);
                mb.putSerializable(MyConstants.CLASS_FROM_ACTIVITY, BorrowMoneyEndActivity.class);
                startAc(BorrowProgressActivity.class, mb);
            }
        });
    }

    private void initData() {


        btnconfirm.setText("查询借款进度");
        tvborrowendsecondsuccess.setText("恭喜您，借款申请提交完成");
        tvborrowendsecondremark.setText("您申请的借款，系统正在核对信息，请关注借款进度");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_BACK == keyCode) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) subscription.unsubscribe();
    }


    private void setWaitProgress() {
        final int count = 5;
        subscription = Observable.interval(0, 1, TimeUnit.SECONDS).take(count + 1).map(new Func1<Long, Long>() {
            @Override
            public Long call(Long aLong) {
                return count - aLong;
            }
        }).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                //在call之前执行一些初始化操作
                Log.d(TAG, "doOnSubscribe: ");
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Long>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: ");
                if (subscription != null) subscription.unsubscribe();
                rltopprogress.setBackgroundResource(R.drawable.icon_borrow_end_top_success);
                tvborrowendsecondremark.setVisibility(View.VISIBLE);
                tvborrowendsecondsuccess.setVisibility(View.VISIBLE);
                tvborrowendsecond.setVisibility(View.GONE);
                idloadingprogressbar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Long aLong) { //接受到一条就是会操作一次UI
                Log.d(TAG, "onNext: " + aLong);
                if (tvborrowendsecond != null) tvborrowendsecond.setText(String.valueOf(aLong).concat("S"));
            }
        });
    }


}
