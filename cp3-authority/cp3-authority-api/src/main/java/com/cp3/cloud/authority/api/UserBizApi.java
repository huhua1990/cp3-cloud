package com.cp3.cloud.authority.api;

import com.cp3.cloud.authority.api.hystrix.UserBizApiFallback;
import com.cp3.cloud.authority.entity.auth.User;
import com.cp3.cloud.base.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户
 *
 * @author cp3
 * @date 2019/07/02
 */
@FeignClient(name = "${zuihou.feign.authority-server:cp3-authority-server}", fallback = UserBizApiFallback.class
        , path = "/user", qualifier = "userBizApi")
public interface UserBizApi {

    /**
     * 查询所有的用户id
     *
     * @return
     */
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    R<List<Long>> findAllUserId();

    /**
     * 根据id集合查询所有的用户
     *
     * @return
     */
    @RequestMapping(value = "/findUserById", method = RequestMethod.GET)
    R<List<User>> findUserById(@RequestParam(value = "ids") List<Long> ids);
}
