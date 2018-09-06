package com.kodgames.wechattools.model;

import javax.persistence.*;

public class Share {
    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分享人accountId
     */
    @Column(name = "account_id")
    private Long accountId;

    /**
     * 分享地址
     */
    @Column(name = "share_url")
    private String shareUrl;

    /**
     * 获取自增主键
     *
     * @return id - 自增主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增主键
     *
     * @param id 自增主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取分享人accountId
     *
     * @return account_id - 分享人accountId
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * 设置分享人accountId
     *
     * @param accountId 分享人accountId
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * 获取分享地址
     *
     * @return share_url - 分享地址
     */
    public String getShareUrl() {
        return shareUrl;
    }

    /**
     * 设置分享地址
     *
     * @param shareUrl 分享地址
     */
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}