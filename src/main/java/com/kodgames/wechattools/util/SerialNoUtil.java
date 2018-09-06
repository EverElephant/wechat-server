package com.kodgames.wechattools.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 流水号生成工具
 */
public class SerialNoUtil {
    /**
     * 通过ip生成流水号
     * 示例ip:192.168.0.10,当前时间:2017年8月31日15点23分12秒367毫秒,随机数 189
     * 流水号则是:192168000010 20170831152312367 189.(空格没有，只是方便阅读)
     *
     * @param ip
     * @return
     */
    public static String generateByIp(String ip) {
        StringBuilder builder = new StringBuilder();
        String[] ipArr = ip.split("\\.");
        for (String a : ipArr) {
            if (a.length() == 1) {
                builder.append("00" + a);
            } else if (a.length() == 2) {
                builder.append("0" + a);
            } else if (a.length() == 3) {
                builder.append(a);
            } else {
                builder.append("000");
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        builder.append(formatter.format(new Date()));
        int random = (int) (Math.random() * 1000);
        String r = new DecimalFormat(("000")).format(random);
        builder.append(r);
        return builder.toString();
    }
}
