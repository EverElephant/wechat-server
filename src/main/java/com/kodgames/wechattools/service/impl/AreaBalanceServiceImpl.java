package com.kodgames.wechattools.service.impl;

import com.kodgames.wechattools.dao.AreaBalanceMapper;
import com.kodgames.wechattools.model.AreaBalance;
import com.kodgames.wechattools.service.AreaBalanceService;
import com.kodgames.wechattools.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Created by CodeGenerator on 2017/11/30.
*/
@Service
@Transactional
public class AreaBalanceServiceImpl extends AbstractService<AreaBalance> implements AreaBalanceService {
    @Resource
    private AreaBalanceMapper areaBalanceMapper;

    @Override
    public AreaBalance findBy(long balanceId, int areaId) {
        AreaBalance areaBalance = new AreaBalance();
        areaBalance.setBalanceId(balanceId);
        areaBalance.setAreaId(areaId);
        return areaBalanceMapper.selectOne(areaBalance);
    }
}
