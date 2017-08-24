package com.tangguo.tangguoxianjin.util;

import android.content.Context;
import android.text.TextUtils;

import com.tangguo.tangguoxianjin.view.ToastShow;

/**
 * Created by lhy on 2017/5/10.
 */

public class ToastUtils {

    /**
     * 弹出提示框
     *
     * @param conent 提示语句
     * @author windy 2014-8-12 下午7:56:53
     */
    public static void showToast(Context context, String conent) {
        ToastShow toastShow = new ToastShow(context);
        if (!TextUtils.isEmpty(conent)) {
            toastShow.show(conent);
        }
    }


    public static void showToastLong(Context context, String conent) {
        ToastShow toastShow = new ToastShow(context, true);
        if (!TextUtils.isEmpty(conent)) {
            toastShow.show(conent);
        }
    }

    /**
     * 弹出提示框
     *
     * @param str 本地文本资源id
     * @author windy 2014-8-12 下午7:58:14
     */
    public static void showToast(Context context, int str) {
        ToastShow toastShow = new ToastShow(context);
        toastShow.show(context.getString(str));
    }

    public static void showToastLong(Context context, int str) {
        ToastShow toastShow = new ToastShow(context, true);
        toastShow.show(context.getString(str));
    }


}
