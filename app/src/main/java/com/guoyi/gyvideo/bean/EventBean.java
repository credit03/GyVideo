package com.guoyi.gyvideo.bean;

import com.guoyi.gyvideo.MyApplication;
import com.guoyi.gyvideo.utils.rxbus2.RxBus;

/**
 * Created by Credit on 2017/3/22.
 */

public class EventBean {


    public EventBean(int eventFlag) {
        this.eventFlag = eventFlag;
    }

    /**
     * 事件标志
     */
    public int eventFlag = 0;

    /**
     * 事件操作
     */
    public boolean op = false;

    public Object data;

    public void sendRxBus() {
        MyApplication.logD("---RxBus 事件发送---" + eventFlag);
        RxBus.get().withKey(eventFlag).send(this);
    }
}
