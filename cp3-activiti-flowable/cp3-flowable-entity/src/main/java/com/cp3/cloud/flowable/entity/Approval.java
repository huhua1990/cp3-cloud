package com.cp3.cloud.flowable.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.cp3.base.basic.entity.Entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.cp3.cloud.common.constant.DictionaryType;
import static com.cp3.base.utils.DateUtils.DEFAULT_DATE_TIME_FORMAT;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * <p>
 * 实体类
 * 审批
 * </p>
 *
 * @author cp3
 * @since 2021-01-25
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("f_approval")
@ApiModel(value = "Approval", description = "审批")
@AllArgsConstructor
public class Approval extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    @TableField("business_id")
    @Excel(name = "业务id")
    private Long businessId;

    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型")
    @Length(max = 32, message = "业务类型长度不能超过32")
    @TableField(value = "business_type", condition = LIKE)
    @Excel(name = "业务类型")
    private String businessType;

    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人")
    @Length(max = 32, message = "处理人长度不能超过32")
    @TableField(value = "handler", condition = LIKE)
    @Excel(name = "处理人")
    private String handler;

    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    @Length(max = 32, message = "流程实例id长度不能超过32")
    @TableField(value = "proc_inst_id", condition = LIKE)
    @Excel(name = "流程实例id")
    private String procInstId;

    /**
     * 执行任务id
     */
    @ApiModelProperty(value = "执行任务id")
    @Length(max = 32, message = "执行任务id长度不能超过32")
    @TableField(value = "task_id", condition = LIKE)
    @Excel(name = "执行任务id")
    private String taskId;

    /**
     * 审批结果
     */
    @ApiModelProperty(value = "审批结果")
    @TableField("approval_state")
    @Excel(name = "审批结果")
    private Integer approvalState;

    /**
     * 审批备注
     */
    @ApiModelProperty(value = "审批备注")
    @Length(max = 1024, message = "审批备注长度不能超过1024")
    @TableField(value = "approval_context", condition = LIKE)
    @Excel(name = "审批备注")
    private String approvalContext;


    @Builder
    public Approval(Long id, LocalDateTime createTime, Long createdBy, LocalDateTime updateTime, Long updatedBy,
                    Long businessId, String businessType, String handler, String procInstId, String taskId,
                    Integer approvalState, String approvalContext) {
        this.id = id;
        this.createTime = createTime;
        this.createdBy = createdBy;
        this.updateTime = updateTime;
        this.updatedBy = updatedBy;
        this.businessId = businessId;
        this.businessType = businessType;
        this.handler = handler;
        this.procInstId = procInstId;
        this.taskId = taskId;
        this.approvalState = approvalState;
        this.approvalContext = approvalContext;
    }

}
