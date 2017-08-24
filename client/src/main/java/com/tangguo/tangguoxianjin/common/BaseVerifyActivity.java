package com.tangguo.tangguoxianjin.common;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.util.CountdownUtil;

/**
 * 验证界面基类
 *
 * @author yejingjie
 */
public abstract class BaseVerifyActivity extends BaseActivity {
    private CountdownUtil mCountdownUtil;
    private Button btn_get_code = null;


    protected final String VERIFY_CODE_REGISTER = "register_verify_code";
    protected final String VERIFY_CODE_RESET = "reset_verify_code";


    /**
     * 初始化倒计时
     *
     * @param type
     */
    protected void initCountdown(String type) {
        mCountdownUtil = new CountdownUtil(type);
        if (mCountdownUtil.isRunCountdown()) {
            mCountdownUtil.openCountdown(mOnCountdownListener);
        }
    }

    protected void clearCountDown(String type) {
        if (null != mCountdownUtil) {
            mCountdownUtil.cancelAndRemove();
        }
    }

    protected void finishCountdown() {
        if (null != mCountdownUtil) {
            mCountdownUtil.finishCountdown();
        }
    }

    /**
     * 打开倒计时
     */
    protected void openCountDown(boolean isNeedNotify) {
        if (null != mCountdownUtil) {
            mCountdownUtil.openCountdown(mOnCountdownListener);
        }
        if (isNeedNotify) {
            showToast("短信已下发至您手机,请注意查收!");
        }
    }


    /**
     * 重新获取验证码
     *
     * @param type
     */
    protected void reSetCode(String type) {
        clearCountDown(type);
        onFinishTime();
        if (btn_get_code != null) {
            btn_get_code.setSelected(false);
            btn_get_code.setText("重新获取");
            btn_get_code.setTextColor(getResources().getColor(R.color.color_theme));
        }
    }

    /**
     * 是否正在倒计时
     *
     * @return
     */
    protected boolean isRunCountdown() {
        return mCountdownUtil.isRunCountdown();
    }

    /**
     * 倒计时开始回调
     */
    protected void onStartTime() {
        // 子类实现
    }

    /**
     * 倒计时结束回调
     */
    protected void onFinishTime() {
        // 子类实现
    }

    /**
     * 是否可请求验证码
     *
     * @return
     */
    protected boolean isCanRequestCode() {
        if (null == btn_get_code) {
            return false;
        }
        return !btn_get_code.isSelected();
    }

    /**
     * 获得获取验证码按钮
     *
     * @return
     */
    protected abstract Button getBtnGetCodeView();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        btn_get_code = getBtnGetCodeView();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        btn_get_code = getBtnGetCodeView();
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        btn_get_code = getBtnGetCodeView();
    }

    private CountdownUtil.OnCountdownListener mOnCountdownListener = new CountdownUtil.OnCountdownListener() {

        @Override
        public void onOpen(int remnantTime) {
            if (null == btn_get_code) {
                return;
            }
            btn_get_code.setSelected(true);
            onStartTime();
            // btn_get_code.setTextColor(getResources().getColor(
            // R.color.tv_color_99));
            btn_get_code.setText(remnantTime + "秒后重试");
            btn_get_code.setTextColor(ContextCompat.getColor(BaseVerifyActivity.this, R.color.tv_color_99));
        }

        @Override
        public void onTimeChanged(int remnantTime) {
            if (null == btn_get_code) {
                return;
            }
            btn_get_code.setText(remnantTime + "秒后重试");
        }

        @Override
        public void onFinish() {
            if (null == btn_get_code) {
                return;
            }
            btn_get_code.setSelected(false);
            onFinishTime();
            // btn_get_code.setTextColor(getResources().getColor(
            // R.color.mc_blue_light));
            btn_get_code.setText("重新获取");
            btn_get_code.setTextColor(getResources().getColor(R.color.color_theme));
        }
    };

    @Override
    public void finish() {
        super.finish();
        if (null != mCountdownUtil) {
            mCountdownUtil.finishCountdown();
        }
    }
}
