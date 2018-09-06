package com.kodgames.wechattools.service;

import com.kodgames.wechattools.core.Service;
import com.kodgames.wechattools.model.BalanceOperateLog;

import java.util.Date;
import java.util.List;


/**
 * Created by CodeGenerator on 2017/08/30.
 */
public interface BalanceOperateLogService extends Service<BalanceOperateLog> {
    void insertBalanceOperateLog(String serialNo, Long balanceId, double beginMoney, Double money, int type, String unionId, String openId, int areaId, String accountId);

    List<BalanceOperateLog> findAll(String unionId, String openId, String appId, String accountId, String serialNo, Date begin, Date end);

    List<BalanceOperateLog> findUserLogs(Long balanceId, Date operateTime);
}
