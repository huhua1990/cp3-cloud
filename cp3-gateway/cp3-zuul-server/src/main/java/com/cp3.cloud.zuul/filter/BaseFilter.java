package com.cp3.cloud.zuul.filter;

import cn.hutool.core.util.StrUtil;
import com.cp3.cloud.base.R;
import com.cp3.cloud.common.properties.IgnoreProperties;
import com.cp3.cloud.utils.StrPool;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础 网关过滤器
 *
 * @author cp3
 * @date 2018/11/23
 */
@Slf4j
public abstract class BaseFilter extends ZuulFilter {
    protected static UrlPathHelper URL_PATH_HELPER = new UrlPathHelper();
    /**
     * 为zuul设置一个公共的前缀
     */
    @Value("${server.servlet.context-path}")
    protected String zuulPrefix;
    @Value("${spring.profiles.active:dev}")
    protected String profiles;
    @Autowired
    protected IgnoreProperties ignoreTokenProperties;
    @Autowired
    protected RouteLocator routeLocator;

    protected boolean isDev(String token) {
        return !StrPool.PROD.equalsIgnoreCase(profiles) && (StrPool.TEST_TOKEN.equalsIgnoreCase(token) || StrPool.TEST.equalsIgnoreCase(token));
    }

    /**
     * 获取route
     *
     * @return
     */
    protected Route route() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String requestURI = URL_PATH_HELPER.getPathWithinApplication(request);
        return routeLocator.getMatchingRoute(requestURI);
    }

    private String getUri() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getRequestURI();
        uri = StrUtil.subSuf(uri, zuulPrefix.length());
        uri = StrUtil.subSuf(uri, uri.indexOf("/", 1));
        return uri;
    }

    /**
     * 忽略应用级token
     *
     * @return
     */
    protected boolean isIgnoreToken() {
        return ignoreTokenProperties.isIgnoreToken(getUri());
    }

    protected String getHeader(String headerName, HttpServletRequest request) {
        String token = request.getHeader(headerName);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(headerName);
        }
        return token;
    }


    /**
     * 网关抛异常
     *
     * @param body
     * @param code
     */
    protected void setFailedRequest(String body, int code) {
        log.debug("Reporting error ({}): {}", code, body);
        RequestContext ctx = RequestContext.getCurrentContext();
        // 返回错误码
        ctx.setResponseStatusCode(code);
        ctx.addZuulResponseHeader("Content-Type", "application/json;charset=UTF-8");
        if (ctx.getResponseBody() == null) {
            // 返回错误内容
            ctx.setResponseBody(body);
            // 过滤该请求，不对其进行路由
            ctx.setSendZuulResponse(false);
        }
    }

    protected void errorResponse(String errMsg, int errCode, int httpStatusCode) {
        R tokenError = R.fail(errCode, errMsg);
        setFailedRequest(tokenError.toString(), httpStatusCode);
    }
}
