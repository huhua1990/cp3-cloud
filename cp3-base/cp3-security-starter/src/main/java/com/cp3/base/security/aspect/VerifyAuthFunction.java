package com.cp3.base.security.aspect;


import cn.hutool.core.collection.CollUtil;
import com.cp3.base.basic.R;
import com.cp3.base.security.constant.RoleConstant;
import com.cp3.base.security.feign.UserQuery;
import com.cp3.base.security.feign.UserResolverService;
import com.cp3.base.security.model.SysRole;
import com.cp3.base.security.model.SysUser;
import com.cp3.base.security.permission.AuthorizingRealm;
import com.cp3.base.security.properties.SecurityProperties;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限判断
 *
 * @author zuihou
 * @date 2020年03月29日21:14:58
 */
public class VerifyAuthFunction {
    private final UserResolverService userResolverService;
    private final SecurityProperties securityProperties;

    public VerifyAuthFunction(UserResolverService userResolverService, SecurityProperties securityProperties) {
        this.userResolverService = userResolverService;
        this.securityProperties = securityProperties;
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
        return hasAnyRole(RoleConstant.SUPER_ADMIN);
    }

    /**
     * 判断是否有任意 资源
     * <p>
     * 等价于前端自定义指令：v-hasAnyPermission、v-has-any-permission
     *
     * @param permit 多个资源编码
     * @return {boolean}
     */
    public boolean hasAnyPermission(String... permit) {
        // 查询当前用户拥有的所有资源
        Set<String> resources = getAllResources();
        // 判断是否包含所需的角色
        return AuthorizingRealm.hasAnyPermission(resources, permit, securityProperties.getCaseSensitive());
    }

    /**
     * 判断是否有任意 资源
     * <p>
     * 等价于前端自定义指令： v-hasNoPermission、v-has-no-permission
     *
     * @param permit 多个资源编码
     * @return {boolean}
     */
    public boolean hasNoPermission(String... permit) {
        // 查询当前用户拥有的所有资源
        Set<String> resources = getAllResources();
        // 判断是否包含所需的角色
        return AuthorizingRealm.hasNoPermission(resources, permit, securityProperties.getCaseSensitive());
    }

    /**
     * 判断是否包含所有资源
     * <p>
     * 等价于前端自定义指令： v-hasPermission、v-has-permission
     *
     * @param permit 多个资源编码
     * @return {boolean}
     */
    public boolean hasPermission(String... permit) {
        Set<String> resources = getAllResources();
        return AuthorizingRealm.hasAllPermission(resources, permit, securityProperties.getCaseSensitive());
    }

    /**
     * 远程查询当前登录的用户拥有那些权限
     *
     * @return
     */
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
    public boolean hasRole(String... role) {
        Set<String> roles = getAllRoles();

        // 判断是否包含所需的角色
        return CollUtil.containsAll(roles, CollUtil.newHashSet(role));
    }

    /**
     * 判断是否没有所有的角色编码
     *
     * @param role 角色集合
     * @return 是否不含所有角色
     */
    public boolean hasNoRole(String... role) {
        return !hasRole(role);
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
