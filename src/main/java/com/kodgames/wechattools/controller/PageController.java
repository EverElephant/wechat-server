package com.kodgames.wechattools.controller;

import com.kodgames.wechattools.core.Result;
import com.kodgames.wechattools.core.ResultCodeMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PageController {

    /**
     * 错误码返回
     *
     * @return
     */
    @GetMapping("/code")
    public Result<Object> code() {
        List list = new ArrayList();
        for (ResultCodeMessage code : ResultCodeMessage.values()) {
            Object[] arr = new Object[3];
            arr[0] = code.getCode();
            arr[1] = code.name();
            arr[2] = code.getMessage();
            list.add(arr);
        }
        return new Result<>(ResultCodeMessage.SUCCESS, list);
    }
}
