package com.cp3.cloud.shardingsphere.utils;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * @Description 自定义库分片算法
 * @Auther: hh
 * @Date: 2020/9/18 20:22
 * @Version:1.0
 */

public class UserDemoDBPreciseShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    /**
     *
     * @param collection  库名集合
     * @param preciseShardingValue 分片列
     * @return
     */
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        // 分片字段值
        String value = preciseShardingValue.getValue();
        // 求余如果是0则ds0.user_demo,如果是1则ds1.user_demo。但是由于id是字符串而且是很长的，所以截取最后一位然后转为Integer类型再求余
        value = value.substring(value.length()-6,value.length()-3);
        Integer number = Integer.valueOf(value);
        int result = number % 2;
        for (String s : collection) {
            if(s.endsWith(result+"")){
                return s;
            }
        }
        throw new UnsupportedOperationException();
    }
}
