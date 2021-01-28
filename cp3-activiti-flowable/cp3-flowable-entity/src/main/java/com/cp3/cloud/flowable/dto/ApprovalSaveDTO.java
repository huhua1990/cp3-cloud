package com.cp3.cloud.flowable.dto;

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
 * @since 2021-01-25
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
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private Long businessId;
    /**
     * 业务类型
     */
    @ApiModelProperty(value = "业务类型")
    @Length(max = 32, message = "业务类型长度不能超过32")
    private String businessType;
    /**
     * 处理人
     */
    @ApiModelProperty(value = "处理人")
    @Length(max = 32, message = "处理人长度不能超过32")
    private String handler;
    /**
     * 流程实例id
     */
    @ApiModelProperty(value = "流程实例id")
    @Length(max = 32, message = "流程实例id长度不能超过32")
    private String procInstId;
    /**
     * 执行任务id
     */
    @ApiModelProperty(value = "执行任务id")
    @Length(max = 32, message = "执行任务id长度不能超过32")
    private String taskId;
    /**
     * 审批结果
     */
    @ApiModelProperty(value = "审批结果")
    private Integer approvalState;
    /**
     * 审批备注
     */
    @ApiModelProperty(value = "审批备注")
    @Length(max = 1024, message = "审批备注长度不能超过1024")
    private String approvalContext;

}
