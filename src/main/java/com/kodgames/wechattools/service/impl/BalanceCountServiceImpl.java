package com.kodgames.wechattools.service.impl;

import com.github.pagehelper.PageHelper;
import com.kodgames.wechattools.core.AbstractService;
import com.kodgames.wechattools.dao.BalanceCountMapper;
import com.kodgames.wechattools.model.BalanceCount;
import com.kodgames.wechattools.model.RedPacket;
import com.kodgames.wechattools.service.BalanceCountService;
import com.kodgames.wechattools.util.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Created by CodeGenerator on 2017/11/22.
 */
@Service
@Transactional
public class BalanceCountServiceImpl extends AbstractService<BalanceCount> implements BalanceCountService {
    @Resource
    private BalanceCountMapper balanceCountMapper;

    @Override
    public int getWithdrawCount(Long balanceId) {
        Date today = DateUtil.getDateMorning(new Date());
        Condition condition = new Condition(BalanceCount.class);
        condition.createCriteria()
                .andEqualTo("balanceDate", today)
                .andEqualTo("balanceId", balanceId);
        List<BalanceCount> balanceCounts = balanceCountMapper.selectByCondition(condition);
        if (balanceCounts == null || balanceCounts.size() == 0) {
            return 0;
        }
        return balanceCounts.get(0).getBalanceCount();
    }

    @Override
    public void increaseWithCount(Long balanceId) {
        BalanceCount balanceCount;
        Date now = new Date();
        Date today = DateUtil.getDateMorning(now);
        Condition condition = new Condition(BalanceCount.class);
        condition.createCriteria()
                .andEqualTo("balanceDate", today)
                .andEqualTo("balanceId", balanceId);
        List<BalanceCount> balanceCounts = balanceCountMapper.selectByCondition(condition);
        if (balanceCounts == null || balanceCounts.size() == 0) {
            balanceCount = new BalanceCount();
            balanceCount.setBalanceCount(1);
            balanceCount.setUpdateTime(now);
            balanceCount.setBalanceDate(today);
            balanceCount.setBalanceId(balanceId);
            save(balanceCount);
        }else {
            balanceCount = balanceCounts.get(0);
            balanceCount.setBalanceCount(balanceCount.getBalanceCount() + 1);
            balanceCount.setUpdateTime(now);
            update(balanceCount);
        }


    }
}
