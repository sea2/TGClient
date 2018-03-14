package com.tangguo.tangguoxianjin.net;

import com.google.gson.Gson;
import com.tangguo.tangguoxianjin.common.AppInfoHelper;
import com.tangguo.tangguoxianjin.common.MyApplication;
import com.tangguo.tangguoxianjin.config.MyConstants;
import com.tangguo.tangguoxianjin.util.ManifestMetaDataHelper;
import com.tangguo.tangguoxianjin.util.NetworkUtils;
import com.tangguo.tangguoxianjin.util.StringUtils;
import com.tangguo.tangguoxianjin.util.SystemUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by lhy on 2018/3/13.
 */

public class OkHttpManager {

    private final String urlStr; //必选
    private MyConstants.HttpMethod mHttpMethod;
    private Map<String, String> hasMap;
    private int resquestCode;

    OkHttpManager(Builder builder) {
        urlStr = builder.urlStr;
        mHttpMethod = builder.mHttpMethod;
        hasMap = builder.hasMap;
        resquestCode = builder.resquestCode;
    }


    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        if (resquestCode != 0) {
            OkHttpUtils.getInstance().cancelTag(resquestCode);
        }
    }


    public static class Builder {
        private final String urlStr; //必选
        private MyConstants.HttpMethod mHttpMethod=MyConstants.HttpMethod.HTTP_GET;
        private Map<String, String> hasMap;
        private int resquestCode=0;
        private RequestCall call = null;


        public Builder(String urlStr) {
            this.urlStr = urlStr;
        }


        /**
         * 设置请求方式 post get
         *
         * @param mHttpMethod
         * @return
         */
        public Builder setHttpMethod(MyConstants.HttpMethod mHttpMethod) {
            this.mHttpMethod = mHttpMethod;
            return this;
        }

        /**
         * 设置请求标示
         *
         * @param resquestCode
         * @return
         */
        public Builder setResquestCode(int resquestCode) {
            this.resquestCode = resquestCode;
            return this;
        }

        /**
         * 设置请求参数
         *
         * @param hasMap
         * @return
         */
        public Builder setResquestParam(Map<String, String> hasMap) {
            this.hasMap = hasMap;
            return this;
        }



        public OkHttpManager Builder(final OkHttpListener listener) {

            HashMap<String, String> map = addCommonParams(hasMap);
            switch (mHttpMethod) {
                case HTTP_GET://get
                    GetBuilder mGetBuilder = OkHttpUtils.get().url(getUrl(urlStr, hasMap)).tag(resquestCode);
                    call = mGetBuilder.build();
                    break;
                case HTTP_POST: //post
                    PostStringBuilder post = OkHttpUtils.postString();
                    post.mediaType(MediaType.parse("application/json; charset=utf-8")).url(urlStr).content(new Gson().toJson(map)).tag(resquestCode);
                    call = post.build();
                    break;
                case HTTP_PUT: //put
                    OtherRequestBuilder put = OkHttpUtils.put();
                    put.url(urlStr).tag(resquestCode).mediaType(MediaType.parse("application/json; charset=utf-8")).requestBody(new Gson().toJson(map));
                    call = put.build();
                    break;
                case HTTP_DELETE: //delete
                    OtherRequestBuilder delete = OkHttpUtils.delete();
                    delete.url(urlStr).tag(resquestCode).requestBody(new Gson().toJson(map));
                    call = delete.build();
                    break;
                default:
                    GetBuilder mGetBuilderDefault = OkHttpUtils.get().url(getUrl(urlStr, hasMap)).tag(resquestCode);
                    call = mGetBuilderDefault.build();
                    break;
            }

            if (call != null) {
                call.execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (call != null && (!call.isCanceled())) { //被取消的网络不在回调
                            if (listener != null) {
                                String error = "网络错误";
                                if (!NetworkUtils.IsNetWorkEnable(MyApplication.getInstance())) {
                                    error = "当前网络不可用，请检查您的网络设置";
                                }
                                listener.OnError(resquestCode,error);
                            }
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (listener != null) {
                            try {
                                JSONObject json = new JSONObject(response);
                                listener.OnSuccess(resquestCode,json);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.OnError(resquestCode,"json数据格式错误");
                            }

                        }
                    }
                });
            }


            return new OkHttpManager(this);
        }


    }


    /**
     * Get地址拼接
     */
    private static String getUrl(String uri, Map<String, String> map) {
        StringBuilder strBuffer = new StringBuilder();
        if (map != null && map.size() > 0) {
            strBuffer.append(uri.concat("?"));
            Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                if (iter.hasNext()) {
                    if ((!StringUtils.isEmpty(entry.getKey())) && entry.getValue() != null)
                        strBuffer.append(entry.getKey().concat("=" + entry.getValue() + "&"));
                } else {
                    if ((!StringUtils.isEmpty(entry.getKey())) && entry.getValue() != null)
                        strBuffer.append(entry.getKey().concat("=" + entry.getValue()));
                }
            }
        }
        return strBuffer.toString();
    }


    private static HashMap<String, String> addCommonParams(Map<String, String> map) {
        if (map == null) map = new HashMap<>();
        map.put("app_version", AppInfoHelper.getInstance().getAppVersionNumber());
        map.put("version", SystemUtil.getPhoneVersion());
        map.put("market", ManifestMetaDataHelper.getMetaDataChannelKey());
        map.put("mac", SystemUtil.getMacAddress(MyApplication.getInstance()));
        map.put("imei", SystemUtil.getImei());
        map.put("channel", "4");
        map.put("current_time", System.currentTimeMillis() + "");
        HashMap<String, String> paramss = new HashMap<>();
        if (map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if ((!StringUtils.isEmpty(entry.getKey())) && entry.getValue() != null) {
                    paramss.put(entry.getKey().trim(), entry.getValue().trim());
                }
            }
        }
        return paramss;
    }


}
