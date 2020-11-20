package com.cp3.cloud.oauth.api;

import com.cp3.cloud.base.R;
import com.cp3.cloud.log.entity.OptLogDTO;
import com.cp3.cloud.oauth.api.hystrix.LogApiHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 操作日志保存 API
 *
 * @author cp3
 * @date 2019/07/02
 */
@FeignClient(name = "${zuihou.feign.oauth-server:zuihou-oauth-server}", fallback = LogApiHystrix.class, qualifier = "logApi")
public interface LogApi {

    /**
     * 保存日志
     *
     * @param log 日志
     * @return
     */
    @RequestMapping(value = "/optLog", method = RequestMethod.POST)
    R<OptLogDTO> save(@RequestBody OptLogDTO log);

}
