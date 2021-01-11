package com.cp3.cloud.activiti.dto;

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
 * 审批
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
@ApiModel(value = "ApprovalSaveDTO", description = "审批")
public class ApprovalSaveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请假id
     */
    @ApiModelProperty(value = "请假id")
    private Long leaveId;
    /**
     * 流程id
     */
    @ApiModelProperty(value = "流程id")
    private Long flowId;
    /**
     * 流程运行实例id(act_ru_execution)
     */
    @ApiModelProperty(value = "流程运行实例id(act_ru_execution)")
    private Long procInstId;
    /**
     * 规则id
     */
    @ApiModelProperty(value = "规则id")
    private Long ruleId;
    /**
     * 审批结果
     */
    @ApiModelProperty(value = "审批结果")
    private Integer leaveState;
    @ApiModelProperty(value = "")
    private LocalDateTime createtime;
    @ApiModelProperty(value = "")
    private Long createBy;
    @ApiModelProperty(value = "")
    private Long updateBy;

}
