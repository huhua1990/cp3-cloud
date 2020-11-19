package com.cp3.cloud.zuul.filter.pre;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.cp3.cloud.base.R;
import com.cp3.cloud.common.constant.BizConstant;
import com.cp3.cloud.common.constant.CacheKey;
import com.cp3.cloud.common.properties.IgnoreProperties;
import com.cp3.cloud.context.BaseContextConstants;
import com.cp3.cloud.context.BaseContextHandler;
import com.cp3.cloud.exception.BizException;
import com.cp3.cloud.jwt.TokenUtil;
import com.cp3.cloud.jwt.model.AuthInfo;
import com.cp3.cloud.jwt.utils.JwtUtil;
import com.cp3.cloud.zuul.filter.BaseFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.cp3.cloud.context.BaseContextConstants.BASIC_HEADER_KEY;
import static com.cp3.cloud.context.BaseContextConstants.BEARER_HEADER_KEY;
import static com.cp3.cloud.context.BaseContextConstants.JWT_KEY_CLIENT_ID;
import static com.cp3.cloud.context.BaseContextConstants.JWT_KEY_TENANT;
import static com.cp3.cloud.exception.code.ExceptionCode.JWT_OFFLINE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 解析token中的用户信息 过滤器
 *
 * @author cp3
 * @createTime 2017-12-13 15:22
 */
@Component
@Slf4j
@EnableConfigurationProperties({IgnoreProperties.class})
public class TokenContextFilter extends BaseFilter {
    @Autowired
    private TokenUtil tokenUtil;
    @Value("${zuihou.database.multiTenantType:SCHEMA}")
    protected String multiTenantType;

    @Autowired
    private CacheChannel channel;

    /**
     * pre：可以在请求被路由之前调用
     * route：在路由请求时候被调用
     * post：在route和error过滤器之后被调用
     * error：处理请求时发生错误时被调用
     *
     * @return
     */
    @Override
    public String filterType() {
        // 前置过滤器
        return PRE_TYPE;
    }

    /**
     * filterOrder：通过int值来定义过滤器的执行顺序
     *
     * @return
     */
    @Override
    public int filterOrder() {
        // 数字越大，优先级越低
        /**
         * 一定要在 {@link org.springframework.cloud.netflix.zuul.filters.pre.PreDecorationFilter} 过滤器之后执行，因为这个过滤器做了路由  而我们需要这个路由信息来鉴权
         * 这个过滤器会将很多我们鉴权需要的信息放置在请求上下文中。故一定要在此过滤器之后执行
         */
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    /**
     * 返回一个boolean类型来判断该过滤器是否要执行，所以通过此函数可实现过滤器的开关。在上例中，我们直接返回true，所以该过滤器总是生效
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的具体逻辑。需要注意，这里我们通过ctx.setSendZuulResponse(false)令zuul过滤该请求，
     * 不对其进行路由，然后通过ctx.setResponseStatusCode(200)设置了其返回的错误码
     *
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        BaseContextHandler.setGrayVersion(getHeader(BaseContextConstants.GRAY_VERSION, request));

        try {
            //1, 解码 请求头中的租户信息
            parseTenant(ctx, request);

            // 2,解码 Authorization 后面完善
            parseClient(ctx, request);

            // 解析token
            parseToken(ctx, request);
        } catch (BizException e) {
            errorResponse(e.getMessage(), e.getCode(), 200);
        } catch (Exception e) {
            errorResponse("验证token出错", R.FAIL_CODE, 200);
        }
        return null;
    }

    private boolean parseToken(RequestContext ctx, HttpServletRequest request) {

        // 忽略 token 认证的接口
        if (isIgnoreToken()) {
            log.debug("access filter not execute");
            return true;
        }

        //获取token， 解析，然后想信息放入 heade
        //3, 获取token
        String token = getHeader(BEARER_HEADER_KEY, request);

        AuthInfo authInfo = null;
        //添加测试环境的特殊token
        if (isDev(token)) {
            authInfo = new AuthInfo().setAccount("zuihou").setUserId(3L)
                    .setTokenType(BEARER_HEADER_KEY).setName("平台管理员");
        } else {
            authInfo = tokenUtil.getAuthInfo(token);

            // 5，验证 是否在其他设备登录或被挤下线
            String newToken = JwtUtil.getToken(token);
            String tokenKey = CacheKey.buildKey(newToken);
            CacheObject tokenCache = channel.get(CacheKey.TOKEN_USER_ID, tokenKey);
            if (tokenCache.getValue() == null) {
                // 为空就认为是没登录或者被T会有bug，该 bug 取决于登录成功后，异步调用UserTokenService.save 方法的延迟
            } else if (StrUtil.equals(BizConstant.LOGIN_STATUS, (String) tokenCache.getValue())) {
                errorResponse(JWT_OFFLINE.getMsg(), JWT_OFFLINE.getCode(), 200);
                return true;
            }
        }

        //6, 转换，将 token 解析出来的用户身份 和 解码后的tenant、Authorization 重新封装到请求头
        if (authInfo != null) {
            addHeader(ctx, BaseContextConstants.JWT_KEY_ACCOUNT, authInfo.getAccount());
            addHeader(ctx, BaseContextConstants.JWT_KEY_USER_ID, authInfo.getUserId());
            addHeader(ctx, BaseContextConstants.JWT_KEY_NAME, authInfo.getName());
            MDC.put(BaseContextConstants.JWT_KEY_USER_ID, String.valueOf(authInfo.getUserId()));
        }
        return false;
    }

    private void parseClient(RequestContext ctx, HttpServletRequest request) {
        String base64Authorization = getHeader(BASIC_HEADER_KEY, request);
        if (StrUtil.isNotEmpty(base64Authorization)) {
            String[] client = JwtUtil.getClient(base64Authorization);
            BaseContextHandler.setClientId(client[0]);
            addHeader(ctx, JWT_KEY_CLIENT_ID, BaseContextHandler.getClientId());
        }
    }

    private void parseTenant(RequestContext ctx, HttpServletRequest request) {
        // 判断是否忽略tenant
        if (isIgnoreTenant(request.getRequestURI())) {
            return;
        }

        String base64Tenant = getHeader(JWT_KEY_TENANT, request);
        if (StrUtil.isNotEmpty(base64Tenant)) {
            String tenant = JwtUtil.base64Decoder(base64Tenant);
            BaseContextHandler.setTenant(tenant);
            addHeader(ctx, BaseContextConstants.JWT_KEY_TENANT, BaseContextHandler.getTenant());
            MDC.put(BaseContextConstants.JWT_KEY_TENANT, BaseContextHandler.getTenant());
        }
    }

    /**
     * 忽略 租户编码
     *
     * @return
     */
    protected boolean isIgnoreTenant(String path) {
        return "NONE".equals(multiTenantType) || ignoreTokenProperties.isIgnoreTenant(path);
    }

    private void addHeader(RequestContext ctx, String name, Object value) {
        if (ObjectUtil.isEmpty(value)) {
            return;
        }
        String valueStr = value.toString();
        String valueEncode = URLUtil.encode(valueStr);
        ctx.addZuulRequestHeader(name, valueEncode);
    }

}

