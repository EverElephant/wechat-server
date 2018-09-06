package com.kodgames.wechattools.reqres.req.balance;

public class WithdrawReq {
    private double money;
    private long balanceId;
    private int areaId;
    private String ip;

    public WithdrawReq(double money, long balanceId, int areaId, String ip) {
        this.money = money;
        this.balanceId = balanceId;
        this.areaId = areaId;
        this.ip = ip;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(long balanceId) {
        this.balanceId = balanceId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
