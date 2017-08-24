package com.tangguo.tangguoxianjin.model;

/**
 * Created by lhy on 2017/5/15.
 */

public class ContactUrgentInfo {


    /**
     * kinPhone : 测试内容8s3a
     * kinRelationType : 测试内容tdu4
     * satus : 测试内容7i47
     * societyPhone : 测试内容c4ty
     * societyRelationType : 测试内容2in5
     */

    private String kinPhone;
    private String kinRelationType;
    private String status;
    private String societyPhone;
    private String societyRelationType;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKinPhone() {
        return kinPhone;
    }

    public void setKinPhone(String kinPhone) {
        this.kinPhone = kinPhone;
    }

    public String getKinRelationType() {
        return kinRelationType;
    }

    public void setKinRelationType(String kinRelationType) {
        this.kinRelationType = kinRelationType;
    }


    public String getSocietyPhone() {
        return societyPhone;
    }

    public void setSocietyPhone(String societyPhone) {
        this.societyPhone = societyPhone;
    }

    public String getSocietyRelationType() {
        return societyRelationType;
    }

    public void setSocietyRelationType(String societyRelationType) {
        this.societyRelationType = societyRelationType;
    }
}
