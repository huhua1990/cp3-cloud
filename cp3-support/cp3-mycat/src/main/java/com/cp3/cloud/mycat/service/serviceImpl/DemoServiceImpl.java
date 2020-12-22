package com.cp3.cloud.mycat.service.serviceImpl;

import com.cp3.cloud.mycat.entity.UserDemo;
import com.cp3.cloud.mycat.mapper.DemoMapper;
import com.cp3.cloud.mycat.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description
 * @Auther: hh
 * @Date: 2020/9/23 18:28
 * @Version:1.0
 */
@Service
public class DemoServiceImpl implements DemoService {
    private static final Logger logger= LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    DemoMapper demoMapper;

    @Override
    public void save(UserDemo userDemo) {
        demoMapper.insert(userDemo);
    }

    @Override
    public List<UserDemo> findList() {
        return demoMapper.findList();
    }
}
