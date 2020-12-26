package com.cp3.base.model;

import cn.hutool.core.util.ObjectUtil;
import com.cp3.base.basic.validation.IValidatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 远程数据对象
 *
 * @param <K> ID或者code 等唯一键
 * @param <D> 根据key 远程查询出的数据
 * @author zuihou
 * @date 2020年02月02日21:16:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoteData<K, D> implements Serializable, IValidatable {

    private K key;
    private D data;


    public RemoteData(K key) {
        this.key = key;
    }

    /**
     * 获取对象的 主键key
     *
     * @param remoteData 远程参数
     * @return key
     */
    public static <K, D> K getKey(RemoteData<K, D> remoteData) {
        return remoteData != null ? remoteData.getKey() : null;
    }

    public static <K, D> K getKey(RemoteData<K, D> remoteData, K def) {
        return remoteData != null && ObjectUtil.isNotEmpty(remoteData.getKey()) ? remoteData.getKey() : def;
    }

    /**
     * 获取对象的 data
     *
     * @param remoteData 远程参数
     * @return data
     */
    public static <K, D> D getData(RemoteData<K, D> remoteData) {
        return remoteData != null ? remoteData.getData() : null;
    }

    @Override
    public String toString() {
        String toString = key == null ? "" : String.valueOf(key);
        if (ObjectUtil.isNotEmpty(this.data) && this.data instanceof String) {
            toString = String.valueOf(data);
        }
        return toString;
    }


    /**
     * 用于Hibernate-Validator 自定义校验规则
     *
     * @return 验证值
     */
    @Override
    public Object value() {
        return this.key;
    }
}
