package com.cp3.cloud.activiti.controller.flow;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cp3.base.basic.R;
import com.cp3.base.basic.utils.BeanPlusUtil;
import com.cp3.base.dozer.DozerUtils;
import com.cp3.cloud.activiti.controller.flow.cmd.HistoryProcessInstanceDiagramCmd;
import com.cp3.cloud.activiti.dto.ProcessInstanceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @Description 工作流流程控制器
 * @Auther: cp3
 * @Date: 2021/1/5
 */
@Slf4j
@Controller
@RequestMapping("process")
@Api(value = "ActivitiProcess", tags = "工作流流程")
public class ActivitiProcessController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private DozerUtils dozerUtils;

    /**
     * 实例列表查询
     * @return
     */
    @GetMapping("queryProcessList")
    @ResponseBody
    @ApiOperation("实例列表查询")
    public R<IPage<ProcessInstanceDTO>> queryProcessList(@RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        Page<ProcessInstanceDTO> page = new Page<>(currentPage, pageSize);
        int firstResult = (currentPage-1)*pageSize;
        long count = runtimeService.createExecutionQuery().count();

        //List<Execution> list = runtimeService.createExecutionQuery().orderByProcessDefinitionId().desc().listPage(firstResult, pageSize);
        //因ProcessInstance extend Execution,一般使用ProcessInstance接口
        List<ProcessInstance> processInstanceList= runtimeService.createProcessInstanceQuery().orderByProcessDefinitionId().desc().listPage(firstResult, pageSize);
        List<ProcessInstanceDTO> processInstanceDTOList = BeanPlusUtil.toBeanList(processInstanceList, ProcessInstanceDTO.class);
        //dozerUtils对boolean转Long不适用
        //List<ExecutionDTO> executionDTOList2 = dozerUtils.mapList(list, ExecutionDTO.class);
        page.setRecords(processInstanceDTOList);
        page.setTotal(count);
        return R.success(page);
    }

    /**
     * 实例挂起/激活
     * @param processInstanceId
     * @return
     */
    @PostMapping("updateModelState")
    @ApiOperation("实例挂起/激活")
    public R<String> updateModelState(@RequestParam String processInstanceId) {
        //1.查询流程实例对象
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //2.流程实例挂起/激活
        if(processInstance.isSuspended()) {
            //挂起
            runtimeService.suspendProcessInstanceById(processInstance.getId());
        }
        else {
            //激活
            runtimeService.activateProcessInstanceById(processInstance.getId());
        }

        return R.success(processInstanceId);
    }

    /**
     * 启动流程
     * @param key  如"Leave"
     * @param businessKey 如"Leave.#id"
     * @return
     */
    @PostMapping("startProcess")
    @ResponseBody
    @ApiOperation("启动流程-此处为测试接口")
    public R<String> startProcess(@RequestParam String key, @RequestParam String businessKey) {
        runtimeService.startProcessInstanceById(key, businessKey,null);
        return R.success(key);
    }


}
