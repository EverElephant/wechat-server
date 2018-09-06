package com.kodgames.wechattools.constant;

public class WechatConst {
    /* 微信服务器校验  signature **/
    public static final String SIGNACTURE = "myqah";

    /* 购买房卡 **/
    public static final int BUY_CARD = 1;

    /* 约麻 **/
    public static final int LAUNCH_PAIJU = 2;
    public static final int MY_PAIJU = 3;
    public static final int NEARBY_PAIJU = 4;

    /* 分享邀请 **/
    public static final int PRIVATE_LINK = 5;
    public static final String WX_APPID = "wx914d83a7a1871f67";
    public static final String WX_APPSECRET = "4f5253405c7fb149fd9217af619cfa60";
    public static final String DOWNLOAD_URL = "http://home.cs.nqcdmj.com/download-new/mahjong.html";
    /* 微信 接口**/
    /* 获取access_token 地址（GET） 限200（次/天）**/
    public static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /* 获取用户信息  **/
    public static String REQ_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    /* 获取网页授权  **/
    public static String OAUTH_TO_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    /* 菜单创建（POST） 限100（次/天）  **/
    public static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

}
