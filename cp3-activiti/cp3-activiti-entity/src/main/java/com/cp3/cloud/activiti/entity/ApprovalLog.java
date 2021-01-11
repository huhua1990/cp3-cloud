package com.cp3.cloud.activiti.entity;

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
 * 审批日志
 * </p>
 *
 * @author cp3
 * @since 2021-01-11
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_approval_log")
@ApiModel(value = "ApprovalLog", description = "审批日志")
@AllArgsConstructor
public class ApprovalLog extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 请假id
     */
    @ApiModelProperty(value = "请假id")
    @TableField("leave_id")
    @Excel(name = "请假id")
    private Long leaveId;

    /**
     * 实例任务id(act_ru_task)
     */
    @ApiModelProperty(value = "实例任务id(act_ru_task)")
    @Length(max = 32, message = "实例任务id(act_ru_task)长度不能超过32")
    @TableField(value = "task_id", condition = LIKE)
    @Excel(name = "实例任务id(act_ru_task)")
    private String taskId;

    /**
     * 实例任务key(act_ru_task)
     */
    @ApiModelProperty(value = "实例任务key(act_ru_task)")
    @Length(max = 32, message = "实例任务key(act_ru_task)长度不能超过32")
    @TableField(value = "task_key", condition = LIKE)
    @Excel(name = "实例任务key(act_ru_task)")
    private String taskKey;

    /**
     * 实例任务name(act_ru_task)
     */
    @ApiModelProperty(value = "实例任务name(act_ru_task)")
    @Length(max = 255, message = "实例任务name(act_ru_task)长度不能超过255")
    @TableField(value = "task_name", condition = LIKE)
    @Excel(name = "实例任务name(act_ru_task)")
    private String taskName;

    /**
     * 审批结果
     */
    @ApiModelProperty(value = "审批结果")
    @Length(max = 255, message = "审批结果长度不能超过255")
    @TableField(value = "approval_status", condition = LIKE)
    @Excel(name = "审批结果")
    private String approvalStatus;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @Length(max = 50, message = "操作人长度不能超过50")
    @TableField(value = "oper_id", condition = LIKE)
    @Excel(name = "操作人")
    private String operId;

    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容")
    @Length(max = 255, message = "操作内容长度不能超过255")
    @TableField(value = "opervalue", condition = LIKE)
    @Excel(name = "操作内容")
    private String opervalue;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Length(max = 255, message = "备注长度不能超过255")
    @TableField(value = "remark", condition = LIKE)
    @Excel(name = "备注")
    private String remark;

    @ApiModelProperty(value = "")
    @TableField("create_by")
    @Excel(name = "")
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField("update_by")
    @Excel(name = "")
    private Long updateBy;


    @Builder
    public ApprovalLog(Long id, LocalDateTime createTime, LocalDateTime updateTime, 
                    Long leaveId, String taskId, String taskKey, String taskName, String approvalStatus, 
                    String operId, String opervalue, String remark, Long createBy, Long updateBy) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.leaveId = leaveId;
        this.taskId = taskId;
        this.taskKey = taskKey;
        this.taskName = taskName;
        this.approvalStatus = approvalStatus;
        this.operId = operId;
        this.opervalue = opervalue;
        this.remark = remark;
        this.createBy = createBy;
        this.updateBy = updateBy;
    }

}
