package com.cp3.cloud.oauth.api.hystrix;

import com.cp3.base.basic.R;
import com.cp3.cloud.oauth.api.ParameterApi;
import org.springframework.stereotype.Component;


/**
 * 熔断类
 *
 * @author zuihou
 * @date 2020年02月09日11:24:23
 */
@Component
public class ParameterApiFallback implements ParameterApi {
    @Override
    public R<String> getValue(String key, String defVal) {
        return R.timeout();
    }
}
