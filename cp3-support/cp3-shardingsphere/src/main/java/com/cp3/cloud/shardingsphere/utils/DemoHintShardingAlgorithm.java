package com.cp3.cloud.shardingsphere.utils;

import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

/**
 * @Description Hint分片算法 待完善
 * @Auther: hh
 * @Date: 2020/9/24 14:27
 * @Version:1.0
 */
@Slf4j
public class DemoHintShardingAlgorithm implements HintShardingAlgorithm {
    private static final String DEFAULT_DATA_SOURCE = "ds0";

    /**
     *   不需要与表字段关联
     *   自定义目标库源数据之间的算法  进行分库
     *   可用切面来实现 全局的分库逻辑
     * @param availableTargetNames   目标库
     * @param shardingValue   源数据
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ShardingValue shardingValue) {
        Collection<String> result = new HashSet<>();
        if (shardingValue instanceof ListShardingValue) {
            Collection shardingValues = ((ListShardingValue) shardingValue).getValues();
            if (availableTargetNames != null && !availableTargetNames.isEmpty() && shardingValues != null && !shardingValues.isEmpty()) {
                for (String availableName : availableTargetNames) {
                    for (Object value : shardingValues) {
                        if (value != null && availableName.endsWith(value.toString().toLowerCase())) {
                            result.add(availableName);
                        }
                    }
                }
            }
        }

        //匹配到目标库，直接返回目标库
        if (!result.isEmpty()) {
            log.info("force route data source to:{}", result);
        } else {
            log.warn("has no target datasource, route to datasource: {}", DEFAULT_DATA_SOURCE);
            result.add(DEFAULT_DATA_SOURCE);
        }

        return result;
    }

}
