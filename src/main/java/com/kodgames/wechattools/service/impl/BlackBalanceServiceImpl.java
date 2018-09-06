package com.kodgames.wechattools.service.impl;

import com.kodgames.wechattools.dao.BlackBalanceMapper;
import com.kodgames.wechattools.model.BlackBalance;
import com.kodgames.wechattools.service.BlackBalanceService;
import com.kodgames.wechattools.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Created by CodeGenerator on 2017/11/22.
*/
@Service
@Transactional
public class BlackBalanceServiceImpl extends AbstractService<BlackBalance> implements BlackBalanceService {
@Resource
private BlackBalanceMapper blackBalanceMapper;

}
