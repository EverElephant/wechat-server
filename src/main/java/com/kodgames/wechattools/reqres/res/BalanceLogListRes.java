package com.kodgames.wechattools.reqres.res;

import com.github.pagehelper.PageInfo;
import com.kodgames.wechattools.model.BalanceOperateLog;

public class BalanceLogListRes {
    private double income;
    private PageInfo<BalanceOperateLog> table;

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public PageInfo<BalanceOperateLog> getTable() {
        return table;
    }

    public void setTable(PageInfo<BalanceOperateLog> table) {
        this.table = table;
    }
}
