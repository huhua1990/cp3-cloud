package com.cp3.cloud.activiti.config;

import com.cp3.base.boot.config.BaseConfig;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 字体设置
 * @Auther: huhua
 * @Date: 2021/1/21
 */
@Configuration
public class ActivitiFontConfig extends BaseConfig {
    //@Resource
    //ActivitiIdGenerate activitiIdGenerate;

    @Bean
    public ProcessEngineConfigurationImpl getProcessEngineConfiguration(ProcessEngineConfigurationImpl processEngineConfiguration) {

        //id生成策略
        //processEngineConfiguration.setIdGenerator(activitiIdGenerate);
        //设置DbSqlSessionFactory的uuidGenerator，否则流程id，任务id，实例id依然是用DbIdGenerator生成
        //processEngineConfiguration.getDbSqlSessionFactory().setIdGenerator(activitiIdGenerate);
        //设置流程图片中文乱码
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        return processEngineConfiguration;
    }
}
