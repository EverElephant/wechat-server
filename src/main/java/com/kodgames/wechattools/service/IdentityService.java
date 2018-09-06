package com.kodgames.wechattools.service;

import com.alibaba.fastjson.JSONObject;
import com.kodgames.wechattools.configurer.KeyConfig;
import com.kodgames.wechattools.configurer.WechatConfig;
import com.kodgames.wechattools.configurer.WithDrawConfig;
import com.kodgames.wechattools.constant.StatusCode;
import com.kodgames.wechattools.util.RSACoder;
import com.kodgames.wechattools.util.WeiXinUtil;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangbin on 2017/9/11.
 */
@Service
public class IdentityService {
    @Autowired
    WithDrawConfig withDrawConfig;
    @Autowired
    WechatConfig wechatConfig;
    @Autowired
    KeyConfig keyConfig;
    private static Logger logger = LoggerFactory.getLogger(IdentityService.class);

    public static String getIp(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getRemoteAddr();
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = "0.0.0.0";
        return ip;
    }

    /**
     * 获取微信授权链接的Url,state为自定义参数
     *
     * @param areaUrl 回调到地区Agent服务器的地址
     * @param scope   微信信息类型，1为仅获取openid，2为获取unionid
     */
    public HashMap<String, Object> getWxAuthUrl(String areaUrl, int scope) {
        HashMap<String, Object> result = new HashMap<>();
        String scopeStr = "";
        if (scope == 1) {
            scopeStr = "snsapi_base";
        } else if (scope == 2) {
            scopeStr = "snsapi_userinfo";
        } else {
            result.put("result", StatusCode.FAIL.getValue());
            return result;
        }
        StringBuilder builder = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize");
        builder.append("?appid=" + withDrawConfig.getWxAppId())
                .append("&redirect_uri=" + wechatConfig.getRedirectUri())
                .append("&response_type=code")
                .append("&scope=" + scopeStr)
                .append("&state=" + areaUrl)
                .append("#wechat_redirect");
        logger.info("getWxAuthUrl :" + builder.toString());
        result.put("result", StatusCode.SUCCESS.getValue());
        result.put("data", builder.toString());
        return result;
    }

    /**
     * 微信认证，获取openid
     */
    public HashMap<String, Object> getOpenId(String code, String state, HttpServletResponse response) {
        HashMap<String, Object> result = new HashMap<>();
        JSONObject resObject = WeiXinUtil.getAccessToken(code, withDrawConfig.getWxAppId(), withDrawConfig.getWxSecret());
        if (resObject == null) {
            result.put("result", StatusCode.WX_OPEN_ID_ERROR.getValue());
            return result;
        } else {
            String openId = resObject.getString("openid");
            String accessToken = resObject.getString("access_token");
            String scope = resObject.getString("scope");
            logger.debug("openid:-> {}, access_token:-> {}, scope:-> {}", openId, accessToken, scope);
            logger.debug("state:-> {}", state);
            if (scope.equals("snsapi_base")) {
                try {
                    long stime = System.currentTimeMillis();
                    String data = openId + ":" + stime;
                    String sign = "";
                    try {
                        sign = RSACoder.sign(data.getBytes(), keyConfig.getPrivateKey());
                    } catch (Exception e) {
                        logger.error("getOpenId RSACoder sign Exception->{}", e);
                        result.put("result", StatusCode.FAIL.getValue());
                        return result;
                    }
                    response.sendRedirect("http://" + state + "/agentboot/wx/openid/login?openid=" + openId + "&stime=" + stime + "&sign=" + sign);
                } catch (IOException e) {
                    logger.error("getOpenId IOException e->{}", e);
                    result.put("result", StatusCode.FAIL.getValue());
                    return result;
                }
            } else if (scope.equals("snsapi_userinfo")) {
                JSONObject userInfo = WeiXinUtil.getUserInfo(accessToken, openId);
                if (userInfo == null) {
                    result.put("result", StatusCode.WX_UNION_ID_ERROR.getValue());
                    return result;
                }

                //TODO 若需要，将UnionId等用户信息发送给地区服务器
            } else {
                result.put("result", StatusCode.WX_OPEN_ID_ERROR.getValue());
                return result;
            }
        }
        result.put("result", StatusCode.SUCCESS.getValue());
        return result;
    }
}
