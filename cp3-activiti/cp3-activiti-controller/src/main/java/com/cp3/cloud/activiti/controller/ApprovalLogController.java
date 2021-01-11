package com.cp3.cloud.activiti.controller;

import com.cp3.cloud.activiti.entity.ApprovalLog;
import com.cp3.cloud.activiti.dto.ApprovalLogSaveDTO;
import com.cp3.cloud.activiti.dto.ApprovalLogUpdateDTO;
import com.cp3.cloud.activiti.dto.ApprovalLogPageQuery;
import com.cp3.cloud.activiti.service.ApprovalLogService;
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
 * 审批日志
 * </p>
 *
 * @author cp3
 * @date 2021-01-11
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/approvalLog")
@Api(value = "ApprovalLog", tags = "审批日志")
@PreAuth(replace = "activiti:approvalLog:")
public class ApprovalLogController extends SuperController<ApprovalLogService, Long, ApprovalLog, ApprovalLogPageQuery, ApprovalLogSaveDTO, ApprovalLogUpdateDTO> {

    /**
     * Excel导入后的操作
     *
     * @param list
     */
    @Override
    public R<Boolean> handlerImport(List<Map<String, String>> list){
        List<ApprovalLog> approvalLogList = list.stream().map((map) -> {
            ApprovalLog approvalLog = ApprovalLog.builder().build();
            //TODO 请在这里完成转换
            return approvalLog;
        }).collect(Collectors.toList());

        return R.success(baseService.saveBatch(approvalLogList));
    }
}
