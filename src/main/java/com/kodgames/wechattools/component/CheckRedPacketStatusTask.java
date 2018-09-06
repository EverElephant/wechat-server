package com.kodgames.wechattools.component;

import com.kodgames.wechattools.model.RedPacket;
import com.kodgames.wechattools.service.BalanceOperateLogService;
import com.kodgames.wechattools.service.BalanceService;
import com.kodgames.wechattools.service.RedPacketService;
import com.kodgames.wechattools.service.RedPacketStatusLogService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CheckRedPacketStatusTask {
    private static final Logger logger = LoggerFactory.getLogger(CheckRedPacketStatusTask.class);
    @Autowired
    RedPacketService redPacketService;
    @Autowired
    BalanceService balanceService;
    @Autowired
    BalanceOperateLogService balanceOperateLogService;
    @Autowired
    RedPacketStatusLogService redPacketStatusLogService;

    @Scheduled(fixedRate = 1000 * 60 * 20)
    public void checkRedPacketStatus() {
        logger.info("-------------check red packet status, time:{}---------------", new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        List<RedPacket> redPacketList = redPacketService.findUnfinishedRedPackets();
        for (RedPacket redPacket : redPacketList) {
            try {
                redPacketService.checkRedPacketStatus(redPacket);
            } catch (IOException e) {
                logger.error("check red packet occurs exception:{}", e);
            }
        }
    }
}
