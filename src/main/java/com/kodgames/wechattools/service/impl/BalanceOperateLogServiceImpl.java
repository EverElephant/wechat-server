package com.kodgames.wechattools.service.impl;

import com.kodgames.wechattools.constant.BalanceOperateTypes;
import com.kodgames.wechattools.core.AbstractService;
import com.kodgames.wechattools.dao.BalanceOperateLogMapper;
import com.kodgames.wechattools.model.BalanceOperateLog;
import com.kodgames.wechattools.service.BalanceOperateLogService;
import com.kodgames.wechattools.util.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Created by CodeGenerator on 2017/08/30.
 */
@Service
@Transactional
public class BalanceOperateLogServiceImpl extends AbstractService<BalanceOperateLog> implements BalanceOperateLogService {
    @Resource
    private BalanceOperateLogMapper balanceOperateLogMapper;

    @Override
    public void insertBalanceOperateLog(String serialNo, Long balanceId, double beginMoney, Double money, int type, String unionId, String openId, int areaId, String accountId) {
        BalanceOperateLog balanceOperateLog = new BalanceOperateLog();
        balanceOperateLog.setBalanceId(balanceId);
        balanceOperateLog.setBeginMoney(beginMoney);
        balanceOperateLog.setEndMoney(money);
        balanceOperateLog.setOperateTime(new Date());
        balanceOperateLog.setOperateType(type);
        balanceOperateLog.setSerialNo(serialNo);
        balanceOperateLog.setUnionId(unionId);
        balanceOperateLog.setOpenId(openId);
        balanceOperateLog.setAreaId(areaId);
        balanceOperateLog.setAccountId(accountId);
        save(balanceOperateLog);
    }

    @Override
    public List<BalanceOperateLog> findAll(String unionId, String openId, String appId, String accountId, String serialNo, Date begin, Date end) {
        Condition condition = new Condition(BalanceOperateLog.class);
        condition.orderBy("id").desc();
        Condition.Criteria criteria = condition.createCriteria();
        if (unionId != null) {
            criteria.andEqualTo("unionId", unionId);
        }
        if (openId != null) {
            criteria.andEqualTo("openId", openId);
        }
        if (appId != null) {
            criteria.andEqualTo("appId", appId);
        }
        if (accountId != null) {
            criteria.andEqualTo("accountId", accountId);
        }
        if (serialNo != null) {
            criteria.andEqualTo("serialNo", serialNo);
        }

        if (begin != null) {
            criteria.andGreaterThanOrEqualTo("operateTime", begin);
        }

        if (end != null) {
            criteria.andLessThan("operateTime", end);
        }

        return balanceOperateLogMapper.selectByCondition(condition);
    }

    @Override
    public List<BalanceOperateLog> findUserLogs(Long balanceId, Date operateTime) {
        Condition condition = new Condition(BalanceOperateLog.class);
        condition.orderBy("id").desc();
        Condition.Criteria criteria = condition.createCriteria();
        criteria.andNotEqualTo("operateType", BalanceOperateTypes.CHARGE.getType())
                .andEqualTo("balanceId", balanceId);
        if (operateTime != null) {
            criteria.andBetween("operateTime", DateUtil.getDateMorning(operateTime), DateUtil.getDateNight(operateTime));
        }

        return balanceOperateLogMapper.selectByCondition(condition);
    }
}
