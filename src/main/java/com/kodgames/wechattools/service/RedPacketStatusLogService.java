package com.kodgames.wechattools.service;

import com.kodgames.wechattools.core.Service;
import com.kodgames.wechattools.model.RedPacketStatusLog;

import java.util.List;


/**
 * Created by CodeGenerator on 2017/08/29.
 */
public interface RedPacketStatusLogService extends Service<RedPacketStatusLog> {
    void insertRedPacketStatusLog(Long redPacketId, int initStatus, int updateStatus);

    List<RedPacketStatusLog> findAllByRedPacketId(Long redPacketRecordId);
}