package com.cp3.cloud.tenant.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.cp3.base.basic.mapper.SuperMapper;
import com.cp3.cloud.tenant.entity.Tenant;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 企业
 * </p>
 *
 * @author zuihou
 * @date 2019-10-25
 */
@Repository
@InterceptorIgnore(tenantLine = "true", dynamicTableName = "true")
public interface TenantMapper extends SuperMapper<Tenant> {

    /**
     * 根据租户编码查询
     *
     * @param code 租户编码
     * @return 租户
     */
    Tenant getByCode(@Param("code") String code);
}
