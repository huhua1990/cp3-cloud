package com.cp3.cloud.file.strategy.impl.fastdfs;


import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.cp3.cloud.file.dao.AttachmentMapper;
import com.cp3.cloud.file.domain.FileDeleteDO;
import com.cp3.cloud.file.entity.Attachment;
import com.cp3.cloud.file.properties.FileServerProperties;
import com.cp3.cloud.file.strategy.impl.AbstractFileStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zuihou
 * @date 2020/11/22 5:17 下午
 */

public class FastDfsFileStrategyImpl extends AbstractFileStrategy {
    private final FastFileStorageClient storageClient;
    private final AttachmentMapper attachmentMapper;

    public FastDfsFileStrategyImpl(FileServerProperties fileProperties, FastFileStorageClient storageClient, AttachmentMapper attachmentMapper) {
        super(fileProperties);
        this.storageClient = storageClient;
        this.attachmentMapper = attachmentMapper;
    }

    @Override
    protected void uploadFile(Attachment file, MultipartFile multipartFile) throws Exception {
        StorePath storePath = storageClient.uploadFile(multipartFile.getInputStream(), multipartFile.getSize(), file.getExt(), null);
        file.setUrl(fileProperties.getUriPrefix() + storePath.getFullPath());
        file.setGroup(storePath.getGroup());
        file.setPath(storePath.getPath());
    }

    @Override
    protected void delete(List<FileDeleteDO> list, FileDeleteDO file) {
        if (file.getFile()) {
            List<Long> ids = list.stream().mapToLong(FileDeleteDO::getId).boxed().collect(Collectors.toList());
            Integer count = attachmentMapper.countByGroup(ids, file.getGroup(), file.getPath());
            if (count > 0) {
                return;
            }
        }
        storageClient.deleteFile(file.getGroup(), file.getPath());
    }

}

