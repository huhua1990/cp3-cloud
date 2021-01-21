package com.cp3.cloud.activiti.config;

import cn.hutool.core.util.IdUtil;
import com.baidu.fsg.uid.UidGenerator;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * activiti自定义ID生成策略
 */
@Component
public class ActivitiIdGenerate implements IdGenerator {

    @Autowired
    private UidGenerator uidGenerator;

    @Override
    public String getNextId() {
        return String.valueOf(uidGenerator.getUid());
    }
}
