package com.kodgames.wechattools.service.impl;

import com.kodgames.wechattools.constant.BalanceOperateTypes;
import com.kodgames.wechattools.constant.RedPacketStatus;
import com.kodgames.wechattools.core.AbstractService;
import com.kodgames.wechattools.dao.BalanceMapper;
import com.kodgames.wechattools.model.AreaBalance;
import com.kodgames.wechattools.model.Balance;
import com.kodgames.wechattools.model.RedPacket;
import com.kodgames.wechattools.service.*;
import com.kodgames.wechattools.util.SerialNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


/**
 * Created by CodeGenerator on 2017/08/30.
 */
@Service
@Transactional
public class BalanceServiceImpl extends AbstractService<Balance> implements BalanceService {
    @Resource
    private BalanceMapper balanceMapper;
    @Autowired
    private BalanceOperateLogService balanceOperateLogService;
    @Autowired
    private AreaBalanceService areaBalanceService;
    @Autowired
    private RedPacketStatusLogService redPacketStatusLogService;
    @Autowired
    private RedPacketService redPacketService;
    @Autowired
    private BalanceCountService balanceCountService;


    /**
     * 红包发送中处理
     * @param redPacket
     * @param balance
     * @param ip
     */
    @Override
    public void sendingProcess(RedPacket redPacket, Balance balance, String ip) {
        AreaBalance areaBalance = areaBalanceService.findBy(redPacket.getBalanceId(), redPacket.getAreaId());
        if (areaBalance == null) {
            return;
        }

        int lastStatus = redPacket.getStatus();
        balanceCountService.increaseWithCount(balance.getId());
        //更新账户
        double beginMoney = areaBalance.getMoney();
        areaBalance.setMoney(beginMoney - redPacket.getRmb());
        if (areaBalance.getMoney() < 0) {
            areaBalance.setMoney(0.0);
        }
        areaBalance.setUpdateTime(new Date());
        areaBalanceService.update(areaBalance);
        //账户余额变更记录
        String serialNo = SerialNoUtil.generateByIp(ip);
        balanceOperateLogService.insertBalanceOperateLog(serialNo, balance.getId(), beginMoney, areaBalance.getMoney(), BalanceOperateTypes.WITHDRAW.getType(), balance.getUnionId(), balance.getOpenId(), redPacket.getAreaId(), null);
        //红包状态变更记录
        redPacket.setStatus(RedPacketStatus.SENDING.getStatus());
        redPacketService.update(redPacket);
        redPacketStatusLogService.insertRedPacketStatusLog(redPacket.getId(), lastStatus, redPacket.getStatus());
    }

    /**
     * 红包发送失败处理
     *
     * @param redPacket
     */
    @Override
    public void sendFailedProcess(RedPacket redPacket) {
        int lastStatus = redPacket.getStatus();
        //更新状态
        redPacket.setStatus(RedPacketStatus.FAILED.getStatus());
        redPacketService.update(redPacket);
        //添加红包状态变更记录
        redPacketStatusLogService.insertRedPacketStatusLog(redPacket.getId(), lastStatus, redPacket.getStatus());
    }

    /**
     * 成功发送红包处理
     * @param redPacket
     * @param balance
     * @param ip
     */
    @Override
    public void sentProcess(RedPacket redPacket, Balance balance, String ip) {
        AreaBalance areaBalance = areaBalanceService.findBy(redPacket.getBalanceId(), redPacket.getAreaId());
        if (areaBalance == null) {
            return;
        }
        balanceCountService.increaseWithCount(balance.getId());
        int lastStatus = redPacket.getStatus();
        //更新账户
        areaBalance.setUpdateTime(new Date());
        double beginMoney = areaBalance.getMoney();
        areaBalance.setMoney(beginMoney - redPacket.getRmb());
        if (areaBalance.getMoney() < 0) {
            areaBalance.setMoney(0.0);
        }
        areaBalanceService.update(areaBalance);

        //账户余额变更记录
        String serialNo = SerialNoUtil.generateByIp(ip);
        balanceOperateLogService.insertBalanceOperateLog(serialNo, balance.getId(), beginMoney, areaBalance.getMoney(), BalanceOperateTypes.WITHDRAW.getType(), balance.getUnionId(), balance.getOpenId(), redPacket.getAreaId(), null);
        //红包状态变更记录
        redPacket.setStatus(RedPacketStatus.SENT.getStatus());
        redPacketService.update(redPacket);
        redPacketStatusLogService.insertRedPacketStatusLog(redPacket.getId(), lastStatus, redPacket.getStatus());
    }
}
