package com.cp3.cloud.oauth.controller;

import cn.hutool.core.util.ObjectUtil;
import com.cp3.base.annotation.user.LoginUser;
import com.cp3.base.basic.R;
import com.cp3.base.dozer.DozerUtils;
import com.cp3.base.security.model.SysUser;
import com.cp3.base.security.properties.SecurityProperties;
import com.cp3.base.utils.CollHelper;
import com.cp3.base.utils.StrPool;
import com.cp3.base.utils.TreeUtil;
import com.cp3.cloud.authority.dto.auth.AuthorityResourceDTO;
import com.cp3.cloud.authority.dto.auth.ResourceQueryDTO;
import com.cp3.cloud.authority.dto.auth.VueRouter;
import com.cp3.cloud.authority.entity.auth.Menu;
import com.cp3.cloud.authority.entity.auth.Resource;
import com.cp3.cloud.authority.entity.auth.Role;
import com.cp3.cloud.authority.service.auth.MenuService;
import com.cp3.cloud.authority.service.auth.ResourceService;
import com.cp3.cloud.authority.service.auth.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

import static com.cp3.cloud.common.constant.SwaggerConstants.DATA_TYPE_LONG;
import static com.cp3.cloud.common.constant.SwaggerConstants.DATA_TYPE_STRING;
import static com.cp3.cloud.common.constant.SwaggerConstants.PARAM_TYPE_QUERY;


/**
 * <p>
 * 前端控制器
 * 资源 角色 菜单
 * </p>
 *
 * @author zuihou
 * @date 2019-07-22
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(value = "OauthMenuResource", tags = "资源")
public class OauthMenuResourceController {
    private final DozerUtils dozer;
    private final ResourceService resourceService;
    private final MenuService menuService;
    private final RoleService roleService;
    private final SecurityProperties securityProperties;

    /**
     * 查询用户可用的所有资源
     *
     * @param resource <br>
     *                 menuId 菜单 <br>
     *                 userId 当前登录人id
     */
    @ApiOperation(value = "查询用户可用的所有资源", notes = "查询用户可用的所有资源")
    @GetMapping("/resource/visible")
    public R<AuthorityResourceDTO> visible(ResourceQueryDTO resource, @ApiIgnore @LoginUser SysUser sysUser) {
        if (resource == null) {
            resource = new ResourceQueryDTO();
        }

        if (resource.getUserId() == null) {
            resource.setUserId(sysUser.getId());
        }
        List<Resource> resourceList = resourceService.findVisibleResource(resource);
        List<Role> roleList = roleService.findRoleByUserId(resource.getUserId());
        return R.success(AuthorityResourceDTO.builder()
                .roleList(roleList.parallelStream().filter(ObjectUtil::isNotEmpty).map(Role::getCode).distinct().collect(Collectors.toList()))
                .resourceList(CollHelper.split(resourceList, Resource::getCode, StrPool.SEMICOLON))
                .caseSensitive(securityProperties.getCaseSensitive())
                .enabled(securityProperties.getEnabled())
                .build());
    }

    /**
     * 查询用户可用的所有菜单
     *
     * @param group  分组 <br>
     * @param userId 指定用户id
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group", value = "菜单组", dataType = DATA_TYPE_STRING, paramType = PARAM_TYPE_QUERY),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = DATA_TYPE_LONG, paramType = PARAM_TYPE_QUERY),
    })
    @ApiOperation(value = "查询用户可用的所有菜单", notes = "查询用户可用的所有菜单")
    @GetMapping("/menu/menus")
    public R<List<Menu>> myMenus(@RequestParam(value = "group", required = false) String group,
                                 @RequestParam(value = "userId", required = false) Long userId,
                                 @ApiIgnore @LoginUser SysUser sysUser) {
        if (userId == null || userId <= 0) {
            userId = sysUser.getId();
        }
        List<Menu> list = menuService.findVisibleMenu(group, userId);
        List<Menu> tree = TreeUtil.buildTree(list);
        return R.success(tree);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "group", value = "菜单组", dataType = DATA_TYPE_STRING, paramType = PARAM_TYPE_QUERY),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = DATA_TYPE_LONG, paramType = PARAM_TYPE_QUERY),
    })
    @ApiOperation(value = "查询用户可用的所有菜单路由树", notes = "查询用户可用的所有菜单路由树")
    @GetMapping("/menu/router")
    public R<List<VueRouter>> myRouter(@RequestParam(value = "group", required = false) String group,
                                       @RequestParam(value = "userId", required = false) Long userId,
                                       @ApiIgnore @LoginUser SysUser sysUser) {
        if (userId == null || userId <= 0) {
            userId = sysUser.getId();
        }
        List<Menu> list = menuService.findVisibleMenu(group, userId);
        List<VueRouter> treeList = dozer.mapList(list, VueRouter.class);
        return R.success(TreeUtil.buildTree(treeList));
    }

}
