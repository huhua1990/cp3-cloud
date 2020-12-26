package com.cp3.cloud.tenant.service.impl;

import cn.hutool.core.convert.Convert;

import com.cp3.base.basic.service.SuperCacheServiceImpl;
import com.cp3.base.cache.model.CacheKey;
import com.cp3.base.cache.model.CacheKeyBuilder;
import com.cp3.base.database.mybatis.conditions.Wraps;
import com.cp3.base.basic.utils.BeanPlusUtil;
import com.cp3.cloud.common.cache.tenant.TenantCacheKeyBuilder;
import com.cp3.cloud.common.cache.tenant.TenantCodeCacheKeyBuilder;
import com.cp3.cloud.tenant.dao.TenantMapper;
import com.cp3.cloud.tenant.dto.TenantConnectDTO;
import com.cp3.cloud.tenant.dto.TenantSaveDTO;
import com.cp3.cloud.tenant.entity.Tenant;
import com.cp3.cloud.tenant.enumeration.TenantStatusEnum;
import com.cp3.cloud.tenant.enumeration.TenantTypeEnum;
import com.cp3.cloud.tenant.service.TenantService;
import com.cp3.cloud.tenant.strategy.InitSystemContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

import static com.cp3.base.utils.BizAssert.isFalse;

/**
 * <p>
 * 业务实现类
 * 企业
 * </p>
 *
 * @author zuihou
 * @date 2019-10-24
 */
@Slf4j
@Service

@RequiredArgsConstructor
public class TenantServiceImpl extends SuperCacheServiceImpl<TenantMapper, Tenant> implements TenantService {

    private final InitSystemContext initSystemContext;

    @Override
    protected CacheKeyBuilder cacheKeyBuilder() {
        return new TenantCacheKeyBuilder();
    }


    /**
     * tenant_name:{tenantCode} -> id 只存租户的id，然后根据id再次查询缓存，这样子的好处是，删除或者修改租户信息时，只需要根据id淘汰缓存即可
     * 缺点就是 每次查询，需要多查一次缓存
     *
     * @param tenant
     * @return
     */
    @Override
    public Tenant getByCode(String tenant) {
        Function<CacheKey, Object> loader = (k) ->
                getObj(Wraps.<Tenant>lbQ().select(Tenant::getId).eq(Tenant::getCode, tenant), Convert::toLong);
        CacheKey cacheKey = new TenantCodeCacheKeyBuilder().key(tenant);
        return getByKey(cacheKey, loader);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tenant save(TenantSaveDTO data) {
        // defaults 库
        isFalse(check(data.getCode()), "编码重复，请重新输入");

        // 1， 保存租户 (默认库)
        Tenant tenant = BeanPlusUtil.toBean(data, Tenant.class);
        tenant.setStatus(TenantStatusEnum.WAIT_INIT);
        tenant.setType(TenantTypeEnum.CREATE);
        // defaults 库
        save(tenant);

        CacheKey cacheKey = new TenantCodeCacheKeyBuilder().key(tenant.getCode());
        cacheOps.set(cacheKey, tenant.getId());
        return tenant;
    }

    @Override
    public boolean check(String tenantCode) {
        return super.count(Wraps.<Tenant>lbQ().eq(Tenant::getCode, tenantCode)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean connect(TenantConnectDTO tenantConnect) {
        boolean flag = initSystemContext.initConnect(tenantConnect);
        if (flag) {
            updateById(Tenant.builder().id(tenantConnect.getId()).connectType(tenantConnect.getConnectType())
                    .status(TenantStatusEnum.NORMAL).build());
        }
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(List<Long> ids) {
        List<String> tenantCodeList = listObjs(Wraps.<Tenant>lbQ().select(Tenant::getCode).in(Tenant::getId, ids), Convert::toStr);
        if (tenantCodeList.isEmpty()) {
            return true;
        }
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAll(List<Long> ids) {
        List<String> tenantCodeList = listObjs(Wraps.<Tenant>lbQ().select(Tenant::getCode).in(Tenant::getId, ids), Convert::toStr);
        if (tenantCodeList.isEmpty()) {
            return true;
        }
        removeByIds(ids);
        return initSystemContext.delete(ids, tenantCodeList);
    }


    @Override
    public List<Tenant> find() {
        return list(Wraps.<Tenant>lbQ().eq(Tenant::getStatus, TenantStatusEnum.NORMAL));
    }
}
