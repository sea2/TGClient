package com.tangguo.tangguoxianjin.model;

/**
 * Created by lhy on 2017/5/3.
 */

public class LoginInfo {


    /**
     * status : 0:验证码注册 ,1:验证码登录 ,2:密码登录
     * agreement_title : 用户协议标题
     * agreement_url : 用户协议链接
     * scheme_url : 操作后跳转注册输入验证码scheme页面
     * cert_indentity : 实名
     */

    private String status;
    private String agreement_title;
    private String agreement_url;
    private String scheme_url;
    private String cert_indentity;

    public String getCert_indentity() {
        return cert_indentity;
    }

    public void setCert_indentity(String cert_indentity) {
        this.cert_indentity = cert_indentity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgreement_title() {
        return agreement_title;
    }

    public void setAgreement_title(String agreement_title) {
        this.agreement_title = agreement_title;
    }

    public String getAgreement_url() {
        return agreement_url;
    }

    public void setAgreement_url(String agreement_url) {
        this.agreement_url = agreement_url;
    }

    public String getScheme_url() {
        return scheme_url;
    }

    public void setScheme_url(String scheme_url) {
        this.scheme_url = scheme_url;
    }
}
