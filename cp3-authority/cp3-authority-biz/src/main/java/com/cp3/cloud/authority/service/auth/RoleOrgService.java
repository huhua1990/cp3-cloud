package com.cp3.cloud.authority.service.auth;

import com.cp3.cloud.authority.entity.auth.RoleOrg;
import com.cp3.base.basic.service.SuperService;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 角色组织关系
 * </p>
 *
 * @author zuihou
 * @date 2019-07-03
 */
public interface RoleOrgService extends SuperService<RoleOrg> {

    /**
     * 根据角色id查询
     *
     * @param roleId 角色id
     * @return 组织Id
     */
    List<Long> listOrgByRoleId(Long roleId);
}
