package com.kodgames.wechattools.reqres.req.out;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

public class CheckBalanceInfoReq {
    @ApiModelProperty(required = true)
    @NotBlank
    private String unionId;

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }
}
