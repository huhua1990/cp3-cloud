package com.cp3.cloud.oauth.controller;

import cn.hutool.core.bean.BeanUtil;
import com.cp3.cloud.authority.dto.auth.RouterMeta;
import com.cp3.cloud.authority.dto.auth.VueRouter;
import com.cp3.cloud.authority.entity.auth.Menu;
import com.cp3.cloud.authority.service.auth.MenuService;
import com.cp3.cloud.base.R;
import com.cp3.cloud.security.annotation.LoginUser;
import com.cp3.cloud.security.model.SysUser;
import com.cp3.cloud.utils.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * 菜单
 * </p>
 *
 * @author cp3
 * @date 2019-07-22
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
@Api(value = "Menu", tags = "菜单")
public class OauthMenuController {

    //@Autowired
    //private DozerUtils dozer;
    @Autowired
    private MenuService menuService;

    /**
     * 查询用户可用的所有资源
     *
     * @param group  分组 <br>
     * @param userId 指定用户id
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "group", value = "菜单组", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "long", paramType = "query"),
    })
    @ApiOperation(value = "查询用户可用的所有菜单", notes = "查询用户可用的所有菜单")
    @GetMapping("/menus")
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
            @ApiImplicitParam(name = "group", value = "菜单组", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "long", paramType = "query"),
    })
    @ApiOperation(value = "查询用户可用的所有菜单路由树", notes = "查询用户可用的所有菜单路由树")
    @GetMapping("/router")
    public R<List<VueRouter>> myRouter(@RequestParam(value = "group", required = false) String group,
                                       @RequestParam(value = "userId", required = false) Long userId,
                                       @ApiIgnore @LoginUser SysUser sysUser) {
        if (userId == null || userId <= 0) {
            userId = sysUser.getId();
        }
        List<Menu> list = menuService.findVisibleMenu(group, userId);
        //TODO cp3
        List<VueRouter> treeList = new ArrayList<>();
        //List<VueRouter> treeList = dozer.mapList(list, VueRouter.class);
        return R.success(TreeUtil.buildTree(treeList));
    }

    /**
     * 超管的路由菜单写死，后期在考虑配置在
     *
     * @return
     */
    @ApiOperation(value = "查询超管菜单路由树", notes = "查询超管菜单路由树")
    @GetMapping("/admin/router")
    public R<List<VueRouter>> adminRouter() {
        return R.success(buildSuperAdminRouter());
    }

    private List<VueRouter> buildSuperAdminRouter() {
        List<VueRouter> tree = new ArrayList<>();
        List<VueRouter> children = new ArrayList<>();

        VueRouter datasourceConfig = new VueRouter();
        datasourceConfig.setPath("/defaults/datasourceconfig");
        datasourceConfig.setComponent("zuihou/defaults/datasourceconfig/Index");
        datasourceConfig.setName("数据源(联系群主开通)");
        datasourceConfig.setHidden(false);
        datasourceConfig.setMeta(RouterMeta.builder()
                .title("数据源(联系群主开通)").breadcrumb(true).icon("")
                .build());
        datasourceConfig.setId(-4L);
        datasourceConfig.setParentId(-1L);
        children.add(datasourceConfig);

        VueRouter tenant = new VueRouter();
        tenant.setPath("/defaults/tenant");
        tenant.setComponent("zuihou/defaults/tenant/Index");
        tenant.setHidden(false);
        // 没有name ，刷新页面后，切换菜单会报错：
        // [Vue warn]: Error in nextTick: "TypeError: undefined is not iterable (cannot read property Symbol(Symbol.iterator))"
        // found in
        // <TagsView> at src/layout/components/TagsView/index.vue
        tenant.setName("租户管理");
        tenant.setAlwaysShow(true);
        tenant.setMeta(RouterMeta.builder()
                .title("租户管理").breadcrumb(true).icon("")
                .build());
        tenant.setId(-2L);
        tenant.setParentId(-1L);
        children.add(tenant);

        VueRouter globalUser = new VueRouter();
        globalUser.setPath("/defaults/globaluser");
        globalUser.setComponent("zuihou/defaults/globaluser/Index");
        globalUser.setName("运营用户");
        globalUser.setHidden(false);
        globalUser.setMeta(RouterMeta.builder()
                .title("运营用户").breadcrumb(true).icon("")
                .build());
        globalUser.setId(-3L);
        globalUser.setParentId(-1L);
        children.add(globalUser);

        VueRouter defaults = new VueRouter();
        defaults.setPath("/defaults");
        defaults.setComponent("Layout");
        defaults.setHidden(false);
        defaults.setName("系统设置");
        defaults.setAlwaysShow(true);
        defaults.setMeta(RouterMeta.builder()
                .title("系统设置").icon("el-icon-coin").breadcrumb(true)
                .build());
        defaults.setId(-1L);
        defaults.setChildren(children);

        tree.add(defaults);
        return tree;
    }

}
