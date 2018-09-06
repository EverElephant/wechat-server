package com.kodgames.wechattools.controller;

import com.kodgames.wechattools.component.WithDrawReqProcessor;
import com.kodgames.wechattools.configurer.WithDrawConfig;
import com.kodgames.wechattools.core.Result;
import com.kodgames.wechattools.core.ResultCodeMessage;
import com.kodgames.wechattools.reqres.req.balance.WithdrawReq;
import com.kodgames.wechattools.service.BalanceOperateLogService;
import com.kodgames.wechattools.service.BalanceService;
import com.kodgames.wechattools.service.RedPacketService;
import com.kodgames.wechattools.service.RedPacketStatusLogService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
    @Autowired
    BalanceService balanceService;
    @Autowired
    RedPacketService redPacketService;
    @Autowired
    BalanceOperateLogService balanceOperateLogService;
    @Autowired
    RedPacketStatusLogService redPacketStatusLogService;
    @Autowired
    WithDrawReqProcessor withDrawReqProcessor;

    /**
     * 提现
     *
     * @param session
     * @return
     */
    @ApiOperation(value = "提现操作")
    @PostMapping(path = "/withdraw")
    public Result<Void> withdraw(HttpSession session, double money, Long balanceId, int areaId, HttpServletRequest request) {
        logger.info("withdraw req, money:{}", money);
        String ip = getIpAddress(request);
        if (balanceId == null) {
            logger.warn("user not auth");
            return new Result(ResultCodeMessage.USER_NOT_AUTH);
        }
        logger.info("withdraw req, balanceId:{}, money:{}", balanceId, money);
        if (!withDrawReqProcessor.withdrawable(balanceId)) {
            logger.info("request too ");
            return new Result<>(ResultCodeMessage.FAIL);
        }
        System.out.println("begin to withdraw");
        WithdrawReq withdrawReq = new WithdrawReq(money, balanceId, areaId, ip);
        withDrawReqProcessor.addReq(withdrawReq);
        return new Result<>(ResultCodeMessage.SUCCESS);
    }

    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果是多级代理，那么取第一个ip为客户端ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }

        return ip;
    }
}
