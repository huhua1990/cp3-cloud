package com.cp3.cloud.authority.api.hystrix;

import com.cp3.cloud.authority.api.UserBizApi;
import com.cp3.cloud.authority.entity.auth.User;
import com.cp3.base.basic.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户API熔断
 *
 * @author zuihou
 * @date 2019/07/23
 */
@Component
public class UserBizApiFallback implements UserBizApi {
    @Override
    public R<List<Long>> findAllUserId() {
        return R.timeout();
    }

    @Override
    public R<List<User>> findUserById(List<Long> ids) {
        return R.timeout();
    }
}
