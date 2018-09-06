package com.kodgames.wechattools.service.impl;

import com.kodgames.wechattools.dao.ShareMapper;
import com.kodgames.wechattools.model.Share;
import com.kodgames.wechattools.service.ShareService;
import com.kodgames.wechattools.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Created by CodeGenerator on 2017/12/01.
*/
@Service
@Transactional
public class ShareServiceImpl extends AbstractService<Share> implements ShareService {
@Resource
private ShareMapper shareMapper;

}
