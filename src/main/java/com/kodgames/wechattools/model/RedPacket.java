package com.kodgames.wechattools.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "red_packet")
public class RedPacket {
    /**
     * id
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
     * billing订单id
     */
    @Column(name = "billing_order_id")
    private String billingOrderId;

    /**
     * 第三方订单id
     */
    @Column(name = "channel_order_id")
    private String channelOrderId;

    /**
     * 提现金额
     */
    private Double rmb;

    /**
     * 订单状态，1:正在发放,2:已发待领,3:发放失败,4红包领取,5:正在退款,6:退款成功
     */
    private Integer status;

    /**
     * 说明
     */
    private String note;

    /**
     * 订单创建时间
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
     * 获取billing订单id
     *
     * @return billing_order_id - billing订单id
     */
    public String getBillingOrderId() {
        return billingOrderId;
    }

    /**
     * 设置billing订单id
     *
     * @param billingOrderId billing订单id
     */
    public void setBillingOrderId(String billingOrderId) {
        this.billingOrderId = billingOrderId;
    }

    /**
     * 获取第三方订单id
     *
     * @return channel_order_id - 第三方订单id
     */
    public String getChannelOrderId() {
        return channelOrderId;
    }

    /**
     * 设置第三方订单id
     *
     * @param channelOrderId 第三方订单id
     */
    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    /**
     * 获取提现金额
     *
     * @return rmb - 提现金额
     */
    public Double getRmb() {
        return rmb;
    }

    /**
     * 设置提现金额
     *
     * @param rmb 提现金额
     */
    public void setRmb(Double rmb) {
        this.rmb = rmb;
    }

    /**
     * 获取订单状态，1:正在发放,2:已发待领,3:发放失败,4红包领取,5:正在退款,6:退款成功
     *
     * @return status - 订单状态，1:正在发放,2:已发待领,3:发放失败,4红包领取,5:正在退款,6:退款成功
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置订单状态，1:正在发放,2:已发待领,3:发放失败,4红包领取,5:正在退款,6:退款成功
     *
     * @param status 订单状态，1:正在发放,2:已发待领,3:发放失败,4红包领取,5:正在退款,6:退款成功
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取说明
     *
     * @return note - 说明
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置说明
     *
     * @param note 说明
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 获取订单创建时间
     *
     * @return create_time - 订单创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置订单创建时间
     *
     * @param createTime 订单创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}