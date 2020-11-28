package com.cp3.cloud.jobs.api;

import com.cp3.cloud.base.R;
import com.cp3.cloud.jobs.api.fallback.JobsTimingApiFallback;
import com.cp3.cloud.jobs.dto.XxlJobInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 定时API
 *
 * @author cp3
 * @date 2019/07/05
 */
@FeignClient(name = "JobsTimingApi", url = "${zuihou.feign.jobs-server:http://127.0.0.1:8767}",
        path = "/zuihou-jobs-server", fallback = JobsTimingApiFallback.class)
public interface JobsTimingApi {

    /**
     * 定时发送接口
     *
     * @param xxlJobInfo
     * @return
     */
    @RequestMapping(value = "/jobinfo/addTimingTask", method = RequestMethod.POST)
    R<String> addTimingTask(@RequestBody XxlJobInfo xxlJobInfo);

}
