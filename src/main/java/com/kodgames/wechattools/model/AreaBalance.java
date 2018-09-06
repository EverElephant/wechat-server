package com.kodgames.wechattools.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "area_balance")
public class AreaBalance {
    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 账户id
     */
    @Column(name = "balance_id")
    private Long balanceId;

    /**
     * 地区id
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * 账户余额
     */
    private Double money;

    /**
     * 账户创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 账户更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取账户id
     *
     * @return balance_id - 账户id
     */
    public Long getBalanceId() {
        return balanceId;
    }

    /**
     * 设置账户id
     *
     * @param balanceId 账户id
     */
    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }

    /**
     * 获取地区id
     *
     * @return area_id - 地区id
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * 设置地区id
     *
     * @param areaId 地区id
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取账户余额
     *
     * @return money - 账户余额
     */
    public Double getMoney() {
        return money;
    }

    /**
     * 设置账户余额
     *
     * @param money 账户余额
     */
    public void setMoney(Double money) {
        this.money = money;
    }

    /**
     * 获取账户创建时间
     *
     * @return create_time - 账户创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置账户创建时间
     *
     * @param createTime 账户创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取账户更新时间
     *
     * @return update_time - 账户更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置账户更新时间
     *
     * @param updateTime 账户更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}