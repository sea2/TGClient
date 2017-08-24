package com.tangguo.tangguoxianjin.model;

/**
 * Created by lhy on 2017/5/11.
 */

public class UploadImgInfo {


    /**
     * code : 36203
     * result : {"idCard":"测试内容9qo4","imgUrl":"测试内容9j5g","name":"测试内容w8x1","status":43665}
     */


    private String idCard;
    private String imgUrl;
    private String name;
    private int status;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
