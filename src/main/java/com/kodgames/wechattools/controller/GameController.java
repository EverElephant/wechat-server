package com.kodgames.wechattools.controller;

import com.alibaba.fastjson.JSONObject;
import com.kodgames.wechattools.constant.BalanceOperateTypes;
import com.kodgames.wechattools.core.Result;
import com.kodgames.wechattools.core.ResultCodeMessage;
import com.kodgames.wechattools.model.AreaBalance;
import com.kodgames.wechattools.model.Balance;
import com.kodgames.wechattools.reqres.req.game.BalanceChargeReq;
import com.kodgames.wechattools.service.AreaBalanceService;
import com.kodgames.wechattools.service.BalanceOperateLogService;
import com.kodgames.wechattools.service.BalanceService;
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

/**
 * 统一接受game的请求
 */
@RestController
@RequestMapping(value = "/withdraw/game")
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private AreaBalanceService areaBalanceService;
    @Autowired
    private BalanceOperateLogService balanceOperateLogService;

    @ApiOperation(value = "game给用户充值")
    @PostMapping(value = "/balance/recharge")
    public Result<Void> operate(@RequestBody @Valid BalanceChargeReq req) {
        logger.info("game operate balance req:{}", JSONObject.toJSONString(req));
        return updateBalance(req);
    }

    public synchronized Result<Void> updateBalance(BalanceChargeReq req) {
        Balance balance = balanceService.findBy("unionId", req.getUnionId());
        if (balance == null) {      //创建主账户
            balance = new Balance();
            balance.setUnionId(req.getUnionId());
            balance.setCreateTime(new Date());
            balance.setIncome(0.0);
            balanceService.save(balance);
        }

        //更新主账户总金额
        double income = balance.getIncome();
        balance.setIncome(income + req.getRmb());
        balanceService.update(balance);

        AreaBalance areaBalance = areaBalanceService.findBy(balance.getId(), Integer.parseInt(req.getAppId()));
        Date now = new Date();
        if (areaBalance == null) {
            areaBalance = new AreaBalance();
            areaBalance.setAreaId(Integer.parseInt(req.getAppId()));
            areaBalance.setBalanceId(balance.getId());
            areaBalance.setCreateTime(now);
            areaBalance.setMoney(0.0);
            areaBalanceService.save(areaBalance);
        }

        areaBalance.setUpdateTime(now);
        double beginMoney = areaBalance.getMoney();
        areaBalance.setMoney(beginMoney + req.getRmb());
        areaBalanceService.update(areaBalance);
        balanceOperateLogService.insertBalanceOperateLog(req.getSerialNo(), balance.getId(), beginMoney, areaBalance.getMoney(), BalanceOperateTypes.RECHARGE.getType(), req.getUnionId(), req.getOpenId(), Integer.parseInt(req.getAppId()), req.getAccountId());
        logger.info("game operate balance res:{}", "success");
        return new Result(ResultCodeMessage.SUCCESS);
    }
}
