package com.cp3.cloud.oauth.api.hystrix;

import com.cp3.cloud.oauth.api.LogApi;
import com.cp3.base.basic.R;
import com.cp3.base.log.entity.OptLogDTO;
import org.springframework.stereotype.Component;

/**
 * 日志操作 熔断
 *
 * @author zuihou
 * @date 2019/07/02
 */
@Component
public class LogApiHystrix implements LogApi {
    @Override
    public R<OptLogDTO> save(OptLogDTO log) {
        return R.timeout();
    }
}
