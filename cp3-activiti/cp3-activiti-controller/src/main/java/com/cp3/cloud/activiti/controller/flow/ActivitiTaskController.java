package com.cp3.cloud.activiti.controller.flow;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cp3.base.basic.R;
import com.cp3.base.basic.utils.BeanPlusUtil;
import com.cp3.cloud.activiti.dto.TaskDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 工作流流程任务控制器
 * @Auther: cp3
 * @Date: 2021/1/5
 */
@Slf4j
@Controller
@RequestMapping("task")
@Api(value = "ActivitiTask", tags = "工作流任务")
public class ActivitiTaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 实例任务列表查询
     * @param assignee 任务处理人
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("queryTaskList")
    @ResponseBody
    @ApiOperation("实例任务列表查询")
    public R<IPage<TaskDTO>> queryTaskList(@RequestParam String assignee,@RequestParam String businessType, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        Page<TaskDTO> page = new Page<>(currentPage, pageSize);
        int firstResult = (currentPage-1)*pageSize;
        long count = taskService.createTaskQuery().count();
        List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).processInstanceBusinessKey(businessType).orderByExecutionId().desc().listPage(firstResult,pageSize);
        List<TaskDTO> taskDTOList = BeanPlusUtil.toBeanList(list, TaskDTO.class);
        page.setRecords(taskDTOList);
        page.setTotal(count);
        return R.success(page);
    }

    /**
     * 任务操作
     * @param taskId
     * @param state
     * @return
     */
    @PostMapping("completeTask")
    @ResponseBody
    @ApiOperation("任务操作")
    public R<String> updateModelState(@RequestParam String taskId, @RequestParam String state, @RequestParam(required = false) Long day, @RequestParam(required = false) Long age) {
        //任务完成
        if ("F".equals(state)) {
            Map<String, Object> map = new HashMap<>();
            if(null != day) {
                map.put("day", day);
            }
            if(null != age) {
                map.put("age", age);
            }
            taskService.complete(taskId, map);
        }
        //任务删除
        if ("D".equals(state)) {
            taskService.deleteTask(taskId);
        }
        return R.success(taskId);
    }
}
