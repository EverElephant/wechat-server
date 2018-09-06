package com.kodgames.wechattools.constant;

/**
 * Created by zhangbin on 2017/9/11.
 */
public enum StatusCode {
    FAIL(-1, "请求失败"),
    SUCCESS(1, "请求成功"),
    WX_OPEN_ID_ERROR(-160, "获取微信openId失败"),
    WX_UNION_ID_ERROR(-161, "获取微信unionId失败");

    int value;
    String description;

    StatusCode(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }
}
