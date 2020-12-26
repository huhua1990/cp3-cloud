package com.cp3.cloud.authority.service.auth.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.base.basic.request.PageParams;
import com.cp3.base.basic.request.PageUtil;
import com.cp3.base.basic.service.SuperCacheServiceImpl;
import com.cp3.base.cache.model.CacheKey;
import com.cp3.base.cache.model.CacheKeyBuilder;
import com.cp3.base.database.mybatis.auth.DataScope;
import com.cp3.base.database.mybatis.auth.DataScopeType;
import com.cp3.base.database.mybatis.conditions.Wraps;
import com.cp3.base.database.mybatis.conditions.query.LbqWrapper;
import com.cp3.base.model.RemoteData;
import com.cp3.base.security.feign.UserQuery;
import com.cp3.base.security.model.SysOrg;
import com.cp3.base.security.model.SysRole;
import com.cp3.base.security.model.SysStation;
import com.cp3.base.security.model.SysUser;
import com.cp3.base.basic.utils.BeanPlusUtil;
import com.cp3.base.utils.BizAssert;
import com.cp3.base.utils.CollHelper;
import com.cp3.base.utils.StrPool;
import com.cp3.cloud.authority.dao.auth.UserMapper;
import com.cp3.cloud.authority.dto.auth.GlobalUserPageDTO;
import com.cp3.cloud.authority.dto.auth.ResourceQueryDTO;
import com.cp3.cloud.authority.dto.auth.UserUpdatePasswordDTO;
import com.cp3.cloud.authority.entity.auth.Resource;
import com.cp3.cloud.authority.entity.auth.Role;
import com.cp3.cloud.authority.entity.auth.RoleOrg;
import com.cp3.cloud.authority.entity.auth.User;
import com.cp3.cloud.authority.entity.auth.UserRole;
import com.cp3.cloud.authority.entity.core.Org;
import com.cp3.cloud.authority.entity.core.Station;
import com.cp3.cloud.authority.service.auth.ResourceService;
import com.cp3.cloud.authority.service.auth.RoleOrgService;
import com.cp3.cloud.authority.service.auth.RoleService;
import com.cp3.cloud.authority.service.auth.UserRoleService;
import com.cp3.cloud.authority.service.auth.UserService;
import com.cp3.cloud.authority.service.core.OrgService;
import com.cp3.cloud.authority.service.core.StationService;
import com.cp3.cloud.common.cache.auth.UserAccountCacheKeyBuilder;
import com.cp3.cloud.common.cache.auth.UserCacheKeyBuilder;
import com.cp3.cloud.common.cache.auth.UserMenuCacheKeyBuilder;
import com.cp3.cloud.common.cache.auth.UserResourceCacheKeyBuilder;
import com.cp3.cloud.common.cache.auth.UserRoleCacheKeyBuilder;
import com.cp3.cloud.common.constant.BizConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * <p>
 * 业务实现类
 * 账号
 * </p>
 *
 * @author zuihou
 * @date 2019-07-03
 */
@Slf4j
@Service

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl extends SuperCacheServiceImpl<UserMapper, User> implements UserService {

    private final StationService stationService;
    private final RoleService roleService;
    private final ResourceService resourceService;
    private final UserRoleService userRoleService;
    private final RoleOrgService roleOrgService;
    private final OrgService orgService;

    @Override
    protected CacheKeyBuilder cacheKeyBuilder() {
        return new UserCacheKeyBuilder();
    }

    @Override
    public IPage<User> pageByRole(IPage<User> page, PageParams<GlobalUserPageDTO> params) {
        params.put("roleCode", BizConstant.INIT_ROLE_CODE);
        PageUtil.timeRange(params);
        return baseMapper.pageByRole(page, params);
    }

    @Override
    public IPage<User> findPage(IPage<User> page, LbqWrapper<User> wrapper) {
        return baseMapper.findPage(page, wrapper, new DataScope());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePassword(UserUpdatePasswordDTO data) {
        User user = getById(data.getId());
        BizAssert.notNull(user, "用户不存在");
        String oldPassword = SecureUtil.sha256(data.getOldPassword() + user.getSalt());
        BizAssert.equals(user.getPassword(), oldPassword, "旧密码错误");

        return reset(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reset(UserUpdatePasswordDTO data) {
        BizAssert.equals(data.getConfirmPassword(), data.getPassword(), "密码和重复密码不一致");
        User user = getById(data.getId());
        BizAssert.notNull(user, "用户不存在");
        String defPassword = SecureUtil.sha256(data.getPassword() + user.getSalt());
        super.update(Wraps.<User>lbU()
                .set(User::getPassword, defPassword)
                .set(User::getPasswordErrorNum, 0L)
                // 置空
                .set(User::getPasswordErrorLastTime, null)
                .eq(User::getId, data.getId())
        );
        delCache(data.getId());
        return true;
    }

    @Override
    public User getByAccount(String account) {
        Function<CacheKey, Object> loader = k -> getObj(Wraps.<User>lbQ().select(User::getId).eq(User::getAccount, account), Convert::toLong);
        CacheKeyBuilder builder = new UserAccountCacheKeyBuilder();
        return getByKey(builder.key(account), loader);
    }

    @Override
    public List<User> findUserByRoleId(Long roleId, String keyword) {
        return baseMapper.findUserByRoleId(roleId, keyword);
    }

    @Override
    public Map<String, Object> getDataScopeById(Long userId) {
        Map<String, Object> map = new HashMap<>(4);
        List<Long> orgIds = new ArrayList<>();

        List<Role> list = roleService.findRoleByUserId(userId);

        // 找到 dsType 最大的角色， dsType越大，角色拥有的权限最大
        Optional<Role> max = list.stream().max(Comparator.comparingInt(item -> item.getDsType().getVal()));

        DataScopeType dsType;
        if (max.isPresent()) {
            Role role = max.get();
            dsType = role.getDsType();
            map.put("dsType", dsType.getVal());
            if (DataScopeType.CUSTOMIZE.eq(dsType)) {
                LbqWrapper<RoleOrg> wrapper = Wraps.<RoleOrg>lbQ().select(RoleOrg::getOrgId).eq(RoleOrg::getRoleId, role.getId());
                List<RoleOrg> roleOrgList = roleOrgService.list(wrapper);

                orgIds = roleOrgList.stream().mapToLong(RoleOrg::getOrgId).boxed().collect(Collectors.toList());
            } else if (DataScopeType.THIS_LEVEL.eq(dsType)) {
                User user = getByIdCache(userId);
                if (user != null) {
                    Long orgId = RemoteData.getKey(user.getOrg());
                    if (orgId != null) {
                        orgIds.add(orgId);
                    }
                }
            } else if (DataScopeType.THIS_LEVEL_CHILDREN.eq(dsType)) {
                User user = getByIdCache(userId);
                if (user != null) {
                    Long orgId = RemoteData.getKey(user.getOrg());
                    List<Org> orgList = orgService.findChildren(CollUtil.newArrayList(orgId));
                    orgIds = orgList.stream().mapToLong(Org::getId).boxed().collect(Collectors.toList());
                }
            }
        }
        map.put("orgIds", orgIds);
        return map;
    }

    @Override
    public boolean check(String account) {
        return getByAccount(account) != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrPasswordErrorNumById(Long id) {
        baseMapper.incrPasswordErrorNumById(id);
        delCache(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resetPassErrorNum(Long id) {
        int count = baseMapper.resetPassErrorNum(id, LocalDateTime.now());
        delCache(id);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User saveUser(User user) {
        user.setSalt(RandomUtil.randomString(20));
        user.setPassword(SecureUtil.sha256(user.getPassword() + user.getSalt()));
        user.setPasswordErrorNum(0);
        super.save(user);
        return user;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(User user) {
        // 不允许修改用户信息时修改密码，请单独调用修改密码接口
        user.setPassword(null);
        user.setSalt(null);
        updateById(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(List<Long> ids) {
        if (ids.isEmpty()) {
            return true;
        }
        userRoleService.remove(Wraps.<UserRole>lbQ().in(UserRole::getUserId, ids));
        cacheOps.del(ids.stream().map(new UserRoleCacheKeyBuilder()::key).toArray(CacheKey[]::new));
        cacheOps.del(ids.stream().map(new UserMenuCacheKeyBuilder()::key).toArray(CacheKey[]::new));
        cacheOps.del(ids.stream().map(new UserResourceCacheKeyBuilder()::key).toArray(CacheKey[]::new));
        return removeByIds(ids);
    }

    @Override
    public Map<Serializable, Object> findUserByIds(Set<Serializable> ids) {
        //key 是 用户id
        return CollHelper.uniqueIndex(findUser(ids), User::getId, (user) -> user);
    }

    @Override
    public Map<Serializable, Object> findUserNameByIds(Set<Serializable> ids) {
        return CollHelper.uniqueIndex(findUser(ids), User::getId, User::getName);
    }

    @Override
    public List<User> findUser(Set<Serializable> ids) {
        // 强转， 防止数据库隐式转换，  若你的id 是string类型，请勿强转
        return findByIds(ids,
                missIds -> super.listByIds(missIds.stream().filter(Objects::nonNull).map(Convert::toLong).collect(Collectors.toList()))
        );
    }

    @Override
    public List<User> findUserById(List<Long> ids) {
        return findUser(new HashSet<>(ids));
    }

    @Override
    public SysUser getSysUserById(Long id, UserQuery query) {
        User user = getByIdCache(id);
        if (user == null) {
            return null;
        }
        SysUser sysUser = BeanUtil.toBean(user, SysUser.class);

        sysUser.setOrgId(RemoteData.getKey(user.getOrg()));
        sysUser.setStationId(RemoteData.getKey(user.getOrg()));

        if (query.getFull() || query.getOrg()) {
            sysUser.setOrg(BeanUtil.toBean(orgService.getByIdCache(user.getOrg()), SysOrg.class));
        }

        if (query.getFull() || query.getStation()) {
            Station station = stationService.getByIdCache(user.getStation());
            if (station != null) {
                SysStation sysStation = BeanUtil.toBean(station, SysStation.class);
                sysStation.setOrgId(RemoteData.getKey(station.getOrg()));
                sysUser.setStation(sysStation);
            }
        }

        if (query.getFull() || query.getRoles()) {
            List<Role> list = roleService.findRoleByUserId(id);
            sysUser.setRoles(BeanPlusUtil.toBeanList(list, SysRole.class));
        }
        if (query.getFull() || query.getResource()) {
            List<Resource> resourceList = resourceService.findVisibleResource(ResourceQueryDTO.builder().userId(id).build());
            sysUser.setResources(CollHelper.split(resourceList, Resource::getCode, StrPool.SEMICOLON));
        }

        return sysUser;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Long> findAllUserId() {
        return super.listObjs(Wraps.<User>lbQ().select(User::getId), Convert::toLong);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initUser(User user) {
        this.saveUser(user);
        return userRoleService.initAdmin(user.getId());
    }
}
