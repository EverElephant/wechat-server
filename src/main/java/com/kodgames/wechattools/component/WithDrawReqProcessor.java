package com.kodgames.wechattools.component;

import com.alibaba.fastjson.JSONObject;
import com.kodgames.wechattools.configurer.GameConfigs;
import com.kodgames.wechattools.configurer.WithDrawConfig;
import com.kodgames.wechattools.constant.BillingCode;
import com.kodgames.wechattools.constant.RedPacketStatus;
import com.kodgames.wechattools.model.AreaBalance;
import com.kodgames.wechattools.model.Balance;
import com.kodgames.wechattools.model.BlackBalance;
import com.kodgames.wechattools.model.RedPacket;
import com.kodgames.wechattools.reqres.req.balance.WithdrawReq;
import com.kodgames.wechattools.service.*;
import com.kodgames.wechattools.util.HttpClient;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WithDrawReqProcessor {
    private ConcurrentHashMap<Long, Long> stimeMap = new ConcurrentHashMap<>();
//    private ConcurrentHashMap<Long, String> infoMap = new ConcurrentHashMap<>();

    private BlockingQueue<WithdrawReq> reqs = new LinkedBlockingDeque<>();
    @Autowired
    GameConfigs gameConfigs;
    @Autowired
    WithDrawConfig withDrawConfig;
    @Autowired
    BalanceService balanceService;
    @Autowired
    AreaBalanceService areaBalanceService;
    @Autowired
    RedPacketService redPacketService;
    @Autowired
    BalanceCountService balanceCountService;
    @Autowired
    BlackBalanceService blackBalanceService;

    public static final int WITHDRAW_MONEY_LIMIT = 200;    //单次提现钱数
    public static final int WITHDRAW_COUNT_LIMIT = 5;    //一天提现次数
    private static Logger logger = LoggerFactory.getLogger(WithDrawReqProcessor.class);

    public WithDrawReqProcessor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        WithdrawReq req = reqs.take();
                        process(req);
//                        infoMap.remove(req.getBalanceId());
                    } catch (Exception e) {
                        logger.error("push order failed exception:{}", e);
                    }
                }
            }
        }).start();
    }

    private void process(WithdrawReq req) {
        String ip = req.getIp();
        long balanceId = req.getBalanceId();
        logger.info("user:{} balance process start ... ", balanceId);
        double money = req.getMoney();
        Balance balance = balanceService.findById(balanceId);

        //如果帐号不存在，则提现失败
        if (balance == null) {
            logger.warn("user:{} balance account not exist", balanceId);
            return;
        }

        //如果上次红包没有发放完毕，则提现失败
        if (!lastRedPacketIsFinished(balance.getId())) {
            logger.warn("the last red packet of user:{} not finished", balance.getId());
            return;
        }
        //判断提现次数
        int count = balanceCountService.getWithdrawCount(balanceId);
        if (count >= WITHDRAW_COUNT_LIMIT) {
            logger.warn("user:{} withdraw count limit", balance.getId());
            return;
        }

        AreaBalance areaBalance = areaBalanceService.findBy(balanceId, req.getAreaId());
        if (areaBalance == null) {
            logger.warn("user:{} area:{} balance account not exist", balanceId, req.getAreaId());
            return;
        }

        //判断用户余额是否充足
        if (money > areaBalance.getMoney()) {
            logger.info("user:{}, area:{} account money not enough", balanceId, req.getAreaId());
            return;
        }

        //判断用户提现金额是否超过上限
        if (money > WITHDRAW_MONEY_LIMIT) {
            logger.warn("user:{} account withdraw money:{} illegal", balance.getId(), money);
            return;
        }

        RedPacket redPacket = new RedPacket();
        try {
            JSONObject body = new JSONObject();
            String kodId = gameConfigs.getBundleIdByAreaId(String.valueOf(req.getAreaId()));
            if (kodId == null) {
                logger.info("this area not support, areaId:{}", req.getAreaId());
                return;
            }
            body.put("kodId", kodId);
            body.put("serverType", withDrawConfig.getServerType());
            body.put("rechargeService", 3);
            body.put("playerId", balance.getId());
            body.put("payType", 4);
            body.put("osType", 0);
            body.put("rmb", money);
            body.put("itemId", 0);
            body.put("itemName", "提现");
            body.put("itemDetails", "");
            body.put("areaId", 0);
            body.put("deviceType", 0);
            body.put("channelId", 0);
            body.put("subChannelId", 0);
            JSONObject custom = new JSONObject();
            custom.put("openid", balance.getOpenId());
            custom.put("act_name", withDrawConfig.getActName());
            custom.put("send_name", withDrawConfig.getSendName());
            custom.put("wishing", withDrawConfig.getWishing());
            body.put("custom", custom);
            String res = HttpClient.doPost(withDrawConfig.getOrderUrl(), body.toJSONString(), HttpClient.FORM);
            if (res == null || res.isEmpty() || res.equals(BillingCode.ORDER_FAILED)) {     //下单失败
                logger.warn("red packet request order failed");
                return;
            }
            JSONObject json = JSONObject.parseObject(res);
            redPacket.setBillingOrderId(json.getString("orderId"));
            redPacket.setCreateTime(new Date());
            redPacket.setAreaId(req.getAreaId());
            redPacket.setStatus(RedPacketStatus.CREATE.getStatus());
            redPacket.setRmb(money);
            redPacket.setBalanceId(balance.getId());
            redPacketService.save(redPacket);
        } catch (IOException e) {
            logger.error("recharge money create order occurs net exception:{}", e);
            return;
        }

        try {
            JSONObject request = new JSONObject();
            request.put("orderId", redPacket.getBillingOrderId());
            String res = HttpClient.doPost(withDrawConfig.getWithdrawUrl(), request.toJSONString(), HttpClient.FORM);
            if (res != null && !res.isEmpty()) {
                JSONObject obj = JSONObject.parseObject(res);
                Integer code = obj.getInteger("code");
                if (code == null || code.equals(BillingCode.SEND_FAILED)) {      //红包发送失败
                    logger.warn("send red packet failed, billing res:{}", res);
                    balanceService.sendFailedProcess(redPacket);
                    return;
                }

                if (code.equals(BillingCode.SEND_SUCCESS)) {     //红包发送成功
                    logger.info("send red packet to unionId:{} success", balance.getUnionId());
                    balanceService.sentProcess(redPacket, balance, ip);
                    return;
                }

                if (code.equals(BillingCode.SENDING)) {        //红包发送中
                    logger.warn("sending red packet, billing res:{}", res);
                    balanceService.sendingProcess(redPacket, balance, ip);
                    return;
                }
            }
        } catch (IOException e) {
            logger.error("recharge money occurs net exception:{}", e);
            balanceService.sendingProcess(redPacket, balance, ip);
            return;
        } catch (Exception e) {
            logger.error("recharge money occurs other exception:{}", e);
        }
        balanceService.sendFailedProcess(redPacket);
    }

    public void addReq(WithdrawReq req) {
        this.reqs.add(req);
    }


    public boolean lastRedPacketIsFinished(Long balanceId) {
        RedPacket redPacketRecord = redPacketService.findLastRecordByBalanceId(balanceId);
        if (redPacketRecord == null) {
            return true;
        }

        int status = redPacketRecord.getStatus();
        return isFinishedStatus(status);
    }

    //状态是否为结束状态
    public boolean isFinishedStatus(int status) {
        if (status == RedPacketStatus.RECEIVED.getStatus() || status == RedPacketStatus.REFUND.getStatus() || status == RedPacketStatus.FAILED.getStatus()) {
            return true;
        }
        return false;
    }

    /**
     * 当前用户是否可提现
     *
     * @param balanceId
     * @return
     */
    public boolean withdrawable(long balanceId) {
        //加入黑名单拦截
        if (blackBalanceService.findBy("balanceId", balanceId) != null) {
            return false;
        }

        Long now = new Long(System.currentTimeMillis());
        Long old = stimeMap.get(balanceId);

        //通过时间戳判断是否是前端恶意并发
        if (old != null && (now - old) < 5 * 1000) {
            BlackBalance blackBalance = blackBalanceService.findBy("balanceId", balanceId);
            //计入黑名单
            if (blackBalance == null) {
                blackBalance = new BlackBalance();
                blackBalance.setBalanceId(balanceId);
                blackBalance.setCreateTime(new Date());
                blackBalanceService.save(blackBalance);
            }
            return false;
        }
        //允许本次请求
        now = new Long(System.currentTimeMillis());
        stimeMap.put(balanceId, now);
        return true;
        //stimeMap.put(balanceId, now);
//        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//        String previus = infoMap.putIfAbsent(balanceId, uuid);
//        String current = infoMap.get(balanceId);
//        if (previus == null && current != null && current.equals(uuid)) {
//
//        }
//        return false;
    }

    public void setInitTime(Long balanceId) {
        stimeMap.put(balanceId, System.currentTimeMillis());
    }
}
