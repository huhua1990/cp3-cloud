package com.cp3.cloud.security.aspect;


import cn.hutool.core.collection.CollUtil;
import com.cp3.cloud.base.R;
import com.cp3.cloud.security.constant.RoleConstant;
import com.cp3.cloud.security.feign.UserQuery;
import com.cp3.cloud.security.feign.UserResolverService;
import com.cp3.cloud.security.model.SysRole;
import com.cp3.cloud.security.model.SysUser;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限判断
 *
 * @author cp3
 * @date 2020年03月29日21:14:58
 */
public class VerifyAuthFunction {
    private final UserResolverService userResolverService;

    public VerifyAuthFunction(UserResolverService userResolverService) {
        this.userResolverService = userResolverService;
    }

    /**
     * 放行请求
     *
     * @return {boolean}
     */
    public boolean permit() {
        return true;
    }

    /**
     * 只有超管角色才可访问
     *
     * @return {boolean}
     */
    public boolean denyAll() {
        return hasRole(RoleConstant.PT_ADMIN);
    }

    /**
     * 判断是否有该角色权限
     *
     * @param role 单角色编码
     * @return {boolean}
     */
    public boolean hasRole(String role) {
        return hasAnyRole(role);
    }

    /**
     * 判断是否有该 资源
     *
     * @param permit 单资源编码
     * @return {boolean}
     */
    public boolean hasPermit(String permit) {
        return hasAnyPermit(permit);
    }

    /**
     * 判断是否有任意 资源
     *
     * @param permit 多个资源编码
     * @return {boolean}
     */
    public boolean hasAnyPermit(String... permit) {
        // 查询当前用户拥有的所有资源
        Set<String> resources = getAllResources();

        // 判断是否包含所需的角色
        return CollUtil.containsAny(resources, CollUtil.newHashSet(permit));
    }

    /**
     * 判断是否包含所有资源
     *
     * @param permit 多个资源编码
     * @return {boolean}
     */
    public boolean hasAllPermit(String... permit) {
        Set<String> resources = getAllResources();

        // 判断是否包含所需的角色
        return CollUtil.containsAll(resources, CollUtil.newHashSet(permit));
    }

    @NonNull
    private Set<String> getAllResources() {
        // 查询当前用户拥有的所有资源
        Set<String> resources = new HashSet<>();

        R<SysUser> result = userResolverService.getById(UserQuery.buildResource());
        if (result.getIsSuccess() && result.getData() != null && result.getData().getResources() != null) {
            SysUser sysUser = result.getData();
            resources = new HashSet<>(sysUser.getResources());
        }
        return resources;
    }

    /**
     * 判断是否包含任意角色
     *
     * @param role 角色集合
     * @return {boolean}
     */
    public boolean hasAnyRole(String... role) {
        Set<String> roles = getAllRoles();

        // 判断是否包含所需的角色
        return CollUtil.containsAny(roles, CollUtil.newHashSet(role));
    }

    /**
     * 判断是否拥有所有的角色编码
     *
     * @param role 角色集合
     * @return {boolean}
     */
    public boolean hasAllRole(String... role) {
        Set<String> roles = getAllRoles();

        // 判断是否包含所需的角色
        return CollUtil.containsAll(roles, CollUtil.newHashSet(role));
    }

    @NonNull
    private Set<String> getAllRoles() {
        // 查询当前用户拥有的所有角色
        Set<String> roles = new HashSet<>();

        R<SysUser> result = userResolverService.getById(UserQuery.buildRoles());
        if (result.getIsSuccess() && result.getData() != null && result.getData().getRoles() != null) {
            SysUser sysUser = result.getData();
            roles = sysUser.getRoles().stream().map(SysRole::getCode).collect(Collectors.toSet());
        }
        return roles;
    }
}
