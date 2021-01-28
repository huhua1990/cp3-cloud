package com.cp3.cloud.flowable.controller;

import com.cp3.cloud.flowable.entity.Approval;
import com.cp3.cloud.flowable.dto.ApprovalSaveDTO;
import com.cp3.cloud.flowable.dto.ApprovalUpdateDTO;
import com.cp3.cloud.flowable.dto.ApprovalPageQuery;
import com.cp3.cloud.flowable.service.ApprovalService;
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
 * 审批
 * </p>
 *
 * @author cp3
 * @date 2021-01-25
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/approval")
@Api(value = "Approval", tags = "审批")
@PreAuth(replace = "flowable:approval:")
public class ApprovalController extends SuperController<ApprovalService, Long, Approval, ApprovalPageQuery, ApprovalSaveDTO, ApprovalUpdateDTO> {

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
