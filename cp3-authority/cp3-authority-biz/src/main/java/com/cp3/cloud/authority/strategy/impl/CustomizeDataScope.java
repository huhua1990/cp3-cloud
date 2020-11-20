package com.cp3.cloud.authority.strategy.impl;

import com.cp3.cloud.authority.entity.core.Org;
import com.cp3.cloud.authority.service.core.OrgService;
import com.cp3.cloud.authority.strategy.AbstractDataScopeHandler;
import com.cp3.cloud.exception.BizException;
import com.cp3.cloud.exception.code.ExceptionCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义模式
 *
 * @author cp3
 * @version 1.0
 * @date 2019-06-08 16:31
 */
@Component("CUSTOMIZE")
public class CustomizeDataScope implements AbstractDataScopeHandler {

    @Autowired
    private OrgService orgService;

    @Override
    public List<Long> getOrgIds(List<Long> orgList, Long userId) {
        if (orgList == null || orgList.isEmpty()) {
            throw new BizException(ExceptionCode.BASE_VALID_PARAM.getCode(), "自定义数据权限类型时，组织不能为空");
        }
        List<Org> children = orgService.findChildren(orgList);
        return children.stream().mapToLong(Org::getId).boxed().collect(Collectors.toList());
    }
}
