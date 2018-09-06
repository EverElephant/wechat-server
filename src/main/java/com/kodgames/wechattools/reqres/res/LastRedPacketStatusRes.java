package com.kodgames.wechattools.reqres.res;

import com.kodgames.wechattools.model.RedPacketStatusLog;

import java.util.List;

public class LastRedPacketStatusRes {
    private String billingOrderId;
    private List<RedPacketStatusLog> list;

    public String getBillingOrderId() {
        return billingOrderId;
    }

    public void setBillingOrderId(String billingOrderId) {
        this.billingOrderId = billingOrderId;
    }

    public List<RedPacketStatusLog> getList() {
        return list;
    }

    public void setList(List<RedPacketStatusLog> list) {
        this.list = list;
    }
}
