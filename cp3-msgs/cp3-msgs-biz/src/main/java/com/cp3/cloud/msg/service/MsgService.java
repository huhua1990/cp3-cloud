package com.cp3.cloud.msg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.base.basic.request.PageParams;
import com.cp3.base.basic.service.SuperService;
import com.cp3.cloud.msg.dto.MsgPageResult;
import com.cp3.cloud.msg.dto.MsgQuery;
import com.cp3.cloud.msg.dto.MsgSaveDTO;
import com.cp3.cloud.msg.entity.Msg;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 消息中心
 * </p>
 *
 * @author zuihou
 * @date 2019-08-01
 */
public interface MsgService extends SuperService<Msg> {

    /**
     * 保存消息
     *
     * @param data 消息
     * @return 消息
     */
    Msg saveMsg(MsgSaveDTO data);

    /**
     * 删除指定用户 指定消息的数据
     *
     * @param ids    消息id
     * @param userId 用户id
     * @return 是否成功
     */
    boolean delete(List<Long> ids, Long userId);

    /**
     * 标记状态
     *
     * @param msgCenterIds 主表id
     * @param userId       用户id
     * @return 是否成功
     */
    boolean mark(List<Long> msgCenterIds, Long userId);

    /**
     * 分页查询
     *
     * @param page  分页
     * @param param 消息条件
     * @return 分页数据
     */
    IPage<MsgPageResult> page(IPage<MsgPageResult> page, PageParams<MsgQuery> param);
}
