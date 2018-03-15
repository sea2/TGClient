package com.tangguo.tangguoxianjin.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.leakcanary.RefWatcher;
import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.common.MyApplication;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.util.SharePreferenceUtil;
import com.tangguo.tangguoxianjin.util.StringUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

public class WelcomeActivity extends BaseActivity {

    boolean isFirstResume = true;
    Subscription subscription = null;
    private android.widget.ImageView ivadbg;
    private android.widget.Button btngetcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isUserDefinedColorForStatusBar = false;
        isUserDefinedTitle = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        this.btngetcode = (Button) findViewById(R.id.btn_get_code);
        this.ivadbg = (ImageView) findViewById(R.id.iv_ad_bg);


        btngetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endWelcome();
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_BACK == keyCode) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstResume) {


            final int count = 3;
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
                     endWelcome();
                    if (subscription != null) subscription.unsubscribe();
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Long aLong) { //接受到一条就是会操作一次UI
                    Log.d(TAG, "onNext: " + aLong);
                    btngetcode.setText(String.valueOf(aLong).concat("S"));
                }
            });

        }

    }


    /**
     * 结束欢迎页
     */
    private void endWelcome() {
        if (StringUtils.isEquals(SharePreferenceUtil.getInstance().getValue(MyConstants.FIRST_START_APP, WelcomeActivity.this), "0")) {
            startAc(MainActivity.class);
            finish();
        } else {
            SharePreferenceUtil.getInstance().save(MyConstants.FIRST_START_APP, "0", WelcomeActivity.this);
            startAc(StaticPageGuideActivity.class);
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }
}