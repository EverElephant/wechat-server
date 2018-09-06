package com.kodgames.wechattools.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "balance_operate_log")
public class BalanceOperateLog {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作流水号
     */
    @Column(name = "serial_no")
    private String serialNo;

    /**
     * 账户id
     */
    @Column(name = "balance_id")
    private Long balanceId;

    /**
     * 1.充值 2.扣款 3.提现 4.补款
     */
    @Column(name = "operate_type")
    private Integer operateType;

    /**
     * 操作前余额
     */
    @Column(name = "begin_money")
    private Double beginMoney;

    /**
     * 操作后余额
     */
    @Column(name = "end_money")
    private Double endMoney;

    /**
     * 操作时间
     */
    @Column(name = "operate_time")
    private Date operateTime;

    /**
     * union_id
     */
    @Column(name = "union_id")
    private String unionId;

    /**
     * open_id
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 地区标识
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * 玩家id
     */
    @Column(name = "account_id")
    private String accountId;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取操作流水号
     *
     * @return serial_no - 操作流水号
     */
    public String getSerialNo() {
        return serialNo;
    }

    /**
     * 设置操作流水号
     *
     * @param serialNo 操作流水号
     */
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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
     * 获取1.充值 2.扣款 3.提现 4.补款
     *
     * @return operate_type - 1.充值 2.扣款 3.提现 4.补款
     */
    public Integer getOperateType() {
        return operateType;
    }

    /**
     * 设置1.充值 2.扣款 3.提现 4.补款
     *
     * @param operateType 1.充值 2.扣款 3.提现 4.补款
     */
    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    /**
     * 获取操作前余额
     *
     * @return begin_money - 操作前余额
     */
    public Double getBeginMoney() {
        return beginMoney;
    }

    /**
     * 设置操作前余额
     *
     * @param beginMoney 操作前余额
     */
    public void setBeginMoney(Double beginMoney) {
        this.beginMoney = beginMoney;
    }

    /**
     * 获取操作后余额
     *
     * @return end_money - 操作后余额
     */
    public Double getEndMoney() {
        return endMoney;
    }

    /**
     * 设置操作后余额
     *
     * @param endMoney 操作后余额
     */
    public void setEndMoney(Double endMoney) {
        this.endMoney = endMoney;
    }

    /**
     * 获取操作时间
     *
     * @return operate_time - 操作时间
     */
    public Date getOperateTime() {
        return operateTime;
    }

    /**
     * 设置操作时间
     *
     * @param operateTime 操作时间
     */
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    /**
     * 获取union_id
     *
     * @return union_id - union_id
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * 设置union_id
     *
     * @param unionId union_id
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    /**
     * 获取open_id
     *
     * @return open_id - open_id
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置open_id
     *
     * @param openId open_id
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取地区标识
     *
     * @return area_id - 地区标识
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * 设置地区标识
     *
     * @param areaId 地区标识
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取玩家id
     *
     * @return account_id - 玩家id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置玩家id
     *
     * @param accountId 玩家id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}