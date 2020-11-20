package com.cp3.cloud.authority.service.auth.impl;

import cn.hutool.core.convert.Convert;
import com.cp3.cloud.authority.dao.auth.RoleMapper;
import com.cp3.cloud.authority.dto.auth.RoleSaveDTO;
import com.cp3.cloud.authority.dto.auth.RoleUpdateDTO;
import com.cp3.cloud.authority.entity.auth.Role;
import com.cp3.cloud.authority.entity.auth.RoleAuthority;
import com.cp3.cloud.authority.entity.auth.RoleOrg;
import com.cp3.cloud.authority.entity.auth.UserRole;
import com.cp3.cloud.authority.service.auth.RoleAuthorityService;
import com.cp3.cloud.authority.service.auth.RoleOrgService;
import com.cp3.cloud.authority.service.auth.RoleService;
import com.cp3.cloud.authority.service.auth.UserRoleService;
import com.cp3.cloud.authority.strategy.DataScopeContext;
import com.cp3.cloud.base.service.SuperCacheServiceImpl;
import com.cp3.cloud.common.constant.CacheKey;
import com.cp3.cloud.database.mybatis.conditions.Wraps;
import com.cp3.cloud.utils.BeanPlusUtil;
import com.cp3.cloud.utils.CodeGenerate;
import com.cp3.cloud.utils.StrHelper;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.cp3.cloud.common.constant.CacheKey.ROLE;

/**
 * <p>
 * 业务实现类
 * 角色
 * </p>
 *
 * @author cp3
 * @date 2019-07-03
 */
@Slf4j
@Service
public class RoleServiceImpl extends SuperCacheServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleOrgService roleOrgService;
    @Autowired
    private RoleAuthorityService roleAuthorityService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private DataScopeContext dataScopeContext;
    @Autowired
    private CodeGenerate codeGenerate;

    @Override
    protected String getRegion() {
        return ROLE;
    }

    @Override
    public boolean isSuperAdmin(Long userId) {
        return userId != null && userId.equals(1L);
    }


    /**
     * 删除角色时，需要级联删除跟角色相关的一切资源：
     * 1，角色本身
     * 2，角色-组织：
     * 3，角色-权限（菜单和按钮）：
     * 4，角色-用户：角色拥有的用户
     * 5，用户-权限：
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIdWithCache(List<Long> ids) {
        if (ids.isEmpty()) {
            return true;
        }
        // 橘色
        boolean removeFlag = removeByIds(ids);
        // 角色组织
        roleOrgService.remove(Wraps.<RoleOrg>lbQ().in(RoleOrg::getRoleId, ids));
        // 角色权限
        roleAuthorityService.remove(Wraps.<RoleAuthority>lbQ().in(RoleAuthority::getRoleId, ids));

        List<Long> userIds = userRoleService.listObjs(
                Wraps.<UserRole>lbQ().select(UserRole::getUserId).in(UserRole::getRoleId, ids),
                Convert::toLong);

        //角色拥有的用户
        userRoleService.remove(Wraps.<UserRole>lbQ().in(UserRole::getRoleId, ids));

        ids.forEach((id) -> {
            cacheChannel.evict(CacheKey.ROLE_MENU, key(id));
            cacheChannel.evict(CacheKey.ROLE_RESOURCE, key(id));
        });

        if (!userIds.isEmpty()) {
            //用户角色 、 用户菜单、用户资源
            String[] userIdArray = userIds.stream().map(this::key).toArray(String[]::new);
            cacheChannel.evict(CacheKey.USER_ROLE, userIdArray);
            cacheChannel.evict(CacheKey.USER_RESOURCE, userIdArray);
            cacheChannel.evict(CacheKey.USER_MENU, userIdArray);
        }
        return removeFlag;
    }

    /**
     * 1、根据 {tenant}:USER_ROLE:{userId} 查询用户拥有的角色ID集合
     * 2、缓存中有，则根据角色ID集合查询 角色集合
     * 3、缓存中有查不到，则从DB查询，并写入缓存， 立即返回
     *
     * @param userId 用户id
     * @return
     */
    @Override
    public List<Role> findRoleByUserId(Long userId) {
        String key = key(userId);
        List<Role> roleList = new ArrayList<>();
        CacheObject cacheObject = cacheChannel.get(CacheKey.USER_ROLE, key, (k) -> {
            roleList.addAll(baseMapper.findRoleByUserId(userId));
            return roleList.stream().mapToLong(Role::getId).boxed().collect(Collectors.toList());
        });

        if (cacheObject.getValue() == null) {
            return Collections.emptyList();
        }


        if (!roleList.isEmpty()) {
            // TODO 异步性能 更佳
            roleList.forEach((item) -> {
                String itemKey = key(item.getId());
                cacheChannel.set(ROLE, itemKey, item);
            });

            return roleList;
        }

        List<Long> list = (List<Long>) cacheObject.getValue();

        List<Role> menuList = list.stream().map(this::getByIdCache)
                .filter(Objects::nonNull).collect(Collectors.toList());

        return menuList;
    }

    /**
     * 1，保存角色
     * 2，保存 与组织的关系
     *
     * @param data
     * @param userId 用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(RoleSaveDTO data, Long userId) {
        Role role = BeanPlusUtil.toBean(data, Role.class);
        role.setCode(StrHelper.getOrDef(data.getCode(), codeGenerate.next()));
        role.setReadonly(false);
        save(role);

        saveRoleOrg(userId, role, data.getOrgList());

        cacheChannel.set(CacheKey.ROLE, key(role.getId()), role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleUpdateDTO data, Long userId) {
        Role role = BeanPlusUtil.toBean(data, Role.class);
        updateById(role);

        roleOrgService.remove(Wraps.<RoleOrg>lbQ().eq(RoleOrg::getRoleId, data.getId()));
        saveRoleOrg(userId, role, data.getOrgList());

    }

    private void saveRoleOrg(Long userId, Role role, List<Long> orgList) {
        // 根据 数据范围类型 和 勾选的组织ID， 重新计算全量的组织ID
        List<Long> orgIds = dataScopeContext.getOrgIdsForDataScope(orgList, role.getDsType(), userId);
        if (orgIds != null && !orgIds.isEmpty()) {
            List<RoleOrg> list = orgIds.stream().map((orgId) -> RoleOrg.builder().orgId(orgId).roleId(role.getId()).build()).collect(Collectors.toList());
            roleOrgService.saveBatch(list);
        }
    }

    @Override
    public List<Long> findUserIdByCode(String[] codes) {
        return baseMapper.findUserIdByCode(codes);
    }

    @Override
    public Boolean check(String code) {
        return super.count(Wraps.<Role>lbQ().eq(Role::getCode, code)) > 0;
    }
}
