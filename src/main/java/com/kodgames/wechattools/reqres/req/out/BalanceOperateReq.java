package com.kodgames.wechattools.reqres.req.out;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class BalanceOperateReq {
    @ApiModelProperty(required = true)
    @NotBlank(message = "不能为空")
    private String unionId;
    @ApiModelProperty(required = true)
    @NotBlank(message = "不能为空")
    private String openId;
    @ApiModelProperty(required = true)
    @NotBlank(message = "不能为空")
    private String appId;
    @ApiModelProperty(required = true)
    @NotBlank(message = "不能为空")
    private String accountId;
    @ApiModelProperty(required = true)
    @NotBlank(message = "不能为空")
    private String serialNo;
    @ApiModelProperty(required = true, notes = "范围值1-10000")
    @Range(min = 1, max = 10000, message = "必须大于1小于10000")
    private int rmb;
    @ApiModelProperty(required = true, notes = "1充值，2扣款")
    @Range(min = 1, max = 2)
    private int operateType;

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

    public int getOperateType() {
        return operateType;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }
}
