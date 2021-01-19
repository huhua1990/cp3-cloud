package com.cp3.cloud.activiti.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.cp3.base.basic.entity.Entity;
import java.time.LocalDateTime;
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
 * @since 2021-01-11
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b_approval")
@ApiModel(value = "Approval", description = "审批")
@AllArgsConstructor
public class Approval extends Entity<Long> {

    private static final long serialVersionUID = 1L;

    /**
     * 请假id
     */
    @ApiModelProperty(value = "请假id")
    @TableField("leave_id")
    @Excel(name = "请假id")
    private Long leaveId;

    /**
     * 流程id
     */
    @ApiModelProperty(value = "流程id")
    @TableField("flow_id")
    @Excel(name = "流程id")
    private Long flowId;

    /**
     * 流程运行实例id(act_ru_execution)
     */
    @ApiModelProperty(value = "流程运行实例id(act_ru_execution)")
    @TableField("proc_inst_id")
    @Excel(name = "流程运行实例id(act_ru_execution)")
    private Long procInstId;

    /**
     * 规则id
     */
    @ApiModelProperty(value = "规则id")
    @TableField("rule_id")
    @Excel(name = "规则id")
    private Long ruleId;

    /**
     * 审批结果
     */
    @ApiModelProperty(value = "审批结果")
    @TableField("leave_state")
    @Excel(name = "审批结果")
    private Integer leaveState;

    @Builder
    public Approval(Long id, LocalDateTime updateTime,
                    Long leaveId, Long flowId, Long procInstId, Long ruleId, Integer leaveState,
                    LocalDateTime createtime, Long createdBy, Long updatedBy) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.leaveId = leaveId;
        this.flowId = flowId;
        this.procInstId = procInstId;
        this.ruleId = ruleId;
        this.leaveState = leaveState;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

}
