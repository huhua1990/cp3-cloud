package com.cp3.cloud.msg.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.base.basic.mapper.SuperMapper;
import com.cp3.base.basic.request.PageParams;
import com.cp3.cloud.msg.dto.MsgPageResult;
import com.cp3.cloud.msg.dto.MsgQuery;
import com.cp3.cloud.msg.entity.Msg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 消息中心
 * </p>
 *
 * @author zuihou
 * @date 2019-08-01
 */
@Repository
public interface MsgMapper extends SuperMapper<Msg> {
    /**
     * 查询消息中心分页数据
     *
     * @param page  分页参数
     * @param param 消息参数
     * @return 分页数据
     */
    IPage<MsgPageResult> page(IPage<MsgPageResult> page, @Param("param") PageParams<MsgQuery> param);
}
