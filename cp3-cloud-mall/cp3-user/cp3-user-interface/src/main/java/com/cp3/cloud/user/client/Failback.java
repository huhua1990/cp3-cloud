package com.cp3.cloud.user.client;

import com.cp3.cloud.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/5/5 12:16
 * @Version:1.0
 */
@Component
@Slf4j
public class Failback implements UserClient{

    @Autowired
    ServiceInstance serviceInstance;

    @Override
    public User queryUser(String username, String password) {
        log.error("{},UserClient queryUser Failback!!!!",serviceInstance.getInstanceId());
        return new User();
    }
}
