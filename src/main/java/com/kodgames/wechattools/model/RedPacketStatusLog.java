package com.kodgames.wechattools.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "red_packet_status_log")
public class RedPacketStatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "red_packet_id")
    private Long redPacketId;

    @Column(name = "init_status")
    private Integer initStatus;

    @Column(name = "update_status")
    private Integer updateStatus;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return red_packet_record_id
     */
    public Long getRedPacketId() {
        return redPacketId;
    }

    /**
     * @param redPacketId
     */
    public void setRedPacketId(Long redPacketId) {
        this.redPacketId = redPacketId;
    }

    /**
     * @return init_status
     */
    public Integer getInitStatus() {
        return initStatus;
    }

    /**
     * @param initStatus
     */
    public void setInitStatus(Integer initStatus) {
        this.initStatus = initStatus;
    }

    /**
     * @return update_status
     */
    public Integer getUpdateStatus() {
        return updateStatus;
    }

    /**
     * @param updateStatus
     */
    public void setUpdateStatus(Integer updateStatus) {
        this.updateStatus = updateStatus;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}