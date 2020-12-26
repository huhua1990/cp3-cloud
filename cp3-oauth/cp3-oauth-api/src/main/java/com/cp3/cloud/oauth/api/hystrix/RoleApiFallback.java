package com.cp3.cloud.oauth.api.hystrix;

import com.cp3.cloud.oauth.api.RoleApi;
import com.cp3.base.basic.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 角色查询API
 *
 * @author zuihou
 * @date 2019/08/02
 */
@Component
public class RoleApiFallback implements RoleApi {
    @Override
    public R<List<Long>> findUserIdByCode(String[] codes) {
        return R.timeout();
    }
}
