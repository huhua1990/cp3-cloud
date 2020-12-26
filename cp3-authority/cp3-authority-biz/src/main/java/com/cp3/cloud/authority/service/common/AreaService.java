package com.cp3.cloud.authority.service.common;

import com.cp3.cloud.authority.entity.common.Area;
import com.cp3.base.basic.service.SuperCacheService;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 地区表
 * </p>
 *
 * @author zuihou
 * @date 2019-07-02
 */
public interface AreaService extends SuperCacheService<Area> {

    /**
     * 递归删除
     *
     * @param ids 地区id
     * @return 是否成功
     */
    boolean recursively(List<Long> ids);
}
