package com.cp3.cloud.tenant.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.cp3.cloud.base.mapper.SuperMapper;
import com.cp3.cloud.tenant.entity.GlobalUser;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * 全局账号
 * </p>
 *
 * @author cp3
 * @date 2019-10-25
 */
@Repository
@InterceptorIgnore(tenantLine = "true", dynamicTableName = "true")
public interface GlobalUserMapper extends SuperMapper<GlobalUser> {

}
