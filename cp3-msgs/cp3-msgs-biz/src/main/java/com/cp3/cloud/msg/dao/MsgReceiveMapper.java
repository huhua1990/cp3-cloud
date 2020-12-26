package com.cp3.cloud.msg.dao;

import com.cp3.base.basic.mapper.SuperMapper;
import com.cp3.cloud.msg.entity.MsgReceive;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 消息中心 接收表
 * 全量数据
 * </p>
 *
 * @author zuihou
 * @date 2019-08-01
 */
@Repository
public interface MsgReceiveMapper extends SuperMapper<MsgReceive> {

}
