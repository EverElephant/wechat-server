package com.kodgames.wechattools.reqres.req.out;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import java.util.Date;

public class CheckRedPacketStatusReq {
    @ApiModelProperty(notes = "查询所有不传值.0:初始状态,1:正在发放,2:已发待领,3:发放失败,4:红包领取,5:正在退款,6:退款成功")
    @Range(min = 0, max = 6)
    Integer status;
    @Min(0)
    int pageNum;
    @Min(0)
    int pageSize;
    @ApiModelProperty(notes = "与时间不能同时为空")
    private String unionId;
    private Date begin;
    private Date end;

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
