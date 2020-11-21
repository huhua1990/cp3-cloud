package com.cp3.cloud.authority.service.auth.impl;


import com.cp3.cloud.authority.dao.auth.ResourceMapper;
import com.cp3.cloud.authority.dto.auth.ResourceQueryDTO;
import com.cp3.cloud.authority.entity.auth.Resource;
import com.cp3.cloud.authority.service.auth.ResourceService;
import com.cp3.cloud.authority.service.auth.RoleAuthorityService;
import com.cp3.cloud.base.service.SuperCacheServiceImpl;
import com.cp3.cloud.common.constant.CacheKey;
import com.cp3.cloud.database.mybatis.conditions.Wraps;
import com.cp3.cloud.exception.BizException;
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

import static com.cp3.cloud.common.constant.CacheKey.RESOURCE;

/**
 * <p>
 * 业务实现类
 * 资源
 * </p>
 *
 * @author cp3
 * @date 2019-07-03
 */
@Slf4j
@Service
public class ResourceServiceImpl extends SuperCacheServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private CodeGenerate codeGenerate;
    @Autowired
    private RoleAuthorityService roleAuthorityService;

    @Override
    protected String getRegion() {
        return RESOURCE;
    }

    /**
     * 查询用户的可用资源
     *
     * 注意：什么地方需要清除 USER_MENU 缓存
     * 给用户重新分配角色时， 角色重新分配资源/菜单时
     *
     * @param resource
     * @return
     */
    @Override
    public List<Resource> findVisibleResource(ResourceQueryDTO resource) {
        //1, 先查 cache，cache中没有就执行回调查询DB，并设置到缓存
        String userResourceKey = key(resource.getUserId());

        List<Resource> visibleResource = new ArrayList<>();
        CacheObject cacheObject = cacheChannel.get(CacheKey.USER_RESOURCE, userResourceKey, (key) -> {
            visibleResource.addAll(baseMapper.findVisibleResource(resource));
            return visibleResource.stream().mapToLong(Resource::getId).boxed().collect(Collectors.toList());
        });

        //cache 和 db 都没有时直接返回
        if (cacheObject.getValue() == null) {
            return Collections.emptyList();
        }

        if (!visibleResource.isEmpty()) {
            visibleResource.forEach((r) -> {
                String menuKey = key(r.getId());
                cacheChannel.set(RESOURCE, menuKey, r);
            });
            return resourceListFilterGroup(resource.getMenuId(), visibleResource);
        }

        // 若list里面的值过多，而资源又均没有缓存（或者缓存穿透），则这里的效率并不高

        List<Long> list = (List<Long>) cacheObject.getValue();
        List<Resource> resourceList = list.stream().map(this::getByIdCache).filter(Objects::nonNull).collect(Collectors.toList());

        if (resource.getMenuId() == null) {
            return resourceList;
        }

        // 根据查询条件过滤数据
        return resourceListFilterGroup(resource.getMenuId(), visibleResource);
    }

    private List<Resource> resourceListFilterGroup(Long menuId, List<Resource> visibleResource) {
        if (menuId == null) {
            return visibleResource;
        }
        return visibleResource.stream().filter((item) -> Objects.equals(menuId, item.getMenuId())).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIdWithCache(List<Long> ids) {
        boolean result = this.removeByIds(ids);

        // 删除角色的权限
        roleAuthorityService.removeByAuthorityId(ids);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByMenuIdWithCache(List<Long> menuIds) {
        List<Resource> resources = super.list(Wraps.<Resource>lbQ().in(Resource::getMenuId, menuIds));
        if (resources.isEmpty()) {
            return;
        }
        List<Long> idList = resources.stream().mapToLong(Resource::getId).boxed().collect(Collectors.toList());
        this.removeByIds(idList);

        // 删除角色的权限
        roleAuthorityService.removeByAuthorityId(menuIds);

        String[] keys = idList.stream().map(this::key).toArray(String[]::new);
        cacheChannel.evict(CacheKey.RESOURCE, keys);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithCache(Resource resource) {
        resource.setCode(StrHelper.getOrDef(resource.getCode(), codeGenerate.next()));
        if (super.count(Wraps.<Resource>lbQ().eq(Resource::getCode, resource.getCode())) > 0) {
            throw BizException.validFail("编码[%s]重复", resource.getCode());
        }

        this.save(resource);
        String resourceKey = key(resource.getId());
        cacheChannel.set(CacheKey.RESOURCE, resourceKey, resource);
        return true;
    }

    @Override
    public List<Long> findMenuIdByResourceId(List<Long> resourceIdList) {
        return baseMapper.findMenuIdByResourceId(resourceIdList);
    }
}
