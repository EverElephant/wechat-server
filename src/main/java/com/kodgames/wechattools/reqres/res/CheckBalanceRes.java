package com.kodgames.wechattools.reqres.res;

import com.kodgames.wechattools.model.AreaBalance;
import io.swagger.annotations.ApiModel;

import java.util.List;

@ApiModel("账户信息")
public class CheckBalanceRes {
    private int todayCount;
    private int totalCount;
    private int moneyLimit;
    private List<AreaBalance> areaBalances;

    public int getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(int todayCount) {
        this.todayCount = todayCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getMoneyLimit() {
        return moneyLimit;
    }

    public void setMoneyLimit(int moneyLimit) {
        this.moneyLimit = moneyLimit;
    }

    public List<AreaBalance> getAreaBalances() {
        return areaBalances;
    }

    public void setAreaBalances(List<AreaBalance> areaBalances) {
        this.areaBalances = areaBalances;
    }
}
