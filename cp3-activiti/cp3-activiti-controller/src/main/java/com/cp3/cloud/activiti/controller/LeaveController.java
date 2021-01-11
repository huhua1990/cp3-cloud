package com.cp3.cloud.activiti.controller;

import com.cp3.cloud.activiti.entity.Leave;
import com.cp3.cloud.activiti.dto.LeaveSaveDTO;
import com.cp3.cloud.activiti.dto.LeaveUpdateDTO;
import com.cp3.cloud.activiti.dto.LeavePageQuery;
import com.cp3.cloud.activiti.service.LeaveService;
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
 * 假期申请
 * </p>
 *
 * @author cp3
 * @date 2021-01-11
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/leave")
@Api(value = "Leave", tags = "假期申请")
@PreAuth(replace = "activiti:leave:")
public class LeaveController extends SuperController<LeaveService, Long, Leave, LeavePageQuery, LeaveSaveDTO, LeaveUpdateDTO> {

    /**
     * Excel导入后的操作
     *
     * @param list
     */
    @Override
    public R<Boolean> handlerImport(List<Map<String, String>> list){
        List<Leave> leaveList = list.stream().map((map) -> {
            Leave leave = Leave.builder().build();
            //TODO 请在这里完成转换
            return leave;
        }).collect(Collectors.toList());

        return R.success(baseService.saveBatch(leaveList));
    }
}
