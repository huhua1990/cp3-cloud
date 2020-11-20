package com.cp3.cloud.authority.service.core;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.cloud.authority.dto.core.StationPageDTO;
import com.cp3.cloud.authority.entity.core.Station;
import com.cp3.cloud.base.request.PageParams;
import com.cp3.cloud.base.service.SuperCacheService;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 业务接口
 * 岗位
 * </p>
 *
 * @author cp3
 * @date 2019-07-22
 */
public interface StationService extends SuperCacheService<Station> {
    /**
     * 按权限查询岗位的分页信息
     *
     * @param page
     * @param params
     * @return
     */
    IPage<Station> findStationPage(IPage page, PageParams<StationPageDTO> params);

    /**
     * 根据id 查询
     *
     * @param ids
     * @return
     */
    Map<Serializable, Object> findStationByIds(Set<Serializable> ids);

    /**
     * 根据id 查询 岗位名称
     *
     * @param ids
     * @return
     */
    Map<Serializable, Object> findStationNameByIds(Set<Serializable> ids);
}
