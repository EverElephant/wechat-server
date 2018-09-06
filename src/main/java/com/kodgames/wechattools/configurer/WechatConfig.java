package com.kodgames.wechattools.configurer;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Created by zhangbin on 2017/9/11.
 */
@ConfigurationProperties(prefix = "wechat")
@Component("wechatConfig")
@EnableApolloConfig(value ="WechatConfig")
@RefreshScope
public class WechatConfig{
    private String redirectUri;
    public String getRedirectUri() {
        return redirectUri;
    }
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
