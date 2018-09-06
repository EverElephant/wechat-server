package com.kodgames.wechattools.refresh;

import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.kodgames.wechattools.configurer.GameConfigs;
import com.kodgames.wechattools.configurer.WechatConfig;
import com.kodgames.wechattools.configurer.WithDrawConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Created by Ouyangyuying on 2017/12/13.
 */
@Component
public class RefreshConfig {
    private static final Logger logger = LoggerFactory.getLogger(RefreshConfig.class);

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private WithDrawConfig withDrawConfig;

    @Autowired
    private GameConfigs gameConfig;

    @Autowired
    private RefreshScope refreshScope;

    @ApolloConfigChangeListener(value = "WechatConfig")
    public void onChangeWechatConfig(ConfigChangeEvent changeEvent) {
        logger.info("before refresh {}", JSONObject.toJSONString(wechatConfig));
        refreshScope.refresh("wechatConfig");
        logger.info("after refresh {}", JSONObject.toJSONString(wechatConfig));
    }

    @ApolloConfigChangeListener(value = "WithDrawConfig")
    public void onChangeWithDrawConfig(ConfigChangeEvent changeEvent) {
        logger.info("before refresh {}", JSONObject.toJSONString(withDrawConfig));
        refreshScope.refresh("withDrawConfig");
        logger.info("after refresh {}", JSONObject.toJSONString(withDrawConfig));
    }

    @ApolloConfigChangeListener(value = "GameConfig")
    public void onChangeGameConfig(ConfigChangeEvent changeEvent) {
        logger.info("before refresh {}", JSONObject.toJSONString(gameConfig));
        refreshScope.refresh("gameConfig");
        logger.info("after refresh {}", JSONObject.toJSONString(gameConfig));
    }
}
