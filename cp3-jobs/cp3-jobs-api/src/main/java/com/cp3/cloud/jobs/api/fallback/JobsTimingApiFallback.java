package com.cp3.cloud.jobs.api.fallback;

import com.cp3.base.basic.R;
import com.cp3.cloud.jobs.api.JobsTimingApi;
import com.cp3.cloud.jobs.dto.XxlJobInfo;
import org.springframework.stereotype.Component;

/**
 * @Description 定时任务api 熔断
 * @Auther: huhua
 * @Date: 2020/12/11
 */
@Component
public class JobsTimingApiFallback implements JobsTimingApi {
    @Override
    public R<String> addTimingTask(XxlJobInfo xxlJobInfo) {
        return R.timeout();
    }
}
