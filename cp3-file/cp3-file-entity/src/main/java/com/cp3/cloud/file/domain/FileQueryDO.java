package com.cp3.cloud.file.domain;


import com.cp3.cloud.file.entity.File;
import lombok.Data;

/**
 * 文件查询 DO
 *
 * @author cp3
 * @date 2019/05/07
 */
@Data
public class FileQueryDO extends File {
    private File parent;

}
