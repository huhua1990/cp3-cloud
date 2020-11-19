package com.cp3.cloud.common.constant;

import cn.hutool.core.util.StrUtil;
import com.cp3.cloud.context.BaseContextHandler;
import com.cp3.cloud.utils.StrPool;

/**
 * 用于同一管理和生成缓存的key， 避免多个项目使用的key重复
 * <p>
 * 使用@Cacheable时， 其value值一定要在此处指定
 * <p>
 * 3.0.0 废弃本类
 *
 * @author cp3
 * @date 2019/08/06
 * @see com.cp3.cloud.common.cache.CacheKeyDefinition
 */
@Deprecated
public interface CacheKey {
    // 权限系统缓存 start

    /**
     * 验证码 前缀
     * 完整key: captcha:{key} -> str
     */
    String CAPTCHA = "captcha";
    /**
     * token 前缀
     * 完整key： token:{token} -> userid
     */
    String TOKEN = "token";


    /**
     * 菜单 前缀
     * 完整key: menu:{menuId} -> obj
     */
    String MENU = "menu";
    /**
     * 组织 前缀
     * 完整key: station:{stationId} -> obj
     */
    String ORG = "org";
    /**
     * 岗位 前缀
     * 完整key: station:{stationId} -> obj
     */
    String STATION = "station";

    /**
     * 资源 前缀
     * 完整key: resource:{resourceId} -> obj
     */
    String RESOURCE = "resource";

    /**
     * 角色 前缀
     * 完整key: role:{roleId}
     */
    String ROLE = "role";
    /**
     * 角色拥有那些菜单 前缀
     * 完整key: role_menu:{ROLE_ID} -> [MENU_ID, MENU_ID, ...]
     */
    String ROLE_MENU = "role_menu";
    /**
     * 角色拥有那些资源 前缀
     * 完整key: role_resource:{ROLE_ID} -> [RESOURCE_ID, ...]
     */
    String ROLE_RESOURCE = "role_resource";
//    /**
//     * 角色拥有那些组织 前缀
//     * 完整key: role_org:{ROLE_ID} -> [ORG_ID, ...]
//     */
//    String ROLE_ORG = "role_org";

    /**
     * 用户 前缀
     * 完整key: user:{USER_ID} -> obj
     */
    String USER = "user";
    /**
     * 用户 前缀
     * 完整key: user_account:{account} -> id
     */
    String USER_ACCOUNT = "user_account";

    /**
     * 用户拥有那些角色 前缀
     * 完整key: user_role:{USER_ID} -> [ROLE_ID, ...]
     */
    String USER_ROLE = "user_role";
    /**
     * 用户拥有的菜单 前缀
     * 完整key: user_menu:{userId} -> [MENU_ID, MENU_ID, ...]
     */
    String USER_MENU = "user_menu";
    /**
     * 用户拥有的资源 前缀
     * 完整key: user_resource:{userId} -> [RESOURCE_ID, ...]
     */
    String USER_RESOURCE = "user_resource";


    /**
     * 系统URI 前缀
     * <p>
     * 完整key: system_api:{id} -> [ID, ...]
     */
    String SYSTEM_API = "system_api";

    /**
     * 登录总次数
     * login_log_total:{TENANT} -> Long
     */
    String LOGIN_LOG_TOTAL = "login_log_total";
    /**
     * 今日登录总次数
     * login_log_today:{TENANT}:{today} -> Long
     */
    String LOGIN_LOG_TODAY = "login_log_today";
    /**
     * 今日登录总ip
     * login_log_todayip:{TENANT}:{today} -> Map
     */
    String LOGIN_LOG_TODAY_IP = "login_log_todayip";
    /**
     * 最近10访问记录
     * login_log_tenday:{TENANT}:{today}:{account} -> Map
     */
    String LOGIN_LOG_TEN_DAY = "login_log_tenday";
    /**
     * 登录总次数
     * login_log_browser:{TENANT} -> Map
     */
    String LOGIN_LOG_BROWSER = "login_log_browser";
    /**
     * 登录总次数
     * login_log_system{TENANT} -> Map
     */
    String LOGIN_LOG_SYSTEM = "login_log_system";

    /**
     * 地区 前缀
     * 完整key: area:{id} -> obj
     */
    String AREA = "area";
    /**
     * 所有地区 前缀
     * 完整key: area_all -> [AREA_ID]
     */
    String AREA_ALL = "area_all";

    /**
     * 字典 前缀
     * 完整key: dictionary_item:{id} -> obj
     */
    String DICTIONARY_ITEM = "dictionary_item";

    /**
     * 参数 前缀
     * 完整key: parameter:{id} -> obj
     */
    String PARAMETER = "parameter";
    /**
     * 应用 前缀
     * 完整key: application:{id} -> obj
     */
    String APPLICATION = "application";
    /**
     * 应用 前缀
     * 完整key: application:{clientId}:{clientSecret} -> id
     */
    String APPLICATION_CLIENT = "application_client";
//    /**
//     * 用户登录的客户端 前缀： 用于记录用户在那几个设备上登录了
//     * 完整key: user_login_client:{userid} -> [client, client, ...] (Set)
//     */
//    String USER_LOGIN_CLIENT = "user_login_client";

    /**
     * 用户客户端token 前缀
     * 完整key: user_client_token:{userid}:{client} -> token (String)
     */
    String USER_CLIENT_TOKEN = "user_client_token";

    /**
     * 用户token 前缀
     * 完整key: user_token:{userid} -> token (String)
     */
    String USER_TOKEN = "user_token";

    /**
     * 用户token 前缀
     * 完整key: token_user_id:{token} -> userid (Long)
     */
    String TOKEN_USER_ID = "token_user_id";


    /**
     * 租户 前缀
     * 完整key: tenant:{id} -> obj
     */
    String TENANT = "tenant";
    /**
     * 租户 前缀
     * 完整key: tenant_name:{name} -> id
     */
    String TENANT_NAME = "tenant_name";
    // 权限系统缓存 end


    // 消息服务缓存 start
    /**
     * 用户注册 前缀
     * 完整key: register:{注册类型}:{手机号}
     */
    String REGISTER_USER = "register";
    // 消息服务缓存 end


    /**
     * 阻止列表
     */
    String BLOCKLIST_ID = "gateway:blocklist:id";
    String BLOCKLIST = "gateway:blocklist";
    /**
     * 限流
     */
    String RATE_LIMITER_ID = "gateway:ratelimiter:id";
    String RATE_LIMITER = "gateway:ratelimiter";

    /**
     * 构建key
     *
     * @param args
     * @return
     */
    static String buildTenantKey(Object... args) {
        if (args.length > 0) {
            return StrUtil.join(StrPool.COLON, BaseContextHandler.getTenant(), args);
        } else {
            return BaseContextHandler.getTenant();
        }
    }

    /**
     * 构建没有租户信息的key
     *
     * @param args
     * @return
     */
    static String buildKey(Object... args) {
        if (args.length == 1) {
            return String.valueOf(args[0]);
        } else if (args.length > 0) {
            return StrUtil.join(StrPool.COLON, args);
        } else {
            return "";
        }
    }
}
