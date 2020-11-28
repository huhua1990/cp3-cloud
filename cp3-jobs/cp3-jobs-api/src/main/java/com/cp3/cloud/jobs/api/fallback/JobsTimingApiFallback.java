package com.cp3.cloud.jobs.api.fallback;

import com.cp3.cloud.base.R;
import com.cp3.cloud.jobs.api.JobsTimingApi;
import com.cp3.cloud.jobs.dto.XxlJobInfo;
import org.springframework.stereotype.Component;

/**
 * 定时API 熔断
 *
 * @author cp3
 * @date 2019/07/16
 */
@Component
public class JobsTimingApiFallback implements JobsTimingApi {
    @Override
    public R<String> addTimingTask(XxlJobInfo xxlJobInfo) {
        return R.timeout();
    }
}
