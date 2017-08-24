package com.tangguo.tangguoxianjin.util;

import android.content.Context;

import com.tangguo.tangguoxianjin.common.MyApplication;


/**
 * Created by lhy on 2017/5/3.
 */

public class AccountInfoUtils {

    public static final String ACCOUNT_UID = "ACCOUNT_UID";
    public static final String ACCOUNT_PHONE = "ACCOUNT_PHONE";


    /**
     * 获取uid
     *
     * @param context
     * @return
     */
    public static String getUid(Context context) {
        String uidStr = "";
        uidStr = SharePreferenceUtil.getInstance().getValue(ACCOUNT_UID, context);
        return uidStr;
    }


    /**
     * 保存uid
     *
     * @param context
     * @param valueStr
     * @return
     */
    public static boolean setUid(Context context, String valueStr) {
        boolean uidSuccess = false;
        if (!StringUtils.isEmpty(valueStr)) uidSuccess = SharePreferenceUtil.getInstance().save(ACCOUNT_UID, valueStr, context);
        return uidSuccess;
    }

    /**
     * 获取手机号
     *
     * @param context
     * @return
     */
    public static String getAccountPhone(Context context) {
        String uidStr = "";
        uidStr = SharePreferenceUtil.getInstance().getValue(ACCOUNT_PHONE, context);
        return uidStr;
    }


    /**
     * 保存手机号
     *
     * @param context
     * @param valueStr
     * @return
     */
    public static boolean setAccountPhone(Context context, String valueStr) {
        boolean uidSuccess = false;
        if (!StringUtils.isEmpty(valueStr)) uidSuccess = SharePreferenceUtil.getInstance().save(ACCOUNT_PHONE, valueStr, context);
        return uidSuccess;
    }


    /**
     * 退出登录
     */
    public static void logoutClearLoginInfo() {
        SharePreferenceUtil.getInstance().delete(ACCOUNT_UID, MyApplication.getInstance());
        SharePreferenceUtil.getInstance().delete(ACCOUNT_PHONE, MyApplication.getInstance());

    }

}
