package com.tangguo.tangguoxianjin.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;

/**
 * Created by lhy on 2017/5/19.
 */

public class AppUtil {


    /**
     * 是否已安装
     *
     * @param context
     * @param uri
     * @return
     */
    public static boolean isAppInstalled(Context context, String uri) {
        boolean installed = false;
        if (context == null || StringUtils.isEmpty(uri)) return installed;
        PackageManager pm = context.getPackageManager();

        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    /**
     * ��װ安装app
     *
     * @param context
     * @param fileName
     */
    public static void installApp(Context context, String fileName) {
        if (context == null || StringUtils.isEmpty(fileName)) return;
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 打开别的应用
     *
     * @param context
     * @param packageName
     */
    public static void openOtherApp(Context context, String packageName) {
        if (context == null || StringUtils.isEmpty(packageName)) return;
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) context.startActivity(intent);

    }
}
