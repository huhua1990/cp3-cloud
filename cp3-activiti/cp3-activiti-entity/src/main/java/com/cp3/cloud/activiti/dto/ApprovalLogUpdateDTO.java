package com.cp3.cloud.activiti.dto;

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
import com.cp3.base.basic.entity.SuperEntity;
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
@ApiModel(value = "ApprovalLogUpdateDTO", description = "审批日志")
public class ApprovalLogUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @NotNull(message = "id不能为空", groups = SuperEntity.Update.class)
    private Long id;

    /**
     * 请假id
     */
    @ApiModelProperty(value = "请假id")
    private Long leaveId;
    /**
     * 实例任务id(act_ru_task)
     */
    @ApiModelProperty(value = "实例任务id(act_ru_task)")
    @Length(max = 32, message = "实例任务id(act_ru_task)长度不能超过32")
    private String taskId;
    /**
     * 实例任务key(act_ru_task)
     */
    @ApiModelProperty(value = "实例任务key(act_ru_task)")
    @Length(max = 32, message = "实例任务key(act_ru_task)长度不能超过32")
    private String taskKey;
    /**
     * 实例任务name(act_ru_task)
     */
    @ApiModelProperty(value = "实例任务name(act_ru_task)")
    @Length(max = 255, message = "实例任务name(act_ru_task)长度不能超过255")
    private String taskName;
    /**
     * 审批结果
     */
    @ApiModelProperty(value = "审批结果")
    @Length(max = 255, message = "审批结果长度不能超过255")
    private String approvalStatus;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @Length(max = 50, message = "操作人长度不能超过50")
    private String operId;
    /**
     * 操作内容
     */
    @ApiModelProperty(value = "操作内容")
    @Length(max = 255, message = "操作内容长度不能超过255")
    private String opervalue;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    @Length(max = 255, message = "备注长度不能超过255")
    private String remark;
    @ApiModelProperty(value = "")
    private Long createdBy;
    @ApiModelProperty(value = "")
    private Long updatedBy;
}
