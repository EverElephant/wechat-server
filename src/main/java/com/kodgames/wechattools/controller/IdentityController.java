package com.kodgames.wechattools.controller;

import com.kodgames.wechattools.service.IdentityService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created by zhangbin on 2017/9/11.
 */
@Api(value = "微信身份认证相关api")
@RestController
public class IdentityController {

    private static Logger logger = LoggerFactory.getLogger(IdentityController.class);

    @Autowired
    private IdentityService identityService;

    @ApiOperation(value = "获取微信授权链接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaurl", value = "地区回调的接口url", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "scope", value = "1为仅获取openid，2为获取unionid", required = true, dataType = "int", paramType = "query")
    })
    @ApiResponse(code = 200, message = "result：1请求成功；-1请求失败")
    @RequestMapping(value = "/wx_auth/login", method = RequestMethod.GET)
    public HashMap<String, Object> getWxAuthUrl(@RequestParam(value = "areaurl") String areaUrl, @RequestParam(value = "scope") int scope) {
        return identityService.getWxAuthUrl(areaUrl, scope);
    }

    /**
     * 微信认证
     */
    @ApiOperation(value = "获取openid,微信回调")
    @ApiResponse(code = 200, message = "result：1请求成功；-1请求失败; -160获取微信openId失败; -161获取微信unionId失败")
    @RequestMapping(value = "/wx/openid/login", method = RequestMethod.GET)
    public HashMap<String, Object> wxAuth(HttpServletRequest request, HttpServletResponse response) {
        String code = "";
        String state = "";
        try {
            code = request.getParameter("code");
            state = request.getParameter("state");
        } catch (Exception e) {
            logger.warn("wechat wxAuth callback parameter error e:->{}", e);
            return new HashMap<>();
        }
        return identityService.getOpenId(code, state, response);
    }
}
