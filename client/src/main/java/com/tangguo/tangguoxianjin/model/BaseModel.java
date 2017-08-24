package com.tangguo.tangguoxianjin.model;

import java.io.Serializable;

/**
 * Created by 171842474@qq.com on 2016/3/14.
 */
public abstract class BaseModel implements Serializable {

    /**
     * code : 200
     */
    private String code;
    private String message;
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
