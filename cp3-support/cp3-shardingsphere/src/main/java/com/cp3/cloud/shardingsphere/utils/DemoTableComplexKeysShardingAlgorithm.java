package com.cp3.cloud.shardingsphere.utils;

import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description 自定义表复合分片算法
 * @Auther: hh
 * @Date: 2020/9/24 11:26
 * @Version:1.0
 */
@Slf4j
public class DemoTableComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm {

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue> shardingValues) {

        List<String> shardingResults = new ArrayList<>();
        Long shardingIndex = getIndex(shardingValues) % availableTargetNames.size();
        // loop and match datasource
        for (String name : availableTargetNames) {
            // get logic datasource index suffix
            String nameSuffix = name.substring(name.length() - 1, name.length());
            if (nameSuffix.equals(shardingIndex.toString())) {
                shardingResults.add(name);
                break;
            }
        }
        log.info("Table sharding index ： {}", shardingIndex);
        return shardingResults;
    }

    /**
     * get table sharding index <p>
     * @param shardingValues
     * @return
     */
    private long getIndex(Collection<ShardingValue> shardingValues)
    {
        Long shardingIndex = 0L;
        ListShardingValue<String> listShardingValue;
        List<String> shardingValue;
        for (ShardingValue sVal : shardingValues) {
            listShardingValue = (ListShardingValue<String>) sVal;
            shardingValue = (List<String>) listShardingValue.getValues();
            String shardingValue_last = shardingValue.get(0).substring(shardingValue.get(0).length()-1, shardingValue.get(0).length());
            shardingIndex += Long.valueOf(shardingValue_last);
        }
        return shardingIndex;
    }
}
