package com.cp3.cloud.file.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件部分属性
 *
 * @author cp3
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FileAttrDO {
    /**
     * 路径
     */
    private String treePath;
    /**
     * 层级
     */
    private Integer grade;
    /**
     * 文件夹名称
     */
    private String folderName;
    /**
     * 文件夹id
     */
    private Long folderId;

}
