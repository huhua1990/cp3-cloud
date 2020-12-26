package com.cp3.base.jobs.api;

import com.cp3.base.basic.R;
import com.cp3.base.jobs.api.fallback.JobsTimingApiFallback;
import com.cp3.base.jobs.dto.XxlJobInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description 定时任务api
 * @Auther: huhua
 * @Date: 2020/12/11
 */
@FeignClient(name = "cp3-jobs-server", fallback = JobsTimingApiFallback.class)
public interface JobsTimingApi {
    /**
     * 定时任务发送接口
     * @param xxlJobInfo
     * @return
     */
    @RequestMapping(value = "/jobinfo/addTimingTask", method = RequestMethod.POST)
    R<String> addTimingTask(@RequestBody XxlJobInfo xxlJobInfo);
}
