package com.tangguo.tangguoxianjin.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.tangguo.tangguoxianjin.common.MyApplication;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 倒计时工具(目前用于验证码倒计时)
 *
 * @author yejingjie
 */
public class CountdownUtil {
    public static final int REMNANT_TIME = 60;// 剩余时间(单位秒)

    private final int MSG_CHANGE = 1;
    private final int MSG_FINISH = 2;

    private String curType = null;
    private int remnantTime = REMNANT_TIME;// 剩余时间

    private Timer oneSecondTimer = null;
    private SendMsgTimerTask currTimerTask = null;

    private OnCountdownListener listener = null;
    private Context context = MyApplication.getInstance();


    public CountdownUtil(String type) {
        curType = type;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case MSG_CHANGE:
                    listener.onTimeChanged(msg.arg1);
                    break;
                case MSG_FINISH:
                    listener.onFinish();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 开启时间倒计时
     */
    public synchronized void openCountdown(final OnCountdownListener listener) {
        if (currTimerTask == null) {
            currTimerTask = new SendMsgTimerTask();
            this.listener = listener;
            if (null == oneSecondTimer) {
                oneSecondTimer = new Timer();
            }
            remnantTime = getRemnantTime();
            if (!isRunCountdown()) {
                recordTime();
            }
            if (null != listener) {
                listener.onOpen(remnantTime);
            }
            oneSecondTimer.schedule(currTimerTask, 0, 1000);
        }

    }

    /**
     * 是否正在倒计时
     *
     * @return
     */
    public boolean isRunCountdown() {
        if (Math.abs(System.currentTimeMillis() - Store.getVerifyTime(context, Store.RECORD_CURR_OBTAIN_TIME + curType, 0l)) > REMNANT_TIME * 1000) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 关闭时间倒计时
     */
    public void finishCountdown() {
        if (null != currTimerTask) {
            currTimerTask.cancel();
            currTimerTask = null;
        }
        if (null != oneSecondTimer) {
            oneSecondTimer.cancel();
            oneSecondTimer = null;
        }
    }

    /**
     * 时间倒计时监听
     *
     * @author yejingjie
     */
    public static interface OnCountdownListener {
        /**
         * 倒计时打开
         */
        public void onOpen(int remnantTime);

        /**
         * 时间改变
         */
        public void onTimeChanged(int remnantTime);

        /**
         * 倒计时结束
         */
        public void onFinish();
    }

    // 获取剩余时间(单位秒)
    private int getRemnantTime() {
        long time = REMNANT_TIME * 1000 - (long) Math.abs((System.currentTimeMillis() - Store.getVerifyTime(context, Store.RECORD_CURR_OBTAIN_TIME + curType, 0l)));
        if (time <= 0 || time > (REMNANT_TIME * 1000)) {
            time = REMNANT_TIME * 1000;
        }
        return (int) (time / 1000);
    }

    // 存时间
    private void recordTime() {
        Store.putVerifyTime(context, Store.RECORD_CURR_OBTAIN_TIME + curType, System.currentTimeMillis());
    }

    // 删除时间
    private void removeTime() {
        Store.removeVerifyTime(context, Store.RECORD_CURR_OBTAIN_TIME + curType);
    }

    private class SendMsgTimerTask extends TimerTask {

        @Override
        public void run() {
            if (remnantTime > 1) {
                if (null != listener) {
                    --remnantTime;
                    Message msg = Message.obtain();
                    msg.what = MSG_CHANGE;
                    msg.arg1 = remnantTime;
                    handler.sendMessage(msg);
                }
            } else {
                if (null != listener) {
                    handler.sendEmptyMessage(MSG_FINISH);
                }
                remnantTime = REMNANT_TIME;
                removeTime();
                currTimerTask = null;
                cancel();
            }
        }
    }

    ;

    public void cancel() {
        if (null != oneSecondTimer) {
            oneSecondTimer.cancel();
            oneSecondTimer.purge();
            oneSecondTimer = null;
        }
    }

    public void cancelAndRemove() {
        if (null != oneSecondTimer) {
            oneSecondTimer.cancel();
            oneSecondTimer.purge();
            oneSecondTimer = null;
        }
        if (null != currTimerTask) {
            currTimerTask.cancel();
            currTimerTask = null;
        }
        removeTime();
    }

}
