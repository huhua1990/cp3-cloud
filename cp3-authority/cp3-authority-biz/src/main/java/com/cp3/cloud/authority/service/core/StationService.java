package com.cp3.cloud.authority.service.core;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.base.basic.request.PageParams;
import com.cp3.base.basic.service.SuperCacheService;
import com.cp3.cloud.authority.dto.core.StationPageQuery;
import com.cp3.cloud.authority.entity.core.Station;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 业务接口
 * 岗位
 * </p>
 *
 * @author zuihou
 * @date 2019-07-22
 */
public interface StationService extends SuperCacheService<Station> {
    /**
     * 按权限查询岗位的分页信息
     *
     * @param page   分页对象
     * @param params 分页参数
     * @return 分页数据
     */
    IPage<Station> findStationPage(IPage<Station> page, PageParams<StationPageQuery> params);

    /**
     * 根据id 查询
     *
     * @param ids id
     * @return id-岗位
     */
    Map<Serializable, Object> findStationByIds(Set<Serializable> ids);

    /**
     * 根据id 查询 岗位名称
     *
     * @param ids id
     * @return id-岗位名称
     */
    Map<Serializable, Object> findStationNameByIds(Set<Serializable> ids);
}
