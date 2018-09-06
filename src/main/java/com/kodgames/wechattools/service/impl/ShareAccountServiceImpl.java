package com.kodgames.wechattools.service.impl;

import com.kodgames.wechattools.dao.ShareAccountMapper;
import com.kodgames.wechattools.model.ShareAccount;
import com.kodgames.wechattools.service.ShareAccountService;
import com.kodgames.wechattools.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Created by CodeGenerator on 2017/12/01.
*/
@Service
@Transactional
public class ShareAccountServiceImpl extends AbstractService<ShareAccount> implements ShareAccountService {
@Resource
private ShareAccountMapper shareAccountMapper;

}
