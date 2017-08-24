package com.tangguo.tangguoxianjin.model;

import java.util.List;

/**
 * Created by lhy on 2017/5/15.
 */

public class BankSupportInfo {


    private List<BankListBean> bankList;

    public List<BankListBean> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankListBean> bankList) {
        this.bankList = bankList;
    }

    public static class BankListBean {
        /**
         * bankCode : 测试内容3h26
         * bankId : 测试内容wgiv
         * bankName : 测试内容722n
         * iconUrl : 测试内容9e2h
         */

        private String bankCode;
        private String bankId;
        private String bankName;
        private String iconUrl;

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getBankId() {
            return bankId;
        }

        public void setBankId(String bankId) {
            this.bankId = bankId;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }
    }
}
