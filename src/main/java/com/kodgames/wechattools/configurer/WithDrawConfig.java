package com.kodgames.wechattools.configurer;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "withdraw")
@Component("withDrawConfig")
@EnableApolloConfig(value ="WithDrawConfig")
@RefreshScope
public class WithDrawConfig{
    private String orderUrl;
    private String withdrawUrl;
    private String withdrawCheckUrl;
    private String wxAppId;
    private String wxSecret;
    private String bundleId;
    private int serverType;
    private String actName;
    private String sendName;
    private String wishing;


    public String getOrderUrl() {
        return orderUrl;
    }


    public String getWithdrawUrl() {
        return withdrawUrl;
    }


    public String getWithdrawCheckUrl() {
        return withdrawCheckUrl;
    }


    public String getWxAppId() {
        return wxAppId;
    }


    public String getWxSecret() {
        return wxSecret;
    }


    public String getBundleId() {
        return bundleId;
    }


    public int getServerType() {
        return serverType;
    }


    public String getActName() {
        return actName;
    }


    public String getSendName() {
        return sendName;
    }


    public String getWishing() {
        return wishing;
    }

    public void setOrderUrl(String orderUrl)
    {
        this.orderUrl = orderUrl;
    }

    public void setWithdrawUrl(String withdrawUrl)
    {
        this.withdrawUrl = withdrawUrl;
    }

    public void setWithdrawCheckUrl(String withdrawCheckUrl)
    {
        this.withdrawCheckUrl = withdrawCheckUrl;
    }

    public void setWxAppId(String wxAppId)
    {
        this.wxAppId = wxAppId;
    }

    public void setWxSecret(String wxSecret)
    {
        this.wxSecret = wxSecret;
    }

    public void setBundleId(String bundleId)
    {
        this.bundleId = bundleId;
    }

    public void setServerType(int serverType)
    {
        this.serverType = serverType;
    }

    public void setActName(String actName)
    {
        this.actName = actName;
    }

    public void setSendName(String sendName)
    {
        this.sendName = sendName;
    }

    public void setWishing(String wishing)
    {
        this.wishing = wishing;
    }
}
