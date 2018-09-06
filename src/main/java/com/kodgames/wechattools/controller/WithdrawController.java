package com.kodgames.wechattools.controller;

import com.alibaba.fastjson.JSONObject;
import com.kodgames.wechattools.configurer.GameConfig;
import com.kodgames.wechattools.configurer.GameConfigs;
import com.kodgames.wechattools.configurer.WithDrawConfig;
import com.kodgames.wechattools.core.Result;
import com.kodgames.wechattools.core.ResultCodeMessage;
import com.kodgames.wechattools.model.Share;
import com.kodgames.wechattools.model.ShareAccount;
import com.kodgames.wechattools.service.AreaConfigService;
import com.kodgames.wechattools.service.ShareAccountService;
import com.kodgames.wechattools.service.ShareService;
import com.kodgames.wechattools.util.WeiXinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

import static com.kodgames.wechattools.util.HttpClient.doGet;

/**
 * 老的wechattools 中的withdraw controller，与现有的提现系统进行区分
 */
@RequestMapping(value = "/wechattools")
@RestController
public class WithdrawController {
    private static Logger logger = LoggerFactory.getLogger(WithdrawController.class);
    @Autowired
    WithDrawConfig withDrawConfig;
    @Autowired
    GameConfigs gameConfigs;
    @Autowired
    AreaConfigService areaConfigService;
    @Autowired
    ShareService shareService;
    @Autowired
    ShareAccountService shareAccountService;

    @GetMapping(value = "share_activity.do")
    public Result shareActivity(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        logger.info("gmt shareActivity code:{}  state:{}", code, state);
        JSONObject userInfo = null;
        if (session.getAttribute("userInfo") == null) {
            logger.info("gmt shareActivity userInfo is null");
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
            session.setAttribute("userInfo", userInfoJson);
        }

        userInfo = (JSONObject) session.getAttribute("userInfo");
        String[] shareParam = state.split("\\*");
        HashMap<String, Object> params = new HashMap<>();
        String areaId = shareParam[0];
        if (areaId == null || areaId.isEmpty()) {
            logger.warn("areaId is null or empty, state:{}", state);
            return new Result(ResultCodeMessage.FAIL);
        }
        //        AreaConfig areaConfig = areaConfigService.findBy("areaId", areaId);
        GameConfig areaConfig = gameConfigs.getConfigByAreaId(areaId);
        if (areaConfig == null) {
            logger.warn("please check the config of area:{}", areaId);
            return new Result(ResultCodeMessage.FAIL);
        }
        params.put("promoter", shareParam[2]);
        params.put("invitee", userInfo.getString("unionid"));
        params.put("handler", "BindPromoter");
        if (!areaId.equals("guizhou")) {
            params.put("area", Integer.parseInt(areaId));     //添加area字段
        }
        params.put("server_id", areaConfig.getGameServerId());
        String res = null;
        try {
            String url = new StringBuilder(areaConfig.getAddress() + "/gmtools?").append(URLEncoder.encode(JSONObject.toJSONString(params), StandardCharsets.UTF_8.name())).toString();
            res = doGet(url);
        } catch (Exception e) {
            logger.error("send gmt req failed, e:{}", e);
        }
        //        return null;
        //        String res = areaConfigService.sendGmtReq(params, areaConfig);
        logger.info("send gmt request get res:{}", res);

        try {
            response.sendRedirect(areaConfig.getDownloadUrl());
            return new Result(ResultCodeMessage.SUCCESS);
        } catch (IOException e) {
            logger.error("redirect url:{} error:{}", areaConfig.getDownloadUrl(), e);
            return new Result(ResultCodeMessage.FAIL);
        }
    }


    @GetMapping(value = "ordinary_share.do")
    public Result ordinaryShare(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        logger.info("ordinary code:{}  state:{}", code, state);
        JSONObject userInfo = null;
        if (session.getAttribute("userInfo") == null) {
            logger.info("ordinary code userInfo is null");
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
            session.setAttribute("userInfo", userInfoJson);
        }

        userInfo = (JSONObject) session.getAttribute("userInfo");
        String[] shareParam = state.split("\\*");

        String uid = userInfo.getString("unionid");
        String link = shareParam[0];
        String accountId = shareParam[1];

        Share share = new Share();
        share.setShareUrl(link);
        share.setAccountId(Long.parseLong(accountId));
        shareService.save(share);

        ShareAccount shareAccount = new ShareAccount();
        shareAccount.setAccountId(Long.parseLong(accountId));
        shareAccount.setUid(uid);
        shareAccount.setTime(new Date());
        shareAccountService.update(shareAccount);
        try {
            response.sendRedirect(link);
            return new Result(ResultCodeMessage.SUCCESS);
        } catch (IOException e) {
            logger.error("redirect url:{} error:{}", link, e);
            return new Result(ResultCodeMessage.FAIL);
        }
    }
}
