package com.kodgames.wechattools.service;
import com.kodgames.wechattools.model.BalanceCount;
import com.kodgames.wechattools.core.Service;


/**
* Created by CodeGenerator on 2017/11/22.
*/
public interface BalanceCountService extends Service<BalanceCount> {

    int getWithdrawCount(Long balanceId);

    void increaseWithCount(Long balanceId);
}
