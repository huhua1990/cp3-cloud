package com.cp3.cloud.authority.service.common.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.cp3.cloud.authority.dao.common.ParameterMapper;
import com.cp3.cloud.authority.entity.common.Parameter;
import com.cp3.cloud.authority.event.ParameterUpdateEvent;
import com.cp3.cloud.authority.event.model.ParameterUpdate;
import com.cp3.cloud.authority.service.common.ParameterService;
import com.cp3.cloud.base.service.SuperServiceImpl;
import com.cp3.cloud.context.BaseContextHandler;
import com.cp3.cloud.database.mybatis.conditions.Wraps;
import com.cp3.cloud.utils.BizAssert;
import com.cp3.cloud.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static com.cp3.cloud.common.constant.CacheKey.PARAMETER;

/**
 * <p>
 * 业务实现类
 * 参数配置
 * </p>
 *
 * @author cp3
 * @date 2020-02-05
 */
@Slf4j
@Service
public class ParameterServiceImpl extends SuperServiceImpl<ParameterMapper, Parameter> implements ParameterService {

    @Autowired
    private CacheChannel channel;

    protected String getRegion() {
        return PARAMETER;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Parameter model) {
        int count = count(Wraps.<Parameter>lbQ().eq(Parameter::getKey, model.getKey()));
        BizAssert.isFalse(count > 0, StrUtil.format("参数key[{}]已经存在，请勿重复创建", model.getKey()));

        boolean bool = SqlHelper.retBool(baseMapper.insert(model));
        if (bool) {
            String cacheKey = buildTenantKey(model.getKey());
            channel.set(getRegion(), cacheKey, model.getValue());
        }
        return bool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(Parameter model) {
        int count = count(Wraps.<Parameter>lbQ().eq(Parameter::getKey, model.getKey()).ne(Parameter::getId, model.getId()));
        BizAssert.isFalse(count > 0, StrUtil.format("参数key[{}]已经存在，请勿重复创建", model.getKey()));

        boolean bool = SqlHelper.retBool(getBaseMapper().updateById(model));
        if (bool) {
            String cacheKey = buildTenantKey(model.getKey());
            channel.set(getRegion(), cacheKey, model.getValue());

            SpringUtils.publishEvent(new ParameterUpdateEvent(
                    new ParameterUpdate(model.getKey(), model.getValue(), null, BaseContextHandler.getTenant())
            ));
        }
        return bool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return true;
        }
        List<Parameter> parameterList = super.listByIds(idList);
        if (parameterList.isEmpty()) {
            return true;
        }
        boolean bool = SqlHelper.retBool(getBaseMapper().deleteBatchIds(idList));
        if (bool) {
            String[] cacheKeys = parameterList.stream().map((item) -> buildTenantKey(item.getKey())).toArray(String[]::new);
            channel.evict(getRegion(), cacheKeys);

            parameterList.forEach((model -> {
                SpringUtils.publishEvent(new ParameterUpdateEvent(
                        new ParameterUpdate(model.getKey(), model.getValue(), null, BaseContextHandler.getTenant())
                ));
            }));
        }
        return bool;
    }

    @Override
    public String getValue(String key, String defVal) {
        if (StrUtil.isEmpty(key)) {
            return defVal;
        }

        String cacheKey = buildTenantKey(key);
        CacheObject cacheObject = channel.get(getRegion(), cacheKey, (k) -> {
            Parameter parameter = getOne(Wraps.<Parameter>lbQ().eq(Parameter::getKey, key).eq(Parameter::getStatus, true));
            return parameter == null ? null : parameter.getValue();
        }, true);

        return (String) cacheObject.getValue();
    }
}
