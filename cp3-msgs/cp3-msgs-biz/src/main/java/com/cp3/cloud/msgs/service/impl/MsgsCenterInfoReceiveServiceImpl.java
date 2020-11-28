package com.cp3.cloud.msgs.service.impl;


import com.cp3.cloud.base.service.SuperServiceImpl;
import com.cp3.cloud.msgs.dao.MsgsCenterInfoReceiveMapper;
import com.cp3.cloud.msgs.entity.MsgsCenterInfoReceive;
import com.cp3.cloud.msgs.service.MsgsCenterInfoReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实现类
 * 消息中心 接收表
 * 全量数据
 * </p>
 *
 * @author cp3
 * @date 2019-08-01
 */
@Slf4j
@Service

public class MsgsCenterInfoReceiveServiceImpl extends SuperServiceImpl<MsgsCenterInfoReceiveMapper, MsgsCenterInfoReceive> implements MsgsCenterInfoReceiveService {

}
