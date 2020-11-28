package com.cp3.cloud.msgs.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.cloud.base.mapper.SuperMapper;
import com.cp3.cloud.msgs.dto.MsgsCenterInfoPageResultDTO;
import com.cp3.cloud.msgs.dto.MsgsCenterInfoQueryDTO;
import com.cp3.cloud.msgs.entity.MsgsCenterInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 消息中心
 * </p>
 *
 * @author cp3
 * @date 2019-08-01
 */
@Repository
public interface MsgsCenterInfoMapper extends SuperMapper<MsgsCenterInfo> {
    /**
     * 查询消息中心分页数据
     *
     * @param page
     * @param param
     * @return
     */
    IPage<MsgsCenterInfoPageResultDTO> page(IPage page, @Param("data") MsgsCenterInfoQueryDTO param);
}
