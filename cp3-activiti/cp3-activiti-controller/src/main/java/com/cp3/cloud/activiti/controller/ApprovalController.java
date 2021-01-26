package com.cp3.cloud.activiti.controller;

import com.cp3.cloud.activiti.entity.Approval;
import com.cp3.cloud.activiti.dto.ApprovalSaveDTO;
import com.cp3.cloud.activiti.dto.ApprovalUpdateDTO;
import com.cp3.cloud.activiti.dto.ApprovalPageQuery;
import com.cp3.cloud.activiti.service.ApprovalService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.cp3.base.basic.controller.SuperController;
import com.cp3.base.basic.R;
import com.cp3.cloud.activiti.strategy.ApprovalHandlerBuilder;
import com.cp3.cloud.activiti.strategy.ApprovalHandlerStrategy;
import com.cp3.cloud.activiti.strategy.impl.AbstractApprovalHandlerStrategy;
import com.cp3.cloud.activiti.strategy.impl.account.AccountApprovalHandlerStrategyImpl;
import com.cp3.cloud.activiti.strategy.impl.leave.LeaveApprovalHandlerStrategyImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cp3.base.annotation.security.PreAuth;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 前端控制器
 * 审批
 * </p>
 *
 * @author cp3
 * @date 2021-01-11
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/approval")
@Api(value = "Approval", tags = "审批")
@PreAuth(replace = "activiti:approval:", enabled = false)
public class ApprovalController extends SuperController<ApprovalService, Long, Approval, ApprovalPageQuery, ApprovalSaveDTO, ApprovalUpdateDTO> {

    @Autowired
    ApprovalHandlerBuilder approvalHandlerBuilder;

    @PostMapping("handler")
    @ApiOperation("任务审批")
    public R handler(@RequestBody ApprovalSaveDTO approvalSaveDTO){
        approvalHandlerBuilder.getStrategy(approvalSaveDTO.getBusinessType()).approval(approvalSaveDTO);
        return R.success();
    }

    /**
     * Excel导入后的操作
     *
     * @param list
     */
    @Override
    public R<Boolean> handlerImport(List<Map<String, String>> list){
        List<Approval> approvalList = list.stream().map((map) -> {
            Approval approval = Approval.builder().build();
            //TODO 请在这里完成转换
            return approval;
        }).collect(Collectors.toList());

        return R.success(baseService.saveBatch(approvalList));
    }
}
