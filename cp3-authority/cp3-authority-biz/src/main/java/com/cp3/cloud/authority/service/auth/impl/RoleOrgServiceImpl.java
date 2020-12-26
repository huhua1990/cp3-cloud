package com.cp3.cloud.authority.service.auth.impl;


import com.cp3.cloud.authority.dao.auth.RoleOrgMapper;
import com.cp3.cloud.authority.entity.auth.RoleOrg;
import com.cp3.cloud.authority.service.auth.RoleOrgService;
import com.cp3.base.basic.service.SuperServiceImpl;
import com.cp3.base.database.mybatis.conditions.Wraps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 业务实现类
 * 角色组织关系
 * </p>
 *
 * @author zuihou
 * @date 2019-07-03
 */
@Slf4j
@Service

public class RoleOrgServiceImpl extends SuperServiceImpl<RoleOrgMapper, RoleOrg> implements RoleOrgService {
    @Override
    public List<Long> listOrgByRoleId(Long roleId) {
        List<RoleOrg> list = super.list(Wraps.<RoleOrg>lbQ().eq(RoleOrg::getRoleId, roleId));
        return list.stream().mapToLong(RoleOrg::getOrgId).boxed().collect(Collectors.toList());
    }
}
