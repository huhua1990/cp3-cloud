package com.cp3.cloud.file.strategy;

import com.cp3.cloud.file.domain.FileDeleteDO;
import com.cp3.cloud.file.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件策略接口
 *
 * @author zuihou
 * @date 2019/06/17
 */
public interface FileStrategy {
    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件对象
     */
    Attachment upload(MultipartFile file);

    /**
     * 删除源文件
     *
     * @param list 列表
     * @return 是否成功
     */
    boolean delete(List<FileDeleteDO> list);

}
