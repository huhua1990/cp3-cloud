package com.cp3.cloud.file.storage;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.cp3.cloud.base.R;
import com.cp3.cloud.context.BaseContextHandler;
import com.cp3.cloud.file.domain.FileDeleteDO;
import com.cp3.cloud.file.dto.chunk.FileChunksMergeDTO;
import com.cp3.cloud.file.entity.File;
import com.cp3.cloud.file.properties.FileServerProperties;
import com.cp3.cloud.file.strategy.impl.AbstractFileChunkStrategy;
import com.cp3.cloud.file.strategy.impl.AbstractFileStrategy;
import com.cp3.cloud.utils.StrPool;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static com.cp3.cloud.utils.DateUtils.DEFAULT_MONTH_FORMAT_SLASH;

/**
 * Minio
 * @author cp3
 * @date 2020/11/30
 */
@EnableConfigurationProperties(FileServerProperties.class)
@Configuration
@Slf4j
@ConditionalOnProperty(prefix = FileServerProperties.PREFIX, name = "type", havingValue = "MINIO")
public class MinioAutoConfigure {

    @Service
    public class MinioServiceImpl extends AbstractFileStrategy {
        @Override
        protected void uploadFile(File file, MultipartFile multipartFile) throws Exception {
            FileServerProperties.Minio minio = fileProperties.getMinio();
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = MinioClient.builder().endpoint(minio.getUriPrefix()).credentials(minio.getAccessKey(), minio.getSecretKey()).build();
            //生成文件名
            String fileName = StrUtil.join(StrPool.EMPTY, UUID.randomUUID().toString(), StrPool.DOT, file.getExt());
            //日期文件夹
            String tenant = BaseContextHandler.getTenant();
            String relativePath = tenant + StrPool.SLASH + LocalDate.now().format(DateTimeFormatter.ofPattern(DEFAULT_MONTH_FORMAT_SLASH));
            // web服务器存放的绝对路径
            String relativeFileName = relativePath + StrPool.SLASH + fileName;
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(new BucketExistsArgs().builder().bucket(minio.getBucketName()).build());
            if (!isExist) {
                // 创建存储桶，用于存储文件。
                minioClient.makeBucket(new MakeBucketArgs().builder().bucket(minio.getBucketName()).build());
            }
            // 使用putObject上传一个文件到存储桶中。
            ObjectWriteResponse response = minioClient.putObject(PutObjectArgs.builder().bucket(minio.getBucketName()).object(relativeFileName).stream(multipartFile.getInputStream(),multipartFile.getInputStream().available(),-1).contentType(file.getContextType()).build());
            log.info("result={}", JSONObject.toJSONString(response));
            String url  = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(minio.getBucketName()).object(relativeFileName).method(Method.GET).build());
            file.setUrl(StrUtil.replace(url, "\\\\", StrPool.SLASH));
            file.setFilename(fileName);
            file.setRelativePath(relativePath);
            file.setGroup(response.etag());
            file.setPath(response.versionId());
        }

        @Override
        protected void delete(List<FileDeleteDO> list, FileDeleteDO file) throws Exception {
            FileServerProperties.Minio minio = fileProperties.getMinio();
            MinioClient minioClient = MinioClient.builder().endpoint(minio.getUriPrefix()).credentials(minio.getAccessKey(), minio.getSecretKey()).build();
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minio.getBucketName()).object(file.getRelativePath() + StrPool.SLASH + file.getFileName()).build());
        }
    }

    //大文件自动分片上传
    @Service
    public class MinioAbstractFileChunkStrategy extends AbstractFileChunkStrategy {
        @Override
        protected void copyFile(File file) {
        }

        @Override
        protected R<File> merge(List<java.io.File> files, String path, String fileName, FileChunksMergeDTO info) throws IOException {
            return null;
        }
    }
}
