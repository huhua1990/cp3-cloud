package com.cp3.cloud.authority.strategy.impl;

import com.cp3.base.exception.BizException;
import com.cp3.base.exception.code.ExceptionCode;
import com.cp3.cloud.authority.entity.core.Org;
import com.cp3.cloud.authority.service.core.OrgService;
import com.cp3.cloud.authority.strategy.AbstractDataScopeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义模式
 *
 * @author zuihou
 * @version 1.0
 * @date 2019-06-08 16:31
 */
@Component("CUSTOMIZE")
@RequiredArgsConstructor
public class CustomizeDataScope implements AbstractDataScopeHandler {
    private final OrgService orgService;

    @Override
    public List<Long> getOrgIds(List<Long> orgList, Long userId) {
        if (orgList == null || orgList.isEmpty()) {
            throw new BizException(ExceptionCode.BASE_VALID_PARAM.getCode(), "自定义数据权限类型时，组织不能为空");
        }
        return orgList;
        //List<Org> children = orgService.findChildren(orgList);
        //return children.stream().mapToLong(Org::getId).boxed().collect(Collectors.toList());
    }
}
