package com.cp3.cloud.shardingsphere.utils;

import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description 自定义库复合分片算法
 * @Auther: hh
 * @Date: 2020/9/24 11:18
 * @Version:1.0
 */
@Slf4j
public class DemoDBComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm {

    private static Logger logger = LoggerFactory.getLogger(DemoDBComplexKeysShardingAlgorithm.class);

    /**
     *  (id + phone)% availableTargetNames
     * @param availableTargetNames
     * @param shardingValues
     * @return
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue> shardingValues) {

        List<String> shardingResults = new ArrayList<>();
        Long shardingIndex = getIndex(shardingValues) % availableTargetNames.size();
        // loop and match datasource
        for (String name : availableTargetNames) {
            // get logic datasource index suffix
            String nameSuffix = name.substring(2);
            if (nameSuffix.equals(shardingIndex.toString())) {
                shardingResults.add(name);
                break;
            }
        }

        logger.info("DataSource sharding index ： {}", shardingIndex);
        return shardingResults;
    }

    /**
     * get datasource sharding index <p>
     * sharding algorithm : shardingIndex = (orderId + userId.hashCode()) % db.size
     * @param shardingValues
     * @return
     */
    private long getIndex(Collection<ShardingValue> shardingValues)
    {
        long shardingIndex = 0L;
        ListShardingValue<String> listShardingValue;
        List<String> shardingValue;
        for (ShardingValue sVal : shardingValues) {
            listShardingValue = (ListShardingValue<String>) sVal;
            if ("id".equals(listShardingValue.getColumnName())) {
                shardingValue = ( List<String>)listShardingValue.getValues();
                String shardingIndex_last = shardingValue.get(0).substring(shardingValue.get(0).length()-1, shardingValue.get(0).length());
                shardingIndex += Integer.valueOf(shardingIndex_last);
            } else if ("phone".equals(listShardingValue.getColumnName())) {
                shardingValue = ( List<String>)listShardingValue.getValues();
                String shardingIndex_last = shardingValue.get(0).substring(shardingValue.get(0).length()-1, shardingValue.get(0).length());
                shardingIndex += Integer.valueOf(shardingIndex_last);
            }
        }
        return shardingIndex;
    }
}
