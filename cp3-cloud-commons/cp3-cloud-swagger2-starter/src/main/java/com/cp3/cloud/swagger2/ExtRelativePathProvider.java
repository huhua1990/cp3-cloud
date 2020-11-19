package com.cp3.cloud.swagger2;

import springfox.documentation.spring.web.paths.RelativePathProvider;

import javax.servlet.ServletContext;

/**
 * 相对路径重写
 *
 * @author tangyh
 * @date 2018/11/23
 */
public class ExtRelativePathProvider extends RelativePathProvider {
    private String basePath;

    public ExtRelativePathProvider(ServletContext servletContext, String basePath) {
        super(servletContext);
        this.basePath = basePath;
    }

    @Override
    public String getApplicationBasePath() {
        String applicationPath = super.applicationPath();
        if (ROOT.equals(applicationPath)) {
            applicationPath = "";
        }
        return basePath + applicationPath;
    }
}
