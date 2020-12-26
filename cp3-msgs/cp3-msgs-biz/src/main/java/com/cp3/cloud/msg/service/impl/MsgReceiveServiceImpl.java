package com.cp3.cloud.msg.service.impl;


import com.cp3.base.basic.service.SuperServiceImpl;
import com.cp3.cloud.msg.dao.MsgReceiveMapper;
import com.cp3.cloud.msg.entity.MsgReceive;
import com.cp3.cloud.msg.service.MsgReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实现类
 * 消息中心 接收表
 * 全量数据
 * </p>
 *
 * @author zuihou
 * @date 2019-08-01
 */
@Slf4j
@Service

public class MsgReceiveServiceImpl extends SuperServiceImpl<MsgReceiveMapper, MsgReceive> implements MsgReceiveService {

}
