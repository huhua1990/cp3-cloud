package com.cp3.cloud.authority.service.auth.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

import com.cp3.base.basic.service.SuperCacheServiceImpl;
import com.cp3.base.cache.model.CacheKey;
import com.cp3.base.cache.model.CacheKeyBuilder;
import com.cp3.base.utils.BizAssert;
import com.cp3.base.utils.DefValueHelper;
import com.cp3.cloud.authority.dao.auth.MenuMapper;
import com.cp3.cloud.authority.entity.auth.Menu;
import com.cp3.cloud.authority.service.auth.MenuService;
import com.cp3.cloud.authority.service.auth.ResourceService;
import com.cp3.cloud.authority.service.auth.UserService;
import com.cp3.cloud.common.cache.auth.MenuCacheKeyBuilder;
import com.cp3.cloud.common.cache.auth.UserMenuCacheKeyBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cp3.base.utils.StrPool.DEF_PARENT_ID;

/**
 * <p>
 * 业务实现类
 * 菜单
 * </p>
 *
 * @author zuihou
 * @date 2019-07-03
 */
@Slf4j
@Service

@RequiredArgsConstructor
public class MenuServiceImpl extends SuperCacheServiceImpl<MenuMapper, Menu> implements MenuService {

    private final ResourceService resourceService;
    private final UserService userService;

    @Override
    protected CacheKeyBuilder cacheKeyBuilder() {
        return new MenuCacheKeyBuilder();
    }

    /**
     * 查询用户可用菜单
     * 1，查询缓存中存放的当前用户拥有的所有菜单列表 [menuId,menuId...]
     * 2，缓存&DB为空则返回
     * 3，缓存总用户菜单列表 为空，但db存在，则将list便利set到缓存，并直接返回
     * 4，缓存存在用户菜单列表，则根据菜单id遍历去缓存查询菜单。
     * 5，过滤group后，返回
     *
     * <p>
     * 注意：什么地方需要清除 USER_MENU 缓存
     * 给用户重新分配角色时， 角色重新分配资源/菜单时
     *
     * @param group  分组
     * @param userId 用户id
     * @return 指定用户的拥有的菜单
     */
    @Override
    public List<Menu> findVisibleMenu(String group, Long userId) {
        CacheKey userMenuKey = new UserMenuCacheKeyBuilder().key(userId);
        List<Menu> visibleMenu = new ArrayList<>();

        List<Long> list = cacheOps.get(userMenuKey, k -> {
            visibleMenu.addAll(baseMapper.findVisibleMenu(userId));
            return visibleMenu.stream().map(Menu::getId).collect(Collectors.toList());
        });

        if (!visibleMenu.isEmpty()) {
            visibleMenu.forEach(this::setCache);
        } else {
            visibleMenu.addAll(findByIds(list, this::listByIds));
        }
        return menuListFilterGroup(group, visibleMenu);
    }

    private List<Menu> menuListFilterGroup(String group, List<Menu> visibleMenu) {
        if (StrUtil.isEmpty(group)) {
            return visibleMenu;
        }
        return visibleMenu.stream().filter(menu -> group.equals(menu.getGroup())).collect(Collectors.toList());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIdWithCache(List<Long> ids) {
        if (ids.isEmpty()) {
            return true;
        }
        boolean result = this.removeByIds(ids);
        if (result) {
            resourceService.removeByMenuIdWithCache(ids);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWithCache(Menu menu) {
        Menu old = getById(menu);
        BizAssert.notNull(old, "菜单不存在");

        Boolean oldIsPublic = DefValueHelper.getOrDef(old.getIsGeneral(), false);
        Boolean newIsPublic = DefValueHelper.getOrDef(menu.getIsGeneral(), false);
        if (!oldIsPublic.equals(newIsPublic)) {
            List<Long> userIds = userService.findAllUserId();
            cacheOps.del(userIds.stream().map(new UserMenuCacheKeyBuilder()::key).toArray(CacheKey[]::new));
        }

        return this.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithCache(Menu menu) {
        menu.setState(Convert.toBool(menu.getState(), true));
        menu.setIsGeneral(Convert.toBool(menu.getIsGeneral(), false));
        menu.setParentId(Convert.toLong(menu.getParentId(), DEF_PARENT_ID));
        save(menu);

        if (menu.getIsGeneral()) {
            List<Long> userIds = userService.findAllUserId();
            cacheOps.del(userIds.stream().map(new UserMenuCacheKeyBuilder()::key).toArray(CacheKey[]::new));
        }
        return true;
    }
}
