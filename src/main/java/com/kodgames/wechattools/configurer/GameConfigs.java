package com.kodgames.wechattools.configurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Created by Ouyangyuying on 2017/11/14.
 */
@Component("gameConfig")
@RefreshScope
public class GameConfigs{
    private ConcurrentHashMap<String, GameConfig> configMap = new ConcurrentHashMap<>();

    public GameConfigs() {
        Config config= ConfigService.getConfig("GameConfig");
        Set<String> areaIds=config.getPropertyNames();
        for(String areaId:areaIds){
            String jsonStr = config.getProperty(areaId,"");
            GameConfig gameConfig = JSON.parseObject(jsonStr, new TypeReference<GameConfig>() {});
            configMap.put(gameConfig.getAreaId(), gameConfig);
        }
    }


    public Map<String, GameConfig> getConfigMap() {
        return configMap;
    }


    public GameConfig getConfigByAreaId(String areaId) {
        return configMap.get(areaId);
    }

    public String getBundleIdByAreaId(String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            return null;
        }

        GameConfig config = getConfigByAreaId(areaId);
        if (config == null) {
            return null;
        }

        return config.getBundleId();
    }
}
