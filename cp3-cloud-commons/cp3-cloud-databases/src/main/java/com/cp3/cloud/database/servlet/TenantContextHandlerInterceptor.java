//package com.cp3.cloud.database.servlet;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.core.util.URLUtil;
//import com.cp3.cloud.context.BaseContextConstants;
//import com.cp3.cloud.context.BaseContextHandler;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 租户信息解析器
// * 用于将请求头中的租户编码和数据库名 封装到当前请求的线程变量中
// *
// * @author cp3
// * @date 2019-06-20 22:22
// */
//@Slf4j
//@AllArgsConstructor
//public class TenantContextHandlerInterceptor extends HandlerInterceptorAdapter {
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (!(handler instanceof HandlerMethod)) {
//            return super.preHandle(request, response, handler);
//        }
//        if (!BaseContextHandler.getBoot()) {
//            BaseContextHandler.setTenant(this.getHeader(request, BaseContextConstants.JWT_KEY_TENANT));
//        }
//        return super.preHandle(request, response, handler);
//    }
//
//    private String getHeader(HttpServletRequest request, String name) {
//        String value = request.getHeader(name);
//        if (StrUtil.isEmpty(value)) {
//            value = request.getParameter(name);
//        }
//        if (StrUtil.isEmpty(value)) {
//            return StrUtil.EMPTY;
//        }
//        return URLUtil.decode(value);
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        BaseContextHandler.remove();
//        super.afterCompletion(request, response, handler, ex);
//    }
//
//}
