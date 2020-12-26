package com.cp3.base.validator.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 验证请求包装器
 *
 * @author zuihou
 * @date 2019-07-12 14:31
 */
@SuppressWarnings("ALL")
public class HttpServletRequestValidatorWrapper extends HttpServletRequestWrapper {

    private String formPath;

    public HttpServletRequestValidatorWrapper(HttpServletRequest request, String uri) {
        super(request);
        this.formPath = uri;
    }

    @Override
    public String getRequestURI() {
        return this.formPath;
    }

    @Override
    public String getServletPath() {
        return this.formPath;
    }
}
