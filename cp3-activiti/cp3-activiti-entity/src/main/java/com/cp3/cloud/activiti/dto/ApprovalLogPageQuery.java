package com.cp3.cloud.activiti.dto;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.cp3.base.basic.entity.Entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.cp3.cloud.common.constant.DictionaryType;
import java.io.Serializable;

/**
 * <p>
 * 实体类
 * 审批日志
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
@ApiModel(value = "ApprovalLogPageQuery", description = "审批日志")
public class ApprovalLogPageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请假id
     */
    @ApiModelProperty(value = "请假id")
    private Long leaveId;
    /**
     * 实例任务id(act_ru_task)
     */
    @ApiModelProperty(value = "实例任务id(act_ru_task)")
    private String taskId;
    /**
     * 实例任务key(act_ru_task)
     */
    @ApiModelProperty(value = "实例任务key(act_ru_task)")
    private String taskKey;
    /**
     * 实例任务name(act_ru_task)
     */
    @ApiModelProperty(value = "实例任务name(act_ru_task)")
    private String taskName;
    /**
     * 审批结果
     */
    @ApiModelProperty(value = "审批结果")
    private String approvalStatus;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operId;
    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容")
    private String opervalue;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "")
    private Long createBy;
    @ApiModelProperty(value = "")
    private Long updateBy;

}
