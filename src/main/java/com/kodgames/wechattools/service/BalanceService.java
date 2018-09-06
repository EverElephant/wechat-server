package com.kodgames.wechattools.service;

import com.kodgames.wechattools.core.Service;
import com.kodgames.wechattools.model.Balance;
import com.kodgames.wechattools.model.RedPacket;


/**
 * Created by CodeGenerator on 2017/08/30.
 */
public interface BalanceService extends Service<Balance> {
    void sendingProcess(RedPacket redPacket, Balance balance, String ip);

    void sendFailedProcess(RedPacket redPacket);

    void sentProcess(RedPacket redPacket, Balance balance, String ip);
}
