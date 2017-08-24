package com.tangguo.tangguoxianjin.config;

/**
 * Created by Administrator on 2017/3/24.
 */

public interface UrlConstans {
    String HOST = MyConstants.getHost();

    String ACCOUNT_TEST_GET = HOST + "account/adddatatestget";
    //检测手机号是否注册
    String LOGIN_OPEN = HOST + "login/open";
    int LOGIN_OPEN_CODE = 1;
    //发送验证码
    String VERIFICATION_SENDSMS = HOST + "verification/sendSMS";
    int VERIFICATION_SENDSMS_CODE = 2;
    //注册
    String ACCOUNT_REGIST = HOST + "register";
    int ACCOUNT_REGIST_CODE = 3;
    //登录
    String ACCOUNT_LOGIN = HOST + "login/login";
    int ACCOUNT_LOGIN_CODE = 4;
    //重置登录密码
    String ACCOUNT_RESET_PASSWORD = HOST + "password/reset";
    int ACCOUNT_RESET_PASSWORD_CODE = 5;
    //广播公告
    String ACCOUNT_NOTICE_ADV = HOST + "account/notice";
    int ACCOUNT_NOTICE_ADV_CODE = 6;
    //广播公告
    String ADVERT_HOME = HOST + "advert/home";
    int ADVERT_HOME_CODE = 7;
    //我的消息
    String ACCOUNT_MESSAGE = HOST + "account/message";
    int ACCOUNT_MESSAGE_CODE = 8;
    //账户信息
    String ACCOUNT_BASE = HOST + "account/base";
    int ACCOUNT_BASE_CODE = 9;
    //认证状态
    String AUTH_STATUS = HOST + "realname/findMbVtStatus";
    int AUTH_STATUS_CODE = 10;
    //借款费用计算
    String BORROW_CALCMONEY = HOST + "borrow/calcMoney";
    int BORROW_CALCMONEY_CODE = 11;
    //借款页面配置信息
    String BORROW_CONFIG = HOST + "borrow/configuration";
    int BORROW_CONFIG_CODE = 12;
    //发起借款
    String BORROW_SUBMIT = HOST + "borrow/submit";
    int BORROW_SUBMIT_CODE = 13;
    //身份信息
    String REALNAME_INFO = HOST + "realname/info";
    int REALNAME_INFO_CODE = 14;
    //上传图片
    String REALNAME_UPLOAD_IMG = HOST + "realname/uploadIdCardImg";
    int REALNAME_UPLOAD_IMG_CODE = 15;
    //保存身份信息
    String SAVE_ID_INFO_CERTIFY = HOST + "realname/saveMbInfoParams";
    int SAVE_ID_INFO_CERTIFY_CODE = 16;
    //手机运行商密码提交
    String PHONE_VALID_PASSWORD = HOST + "phone/validOperatorPassword";
    int PHONE_VALID_PASSWORD_CODE = 17;
    //重新获取验证码
    String PHONE_RETRY_VALID_PASSWORD = HOST + "phone/retryOperatorCode";
    int PHONE_RETRY_VALID_PASSWORD_CODE = 18;
    //提交服务商验证码
    String PHONE_VALIDOPERATOR = HOST + "phone/validOperatorCode";
    int PHONE_VALIDOPERATOR_CODE = 19;
    //查询紧急联系人
    String CONTACT_FIND_INFO = HOST + "contact/findContact";
    int CONTACT_FIND_INFO_CODE = 20;
    //保存紧急联系人
    String CONTACT_SAVE_INFO = HOST + "contact/saveContact";
    int CONTACT_SAVE_INFO_CODE = 21;
    //我的银行卡信息
    String MY_BANKCARD_INFO = HOST + "bankcard/info";
    int MY_BANKCARD_INFO_CODE = 22;
    //支持银行卡
    String BANK_CARD_SUPPORT = HOST + "bankcard/support";
    int BANK_CARD_SUPPORT_CODE = 23;
    //添加银行卡
    String BANK_CARD_ADD = HOST + "bankcard/add";
    int BANK_CARD_ADD_CODE = 24;
    //删除银行卡
    String BANK_CARD_DEL = HOST + "bankcard/del";
    int BANK_CARD_DEL_CODE = 25;
    //还款详情
    String REPAY_MONEY_DETAIL = HOST + "borrow/detail";
    int REPAY_MONEY_DETAIL_CODE = 26;
    //还款
    String REPAY_MONEY_SUBMIT = HOST + "repay/submit";
    int REPAY_MONEY_SUBMIT_CODE = 27;
    //还款结果
    String REPAY_MONEY_SUBMIT_STATUS = HOST + "repay/status";
    int REPAY_MONEY_SUBMIT_STATUS_CODE = 28;
    //借款进度
    String BORROW_PROGRESS = HOST + "borrow/progress";
    int BORROW_PROGRESS_CODE = 29;
    //借款记录
    String BORROW_RECORD = HOST + "borrow/record";
    int BORROW_RECORD_CODE = 30;
    //帮助
    String ACCOUNT_HELP = HOST + "account/help";
    int ACCOUNT_HELP_CODE = 31;
}
