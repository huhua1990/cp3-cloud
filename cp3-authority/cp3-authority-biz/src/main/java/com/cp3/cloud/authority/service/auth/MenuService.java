package com.cp3.cloud.authority.service.auth;

import com.cp3.cloud.authority.entity.auth.Menu;
import com.cp3.cloud.base.service.SuperCacheService;

import java.util.List;

/**
 * <p>
 * 业务接口
 * 菜单
 * </p>
 *
 * @author cp3
 * @date 2019-07-03
 */
public interface MenuService extends SuperCacheService<Menu> {

    /**
     * 根据ID删除
     *
     * @param ids
     * @return
     */
    boolean removeByIdWithCache(List<Long> ids);

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    boolean updateWithCache(Menu menu);

    /**
     * 保存菜单
     *
     * @param menu
     * @return
     */
    boolean saveWithCache(Menu menu);

    /**
     * 查询用户可用菜单
     *
     * @param group
     * @param userId
     * @return
     */
    List<Menu> findVisibleMenu(String group, Long userId);

}
