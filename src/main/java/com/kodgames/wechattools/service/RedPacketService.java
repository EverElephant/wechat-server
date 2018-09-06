package com.kodgames.wechattools.service;

import com.kodgames.wechattools.core.Service;
import com.kodgames.wechattools.model.RedPacket;

import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * Created by CodeGenerator on 2017/08/30.
 */
public interface RedPacketService extends Service<RedPacket> {
    RedPacket findLastRecordByBalanceId(Long balanceId);

    List<RedPacket> findUnfinishedRedPackets();

    void checkRedPacketStatus(RedPacket redPacket) throws IOException;

//    int getWithdrawCount(Long balanceId);

    List<RedPacket> findRedPackets(Long balanceId, Date begin, Date end, Integer status);
}
