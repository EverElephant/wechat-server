package com.kodgames.wechattools.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "black_balance")
public class BlackBalance {
    /**
     * 主键自增
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
     * 拉黑时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 获取主键自增
     *
     * @return id - 主键自增
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键自增
     *
     * @param id 主键自增
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
     * 获取拉黑时间
     *
     * @return create_time - 拉黑时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置拉黑时间
     *
     * @param createTime 拉黑时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}