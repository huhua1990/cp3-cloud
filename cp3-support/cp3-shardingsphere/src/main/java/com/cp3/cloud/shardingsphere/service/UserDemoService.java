package com.cp3.cloud.shardingsphere.service;

import com.cp3.cloud.shardingsphere.common.ResponseJson;
import com.cp3.cloud.shardingsphere.entity.Login;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/18 19:13
 * @Version:1.0
 */
public interface UserDemoService {

    public ResponseJson insertUser(Login login);

    public ResponseJson updateUserUserNameByPhone(Login login);

    public ResponseJson deleteUserByPhone(Login login);

    public ResponseJson findList(String id, String phone);
}
