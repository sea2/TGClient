package com.tangguo.tangguoxianjin.config;


import com.tangguo.tangguoxianjin.BuildConfig;

/**
 * Created by Administrator on 2017/3/22.
 */

public class MyConstants {


    /**
     * 0:开发模式  DEV,
     * 1:生产模式  PRO,
     */
    public static final RunModel AppRunModel = ("release".equals(BuildConfig.BUILD_TYPE) && !"内部测试_1".equals(BuildConfig.FLAVOR)) ? RunModel.PRO : RunModel.DEV;
    // public static final RunModel AppRunModel =RunModel.PRO ;


    /**
     * APP所在运行环境
     */
    public static String getHost() {
        if (AppRunModel == RunModel.PRO) {// 生产地址
            return "https://app.91xcm.com/v1.0/app/";
        } else if (AppRunModel == RunModel.DEV) {//开发
//                return "http://192.168.3.171:8060/v1.0/app/";
               return "http://192.168.2.61:8080/v1.0/app/";
            //    return "http://192.168.3.35:8080/app/";
        }
        return "";
    }


    public enum RunModel {
        /**
         * 开发模式
         */
        DEV, /**
         * /**
         * 开发模式
         */
        TES, /**
         * 预发布
         */
        UAT, /**
         * 生产模式
         */
        PRO;
    }


    public enum HttpMethod {
        HTTP_GET, HTTP_POST, HTTP_DELETE, HTTP_PUT;
    }


    public static String CLASS_FROM_ACTIVITY = "class_from_activty";
    public static int CERTIFICATION_ID_POSITIVE = 100;  //身份证正面拍照
    public static int CERTIFICATION_ID_TURN = 101;   //身份证反面拍照
    public static int CERTIFICATION_LIVE_BODY = 99;   //活体拍照
    public static int CERTIFICATION_RALETION = 98;   //通讯录选择
    public static int CERTIFICATION_RALETION2 = 97;   //通讯录选择

    public static String CERTIFICATION_STEP = "certify_step";
    public static String CERTIFICATION_STEP_ONLY_PART = "certify_step_only_part";
    public static String CERTIFICATION_SHOW_TOP = "certify_step_show_top";
    public static int CERTIFICATION_STEP_ID = 0;  //身份认证
    public static int CERTIFICATION_STEP_PHONE = 1; //手机认证
    public static int CERTIFICATION_STEP_BANK = 2; //银行卡认证
    public static int CERTIFICATION_STEP_ONLY_PART_PHONE = 3;
    public static int CERTIFICATION_STEP_ONLY_PART_CONTACT = 4;
    public static int CERTIFICATION_STEP_ONLY_PART_SESAME = 5;
    public static int CERTIFICATION_STEP_SUCCESS = 6; //认证完成

    public static String MAIN_PAGE = "main_page";


    //第一次打开app
    public static String FIRST_START_APP = "first_start_app";

    //注册的短信回执no
    public static String VERIFICATION_NO = "verification_no";

    //登录框输入的手机号
    public static String LOGIN_INPUT_PHONE = "login_input_phone";


}
