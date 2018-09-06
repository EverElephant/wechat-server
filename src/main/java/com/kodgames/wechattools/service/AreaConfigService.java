package com.kodgames.wechattools.service;

import com.kodgames.wechattools.core.Service;
import com.kodgames.wechattools.model.AreaConfig;

import java.util.HashMap;


/**
 * Created by CodeGenerator on 2017/09/25.
 */
public interface AreaConfigService extends Service<AreaConfig> {

    String sendGmtReq(HashMap<String, Object> params, AreaConfig areaConfig);
}
