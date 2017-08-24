package com.tangguo.tangguoxianjin.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.tangguo.tangguoxianjin.R;
import com.tangguo.tangguoxianjin.activity.LoginActivity;
import com.tangguo.tangguoxianjin.activity.WebActivity;
import com.tangguo.tangguoxianjin.common.BaseActivity;
import com.tangguo.tangguoxianjin.config.MyConstants;

/**
 * Created by lhy on 2017/4/20.
 */

public class IntentUtil {


    /**
     * 活体
     */
    public static void liveBodyTake(Activity activity) {
    }


    /**
     * webActivty
     */
    public static void startWebActivity(BaseActivity activity, Class<?> fromClasss, String url) {
        if (!StringUtils.isEmpty(url)) {
            Bundle mBundle = new Bundle();
            mBundle.putSerializable(MyConstants.CLASS_FROM_ACTIVITY, fromClasss);
            mBundle.putString("web_view_url", url);
            activity.startAc(WebActivity.class, mBundle);
        }
    }


    /**
     * 登录
     */
    public static void startLogin(BaseActivity activity, Class<?> fromClasss) {
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(MyConstants.CLASS_FROM_ACTIVITY, fromClasss);
        activity.startAc(LoginActivity.class, mBundle);
        activity.overridePendingTransition(R.anim.activity_translate_up_into, R.anim.activity_alpha_out);
    }

    /**
     * 打电话
     */
    public static void callPhone(Activity activity, String number) {
        if (!StringUtils.isEmpty(number)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            activity.startActivity(intent);
        }
    }


    /**
     * 返回activity
     *
     * @param activity
     * @param calss
     * @param bundle
     */
    public static void backPageActivity(Activity activity, Class<?> calss, Bundle bundle) {
        Intent intent = new Intent(activity, calss);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (bundle != null) intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void backPageActivity(Activity activity, Class<?> calss) {
        backPageActivity(activity, calss, null);
    }


    /**
     * 发送一个intent
     * @param context
     * @param s
     */
    public static void openUri(Context context, String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        context.startActivity(intent);
    }


}
