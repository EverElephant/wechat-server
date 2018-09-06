package com.kodgames.wechattools.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeiXinUtil {
    public static final Logger logger = LoggerFactory.getLogger(WeiXinUtil.class);

    /**
     * 通过code换取网页授权AccessToken
     *
     * @param code
     * @param appId
     * @param secret
     * @return
     */
    public static JSONObject getAccessToken(String code, String appId, String secret) {
        StringBuilder builder = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=" + appId)
                .append("&secret=" + secret)
                .append("&code=" + code)
                .append("&grant_type=authorization_code");
        try {
            String response = HttpClient.doGet(builder.toString());
            JSONObject json = JSONObject.parseObject(response);
            logger.debug("wx get acess token res:{}", json);
            Integer errcode = json.getInteger("errcode");
            if (errcode != null) {
                return null;
            }
            return json;
        } catch (Exception e) {
            logger.error("wx get access token failed, e:{}", e);
        }
        return null;
    }

    /**
     * 获取用户信息
     *
     * @param access_token
     * @param openid
     * @return
     */
    public static JSONObject getUserInfo(String access_token, String openid) {
        StringBuilder builder = new StringBuilder("https://api.weixin.qq.com/sns/userinfo")
                .append("?access_token=" + access_token)
                .append("&openid=" + openid)
                .append("&lang=zh_CN");
        try {
            String response = HttpClient.doGet(builder.toString());
            JSONObject json = JSONObject.parseObject(response);
            logger.debug("wx get userInfo res:{}", json);
            Integer errcode = json.getInteger("errcode");
            if (errcode != null) {
                return null;
            }
            return json;
        } catch (Exception e) {
            logger.error("wx get userInfo failed, e:{}", e);
        }
        return null;
    }
}
