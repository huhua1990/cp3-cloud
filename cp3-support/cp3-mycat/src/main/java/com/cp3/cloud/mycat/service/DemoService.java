package com.cp3.cloud.mycat.service;

import com.cp3.cloud.mycat.entity.UserDemo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/23 18:28
 * @Version:1.0
 */
public interface DemoService {
    void save(UserDemo userDemo);
    List<UserDemo> findList();
}
