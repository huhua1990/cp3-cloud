package com.cp3.cloud.file.storage;

import com.cp3.cloud.file.properties.FileServerProperties;
import com.cp3.cloud.file.service.AttachmentService;
import com.cp3.cloud.file.strategy.FileChunkStrategy;
import com.cp3.cloud.file.strategy.FileStrategy;
import com.cp3.cloud.file.strategy.impl.ali.AliFileChunkStrategyImpl;
import com.cp3.cloud.file.strategy.impl.ali.AliFileStrategyImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里OSS
 *
 * @author zuihou
 * @date 2019/08/09
 */
@EnableConfigurationProperties(FileServerProperties.class)
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = FileServerProperties.PREFIX, name = "type", havingValue = "MINIO")
public class MinioAutoConfigure {

    @Bean
    public FileStrategy getFileStrategy(FileServerProperties fileProperties) {
        return new AliFileStrategyImpl(fileProperties);
    }

    @Bean
    public FileChunkStrategy getFileChunkStrategy(AttachmentService fileService, FileServerProperties fileProperties) {
        return new AliFileChunkStrategyImpl(fileService, fileProperties);
    }
}
