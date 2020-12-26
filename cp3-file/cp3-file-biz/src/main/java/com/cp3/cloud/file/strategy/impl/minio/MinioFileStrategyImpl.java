package com.cp3.cloud.file.strategy.impl.minio;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.cp3.base.context.ContextUtil;
import com.cp3.base.utils.StrPool;
import com.cp3.cloud.file.domain.FileDeleteDO;
import com.cp3.cloud.file.entity.Attachment;
import com.cp3.cloud.file.properties.FileServerProperties;
import com.cp3.cloud.file.strategy.impl.AbstractFileStrategy;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static com.cp3.base.utils.DateUtils.DEFAULT_MONTH_FORMAT_SLASH;

/**
 * @Description mino impl
 * @Auther: cp3
 * @Date: 2020/12/26
 */
@Slf4j
public class MinioFileStrategyImpl extends AbstractFileStrategy {
    public MinioFileStrategyImpl(FileServerProperties fileProperties) {
        super(fileProperties);
    }

    @Override
    protected void uploadFile(Attachment file, MultipartFile multipartFile) throws Exception {
        FileServerProperties.Minio minio = fileProperties.getMinio();
        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient = MinioClient.builder().endpoint(minio.getUriPrefix()).credentials(minio.getAccessKey(), minio.getSecretKey()).build();
        //生成文件名
        String fileName = StrUtil.join(StrPool.EMPTY, UUID.randomUUID().toString(), StrPool.DOT, file.getExt());
        //日期文件夹
        String tenant = ContextUtil.getTenant();
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
    protected void delete(List<FileDeleteDO> list, FileDeleteDO file) throws Exception{
        FileServerProperties.Minio minio = fileProperties.getMinio();
        MinioClient minioClient = MinioClient.builder().endpoint(minio.getUriPrefix()).credentials(minio.getAccessKey(), minio.getSecretKey()).build();
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(minio.getBucketName()).object(file.getRelativePath() + StrPool.SLASH + file.getFileName()).build());
    }

}
