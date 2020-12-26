package com.cp3.cloud.file.strategy.impl.minio;

import com.cp3.base.basic.R;
import com.cp3.cloud.file.dto.chunk.FileChunksMergeDTO;
import com.cp3.cloud.file.entity.Attachment;
import com.cp3.cloud.file.properties.FileServerProperties;
import com.cp3.cloud.file.service.AttachmentService;
import com.cp3.cloud.file.strategy.impl.AbstractFileChunkStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Description mino impl
 * @Auther: cp3
 * @Date: 2020/12/26
 */
@Slf4j
public class MinioFileChunkStrategyImpl extends AbstractFileChunkStrategy {
    public MinioFileChunkStrategyImpl(AttachmentService fileService, FileServerProperties fileProperties) {
        super(fileService, fileProperties);
    }

    @Override
    protected void copyFile(Attachment file) {

    }

    @Override
    protected R<Attachment> merge(List<File> files, String path, String fileName, FileChunksMergeDTO info) throws IOException {
        return null;
    }
}
