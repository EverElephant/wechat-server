package com.kodgames.wechattools.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kodgames.wechattools.core.AbstractService;
import com.kodgames.wechattools.model.AreaConfig;
import com.kodgames.wechattools.service.AreaConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static com.kodgames.wechattools.util.HttpClient.doGet;


/**
 * Created by CodeGenerator on 2017/09/25.
 */
@Service
@Transactional
public class AreaConfigServiceImpl extends AbstractService<AreaConfig> implements AreaConfigService {
    private static final Logger logger = LoggerFactory.getLogger(AreaConfigServiceImpl.class);

    @Override
    public String sendGmtReq(HashMap<String, Object> params, AreaConfig areaConfig) {
        try {
            String url = new StringBuilder(areaConfig.getAddress() + "/gmtools?")
                    .append(URLEncoder.encode(JSONObject.toJSONString(params), StandardCharsets.UTF_8.name()))
                    .toString();
            return doGet(url);
        } catch (Exception e) {
            logger.error("send gmt req failed, e:{}", e);
            e.printStackTrace();
        }
        return null;
    }
}
