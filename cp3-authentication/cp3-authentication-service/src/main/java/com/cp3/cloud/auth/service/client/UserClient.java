package com.cp3.cloud.auth.service.client;

import com.cp3.cloud.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 98050
 * @Time: 2018-10-23 23:48
 * @Feature: 用户feignclient
 */
@FeignClient(value = "user-service", fallback = Failback.class)
public interface UserClient extends UserApi {
}
