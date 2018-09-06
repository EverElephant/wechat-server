package com.kodgames.wechattools.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "balance_count")
public class BalanceCount {
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
     * 提现日期
     */
    @Column(name = "balance_date")
    private Date balanceDate;

    /**
     * 提现次数
     */
    @Column(name = "balance_count")
    private Integer balanceCount;

    /**
     * 更新时间
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
     * 获取提现日期
     *
     * @return balance_date - 提现日期
     */
    public Date getBalanceDate() {
        return balanceDate;
    }

    /**
     * 设置提现日期
     *
     * @param balanceDate 提现日期
     */
    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    /**
     * 获取提现次数
     *
     * @return balance_count - 提现次数
     */
    public Integer getBalanceCount() {
        return balanceCount;
    }

    /**
     * 设置提现次数
     *
     * @param balanceCount 提现次数
     */
    public void setBalanceCount(Integer balanceCount) {
        this.balanceCount = balanceCount;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}