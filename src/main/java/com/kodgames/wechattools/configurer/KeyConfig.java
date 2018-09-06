package com.kodgames.wechattools.configurer;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Created by zhangbin on 2017/8/15.
 */
@ConfigurationProperties(prefix = "key")
@Component("keyConfig")
@EnableApolloConfig(value ="KeyConfig")
@RefreshScope
public class KeyConfig
{

    private static final Logger logger = LoggerFactory.getLogger(KeyConfig.class);
    private String privateKey;

    @PostConstruct
    private void initialize() {
        logger.info(
            "KeyConfig initialized - privateKey: {}",
            privateKey);
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }


    @Override
    public String toString() {
        return String.format(
            "[KeyConfig] privateKey: %s",
            privateKey);
    }
}
