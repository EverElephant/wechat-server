package com.kodgames.wechattools.core;

public enum ResultCodeMessage {
    //    SUCCESS(200,"成功"),
//    FAIL(400, "失败"),
    UNAUTHORIZED(401, "未认证"),
    NOT_FOUND(404, "接口不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    SUCCESS(0, "成功"),
    FAIL(10001, "失败"),
    USER_NOT_EXIST(10002, "用户不存在"),
    USER_NOT_AUTH(10003, "用户未授权"),
    USER_MONEY_NOT_ENOUGH(10004, "用户余额不足"),
    WITHDRAW_COUNT_LIMIT(10005, "已达到次数上限"),
    WITHDRAW_FAILED(10006, "提现失败"),
    //    WITHDRAW_SUCCESS(10007,"提现成功"),
    SEND_RED_PACKET_FAILED(10008, "红包发放失败"),
    SEND_RED_PACKET_SUCCESS(10009, "红包发放成功"),
    SEND_RED_PACKET_PROCESSING(10010, "红包正在发放"),
    LAST_RED_PACKET_NOT_FINISHED(10011, "上个红包流程还没结束"),
    WITHDRAW_MONER_LIMIT(10012, "提现金额超出限制"),
    //    WITHDRAW_PROCESSING(10006, "提现处理中"),
    UNKNOW_ERROR(20000, "未知错误");


    private int code;
    private String message;

    ResultCodeMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
