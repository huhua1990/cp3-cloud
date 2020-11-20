package com.cp3.cloud.authority.service.common.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.cp3.cloud.authority.dao.common.DictionaryItemMapper;
import com.cp3.cloud.authority.entity.common.DictionaryItem;
import com.cp3.cloud.authority.service.common.DictionaryItemService;
import com.cp3.cloud.base.service.SuperCacheServiceImpl;
import com.cp3.cloud.database.mybatis.conditions.Wraps;
import com.cp3.cloud.database.mybatis.conditions.query.LbqWrapper;
import com.cp3.cloud.injection.properties.InjectionProperties;
import com.cp3.cloud.utils.BizAssert;
import com.cp3.cloud.utils.MapHelper;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cp3.cloud.common.constant.CacheKey.DICTIONARY_ITEM;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * <p>
 * 业务实现类
 * 字典项
 * </p>
 *
 * @author cp3
 * @date 2019-07-02
 */
@Slf4j
@Service
public class DictionaryItemServiceImpl extends SuperCacheServiceImpl<DictionaryItemMapper, DictionaryItem> implements DictionaryItemService {

    @Autowired
    private InjectionProperties ips;

    @Override
    protected String getRegion() {
        return DICTIONARY_ITEM;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(DictionaryItem model) {
        int count = count(Wraps.<DictionaryItem>lbQ().eq(DictionaryItem::getDictionaryType, model.getDictionaryType()).eq(DictionaryItem::getCode, model.getCode()));
        BizAssert.isFalse(count > 0, StrUtil.format("字典类型[{}]已经存在，请勿重复创建", model.getCode()));
        return super.save(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(DictionaryItem model) {
        int count = count(Wraps.<DictionaryItem>lbQ().eq(DictionaryItem::getDictionaryType, model.getDictionaryType())
                .eq(DictionaryItem::getCode, model.getCode()).ne(DictionaryItem::getId, model.getId()));
        BizAssert.isFalse(count > 0, StrUtil.format("字典类型[{}]已经存在，请勿重复创建", model.getCode()));
        return super.updateById(model);
    }

    @Override
    public Map<String, Map<String, String>> map(String[] types) {
        if (ArrayUtil.isEmpty(types)) {
            return Collections.emptyMap();
        }
        LbqWrapper<DictionaryItem> query = Wraps.<DictionaryItem>lbQ()
                .in(DictionaryItem::getDictionaryType, types)
                .eq(DictionaryItem::getStatus, true)
                .orderByAsc(DictionaryItem::getSortValue);
        List<DictionaryItem> list = super.list(query);

        //key 是类型
        Map<String, List<DictionaryItem>> typeMap = list.stream().collect(groupingBy(DictionaryItem::getDictionaryType, LinkedHashMap::new, toList()));

        //需要返回的map
        Map<String, Map<String, String>> typeCodeNameMap = new LinkedHashMap<>(typeMap.size());

        typeMap.forEach((key, items) -> {
            ImmutableMap<String, String> itemCodeMap = MapHelper.uniqueIndex(items, DictionaryItem::getCode, DictionaryItem::getName);
            typeCodeNameMap.put(key, itemCodeMap);
        });
        return typeCodeNameMap;
    }

    @Override
    public Map<Serializable, Object> findDictionaryItem(Set<Serializable> typeCodes) {
        if (typeCodes.isEmpty()) {
            return Collections.emptyMap();
        }
        Set<String> types = typeCodes.stream().filter(Objects::nonNull)
                .map((item) -> StrUtil.split(String.valueOf(item), ips.getDictSeparator())[0]).collect(Collectors.toSet());
        Set<String> multiCodes = typeCodes.stream().filter(Objects::nonNull)
                .map((item) -> StrUtil.split(String.valueOf(item), ips.getDictSeparator())[1]).collect(Collectors.toSet());
        Set<String> codes = multiCodes.parallelStream()
                .map(item -> StrUtil.split(String.valueOf(item), ips.getDictItemSeparator()))
                .flatMap(Arrays::stream).collect(Collectors.toSet());


        // 1. 根据 字典编码查询可用的字典列表
        LbqWrapper<DictionaryItem> query = Wraps.<DictionaryItem>lbQ()
                .in(DictionaryItem::getDictionaryType, types)
                .in(DictionaryItem::getCode, codes)
                .orderByAsc(DictionaryItem::getSortValue);
        List<DictionaryItem> list = super.list(query);

        // 2. 将 list 转换成 Map，Map的key是字典编码，value是字典名称
        ImmutableMap<String, String> typeMap = MapHelper.uniqueIndex(list,
                item -> StrUtil.join(ips.getDictSeparator(), item.getDictionaryType(), item.getCode()), DictionaryItem::getName);

        // 3. 将 Map<String, String> 转换成 Map<Serializable, Object>
        Map<Serializable, Object> typeCodeNameMap = new HashMap<>(typeMap.size());
        typeMap.forEach(typeCodeNameMap::put);
        return typeCodeNameMap;
    }
}
