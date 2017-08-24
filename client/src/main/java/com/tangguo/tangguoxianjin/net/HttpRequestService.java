package com.tangguo.tangguoxianjin.net;

import android.content.Context;

import com.tangguo.tangguoxianjin.common.MyApplication;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.config.UrlConstans;
import com.tangguo.tangguoxianjin.util.AccountInfoUtils;
import com.tangguo.tangguoxianjin.util.LogManager;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.util.ToastUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

/**
 * Created by lhy on 2017/5/10.
 */

public class HttpRequestService {


    /**
     * 用户基本信息
     *
     * @param context
     * @param listener
     */
    public static void requestBaseAccount(final Context context, final ResponseNewListener listener) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(MyApplication.getInstance()));
        new RequestManager().getServerData(UrlConstans.ACCOUNT_BASE, map, MyConstants.HttpMethod.HTTP_POST, UrlConstans.ACCOUNT_BASE_CODE, new ResponseListener() {
            @Override
            public void OnSuccess(JSONObject json) {
                String jsonStr = "";
                String resultStr = "";//为空时 code不等于200或者不存在result
                if (json != null) jsonStr = json.toString();
                LogManager.d(jsonStr);
                if (listener != null) {
                    try {
                        JSONObject mJSONObject = new JSONObject(jsonStr);
                        if (mJSONObject.has("code")) {
                            if (StringUtils.isEquals(mJSONObject.getString("code"), "200")) {
                                if (mJSONObject.has("result")) {
                                    resultStr = mJSONObject.getString("result");
                                }
                            } else {
                                if (mJSONObject.has("message")) {
                                    if (!StringUtils.isEmpty(mJSONObject.getString("message")))
                                        ToastUtils.showToast(context, mJSONObject.getString("message"));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    listener.OnResponse(resultStr, true);
                }
            }

            @Override
            public void OnError(String error) {
                if (listener != null) {
                    listener.OnResponse(error, false);
                }
                if (!StringUtils.isEmpty(error)) ToastUtils.showToast(context, error);
            }
        });


    }

    /**
     * 认证状态
     *
     * @param context
     * @param listener
     */
    public static void requestCertifyStatus(final Context context, final ResponseNewListener listener) {
        if (StringUtils.isEmpty(AccountInfoUtils.getUid(MyApplication.getInstance()))) {
            listener.OnResponse("", false);
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("uid", AccountInfoUtils.getUid(MyApplication.getInstance()));
        new RequestManager().getServerData(UrlConstans.AUTH_STATUS, map, MyConstants.HttpMethod.HTTP_POST, UrlConstans.AUTH_STATUS_CODE, new ResponseListener() {
            @Override
            public void OnSuccess(JSONObject json) {
                String jsonStr = "";
                String resultStr = "";//为空时 code不等于200或者不存在result
                if (json != null) jsonStr = json.toString();
                LogManager.d(jsonStr);
                if (listener != null) {
                    try {
                        JSONObject mJSONObject = new JSONObject(jsonStr);
                        if (mJSONObject.has("code")) {
                            if (StringUtils.isEquals(mJSONObject.getString("code"), "200")) {
                                if (mJSONObject.has("result")) {
                                    resultStr = mJSONObject.getString("result");
                                }
                            } else {
                                if (mJSONObject.has("message")) {
                                    if (!StringUtils.isEmpty(mJSONObject.getString("message")))
                                        ToastUtils.showToast(context, mJSONObject.getString("message"));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    listener.OnResponse(resultStr, true);
                }
            }

            @Override
            public void OnError(String error) {
                if (listener != null) {
                    listener.OnResponse(error, false);
                }
                if (!StringUtils.isEmpty(error)) ToastUtils.showToast(context, error);
            }
        });
    }

    /**
     * 上传
     *
     * @param listener
     */
    public static void requestUploadImg(final Context context, String urlUpload, HashMap<String, String> hasMap, int resquestCode, File file, final ResponseNewListener listener) {
        new RequestManager().uploadData(urlUpload, hasMap, resquestCode, file, new ResponseListener() {
            @Override
            public void OnSuccess(JSONObject json) {
                String jsonStr = "";
                String resultStr = "";//为空时 code不等于200或者不存在result
                if (json != null) jsonStr = json.toString();
                LogManager.d(jsonStr);
                if (listener != null) {
                    try {
                        JSONObject mJSONObject = new JSONObject(jsonStr);
                        if (mJSONObject.has("code")) {
                            if (StringUtils.isEquals(mJSONObject.getString("code"), "200")) {
                                if (mJSONObject.has("result")) {
                                    resultStr = mJSONObject.getString("result");
                                }
                            } else {
                                if (mJSONObject.has("message")) {
                                    if (!StringUtils.isEmpty(mJSONObject.getString("message")))
                                        ToastUtils.showToast(context, mJSONObject.getString("message"));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    listener.OnResponse(resultStr, true);
                }
            }

            @Override
            public void OnError(String json) {
                if (listener != null) {
                    listener.OnResponse(json, false);
                }
                if (!StringUtils.isEmpty(json)) ToastUtils.showToast(context, json);
            }
        });
    }


}
