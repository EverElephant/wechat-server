package com.kodgames.wechattools.constant;

public enum RedPacketStatus {
    CREATE(0, "初始状态"),
    SENDING(1, "正在发放"),
    SENT(2, "已发待领"),
    FAILED(3, "发放失败"),
    RECEIVED(4, "红包领取"),
    REFUND_ING(5, "正在退款"),
    REFUND(6, "退款成功");


    private int status;
    private String message;

    RedPacketStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
