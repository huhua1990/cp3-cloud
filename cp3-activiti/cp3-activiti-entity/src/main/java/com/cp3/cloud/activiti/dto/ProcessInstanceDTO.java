package com.cp3.cloud.activiti.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 实体类
 * 流程执行实例
 * </p>
 *
 * @author cp3
 * @since 2021-01-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@ApiModel(value = "ExecutionDTO", description = "流程实例对象")
public class ProcessInstanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "parentId")
    private Long parentId;

    @ApiModelProperty(value = "superExecutionId")
    private Long superExecutionId;

    @ApiModelProperty(value = "rev")
    private Long revision;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "procInstId")
    private Long processInstanceId;

    @ApiModelProperty(value = "businessKey")
    private String businessKey;

    @ApiModelProperty(value = "act_re_procdef中id")
    private String processDefinitionId;

    @ApiModelProperty(value = "act_re_procdef中name")
    private String processDefinitionName;

    @ApiModelProperty(value = "act_re_procdef中key")
    private String processDefinitionKey;

    @ApiModelProperty(value = "act_re_procdef中version")
    private String processDefinitionVersion;

    @ApiModelProperty(value = "deploymentId")
    private String deploymentId;

    @ApiModelProperty(value = "环节定义Id")
    private String activityId;

    @ApiModelProperty(value = "isActive")
    private Long isActive;

    @ApiModelProperty(value = "isEnd")
    private Long isEnd;

    @ApiModelProperty(value = "暂停状态isSuspended")
    private Long isSuspended;

    @ApiModelProperty(value = "cacheEndState")
    private String cacheEndState;

    @ApiModelProperty(value = "processVariables")
    Map<String, Object> processVariables;

}
