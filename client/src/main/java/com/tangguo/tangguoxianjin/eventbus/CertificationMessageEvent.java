package com.tangguo.tangguoxianjin.eventbus;

/**
 * Created by lhy on 2017/4/18.
 */

public class CertificationMessageEvent extends BaseEvent {

    public final int type;

    public CertificationMessageEvent(int message) {
        this.type = message;
    }
}
