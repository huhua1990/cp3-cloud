package com.cp3.cloud.file.strategy;


import com.cp3.base.basic.R;
import com.cp3.cloud.file.dto.chunk.FileChunksMergeDTO;
import com.cp3.cloud.file.entity.Attachment;

/**
 * 文件分片处理策略类
 *
 * @author zuihou
 * @date 2019/06/19
 */
public interface FileChunkStrategy {

    /**
     * 根据md5检测文件
     *
     * @param md5    md5
     * @param userId 用户id
     * @return 附件
     */
    Attachment md5Check(String md5, Long userId);

    /**
     * 合并文件
     *
     * @param merge 合并参数
     * @return 附件
     */
    R<Attachment> chunksMerge(FileChunksMergeDTO merge);
}
