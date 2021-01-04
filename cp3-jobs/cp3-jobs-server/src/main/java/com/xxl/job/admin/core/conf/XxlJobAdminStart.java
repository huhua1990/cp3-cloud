package com.xxl.job.admin.core.conf;

import com.xxl.job.admin.core.scheduler.XxlJobScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Auther: huhua
 * @Date: 2021/1/3
 */

@Component
@Slf4j
public class XxlJobAdminStart implements ApplicationRunner {
    private XxlJobScheduler xxlJobScheduler;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("在应用启动成功后,再初始化JOBS");

        xxlJobScheduler = new XxlJobScheduler();
        xxlJobScheduler.init();
    }
}
