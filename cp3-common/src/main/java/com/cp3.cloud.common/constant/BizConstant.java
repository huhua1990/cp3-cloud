package com.cp3.cloud.common.constant;

/**
 * 业务常量
 *
 * @author cp3
 * @date 2019/08/06
 */
public interface BizConstant {
    /**
     * 超级租户编码
     */
    String SUPER_TENANT = "admin";
    /**
     * 初始化的租户管理员角色
     */
    String INIT_ROLE_CODE = "PT_ADMIN";

    /**
     * 演示用的组织ID
     */
    Long DEMO_ORG_ID = 101L;
    /**
     * 角色前缀
     */
    String ROLE_PREFIX = "ROLE_";
    /**
     * 演示用的岗位ID
     */
    Long DEMO_STATION_ID = 101L;

    /**
     * 默认MD5密码：123456
     */
    String DEF_PASSWORD_MD5 = "e10adc3949ba59abbe56e057f20f883e";
    /**
     * 默认密码：123456
     */
    String DEF_PASSWORD = "123456";

    /**
     * 默认的定时任务组
     */
    String DEF_JOB_GROUP_NAME = "zuihou-jobs-server";
    /**
     * 短信发送处理器
     */
    String SMS_SEND_JOB_HANDLER = "smsSendJobHandler";

    /**
     * 基础库
     */
    String BASE_DATABASE = "zuihou_base";
    /**
     * 扩展库
     */
    String EXTEND_DATABASE = "zuihou_extend";

    /**
     * 被T
     */
    String LOGIN_STATUS = "T";

    String AUTHORITY = "zuihou-authority-server";
    String FILE = "zuihou-file-server";
    String MSGS = "zuihou-msgs-server";
    String OAUTH = "zuihou-oauth-server";
    String GATE = "zuihou-gateway-server";
    String ORDER = "zuihou-order-server";
    String DEMO = "zuihou-demo-server";

    /**
     * 初始化数据源时json的参数，
     * method 的可选值为 {INIT_DS_PARAM_METHOD_INIT} 和 {INIT_DS_PARAM_METHOD_REMOVE}
     */
    String INIT_DS_PARAM_METHOD = "method";
    /**
     * 初始化数据源时json的参数，
     * tenant 的值为 需要初始化的租户编码
     */
    String INIT_DS_PARAM_TENANT = "tenant";
    /**
     * 初始化数据源时，需要执行的方法
     * init 表示初始化数据源
     * remove 表示删除数据源
     */
    String INIT_DS_PARAM_METHOD_INIT = "init";
    /**
     * 初始化数据源时，需要执行的方法
     * init 表示初始化数据源
     * remove 表示删除数据源
     */
    String INIT_DS_PARAM_METHOD_REMOVE = "remove";
}
