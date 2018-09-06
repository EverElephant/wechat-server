package com.kodgames.wechattools.model;

import java.util.Date;
import javax.persistence.*;

public class Balance {
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 总计收入
     */
    private Double income;

    /**
     * 微信openid
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 微信unionid
     */
    @Column(name = "union_id")
    private String unionId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取id
     *
     * @return id - id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取总计收入
     *
     * @return income - 总计收入
     */
    public Double getIncome() {
        return income;
    }

    /**
     * 设置总计收入
     *
     * @param income 总计收入
     */
    public void setIncome(Double income) {
        this.income = income;
    }

    /**
     * 获取微信openid
     *
     * @return open_id - 微信openid
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置微信openid
     *
     * @param openId 微信openid
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取微信unionid
     *
     * @return union_id - 微信unionid
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * 设置微信unionid
     *
     * @param unionId 微信unionid
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}