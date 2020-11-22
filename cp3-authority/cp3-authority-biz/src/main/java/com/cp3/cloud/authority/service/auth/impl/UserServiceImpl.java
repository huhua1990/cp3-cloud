package com.cp3.cloud.authority.service.auth.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cp3.cloud.authority.dao.auth.UserMapper;
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
import com.cp3.cloud.base.service.SuperCacheServiceImpl;
import com.cp3.cloud.common.constant.CacheKey;
import com.cp3.cloud.database.mybatis.auth.DataScope;
import com.cp3.cloud.database.mybatis.auth.DataScopeType;
import com.cp3.cloud.database.mybatis.conditions.Wraps;
import com.cp3.cloud.database.mybatis.conditions.query.LbqWrapper;
import com.cp3.cloud.model.RemoteData;
import com.cp3.cloud.security.feign.UserQuery;
import com.cp3.cloud.security.model.SysOrg;
import com.cp3.cloud.security.model.SysRole;
import com.cp3.cloud.security.model.SysStation;
import com.cp3.cloud.security.model.SysUser;
import com.cp3.cloud.utils.BeanPlusUtil;
import com.cp3.cloud.utils.BizAssert;
import com.cp3.cloud.utils.MapHelper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import static com.cp3.cloud.common.constant.CacheKey.USER_ACCOUNT;


/**
 * <p>
 * 业务实现类
 * 账号
 * </p>
 *
 * @author cp3
 * @date 2019-07-03
 */
@Slf4j
@Service
public class UserServiceImpl extends SuperCacheServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StationService stationService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleOrgService roleOrgService;
    @Autowired
    private OrgService orgService;

    @Override
    protected String getRegion() {
        return CacheKey.USER;
    }

    @Override
    public IPage<User> findPage(IPage<User> page, LbqWrapper<User> wrapper) {
        return baseMapper.findPage(page, wrapper, new DataScope());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updatePassword(UserUpdatePasswordDTO data) {
        BizAssert.equals(data.getConfirmPassword(), data.getPassword(), "密码与确认密码不一致");

        User user = getById(data.getId());
        BizAssert.notNull(user, "用户不存在");
        String oldPassword = SecureUtil.md5(data.getOldPassword());
        BizAssert.equals(user.getPassword(), oldPassword, "旧密码错误");

        User build = User.builder().password(SecureUtil.md5(data.getPassword())).id(data.getId()).build();
        return updateById(build);
    }

    @Override
    public User getByAccount(String account) {
        String key = buildTenantKey(account);
        Function<String, Object> loader = (k) -> getObj(Wraps.<User>lbQ().select(User::getId).eq(User::getAccount, account), Convert::toLong);
        return getByKey(USER_ACCOUNT, key, loader);
    }

    @Override
    public List<User> findUserByRoleId(Long roleId, String keyword) {
        return baseMapper.findUserByRoleId(roleId, keyword);
    }

    @Override
    public Map<String, Object> getDataScopeById(Long userId) {
        Map<String, Object> map = new HashMap<>(2);
        List<Long> orgIds = new ArrayList<>();
        DataScopeType dsType = DataScopeType.SELF;

        List<Role> list = roleService.findRoleByUserId(userId);

        // 找到 dsType 最大的角色， dsType越大，角色拥有的权限最大
        Optional<Role> max = list.stream().max(Comparator.comparingInt((item) -> item.getDsType().getVal()));

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
                    List<Org> orgList = orgService.findChildren(Arrays.asList(orgId));
                    orgIds = orgList.stream().mapToLong(Org::getId).boxed().collect(Collectors.toList());
                }
            }
        }
        map.put("orgIds", orgIds);
        return map;
    }

    @Override
    public boolean check(String account) {
        return super.count(Wraps.<User>lbQ().eq(User::getAccount, account)) > 0;
    }

    @Override
    @CacheEvict(cacheNames = CacheKey.USER, key = "#id")
    public void incrPasswordErrorNumById(Long id) {
        baseMapper.incrPasswordErrorNumById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resetPassErrorNum(Long id) {
        int count = baseMapper.resetPassErrorNum(id, LocalDateTime.now());
        String key = key(id);
        cacheChannel.evict(getRegion(), key);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User saveUser(User user) {
        user.setPassword(SecureUtil.md5(user.getPassword()));
        user.setPasswordErrorNum(0);
        super.save(user);
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reset(UserUpdatePasswordDTO data) {
        BizAssert.equals(data.getConfirmPassword(), data.getPassword(), "2次输入的密码不一致");
        String defPassword = SecureUtil.md5(data.getPassword());
        super.update(Wraps.<User>lbU()
                .set(User::getPassword, defPassword)
                .set(User::getPasswordErrorNum, 0L)
                .set(User::getPasswordErrorLastTime, null)
                .in(User::getId, data.getId())
        );
        cacheChannel.evict(getRegion(), key(data.getId()));

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User updateUser(User user) {
        // 不允许修改用户信息时修改密码，请单独调用修改密码接口
        user.setPassword(null);
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
        String[] userIdArray = ids.stream().map(this::key).toArray(String[]::new);
        cacheChannel.evict(CacheKey.USER_ROLE, userIdArray);
        cacheChannel.evict(CacheKey.USER_MENU, userIdArray);
        cacheChannel.evict(CacheKey.USER_RESOURCE, userIdArray);
        return removeByIds(ids);
    }

    @Override
    public Map<Serializable, Object> findUserByIds(Set<Serializable> ids) {
        List<User> list = findUser(ids);

        //key 是 用户id
        ImmutableMap<Serializable, Object> typeMap = MapHelper.uniqueIndex(list, User::getId, (user) -> user);
        return typeMap;
    }

    @Override
    public List<User> findUserById(List<Long> ids) {
        Set<Serializable> set = new HashSet<>();
        ids.forEach(set::add);
        return findUser(set);
    }

    @Override
    public List<User> findUser(Set<Serializable> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> idList = ids.stream().mapToLong(Convert::toLong).boxed().collect(Collectors.toList());


        List<User> list = null;
        if (idList.size() > 100) {
            LbqWrapper<User> query = Wraps.<User>lbQ()
                    .in(User::getId, idList)
                    .eq(User::getStatus, true);
            list = super.list(query);

            if (!list.isEmpty()) {
                list.forEach(user -> {
                    String menuKey = key(user.getId());
                    cacheChannel.set(getRegion(), menuKey, user);
                });
            }

        } else {
            list = idList.stream().map(this::getByIdCache)
                    .filter(Objects::nonNull).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public Map<Serializable, Object> findUserNameByIds(Set<Serializable> ids) {
        List<User> list = findUser(ids);

        //key 是 用户id
        ImmutableMap<Serializable, Object> typeMap = MapHelper.uniqueIndex(list, User::getId, User::getName);
        return typeMap;
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
            sysUser.setResources(resourceList.stream().map(Resource::getCode).distinct().collect(Collectors.toList()));
        }

        return sysUser;
    }

    @Override
    public List<Long> findAllUserId() {
        return super.list().stream().mapToLong(User::getId).boxed().collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initUser(User user) {
        this.saveUser(user);
        return userRoleService.initAdmin(user.getId());
    }
}
