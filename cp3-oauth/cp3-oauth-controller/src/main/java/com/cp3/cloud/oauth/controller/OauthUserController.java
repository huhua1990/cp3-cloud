package com.cp3.cloud.oauth.controller;

import com.cp3.cloud.authority.service.auth.UserService;
import com.cp3.cloud.base.R;
import com.cp3.cloud.security.feign.UserQuery;
import com.cp3.cloud.security.model.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * 用户
 * </p>
 *
 * @author cp3
 * @date 2019-07-22
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@Api(value = "User", tags = "用户")
public class OauthUserController {
    @Autowired
    private UserService userService;

    /**
     * 单体查询用户
     *
     * @param id 主键id
     * @return 查询结果
     */
    @ApiOperation(value = "查询用户详细", notes = "查询用户详细")
    @PostMapping(value = "/anno/id/{id}")
    public R<SysUser> getById(@PathVariable Long id, @RequestBody UserQuery query) {
        return R.success(userService.getSysUserById(id, query));
    }

    /**
     * 根据用户id，查询用户权限范围
     *
     * @param id 用户id
     * @return
     */
    @ApiOperation(value = "查询用户权限范围", notes = "根据用户id，查询用户权限范围")
    @GetMapping(value = "/ds/{id}")
    public Map<String, Object> getDataScopeById(@PathVariable("id") Long id) {
        return userService.getDataScopeById(id);
    }

    /**
     * 调用方传递的参数类型是 Set<Serializable> ，但接收方必须指定为Long类型（实体的主键类型），否则在调用mp提供的方法时，会使得mysql出现类型隐式转换问题。
     * 问题如下: select * from org where id in ('100');
     * <p>
     * 强制转换成Long后，sql就能正常执行: select * from org where id in (100);
     *
     * <p>
     * 接口和实现类的类型不一致，但也能调用，归功于 SpingBoot 的自动转换功能
     * {@link com.cp3.cloud.oauth.api.UserApi#findUserByIds} 方法的实现类
     *
     * @param ids id
     * @return
     */
    @ApiOperation(value = "根据id查询用户", notes = "根据id查询用户")
    @GetMapping("/findUserByIds")
    public Map<Serializable, Object> findUserByIds(@RequestParam(value = "ids") Set<Serializable> ids) {
        return userService.findUserByIds(ids);
    }

    /**
     * 调用方传递的参数类型是 Set<Serializable> ，但接收方必须指定为Long类型（实体的主键类型），否则在调用mp提供的方法时，会使得mysql出现类型隐式转换问题。
     * 问题如下: select * from org where id in ('100');
     * <p>
     * 强制转换成Long后，sql就能正常执行: select * from org where id in (100);
     *
     * <p>
     * 接口和实现类的类型不一致，但也能调用，归功于 SpingBoot 的自动转换功能
     * {@link com.cp3.cloud.oauth.api.UserApi#findUserNameByIds} 方法的实现类
     *
     * @param ids id
     * @return
     */
    @ApiOperation(value = "根据id查询用户名称", notes = "根据id查询用户名称")
    @GetMapping("/findUserNameByIds")
    public Map<Serializable, Object> findUserNameByIds(@RequestParam(value = "ids") Set<Serializable> ids) {
        return userService.findUserNameByIds(ids);
    }

}
