package com.kodgames.wechattools.constant;

public enum BalanceOperateTypes {
    RECHARGE(1, "充值"),
    CHARGE(2, "扣款"),
    WITHDRAW(3, "提现"),
    REPLENISHMENT(4, "补款");

    private int type;
    private String message;

    BalanceOperateTypes(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
