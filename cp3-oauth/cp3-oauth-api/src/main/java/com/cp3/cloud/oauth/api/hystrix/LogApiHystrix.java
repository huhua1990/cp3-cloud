package com.cp3.cloud.oauth.api.hystrix;

import com.cp3.cloud.base.R;
import com.cp3.cloud.log.entity.OptLogDTO;
import com.cp3.cloud.oauth.api.LogApi;
import org.springframework.stereotype.Component;

/**
 * 日志操作 熔断
 *
 * @author cp3
 * @date 2019/07/02
 */
@Component
public class LogApiHystrix implements LogApi {
    @Override
    public R<OptLogDTO> save(OptLogDTO log) {
        return R.timeout();
    }
}
