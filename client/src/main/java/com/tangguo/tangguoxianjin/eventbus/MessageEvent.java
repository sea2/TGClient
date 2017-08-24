package com.tangguo.tangguoxianjin.eventbus;

/**
 * Created by lhy on 2017/3/27.
 */

public class MessageEvent extends BaseEvent {

    public String message = "";
    public int type = 0;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public MessageEvent(int type) {
        this.type = type;
    }
}
