package com.cp3.cloud.authority.service.auth.impl;


import com.cp3.cloud.authority.dao.auth.RoleMapper;
import com.cp3.cloud.authority.dao.auth.UserRoleMapper;
import com.cp3.cloud.authority.entity.auth.Role;
import com.cp3.cloud.authority.entity.auth.UserRole;
import com.cp3.cloud.authority.service.auth.UserRoleService;
import com.cp3.cloud.base.service.SuperServiceImpl;
import com.cp3.cloud.database.mybatis.conditions.Wraps;
import com.cp3.cloud.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cp3.cloud.common.constant.BizConstant.INIT_ROLE_CODE;

/**
 * <p>
 * 业务实现类
 * 角色分配
 * 账号角色绑定
 * </p>
 *
 * @author cp3
 * @date 2019-07-03
 */
@Slf4j
@Service

public class UserRoleServiceImpl extends SuperServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initAdmin(Long userId) {
        Role role = roleMapper.selectOne(Wraps.<Role>lbQ().eq(Role::getCode, INIT_ROLE_CODE));
        if (role == null) {
            throw BizException.wrap("初始化用户角色失败, 无法查询到内置角色:%s", INIT_ROLE_CODE);
        }
        UserRole userRole = UserRole.builder()
                .userId(userId).roleId(role.getId())
                .build();

        return super.save(userRole);
    }
}
