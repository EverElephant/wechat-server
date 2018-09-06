package com.kodgames.wechattools.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kodgames.wechattools.component.WithDrawReqProcessor;
import com.kodgames.wechattools.configurer.WithDrawConfig;
import com.kodgames.wechattools.constant.RedPacketStatus;
import com.kodgames.wechattools.core.Result;
import com.kodgames.wechattools.core.ResultCodeMessage;
import com.kodgames.wechattools.model.*;
import com.kodgames.wechattools.reqres.req.balance.LogListReq;
import com.kodgames.wechattools.reqres.req.balance.WithdrawReq;
import com.kodgames.wechattools.reqres.res.BalanceLogListRes;
import com.kodgames.wechattools.reqres.res.CheckBalanceRes;
import com.kodgames.wechattools.reqres.res.LastRedPacketStatusRes;
import com.kodgames.wechattools.service.*;
import com.kodgames.wechattools.util.IpUtil;
import com.kodgames.wechattools.util.WeiXinUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/withdraw/balance")
public class BalanceController {
    public static final int WITHDRAW_MONEY_LIMIT = 200;    //单次提现钱数
    public static final int WITHDRAW_COUNT_LIMIT = 5;    //一天提现次数
    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);
    @Autowired
    WithDrawConfig withDrawConfig;
    @Autowired
    BalanceService balanceService;
    @Autowired
    AreaBalanceService areaBalanceService;
    @Autowired
    RedPacketService redPacketService;
    @Autowired
    BalanceOperateLogService balanceOperateLogService;
    @Autowired
    RedPacketStatusLogService redPacketStatusLogService;
    @Autowired
    BalanceCountService balanceCountService;
    @Autowired
    WithDrawReqProcessor withDrawReqProcessor;

    //微信授权回调地址
    @GetMapping(value = "/auth")
    public Result auth(String code, String state, HttpSession session, HttpServletResponse response) {
        logger.info("wx auth req,code:{},state:{}", code, state);
        if (session.getAttribute("balanceId") == null) {
            //获取token
            JSONObject accessTokenJson = WeiXinUtil.getAccessToken(code, withDrawConfig.getWxAppId(), withDrawConfig.getWxSecret());
            if (accessTokenJson == null) {
                return new Result(ResultCodeMessage.FAIL);
            }
            //获取微信用户信息
            JSONObject userInfoJson = WeiXinUtil.getUserInfo(accessTokenJson.getString("access_token"), accessTokenJson.getString("openid"));
            if (userInfoJson == null) {
                return new Result(ResultCodeMessage.FAIL);
            }

            String unionid = userInfoJson.getString("unionid");
            if (unionid == null) {
                logger.warn("wx get unionId failed");
                return new Result(ResultCodeMessage.FAIL);
            }
            String openid = userInfoJson.getString("openid");
            Balance balance = balanceService.findBy("unionId", unionid);
            if (balance == null) {          //创建用户
                balance = new Balance();
                balance.setOpenId(openid);
                balance.setUnionId(unionid);
                balance.setCreateTime(new Date());
                balance.setIncome(0.0);
                balanceService.save(balance);
            }

            if (StringUtils.isEmpty(balance.getOpenId())) {
                balance.setOpenId(openid);
                balanceService.update(balance);
            }
            logger.info("balanceId:{}, unionId:{}", balance.getId(), unionid);
            session.setAttribute("balanceId", balance.getId());
            withDrawReqProcessor.setInitTime(balance.getId());
        }

        try {
            response.sendRedirect(state);
            return new Result(ResultCodeMessage.SUCCESS);
        } catch (IOException e) {
            logger.error("redirect error:{}", e);
            return new Result(ResultCodeMessage.FAIL);
        }
    }

    /**
     * 查询账户
     *
     * @return
     */
    @ApiOperation(value = "查询账户")
    @GetMapping(path = "/info")
    public Result<CheckBalanceRes> checkBalance(HttpSession session) {
        logger.info("check balance info req");
        Long balanceId = (Long) session.getAttribute("balanceId");
        if (balanceId == null) {
            logger.warn("user not auth");
            return new Result(ResultCodeMessage.USER_NOT_AUTH);
        }

        int count = balanceCountService.getWithdrawCount(balanceId);
        CheckBalanceRes checkBalanceRes = new CheckBalanceRes();
        checkBalanceRes.setTodayCount(count);
        checkBalanceRes.setTotalCount(WITHDRAW_COUNT_LIMIT);
        checkBalanceRes.setMoneyLimit(WITHDRAW_MONEY_LIMIT);
        List<AreaBalance> areaBalances = areaBalanceService.findListBy("balanceId", balanceId);
        checkBalanceRes.setAreaBalances(areaBalances);
        logger.info("check balance info res data:{}", JSONObject.toJSONString(checkBalanceRes));
        return new Result(ResultCodeMessage.SUCCESS, checkBalanceRes);
    }


    /**
     * 提现
     *
     * @param session
     * @return
     */
    @ApiOperation(value = "提现操作")
    @PostMapping(path = "/withdraw")
    public Result<Void> withdraw(HttpSession session, double money, int areaId, HttpServletRequest request) {
        String ip = IpUtil.getIpAddress(request);
        Long balanceId = (Long) session.getAttribute("balanceId");
        if (balanceId == null) {
            logger.warn("user not auth");
            return new Result(ResultCodeMessage.USER_NOT_AUTH);
        }

        logger.info("withdraw req, balanceId:{}, money:{}", balanceId, money);
        if (money <= 0 || areaId <= 0) {
            return new Result<>(ResultCodeMessage.FAIL);
        }

        if (!withDrawReqProcessor.withdrawable(balanceId)) {
            logger.info("requests are too frequent, balanceId:{}", balanceId);
            return new Result<>(ResultCodeMessage.FAIL);
        }
        WithdrawReq withdrawReq = new WithdrawReq(money, balanceId, areaId, ip);
        withDrawReqProcessor.addReq(withdrawReq);
        return new Result<>(ResultCodeMessage.SUCCESS);
    }


    /**
     * 查询上个红包状态
     *
     * @param session
     * @return
     */
    @ApiOperation(value = "查询上个红包状态")
    @GetMapping(path = "/redpacket/last/status")
    public Result<LastRedPacketStatusRes> checkLastRedPacketStatus(HttpSession session) {
        logger.info("check last red packet status req");
        Long balanceId = (Long) session.getAttribute("balanceId");
        if (balanceId == null) {
            logger.warn("user not auth");
            return new Result(ResultCodeMessage.USER_NOT_AUTH);
        }

        Balance balance = balanceService.findById(balanceId);
        if (balance == null) {
            logger.warn("user:{} balance account not exist", balanceId);
            return new Result(ResultCodeMessage.USER_NOT_EXIST);
        }

        String billingOrderId = null;
        List<RedPacketStatusLog> redPacketStatusLogs = null;
        RedPacket redPacketRecord = redPacketService.findLastRecordByBalanceId(balance.getId());
        if (redPacketRecord != null) {
            billingOrderId = redPacketRecord.getBillingOrderId();
            int status = redPacketRecord.getStatus();
            if (!isFinishedStatus(status)) {
                try {
                    redPacketService.checkRedPacketStatus(redPacketRecord);
                } catch (Exception e) {
                    logger.error("check red packet status failed, e:{}", e);
                }
            }

            redPacketStatusLogs = redPacketStatusLogService.findAllByRedPacketId(redPacketRecord.getId());
        }

        LastRedPacketStatusRes redPacketStatusRes = new LastRedPacketStatusRes();
        redPacketStatusRes.setBillingOrderId(billingOrderId);
        redPacketStatusRes.setList(redPacketStatusLogs);
        logger.info("check last red packet status res:{}", JSONObject.toJSONString(redPacketStatusRes));
        return new Result(ResultCodeMessage.SUCCESS, redPacketStatusRes);
    }

    /**
     * 查询账单
     *
     * @return
     */
    @ApiOperation(value = "账单查询")
    @GetMapping(path = "/log/list")
    public Result<BalanceLogListRes> getBalanceOperateLog(@Valid LogListReq logListReq, HttpSession session) {
        logger.info("user check balance operate log req:{}", JSONObject.toJSONString(logListReq));
        Long balanceId = (Long) session.getAttribute("balanceId");
        if (balanceId == null) {
            logger.warn("user not auth");
            return new Result(ResultCodeMessage.USER_NOT_AUTH);
        }

        Balance balance = balanceService.findById(balanceId);
        if (balance == null) {
            logger.warn("user:{} balance account not exist", balanceId);
            return new Result(ResultCodeMessage.USER_NOT_EXIST);
        }

        PageHelper.startPage(logListReq.getPagenum(), logListReq.getPagesize());
        List<BalanceOperateLog> list = balanceOperateLogService.findUserLogs(balanceId, logListReq.getDate());
        PageInfo pageInfo = new PageInfo(list);
        BalanceLogListRes res = new BalanceLogListRes();
        res.setIncome(balance.getIncome());
        res.setTable(pageInfo);
        logger.info("user check balance operate log res:{}", JSONObject.toJSONString(res));
        return new Result(ResultCodeMessage.SUCCESS, res);
    }


    //状态是否为结束状态
    public boolean isFinishedStatus(int status) {
        if (status == RedPacketStatus.RECEIVED.getStatus() || status == RedPacketStatus.REFUND.getStatus()
                || status == RedPacketStatus.FAILED.getStatus() || status == RedPacketStatus.CREATE.getStatus()) {
            return true;
        }
        return false;
    }
}
