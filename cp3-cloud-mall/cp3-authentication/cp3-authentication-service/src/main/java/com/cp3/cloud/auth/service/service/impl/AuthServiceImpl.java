package com.cp3.cloud.auth.service.service.impl;

import com.cp3.cloud.auth.common.entity.UserInfo;
import com.cp3.cloud.auth.common.utils.JwtUtils;
import com.cp3.cloud.user.client.UserClient;
import com.cp3.cloud.auth.service.service.AuthService;
import com.cp3.cloud.auth.service.properties.JwtProperties;
import com.cp3.cloud.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 98050
 * @Time: 2018-10-23 22:47
 * @Feature:
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties properties;

    /**
     * 用户授权
     * @param username
     * @param password
     * @return
     */
    @Override
    public String authentication(String username, String password) {

        try{
            //1.调用微服务查询用户信息
            User user = this.userClient.queryUser(username,password);
            //2.查询结果为空，则直接返回null
            if (user == null){
                return null;
            }
            //3.查询结果不为空，则生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                    properties.getPrivateKey(), properties.getExpire());
            return token;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
