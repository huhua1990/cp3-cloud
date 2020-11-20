package com.cp3.cloud.authority.service.auth.impl;


import com.cp3.cloud.authority.dao.auth.RoleOrgMapper;
import com.cp3.cloud.authority.entity.auth.RoleOrg;
import com.cp3.cloud.authority.service.auth.RoleOrgService;
import com.cp3.cloud.base.service.SuperServiceImpl;
import com.cp3.cloud.database.mybatis.conditions.Wraps;
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
 * @author cp3
 * @date 2019-07-03
 */
@Slf4j
@Service

public class RoleOrgServiceImpl extends SuperServiceImpl<RoleOrgMapper, RoleOrg> implements RoleOrgService {
    @Override
    public List<Long> listOrgByRoleId(Long id) {
        List<RoleOrg> list = super.list(Wraps.<RoleOrg>lbQ().eq(RoleOrg::getRoleId, id));
        List<Long> orgList = list.stream().mapToLong(RoleOrg::getOrgId).boxed().collect(Collectors.toList());
        return orgList;
    }
}
