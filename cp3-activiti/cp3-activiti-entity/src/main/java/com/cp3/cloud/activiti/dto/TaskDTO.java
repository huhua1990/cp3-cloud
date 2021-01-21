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

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 实体类
 * 执行任务
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
@ApiModel(value = "TaskDTO", description = "执行任务对象")
public class TaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "rev")
    private Long revision;

    @ApiModelProperty(value = "executionId")
    private Long executionId;

    @ApiModelProperty(value = "procInstId,同executionId")
    private Long processInstanceId;

    @ApiModelProperty(value = "act_re_procdef中id")
    private String processDefinitionId;

    @ApiModelProperty(value = "环节名称")
    private String name;

    @ApiModelProperty(value = "环节定义id")
    private String taskDefinitionKey;

    @ApiModelProperty(value = "处理人")
    private String assignee;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "操作时间")
    private Date dueDate;

    @ApiModelProperty(value = "暂停状态suspensionState")
    private String suspensionState;

}
