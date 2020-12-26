package com.cp3.cloud.authority.controller.auth;


import com.cp3.base.annotation.log.SysLog;
import com.cp3.base.basic.R;
import com.cp3.base.database.mybatis.conditions.Wraps;
import com.cp3.cloud.authority.entity.auth.RoleAuthority;
import com.cp3.cloud.authority.service.auth.RoleAuthorityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * 角色的资源
 * </p>
 *
 * @author zuihou
 * @date 2019-07-22
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/roleAuthority")
@Api(value = "RoleAuthority", tags = "角色的资源")
@RequiredArgsConstructor
public class RoleAuthorityController {

    private final RoleAuthorityService roleAuthorityService;

    /**
     * 查询指定角色关联的菜单和资源
     *
     * @param roleId 角色id
     * @return 查询结果
     */
    @ApiOperation(value = "查询指定角色关联的菜单和资源", notes = "查询指定角色关联的菜单和资源")
    @GetMapping("/{roleId}")
    @SysLog(value = "'查询指定角色关联的菜单和资源", response = false)
    public R<List<RoleAuthority>> queryByRoleId(@PathVariable Long roleId) {
        return R.success(roleAuthorityService.list(Wraps.<RoleAuthority>lbQ().eq(RoleAuthority::getRoleId, roleId)));
    }


}
