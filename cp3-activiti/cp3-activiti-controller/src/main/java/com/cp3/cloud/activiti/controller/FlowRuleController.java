package com.cp3.cloud.activiti.controller;

import com.cp3.cloud.activiti.entity.FlowRule;
import com.cp3.cloud.activiti.dto.FlowRuleSaveDTO;
import com.cp3.cloud.activiti.dto.FlowRuleUpdateDTO;
import com.cp3.cloud.activiti.dto.FlowRulePageQuery;
import com.cp3.cloud.activiti.service.FlowRuleService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.cp3.base.basic.controller.SuperController;
import com.cp3.base.basic.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cp3.base.annotation.security.PreAuth;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 前端控制器
 * 流程规则
 * </p>
 *
 * @author cp3
 * @date 2021-01-11
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/flowRule")
@Api(value = "FlowRule", tags = "流程规则")
@PreAuth(replace = "activiti:flowRule:", enabled = false)
public class FlowRuleController extends SuperController<FlowRuleService, Long, FlowRule, FlowRulePageQuery, FlowRuleSaveDTO, FlowRuleUpdateDTO> {

    /**
     * Excel导入后的操作
     *
     * @param list
     */
    @Override
    public R<Boolean> handlerImport(List<Map<String, String>> list){
        List<FlowRule> flowRuleList = list.stream().map((map) -> {
            FlowRule flowRule = FlowRule.builder().build();
            //TODO 请在这里完成转换
            return flowRule;
        }).collect(Collectors.toList());

        return R.success(baseService.saveBatch(flowRuleList));
    }
}
