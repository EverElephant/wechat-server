package com.kodgames.wechattools.service;
import com.kodgames.wechattools.model.AreaBalance;
import com.kodgames.wechattools.core.Service;


/**
* Created by CodeGenerator on 2017/11/30.
*/
public interface AreaBalanceService extends Service<AreaBalance> {
    AreaBalance findBy(long balanceId, int areaId);
}
