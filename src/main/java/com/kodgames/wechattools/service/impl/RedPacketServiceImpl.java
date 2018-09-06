package com.kodgames.wechattools.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.kodgames.wechattools.configurer.WithDrawConfig;
import com.kodgames.wechattools.constant.BalanceOperateTypes;
import com.kodgames.wechattools.constant.BillingCode;
import com.kodgames.wechattools.constant.RedPacketStatus;
import com.kodgames.wechattools.core.AbstractService;
import com.kodgames.wechattools.dao.RedPacketMapper;
import com.kodgames.wechattools.model.AreaBalance;
import com.kodgames.wechattools.model.Balance;
import com.kodgames.wechattools.model.RedPacket;
import com.kodgames.wechattools.service.*;
import com.kodgames.wechattools.util.HttpClient;
import com.kodgames.wechattools.util.SerialNoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by CodeGenerator on 2017/08/30.
 */
@Service
@Transactional
public class RedPacketServiceImpl extends AbstractService<RedPacket> implements RedPacketService {
    public static HashMap<String, Integer> statusMapper = new HashMap<>();

    static {
        statusMapper.put("SENDING", RedPacketStatus.SENDING.getStatus());
        statusMapper.put("SENT", RedPacketStatus.SENT.getStatus());
        statusMapper.put("FAILED", RedPacketStatus.FAILED.getStatus());
        statusMapper.put("RECEIVED", RedPacketStatus.RECEIVED.getStatus());
        statusMapper.put("RFUND_ING", RedPacketStatus.REFUND_ING.getStatus());
        statusMapper.put("REFUND", RedPacketStatus.REFUND.getStatus());
    }

    @Resource
    private RedPacketMapper redPacketMapper;
    @Autowired
    WithDrawConfig withDrawConfig;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private AreaBalanceService areaBalanceService;
    @Autowired
    private RedPacketService redPacketService;
    @Autowired
    private BalanceOperateLogService balanceOperateLogService;
    @Autowired
    private RedPacketStatusLogService redPacketStatusLogService;

    @Override
    public RedPacket findLastRecordByBalanceId(Long balanceId) {
        PageHelper.startPage(1, 1);
        Condition condition = new Condition(RedPacket.class);
        condition.orderBy("id").desc();
        condition.createCriteria()
                .andEqualTo("balanceId", balanceId);
        List<RedPacket> redPackets = redPacketMapper.selectByCondition(condition);
        if (redPackets == null || redPackets.size() == 0) {
            return null;
        }
        return redPackets.get(0);
    }

    @Override
    public List<RedPacket> findUnfinishedRedPackets() {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(RedPacketStatus.FAILED.getStatus());
        statusList.add(RedPacketStatus.RECEIVED.getStatus());
        statusList.add(RedPacketStatus.REFUND.getStatus());
        statusList.add(RedPacketStatus.CREATE.getStatus());

        Condition condition = new Condition(RedPacket.class);
        condition.createCriteria().andNotIn("status", statusList);
        return redPacketMapper.selectByCondition(condition);
    }

    @Override
    public void checkRedPacketStatus(RedPacket redPacket) throws IOException {
        Balance balance = balanceService.findById(redPacket.getBalanceId());
        if (balance == null) {
            return;
        }

        AreaBalance areaBalance = areaBalanceService.findBy(redPacket.getBalanceId(), redPacket.getAreaId());
        if (areaBalance == null) {
            return;
        }

        int status = redPacket.getStatus();
        JSONObject req = new JSONObject();
        req.put("orderId", redPacket.getBillingOrderId());
        String res = HttpClient.doPost(withDrawConfig.getWithdrawCheckUrl(), req.toJSONString(), HttpClient.FORM);
        JSONObject resJson = JSONObject.parseObject(res);
        int code = resJson.getInteger("code");
        if (code == BillingCode.CHECK_FAILED) {       //查询失败
            return;
        }

        if (BillingCode.CHECK_SEND_FAILED == code && status != RedPacketStatus.FAILED.getStatus()) {       //查询到红包发放失败
            redPacket.setStatus(RedPacketStatus.FAILED.getStatus());
            redPacketService.update(redPacket);
            redPacketStatusLogService.insertRedPacketStatusLog(redPacket.getId(), status, redPacket.getStatus());
            //进行补款操作
            double beginMoney = areaBalance.getMoney();
            areaBalance.setMoney(beginMoney + redPacket.getRmb());
            areaBalance.setUpdateTime(new Date());
            areaBalanceService.update(areaBalance);
            String serialNo = SerialNoUtil.generateByIp("127.0.0.1");
            balanceOperateLogService.insertBalanceOperateLog(serialNo, balance.getId(), beginMoney, areaBalance.getMoney(), BalanceOperateTypes.REPLENISHMENT.getType(), balance.getUnionId(), balance.getOpenId(), redPacket.getAreaId(), null);
            return;
        }

        String st = resJson.getJSONObject("data").getString("status");
        if (!statusMapper.get(st).equals(status)) {
            //更新红包状态
            redPacket.setStatus(statusMapper.get(st));
            redPacket.setChannelOrderId(resJson.getJSONObject("data").getString("detail_id"));
            redPacketService.update(redPacket);
            //插入状态更改记录
            redPacketStatusLogService.insertRedPacketStatusLog(redPacket.getId(), status, statusMapper.get(st));
            if ((RedPacketStatus.SENDING.getStatus() == status && statusMapper.get(st).equals(RedPacketStatus.FAILED.getStatus())) ||       //从发送中状态变成失败状态
                    (RedPacketStatus.SENT.getStatus() == status && statusMapper.get(st).equals(RedPacketStatus.REFUND_ING.getStatus())) ||   //从已发代领状态变成退款中状态
                    (RedPacketStatus.SENT.getStatus() == status && statusMapper.get(st).equals(RedPacketStatus.REFUND.getStatus()))) {   //从已发代领状态变成已退款状态
                //进行补款
                double beginMoney = areaBalance.getMoney();
                areaBalance.setMoney(beginMoney + redPacket.getRmb());
                areaBalance.setUpdateTime(new Date());
                areaBalanceService.update(areaBalance);
                String serialNo = SerialNoUtil.generateByIp("127.0.0.1");
                balanceOperateLogService.insertBalanceOperateLog(serialNo, balance.getId(), beginMoney, areaBalance.getMoney(), BalanceOperateTypes.REPLENISHMENT.getType(), balance.getUnionId(), balance.getOpenId(), redPacket.getAreaId(), null);
            }
        } else {
            if (redPacket.getChannelOrderId() == null || redPacket.getChannelOrderId().isEmpty()) {
                redPacket.setChannelOrderId(resJson.getJSONObject("data").getString("detail_id"));
                redPacketService.update(redPacket);
            }
        }
    }


    @Override
    public List<RedPacket> findRedPackets(Long balanceId, Date begin, Date end, Integer status) {
        Condition condition = new Condition(RedPacket.class);
        condition.orderBy("id").desc();
        Condition.Criteria criteria = condition.createCriteria();
        if (balanceId != null) {
            criteria.andEqualTo("balanceId", balanceId);
        }
        if (begin != null) {
            criteria.andGreaterThanOrEqualTo("createTime", begin);
        }
        if (end != null) {
            criteria.andLessThanOrEqualTo("createTime", end);
        }
        if (status != null) {
            criteria.andEqualTo("status", status);
        }
        return redPacketMapper.selectByCondition(condition);
    }
}
