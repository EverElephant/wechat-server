package com.kodgames.wechattools.reqres.req.game;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class BalanceChargeReq {
    @NotBlank(message = "不能为空")
    private String unionId;
    @NotBlank(message = "不能为空")
    private String openId;
    @NotBlank(message = "不能为空")
    private String appId;
    @NotBlank(message = "不能为空")
    private String accountId;
    @NotBlank(message = "不能为空")
    private String serialNo;
    @Range(min = 1, max = 10000, message = "必须大于1小于10000")
    private int rmb;

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public int getRmb() {
        return rmb;
    }

    public void setRmb(int rmb) {
        this.rmb = rmb;
    }
}
