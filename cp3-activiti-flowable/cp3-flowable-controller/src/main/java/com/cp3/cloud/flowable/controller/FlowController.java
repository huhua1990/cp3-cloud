package com.cp3.cloud.flowable.controller;

import com.cp3.cloud.flowable.entity.Flow;
import com.cp3.cloud.flowable.dto.FlowSaveDTO;
import com.cp3.cloud.flowable.dto.FlowUpdateDTO;
import com.cp3.cloud.flowable.dto.FlowPageQuery;
import com.cp3.cloud.flowable.service.FlowService;
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
 * 流程
 * </p>
 *
 * @author cp3
 * @date 2021-01-25
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/flow")
@Api(value = "Flow", tags = "流程")
@PreAuth(replace = "flowable:flow:")
public class FlowController extends SuperController<FlowService, Long, Flow, FlowPageQuery, FlowSaveDTO, FlowUpdateDTO> {

    /**
     * Excel导入后的操作
     *
     * @param list
     */
    @Override
    public R<Boolean> handlerImport(List<Map<String, String>> list){
        List<Flow> flowList = list.stream().map((map) -> {
            Flow flow = Flow.builder().build();
            //TODO 请在这里完成转换
            return flow;
        }).collect(Collectors.toList());

        return R.success(baseService.saveBatch(flowList));
    }
}
