package com.cp3.cloud.zuul.controller;

import cn.hutool.core.util.StrUtil;
import com.cp3.base.utils.StrPool;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URLEncoder;

/**
 * 常用Controller
 *
 * @author zuihou
 * @date 2019-06-21 18:22
 */
@Controller
public class GeneratorController {

    /**
     * 解决swagger-bootstrap-ui的一个bug：
     * <p>
     * 将swagger发起的请求：
     * http://127.0.0.1:8760/api/gate/api/authority/v2/api-docs?group=%E5%85%AC%E5%85%B1%E6%A8%A1%E5%9D%97
     * 从定向到：
     * http://127.0.0.1:8760/api/authority/v2/api-docs?group=%E5%85%AC%E5%85%B1%E6%A8%A1%E5%9D%97
     *
     * <p>
     * 有个前提： nginx的端口要和访问端口一致，否则重定向出错
     *
     * @param service 服务
     * @param group   组
     * @param ext     uri 后缀
     */
    @ApiOperation(value = "获取指定服务的swagger", notes = "获取当前系统所有数据字典和枚举")
    @GetMapping("${server.servlet.context-path}/{service}/v2/{ext}")
    public String apiDocs(@PathVariable String service, @PathVariable String ext, String group) throws Exception {
        if (group == null) {
            group = "default";
        }
        String newGroup = group;
        if (group.contains(StrPool.DASH)) {
            newGroup = StrUtil.subSuf(group, group.indexOf(StrPool.DASH) + 1);
        }
        return "redirect:/" + service + "/v2/" + ext + "?group=" + URLEncoder.encode(newGroup, "UTF-8");
    }

}
