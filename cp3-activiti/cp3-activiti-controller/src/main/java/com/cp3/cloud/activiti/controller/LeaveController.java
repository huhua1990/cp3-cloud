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
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cp3.base.annotation.security.PreAuth;
import org.springframework.web.bind.annotation.RequestParam;
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
@PreAuth(replace = "activiti:leave:", enabled = false)
public class LeaveController extends SuperController<LeaveService, Long, Leave, LeavePageQuery, LeaveSaveDTO, LeaveUpdateDTO> {

    @PostMapping("toApproval2")
    @ApiOperation("提交请假申请-方案二")
    public R<String> toApproval2(@RequestParam String id){
        this.baseService.toApproval2(id);
        return R.success("");
    }

    @PostMapping("toApproval")
    @ApiOperation("提交请假申请-方案一")
    public R<String> toApproval(@RequestParam String id){
        this.baseService.toApproval(id);
        return R.success("");
    }

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
