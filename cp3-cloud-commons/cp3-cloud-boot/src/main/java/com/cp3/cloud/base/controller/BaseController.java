package com.cp3.cloud.base.controller;

import com.cp3.cloud.base.R;
import com.cp3.cloud.base.service.SuperService;
import com.cp3.cloud.context.BaseContextHandler;
import com.cp3.cloud.exception.BizException;
import com.cp3.cloud.exception.code.BaseExceptionCode;

/**
 * 基础接口
 *
 * @param <Entity> 实体
 * @author cp3
 * @date 2020年03月07日21:56:32
 */
public interface BaseController<Entity> {

    /**
     * 获取实体的类型
     *
     * @return
     */
    Class<Entity> getEntityClass();

    /**
     * 获取Service
     *
     * @return
     */
    SuperService<Entity> getBaseService();


    /**
     * 成功返回
     *
     * @param data 返回内容
     * @param <T>  返回类型
     * @return R
     */
    default <T> R<T> success(T data) {
        return R.success(data);
    }

    /**
     * 成功返回
     *
     * @return R.true
     */
    default R<Boolean> success() {
        return R.success();
    }

    /**
     * 失败返回
     *
     * @param msg 失败消息
     * @param <T> 返回类型
     * @return
     */
    default <T> R<T> fail(String msg) {
        return R.fail(msg);
    }

    /**
     * 失败返回
     *
     * @param msg  失败消息
     * @param args 动态参数
     * @param <T>  返回类型
     * @return
     */
    default <T> R<T> fail(String msg, Object... args) {
        return R.fail(msg, args);
    }

    /**
     * 失败返回
     *
     * @param code 失败编码
     * @param msg  失败消息
     * @param <T>  返回类型
     * @return
     */
    default <T> R<T> fail(int code, String msg) {
        return R.fail(code, msg);
    }

    /**
     * 失败返回
     *
     * @param exceptionCode 失败异常码
     * @param <T>
     * @return
     */
    default <T> R<T> fail(BaseExceptionCode exceptionCode) {
        return R.fail(exceptionCode);
    }

    /**
     * 失败返回
     *
     * @param exception 异常
     * @param <T>
     * @return
     */
    default <T> R<T> fail(BizException exception) {
        return R.fail(exception);
    }

    /**
     * 失败返回
     *
     * @param throwable 异常
     * @param <T>
     * @return
     */
    default <T> R<T> fail(Throwable throwable) {
        return R.fail(throwable);
    }

    /**
     * 参数校验失败返回
     *
     * @param msg 错误消息
     * @param <T>
     * @return
     */
    default <T> R<T> validFail(String msg) {
        return R.validFail(msg);
    }

    /**
     * 参数校验失败返回
     *
     * @param msg  错误消息
     * @param args 错误参数
     * @param <T>
     * @return
     */
    default <T> R<T> validFail(String msg, Object... args) {
        return R.validFail(msg, args);
    }

    /**
     * 参数校验失败返回
     *
     * @param exceptionCode 错误编码
     * @param <T>
     * @return
     */
    default <T> R<T> validFail(BaseExceptionCode exceptionCode) {
        return R.validFail(exceptionCode);
    }

    /**
     * 获取当前id
     *
     * @return userId
     */
    default Long getUserId() {
        return BaseContextHandler.getUserId();
    }

    /**
     * 当前请求租户
     *
     * @return 租户编码
     */
    default String getTenant() {
        return BaseContextHandler.getTenant();
    }

    /**
     * 登录人账号
     *
     * @return 账号
     */
    default String getAccount() {
        return BaseContextHandler.getAccount();
    }

    /**
     * 登录人姓名
     *
     * @return 姓名
     */
    default String getName() {
        return BaseContextHandler.getName();
    }

}
