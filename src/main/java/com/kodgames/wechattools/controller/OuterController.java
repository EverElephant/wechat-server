package com.kodgames.wechattools.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kodgames.wechattools.constant.BalanceOperateTypes;
import com.kodgames.wechattools.core.Result;
import com.kodgames.wechattools.core.ResultCodeMessage;
import com.kodgames.wechattools.model.AreaBalance;
import com.kodgames.wechattools.model.Balance;
import com.kodgames.wechattools.model.BalanceOperateLog;
import com.kodgames.wechattools.model.RedPacket;
import com.kodgames.wechattools.reqres.req.out.BalanceOperateReq;
import com.kodgames.wechattools.reqres.req.out.CheckBalanceInfoReq;
import com.kodgames.wechattools.reqres.req.out.CheckBalanceLogReq;
import com.kodgames.wechattools.reqres.req.out.CheckRedPacketStatusReq;
import com.kodgames.wechattools.service.AreaBalanceService;
import com.kodgames.wechattools.service.BalanceOperateLogService;
import com.kodgames.wechattools.service.BalanceService;
import com.kodgames.wechattools.service.RedPacketService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

//对外的controller
@RestController
@RequestMapping(value = "/withdraw/gmt")
public class OuterController {
    private static final Logger logger = LoggerFactory.getLogger(OuterController.class);
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private AreaBalanceService areaBalanceService;
    @Autowired
    private BalanceOperateLogService balanceOperateLogService;
    @Autowired
    private RedPacketService redPacketService;

    @ApiOperation(value = "gmt查询用户信息")
    @PostMapping(value = "/balance/info")
    public Result<Balance> getBalanceInfo(@Valid @RequestBody CheckBalanceInfoReq req) {
        logger.info("gmt check balance info req:{}", JSONObject.toJSONString(req));
        Balance balance = balanceService.findBy("unionId", req.getUnionId());
        if (balance == null) {
            logger.warn("user:{} not exist", req.getUnionId());
            return new Result<>(ResultCodeMessage.USER_NOT_EXIST);
        }
        return new Result<>(ResultCodeMessage.SUCCESS, balance);
    }

    @ApiOperation(value = "gmt给用户充值或扣款")
    @PostMapping(value = "/balance/operate")
    public Result<Void> operate(@RequestBody @Valid BalanceOperateReq req) {
        logger.info("gmt operate balance req:{}", JSONObject.toJSONString(req));
        Balance balance = balanceService.findBy("unionId", req.getUnionId());
        if (balance == null) {
            logger.info("user not exist, unionId:{}", req.getUnionId());
            return new Result<>(ResultCodeMessage.USER_NOT_EXIST);
        }

        AreaBalance areaBalance = areaBalanceService.findBy(balance.getId(),Integer.parseInt(req.getAppId()));
        if (areaBalance == null) {
            logger.info("user:{} area:{} account not exist", balance.getId(), req.getAppId());
            return new Result<>(ResultCodeMessage.USER_NOT_EXIST);
        }

        double beginMoney = areaBalance.getMoney();
        double income = balance.getIncome();
        if (req.getOperateType() == BalanceOperateTypes.RECHARGE.getType()) {       //充值
            areaBalance.setUpdateTime(new Date());
            areaBalance.setMoney(beginMoney + req.getRmb());
            balance.setIncome(income + req.getRmb());
            balanceService.update(balance);
        } else {     //扣款
            if (beginMoney < req.getRmb()) {
                logger.warn("user does not have enough money to charge");
                return new Result<>(ResultCodeMessage.FAIL);
            }
            areaBalance.setUpdateTime(new Date());
            areaBalance.setMoney(beginMoney - req.getRmb());
        }
        areaBalanceService.update(areaBalance);
        balanceOperateLogService.insertBalanceOperateLog(req.getSerialNo(), balance.getId(), beginMoney, areaBalance.getMoney(), req.getOperateType(), req.getUnionId(), req.getOpenId(), Integer.parseInt(req.getAppId()), req.getAccountId());
        logger.info("gmt operate balance res:{}", "success");
        return new Result(ResultCodeMessage.SUCCESS);
    }

    @ApiOperation(value = "gmt 对用户进行账单查询")
    @PostMapping(value = "/balance/log/list")
    public Result<PageInfo<BalanceOperateLog>> checkBalanceLog(@Valid @RequestBody CheckBalanceLogReq req) {
        logger.info("gmt check balance log req:{}", JSONObject.toJSONString(req));
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<BalanceOperateLog> balanceOperateLogs = balanceOperateLogService.findAll(req.getUnionId(), req.getOpenId(), req.getAppId(), req.getAccountId(), req.getSerialNo(), req.getBegin(), req.getEnd());
        logger.info("gmt check balance log res:{}", JSONObject.toJSONString(balanceOperateLogs));
        return new Result(ResultCodeMessage.SUCCESS, new PageInfo<>(balanceOperateLogs));
    }

    @ApiOperation(value = "gmt查询红包状态列表")
    @PostMapping(value = "/redpacket/list")
    public Result<PageInfo<RedPacket>> checkRedPacketList(@Valid @RequestBody CheckRedPacketStatusReq req) {
        logger.info("gmt check red packet list req:{}", JSONObject.toJSONString(req));
        if (req.getBegin() == null && req.getEnd() == null && req.getUnionId() == null) {
            logger.warn("date and unionId can not be empty at the same time");
            return new Result(ResultCodeMessage.FAIL);
        }

        Balance balance = balanceService.findBy("unionId", req.getUnionId());
        if (balance == null) {
            logger.info("user not exist, unionId:{}", req.getUnionId());
            return new Result<>(ResultCodeMessage.USER_NOT_EXIST);
        }

        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<RedPacket> redPackets = redPacketService.findRedPackets(balance.getId(), req.getBegin(), req.getEnd(), req.getStatus());
        logger.info("gmt check red packet list res:{}", JSONObject.toJSONString(redPackets));
        return new Result(ResultCodeMessage.SUCCESS, new PageInfo<>(redPackets));
    }
}
