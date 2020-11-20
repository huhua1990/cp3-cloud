package com.cp3.cloud.authority.controller.common;


import com.cp3.cloud.authority.dto.common.LoginLogUpdateDTO;
import com.cp3.cloud.authority.entity.common.LoginLog;
import com.cp3.cloud.authority.service.common.LoginLogService;
import com.cp3.cloud.base.R;
import com.cp3.cloud.base.controller.SuperController;
import com.cp3.cloud.base.request.PageParams;
import com.cp3.cloud.database.mybatis.conditions.query.QueryWrap;
import com.cp3.cloud.log.annotation.SysLog;
import com.cp3.cloud.security.annotation.PreAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * 登录日志
 * </p>
 *
 * @author cp3
 * @date 2019-10-20
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/loginLog")
@Api(value = "LoginLog", tags = "登录日志")
@PreAuth(enabled = false, replace = "loginLog:")
public class LoginLogController extends SuperController<LoginLogService, Long, LoginLog, LoginLog, LoginLog, LoginLogUpdateDTO> {

    /**
     * 分页查询登录日志
     *
     * @param model  对象
     * @param params 分页查询参数
     * @return 查询结果
     */
    @Override
    public QueryWrap<LoginLog> handlerWrapper(LoginLog model, PageParams<LoginLog> params) {
        QueryWrap<LoginLog> wrapper = super.handlerWrapper(model, params);

        wrapper.lambda()
                // 忽略 Wraps.q(model); 时， account  和 requestIp 字段的默认查询规则，
                .ignore(LoginLog::setAccount)
                .ignore(LoginLog::setRequestIp)
                // 使用 自定义的查询规则
                .likeRight(LoginLog::getAccount, model.getAccount())
                .likeRight(LoginLog::getRequestIp, model.getRequestIp());
        return wrapper;
    }

    @ApiOperation("清空日志")
    @DeleteMapping("clear")
    @SysLog("清空日志")
    public R<Boolean> clear(@RequestParam(required = false, defaultValue = "1") Integer type) {
        LocalDateTime clearBeforeTime = null;
        Integer clearBeforeNum = null;
        if (type == 1) {
            clearBeforeTime = LocalDateTime.now().plusMonths(-1);
        } else if (type == 2) {
            clearBeforeTime = LocalDateTime.now().plusMonths(-3);
        } else if (type == 3) {
            clearBeforeTime = LocalDateTime.now().plusMonths(-6);
        } else if (type == 4) {
            clearBeforeTime = LocalDateTime.now().plusMonths(-12);
        } else if (type == 5) {
            clearBeforeNum = 1000;        // 清理一千条以前日志数据
        } else if (type == 6) {
            clearBeforeNum = 10000;        // 清理一万条以前日志数据
        } else if (type == 7) {
            clearBeforeNum = 30000;        // 清理三万条以前日志数据
        } else if (type == 8) {
            clearBeforeNum = 100000;    // 清理十万条以前日志数据
        } else if (type == 9) {
            clearBeforeNum = null;            // 清理所有日志数据
        } else {
            return R.validFail("参数错误");
        }

        return success(baseService.clearLog(clearBeforeTime, clearBeforeNum));
    }

}
