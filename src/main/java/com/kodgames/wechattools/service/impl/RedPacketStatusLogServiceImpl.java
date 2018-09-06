package com.kodgames.wechattools.service.impl;

import com.kodgames.wechattools.core.AbstractService;
import com.kodgames.wechattools.dao.RedPacketStatusLogMapper;
import com.kodgames.wechattools.model.RedPacketStatusLog;
import com.kodgames.wechattools.service.RedPacketStatusLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Created by CodeGenerator on 2017/08/29.
 */
@Service
@Transactional
public class RedPacketStatusLogServiceImpl extends AbstractService<RedPacketStatusLog> implements RedPacketStatusLogService {
    @Resource
    private RedPacketStatusLogMapper redPacketStatusLogMapper;

    @Override
    public void insertRedPacketStatusLog(Long redPacketId, int initStatus, int updateStatus) {
        RedPacketStatusLog redPacketStatusLog = new RedPacketStatusLog();
        redPacketStatusLog.setInitStatus(initStatus);
        redPacketStatusLog.setUpdateStatus(updateStatus);
        redPacketStatusLog.setUpdateTime(new Date());
        redPacketStatusLog.setRedPacketId(redPacketId);
        save(redPacketStatusLog);
    }

    @Override
    public List<RedPacketStatusLog> findAllByRedPacketId(Long redPacketId) {
        Condition condition = new Condition(RedPacketStatusLog.class);
        condition.orderBy("id").desc();
        condition.createCriteria()
                .andEqualTo("redPacketId", redPacketId);
        return redPacketStatusLogMapper.selectByCondition(condition);
    }
}
