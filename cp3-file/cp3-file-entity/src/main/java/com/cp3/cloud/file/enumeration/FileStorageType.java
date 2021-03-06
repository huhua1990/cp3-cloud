package com.cp3.cloud.file.enumeration;

/**
 * 文件 存储类型 枚举
 *
 * @author zuihou
 * @date 2019/05/06
 */
public enum FileStorageType {
    /**
     * 本地
     */
    LOCAL,
    /**
     * FastDFS
     */
    FAST_DFS,
    ALI,
    QINIU,
    /**
     * MINIO
     */
    MINIO
    ;

    public boolean eq(FileStorageType type) {
        for (FileStorageType t : FileStorageType.values()) {
            return t.equals(type);
        }
        return false;
    }
}
