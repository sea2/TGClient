package com.tangguo.tangguoxianjin.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.tangguo.tangguoxianjin.common.MyApplication;

import java.net.NetworkInterface;
import java.util.Enumeration;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Administrator on 2017/3/24.
 */

public class SystemUtil {


    public static String getPhoneVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取imei
     */
    public static String getImei() {
        try {
            TelephonyManager mTelephonyMgr = (TelephonyManager) MyApplication.getInstance().getSystemService(TELEPHONY_SERVICE);
            String imei = mTelephonyMgr.getDeviceId();
            if (imei == null) {
                imei = "000000000000000";
            }
            return imei;
        } catch (Exception e) {
            return "000000000000000";
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String getMacAddress(final Context context) {
        String macAddress = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaces.nextElement();
                    if (null != networkInterface.getHardwareAddress()) {
                        macAddress = displayMac(networkInterface.getHardwareAddress());
                    }
                    if (!StringUtils.isEmpty(macAddress)) {
                        break;
                    }
                }
            } catch (Exception ex) {
                if (null != ex) {
                    LogManager.e(ex.getMessage());
                }
            }
        }
        if (!StringUtils.isEmpty(macAddress)) {
            LogManager.d("NetworkInterface Mac == " + macAddress);
            return macAddress;
        }

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        macAddress = info.getMacAddress();
        if (!TextUtils.isEmpty(macAddress)) {
            return macAddress;
        } else {
            return "";
        }
    }

    public static String displayMac(byte[] mac) {
        StringBuffer strBuffer = new StringBuffer("");
        for (int i = 0; i < mac.length; i++) {
            int intValue = 0;
            byte b = mac[i];

            if (b >= 0) {
                intValue = b;
            } else {
                intValue = 256 + b;
            }
            String macChar = Integer.toHexString(intValue);
            if (!StringUtils.isEmpty(macChar)) {
                if (1 == macChar.length()) {
                    macChar = "0" + macChar;
                }
            }
            strBuffer.append(macChar);
            if (i != mac.length - 1) {
                strBuffer.append(":");
            }
        }
        return strBuffer.toString();
    }


    /**
     * 应用设置
     */
    public static void openSetting(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent);
        }
    }




    /**
     * 判断当前设备是否是模拟器。如果返回TRUE，则当前是模拟器，不是返回FALSE
     *
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                return true;
            }
            return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

}
