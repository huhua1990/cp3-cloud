package com.cp3.cloud.authority.controller.core;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.cp3.cloud.authority.dto.core.OrgSaveDTO;
import com.cp3.cloud.authority.dto.core.OrgUpdateDTO;
import com.cp3.cloud.authority.entity.core.Org;
import com.cp3.cloud.authority.service.core.OrgService;
import com.cp3.cloud.base.R;
import com.cp3.cloud.base.controller.SuperCacheController;
import com.cp3.cloud.database.mybatis.conditions.Wraps;
import com.cp3.cloud.log.annotation.SysLog;
import com.cp3.cloud.security.annotation.PreAuth;
import com.cp3.cloud.utils.BeanPlusUtil;
import com.cp3.cloud.utils.BizAssert;
import com.cp3.cloud.utils.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cp3.cloud.utils.StrPool.DEF_PARENT_ID;
import static com.cp3.cloud.utils.StrPool.DEF_ROOT_PATH;
import static com.cp3.cloud.utils.StrPool.EMPTY;


/**
 * <p>
 * 前端控制器
 * 组织
 * </p>
 *
 * @author cp3
 * @date 2019-07-22
 */
@Slf4j
@RestController
@RequestMapping("/org")
@Api(value = "Org", tags = "组织")
@PreAuth(replace = "org:")
public class OrgController extends SuperCacheController<OrgService, Long, Org, Org, OrgSaveDTO, OrgUpdateDTO> {

    @Override
    public R<Org> handlerSave(OrgSaveDTO model) {
        Org org = BeanPlusUtil.toBean(model, Org.class);
        fillOrg(org);
        this.baseService.save(org);
        return success(org);
    }

    @Override
    public R<Org> handlerUpdate(OrgUpdateDTO model) {
        Org org = BeanPlusUtil.toBean(model, Org.class);
        fillOrg(org);
        this.baseService.updateAllById(org);
        return success(org);
    }

    private Org fillOrg(Org org) {
        if (org.getParentId() == null || org.getParentId() <= 0) {
            org.setParentId(DEF_PARENT_ID);
            org.setTreePath(DEF_ROOT_PATH);
        } else {
            Org parent = this.baseService.getByIdCache(org.getParentId());
            BizAssert.notNull(parent, "父组织不能为空");

            org.setTreePath(StrUtil.join(DEF_ROOT_PATH, parent.getTreePath(), parent.getId()));
        }
        return org;
    }

    @Override
    public R<Boolean> handlerDelete(List<Long> ids) {
        return this.success(baseService.remove(ids));
    }


    /**
     * 查询系统所有的组织树
     *
     * @param status 状态
     * @return
     * @author cp3
     * @date 2019-07-29 11:59
     */
    @ApiOperation(value = "查询系统所有的组织树", notes = "查询系统所有的组织树")
    @GetMapping("/tree")
    @SysLog("查询系统所有的组织树")
    public R<List<Org>> tree(@RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "status", required = false) Boolean status) {
        List<Org> list = this.baseService.list(Wraps.<Org>lbQ()
                .like(Org::getLabel, name).eq(Org::getStatus, status).orderByAsc(Org::getSortValue));
        return this.success(TreeUtil.buildTree(list));
    }


    @Override
    public R<Boolean> handlerImport(List<Map<String, String>> list) {
        List<Org> userList = list.stream().map((map) -> {
            Org item = new Org();
            item.setDescribe(map.getOrDefault("描述", EMPTY));
            item.setLabel(map.getOrDefault("名称", EMPTY));
            item.setAbbreviation(map.getOrDefault("简称", EMPTY));
            item.setStatus(Convert.toBool(map.getOrDefault("状态", EMPTY)));
            return item;
        }).collect(Collectors.toList());

        return R.success(baseService.saveBatch(userList));
    }

}
